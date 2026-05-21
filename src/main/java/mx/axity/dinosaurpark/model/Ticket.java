package mx.axity.dinosaurpark.model;


import java.time.LocalDateTime;

public record Ticket(long id, int touristId, double price, String category, LocalDateTime issuedAt) {
}