package com.javafxbindingtest.domain;

import javafx.beans.property.*;

public class WeatherInfo {
    private final DoubleProperty temperature;
    private final DoubleProperty windSpeed;
    private final DoubleProperty humidity;
    private final DoubleProperty pressure;
    private final StringProperty condition;
    private final BooleanProperty alert;

    public WeatherInfo(double temperature,
                       double windSpeed,
                       double humidity,
                       double pressure,
                       String condition,
                       boolean alert) {

        this.temperature = new SimpleDoubleProperty(temperature);
        this.windSpeed = new SimpleDoubleProperty(windSpeed);
        this.humidity = new SimpleDoubleProperty(humidity);
        this.pressure = new SimpleDoubleProperty(pressure);
        this.condition = new SimpleStringProperty(condition);
        this.alert = new SimpleBooleanProperty(alert);
    }

    public double getTemperature() { return temperature.get(); }
    public DoubleProperty temperatureProperty() { return temperature; }
    public void setTemperature(double v) { temperature.set(v); }

    public double getWindSpeed() { return windSpeed.get(); }
    public DoubleProperty windSpeedProperty() { return windSpeed; }
    public void setWindSpeed(double v) { windSpeed.set(v); }

    public double getHumidity() { return humidity.get(); }
    public DoubleProperty humidityProperty() { return humidity; }
    public void setHumidity(double v) { humidity.set(v); }

    public double getPressure() { return pressure.get(); }
    public DoubleProperty pressureProperty() { return pressure; }
    public void setPressure(double v) { pressure.set(v); }

    public String getCondition() { return condition.get(); }
    public StringProperty conditionProperty() { return condition; }
    public void setCondition(String v) { condition.set(v); }

    public boolean isAlert() { return alert.get(); }
    public BooleanProperty alertProperty() { return alert; }
    public void setAlert(boolean v) { alert.set(v); }
}

