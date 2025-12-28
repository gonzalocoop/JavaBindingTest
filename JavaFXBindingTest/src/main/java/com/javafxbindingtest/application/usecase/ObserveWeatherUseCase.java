package com.javafxbindingtest.application.usecase;

import com.javafxbindingtest.domain.model.WeatherInfo;
import com.javafxbindingtest.domain.ports.WeatherCache;
import com.javafxbindingtest.domain.ports.WeatherDataSource;
import com.javafxbindingtest.domain.ports.WeatherHistoryRepository;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class ObserveWeatherUseCase {
    // Un caso de uso representa UNA acción del sistema desde el punto de vista del negocio
    // Caso de uso: "Observar el clima en tiempo real"

    private final WeatherDataSource dataSource;
    // Fuente de datos (ZMQ, Kafka, HTTP, etc.)
    // Emite WeatherInfo en tiempo real

    private final WeatherCache cache;
    // Cache del último estado del clima (Redis, memoria, etc.)

    private final WeatherHistoryRepository historyRepository;
    // Repositorio del historial completo (MongoDB, SQL, etc.)

    /* ============================= */
    /*       EXECUTORES              */
    /* ============================= */

    private final ExecutorService cacheExecutor =
            Executors.newSingleThreadExecutor();
    // Executor dedicado EXCLUSIVAMENTE a Redis
    // Si Redis se cae o se bloquea → NO afecta a Mongo ni a la UI

    private final ExecutorService historyExecutor =
            Executors.newSingleThreadExecutor();
    // Executor dedicado a persistencia histórica (MongoDB)
    // Mongo puede ser lento o fallar → no rompe el flujo principal

    private final ExecutorService uiExecutor =
            Executors.newSingleThreadExecutor();
    // Executor para notificar a la UI
    // Evita que el hilo de ZMQ se bloquee esperando a la interfaz

    public ObserveWeatherUseCase(
            WeatherDataSource dataSource,
            WeatherCache cache,
            WeatherHistoryRepository historyRepository
    ) {
        // Constructor con inyección de dependencias
        // El caso de uso NO conoce implementaciones concretas
        this.dataSource = dataSource;
        this.cache = cache;
        this.historyRepository = historyRepository;
    }

    public void start(Consumer<WeatherInfo> onWeatherChanged) {
        // Inicia el flujo del caso de uso "Observar clima"

        cache.getLast().ifPresent(onWeatherChanged);
        // 1️1) Si hay un valor cacheado en Redis:
        // - Se emite inmediatamente a la UI
        // - Mejora UX (no espera a ZMQ)
        // - Esto ocurre SIN BLOQUEAR la suscripción

        dataSource.subscribe(weather -> {
            // 2) Cada vez que llega un nuevo WeatherInfo desde ZMQ
            // Este callback corre en el hilo del DataSource


            //          REDIS

            cacheExecutor.submit(() -> {
                // Se ejecuta en un hilo independiente
                try {
                    cache.save(weather);
                    // Guarda el último clima en Redis
                } catch (Exception e) {
                    // Si Redis falla:
                    // - Se loguea el error
                    // - NO se cae el flujo
                    System.err.println("Error guardando en Redis");
                    e.printStackTrace();
                }
            });


            //          MONGO

            historyExecutor.submit(() -> {
                // Persistencia histórica en segundo plano
                try {
                    historyRepository.save(weather);
                    // Inserta un nuevo registro en MongoDB
                } catch (Exception e) {
                    // Si Mongo se cae:
                    // - No afecta Redis
                    // - No afecta UI
                    System.err.println("Error guardando en MongoDB");
                    e.printStackTrace();
                }
            });


            //           UI

            uiExecutor.submit(() -> {
                // Notificación a la UI desacoplada
                try {
                    onWeatherChanged.accept(weather);
                    // La UI decide cómo renderizar el dato
                } catch (Exception e) {
                    // Un error visual NO debe tumbar el sistema
                    System.err.println("Error notificando a la UI");
                    e.printStackTrace();
                }
            });
        });
    }

    public void stop() {
        // Detiene completamente el caso de uso

        dataSource.stop();
        // Cierra la conexión con ZMQ (o la fuente que sea)

        cacheExecutor.shutdownNow();
        // Detiene el hilo de Redis

        historyExecutor.shutdownNow();
        // Detiene el hilo de MongoDB

        uiExecutor.shutdownNow();
        // Detiene el hilo de notificación a la UI
    }
}
