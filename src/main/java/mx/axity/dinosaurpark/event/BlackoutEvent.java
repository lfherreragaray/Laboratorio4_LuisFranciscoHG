package mx.axity.dinosaurpark.event;

import mx.axity.dinosaurpark.persistence.EventRecord;
import mx.axity.dinosaurpark.persistence.ExpenseRecord;
import mx.axity.dinosaurpark.simulation.ParkState;
import mx.axity.dinosaurpark.zone.PowerPlant;

import java.time.LocalDateTime;
import java.util.Random;

public class BlackoutEvent implements SimulationEvent {
    @Override
    public String getName() { return "APAGON_MASIVO"; }

    @Override
    public String getDescription() { return "Un apagón masivo deja la planta eléctrica fuera de servicio."; }

    @Override
    public void execute(ParkState state, Random rng) {
        PowerPlant plant = state.getPowerPlant();
        plant.triggerFailure(state.getCsvWriter());
        // additional expense
        ExpenseRecord expense = new ExpenseRecord(
                System.currentTimeMillis(), "EVENT_BLACKOUT", 2000.0,
                "Costo adicional por apagón", LocalDateTime.now()
        );
        state.getCsvWriter().appendExpense(expense);
        state.getCsvWriter().appendEvent(toRecord(state.getCurrentStep()));
    }

    @Override
    public EventRecord toRecord(long step) {
        return new EventRecord(step, getName(), getDescription(), "Planta eléctrica", LocalDateTime.now());
    }
}