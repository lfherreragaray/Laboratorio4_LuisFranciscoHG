package mx.axity.dinosaurpark;

import mx.axity.dinosaurpark.event.BlackoutEvent;
import mx.axity.dinosaurpark.persistence.CsvWriter;
import mx.axity.dinosaurpark.simulation.ParkState;
import mx.axity.dinosaurpark.zone.PowerPlant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class BlackoutEventTest {
    private BlackoutEvent event;
    private PowerPlant plant;
    private ParkState state;
    private CsvWriter mockCsv;

    @BeforeEach
    void setUp() {
        event = new BlackoutEvent();
        plant = new PowerPlant(100.0, 1.5, 0.05, 200.0, 500.0);
        mockCsv = Mockito.mock(CsvWriter.class);
        state = Mockito.mock(ParkState.class);
        Mockito.when(state.getPowerPlant()).thenReturn(plant);
        Mockito.when(state.getCsvWriter()).thenReturn(mockCsv);
        Mockito.when(state.getCurrentStep()).thenReturn(5);
    }

    @Test
    void testExecuteMakesPlantNotOperational() {
        event.execute(state, new Random());
        assertFalse(plant.isOperational());
        assertEquals(0.0, plant.getEnergy());
        Mockito.verify(mockCsv, Mockito.atLeastOnce()).appendExpense(Mockito.any());
    }
}