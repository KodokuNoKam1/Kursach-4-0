package by.bsuir.kursach.commercialoffer.util;

public class ConfigService {
    private static ConfigService instance;

    private ConfigService() {}

    public static ConfigService getInstance() {
        if (instance == null) {
            instance = new ConfigService();
        }
        return instance;
    }

    public String getConfig(String key) {
        return System.getProperty(key, "default");
    }
}