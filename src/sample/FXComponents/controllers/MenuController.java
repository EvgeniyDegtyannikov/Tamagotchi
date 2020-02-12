package sample.FXComponents.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import sample.FXComponents.stateViewers.MenuStateHandler;
import sample.pets.PetsFactory;
import sample.pets.PetsType;

import java.net.URL;
import java.util.ResourceBundle;

public class MenuController implements Initializable {
    @FXML
    private Parent root;
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

    private MenuStateHandler menuStateHandler;

    public void setMenuStateHandler(MenuStateHandler menuStateHandler) {
        this.menuStateHandler = menuStateHandler;
        this.menuStateHandler.setCurrentScene(root.getScene());
        nameTextField.textProperty().bindBidirectional(this.menuStateHandler.getNameProperty());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Tooltip.install(dogCharacterButton, new Tooltip("choose dog"));
        Tooltip.install(catCharacterButton, new Tooltip("choose cat"));
        Tooltip.install(pigCharacterButton, new Tooltip("choose pig"));
        Tooltip.install(bearCharacterButton, new Tooltip("choose bear"));
    }

    public void chooseDogCharacterAsPet(ActionEvent event) {
        menuStateHandler.completePetsCreation(PetsFactory.getPetsRealizationInstance(PetsType.DOG));
    }

    public void chooseCatCharacterAsPet(ActionEvent event) {
        menuStateHandler.completePetsCreation(PetsFactory.getPetsRealizationInstance(PetsType.CAT));
    }

    public void choosePigCharacterAsPet(ActionEvent event) {
        menuStateHandler.completePetsCreation(PetsFactory.getPetsRealizationInstance(PetsType.PIG));
    }

    public void chooseBearCharacterAsPet(ActionEvent event) {
        menuStateHandler.completePetsCreation(PetsFactory.getPetsRealizationInstance(PetsType.BEAR));
    }
}
