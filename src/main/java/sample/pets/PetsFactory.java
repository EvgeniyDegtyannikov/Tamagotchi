package sample.pets;

import sample.pets.realizations.Bear;
import sample.pets.realizations.Cat;
import sample.pets.realizations.Dog;
import sample.pets.realizations.Pig;

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
