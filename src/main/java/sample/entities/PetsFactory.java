package sample.entities;

import sample.entities.realizations.Bear;
import sample.entities.realizations.Cat;
import sample.entities.realizations.Dog;
import sample.entities.realizations.Pig;

public class PetsFactory {

    private PetsFactory() {
    }

    public static Pet getPetsRealizationInstance(PetsTypes type) {
        switch (type) {
            case CAT:
                return new Cat();
            case DOG:
                return new Dog();
            case PIG:
                return new Pig();
            case BEAR:
                return new Bear();
            default:
                return null;
        }
    }
}
