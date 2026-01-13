package other;

import java.util.ArrayList;

public class DisplayData {
    private ArrayList<Game> libraryGames;
    private String profileName;
    private String profilePicture;
    private int totalGameCount;

    public void setLibraryGames(ArrayList<Game> libraryGames) {
        this.libraryGames = libraryGames;
    }

    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public void setTotalGameCount(int totalGameCount) {
        this.totalGameCount = totalGameCount;
    }

    public ArrayList<Game> getLibraryGames() {
        return libraryGames;
    }

    public String getProfileName() {
        return profileName;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public int getTotalGameCount() {
        return totalGameCount;
    }
}
