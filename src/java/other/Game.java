package other;

public class Game {
    private String name;
    private float playtimeHours;
    private String lastPlayedDate;

    public Game(String name, float playtimeHours, String lastPlayedDate){
        this.name = name;
        this.playtimeHours = playtimeHours;
        this.lastPlayedDate = lastPlayedDate;
    }

    public String getName() {
        return name;
    }

    public float getPlaytimeHours() {
        return playtimeHours;
    }

    public String getLastPlayedDate() {
        return lastPlayedDate;
    }
}
