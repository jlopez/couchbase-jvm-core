package com.couchbase.client.core;

public class TimedEvent {
    private long timestamp;

    public long getTimestamp() {
        return timestamp;
    }

    protected void updateTimestamp() {
        timestamp = System.nanoTime();
    }
}
