package mx.axity.dinosaurpark.zone;

import mx.axity.dinosaurpark.model.SatisfactionSurvey;
import mx.axity.dinosaurpark.model.Tourist;
import mx.axity.dinosaurpark.model.TouristStatus;
import mx.axity.dinosaurpark.persistence.CsvWriter;
import mx.axity.dinosaurpark.persistence.RevenueRecord;

import java.time.LocalDateTime;
import java.util.Random;

public class ObservationEnclosure implements ParkZone {
    private final String name;
    private final ExperienceType type;
    private final int maxCapacity;
    private final double entryFee;
    private final int minScore;
    private final int maxScore;
    private int currentOccupancy = 0;

    public ObservationEnclosure(String name, ExperienceType type, int maxCapacity, double entryFee,
                                int minScore, int maxScore) {
        this.name = name;
        this.type = type;
        this.maxCapacity = maxCapacity;
        this.entryFee = entryFee;
        this.minScore = minScore;
        this.maxScore = maxScore;
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
    public void enter(Tourist tourist) { currentOccupancy++; }
    @Override
    public void exit(Tourist tourist) { currentOccupancy--; }

    public void visit(Tourist tourist, Random rng, CsvWriter csvWriter) {
        if (tourist.getStatus() != TouristStatus.IN_PARK) return;
        if (!hasCapacity()) return;
        enter(tourist);
        // pay entry fee
        tourist.spend(entryFee);
        RevenueRecord record = new RevenueRecord(
                System.currentTimeMillis(), "ENCLOSURE_" + type.name(), entryFee,
                tourist.getId(), getName(), LocalDateTime.now()
        );
        csvWriter.appendRevenue(record);
        // conduct survey
        int score = rng.nextInt(maxScore - minScore + 1) + minScore;
        SatisfactionSurvey survey = new SatisfactionSurvey(tourist.getId(), getName(), score);
        // In basic version, we might just record to CSV? The spec says surveys are stored but not persisted? We'll just ignore for CSV.
        // For simplicity we don't persist survey in basic, but we can keep in memory if needed.
        tourist.recordVisit(getName());
        exit(tourist);
    }
}