package com.example.customer.api.util;

/**
 * Unique id generator based on ephoc time till nanosecond
 * 
 * @author akshay
 *
 */
public class UniqueIdGenerator {

    private UniqueIdGenerator() {
        super();
    }

    private static long previousTimeMillis = System.currentTimeMillis();
    private static long counter = 0L;

    /**
     * Generate sequence based on current time in millisecond and then have
     * additional counter with nanosecond while removing the last digit and
     * finally get value based on bitwise or
     * 
     * @return newly generated value
     */
    public static synchronized long nextID() {
        long currentTimeMillis = System.currentTimeMillis();
        counter = (currentTimeMillis == previousTimeMillis) ? (counter + 1L) & 1048575L : 0L;
        previousTimeMillis = currentTimeMillis;
        long timeComponent = (currentTimeMillis & 8796093022207L) << 20;
        return timeComponent | counter;
    }
}
