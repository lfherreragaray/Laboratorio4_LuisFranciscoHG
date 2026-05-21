package mx.axity.dinosaurpark.event;

import mx.axity.dinosaurpark.model.Dinosaur;
import mx.axity.dinosaurpark.model.DinosaurStatus;
import mx.axity.dinosaurpark.model.Tourist;
import mx.axity.dinosaurpark.model.TouristStatus;
import mx.axity.dinosaurpark.persistence.EventRecord;
import mx.axity.dinosaurpark.simulation.ParkState;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class DinosaurEscapeEvent implements SimulationEvent {
    @Override
    public String getName() { return "ESCAPE_DINOSAURIO"; }

    @Override
    public String getDescription() { return "Un dinosaurio escapa del encierro y puede atacar a un turista."; }

    @Override
    public void execute(ParkState state, Random rng) {
        List<Dinosaur> inside = state.getDinosaurs().stream()
                .filter(d -> d.getStatus() == DinosaurStatus.IN_ENCLOSURE)
                .collect(Collectors.toList());
        if (inside.isEmpty()) return;
        Dinosaur d = inside.get(rng.nextInt(inside.size()));
        d.escape();
        String affected = "Dinosaurio " + d.getId();
        // Attack a tourist?
        if (rng.nextDouble() < d.getDangerLevel()) {
            List<Tourist> active = state.getActiveTourists();
            if (!active.isEmpty()) {
                Tourist t = active.get(rng.nextInt(active.size()));
                t.setStatus(TouristStatus.ATTACKED);
                affected += ", Turista " + t.getId();
            }
        }
        // record event
        EventRecord record = toRecord(state.getCurrentStep());
        state.getCsvWriter().appendEvent(record);
    }

    @Override
    public EventRecord toRecord(long step) {
        return new EventRecord(step, getName(), getDescription(), "Dinosaurio escapado", LocalDateTime.now());
    }
}