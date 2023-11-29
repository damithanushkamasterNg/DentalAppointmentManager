package com.toothcare.launcher;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

public class Launcher extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Parent parent = FXMLLoader.load(Objects.requireNonNull(this.getClass().getResource("/view/dashboard/dashboard_form.fxml")));
        Scene scene = new Scene(parent);
        stage.setTitle("My Form");
        stage.centerOnScreen();
        stage.setScene(scene);
        stage.show();
    }
}
