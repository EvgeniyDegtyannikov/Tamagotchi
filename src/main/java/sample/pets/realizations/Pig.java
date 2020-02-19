package sample.pets.realizations;

import javafx.scene.image.Image;
import sample.gameComponents.Food;
import sample.pets.Pet;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class Pig extends Pet {

    {
        setSatietyChangeValue(20);
        setTimeToBeBornInSeconds(5);
        setNeedChangeTimeInSeconds(1);
        setFood(Food.APPLE);
        getPetsImagesManager().setRipImage(new Image("images/models/pig/pig_rip.jpg"));
        getPetsImagesManager().setAge0Image(new Image("images/models/pig/base/pig_age_0.jpg"));
        getPetsImagesManager().setAge1Image(new Image("images/models/pig/base/pig_age_1.jpg"));
        getPetsImagesManager().setAge2Image(new Image("images/models/pig/base/pig_age_2.jpg"));
        getPetsImagesManager().setAge3Image(new Image("images/models/pig/base/pig_age_3.jpg"));
    }

    public Pig() {
    }

    @Override
    public void writeExternal(ObjectOutput objectOutput) throws IOException {
        super.writeExternal(objectOutput);
    }

    @Override
    public void readExternal(ObjectInput objectInput) throws IOException, ClassNotFoundException {
        super.readExternal(objectInput);
    }
}
