package com.davis.batchprocessor.reader;

import org.springframework.batch.item.ItemReader;

public class SynchronizedItemReader <T> implements ItemReader<T> {
    private final ItemReader<T> delegate;
    public SynchronizedItemReader(ItemReader<T> delegate) {
        this.delegate = delegate;
    }
    public synchronized T read () throws Exception {
        return delegate.read();
    }
}
