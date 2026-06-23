package src;

import javafx.application.Application;
import javafx.stage.Stage;
import src.ui.MainFormWindow;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        new MainFormWindow().show(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}