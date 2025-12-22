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

    private volatile boolean running = true;

    @Override
    public void subscribe(Consumer<WeatherInfo> callback) {

        executor.submit(() -> {
            ZMQ.Context context = ZMQ.context(1);
            ZMQ.Socket subscriber = context.socket(ZMQ.SUB);

            subscriber.connect("tcp://localhost:5555");
            subscriber.subscribe("");

            try {
                while (running && !Thread.currentThread().isInterrupted()) {
                    byte[] msg = subscriber.recv(0);
                    if (msg != null) {
                        WeatherInfoProto proto =
                                WeatherInfoProto.parseFrom(msg);
                        callback.accept(
                                WeatherProtoMapper.toDomain(proto)
                        );
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                subscriber.close();
                context.close();
            }
        });
    }

    @Override
    public void stop() {
        running = false;
        executor.shutdownNow();
    }
}
