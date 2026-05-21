package mx.axity.dinosaurpark.zone;

import mx.axity.dinosaurpark.model.Tourist;
import mx.axity.dinosaurpark.persistence.CsvWriter;
import mx.axity.dinosaurpark.persistence.RevenueRecord;

import java.time.LocalDateTime;
import java.util.Random;

public class CentralHub implements ParkZone {
    private final String name = "CentralHub";
    private final int maxCapacity = 100; // large enough, not enforced
    private int currentOccupancy = 0;
    private final double souvenirPrice;
    private final double purchaseProbability;

    public CentralHub(double souvenirPrice, double purchaseProbability) {
        this.souvenirPrice = souvenirPrice;
        this.purchaseProbability = purchaseProbability;
    }

    @Override
    public String getName() { return name; }
    @Override
    public boolean hasCapacity() { return true; }
    @Override
    public int getCurrentOccupancy() { return currentOccupancy; }
    @Override
    public int getMaxCapacity() { return maxCapacity; }
    @Override
    public void enter(Tourist tourist) { currentOccupancy++; }
    @Override
    public void exit(Tourist tourist) { currentOccupancy--; }

    public void visit(Tourist tourist, Random rng, CsvWriter csvWriter) {
        if (tourist.getStatus() != mx.axity.dinosaurpark.model.TouristStatus.IN_PARK) return;
        enter(tourist);
        if (rng.nextDouble() < purchaseProbability) {
            tourist.spend(souvenirPrice);
            RevenueRecord record = new RevenueRecord(
                    System.currentTimeMillis(), "SOUVENIR", souvenirPrice,
                    tourist.getId(), getName(), LocalDateTime.now()
            );
            csvWriter.appendRevenue(record);
        }
        tourist.recordVisit(getName());
        exit(tourist);
    }
}