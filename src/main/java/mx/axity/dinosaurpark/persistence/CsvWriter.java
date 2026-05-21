package mx.axity.dinosaurpark.persistence;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class CsvWriter {
    private final Path revenuePath;
    private final Path expensePath;
    private final Path eventPath;

    public CsvWriter(String outputDir) throws IOException {
        Path dir = Paths.get(outputDir);
        if (!Files.exists(dir)) {
            Files.createDirectories(dir);
        }
        revenuePath = dir.resolve("revenues.csv");
        expensePath = dir.resolve("expenses.csv");
        eventPath = dir.resolve("events.csv");

        // Overwrite files with headers
        try (PrintWriter pw = new PrintWriter(new FileWriter(revenuePath.toFile(), false))) {
            pw.println("id,type,amount,touristId,zone,timestamp");
        }
        try (PrintWriter pw = new PrintWriter(new FileWriter(expensePath.toFile(), false))) {
            pw.println("id,type,amount,description,timestamp");
        }
        try (PrintWriter pw = new PrintWriter(new FileWriter(eventPath.toFile(), false))) {
            pw.println("step,eventName,description,affectedEntities,timestamp");
        }
    }

    public synchronized void appendRevenue(RevenueRecord record) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(revenuePath.toFile(), true))) {
            pw.println(record.toCsvLine());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void appendExpense(ExpenseRecord record) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(expensePath.toFile(), true))) {
            pw.println(record.toCsvLine());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void appendEvent(EventRecord record) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(eventPath.toFile(), true))) {
            pw.println(record.toCsvLine());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}