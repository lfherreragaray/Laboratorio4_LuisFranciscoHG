package mx.axity.dinosaurpark;

import mx.axity.dinosaurpark.event.DinosaurEscapeEvent;
import mx.axity.dinosaurpark.model.*;
import mx.axity.dinosaurpark.persistence.CsvWriter;
import mx.axity.dinosaurpark.simulation.ParkState;
import mx.axity.dinosaurpark.zone.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class DinosaurEscapeEventTest {
    private DinosaurEscapeEvent event;
    private ParkState state;
    private Random rng;
    private List<Dinosaur> dinosaurs;

    @BeforeEach
    void setUp() {
        event = new DinosaurEscapeEvent();
        dinosaurs = List.of(new CarnivoreDinosaur(1, "Rex", "T-rex"));
        CsvWriter mockCsv = Mockito.mock(CsvWriter.class);
        state = Mockito.mock(ParkState.class);
        Mockito.when(state.getDinosaurs()).thenReturn(dinosaurs);
        Mockito.when(state.getCsvWriter()).thenReturn(mockCsv);
        Mockito.when(state.getCurrentStep()).thenReturn(5);
        rng = new Random(1);
    }

    @Test
    void testExecuteEscapesDinosaur() {
        event.execute(state, rng);
        assertEquals(DinosaurStatus.ESCAPED, dinosaurs.get(0).getStatus());
        Mockito.verify(state.getCsvWriter()).appendEvent(Mockito.any());
    }

    @Test
    void testNoDinosaursDoesNothing() {
        Mockito.when(state.getDinosaurs()).thenReturn(List.of());
        assertDoesNotThrow(() -> event.execute(state, rng));
    }
}