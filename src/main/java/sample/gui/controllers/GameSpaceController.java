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

    //  ласс, содержащий логику перемещений модели питомца
    private class PetsModelMovementService {
        DoubleProperty petsModelCurrentPosition = new SimpleDoubleProperty(0.0);

        /*
        ћетод, определ€ющий действи€, происход€щие после достижени€ питомцем места назначени€. ”бирает модель еды,
        оповещает GameSpace об отсутствии еды в игровом пространстве и об окончании движени€. ќбновл€ет
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
        ћетод, запускающий слушател€ на petsMovementFinalPositionByXProperty. ѕри изменени значени€, если переход на
        новое значение осуществитс€ в пределах игрового пол€, то GameSpace уведомл€етс€ о том что питомец находитс€
        в состо€нии перемещени€. «апускает scheduleAtFixedRate, каждые 10 миллисекунд перемещающий модель питомца на
        1ед по оси X. ќпредел€ет сторону движени€ при помощи step(-1: лево, 1: право). ≈сли следующее перемещение
        на 1ед приведет к выходу за нужную координату по оси X, то вызывает onPetsMovementFinish
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

    // ћетод дл€ установки св€зей с классом, содержащим логику происход€щего в основной части игры.
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

    // ћетод, отвечающий за настройку работы PetsModelMovementService
    private void startPetsModelMovementServiceWork() {
        PetsModelMovementService petsModelMovementService = new PetsModelMovementService();
        petsModelMovementService.startListenerOnPetsMovementFinalPositionByXProperty();
    }

    /*
    ћетод, запускающий слушател€ дл€ petsModelImage. ѕри смене значени€, пересчитывает позицию по оси Y, исход€ из
    высот backgroundImage, getImageByAge.
     */
    private void startListenerOnPetsImage() {
        petsModelImage.imageProperty().addListener(((observableValue, image, t1) -> {
            petsModelImage.setY(backgroundImage.getFitHeight() - gameSpaceStateHandler.getGameSpace().getPet()
                    .getPetsImagesManager().getImageByAge().getHeight());
        }));
    }

    // ћетод, срабатывающий на кнопку feed. ≈сли взраст питомца !=0, то вызывает метод settleFood и передает питомцу
    // указание перемещатьс€ к ней.
    public void feedPet(ActionEvent actionEvent) {
        if (gameSpaceStateHandler.getGameSpace().getPet().getAge().get() != 0) {
            if (!gameSpaceStateHandler.getGameSpace().isFeedInGameSpace())
                settleFood();
            this.gameSpaceStateHandler.petGoFeeding(foodImage.getX());
        }
    }

    // ћетод, размещающий еду на игровом поле. ”станавливает изображение дл€ еды, размещает модель и оповещает
    // GameSpace о том, что еда на игровом поле присутствует
    private void settleFood() {
        foodImage.setImage(this.gameSpaceStateHandler.getGameSpace().getPet().getFood().getFoodImage());
        foodImage.setY(backgroundImage.getFitHeight() - gameSpaceStateHandler.getGameSpace().getPet().getFood()
                .getFoodImage().getHeight());
        foodImage.setX(new Random().nextInt((int) (backgroundImage.getFitWidth() - gameSpaceStateHandler
                .getGameSpace().getPet().getFood().getFoodImage().getWidth())));
        gameSpaceStateHandler.getGameSpace().setFeedInGameSpace(true);
    }
}
