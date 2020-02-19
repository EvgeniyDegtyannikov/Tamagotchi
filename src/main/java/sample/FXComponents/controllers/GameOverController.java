package sample.FXComponents.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import sample.FXComponents.stateHandlers.GameOverStateHandler;
import sample.GameManager;

import java.net.URL;
import java.util.ResourceBundle;

public class GameOverController implements Initializable {
    @FXML
    private ImageView deathImage;
    @FXML
    private Label title;
    @FXML
    private Label deathReason;
    @FXML
    private Button goToGameMenuButton;
    @FXML
    private Label waitingAfterDeathTextProperty;

    private GameOverStateHandler gameOverStateHandler = GameManager.getInstance().getGameOverStateHandler();

    // ����� ��� ��������� ������ � �������, ���������� ������ ������������� � ��������� ����.
    // ������������� ������������ ����� ����� ����� deathImage � ��������� deathImageProperty ��
    // GameOverStateHandler ������, � �.�.
    public void startMessagingWithGameOverStateHandler() {
        title.textProperty().bind(this.gameOverStateHandler.getTitleProperty());
        deathImage.imageProperty().bind(this.gameOverStateHandler.getDeathImagePropertyProperty());
        deathReason.textProperty().bind(this.gameOverStateHandler.getDeathReasonProperty());
        goToGameMenuButton.visibleProperty().bindBidirectional(this.gameOverStateHandler
                .getGoToGameMenuButtonVisibleProperty());
        waitingAfterDeathTextProperty.textProperty().bind(this.gameOverStateHandler
                .getTimeToGoToGameMenuButtonBecomeVisibleProperty());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        startMessagingWithGameOverStateHandler();
    }

    // �����, ������������� ��� ������� �� ������ goToGameMenuButton. �������� ����� �������� �� �������� gameMenuPage,
    // �� gameOverStateHandler.
    public void goToGameMenu(ActionEvent actionEvent) {
        gameOverStateHandler.goToGameMenu();
    }
}
