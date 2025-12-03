package com.example;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        StatAggregator aggregator = new StatAggregator();
        aggregator.init();

        for (int i = 0; i < 1000; i++) {
            Thread.sleep(10L);
            aggregator.onQueryComplete(String.valueOf(i));
        }

        Thread.sleep(1000L);

        aggregator.show();
    }
}
