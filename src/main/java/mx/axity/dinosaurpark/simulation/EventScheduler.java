package mx.axity.dinosaurpark.simulation;

import mx.axity.dinosaurpark.event.BlackoutEvent;
import mx.axity.dinosaurpark.event.DinosaurEscapeEvent;
import mx.axity.dinosaurpark.event.SimulationEvent;
import mx.axity.dinosaurpark.event.StormEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

public class EventScheduler {
    private final Map<Integer, SimulationEvent> scheduledEvents = new HashMap<>();

    public EventScheduler(long seed, int totalSteps) {
        Random rng = new Random(seed);
        // schedule three events at random steps (could be same step, but we keep one per step in basic)
        int escapeStep = rng.nextInt(totalSteps);
        int blackoutStep = rng.nextInt(totalSteps);
        int stormStep = rng.nextInt(totalSteps);
        scheduledEvents.put(escapeStep, new DinosaurEscapeEvent());
        scheduledEvents.put(blackoutStep, new BlackoutEvent());
        scheduledEvents.put(stormStep, new StormEvent());
    }

    public Optional<SimulationEvent> checkForEvent(int step) {
        return Optional.ofNullable(scheduledEvents.get(step));
    }
}