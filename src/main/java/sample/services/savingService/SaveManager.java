package sample.services.savingService;

import sample.entities.Pet;

// ���������, ������������ ����� ������ ������ � ������������
public interface SaveManager {
    void makeSave(Pet pet);

    Pet loadSave();
}
