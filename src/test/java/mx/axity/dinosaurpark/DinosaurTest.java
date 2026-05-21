package mx.axity.dinosaurpark;

import mx.axity.dinosaurpark.model.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DinosaurTest {

    @Test
    void testInitialStatus() {
        Dinosaur d = new CarnivoreDinosaur(1, "Rex", "T-rex");
        assertEquals(DinosaurStatus.IN_ENCLOSURE, d.getStatus());
    }

    @Test
    void testEscapeAndRecapture() {
        Dinosaur d = new HerbivoreDinosaur(2, "Tri", "Triceratops");
        d.escape();
        assertEquals(DinosaurStatus.ESCAPED, d.getStatus());
        d.returnToEnclosure();
        assertEquals(DinosaurStatus.IN_ENCLOSURE, d.getStatus());
    }

    @Test
    void testDangerLevels() {
        CarnivoreDinosaur carni = new CarnivoreDinosaur(1, "Rex", "T-rex");
        HerbivoreDinosaur herbi = new HerbivoreDinosaur(2, "Tri", "Triceratops");
        assertEquals(0.9, carni.getDangerLevel());
        assertEquals(0.2, herbi.getDangerLevel());
    }

    @Test
    void testDiet() {
        CarnivoreDinosaur carni = new CarnivoreDinosaur(1, "Rex", "T-rex");
        HerbivoreDinosaur herbi = new HerbivoreDinosaur(2, "Tri", "Triceratops");
        assertEquals("CARNIVORE", carni.getDiet());
        assertEquals("HERBIVORE", herbi.getDiet());
    }
}