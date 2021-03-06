package sample.services.controllersStateHandlers;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import sample.entities.Pet;
import sample.entities.PetsFactory;
import sample.entities.PetsTypes;
import sample.services.ApplicationFXManager;
import sample.services.GameManager;

// �����, ���������� ������ ������������� � ������� ����.
public class GameMenuStateHandler {
    private final StringProperty nameProperty = new SimpleStringProperty();

    private GameMenuPropertiesValidationChecker gameMenuPropertiesValidationChecker = new GameMenuPropertiesValidationChecker();

    public StringProperty getNameProperty() {
        return nameProperty;
    }

    // �����, ���������� �� �������� ������� GameMenuStateHandler �� ������������
    private class GameMenuPropertiesValidationChecker {
        // �����, �����������, ���������� �� �������� ����� ������� �������������.
        // ������������ �������� �������������, ��� ��� �������� ������ ����� ���������� �������� ��� �����
        private boolean isNamePropertyValid() {
            return getNameProperty().get() != null && !getNameProperty().get().isEmpty()
                    && getNameProperty().get().matches("[a-zA-Z0-9]+");
        }
    }

    // �����, ����������� ����� �������. ������� ������ ������� �� ����������� ����.
    // ���� ������� ������� ������, ������������� ��� ���� ��� �� nameProperty, ���������� � nameTextField ��
    // GameMenuController, �������������� ������� ����� �������� ����� �� ������������. � ������ ��������
    // ��������� �����, ����������� �������� �� GameSpacePage � ��������� �� ����� startGameProcess,
    // ���������� ������ �� ������������ ������� �� GameSpacePage
    public void completePetsCreation(PetsTypes petsTypes) {
        Pet pet = PetsFactory.getPetsRealizationInstance(petsTypes);
        if (pet != null) {
            if (gameMenuPropertiesValidationChecker.isNamePropertyValid()) {
                settleNamePropertyAsPetsNameToPet(pet);
                goToGameSpace();
                GameManager.getInstance().getGameSpaceStateHandler().startGameProcess(pet);
            }
        }
    }

    // �����, ��������������� ��� ������� ��� �� nameProperty, � ��������� ���������
    private void settleNamePropertyAsPetsNameToPet(Pet pet) {
        pet.setName(getNameProperty().get());
        getNameProperty().set("");
    }

    // �����, �������������� ������� �� �������� gameSpacePage.
    public void goToGameSpace() {
        ApplicationFXManager.getInstance().installSceneOnPrimaryStage(ApplicationFXManager.getInstance()
                .getGameSpacePage().getScene());
    }
}
