package ru.chayka.mproduction.settings;

public enum DefaultSetting {
    RESOURCE_START_ID("resource_start_id", "100"),
    NUM_OF_PRODUCERS("num_of_producers", "5"),
    TIME_TO_PRODUCE("time_to_produce", "2000"),
    PRODUCERS_START_ID("producers_start_id", "200"),
    NUM_OF_CONSUMERS("num_of_consumers", "5"),
    TIME_TO_CONSUME("time_to_consume", "2000"),
    CONSUMERS_START_ID("consumers_start_id", "300"),
    STORAGE_CAPACITY("storage_capacity", "10"),
    APP_RUNNING_TIME("app_running_time", "5000");

    private final String key;
    private final String defaultValue;

    DefaultSetting(String key, String defaultValue) {
        this.key = key;
        this.defaultValue = defaultValue;
    }

    public String getKey() {
        return key;
    }

    public String getDefaultValue() {
        return defaultValue;
    }
}
