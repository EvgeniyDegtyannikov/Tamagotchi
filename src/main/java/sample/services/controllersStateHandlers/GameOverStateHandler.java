package sample.services.controllersStateHandlers;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.scene.image.Image;
import javafx.util.Duration;
import sample.entities.Pet;
import sample.gameComponents.DeathReason;
import sample.services.ApplicationFXManager;

import java.util.Date;
import java.util.concurrent.TimeUnit;

// Класс, содержащий логику происходящего в окончании игры.
public class GameOverStateHandler {
    private final StringProperty titleProperty = new SimpleStringProperty();
    private final StringProperty deathReasonProperty = new SimpleStringProperty();
    private final ObjectProperty<Image> deathImageProperty = new SimpleObjectProperty<>();
    private final BooleanProperty goToGameMenuButtonVisibleProperty = new SimpleBooleanProperty();
    private final StringProperty waitingAfterDeathTextProperty = new SimpleStringProperty();

    // время, которое должно пройти перед созданием нового питомца
    private long timeToWaitAfterPetsDeath = 5;

    public StringProperty getTimeToGoToGameMenuButtonBecomeVisibleProperty() {
        return waitingAfterDeathTextProperty;
    }

    public BooleanProperty getGoToGameMenuButtonVisibleProperty() {
        return goToGameMenuButtonVisibleProperty;
    }

    public StringProperty getTitleProperty() {
        return titleProperty;
    }

    public StringProperty getDeathReasonProperty() {
        return deathReasonProperty;
    }

    public ObjectProperty<Image> getDeathImagePropertyProperty() {
        return deathImageProperty;
    }

    // Метод отвечающий за "похороны" питомца, вызывает методы installEpitaph и startWaitingAfterPetsDeath
    public void performFuneralRitesByPet(Pet pet, DeathReason deathReason) {
        installEpitaph(pet, deathReason);
        startWaitingAfterPetsDeath(pet.getLastUpdate());
    }

    // Метод, устанавливающий своеобразную "надгробную плиту" для питомца, выводя его имя, посмертное изображение
    // и причину смерти.
    private void installEpitaph(Pet pet, DeathReason deathReason) {
        titleProperty.set("Rest in peace, " + pet.getName());
        deathImageProperty.set(pet.getPetsImagesManager().getRipImage());
        deathReasonProperty.set(deathReason.getDeathMessage());
    }

    // Метод, отвечающий за посмертное ожидание перед созданием нового питомца.
    // Сперва, кнопка goToGameMenuButton, отвечающая за выход в меню, становится невидимой.
    // После, вычсляется разница между текущим моментом времени, и моментом lastUpdate питомца в секундах,
    // проверяется разница между timeToWaitAfterPetsDeath и полученной разностью (если <= 0 то 1)
    // и запускается Timeline, которые по прошествии этой разницы делает кнопку goToGameMenuButton видимой.
    private void startWaitingAfterPetsDeath(Date petsLastUpdate) {
        goToGameMenuButtonVisibleProperty.set(false);
        waitingAfterDeathTextProperty.set("Between death of your pet, " +
                "and creation of new pet, must pass at least " + timeToWaitAfterPetsDeath + " seconds");
        long timeDiff = new Date().getTime() - petsLastUpdate.getTime();
        long timeDiffInSeconds = TimeUnit.MILLISECONDS.toSeconds(timeDiff);
        long timeToWaitDiff = timeToWaitAfterPetsDeath - timeDiffInSeconds;
        if (timeToWaitDiff <= 0) timeToWaitDiff = 1;
        Timeline timeline = new Timeline(new KeyFrame(Duration
                .seconds(timeToWaitDiff),
                e -> goToGameMenuButtonVisibleProperty.set(true)));
        Platform.runLater(timeline::play);
    }

    // Метод, осуществляющий переход на страницу gameMenuPage.
    public void goToGameMenu() {
        ApplicationFXManager.getInstance().installSceneOnPrimaryStage(ApplicationFXManager.getInstance()
                .getGameMenuPage().getScene());
    }
}
