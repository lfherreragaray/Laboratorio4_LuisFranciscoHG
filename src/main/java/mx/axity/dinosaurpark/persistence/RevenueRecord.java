package mx.axity.dinosaurpark.persistence;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public record RevenueRecord(long id, String type, double amount, int touristId, String zone, LocalDateTime timestamp) {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    public String toCsvLine() {
        return String.format("%d,%s,%.2f,%d,%s,%s",
                id, type, amount, touristId, zone, timestamp.format(FORMATTER));
    }
}