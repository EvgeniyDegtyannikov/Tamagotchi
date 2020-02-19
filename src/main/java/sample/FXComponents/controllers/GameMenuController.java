package sample.FXComponents.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import sample.FXComponents.stateHandlers.GameMenuStateHandler;
import sample.GameManager;
import sample.pets.PetsTypes;

import java.net.URL;
import java.util.ResourceBundle;

public class GameMenuController implements Initializable {
    @FXML
    private Button dogCharacterButton;
    @FXML
    private Button catCharacterButton;
    @FXML
    private Button pigCharacterButton;
    @FXML
    private Button bearCharacterButton;
    @FXML
    private TextField nameTextField;

    private GameMenuStateHandler gameMenuStateHandler = GameManager.getInstance().getGameMenuStateHandler();

    // Метод для установки связей с классом, содержащим логику происходящего в игровом меню.
    // Устанавливает двустороннюю связь между текстовым полем nameTextField и свойством "nameProperty" из
    // GameMenuStateHandler класса.
    private void startMessagingWithGameMenuStateHandler() {
        nameTextField.textProperty().bindBidirectional(this.gameMenuStateHandler.getNameProperty());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Tooltip.install(dogCharacterButton, new Tooltip("choose dog"));
        Tooltip.install(catCharacterButton, new Tooltip("choose cat"));
        Tooltip.install(pigCharacterButton, new Tooltip("choose pig"));
        Tooltip.install(bearCharacterButton, new Tooltip("choose bear"));
        startMessagingWithGameMenuStateHandler();
    }

    public void chooseDogCharacterAsPet(ActionEvent event) {
        gameMenuStateHandler.completePetsCreation(PetsTypes.DOG);
    }

    public void chooseCatCharacterAsPet(ActionEvent event) {
        gameMenuStateHandler.completePetsCreation(PetsTypes.CAT);
    }

    public void choosePigCharacterAsPet(ActionEvent event) {
        gameMenuStateHandler.completePetsCreation(PetsTypes.PIG);
    }

    public void chooseBearCharacterAsPet(ActionEvent event) {
        gameMenuStateHandler.completePetsCreation(PetsTypes.BEAR);
    }
}
