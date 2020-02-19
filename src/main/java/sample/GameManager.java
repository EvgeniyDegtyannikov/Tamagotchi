package sample;

import sample.FXComponents.stateHandlers.GameMenuStateHandler;
import sample.FXComponents.stateHandlers.GameOverStateHandler;
import sample.FXComponents.stateHandlers.GameSpaceStateHandler;
import sample.pets.Pet;
import sample.saving.SaveManager;

// �����, ����������� ������� ������� ����������
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

    // ����� ����������� ��� ������� ����������, ������������� ������� ������������(�����), �������� ��������� ����������,
    // ��� �������� �������� ����������� ����� �� ������� ������������(�����), ����� �� ������� ����(�����)
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

    // ����� ����������� ��� �������� ����������, ���������� ���������� ������� ��� ������� �������� � ��������� ������,
    // ���������� ��� ���������, ������ ���������� �� ��������� ��������� �������� ���� � ������� �������
    public void onGameStop() {
        if (gameSpaceStateHandler.getGameSpace()!=null && gameSpaceStateHandler.getGameSpace().getPet()!=null)
            saveManager.makeSave(gameSpaceStateHandler.getGameSpace().getPet());
        gameSpaceStateHandler.closePetStateHandlerThread();
        gameSpaceStateHandler.closeGameSpaceStateHandlerThread();
        gameSpaceStateHandler.closePetMovementHandlerThread();
    }
}
