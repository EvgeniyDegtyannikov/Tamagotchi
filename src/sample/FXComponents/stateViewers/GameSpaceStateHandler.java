package sample.FXComponents.stateViewers;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.util.Duration;
import sample.FXComponents.GameSpace;
import sample.gameComponents.Weather;
import sample.mediator.Mediator;
import sample.pets.Pet;

import java.util.Date;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class GameSpaceStateHandler {
    private final ObjectProperty<Image> moodIconProperty=new SimpleObjectProperty<>();
    private final ObjectProperty<Image> petsModelImageProperty=new SimpleObjectProperty<>();
    private final ObjectProperty<Image> backgroundImageProperty=new SimpleObjectProperty<>();
    private final ObjectProperty<Image> foodImageProperty=new SimpleObjectProperty<>();
    private final DoubleProperty petsModelAfterMotionPositionByXProperty =new SimpleDoubleProperty();
    private final DoubleProperty petsModelCurrentPositionByXProperty =new SimpleDoubleProperty();

    private Mediator mediator;
    private ScheduledExecutorService petScheduledExecutorService;
    private ScheduledExecutorService gameSpaceScheduledExecutorService;
    private Scene linkOnGameOverScene;
    private Scene currentScene;
    private GameSpace gameSpace;

    private Image moodGoodImage=new Image("images/icons/mood/mood_good.jpg");
    private Image moodNormalImage=new Image("images/icons/mood/mood_normal.jpg");
    private Image moodBadImage=new Image("images/icons/mood/mood_bad.jpg");
    private Image moodDeadImage=new Image("images/icons/mood/mood_dead.jpg");

    private Image backgroundImage;

    public void setGameSpace(GameSpace gameSpace) {
        this.gameSpace = gameSpace;
    }

    public GameSpace getGameSpace() {
        return gameSpace;
    }

    public void setCurrentScene(Scene currentScene) {
        this.currentScene = currentScene;
    }

    public ObjectProperty<Image> getFoodImageProperty() {
        return foodImageProperty;
    }

    public void setFoodImageProperty(Image foodImageProperty) {
        this.foodImageProperty.set(foodImageProperty);
    }

    public DoubleProperty getPetsModelCurrentPositionByXProperty() {
        return petsModelCurrentPositionByXProperty;
    }

    public void setPetsModelCurrentPositionByXProperty(double petsModelCurrentPositionByXProperty) {
        this.petsModelCurrentPositionByXProperty.set(petsModelCurrentPositionByXProperty);
    }

    public ObjectProperty<Image> getPetsModelImageProperty() {
        return petsModelImageProperty;
    }

    public DoubleProperty getPetsModelAfterMotionPositionByXProperty() {
        return petsModelAfterMotionPositionByXProperty;
    }

    public void setPetsModelAfterMotionPositionByXProperty(double petsModelAfterMotionPositionByXProperty) {
            this.petsModelAfterMotionPositionByXProperty.set(petsModelAfterMotionPositionByXProperty);
    }

    public void setPetsModelImageProperty(Image petsModelImageProperty) {
        this.petsModelImageProperty.set(petsModelImageProperty);
    }

    public ObjectProperty<Image> getBackgroundImageProperty() {
        return backgroundImageProperty;
    }

    public void setBackgroundImageProperty(Image backgroundImageProperty) {
        this.backgroundImageProperty.set(backgroundImageProperty);
    }

    public ObjectProperty<Image> getMoodIconProperty() {
        return moodIconProperty;
    }

    public void setMoodIconProperty(Image moodIconProperty) {
        this.moodIconProperty.set(moodIconProperty);
    }

    public void setLinkOnGameOverScene(Scene linkOnGameOverScene) {
        this.linkOnGameOverScene = linkOnGameOverScene;
    }

    public void setMediator(Mediator mediator) {
        this.mediator = mediator;
    }

    public void startGameProcess(Pet pet){
        if (gameSpaceScheduledExecutorService==null) {
            gameSpaceScheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
            gameSpaceScheduledExecutorService.scheduleAtFixedRate(new Runnable() {
                @Override
                public void run() {
                    installRandomWeatherImage();
                }
            }, 0, 4, TimeUnit.SECONDS);
        }
        this.gameSpace.setPet(pet);
        moodIconProperty.set(null);
        if (pet.isAlive()) {
            settlePetsImage();
            startListenerOnPetsAge();
            if (this.gameSpace.getPet().getAge().isEqualTo(0).get()) {
                System.out.println("Time to be born: "+this.gameSpace.getPet().getTimeToBeBornInSeconds());
                Timeline timeline = new Timeline(new KeyFrame(Duration
                        .seconds(this.gameSpace.getPet().getTimeToBeBornInSeconds()), e -> this.gameSpace.getPet().setAge(1)));
                Platform.runLater(timeline::play);
            } else startLiveProcessForBornPet();
        } else goToGameOver("Already dead");
    }

    private void startLiveProcessForBornPet(){
        if (petScheduledExecutorService==null) {
            petScheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
            petScheduledExecutorService.scheduleAtFixedRate(gameSpace.getPet(),
                    gameSpace.getPet().getNeedChangeTimeInSeconds(),
                    gameSpace.getPet().getNeedChangeTimeInSeconds(),
                    TimeUnit.SECONDS);
        }
        startListenerOnPetsSatiety();
//        scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
//            @Override
//            public void run() {
//                if (new Random().nextInt(100)< gameSpace.getPet().getSatiety().get()) {
//                    petGoRunAround();
//                }
//            }
//        }, 1,1,TimeUnit.SECONDS);
        installMoodIconBySatiety(gameSpace.getPet().getSatiety().get());
    }

    private void startListenerOnPetsAge(){
        ChangeListener<Number> petAgeListener=new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                settlePetsImage();
                if (t1.intValue()==1) {
                    startLiveProcessForBornPet();
                }
            }
        };
        gameSpace.getPet().getAge().addListener(petAgeListener);
    }

    private void startListenerOnPetsSatiety() {
        ChangeListener<Number> petSatietyListener=new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                installMoodIconBySatiety(t1.intValue());
                if (t1.intValue()<1){
                    gameSpace.getPet().setAlive(false);
                    gameSpace.getPet().setLastUpdate(new Date());
                    gameSpace.getPet().getSatiety().removeListener(this);
                    closePetStateHandlerThread();
                    goToGameOver("Death from starvation");
                }
            }
        };
        gameSpace.getPet().getSatiety().addListener(petSatietyListener);
    }

    private void installMoodIconBySatiety(int satiety){
        if (satiety>55)moodIconProperty.set(moodGoodImage);
        else if (satiety>25)moodIconProperty.set(moodNormalImage);
        else if (satiety>0)moodIconProperty.set(moodBadImage);
        else moodIconProperty.set(moodDeadImage);
    }

    private void installRandomWeatherImage(){
        int i=new Random().nextInt(3);
        if (i==0) {
            getGameSpace().setWeather(Weather.STANDARD);
        }
        if (i==1) {
            getGameSpace().setWeather(Weather.RAIN);
        }
        if (i==2) {
            getGameSpace().setWeather(Weather.SNOW);
        }
        getBackgroundImageProperty().set(getGameSpace().getWeather().getWeatherImage());
    }

    public void closePetStateHandlerThread() {
        if (petScheduledExecutorService !=null) {
            petScheduledExecutorService.shutdown();
        }
    }

    public void closeGameSpaceStateHandlerThread() {
        if (gameSpaceScheduledExecutorService !=null) {
            gameSpaceScheduledExecutorService.shutdown();
        }
    }
    public void goToGameOver(String deathReason){
        Stage stage=(Stage)currentScene.getWindow();
        mediator.sendPetOnEpitaph(gameSpace.getPet(), deathReason);
        stage.setScene(linkOnGameOverScene);
    }

    private void settlePetsImage(){
        if (this.gameSpace.getPet().getAge().isEqualTo(0).get()) {
            petMoveByX(-getPetsModelCurrentPositionByXProperty().get());
        }
        getPetsModelImageProperty().set(gameSpace.getPet().getImageByAge());
    }

//    private void petGoRunAround(){
//        if (getGameSpace().isFeedInGameSpace())
//            return;
//        int side=new Random().nextInt(3);
//        if (side==1){
//            petMoveByX(new Random().nextInt(100));
//        }
//        if (side==2){
//            petMoveByX(-(new Random().nextInt(100)));
//        }
//    }

    private void petMoveByX(double x){
        if (petsModelAfterMotionPositionByXProperty.get()==0)
            setPetsModelAfterMotionPositionByXProperty(x);
    }

    public void petGoFeeding(double x){
        setPetsModelAfterMotionPositionByXProperty(x-getPetsModelCurrentPositionByXProperty().get());
    }
}
