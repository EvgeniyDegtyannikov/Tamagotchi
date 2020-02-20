package sample.gui.controllers;

import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import sample.services.GameManager;
import sample.services.controllersStateHandlers.GameSpaceStateHandler;

import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class GameSpaceController implements Initializable {
    @FXML
    private ImageView moodIcon;
    @FXML
    private ImageView backgroundImage;
    @FXML
    private ImageView petsModelImage;
    @FXML
    private ImageView foodImage;

    private GameSpaceStateHandler gameSpaceStateHandler = GameManager.getInstance().getGameSpaceStateHandler();

    // �����, ���������� ������ ����������� ������ �������
    private class PetsModelMovementService {
        DoubleProperty petsModelCurrentPosition = new SimpleDoubleProperty(0.0);

        /*
        �����, ������������ ��������, ������������ ����� ���������� �������� ����� ����������. ������� ������ ���,
        ��������� GameSpace �� ���������� ��� � ������� ������������ � �� ��������� ��������. ���������
        petsModelCurrentPosition.
         */
        private void onPetsMovementFinish() {
            GameSpaceController.this.gameSpaceStateHandler.getGameSpace().setFeedInGameSpace(false);
            GameSpaceController.this.foodImage.setImage(null);
            GameSpaceController.this.gameSpaceStateHandler.closePetMovementHandlerThread();
            petsModelCurrentPosition.set(petsModelImage.getLayoutX());
            GameSpaceController.this.gameSpaceStateHandler.getGameSpace().setPetMoving(false);
            GameSpaceController.this.gameSpaceStateHandler.getGameSpace().getPet().feed();
        }

        /*
        �����, ����������� ��������� �� petsMovementFinalPositionByXProperty. ��� �������� ��������, ���� ������� ��
        ����� �������� ������������ � �������� �������� ����, �� GameSpace ������������ � ��� ��� ������� ���������
        � ��������� �����������. ��������� scheduleAtFixedRate, ������ 10 ����������� ������������ ������ ������� ��
        1�� �� ��� X. ���������� ������� �������� ��� ������ step(-1: ����, 1: �����). ���� ��������� �����������
        �� 1�� �������� � ������ �� ������ ���������� �� ��� X, �� �������� onPetsMovementFinish
         */
        private void startListenerOnPetsMovementFinalPositionByXProperty() {
            gameSpaceStateHandler.getPetsModelAfterMotionPositionByXProperty().addListener((observableValue, number, t1) -> {
                if (t1.doubleValue() >= 0 && backgroundImage.getFitWidth() > t1.doubleValue() + petsModelImage.getFitWidth()
                        && !GameSpaceController.this.gameSpaceStateHandler.getGameSpace().isPetMoving()) {
                    GameSpaceController.this.gameSpaceStateHandler.getGameSpace().setPetMoving(true);
                    GameSpaceController.this.gameSpaceStateHandler
                            .setPetMovementScheduledExecutorService(Executors.newSingleThreadScheduledExecutor());
                    GameSpaceController.this.gameSpaceStateHandler.getPetMovementScheduledExecutorService()
                            .scheduleAtFixedRate(() -> Platform.runLater(() -> {
                                double step;
                                if (t1.doubleValue() >= petsModelCurrentPosition.get()) step = 1;
                                else step = -1;
                                petsModelImage.setLayoutX(petsModelImage.getLayoutX() + step);
                                if ((petsModelImage.getLayoutX() + step > t1.doubleValue() && step >= 0) ||
                                        (petsModelImage.getLayoutX() + step < t1.doubleValue() && step < 0)) {
                                    onPetsMovementFinish();
                                }
                            }), 0, 10, TimeUnit.MILLISECONDS);
                }
            });
        }
    }

    // ����� ��� ��������� ������ � �������, ���������� ������ ������������� � �������� ����� ����.
    public void startMessagingWithGameSpaceStateHandler() {
        moodIcon.imageProperty().bind(this.gameSpaceStateHandler.getMoodIconProperty());
        backgroundImage.imageProperty().bind(this.gameSpaceStateHandler.getBackgroundImageProperty());
        petsModelImage.imageProperty().bind(this.gameSpaceStateHandler.getPetsModelImageProperty());
        foodImage.imageProperty().bindBidirectional(this.gameSpaceStateHandler.getFoodImageProperty());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Tooltip.install(moodIcon, new Tooltip("Mood"));
        startMessagingWithGameSpaceStateHandler();
        startListenerOnPetsImage();
        startPetsModelMovementServiceWork();
    }

    // �����, ���������� �� ��������� ������ PetsModelMovementService
    private void startPetsModelMovementServiceWork() {
        PetsModelMovementService petsModelMovementService = new PetsModelMovementService();
        petsModelMovementService.startListenerOnPetsMovementFinalPositionByXProperty();
    }

    /*
    �����, ����������� ��������� ��� petsModelImage. ��� ����� ��������, ������������� ������� �� ��� Y, ������ ��
    ����� backgroundImage, getImageByAge.
     */
    private void startListenerOnPetsImage() {
        petsModelImage.imageProperty().addListener(((observableValue, image, t1) -> {
            petsModelImage.setY(backgroundImage.getFitHeight() - gameSpaceStateHandler.getGameSpace().getPet()
                    .getPetsImagesManager().getImageByAge().getHeight());
        }));
    }

    // �����, ������������� �� ������ feed. ���� ������ ������� !=0, �� �������� ����� settleFood � �������� �������
    // �������� ������������ � ���.
    public void feedPet(ActionEvent actionEvent) {
        if (gameSpaceStateHandler.getGameSpace().getPet().getAge().get() != 0) {
            if (!gameSpaceStateHandler.getGameSpace().isFeedInGameSpace())
                settleFood();
            this.gameSpaceStateHandler.petGoFeeding(foodImage.getX());
        }
    }

    // �����, ����������� ��� �� ������� ����. ������������� ����������� ��� ���, ��������� ������ � ���������
    // GameSpace � ���, ��� ��� �� ������� ���� ������������
    private void settleFood() {
        foodImage.setImage(this.gameSpaceStateHandler.getGameSpace().getPet().getFood().getFoodImage());
        foodImage.setY(backgroundImage.getFitHeight() - gameSpaceStateHandler.getGameSpace().getPet().getFood()
                .getFoodImage().getHeight());
        foodImage.setX(new Random().nextInt((int) (backgroundImage.getFitWidth() - gameSpaceStateHandler
                .getGameSpace().getPet().getFood().getFoodImage().getWidth())));
        gameSpaceStateHandler.getGameSpace().setFeedInGameSpace(true);
    }
}
