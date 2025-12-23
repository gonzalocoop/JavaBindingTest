package com.javafxbindingtest.infrastructure.messaging.zmq;

//import com.javafxbindingtest.domain.model.WeatherInfo;
import com.javafxbindingtest.domain.model.WeatherInfo;
import com.javafxbindingtest.domain.ports.WeatherDataSource;
import com.javafxbindingtest.infrastructure.messaging.mapper.WeatherProtoMapper;
import com.javafxbindingtest.infrastructure.proto.WeatherInfoProto;
import org.zeromq.ZMQ;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class ZmqWeatherClient implements WeatherDataSource {
    private final ExecutorService executor =
            Executors.newSingleThreadExecutor();
    // Executor con un solo hilo para escuchar ZMQ

    private volatile boolean running = true;
    // Flag para controlar el ciclo de vida del hilo

    @Override
    public void subscribe(Consumer<WeatherInfo> callback) {
        // Suscripción a datos del clima

        executor.submit(() -> {
            // Ejecuta la escucha en un hilo separado

            ZMQ.Context context = ZMQ.context(1);
            // Contexto ZMQ

            ZMQ.Socket subscriber = context.socket(ZMQ.SUB);
            // Socket tipo SUB (subscriber)

            subscriber.connect("tcp://localhost:5555");
            // Se conecta al servidor ZMQ

            subscriber.subscribe("");
            // Se suscribe a todos los mensajes

            try {
                while (running && !Thread.currentThread().isInterrupted()) {
                    // Loop mientras esté activo

                    byte[] msg = subscriber.recv(0);
                    // Recibe mensaje bloqueante

                    if (msg != null) {
                        WeatherInfoProto proto =
                                WeatherInfoProto.parseFrom(msg);
                        // Deserializa Protobuf

                        callback.accept(
                                WeatherProtoMapper.toDomain(proto)
                        );
                        // Convierte a dominio y notifica
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                subscriber.close();
                // Cierra socket
                context.close();
                // Cierra contexto
            }
        });
    }

    @Override
    public void stop() {
        // Detiene el cliente
        running = false;
        executor.shutdownNow();
        // Interrumpe el hilo
    }
}
