package mx.axity.dinosaurpark.zone;
import mx.axity.dinosaurpark.model.Tourist;
import mx.axity.dinosaurpark.model.TouristStatus;
import mx.axity.dinosaurpark.persistence.CsvWriter;
import mx.axity.dinosaurpark.persistence.RevenueRecord;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.ArrayList;

public class ArrivalZone implements ParkZone {
    private final String name = "Arrival";
    private final int maxCapacity;
    private final double ticketPrice;
    private final Queue<Tourist> waitingQueue = new LinkedList<>();
    private int currentOccupancy = 0;

    public ArrivalZone(int maxCapacity, double ticketPrice) {
        this.maxCapacity = maxCapacity;
        this.ticketPrice = ticketPrice;
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
    public void enter(Tourist tourist) {
        if (hasCapacity()) {
            currentOccupancy++;
        } else {
            waitingQueue.add(tourist);
        }
    }

    @Override
    public void exit(Tourist tourist) {
        if (currentOccupancy > 0) {
            currentOccupancy--;
        }
    }

    public void addToQueue(Tourist tourist) {
        waitingQueue.add(tourist);
    }

    public List<Tourist> processBatch(int batchSize, CsvWriter csvWriter) {
        List<Tourist> arrived = new ArrayList<>();
        int toProcess = Math.min(batchSize, waitingQueue.size());
        for (int i = 0; i < toProcess; i++) {
            Tourist t = waitingQueue.poll();
            if (t != null && hasCapacity()) {
                t.setStatus(TouristStatus.IN_PARK);
                t.spend(ticketPrice);
                currentOccupancy++;
                arrived.add(t);
                // register revenue
                RevenueRecord record = new RevenueRecord(
                        System.currentTimeMillis(), "TICKET", ticketPrice,
                        t.getId(), getName(), LocalDateTime.now()
                );
                csvWriter.appendRevenue(record);
            } else if (t != null) {
                // put back if no capacity? For simplicity we keep in queue
                waitingQueue.add(t);
                break;
            }
        }
        return arrived;
    }

    public void reset() {
        currentOccupancy = 0;
        waitingQueue.clear();
    }
}