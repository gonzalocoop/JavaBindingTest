package com.javafxbindingtest.application.usecase;

import com.javafxbindingtest.domain.model.WeatherInfo;
import com.javafxbindingtest.domain.ports.WeatherHistoryRepository;

import java.util.List;

public class GetWeatherHistoryUseCase {
    // Caso de uso: obtener el historial completo del clima en otro fxml

    private final WeatherHistoryRepository repository;
    // Dependencia al puerto del dominio (inyección de dependencias)

    public GetWeatherHistoryUseCase(WeatherHistoryRepository repository) {
        // Constructor del caso de uso

        this.repository = repository;
        // Se asigna la implementación concreta del repositorio
        // (ej: MongoWeatherHistoryRepository)
    }

    public List<WeatherInfo> execute() {
        // Metodo principal del caso de uso
        // Ejecuta la lógica de negocio para obtener el historial

        return repository.findAll();
        // Delegamos al repositorio la obtención de todos los datos
        // El caso de uso NO sabe si vienen de Mongo, SQL, etc.
    }
}
