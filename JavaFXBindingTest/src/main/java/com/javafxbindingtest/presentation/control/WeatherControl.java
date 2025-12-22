package com.javafxbindingtest.presentation.control;

import com.javafxbindingtest.domain.WeatherInfo;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class WeatherControl extends AnchorPane {
    @FXML private Label lblTemperature;
    @FXML private Label lblWind;
    @FXML private Label lblHumidity;
    @FXML private Label lblPressure;
    @FXML private Label lblCondition;
    @FXML private Label lblAlert;
    @FXML private Label lblLatitude;
    @FXML private Label lblLongitude;

    private final ObjectProperty<WeatherInfo> weatherInfo = new SimpleObjectProperty<>();

    public WeatherControl() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/javafxbindingtest/presentation/control/WeatherControl.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        loader.load();

        weatherInfo.addListener(((observableValue, weatherInfo1, t1) -> {
            if (t1 != null) {
                lblTemperature.textProperty().bind(t1.temperatureProperty().asString());
                lblWind.textProperty().bind(t1.windSpeedProperty().asString());
                lblHumidity.textProperty().bind(t1.humidityProperty().asString());
                lblPressure.textProperty().bind(t1.pressureProperty().asString());
                lblCondition.textProperty().bind(t1.conditionProperty());
                lblAlert.textProperty().bind(t1.alertProperty().asString());
                lblLatitude.textProperty().bind(t1.latitudeProperty().asString());
                lblLongitude.textProperty().bind(t1.longitudeProperty().asString());
            }
        }));
    }

    public ObjectProperty<WeatherInfo> weatherInfoProperty() {
        return weatherInfo;
    }
}
