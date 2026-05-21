package mx.axity.dinosaurpark.event;

import mx.axity.dinosaurpark.persistence.EventRecord;
import mx.axity.dinosaurpark.simulation.ParkState;

import java.util.Random;

public interface SimulationEvent {
    String getName();
    String getDescription();
    void execute(ParkState state, Random rng);
    EventRecord toRecord(long step);
}