package sample.saving;

import sample.pets.Pet;

import java.io.*;

public class SerializationSaveManager implements SaveManager {
    private String pathToSaveFile;

    public SerializationSaveManager(String pathToSaveFile) {
        this.pathToSaveFile = pathToSaveFile;
    }

    @Override
    public void makeSave(Pet pet) {
        try (FileOutputStream fileOutputStream = new FileOutputStream(pathToSaveFile);
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream)) {
            objectOutputStream.writeObject(pet);
        } catch (FileNotFoundException ex) {
            System.out.println("Savefile not found");
            ex.printStackTrace();
        } catch (IOException ex) {
            System.out.println("Savefile is incorrect");
            ex.printStackTrace();
        }
    }

    @Override
    public Pet loadSave() {
        try (FileInputStream fileInputStream = new FileInputStream(pathToSaveFile);
             ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream)) {
            Pet pet = (Pet) objectInputStream.readObject();
            System.out.println("Data loaded: " + pet);
            return pet;
        } catch (FileNotFoundException ex) {
            System.out.println("Savefile not found");
            return null;
        } catch (IOException | ClassNotFoundException ex) {
            System.out.println("Savefile is incorrect or empty");
            return null;
        }
    }
}
