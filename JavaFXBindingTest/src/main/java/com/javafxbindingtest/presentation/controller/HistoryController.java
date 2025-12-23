package com.javafxbindingtest.presentation.controller;

import com.javafxbindingtest.application.usecase.GetWeatherHistoryUseCase;
import com.javafxbindingtest.domain.model.WeatherInfo;
import com.javafxbindingtest.infrastructure.messaging.mongo.MongoWeatherHistoryRepository;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class HistoryController {
    // Tabla principal que muestra el historial
    @FXML
    private TableView<WeatherInfo> table;

    // Columnas de la tabla (inyectadas desde el FXML)
    @FXML
    private TableColumn<WeatherInfo, Double> tempCol;

    @FXML
    private TableColumn<WeatherInfo, Double> windCol;

    @FXML
    private TableColumn<WeatherInfo, Double> humidityCol;

    @FXML
    private TableColumn<WeatherInfo, Double> pressureCol;

    @FXML
    private TableColumn<WeatherInfo, String> conditionCol;

    // Lista observable que alimenta la tabla
    // Cualquier cambio aquí se refleja automáticamente en la UI
    private final ObservableList<WeatherInfo> data =
            FXCollections.observableArrayList();

    // Executor que se encarga de actualizar los datos periódicamente
    private ScheduledExecutorService scheduler;

    // Caso de uso que obtiene el historial desde MongoDB
    private GetWeatherHistoryUseCase useCase;

    /**
     * Metodo llamado automáticamente por JavaFX
     * cuando se carga el FXML
     */
    public void initialize() {

        // Se inicializa el caso de uso con su repositorio concreto
        useCase = new GetWeatherHistoryUseCase(
                new MongoWeatherHistoryRepository()
        );

        // Se enlazan las columnas con los atributos de WeatherInfo
        tempCol.setCellValueFactory(
                new PropertyValueFactory<>("temperature")
        );

        windCol.setCellValueFactory(
                new PropertyValueFactory<>("windSpeed")
        );

        humidityCol.setCellValueFactory(
                new PropertyValueFactory<>("humidity")
        );

        pressureCol.setCellValueFactory(
                new PropertyValueFactory<>("pressure")
        );

        conditionCol.setCellValueFactory(
                new PropertyValueFactory<>("condition")
        );

        // Se conecta la lista observable a la tabla
        table.setItems(data);

        // Carga inicial de datos
        loadData();

        // Scheduler que refresca el historial cada 3 segundos
        scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(
                this::loadData, // tarea
                3,              // delay inicial
                3,              // intervalo
                TimeUnit.SECONDS
        );
    }

    /**
     * Consulta MongoDB y actualiza la tabla
     * Se ejecuta en un hilo en segundo plano
     */
    private void loadData() {

        // Se obtiene el historial desde el caso de uso
        List<WeatherInfo> history = useCase.execute();

        // Se actualiza la UI en el hilo de JavaFX
        Platform.runLater(() -> {
            data.setAll(history);
        });
    }

    /**
     * Debe llamarse cuando la ventana se cierra
     * para liberar el hilo del scheduler
     */
    public void shutdown() {
        if (scheduler != null) {
            scheduler.shutdownNow();
        }
    }
}
