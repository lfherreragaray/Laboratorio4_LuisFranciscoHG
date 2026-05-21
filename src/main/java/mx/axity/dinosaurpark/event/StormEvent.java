package mx.axity.dinosaurpark.event;

import mx.axity.dinosaurpark.model.Tourist;
import mx.axity.dinosaurpark.model.TouristStatus;
import mx.axity.dinosaurpark.persistence.EventRecord;
import mx.axity.dinosaurpark.persistence.ExpenseRecord;
import mx.axity.dinosaurpark.simulation.ParkState;

import java.time.LocalDateTime;
import java.util.Random;

public class StormEvent implements SimulationEvent {
    @Override
    public String getName() { return "TORMENTA_TORRENCIAL"; }

    @Override
    public String getDescription() { return "Una tormenta torrencial obliga a evacuar a todos los turistas."; }

    @Override
    public void execute(ParkState state, Random rng) {
        for (Tourist t : state.getAllTourists()) {
            if (t.getStatus() == TouristStatus.IN_PARK) {
                t.recordVisit("Evacuación");
            }
        }
        ExpenseRecord expense = new ExpenseRecord(
                System.currentTimeMillis(), "EVENT_STORM", 500.0,
                "Costo por tormenta", LocalDateTime.now()
        );
        state.getCsvWriter().appendExpense(expense);
        state.getCsvWriter().appendEvent(toRecord(state.getCurrentStep()));
    }

    @Override
    public EventRecord toRecord(long step) {
        return new EventRecord(step, getName(), getDescription(), "Todos los turistas", LocalDateTime.now());
    }
}