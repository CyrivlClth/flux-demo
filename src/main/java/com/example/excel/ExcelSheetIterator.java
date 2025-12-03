package com.example.excel;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;

import java.util.Iterator;
import java.util.List;

@RequiredArgsConstructor
public class ExcelSheetIterator implements Iterator<List<String>> {
    @Getter
    private final String sheetName;
    private final Iterator<List<String>> iterator;
    private final List<String> headers;
    private boolean isHeaderSet = false;


    @Override
    public boolean hasNext() {
        if (isHeaderSet && CollectionUtils.isNotEmpty(headers)) {
            return true;
        }
        return iterator.hasNext();
    }

    @Override
    public List<String> next() {
        if (isHeaderSet && CollectionUtils.isNotEmpty(headers)) {
            isHeaderSet = true;
            return headers;
        }
        return iterator.next();
    }
}
