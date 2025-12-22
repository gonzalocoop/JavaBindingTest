package com.javafxbindingtest.infrastructure.messaging;
import com.javafxbindingtest.proto.WeatherInfoProto;
import org.zeromq.ZMQ;

import java.util.Random;

public class WeatherServer {
    public static void main(String[] args) throws InterruptedException {

        ZMQ.Context context = ZMQ.context(1);
        ZMQ.Socket publisher = context.socket(ZMQ.PUB);

        // Enlazar puerto
        publisher.bind("tcp://localhost:5555");

        Random random = new Random();



        while (true) {
            double temperature = 15 + random.nextDouble() * 15;
            double wind = random.nextDouble() * 10;
            double humidity = 40 + random.nextDouble() * 50;
            double pressure = 990 + random.nextDouble() * 30;
            boolean alert = random.nextBoolean();
            String condition = random.nextBoolean() ? "Sunny" : "Cloudy";
            double latitude = -13 + random.nextDouble() * 1;
            double longitude = -78 + random.nextDouble() * 1;

            //System.out.println("Enviado: " + json);

            // Construir objeto protobuf
            WeatherInfoProto weatherInfo = WeatherInfoProto.newBuilder()
                    .setTemperature(temperature)
                    .setWindSpeed(wind)
                    .setHumidity(humidity)
                    .setPressure(pressure)
                    .setCondition(condition)
                    .setAlert(alert)
                    .setLatitude(latitude)
                    .setLongitude(longitude)
                    .build();

            // Serializar y enviar
            publisher.send(weatherInfo.toByteArray(), 0);
            //System.out.println("Enviado: " + weatherInfo);
            Thread.sleep(1000); // cada 1 segundo
        }
    }


}
