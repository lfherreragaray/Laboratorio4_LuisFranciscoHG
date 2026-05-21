package mx.axity.dinosaurpark.simulation;

import mx.axity.dinosaurpark.config.ParkConfig;
import mx.axity.dinosaurpark.model.*;
import mx.axity.dinosaurpark.monitoring.ParkMonitor;
import mx.axity.dinosaurpark.persistence.CsvWriter;
import mx.axity.dinosaurpark.persistence.ExpenseRecord;
import mx.axity.dinosaurpark.zone.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SimulationEngine {
    private final ParkConfig config;
    private final int totalSteps;
    private final int batchSize;
    private final long seed;
    private final Random rng;
    private final CsvWriter csvWriter;
    private final EventScheduler scheduler;
    private final ParkState state;
    private final List<Guard> guards;
    private final List<Technician> technicians;

    public SimulationEngine(ParkConfig config) {
        this.config = config;
        this.totalSteps = config.getTotalSteps();
        this.batchSize = config.getInt("simulation.arrivalBatchSize", 5);
        this.seed = config.getSeed();
        this.rng = new Random(seed);
        try {
            this.csvWriter = new CsvWriter(config.getString("output.directory", "output"));
        } catch (IOException e) {
            throw new RuntimeException("Failed to create CSV writer", e);
        }
        // Create tourists
        int touristCount = config.getInt("tourists", 50);
        List<Tourist> tourists = new ArrayList<>();
        for (int i = 1; i <= touristCount; i++) {
            tourists.add(new Tourist(i, "Turista " + i));
        }
        // Create dinosaurs
        List<Dinosaur> dinosaurs = new ArrayList<>();
        int carnivores = config.getInt("dinosaurs.carnivores", 5);
        int herbivores = config.getInt("dinosaurs.herbivores", 15);
        int id = 1;
        for (int i = 0; i < carnivores; i++) {
            dinosaurs.add(new CarnivoreDinosaur(id++, "Carnívoro " + i, "T-rex"));
        }
        for (int i = 0; i < herbivores; i++) {
            dinosaurs.add(new HerbivoreDinosaur(id++, "Herbívoro " + i, "Triceratops"));
        }
        // Create workers
        List<Worker> workers = new ArrayList<>();
        int guardCount = config.getInt("workers.guards", 3);
        int techCount = config.getInt("workers.technicians", 2);
        double dailySalary = config.getDouble("workers.dailySalary", 150.0);
        guards = new ArrayList<>();
        technicians = new ArrayList<>();
        for (int i = 1; i <= guardCount; i++) {
            Guard g = new Guard(i, "Guardia " + i, dailySalary);
            guards.add(g);
            workers.add(g);
        }
        for (int i = 1; i <= techCount; i++) {
            Technician t = new Technician(guardCount + i, "Técnico " + i, dailySalary);
            technicians.add(t);
            workers.add(t);
        }
        // Create zones
        ArrivalZone arrival = new ArrivalZone(
                config.getInt("arrival.maxCapacity", 30),
                config.getDouble("arrival.ticketPrice", 25.0)
        );
        CentralHub hub = new CentralHub(
                config.getDouble("hub.souvenirPrice", 15.0),
                config.getDouble("hub.souvenirPurchaseProbability", 0.4)
        );
        BathroomZone bathroom = new BathroomZone(
                config.getInt("bathroom.maxCapacity", 10),
                config.getInt("bathroom.useDurationSteps", 3),
                config.getDouble("bathroom.spaPrice", 20.0),
                config.getDouble("bathroom.spaPurchaseProbability", 0.2)
        );
        PowerPlant plant = new PowerPlant(
                config.getDouble("powerplant.initialEnergy", 100.0),
                config.getDouble("powerplant.consumptionPerStep", 1.5),
                config.getDouble("powerplant.failureProbability", 0.05),
                config.getDouble("powerplant.maintenanceCost", 200.0),
                config.getDouble("powerplant.repairCost", 500.0)
        );
        ObservationEnclosure enclosure = new ObservationEnclosure(
                "Encierro Principal", ExperienceType.BASIC,
                config.getInt("enclosure.basic.maxVisitors", 20),
                config.getDouble("enclosure.basic.entryFee", 10.0),
                1, 3
        );
        // Add tourists to arrival queue
        for (Tourist t : tourists) {
            arrival.addToQueue(t);
        }
        this.scheduler = new EventScheduler(seed, totalSteps);
        this.state = new ParkState(tourists, dinosaurs, workers, arrival, hub, bathroom, plant, enclosure, csvWriter, rng);
    }

    public void run() {
        for (int step = 0; step < totalSteps; step++) {
            state.incrementStep();
            // A. Arrivals
            List<Tourist> arrived = state.getArrivalZone().processBatch(batchSize, csvWriter);
            for (Tourist t : arrived) {
                state.addRevenue(config.getDouble("arrival.ticketPrice", 25.0));
            }
            // B. Movement of active tourists
            List<Tourist> active = state.getActiveTourists();
            for (Tourist t : active) {
                state.getCentralHub().visit(t, rng, csvWriter);
                state.getBathroomZone().tryEnter(t, rng, csvWriter);
                state.getEnclosure().visit(t, rng, csvWriter);
            }
            // C. Ticks
            state.getBathroomZone().tick();
            state.getPowerPlant().tick(rng, csvWriter);
            // D. Event
            scheduler.checkForEvent(step).ifPresent(e -> e.execute(state, rng));
            // E. Workers
            for (Guard g : guards) {
                g.recaptureEscapedDinosaurs(state.getDinosaurs());
            }
            for (Technician t : technicians) {
                t.repairIfNeeded(state.getPowerPlant());
                if (!state.getPowerPlant().isOperational()) {
                    // repair cost
                    double cost = state.getPowerPlant().getRepairCost();
                    state.addExpense(cost);
                    ExpenseRecord record = new ExpenseRecord(
                            System.currentTimeMillis(), "REPAIR", cost,
                            "Reparación de planta", LocalDateTime.now()
                    );
                    csvWriter.appendExpense(record);
                }
            }
            // Pay salaries
            double salaryCost = state.getWorkers().stream().mapToDouble(Worker::getDailySalary).sum();
            state.addExpense(salaryCost);
            ExpenseRecord salaryRecord = new ExpenseRecord(
                    System.currentTimeMillis(), "SALARY", salaryCost,
                    "Salario diario", LocalDateTime.now()
            );
            csvWriter.appendExpense(salaryRecord);
            // F. Monitor
            ParkMonitor.displaySnapshot(state);
        }
        // final summary
        System.out.println("\n=== SIMULACIÓN FINALIZADA ===");
        System.out.printf("Ingresos totales: %.2f\n", state.getTotalRevenue());
        System.out.printf("Gastos totales: %.2f\n", state.getTotalExpenses());
        System.out.printf("Beneficio: %.2f\n", state.getTotalRevenue() - state.getTotalExpenses());
    }
}