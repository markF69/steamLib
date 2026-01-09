package controllers;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.TreeMap;

public class ApiLogic {
    public void returnData(long steam64ID){
        ObjectMapper mapper = new ObjectMapper();
        boolean hasMore = true, noNewGames = false;
        long lastAppId = 0;
        TreeMap<Integer, String> map = new TreeMap<>(); // used for storing appid : name

        String steamProfileUrl = "http://api.steampowered.com/ISteamUser/GetPlayerSummaries/v0002/?key=7C32F141B780243EECD22CBBDBB61F47&steamids=76561198093875269";
    }
}
