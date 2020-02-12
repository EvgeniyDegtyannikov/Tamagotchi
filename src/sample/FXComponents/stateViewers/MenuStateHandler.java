package sample.FXComponents.stateViewers;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sample.mediator.Mediator;
import sample.pets.Pet;

public class MenuStateHandler {
    private final StringProperty nameProperty = new SimpleStringProperty();
    private Scene linkOnGameSpaceScene;
    private Scene currentScene;
    private Mediator mediator;

    public void setCurrentScene(Scene currentScene) {
        this.currentScene = currentScene;
    }

    public StringProperty getNameProperty() {
        return nameProperty;
    }

    public void setNameProperty(String nameProperty) {
        this.nameProperty.set(nameProperty);
    }

    public void setLinkOnGameSpaceScene(Scene linkOnGameSpaceScene) {
        this.linkOnGameSpaceScene = linkOnGameSpaceScene;
    }

    public void setMediator(Mediator mediator) {
        this.mediator = mediator;
    }

    public void completePetsCreation(Pet pet) {
        if (getNameProperty().get()!=null) {
            if (!getNameProperty().get().isEmpty()) {
                pet.setName(getNameProperty().get());
                getNameProperty().set("");
                goToGameSpace();
                mediator.startGameProcessWithSpecifiedPet(pet);
            }
        }
    }

    public void goToGameSpace() {
        Stage stage = (Stage) currentScene.getWindow();
        stage.setScene(linkOnGameSpaceScene);
    }
}
