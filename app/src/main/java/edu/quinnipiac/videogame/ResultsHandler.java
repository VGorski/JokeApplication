package edu.quinnipiac.videogame;

import org.json.JSONException;
import org.json.JSONObject;

public class ResultsHandler {
    public static final String GAME_RESULTS = "Game_Results";
    //We can change this later.....
    final public static String[] results = new String[1-5];

    public ResultsHandler(){
        //populate the array for the results
    }
    public String getGameResults(String gameResultsJSonString) throws JSONException {
        JSONObject gameResultsJSONObject = new JSONObject(gameResultsJSonString);
        return gameResultsJSONObject.getString("text");
    }

}
