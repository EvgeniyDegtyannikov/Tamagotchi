package sample.gameComponents;

import javafx.scene.image.Image;

public enum Weather {
    STANDARD(new Image("images/backgrounds/standard.jpg")),
    RAIN(new Image("images/backgrounds/rain.jpg")),
    SNOW(new Image("images/backgrounds/snow.jpg"));

    private Image weatherImage;

    private Weather(Image weatherImage) {
        this.weatherImage = weatherImage;
    }

    public Image getWeatherImage() {
        return this.weatherImage;
    }
}
