package ru.chayka.mproduction;

import ru.chayka.mproduction.settings.Settings;

public class CustomResource {
    private static final Object SYNC = new Object();
    private static int idCount = Settings.getInstance().getResourceStartId();

    private final int id;

    public CustomResource() {
        synchronized (SYNC) {
            this.id = idCount++;
        }
    }

    public int getId() {
        return id;
    }
}
