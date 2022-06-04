package ru.gb.Chatterbox.client;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Application extends javafx.application.Application {
    public static Stage primaryStage;

    public static void run(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Application.primaryStage = primaryStage;
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(this.getClass().getResource("/ChatWindow.fxml"));
        Parent parent = loader.load();
        Scene scene = new Scene(parent);
        scene.getStylesheets().add("styles.css");
        primaryStage.setResizable(false);
        primaryStage.setTitle("Authorization");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
