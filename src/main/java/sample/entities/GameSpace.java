package sample.entities;

import sample.gameComponents.Weather;

public class GameSpace {
    private Pet pet;
    private Weather weather;
    private boolean isFeedInGameSpace;
    private boolean isPetMoving;

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

    public boolean isPetMoving() {
        return isPetMoving;
    }

    public void setPetMoving(boolean petMoving) {
        isPetMoving = petMoving;
    }
}
