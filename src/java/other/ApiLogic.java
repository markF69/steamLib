package other;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.TreeMap;

public class ApiLogic {
    // Returns the date
    public static String changeIntoDate(long lastPlayedTime){
        Instant lastPlayed = Instant.ofEpochSecond(lastPlayedTime); // starts from 1970-1-1
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyy").withZone(ZoneId.systemDefault()); // default sys time zone
        return formatter.format(lastPlayed);
    }
    // Checks to see if the given data is correct
    public static int authCheck(String steam64ID, String steamWebApiKey) throws Exception {
        HttpClient testClient = HttpClient.newHttpClient();
        HttpRequest testRequest = HttpRequest.newBuilder().uri(URI.create("http://api.steampowered.com/IPlayerService/GetOwnedGames/v0001/?key="+steamWebApiKey+"&steamid="+steam64ID+"&format=json")).GET().build(); // user library request
        HttpResponse<String> testResponse = testClient.send(testRequest, HttpResponse.BodyHandlers.ofString());

        // Auth failed - wrong key
        if (testResponse.statusCode() == 403){
            return 403;
        }

        ObjectMapper mapper = new ObjectMapper();
        JsonNode testNode = mapper.readTree(testResponse.body());

        // Auth failed - wrong steam 64 id
        if (testNode.get("response").isEmpty()){
            return 64; // made up value to represent the steam id being incorrect
        }

        return testResponse.statusCode();
    }
    public static void returnData(String steam64ID, String steamWebApiKey) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        boolean hasMore = true, noNewGames = false;
        long lastAppId = 0;
        TreeMap<Integer, String> map = new TreeMap<>(); // used for storing appid : name

        HttpClient client = HttpClient.newHttpClient();

        // JSON file where the steam store data will be stored
        File storeJsonFile = new File("src/main/storage/steamStore.json");

        // Checks if the folders are already created
        if (storeJsonFile.getParentFile().mkdirs()){
            System.out.println("Folders created");
        }
        else {
            System.out.println("Folder already exists");
        }

        // Checks if the file is already created
        try{
            if (storeJsonFile.createNewFile()){
                System.out.println("Store JSON file created");
            } else {
                System.out.println("Store JSON file already exists");
                // Reads the JSON file and stores it in the TreeMap
                map = mapper.readValue(storeJsonFile, new TypeReference<TreeMap<Integer, String>>() {});
                // Takes the last appID from the map which will be used to check if there are any new games
                lastAppId = map.lastKey();
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }

        // Gathering steam store from the API
        do {
            String steamStoreUrl = "http://api.steampowered.com/IStoreService/GetAppList/v1/?key="+steamWebApiKey+"&last_appid="+lastAppId+"&max_results=50000";

            HttpRequest steamStoreRequest = HttpRequest.newBuilder().uri(URI.create(steamStoreUrl)).GET().build();
            HttpResponse<String> steamStoreResponse = client.send(steamStoreRequest, HttpResponse.BodyHandlers.ofString());
            JsonNode steamStoreNode = mapper.readTree(steamStoreResponse.body());

            // Checks if the response isn't empty which would mean it reached the end
            if (!(steamStoreNode.get("response").isEmpty())){
                JsonNode apps = steamStoreNode.get("response").get("apps");

                // Goes through every steam store game and maps them as appID : name
                for (JsonNode app : apps){
                    int appID = app.get("appid").asInt();
                    String name = app.get("name").asText();
                    map.put(appID, name);
                }

                // Checks if the API call is not at the last page (last API call doesn't contain last_appid / hasMore)
                if (steamStoreNode.get("response").get("last_appid") != null){
                    lastAppId = steamStoreNode.get("response").get("last_appid").asLong();
                    hasMore = steamStoreNode.get("response").get("have_more_results").asBoolean();
                }
                else {
                    hasMore = false;
                }
            }

            // In case that the JSON file is up to date and there are no new games
            else {
                hasMore = false;
                noNewGames = true;
                System.out.println("No new games");
            }
        } while (hasMore);

        // If there are new games that have not been put into the json file
        if (noNewGames == false){
            // Places the TreeMap content into the json file
            mapper.writerWithDefaultPrettyPrinter().writeValue(storeJsonFile, mapper);
            System.out.println("TreeMap content inserted into json file");

            // Writes the lastAppId into a new file
            try (FileWriter writer = new FileWriter("src/main/storage/lastId.txt")){
                writer.write(String.valueOf(map.lastKey()));
                System.out.println("lastAppId added to file");
            }
        }

        // Creates an arraylist to store users game in the format of name : playtime (hours) : last played date
        ArrayList<Game> libraryGames = new ArrayList<>();

        // For profile name and picture
        String steamProfileUrl = "http://api.steampowered.com/ISteamUser/GetPlayerSummaries/v0002/?key="+steamWebApiKey+"&steamids="+steam64ID;
        // For profile library
        String steamProfileLibraryUrl = "http://api.steampowered.com/IPlayerService/GetOwnedGames/v0001/?key="+steamWebApiKey+"&steamid="+steam64ID+"&format=json";

        HttpRequest steamProfileRequest = HttpRequest.newBuilder().uri(URI.create(steamProfileUrl)).GET().build();
        HttpRequest steamProfileLibraryRequest = HttpRequest.newBuilder().uri(URI.create(steamProfileLibraryUrl)).GET().build();

        HttpResponse<String> steamProfileResponse = client.send(steamProfileRequest, HttpResponse.BodyHandlers.ofString());
        HttpResponse<String> steamProfileLibraryResponse = client.send(steamProfileLibraryRequest, HttpResponse.BodyHandlers.ofString());

        // Gathers the name and picture
        JsonNode steamProfileNode = mapper.readTree(steamProfileResponse.body());
        String profileName = steamProfileNode.get("response").get("players").get("personaname").asText();
        String profilePicture = steamProfileNode.get("response").get("players").get("avatarfull").asText();

        // Gathers the users library
        JsonNode steamLibraryNode = mapper.readTree(steamProfileLibraryResponse.body());
        JsonNode gameNode = steamProfileNode.get("response").get("games");

        // Adds the users game library into the ArrayList
        for (JsonNode game : gameNode){
            String gameName = map.get(game.get("appid").asInt()); // name
            if (gameName == null){
                gameName = "[UNKNOWN / REMOVED]";
            }
            float hoursPlayed = (float) game.get("playtime_forever").asInt() / 60; // playtime in hours
            String lastPlayedDate = changeIntoDate(game.get("rtime_last_played").asLong()); // last played date
            libraryGames.add(new Game(gameName, hoursPlayed, lastPlayedDate));
        }
    }
}
