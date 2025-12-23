package com.javafxbindingtest.domain.ports;

import com.javafxbindingtest.domain.model.WeatherInfo;

import java.util.Optional;

public interface WeatherCache {
    // Interfaz = PUERTO del dominio
    // Define el contrato para cualquier sistema de cache

    void save(WeatherInfo weather);
    // Metodo que guarda información del clima
    // No dice si es Redis, memoria, archivo, etc.

    Optional<WeatherInfo> getLast();
    // Devuelve el último WeatherInfo si existe
    // Optional.empty() si no hay nada guardado
}
