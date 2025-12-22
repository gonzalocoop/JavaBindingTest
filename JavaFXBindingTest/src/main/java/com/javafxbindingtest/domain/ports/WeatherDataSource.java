package com.javafxbindingtest.domain.ports;

import com.javafxbindingtest.domain.model.WeatherInfo;

import java.util.function.Consumer;

public interface WeatherDataSource {
    void subscribe(Consumer<WeatherInfo> onData);
    void stop();
}
