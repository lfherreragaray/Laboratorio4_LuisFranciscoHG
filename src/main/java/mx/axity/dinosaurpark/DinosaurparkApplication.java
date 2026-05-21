package mx.axity.dinosaurpark;

import mx.axity.dinosaurpark.config.ParkConfig;
import mx.axity.dinosaurpark.simulation.SimulationEngine;

//@SpringBootApplication
public class DinosaurparkApplication {

	public static void main(String[] args) {
        ParkConfig config = ParkConfig.getInstance();
        SimulationEngine engine = new SimulationEngine(config);
        engine.run();
	}

}
