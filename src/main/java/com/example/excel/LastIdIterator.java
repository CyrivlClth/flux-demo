package com.example.excel;

import java.util.*;
import java.util.function.Function;

public class LastIdIterator<T, ID> implements Iterator<T> {
    private Iterator<T> batch = Collections.emptyIterator();
    private final Function<ID, List<T>> fetchFunction;
    private final Function<T, ID> idGetter;
    private ID lastId;

    public LastIdIterator(Function<ID, List<T>> fetchFunction, Function<T, ID> idGetter) {
        this.idGetter = Objects.requireNonNull(idGetter);
        this.fetchFunction = Objects.requireNonNull(fetchFunction);
        this.lastId = null;
    }

    private void advance() {
        List<T> data = fetchFunction.apply(lastId);
        if (data.isEmpty()) {
            batch = Collections.emptyIterator();
            return;
        }
        T lastElement = data.get(data.size() - 1);
        lastId = idGetter.apply(lastElement);
        batch = data.iterator();
    }

    @Override
    public boolean hasNext() {
        if (!batch.hasNext()) {
            advance();
        }
        return batch.hasNext();
    }

    @Override
    public T next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        return batch.next();
    }
}
