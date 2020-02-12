package sample.FXComponents;

import sample.gameComponents.Weather;
import sample.pets.Pet;

public class GameSpace {
    private Pet pet;
    private Weather weather;
    private boolean isFeedInGameSpace;

    public Pet getPet() {
        return pet;
    }

    public void setPet(Pet pet) {
        this.pet = pet;
    }

    public Weather getWeather() {
        return weather;
    }

    public void setWeather(Weather weather) {
        this.weather = weather;
    }

    public boolean isFeedInGameSpace() {
        return isFeedInGameSpace;
    }

    public void setFeedInGameSpace(boolean feedInGameSpace) {
        isFeedInGameSpace = feedInGameSpace;
    }
}
