package sample.entities.realizations;

import javafx.scene.image.Image;
import sample.entities.Pet;
import sample.gameComponents.Food;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class Bear extends Pet {

    {
        setSatietyChangeValue(5);
        setTimeToBeBornInSeconds(7);
        setNeedChangeTimeInSeconds(2);
        setFood(Food.MEAD);
        getPetsImagesManager().setRipImage(new Image("/images/models/bear/bear_rip.jpg"));
        getPetsImagesManager().setAge0Image(new Image("/images/models/bear/base/bear_age_0.jpg"));
        getPetsImagesManager().setAge1Image(new Image("/images/models/bear/base/bear_age_1.jpg"));
        getPetsImagesManager().setAge2Image(new Image("/images/models/bear/base/bear_age_2.jpg"));
        getPetsImagesManager().setAge3Image(new Image("/images/models/bear/base/bear_age_3.jpg"));
    }

    public Bear() {
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
