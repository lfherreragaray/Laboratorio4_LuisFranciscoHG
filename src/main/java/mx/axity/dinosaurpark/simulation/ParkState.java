package mx.axity.dinosaurpark.simulation;

import mx.axity.dinosaurpark.model.*;
import mx.axity.dinosaurpark.persistence.CsvWriter;
import mx.axity.dinosaurpark.zone.*;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class ParkState {
    private final List<Tourist> allTourists;
    private final List<Dinosaur> dinosaurs;
    private final List<Worker> workers;
    private final ArrivalZone arrivalZone;
    private final CentralHub centralHub;
    private final BathroomZone bathroomZone;
    private final PowerPlant powerPlant;
    private final ObservationEnclosure enclosure;
    private final CsvWriter csvWriter;
    private final Random rng;
    private double totalRevenue = 0.0;
    private double totalExpenses = 0.0;
    private int currentStep = 0;

    public ParkState(List<Tourist> allTourists, List<Dinosaur> dinosaurs, List<Worker> workers,
                     ArrivalZone arrivalZone, CentralHub centralHub, BathroomZone bathroomZone,
                     PowerPlant powerPlant, ObservationEnclosure enclosure,
                     CsvWriter csvWriter, Random rng) {
        this.allTourists = allTourists;
        this.dinosaurs = dinosaurs;
        this.workers = workers;
        this.arrivalZone = arrivalZone;
        this.centralHub = centralHub;
        this.bathroomZone = bathroomZone;
        this.powerPlant = powerPlant;
        this.enclosure = enclosure;
        this.csvWriter = csvWriter;
        this.rng = rng;
    }

    public int countActiveTourists() {
        return (int) allTourists.stream().filter(t -> t.getStatus() == TouristStatus.IN_PARK).count();
    }

    public int countDinosaursInEnclosure() {
        return (int) dinosaurs.stream().filter(d -> d.getStatus() == DinosaurStatus.IN_ENCLOSURE).count();
    }

    public double getEnergyPercentage() {
        return powerPlant.getEnergyPercentage();
    }

    public List<Tourist> getActiveTourists() {
        return allTourists.stream().filter(t -> t.getStatus() == TouristStatus.IN_PARK).collect(Collectors.toList());
    }

    // Getters
    public List<Tourist> getAllTourists() { return allTourists; }
    public List<Dinosaur> getDinosaurs() { return dinosaurs; }
    public List<Worker> getWorkers() { return workers; }
    public ArrivalZone getArrivalZone() { return arrivalZone; }
    public CentralHub getCentralHub() { return centralHub; }
    public BathroomZone getBathroomZone() { return bathroomZone; }
    public PowerPlant getPowerPlant() { return powerPlant; }
    public ObservationEnclosure getEnclosure() { return enclosure; }
    public CsvWriter getCsvWriter() { return csvWriter; }
    public Random getRng() { return rng; }
    public double getTotalRevenue() { return totalRevenue; }
    public void addRevenue(double amount) { this.totalRevenue += amount; }
    public double getTotalExpenses() { return totalExpenses; }
    public void addExpense(double amount) { this.totalExpenses += amount; }
    public int getCurrentStep() { return currentStep; }
    public void incrementStep() { currentStep++; }
}