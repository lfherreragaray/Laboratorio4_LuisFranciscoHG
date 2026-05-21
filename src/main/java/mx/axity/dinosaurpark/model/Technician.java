package mx.axity.dinosaurpark.model;

import mx.axity.dinosaurpark.zone.PowerPlant;

public class Technician extends Worker {
    public Technician(int id, String name, double dailySalary) {
        super(id, name, dailySalary);
    }

    @Override
    public String getRole() {
        return "TECHNICIAN";
    }

    public void repairIfNeeded(PowerPlant plant) {
        if (!plant.isOperational()) {
            plant.repair();
        }
    }
}