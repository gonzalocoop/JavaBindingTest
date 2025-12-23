package com.javafxbindingtest.presentation.controller;

import com.javafxbindingtest.application.usecase.ObserveWeatherUseCase;
import com.javafxbindingtest.domain.model.WeatherInfo;
import com.javafxbindingtest.infrastructure.cache.RedisWeatherCache;
import com.javafxbindingtest.infrastructure.messaging.mongo.MongoWeatherHistoryRepository;
import com.javafxbindingtest.infrastructure.messaging.zmq.ZmqWeatherClient;
import com.javafxbindingtest.presentation.view.WeatherControl;

import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class DashboardController {
    @FXML
    private WeatherControl weatherControl;
    // Control visual inyectado desde FXML

    private final ObjectProperty<WeatherInfo> weather =
            new SimpleObjectProperty<>();
    // Propiedad observable para el clima

    private ObserveWeatherUseCase useCase;
    // Caso de uso

    public void initialize() {
        // Método llamado automáticamente por JavaFX

        weatherControl.weatherInfoProperty().bind(weather);
        // Binding: cuando weather cambia, la UI se actualiza

        useCase = new ObserveWeatherUseCase(
                new ZmqWeatherClient(),
                new RedisWeatherCache(),
                new MongoWeatherHistoryRepository()
        );
        // Se crean e inyectan las dependencias

        useCase.start(info ->
                Platform.runLater(() -> weather.set(info))
        );
        // Inicia el caso de uso
        // Platform.runLater asegura ejecución en el hilo JavaFX
    }


    /**
     * Metodo asociado al botón "Ver historial".
     * Abre una nueva ventana mostrando el historial del clima almacenado en MongoDB.
     */
    @FXML
    private void openHistory() throws Exception {

        // Se carga el archivo FXML de la vista de historial
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource(
                        "/com/javafxbindingtest/presentation/view/HistoryView.fxml"
                )
        );

        // Se crea una nueva ventana (Stage)
        Stage stage = new Stage();

        // Se carga la escena desde el FXML
        stage.setScene(new Scene(loader.load()));

        // Se define el título de la ventana
        stage.setTitle("Weather History");

        //Para que se detengan los hilos
        HistoryController controller = loader.getController();

        stage.setOnCloseRequest(e -> controller.shutdown());

        // Se muestra la ventana
        stage.show();
    }
}
