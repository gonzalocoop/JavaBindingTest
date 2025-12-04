package com.javafxbindingtest.infrastructure.messaging;
import com.javafxbindingtest.proto.WeatherInfo; // clase generada por protobuf

import com.fasterxml.jackson.databind.ObjectMapper;
//import com.javafxbindingtest.domain.WeatherInfo;
import org.zeromq.ZMQ;
import java.util.function.Consumer;
import java.io.IOException;

public class ZmqWeatherClient {
    private final ObjectMapper mapper = new ObjectMapper();
    private boolean running = true;

    public void startListening(String address, Consumer<WeatherInfo> callback) {

        Thread thread = new Thread(() -> {
            ZMQ.Context context = ZMQ.context(1);
            ZMQ.Socket subscriber = context.socket(ZMQ.SUB);
            subscriber.connect(address);
            subscriber.subscribe(""); // suscribirse a todos los mensajes

            while (running) {
                byte[] msgBytes = subscriber.recv(0);
                if (msgBytes != null) {
                    try {
                        //WeatherInfo info = mapper.readValue(msg, WeatherInfo.class);

                        // Deserializar Protobuf (debe ser de tipo proto)
                        WeatherInfo info = WeatherInfo.parseFrom(msgBytes);
                        callback.accept(info);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            subscriber.close();
            context.close();
        });

        thread.setDaemon(true);
        thread.start();
    }

    public void stop() {
        running = false;
    }
}
