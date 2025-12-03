package com.example.excel;

import lombok.RequiredArgsConstructor;

import java.util.Iterator;
import java.util.function.Function;

/**
 * 将 Iterator<T> 转换为 Iterator<R> 的装饰器
 * 在每次 next() 时应用转换函数
 */
@RequiredArgsConstructor(staticName = "from")
public class MappedIterator<R, T> implements Iterator<R> {
    protected final Iterator<T> source;
    protected final Function<T, R> mapper;

    @Override
    public boolean hasNext() {
        return source.hasNext();
    }

    @Override
    public R next() {
        T item = source.next();
        return mapper.apply(item);
    }
}