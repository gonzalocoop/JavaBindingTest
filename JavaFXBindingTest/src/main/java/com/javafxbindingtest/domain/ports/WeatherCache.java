package com.javafxbindingtest.domain.ports;

import com.javafxbindingtest.domain.model.WeatherInfo;

import java.util.Optional;

public interface WeatherCache {
    void save(WeatherInfo weather);

    Optional<WeatherInfo> getLast();
}
