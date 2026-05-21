package mx.axity.dinosaurpark.config;

import java.io.InputStream;
import java.util.Properties;

public final class ParkConfig {
    private static ParkConfig instance;
    private final Properties props;

    private ParkConfig() {
        props = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("park.properties")) {
            if (input == null) {
                throw new RuntimeException("park.properties not found in classpath");
            }
            props.load(input);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load park.properties", e);
        }
    }

    public static ParkConfig getInstance() {
        if (instance == null) {
            instance = new ParkConfig();
        }
        return instance;
    }

    public int getInt(String key, int defaultValue) {
        String val = props.getProperty(key);
        return val == null ? defaultValue : Integer.parseInt(val);
    }

    public double getDouble(String key, double defaultValue) {
        String val = props.getProperty(key);
        return val == null ? defaultValue : Double.parseDouble(val);
    }

    public String getString(String key, String defaultValue) {
        return props.getProperty(key, defaultValue);
    }

    public long getSeed() {
        return getLong("simulation.seed", 42L);
    }

    public long getLong(String key, long defaultValue) {
        String val = props.getProperty(key);
        return val == null ? defaultValue : Long.parseLong(val);
    }

    public int getTotalSteps() {
        return getInt("simulation.totalSteps", 100);
    }

    // Only for testing
    public static void resetForTesting() {
        instance = null;
    }
}