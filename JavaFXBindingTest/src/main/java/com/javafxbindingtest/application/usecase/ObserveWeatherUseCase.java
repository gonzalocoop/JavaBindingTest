package com.javafxbindingtest.application.usecase;

import com.javafxbindingtest.domain.model.WeatherInfo;
import com.javafxbindingtest.domain.ports.WeatherCache;
import com.javafxbindingtest.domain.ports.WeatherDataSource;
import com.javafxbindingtest.domain.ports.WeatherHistoryRepository;

import java.util.function.Consumer;

public class ObserveWeatherUseCase {
    //Un caso de uso es una clase que representa UNA acción del sistema desde el punto de vista del negocio
    // Caso de uso: "Observar el clima en tiempo real"

    private final WeatherDataSource dataSource;
    // Fuente de datos (ZMQ, Kafka, HTTP, etc.)

    private final WeatherCache cache;
    // Cache (Redis, memoria, etc.)

    private final WeatherHistoryRepository historyRepository;



    public ObserveWeatherUseCase(
            WeatherDataSource dataSource,
            WeatherCache cache,
            WeatherHistoryRepository historyRepository
    ) {
        // Constructor con inyección de dependencias
        // El use case NO crea implementaciones concretas
        this.dataSource = dataSource;
        this.cache = cache;
        this.historyRepository = historyRepository;
    }

    public void start(Consumer<WeatherInfo> onWeatherChanged) {
        // Inicia el flujo del caso de uso

        cache.getLast().ifPresent(onWeatherChanged);
        // 1) Si hay un valor cacheado, se emite inmediatamente
        // Mejora UX y permite mostrar datos sin esperar ZMQ

        dataSource.subscribe(weather -> {
            //2) Se suscribe a nuevos datos
            cache.save(weather);
            // Redis → último valor rápido

            historyRepository.save(weather);
            // MongoDB → histórico completo

            onWeatherChanged.accept(weather);
            // Notifica al observador (UI)
        });
    }

    public void stop() {
        // Detiene el caso de uso
        dataSource.stop();
        // Cierra la fuente de datos
    }
}
