package sample.pets.realizations;

import javafx.scene.image.Image;
import sample.gameComponents.Food;
import sample.pets.Pet;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Random;

public class Pig extends Pet {

    {
        setSatietyChangePercent(-20);
        setTimeToBeBornInSeconds(1);
        setNeedChangeTimeInSeconds(1);
        setFood(Food.APPLE);
        setRipImage(new Image("resources/images/models/pig/pig_rip.jpg"));
        setAge0Image(new Image("images/models/pig/base/pig_age_0.jpg"));
        setAge1Image(new Image("images/models/pig/base/pig_age_1.jpg"));
        setAge2Image(new Image("images/models/pig/base/pig_age_2.jpg"));
        setAge3Image(new Image("images/models/pig/base/pig_age_3.jpg"));
    }

    public Pig(){}

    @Override
    protected void decreaseNeeds() {
        this.changeSatiety(this.getSatietyChangePercent());
        if (new Random().nextInt(100) < 15)
            this.setAge(this.getAge().get()+1);
        System.out.println(this.toString());
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
