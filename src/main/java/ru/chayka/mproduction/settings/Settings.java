package ru.chayka.mproduction.settings;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.chayka.mproduction.Main;

import java.util.Properties;

public class Settings {
    public static final String PROPERTIES_FILE_NAME = "data.properties";
    private static final Logger log = LoggerFactory.getLogger(Settings.class.getName());
    private volatile static Settings uniqueInstance;

    private final int resourceStartId;

    private final int numOfProducers;
    private final int timeToProduce;
    private final int producersStartId;

    private final int numOfConsumers;
    private final int timeToConsume;
    private final int consumersStartId;

    private final int storageCapacity;

    private final int appRunningTime;

    private Settings() {
        Properties properties;
        try {
            properties = PropertiesFileParser.parse(PROPERTIES_FILE_NAME, Main.class);
        } catch (Exception ex) {
            log.warn(createParseFailLogMessage());

            resourceStartId = getIntDefaultValue(DefaultSetting.RESOURCE_START_ID);

            numOfProducers = getIntDefaultValue(DefaultSetting.NUM_OF_PRODUCERS);
            timeToProduce = getIntDefaultValue(DefaultSetting.TIME_TO_PRODUCE);
            producersStartId = getIntDefaultValue(DefaultSetting.PRODUCERS_START_ID);

            numOfConsumers = getIntDefaultValue(DefaultSetting.NUM_OF_CONSUMERS);
            timeToConsume = getIntDefaultValue(DefaultSetting.TIME_TO_CONSUME);
            consumersStartId = getIntDefaultValue(DefaultSetting.CONSUMERS_START_ID);

            storageCapacity = getIntDefaultValue(DefaultSetting.STORAGE_CAPACITY);

            appRunningTime = getIntDefaultValue(DefaultSetting.APP_RUNNING_TIME);

            log.debug("Default setting values are set successfully" + System.lineSeparator());
            return;
        }

        resourceStartId = tryToGetIntProperty(DefaultSetting.RESOURCE_START_ID, properties);

        numOfProducers = tryToGetIntProperty(DefaultSetting.NUM_OF_PRODUCERS, properties);
        timeToProduce = tryToGetIntProperty(DefaultSetting.TIME_TO_PRODUCE, properties);
        producersStartId = tryToGetIntProperty(DefaultSetting.PRODUCERS_START_ID, properties);

        numOfConsumers = tryToGetIntProperty(DefaultSetting.NUM_OF_CONSUMERS, properties);
        timeToConsume = tryToGetIntProperty(DefaultSetting.TIME_TO_CONSUME, properties);
        consumersStartId = tryToGetIntProperty(DefaultSetting.CONSUMERS_START_ID, properties);

        storageCapacity = tryToGetIntProperty(DefaultSetting.STORAGE_CAPACITY, properties);

        appRunningTime = tryToGetIntProperty(DefaultSetting.APP_RUNNING_TIME, properties);

        log.debug("Setting values are set successfully" + System.lineSeparator());
    }

    public static Settings getInstance() {
        if (uniqueInstance == null) {
            synchronized (Settings.class) {
                if (uniqueInstance == null) {
                    uniqueInstance = new Settings();
                }
            }
        }
        return uniqueInstance;
    }

    public int getResourceStartId() {
        return resourceStartId;
    }

    public int getNumOfProducers() {
        return numOfProducers;
    }

    public int getTimeToProduce() {
        return timeToProduce;
    }

    public int getProducersStartId() {
        return producersStartId;
    }

    public int getNumOfConsumers() {
        return numOfConsumers;
    }

    public int getTimeToConsume() {
        return timeToConsume;
    }

    public int getConsumersStartId() {
        return consumersStartId;
    }

    public int getStorageCapacity() {
        return storageCapacity;
    }

    public int getAppRunningTime() {
        return appRunningTime;
    }

    private String tryToGetStringProperty(DefaultSetting setting, Properties properties) {
        String parsedProperty = properties.getProperty(setting.getKey());
        if (parsedProperty == null || parsedProperty.isBlank()) {
            log.warn(createInvalidPropertyLogMessage(setting, properties));
            return getStringDefaultValue(setting);
        } else {
            return parsedProperty;
        }
    }

    private int tryToGetIntProperty(DefaultSetting setting, Properties properties) {
        try {
            int parsedProperty = Integer.parseInt(
                    properties.getProperty(setting.getKey()));
            if (!checkIfValueIsValid(parsedProperty)) {
                throw new NumberFormatException();
            } else {
                return parsedProperty;
            }
        } catch (NumberFormatException ex) {
            log.warn(createInvalidPropertyLogMessage(setting, properties));
            return getIntDefaultValue(setting);
        }
    }

    private String getStringDefaultValue(DefaultSetting setting) {
        return setting.getDefaultValue();
    }

    private int getIntDefaultValue(DefaultSetting setting) {
        try {
            int parsedProperty = Integer.parseInt(
                    setting.getDefaultValue());
            if (!checkIfValueIsValid(parsedProperty)) {
                throw new NumberFormatException();
            } else {
                return parsedProperty;
            }
        } catch (NumberFormatException ex) {
            log.error("Default value for {} property is invalid: {}",
                    setting.getKey(),
                    setting.getDefaultValue());
            throw ex;
        }
    }

    private boolean checkIfValueIsValid(int value) {
        return value > 0;
    }

    private String createParseFailLogMessage() {
        StringBuilder errorMessage = new StringBuilder(50);
        errorMessage.append("Failed to parse Properties file, default values are used:");
        for (DefaultSetting setting : DefaultSetting.values()) {
            errorMessage.append(System.lineSeparator());
            errorMessage.append(String.format(
                    "%s: %s", setting.getKey(), setting.getDefaultValue()));

        }
        return errorMessage.toString();
    }

    private String createInvalidPropertyLogMessage(DefaultSetting setting, Properties properties) {
        return String.format("%s property is invalid: %s. Default value is used: %s",
                setting.getKey(),
                properties.getProperty(setting.getKey()),
                setting.getDefaultValue());
    }
}
