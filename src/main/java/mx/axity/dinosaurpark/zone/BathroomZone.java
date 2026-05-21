
package mx.axity.dinosaurpark.zone;

import mx.axity.dinosaurpark.model.Tourist;
import mx.axity.dinosaurpark.model.TouristStatus;
import mx.axity.dinosaurpark.persistence.CsvWriter;
import mx.axity.dinosaurpark.persistence.RevenueRecord;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Random;

public class BathroomZone implements ParkZone {
    private final String name = "Bathroom";
    private final int maxCapacity;
    private final int useDurationSteps;
    private final double spaPrice;
    private final double spaProbability;
    private final int[] slots; // remaining steps of use, 0 = free
    private int currentOccupancy = 0;

    public BathroomZone(int maxCapacity, int useDurationSteps, double spaPrice, double spaProbability) {
        this.maxCapacity = maxCapacity;
        this.useDurationSteps = useDurationSteps;
        this.spaPrice = spaPrice;
        this.spaProbability = spaProbability;
        this.slots = new int[maxCapacity];
        Arrays.fill(slots, 0);
    }

    @Override
    public String getName() { return name; }
    @Override
    public boolean hasCapacity() { return currentOccupancy < maxCapacity; }
    @Override
    public int getCurrentOccupancy() { return currentOccupancy; }
    @Override
    public int getMaxCapacity() { return maxCapacity; }
    @Override
    public void enter(Tourist tourist) { /* not used directly */ }
    @Override
    public void exit(Tourist tourist) { /* not used directly */ }

    public void tryEnter(Tourist tourist, Random rng, CsvWriter csvWriter) {
        if (tourist.getStatus() != TouristStatus.IN_PARK) return;
        for (int i = 0; i < slots.length; i++) {
            if (slots[i] == 0) {
                slots[i] = useDurationSteps;
                currentOccupancy++;
                tourist.recordVisit(getName());
                // Spa purchase chance
                if (rng.nextDouble() < spaProbability) {
                    tourist.spend(spaPrice);
                    RevenueRecord record = new RevenueRecord(
                            System.currentTimeMillis(), "SPA", spaPrice,
                            tourist.getId(), getName(), LocalDateTime.now()
                    );
                    csvWriter.appendRevenue(record);
                }
                return;
            }
        }
        // no free slot, tourist does nothing
    }

    public void tick() {
        for (int i = 0; i < slots.length; i++) {
            if (slots[i] > 0) {
                slots[i]--;
                if (slots[i] == 0) {
                    currentOccupancy--;
                }
            }
        }
    }
}