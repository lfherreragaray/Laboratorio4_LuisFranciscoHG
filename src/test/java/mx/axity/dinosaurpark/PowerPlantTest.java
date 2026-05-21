package mx.axity.dinosaurpark;

import mx.axity.dinosaurpark.persistence.CsvWriter;
import mx.axity.dinosaurpark.zone.PowerPlant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class PowerPlantTest {
    private PowerPlant plant;
    private CsvWriter mockCsv;
    private Random rng;

    @BeforeEach
    void setUp() {
        plant = new PowerPlant(100.0, 1.5, 0.05, 200.0, 500.0);
        mockCsv = Mockito.mock(CsvWriter.class);
        rng = new Random(1);
    }

    @Test
    void testInitialOperational() {
        assertTrue(plant.isOperational());
        assertEquals(100.0, plant.getEnergy());
    }

    @Test
    void testTriggerFailure() {
        plant.triggerFailure(mockCsv);
        assertFalse(plant.isOperational());
        assertEquals(0.0, plant.getEnergy());
        Mockito.verify(mockCsv).appendExpense(Mockito.any());
    }

    @Test
    void testRepair() {
        plant.triggerFailure(mockCsv);
        plant.repair();
        assertTrue(plant.isOperational());
        assertEquals(100.0, plant.getEnergy());
    }

    @Test
    void testEnergyNeverNegative() {
        for (int i = 0; i < 100; i++) {
            plant.tick(rng, mockCsv);
        }
        assertTrue(plant.getEnergy() >= 0);
    }
}