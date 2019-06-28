package com.example.zuul.demo;

import com.netflix.zuul.monitoring.CounterFactory;

public class EmptyCounterFactory extends CounterFactory {
    @Override
    public void increment(String name) {
    }
}
