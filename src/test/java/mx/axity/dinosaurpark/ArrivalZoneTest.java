package mx.axity.dinosaurpark;

import mx.axity.dinosaurpark.model.Tourist;
import mx.axity.dinosaurpark.model.TouristStatus;
import mx.axity.dinosaurpark.persistence.CsvWriter;
import mx.axity.dinosaurpark.zone.ArrivalZone;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ArrivalZoneTest {
    private ArrivalZone zone;
    private CsvWriter mockCsv;

    @BeforeEach
    void setUp() throws IOException {
        zone = new ArrivalZone(10, 25.0);
        mockCsv = Mockito.mock(CsvWriter.class);
    }

    @Test
    void testTouristEntersAndStatusChanges() {
        Tourist t = new Tourist(1, "Ana");
        zone.addToQueue(t);
        List<Tourist> arrived = zone.processBatch(1, mockCsv);
        assertEquals(1, arrived.size());
        assertEquals(TouristStatus.IN_PARK, t.getStatus());
        assertEquals(25.0, t.getMoneySpent());
        assertEquals(1, zone.getCurrentOccupancy());
    }

    @Test
    void testMaxCapacityRespected() {
        for (int i = 0; i < 12; i++) {
            zone.addToQueue(new Tourist(i, "T" + i));
        }
        List<Tourist> arrived = zone.processBatch(20, mockCsv);
        assertEquals(10, arrived.size());
        assertEquals(10, zone.getCurrentOccupancy());
        // remaining 2 still in queue
        arrived = zone.processBatch(20, mockCsv);
        assertEquals(0, arrived.size());
    }

    @Test
    void testEmptyQueue() {
        List<Tourist> arrived = zone.processBatch(5, mockCsv);
        assertTrue(arrived.isEmpty());
    }
}