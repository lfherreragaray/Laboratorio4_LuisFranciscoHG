package mx.axity.dinosaurpark;

import mx.axity.dinosaurpark.model.Tourist;
import mx.axity.dinosaurpark.model.TouristStatus;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TouristTest {

    @Test
    void testInitialStatus() {
        Tourist t = new Tourist(1, "Ana");
        assertEquals(TouristStatus.WAITING, t.getStatus());
        assertEquals(0.0, t.getMoneySpent());
        assertTrue(t.getVisitedZones().isEmpty());
    }

    @Test
    void testSpend() {
        Tourist t = new Tourist(1, "Ana");
        t.spend(25.0);
        assertEquals(25.0, t.getMoneySpent());
        t.spend(10.0);
        assertEquals(35.0, t.getMoneySpent());
    }

    @Test
    void testRecordVisit() {
        Tourist t = new Tourist(1, "Ana");
        t.recordVisit("Zona A");
        assertEquals(1, t.getVisitedZones().size());
        assertEquals("Zona A", t.getVisitedZones().get(0));
    }

    @Test
    void testSetStatus() {
        Tourist t = new Tourist(1, "Ana");
        t.setStatus(TouristStatus.IN_PARK);
        assertEquals(TouristStatus.IN_PARK, t.getStatus());
    }
}