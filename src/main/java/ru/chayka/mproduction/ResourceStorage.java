package ru.chayka.mproduction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayDeque;

public class ResourceStorage {
    private static final Logger log = LoggerFactory.getLogger(ResourceStorage.class.getName());

    private final int capacity;

    private final ArrayDeque<CustomResource> customResources;

    private final Object syncForProducers;
    private final Object syncForConsumers;

    public ResourceStorage(int capacity) {
        this.capacity = capacity;

        customResources = new ArrayDeque<>();

        syncForProducers = new Object();
        syncForConsumers = new Object();
    }

    public void addResource(CustomResource resource) throws InterruptedException {
        synchronized (syncForProducers) {
            while (customResources.size() >= capacity) {
                log.debug("{} is waiting...", Thread.currentThread().getName());
                syncForProducers.wait();
                log.debug("{} is running again", Thread.currentThread().getName());
            }

            customResources.add(resource);
            log.debug("{} produced resource {}",
                    Thread.currentThread().getName(), resource.getId());
        }

        synchronized (syncForConsumers) {
            syncForConsumers.notify();
        }
    }

    public CustomResource consumeResource() throws InterruptedException {
        CustomResource buffer;
        synchronized (syncForConsumers) {
            while (customResources.isEmpty()) {
                log.debug("{} is waiting...", Thread.currentThread().getName());
                syncForConsumers.wait();
                log.debug("{} is running again", Thread.currentThread().getName());
            }

            buffer = customResources.pollLast();
            log.debug("{} consumed resource {}",
                    Thread.currentThread().getName(), buffer.getId());
        }

        synchronized (syncForProducers) {
            syncForProducers.notify();
        }

        return buffer;
    }
}
