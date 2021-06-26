package ru.chayka.mproduction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.chayka.mproduction.settings.Settings;

public class ResourceProducer implements CustomRunnable {
    private static final Logger log = LoggerFactory.getLogger(ResourceProducer.class.getName());

    private final int id;
    private final ResourceStorage resourceStorage;
    private final int timeDelay;

    public ResourceProducer(int id, ResourceStorage resourceStorage, int timeDelay) {
        this.id = id + Settings.getInstance().getProducersStartId();
        this.resourceStorage = resourceStorage;
        this.timeDelay = timeDelay;
        log.debug("Producer {} is created", this.id);
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void run() {
        log.debug("Producer {} started", this.id);
        while (true) {
            try {
                resourceStorage.addResource(new CustomResource());
                Thread.sleep(timeDelay);
            } catch (InterruptedException ex) {
                log.debug("Producer {} thread is interrupted", this.id);
                return;
            }
        }
    }
}
