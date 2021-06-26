package ru.chayka.mproduction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.chayka.mproduction.settings.Settings;

public class Main {
    private static final Logger log = LoggerFactory.getLogger(Main.class.getName());

    private static void startMultiThreadedProductionForLimitedTime(int time) {
        ResourceStorage resourceStorage = new ResourceStorage(Settings.getInstance().getStorageCapacity());

        Thread[] resourceProducersThreads = new Thread[Settings.getInstance().getNumOfProducers()];
        for (int i = 0; i < Settings.getInstance().getNumOfProducers(); i++) {
            ResourceProducer resourceProducer =
                    new ResourceProducer(i + 1, resourceStorage, Settings.getInstance().getTimeToProduce());
            resourceProducersThreads[i] =
                    new Thread(resourceProducer, String.format("Producer %d", resourceProducer.getId()));
        }

        Thread[] resourceConsumersThreads = new Thread[Settings.getInstance().getNumOfConsumers()];
        for (int i = 0; i < Settings.getInstance().getNumOfConsumers(); i++) {
            ResourceConsumer resourceConsumer =
                    new ResourceConsumer(i + 1, resourceStorage, Settings.getInstance().getTimeToConsume());
            resourceConsumersThreads[i] =
                    new Thread(resourceConsumer, String.format("Consumer %d", resourceConsumer.getId()));
        }
        log.debug("All threads are created successfully" + System.lineSeparator());

        for (Thread thread : resourceProducersThreads) {
            thread.start();
        }

        for (Thread thread : resourceConsumersThreads) {
            thread.start();
        }

        if (time > 0) {
            try {
                Thread.sleep(time);
                for (Thread thread : resourceProducersThreads) {
                    thread.interrupt();
                }
                for (Thread thread : resourceConsumersThreads) {
                    thread.interrupt();
                }
                log.debug("All threads were interrupted");
            } catch (InterruptedException ignore) {
            }
        }
    }

    private static void startMultiThreadedProductionForLimitedTime() {
        startMultiThreadedProductionForLimitedTime(0);
    }

    public static void main(String[] args) {
        try {
            startMultiThreadedProductionForLimitedTime(Settings.getInstance().getAppRunningTime());
        } catch (Exception ex) {
            log.debug(ex.getMessage(), ex);
            log.debug("Application finished with errors");
        }
    }
}
