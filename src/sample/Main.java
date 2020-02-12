package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sample.FXComponents.GameSpace;
import sample.FXComponents.controllers.Controller;
import sample.FXComponents.controllers.GameOverController;
import sample.FXComponents.controllers.MenuController;
import sample.FXComponents.stateViewers.GameOverStateHandler;
import sample.FXComponents.stateViewers.GameSpaceStateHandler;
import sample.FXComponents.stateViewers.MenuStateHandler;
import sample.mediator.Mediator;
import sample.mediator.MediatorImpl;
import sample.pets.Pet;
import sample.saving.SaveManager;
import sample.saving.SerializationSaveManager;

public class Main extends Application {
    private Mediator mediator;
    private SaveManager saveManager;

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader gameSpaceFxmlLoader = new FXMLLoader(getClass().getResource("../resources/standardGamingSpace.fxml"));
        Parent gameSpaceRoot = gameSpaceFxmlLoader.load();
        Scene gameSpaceScene=new Scene(gameSpaceRoot,800,450);
        Controller gameSpaceController= gameSpaceFxmlLoader.getController();

        FXMLLoader gameMenuFxmlLoader = new FXMLLoader(getClass().getResource("../resources/gameMenu.fxml"));
        Parent gameMenuRoot= gameMenuFxmlLoader.load();
        Scene gameMenuScene=new Scene(gameMenuRoot, 450,450);
        MenuController gameMenuController= gameMenuFxmlLoader.getController();

        FXMLLoader gameOverFxmlLoader = new FXMLLoader(getClass().getResource("../resources/gameOver.fxml"));
        Parent gameOverRoot= gameOverFxmlLoader.load();
        Scene gameOverScene=new Scene(gameOverRoot, 450,450);
        GameOverController gameOverController= gameOverFxmlLoader.getController();

        saveManager =new SerializationSaveManager("savefile.txt");
        mediator=new MediatorImpl();

        GameOverStateHandler gameOverStateHandler =new GameOverStateHandler();
        gameOverStateHandler.setLinkOnMenuScene(gameMenuScene);
        mediator.setGameOverStateHandler(gameOverStateHandler);
        gameOverController.setGameOverStateHandler(gameOverStateHandler);

        MenuStateHandler menuStateHandler=new MenuStateHandler();
        menuStateHandler.setLinkOnGameSpaceScene(gameSpaceScene);
        menuStateHandler.setMediator(mediator);
        mediator.setMenuStateHandler(menuStateHandler);
        gameMenuController.setMenuStateHandler(menuStateHandler);

        GameSpaceStateHandler gameSpaceStateHandler=new GameSpaceStateHandler();
        gameSpaceStateHandler.setLinkOnGameOverScene(gameOverScene);
        gameSpaceStateHandler.setMediator(mediator);
        mediator.setGameSpaceStateHandler(gameSpaceStateHandler);
        gameSpaceController.setGameSpaceStateHandler(gameSpaceStateHandler);
        gameSpaceStateHandler.setGameSpace(new GameSpace());

        Pet loadedPetFromSaveFile= saveManager.loadSave();
        if (loadedPetFromSaveFile!=null){
            primaryStage.setScene(gameSpaceScene);
            mediator.startGameProcessWithSpecifiedPet(loadedPetFromSaveFile);
        } else {
            primaryStage.setScene(gameMenuScene);
        }

        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void stop() throws Exception {
        saveManager.makeSave(mediator.getCurrentGamePet());
        mediator.closeHandlersThreads();
    }
}
