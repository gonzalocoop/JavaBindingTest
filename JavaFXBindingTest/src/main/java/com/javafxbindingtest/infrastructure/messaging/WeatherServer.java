package com.javafxbindingtest.infrastructure.messaging;
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

            // Construimos JSON manualmente (Java 11 compatible)
            String json =
                    "{" +
                            "\"temperature\":" + temperature + "," +
                            "\"windSpeed\":" + wind + "," +
                            "\"humidity\":" + humidity + "," +
                            "\"pressure\":" + pressure + "," +
                            "\"condition\":\"" + condition + "\"," +
                            "\"alert\":" + alert +
                            "}";


            publisher.send(json);

            System.out.println("Enviado: " + json);

            Thread.sleep(1000); // cada 1 segundo
        }
    }


}
