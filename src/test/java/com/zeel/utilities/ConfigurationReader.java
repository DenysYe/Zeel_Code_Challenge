package com.zeel.utilities;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigurationReader {
    private static final Logger logger = LogManager.getLogger(ConfigurationReader.class);
    private static Properties configFile;
    static {
        try {
            FileInputStream fileInputStream = new FileInputStream("configuration.properties");
            configFile = new Properties();
            configFile.load(fileInputStream);
            fileInputStream.close();
        } catch (IOException e) {
            logger.info("Failed to load properties file!");
            e.printStackTrace();
        }
    }
    public static String getProperty(String key) {
        return configFile.getProperty(key);
    }
}

