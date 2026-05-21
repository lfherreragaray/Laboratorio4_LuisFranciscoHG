package mx.axity.dinosaurpark.zone;

import mx.axity.dinosaurpark.persistence.CsvWriter;
import mx.axity.dinosaurpark.persistence.ExpenseRecord;
import mx.axity.dinosaurpark.model.Tourist;
import java.time.LocalDateTime;
import java.util.Random;

public class PowerPlant implements ParkZone {
    private final String name = "PowerPlant";
    private double energy;
    private boolean operational;
    private final double initialEnergy;
    private final double consumptionPerStep;
    private final double failureProbability;
    private final double maintenanceCost;
    private final double repairCost;

    public PowerPlant(double initialEnergy, double consumptionPerStep, double failureProbability,
                      double maintenanceCost, double repairCost) {
        this.initialEnergy = initialEnergy;
        this.energy = initialEnergy;
        this.operational = true;
        this.consumptionPerStep = consumptionPerStep;
        this.failureProbability = failureProbability;
        this.maintenanceCost = maintenanceCost;
        this.repairCost = repairCost;
    }

    @Override
    public String getName() { return name; }
    @Override
    public boolean hasCapacity() { return true; }
    @Override
    public int getCurrentOccupancy() { return 0; }
    @Override
    public int getMaxCapacity() { return 0; }
    @Override
    public void enter(Tourist tourist) {}
    @Override
    public void exit(Tourist tourist) {}

    public void tick(Random rng, CsvWriter csvWriter) {
        if (!operational) return;
        energy -= consumptionPerStep;
        if (energy < 0) energy = 0;
        if (energy <= 0) {
            triggerFailure(csvWriter);
        } else if (rng.nextDouble() < failureProbability) {
            triggerFailure(csvWriter);
        }
    }

    public void triggerFailure(CsvWriter csvWriter) {
        operational = false;
        energy = 0;
        ExpenseRecord expense = new ExpenseRecord(
                System.currentTimeMillis(), "BREAKDOWN", maintenanceCost,
                "Power plant failure", LocalDateTime.now()
        );
        csvWriter.appendExpense(expense);
    }

    public void repair() {
        if (!operational) {
            operational = true;
            energy = initialEnergy;
            // repair cost is recorded elsewhere (SimulationEngine)
        }
    }

    public boolean isOperational() { return operational; }
    public double getEnergy() { return energy; }
    public double getEnergyPercentage() { return (energy / initialEnergy) * 100; }
    public double getRepairCost() { return repairCost; }
}