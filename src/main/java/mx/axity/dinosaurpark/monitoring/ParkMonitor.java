package mx.axity.dinosaurpark.monitoring;

import mx.axity.dinosaurpark.simulation.ParkState;

public class ParkMonitor {
    public static void displaySnapshot(ParkState state) {
        System.out.println("=== STEP " + state.getCurrentStep() + " ===");
        System.out.printf("Turistas activos: %d\n", state.countActiveTourists());
        System.out.printf("Dinosaurios en encierro: %d\n", state.countDinosaursInEnclosure());
        System.out.printf("Energía disponible: %.2f%%\n", state.getEnergyPercentage());
        System.out.printf("Ingresos acumulados: %.2f\n", state.getTotalRevenue());
        System.out.printf("Gastos acumulados: %.2f\n", state.getTotalExpenses());
        System.out.println();
    }
}