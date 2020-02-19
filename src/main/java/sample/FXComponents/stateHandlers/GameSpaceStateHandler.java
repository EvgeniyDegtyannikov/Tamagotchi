package sample.FXComponents.stateHandlers;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.image.Image;
import javafx.util.Duration;
import sample.ApplicationFXManager;
import sample.FXComponents.GameSpace;
import sample.GameManager;
import sample.gameComponents.DeathReason;
import sample.gameComponents.Mood;
import sample.gameComponents.Weather;
import sample.pets.Pet;

import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class GameSpaceStateHandler {
    private final ObjectProperty<Image> moodIconProperty = new SimpleObjectProperty<>();
    private final ObjectProperty<Image> petsModelImageProperty = new SimpleObjectProperty<>();
    private final ObjectProperty<Image> backgroundImageProperty = new SimpleObjectProperty<>();
    private final ObjectProperty<Image> foodImageProperty = new SimpleObjectProperty<>();
    private final DoubleProperty petsModelAfterMotionPositionByXProperty = new SimpleDoubleProperty();

    private ScheduledExecutorService petStateScheduledExecutorService;
    private ScheduledExecutorService gameSpaceScheduledExecutorService;
    private ScheduledExecutorService petMovementScheduledExecutorService;
    private GameSpace gameSpace;

    public void setGameSpace(GameSpace gameSpace) {
        this.gameSpace = gameSpace;
    }

    public GameSpace getGameSpace() {
        return gameSpace;
    }

    public ObjectProperty<Image> getFoodImageProperty() {
        return foodImageProperty;
    }

    public ObjectProperty<Image> getPetsModelImageProperty() {
        return petsModelImageProperty;
    }

    public ScheduledExecutorService getPetMovementScheduledExecutorService() {
        return petMovementScheduledExecutorService;
    }

    public void setPetMovementScheduledExecutorService(ScheduledExecutorService petMovementScheduledExecutorService) {
        this.petMovementScheduledExecutorService = petMovementScheduledExecutorService;
    }

    public DoubleProperty getPetsModelAfterMotionPositionByXProperty() {
        return petsModelAfterMotionPositionByXProperty;
    }

    public void setPetsModelAfterMotionPositionByXProperty(double petsModelAfterMotionPositionByXProperty) {
        this.petsModelAfterMotionPositionByXProperty.set(petsModelAfterMotionPositionByXProperty);
    }

    public ObjectProperty<Image> getBackgroundImageProperty() {
        return backgroundImageProperty;
    }

    public ObjectProperty<Image> getMoodIconProperty() {
        return moodIconProperty;
    }

    // �����, ���������� �� ����� ������. ��������� scheduleAtFixedRate, ���������� ������ 4 �������
    // installRandomWeatherImage
    private void startWeatherChangeScheduledThread() {
        if (gameSpaceScheduledExecutorService == null)
            gameSpaceScheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        gameSpaceScheduledExecutorService.scheduleAtFixedRate(this::installRandomWeatherImage, 0, 4, TimeUnit.SECONDS);
    }

    // �����, ���������� �� �������� �������. ������� �����, ���������� �� �������� � ������������� ������� � �������� 1
    private void startPetsBornTimeline() {
        System.out.println("Time to be born: " + this.gameSpace.getPet().getTimeToBeBornInSeconds());
        Timeline timeline = new Timeline(new KeyFrame(Duration
                .seconds(this.gameSpace.getPet().getTimeToBeBornInSeconds()), e -> this.gameSpace.getPet().setAge(1)));
        Platform.runLater(timeline::play);
    }

    // �����, ���������� ������� ������� ���������������. ������������� GameSpace � �������, ��������� �����,
    // ���������� �� ����� ������ startWeatherChangeScheduledThread, ������������� �������� moodIconProperty � null.
    // ���� ������� isAlive, �� ��������� ��� ����������� settlePetsImage � ��������� ��������� �� ��������� ���
    // ��������. ���� ������� ������� ==0, �� ��������� ����� startPetsBornTimeline, ����� ��������� �����
    // startLiveProcessForBornPet. ���� ������� �� isAlive, �������� goToGameOver � �������� ������ ALREADY_DEAD
    public void startGameProcess(Pet pet) {
        this.setGameSpace(new GameSpace());
        startWeatherChangeScheduledThread();
        this.gameSpace.setPet(pet);
        moodIconProperty.set(null);
        if (pet.isAlive()) {
            settlePetsImage();
            startListenerOnPetsAge();
            if (this.gameSpace.getPet().getAge().get() == 0) startPetsBornTimeline();
            else startLiveProcessForBornPet();
        } else goToGameOver(DeathReason.ALREADY_DEAD);
    }

    // �����, ���������� ��������� ������� �������. ��������� scheduleAtFixedRate, ����������� ����� run �� Pet
    // ����� getNeedChangeTimeInSeconds ������� � ����� � ����� �� �����������. ��������� ��������� �� �������
    // ������� � ������������� installMoodIconBySatiety �� ������� �������.
    private void startLiveProcessForBornPet() {
        petStateScheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        petStateScheduledExecutorService.scheduleAtFixedRate(gameSpace.getPet(),
                gameSpace.getPet().getNeedChangeTimeInSeconds(),
                gameSpace.getPet().getNeedChangeTimeInSeconds(),
                TimeUnit.SECONDS);
        startListenerOnPetsSatiety();
        installMoodIconBySatiety(gameSpace.getPet().getSatiety().get());
    }

    // �����, ����������� ��������� �� ������� �������. �� ��������� ��������, ��������������� ����� �����������
    // �������. ���� ������� ��������� � 1(������� �������), �� ��������� startLiveProcessForBornPet
    private void startListenerOnPetsAge() {
        ChangeListener<Number> petAgeListener = new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                settlePetsImage();
                if (t1.intValue() == 1) {
                    startLiveProcessForBornPet();
                }
            }
        };
        gameSpace.getPet().getAge().addListener(petAgeListener);
    }

    /*
    �����, ����������� ��������� �� ������� �������. �� ��������� �������� �������� installMoodIconBySatiety � �����
    ���������. ���� ����� �������� ������ MIN_SATIETY, �������� ����� die � �������, ������� ��������� �
    �������� goToGameOver � �������� ������ STARVATION
     */
    private void startListenerOnPetsSatiety() {
        ChangeListener<Number> petSatietyListener = new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                installMoodIconBySatiety(t1.intValue());
                if (t1.intValue() < Pet.MIN_SATIETY) {
                    gameSpace.getPet().die();
                    gameSpace.getPet().getSatiety().removeListener(this);
                    goToGameOver(DeathReason.STARVATION);
                }
            }
        };
        gameSpace.getPet().getSatiety().addListener(petSatietyListener);
    }


    /*
    ������������� moodIconProperty �� ����������� �������� satiety
     */
    private void installMoodIconBySatiety(int satiety) {
        if (satiety > Pet.MAX_SATIETY * 0.65) moodIconProperty.set(Mood.GOOD.getMoodImage());
        else if (satiety > Pet.MAX_SATIETY * 0.25) moodIconProperty.set(Mood.NORMAL.getMoodImage());
        else if (satiety >= Pet.MIN_SATIETY) moodIconProperty.set(Mood.BAD.getMoodImage());
        else moodIconProperty.set(Mood.DEAD.getMoodImage());
    }

    /*
    �����, ���������� �� ����� � ��������� �������� �����������. �������� ��������� ����� ����� �� (0;3] � �� �������
    �������� ������ ����� �����, ������������ Weather �� GameSpace. ����� ������������� ��� � backgroundImageProperty
     */
    private void installRandomWeatherImage() {
        int i = new Random().nextInt(3);
        if (i == 0) {
            getGameSpace().setWeather(Weather.STANDARD);
        }
        if (i == 1) {
            getGameSpace().setWeather(Weather.RAIN);
        }
        if (i == 2) {
            getGameSpace().setWeather(Weather.SNOW);
        }
        getBackgroundImageProperty().set(getGameSpace().getWeather().getWeatherImage());
    }

    public void closePetMovementHandlerThread() {
        if (petMovementScheduledExecutorService != null)
            petMovementScheduledExecutorService.shutdown();
    }

    public void closePetStateHandlerThread() {
        if (petStateScheduledExecutorService != null) {
            petStateScheduledExecutorService.shutdown();
        }
    }

    public void closeGameSpaceStateHandlerThread() {
        if (gameSpaceScheduledExecutorService != null) {
            gameSpaceScheduledExecutorService.shutdown();
        }
    }

    /*
    �����, ���������� �� ������� �� �������� gameOverPage. ��������� ������ ��������� � ��������, ��������
    performFuneralRitesByPet � ������� �������� � ���������� deathReason.
     */
    public void goToGameOver(DeathReason deathReason) {
        closePetStateHandlerThread();
        closePetMovementHandlerThread();
        GameManager.getInstance().getGameOverStateHandler().performFuneralRitesByPet(getGameSpace().getPet(), deathReason);
        ApplicationFXManager.getInstance().installSceneOnPrimaryStage(ApplicationFXManager.getInstance()
                .getGameOverPage().getScene());
    }

    /*
    �����, ����������� ����������� ������� �� ��������. ���� ������� ������� ����� 0, ���������� ��� ������ �
    ���������� 0 �� ��� X
     */
    private void settlePetsImage() {
        if (this.gameSpace.getPet().getAge().isEqualTo(0).get()) {
            setPetsModelAfterMotionPositionByXProperty(0);
        }
        getPetsModelImageProperty().set(gameSpace.getPet().getPetsImagesManager().getImageByAge());
    }

    // �����, ���������� �� ������������ ������� � ���. �������� setPetsModelAfterMotionPositionByXProperty
    // � �������� ��� x
    public void petGoFeeding(double x) {
        setPetsModelAfterMotionPositionByXProperty(x);
    }
}
