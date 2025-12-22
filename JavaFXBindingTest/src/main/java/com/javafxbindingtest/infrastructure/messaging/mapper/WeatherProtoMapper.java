package com.javafxbindingtest.infrastructure.messaging.mapper;

import com.javafxbindingtest.domain.model.WeatherInfo;
import com.javafxbindingtest.infrastructure.proto.WeatherInfoProto;

public class WeatherProtoMapper {

    public static WeatherInfo toDomain(WeatherInfoProto proto) {
        return new WeatherInfo(
                proto.getTemperature(),
                proto.getWindSpeed(),
                proto.getHumidity(),
                proto.getPressure(),
                proto.getCondition(),
                proto.getAlert(),
                proto.getLatitude(),
                proto.getLongitude()
        );
    }
}
