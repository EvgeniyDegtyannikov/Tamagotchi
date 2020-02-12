package sample.FXComponents.controllers;

import javafx.animation.TranslateTransition;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import sample.FXComponents.stateViewers.GameSpaceStateHandler;

import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML
    private Parent root;
    @FXML
    private ImageView moodIcon;
    @FXML
    private ImageView backgroundImage;
    @FXML
    private ImageView petsModelImage;
    @FXML
    private ImageView foodImage;

    private GameSpaceStateHandler gameSpaceStateHandler;

    public void setGameSpaceStateHandler(GameSpaceStateHandler gameSpaceStateHandler) {
        this.gameSpaceStateHandler = gameSpaceStateHandler;
        this.gameSpaceStateHandler.setCurrentScene(root.getScene());
        moodIcon.imageProperty().bind(this.gameSpaceStateHandler.getMoodIconProperty());
        backgroundImage.imageProperty().bind(this.gameSpaceStateHandler.getBackgroundImageProperty());
        petsModelImage.imageProperty().bind(this.gameSpaceStateHandler.getPetsModelImageProperty());
        foodImage.imageProperty().bindBidirectional(this.gameSpaceStateHandler.getFoodImageProperty());

        petsModelImage.imageProperty().addListener(((observableValue, image, t1) -> {
            petsModelImage.setY(backgroundImage.getFitHeight()-gameSpaceStateHandler.getGameSpace().getPet().getImageByAge().getHeight());
        }));

        DoubleProperty petsMovementFinalPositionByXProperty=new SimpleDoubleProperty();
        DoubleProperty petsModelCurrentPosition=new SimpleDoubleProperty(0.0);
        petsModelCurrentPosition.bindBidirectional(this.gameSpaceStateHandler.getPetsModelCurrentPositionByXProperty());
        petsMovementFinalPositionByXProperty.bindBidirectional(this.gameSpaceStateHandler.getPetsModelAfterMotionPositionByXProperty());
        petsMovementFinalPositionByXProperty.addListener((observableValue, number, t1) -> {
            if ((t1.doubleValue()>=0 && backgroundImage.getFitWidth()>petsModelCurrentPosition.get()+t1.intValue()+petsModelImage.getFitWidth()) ||
                    ((t1.doubleValue()<0)&&(petsModelCurrentPosition.get()+t1.doubleValue()>=0))){
                TranslateTransition translateTransition = new TranslateTransition();
                translateTransition.setByX(t1.intValue());
                petsModelCurrentPosition.set(petsModelCurrentPosition.get()+t1.doubleValue());
                translateTransition.setDuration(Duration.millis(1000));
                translateTransition.setNode(petsModelImage);
                translateTransition.setOnFinished(e->{
                    petsMovementFinalPositionByXProperty.set(0);
                    this.gameSpaceStateHandler.getGameSpace().setFeedInGameSpace(false);
                    this.foodImage.setImage(null);
                });
                translateTransition.play();
            }
        });
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Tooltip.install(moodIcon,new Tooltip("Mood"));
    }

    public void feedPet(ActionEvent actionEvent){
        if (!gameSpaceStateHandler.getGameSpace().isFeedInGameSpace() && gameSpaceStateHandler.getGameSpace().getPet().getAge().get()!=0) {
            foodImage.setImage(this.gameSpaceStateHandler.getGameSpace().getPet().getFood().getFoodImage());
            foodImage.setY(backgroundImage.getFitHeight() - gameSpaceStateHandler.getGameSpace().getPet().getFood().getFoodImage().getHeight());
            foodImage.setX(new Random().nextInt((int) (backgroundImage.getFitWidth() - gameSpaceStateHandler.getGameSpace().getPet().getFood().getFoodImage().getWidth())));
            gameSpaceStateHandler.getGameSpace().setFeedInGameSpace(true);
            this.gameSpaceStateHandler.petGoFeeding(foodImage.getX());
        }
    }
}
