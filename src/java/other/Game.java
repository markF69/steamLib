package other;

public class Game {
    private String name;
    private float playtimeHours;
    private String lastPlayedDate;

    public Game(String name, float playtimeHours, String lastPlayedDate){
        this.name = name;
        this.playtimeHours = playtimeHours;
        if (lastPlayedDate.equals("01/01/1970")){
            this.lastPlayedDate = "";
        } else {
            this.lastPlayedDate = lastPlayedDate;
        }

    }

    public String getName() {
        return name;
    }

    public String getPlaytimeHours() {
        return String.format("%.1f", playtimeHours);
    }

    public String getLastPlayedDate() {
        return lastPlayedDate;
    }
}
