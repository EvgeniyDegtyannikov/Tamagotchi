package sample;

import sample.FXComponents.stateHandlers.GameMenuStateHandler;
import sample.FXComponents.stateHandlers.GameOverStateHandler;
import sample.FXComponents.stateHandlers.GameSpaceStateHandler;
import sample.pets.Pet;
import sample.saving.SaveManager;

// Класс, управляющий игровой логикой приложения
public class GameManager {
    private static final GameManager gameManager = new GameManager();
    private SaveManager saveManager;
    private GameSpaceStateHandler gameSpaceStateHandler;
    private GameOverStateHandler gameOverStateHandler;
    private GameMenuStateHandler gameMenuStateHandler;

    private GameManager() {
        gameSpaceStateHandler = new GameSpaceStateHandler();
        gameOverStateHandler = new GameOverStateHandler();
        gameMenuStateHandler = new GameMenuStateHandler();
    }

    public static GameManager getInstance() {
        return gameManager;
    }

    public GameSpaceStateHandler getGameSpaceStateHandler() {
        return gameSpaceStateHandler;
    }

    public GameOverStateHandler getGameOverStateHandler() {
        return gameOverStateHandler;
    }

    public GameMenuStateHandler getGameMenuStateHandler() {
        return gameMenuStateHandler;
    }

    public void setSaveManager(SaveManager saveManager) {
        this.saveManager = saveManager;
    }

    // Метод выполняется при запуске приложения, устанавливает игровое пространство(класс), пытается загрузить сохранение,
    // при успешной загрузке переключает сцену на игровое пространство(сцена), иначе на игровое меню(сцена)
    public void onGameStart() {
        Pet loadedPetFromSaveFile = saveManager.loadSave();
        if (loadedPetFromSaveFile != null) {
            ApplicationFXManager.getInstance().installSceneOnPrimaryStage(ApplicationFXManager.getInstance()
                    .getGameSpacePage().getScene());
            gameSpaceStateHandler.startGameProcess(loadedPetFromSaveFile);
        } else {
            ApplicationFXManager.getInstance().installSceneOnPrimaryStage(ApplicationFXManager.getInstance()
                    .getGameMenuPage().getScene());
        }
    }

    // Метод выполняется при закрытии приложения, производит сохранение питомца при наличии такового и закрывает потоки,
    // изменяющие его состояние, потоки отвечающие за изменение состояния игрового поля и игровых моделей
    public void onGameStop() {
        if (gameSpaceStateHandler.getGameSpace()!=null && gameSpaceStateHandler.getGameSpace().getPet()!=null)
            saveManager.makeSave(gameSpaceStateHandler.getGameSpace().getPet());
        gameSpaceStateHandler.closePetStateHandlerThread();
        gameSpaceStateHandler.closeGameSpaceStateHandlerThread();
        gameSpaceStateHandler.closePetMovementHandlerThread();
    }
}
