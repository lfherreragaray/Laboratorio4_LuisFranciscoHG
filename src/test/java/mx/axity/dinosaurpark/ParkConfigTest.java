package mx.axity.dinosaurpark;

import mx.axity.dinosaurpark.config.ParkConfig;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ParkConfigTest {

    @Test
    void testSingleton() {
        ParkConfig config1 = ParkConfig.getInstance();
        ParkConfig config2 = ParkConfig.getInstance();
        assertSame(config1, config2);
    }

    @Test
    void testGetInt() {
        ParkConfig config = ParkConfig.getInstance();
        assertEquals(50, config.getInt("tourists", 0));
        assertEquals(999, config.getInt("non.existent", 999));
    }

    @Test
    void testGetDouble() {
        ParkConfig config = ParkConfig.getInstance();
        assertEquals(25.0, config.getDouble("arrival.ticketPrice", 0.0));
    }

    @Test
    void testGetSeed() {
        ParkConfig config = ParkConfig.getInstance();
        assertEquals(42L, config.getSeed());
    }

    @Test
    void testResetForTesting() {
        ParkConfig.resetForTesting();
        ParkConfig config = ParkConfig.getInstance();
        assertNotNull(config);
    }
}