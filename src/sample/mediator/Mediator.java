package sample.mediator;

import sample.FXComponents.stateViewers.GameOverStateHandler;
import sample.FXComponents.stateViewers.GameSpaceStateHandler;
import sample.FXComponents.stateViewers.MenuStateHandler;
import sample.pets.Pet;

public interface Mediator {
    void startGameProcessWithSpecifiedPet(Pet pet);
    void sendPetOnEpitaph(Pet pet, String deathReason);
    Pet getCurrentGamePet();
    void closeHandlersThreads();
    void setGameOverStateHandler(GameOverStateHandler gameOverStateHandler);
    void setMenuStateHandler(MenuStateHandler menuStateHandler);
    void setGameSpaceStateHandler(GameSpaceStateHandler gameSpaceStateHandler);
}
