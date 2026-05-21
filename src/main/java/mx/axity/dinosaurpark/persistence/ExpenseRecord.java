package mx.axity.dinosaurpark.persistence;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public record ExpenseRecord(long id, String type, double amount, String description, LocalDateTime timestamp) {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    public String toCsvLine() {
        return String.format("%d,%s,%.2f,%s,%s",
                id, type, amount, description, timestamp.format(FORMATTER));
    }
}