package mx.axity.dinosaurpark.persistence;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public record EventRecord(long step, String eventName, String description, String affectedEntities, LocalDateTime timestamp) {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    public String toCsvLine() {
        return String.format("%d,%s,%s,%s,%s",
                step, eventName, description, affectedEntities, timestamp.format(FORMATTER));
    }
}