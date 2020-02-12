package sample.FXComponents.stateViewers;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.util.Duration;
import sample.pets.Pet;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class GameOverStateHandler {
    private final StringProperty titleProperty=new SimpleStringProperty();
    private final StringProperty deathReasonProperty=new SimpleStringProperty();
    private final ObjectProperty<Image> deathImageProperty=new SimpleObjectProperty<>();
    private final BooleanProperty goToGameMenuButtonVisibleProperty =new SimpleBooleanProperty();
    private final StringProperty timeTogoToGameMenuButtonBecomeVisibleProperty=new SimpleStringProperty();
    private Scene linkOnMenuScene;
    private Scene currentScene;

    private long timeToWaitAfterPetsDeath=5;

    public void setCurrentScene(Scene currentScene) {
        this.currentScene = currentScene;
    }

    public StringProperty getTimeToGoToGameMenuButtonBecomeVisibleProperty() {
        return timeTogoToGameMenuButtonBecomeVisibleProperty;
    }

    public void setTimeTogoToGameMenuButtonBecomeVisibleProperty(String timeTogoToGameMenuButtonBecomeVisibleProperty) {
        this.timeTogoToGameMenuButtonBecomeVisibleProperty.set(timeTogoToGameMenuButtonBecomeVisibleProperty);
    }

    public BooleanProperty getGoToGameMenuButtonVisibleProperty() {
        return goToGameMenuButtonVisibleProperty;
    }

    public void setGoToGameMenuButtonVisibleProperty(boolean goToGameMenuButtonVisibleProperty) {
        this.goToGameMenuButtonVisibleProperty.set(goToGameMenuButtonVisibleProperty);
    }

    public StringProperty getTitleProperty() {
        return titleProperty;
    }

    public void setTitleProperty(String titleProperty) {
        this.titleProperty.set(titleProperty);
    }

    public StringProperty getDeathReasonProperty() {
        return deathReasonProperty;
    }

    public void setDeathReasonProperty(String deathReasonProperty) {
        this.deathReasonProperty.set(deathReasonProperty);
    }

    public ObjectProperty<Image> getDeathImagePropertyProperty() {
        return deathImageProperty;
    }

    public void setDeathImageProperty(Image deathImageProperty) {
        this.deathImageProperty.set(deathImageProperty);
    }

    public void installEpitaph(Pet pet, String deathReason){
        titleProperty.set("Rest in peace, "+pet.getName());
        deathImageProperty.set(pet.getRipImage());
        deathReasonProperty.set(deathReason);
        long timeDiff=new Date().getTime()-pet.getLastUpdate().getTime();
        long timeDiffInSeconds= TimeUnit.MILLISECONDS.toSeconds(timeDiff);
        long timeToWaitDiff=timeToWaitAfterPetsDeath-timeDiffInSeconds;
        if (timeToWaitDiff<=0)timeToWaitDiff=1;
        timeTogoToGameMenuButtonBecomeVisibleProperty.set("Before creation of new pet you have to wait: "+timeToWaitDiff+" seconds");
        Timeline timeline = new Timeline(new KeyFrame(Duration
                .seconds(timeToWaitDiff),
                e -> goToGameMenuButtonVisibleProperty.set(true)));
        Platform.runLater(timeline::play);
    }

    public void setLinkOnMenuScene(Scene linkOnMenuScene) {
        this.linkOnMenuScene = linkOnMenuScene;
    }

    public void goToGameMenu(){
        Stage stage=(Stage)currentScene.getWindow();
        stage.setScene(linkOnMenuScene);
    }
}
