package sample.FXComponents.stateHandlers;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import sample.ApplicationFXManager;
import sample.GameManager;
import sample.pets.Pet;
import sample.pets.PetsFactory;
import sample.pets.PetsTypes;

// Класс, содержащий логику происходящего в игровом меню.
public class GameMenuStateHandler {
    private final StringProperty nameProperty = new SimpleStringProperty();

    private GameMenuPropertiesValidationChecker gameMenuPropertiesValidationChecker = new GameMenuPropertiesValidationChecker();

    public StringProperty getNameProperty() {
        return nameProperty;
    }

    // Класс, отвечающий за проверку свойств GameMenuStateHandler на корректность
    private class GameMenuPropertiesValidationChecker {
        // Метод, проверяющий, корректное ли значение имени введено пользователем.
        // Корректность значения подразумевает, что имя содержит только буквы латинского алфавита или цифры
        private boolean isNamePropertyValid() {
            return getNameProperty().get() != null && !getNameProperty().get().isEmpty()
                    && getNameProperty().get().matches("[a-zA-Z0-9]+");
        }
    }

    // Метод, завершающий выбор питомца. Создает нового питомца по переданному типу.
    // Если питомец успешно создан, устанавливает для него имя из nameProperty, связанного с nameTextField из
    // GameMenuController, предварительно вызывая метод проверки имени на корректность. В случае успешной
    // установки имени, переключает страницу на GameSpacePage и запускает ее метод startGameProcess,
    // начинающий работу по обслуживанию питомца на GameSpacePage
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

    // Метод, устанавливающий для питомца имя из nameProperty, и очищающий последнее
    private void settleNamePropertyAsPetsNameToPet(Pet pet) {
        pet.setName(getNameProperty().get());
        getNameProperty().set("");
    }

    // Метод, осуществляющий переход на страницу gameSpacePage.
    public void goToGameSpace() {
        ApplicationFXManager.getInstance().installSceneOnPrimaryStage(ApplicationFXManager.getInstance()
                .getGameSpacePage().getScene());
    }
}
