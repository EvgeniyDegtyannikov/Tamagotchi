package sample.services.savingService;

import sample.entities.Pet;

// »нтерфейс, определ€ющий общую логику работы с сохранени€ми
public interface SaveManager {
    void makeSave(Pet pet);

    Pet loadSave();
}
