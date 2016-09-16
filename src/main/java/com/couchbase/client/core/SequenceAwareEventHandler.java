package com.couchbase.client.core;

import com.lmax.disruptor.Sequence;
import com.lmax.disruptor.SequenceReportingEventHandler;

abstract public class SequenceAwareEventHandler<T extends TimedEvent> implements SequenceReportingEventHandler<T> {
    private Sequence sequence;
    private Listener listener;

    public interface Listener {
        void onEvent(long sequence, long delay, boolean endOfBatch);
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    @Override
    public void setSequenceCallback(Sequence sequence) {
        this.sequence = sequence;
    }

    @Override
    public void onEvent(T event, long sequence, boolean endOfBatch) throws Exception {
        if (listener != null) {
            try {
                long delay = System.nanoTime() - event.getTimestamp();
                listener.onEvent(sequence, delay, endOfBatch);
            } catch (Throwable t) {}
        }
    }

    public Sequence getSequence() {
        return sequence;
    }
}
