package com.javafxbindingtest.domain.ports;

import com.javafxbindingtest.domain.model.WeatherInfo;

import java.util.List;

public interface WeatherHistoryRepository {
    void save(WeatherInfo weather);
    List<WeatherInfo> findAll();
    // Devuelve TODO el historial guardado
}
