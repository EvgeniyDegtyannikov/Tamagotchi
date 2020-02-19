package sample.gameComponents;

public enum DeathReason {
    STARVATION("Death from starvation"),
    ALREADY_DEAD("Pet was already dead");
    private String deathMessage;

    DeathReason(String deathMessage) {
        this.deathMessage = deathMessage;
    }

    public String getDeathMessage() {
        return deathMessage;
    }
}
