package ru.chayka.mproduction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.chayka.mproduction.settings.Settings;

public class ResourceConsumer implements CustomRunnable {
    private static final Logger log = LoggerFactory.getLogger(ResourceConsumer.class.getName());

    private final int id;
    private final ResourceStorage resourceStorage;
    private final int timeDelay;

    public ResourceConsumer(int id, ResourceStorage resourceStorage, int timeDelay) {
        this.id = id + Settings.getInstance().getConsumersStartId();
        this.resourceStorage = resourceStorage;
        this.timeDelay = timeDelay;
        log.debug("Consumer {} is created", this.id);
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void run() {
        log.debug("Consumer {} started", this.id);
        try {
            while (true) {
                resourceStorage.consumeResource();
                Thread.sleep(timeDelay);
            }
        } catch (InterruptedException ex) {
            log.debug("Consumer {} thread is interrupted", this.id);
        }
    }
}
