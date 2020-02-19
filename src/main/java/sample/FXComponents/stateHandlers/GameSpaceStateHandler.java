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

    // Метод, отвечающий за смену погоды. Запускает scheduleAtFixedRate, вызывающий каждые 4 секунды
    // installRandomWeatherImage
    private void startWeatherChangeScheduledThread() {
        if (gameSpaceScheduledExecutorService == null)
            gameSpaceScheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        gameSpaceScheduledExecutorService.scheduleAtFixedRate(this::installRandomWeatherImage, 0, 4, TimeUnit.SECONDS);
    }

    // Метод, отвечающий за рождение питомца. Ожидает время, оставшееся до рождения и устанавливает возраст в значение 1
    private void startPetsBornTimeline() {
        System.out.println("Time to be born: " + this.gameSpace.getPet().getTimeToBeBornInSeconds());
        Timeline timeline = new Timeline(new KeyFrame(Duration
                .seconds(this.gameSpace.getPet().getTimeToBeBornInSeconds()), e -> this.gameSpace.getPet().setAge(1)));
        Platform.runLater(timeline::play);
    }

    // Метод, начинающий игровой процесс непосредственно. Устанавливает GameSpace и питомца, запускает метод,
    // отвечающий за смену погоды startWeatherChangeScheduledThread, устанавливает значение moodIconProperty в null.
    // Если питомец isAlive, то размещает его изображение settlePetsImage и запускает слышатель на изменение его
    // возраста. Если возраст питомца ==0, то запускает метод startPetsBornTimeline, иначе запускает метод
    // startLiveProcessForBornPet. Если питомец не isAlive, вызывает goToGameOver с причиной смерти ALREADY_DEAD
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

    // Метод, начинающий жизненный процесс питомца. Запускает scheduleAtFixedRate, исполняющий метод run из Pet
    // после getNeedChangeTimeInSeconds питомца и далее с таким же промежутком. Запускает слушатель на сытость
    // питомца и устанавливает installMoodIconBySatiety по сытости питомца.
    private void startLiveProcessForBornPet() {
        petStateScheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        petStateScheduledExecutorService.scheduleAtFixedRate(gameSpace.getPet(),
                gameSpace.getPet().getNeedChangeTimeInSeconds(),
                gameSpace.getPet().getNeedChangeTimeInSeconds(),
                TimeUnit.SECONDS);
        startListenerOnPetsSatiety();
        installMoodIconBySatiety(gameSpace.getPet().getSatiety().get());
    }

    // Метод, запускающий слушатель на возраст питомца. По изменении значения, устанавливается новое изображение
    // питомца. Если возраст изменился в 1(питомец родился), то запускает startLiveProcessForBornPet
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
    Метод, запускающий слушателя на сытость питомца. По изменению значения вызывает installMoodIconBySatiety с новым
    значением. Если новое значение меньше MIN_SATIETY, вызывает метод die у питомца, убирает слушателя и
    вызывает goToGameOver с причиной смерти STARVATION
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
    Устанавливает moodIconProperty по переданному значению satiety
     */
    private void installMoodIconBySatiety(int satiety) {
        if (satiety > Pet.MAX_SATIETY * 0.65) moodIconProperty.set(Mood.GOOD.getMoodImage());
        else if (satiety > Pet.MAX_SATIETY * 0.25) moodIconProperty.set(Mood.NORMAL.getMoodImage());
        else if (satiety >= Pet.MIN_SATIETY) moodIconProperty.set(Mood.BAD.getMoodImage());
        else moodIconProperty.set(Mood.DEAD.getMoodImage());
    }

    /*
    Метод, отвечающий за выбор и установку фонового изображения. Выбирает случайное целое число из (0;3] и по каждому
    значению делает некий выбор, устанавливая Weather из GameSpace. Затем устанавливает его в backgroundImageProperty
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
    Метод, отвечающий за переход на страницу gameOverPage. Закрывает потоки состояния и движения, вызывает
    performFuneralRitesByPet с текущим питомцем и полученным deathReason.
     */
    public void goToGameOver(DeathReason deathReason) {
        closePetStateHandlerThread();
        closePetMovementHandlerThread();
        GameManager.getInstance().getGameOverStateHandler().performFuneralRitesByPet(getGameSpace().getPet(), deathReason);
        ApplicationFXManager.getInstance().installSceneOnPrimaryStage(ApplicationFXManager.getInstance()
                .getGameOverPage().getScene());
    }

    /*
    Метод, размещающий изображение питомца на странице. Если возраст питомца равен 0, перемещает его модель в
    координату 0 по оси X
     */
    private void settlePetsImage() {
        if (this.gameSpace.getPet().getAge().isEqualTo(0).get()) {
            setPetsModelAfterMotionPositionByXProperty(0);
        }
        getPetsModelImageProperty().set(gameSpace.getPet().getPetsImagesManager().getImageByAge());
    }

    // Метод, отвечающий за передвижение питомца к еде. Вызывает setPetsModelAfterMotionPositionByXProperty
    // с позицией еды x
    public void petGoFeeding(double x) {
        setPetsModelAfterMotionPositionByXProperty(x);
    }
}
