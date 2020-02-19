package sample;

import javafx.application.Application;
import javafx.stage.Stage;
import sample.saving.SerializationSaveManager;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        ApplicationFXManager.getInstance().setPrimaryStage(primaryStage);
        GameManager.getInstance().setSaveManager(new SerializationSaveManager("savefile.txt"));
        GameManager.getInstance().onGameStart();
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void stop() {
        GameManager.getInstance().onGameStop();
    }
}
