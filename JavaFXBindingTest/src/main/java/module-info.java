module com.javafxbindingtest {
    requires javafx.controls;
    requires javafx.fxml;


    exports com.javafxbindingtest.app;
    exports com.javafxbindingtest.domain;


    opens com.javafxbindingtest.app to javafx.fxml;
    opens com.javafxbindingtest.presentation.control to javafx.fxml;
    opens com.javafxbindingtest.presentation.view to javafx.fxml;
}