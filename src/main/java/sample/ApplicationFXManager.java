package sample;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

//  ласс, управл€ющий общей графической логикой приложени€
public class ApplicationFXManager {
    private static final ApplicationFXManager INSTANCE = new ApplicationFXManager();
    private Stage primaryStage;
    private GamePage gameSpacePage;
    private GamePage gameOverPage;
    private GamePage gameMenuPage;

    private ApplicationFXManager() {
        gameSpacePage = new GamePage("/fxml/gameSpace.fxml", 800, 450);
        gameOverPage = new GamePage("/fxml/gameOver.fxml", 450, 450);
        gameMenuPage = new GamePage("/fxml/gameMenu.fxml", 450, 450);
    }

    //  ласс, реализующий различные игровые страницы(например меню, конец игры и т.д.)
    public static class GamePage {
        private FXMLLoader fxmlLoader;
        private Scene scene;

        private GamePage(String fxmlResource, double sceneWidth, double sceneHeight) {
            fxmlLoader = new FXMLLoader(getClass().getResource(fxmlResource));
            try {
                scene = new Scene(fxmlLoader.load(), sceneWidth, sceneHeight);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        public Scene getScene() {
            return scene;
        }
    }

    public static ApplicationFXManager getInstance() {
        return INSTANCE;
    }

    public GamePage getGameSpacePage() {
        return gameSpacePage;
    }

    public GamePage getGameOverPage() {
        return gameOverPage;
    }

    public GamePage getGameMenuPage() {
        return gameMenuPage;
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    // ћетод, позвол€ющий переключатьс€ между сценами во врем€ работы приложени€
    public void installSceneOnPrimaryStage(Scene scene) {
        primaryStage.setScene(scene);
    }
}
