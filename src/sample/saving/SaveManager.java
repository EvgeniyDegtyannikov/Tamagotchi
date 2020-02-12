package sample.saving;

import sample.pets.Pet;

public interface SaveManager {
    void makeSave(Pet pet);
    Pet loadSave();
}
