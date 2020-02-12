package sample.mediator;

import sample.FXComponents.stateViewers.GameOverStateHandler;
import sample.FXComponents.stateViewers.GameSpaceStateHandler;
import sample.FXComponents.stateViewers.MenuStateHandler;
import sample.pets.Pet;

public class MediatorImpl implements Mediator {
    private GameOverStateHandler gameOverStateHandler;
    private MenuStateHandler menuStateHandler;
    private GameSpaceStateHandler gameSpaceStateHandler;

    @Override
    public void setGameSpaceStateHandler(GameSpaceStateHandler gameSpaceStateHandler) {
        this.gameSpaceStateHandler = gameSpaceStateHandler;
    }

    public void setGameOverStateHandler(GameOverStateHandler gameOverStateHandler) {
        this.gameOverStateHandler = gameOverStateHandler;
    }

    @Override
    public void setMenuStateHandler(MenuStateHandler menuStateHandler) {
        this.menuStateHandler=menuStateHandler;
    }

    @Override
    public void startGameProcessWithSpecifiedPet(Pet pet) {
        gameSpaceStateHandler.startGameProcess(pet);
    }

    @Override
    public Pet getCurrentGamePet() {
        return gameSpaceStateHandler.getGameSpace().getPet();
    }

    @Override
    public void closeHandlersThreads() {
        gameSpaceStateHandler.closePetStateHandlerThread();
        gameSpaceStateHandler.closeGameSpaceStateHandlerThread();
    }

    @Override
    public void sendPetOnEpitaph(Pet pet, String deathReason) {
        gameOverStateHandler.installEpitaph(pet,deathReason);
    }
}
