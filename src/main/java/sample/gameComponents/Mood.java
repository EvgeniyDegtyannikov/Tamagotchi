package sample.gameComponents;

import javafx.scene.image.Image;

public enum Mood {
    GOOD(new Image("images/icons/mood/mood_good.jpg")),
    NORMAL(new Image("images/icons/mood/mood_normal.jpg")),
    BAD(new Image("images/icons/mood/mood_bad.jpg")),
    DEAD(new Image("images/icons/mood/mood_dead.jpg"));

    private Image moodImage;

    Mood(Image image) {
        this.moodImage = image;
    }

    public Image getMoodImage() {
        return moodImage;
    }
}
