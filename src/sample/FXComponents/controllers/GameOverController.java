package sample.FXComponents.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import sample.FXComponents.stateViewers.GameOverStateHandler;

public class GameOverController {
    @FXML
    private Parent root;
    @FXML
    private ImageView deathImage;
    @FXML
    private Label title;
    @FXML
    private Label deathReason;
    @FXML
    private Button goToGameMenuButton;
    @FXML
    private Label timeTogoToGameMenuButtonBecomeVisible;

    private GameOverStateHandler gameOverStateHandler;

    public void setGameOverStateHandler(GameOverStateHandler gameOverStateHandler) {
        this.gameOverStateHandler = gameOverStateHandler;
        this.gameOverStateHandler.setCurrentScene(root.getScene());
        title.textProperty().bind(this.gameOverStateHandler.getTitleProperty());
        deathImage.imageProperty().bind(this.gameOverStateHandler.getDeathImagePropertyProperty());
        deathReason.textProperty().bind(this.gameOverStateHandler.getDeathReasonProperty());
        goToGameMenuButton.visibleProperty().bindBidirectional(this.gameOverStateHandler.getGoToGameMenuButtonVisibleProperty());
        timeTogoToGameMenuButtonBecomeVisible.textProperty().bind(this.gameOverStateHandler.getTimeToGoToGameMenuButtonBecomeVisibleProperty());
    }

    public void goToGameMenu(ActionEvent actionEvent){
        gameOverStateHandler.goToGameMenu();
        goToGameMenuButton.setVisible(false);
    }
}
