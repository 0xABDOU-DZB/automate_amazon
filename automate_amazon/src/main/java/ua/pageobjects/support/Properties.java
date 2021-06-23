package ua.startit.support;

import java.io.IOException;
import java.io.InputStream;

public class Properties {

    private static Properties instance;
    private String browser;
    private String env;
    private String headless;

    private Properties() {
        InputStream config = Thread.currentThread().getContextClassLoader().getResourceAsStream("config.properties");
        java.util.Properties properties = new java.util.Properties();
        try {
            properties.load(config);
        } catch (IOException e) {
            e.printStackTrace();
        }

        browser = System.getProperty("browser");
        if (browser == null) {
            browser = properties.getProperty("browser");
        }

        env = System.getProperty("env");
        if (env == null) {
            env = properties.getProperty("env");
        }

        headless = System.getProperty("headless");
        if (headless == null) {
            headless = properties.getProperty("headless");
        }
    }

    public String getBrowser() {
        return browser;
    }

    public String getEnv() {
        return env;
    }

    public String getHeadless() {
        return headless;
    }

    public static Properties getInstance() {
        if (instance == null) {
            instance = new Properties();
        }
        return instance;
    }
}
© 2021 GitHub, Inc.