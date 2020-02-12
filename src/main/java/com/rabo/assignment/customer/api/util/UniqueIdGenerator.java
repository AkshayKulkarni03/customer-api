package com.rabo.assignment.customer.api.util;

public class UniqueIdGenerator {

    private UniqueIdGenerator() {

    }

    private static long previousTimeMillis = System.currentTimeMillis();
    private static long counter = 0L;

    public static synchronized long nextID() {
        long currentTimeMillis = System.currentTimeMillis();
        counter = (currentTimeMillis == previousTimeMillis) ? (counter + 1L) & 1048575L : 0L;
        previousTimeMillis = currentTimeMillis;
        long timeComponent = (currentTimeMillis & 8796093022207L) << 20;
        return timeComponent | counter;
    }
}