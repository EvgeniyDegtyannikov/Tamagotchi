package sample.saving;

import sample.pets.Pet;

// »нтерфейс, определ€ющий общую логику работы с сохранени€ми
public interface SaveManager {
    void makeSave(Pet pet);

    Pet loadSave();
}
