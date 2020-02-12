package sample.gameComponents;

import javafx.scene.image.Image;

public enum Food {
    BONE(new Image("resources/images/icons/food/bone_food.jpg")),
    APPLE(new Image("resources/images/icons/food/apple_food.jpg")),
    FISH(new Image("resources/images/icons/food/fish_food.jpg")),
    MEAD(new Image("resources/images/icons/food/mead_food.jpg"));

    private Image foodImage;
    private Food(Image foodImage){
        this.foodImage=foodImage;
    }

    public Image getFoodImage(){
        return this.foodImage;
    }
}
