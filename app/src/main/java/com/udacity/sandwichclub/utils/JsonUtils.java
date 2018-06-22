package com.udacity.sandwichclub.utils;

import android.util.Log;

import com.udacity.sandwichclub.model.Sandwich;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonUtils {

    private static final String JSON_KEY_NAME = "name";
    private static final String JSON_KEY_MAIN_NAME = "mainName";
    private static final String JSON_KEY_ALSO_KNOWN_AS = "alsoKnownAs";
    private static final String JSON_KEY_PLACE_OF_ORIGIN = "placeOfOrigin";
    private static final String JSON_KEY_DESCRIPTION = "description";
    private static final String JSON_KEY_IMAGE_URL = "image";
    private static final String JSON_KEY_INGREDIENTS = "ingredients";

    private static final String JSON_DEFAULT_DATA_TEXT = "Not Available";

    public static Sandwich parseSandwichJson(String json) {

        JSONObject sandwichJSON;

        try {

            sandwichJSON = new JSONObject(json);

            if (sandwichJSON.has(JSON_KEY_NAME)) {

                JSONObject sandwichName = sandwichJSON.getJSONObject(JSON_KEY_NAME);
                String mainName = sandwichName.optString(JSON_KEY_MAIN_NAME, JSON_DEFAULT_DATA_TEXT);
                JSONArray alsoKnownAs = sandwichName.getJSONArray(JSON_KEY_ALSO_KNOWN_AS);
                List<String> otherNames = new ArrayList<>();

                if (alsoKnownAs.length() != 0) {
                    otherNames = convertJSONArrayToArrayList(alsoKnownAs);
                } else {
                    otherNames.add(JSON_DEFAULT_DATA_TEXT);
                }

                String origin = sandwichJSON.optString(JSON_KEY_PLACE_OF_ORIGIN, JSON_DEFAULT_DATA_TEXT);
                String desc = sandwichJSON.optString(JSON_KEY_DESCRIPTION, JSON_DEFAULT_DATA_TEXT);

                String imgURL = sandwichJSON.has(JSON_KEY_IMAGE_URL) ? sandwichJSON.optString(JSON_KEY_IMAGE_URL) : null;

                JSONArray ingredientsJSONArray = sandwichJSON.getJSONArray(JSON_KEY_INGREDIENTS);
                List<String> inggredients = new ArrayList<>();

                if (ingredientsJSONArray.length() != 0) {
                    inggredients = convertJSONArrayToArrayList(ingredientsJSONArray);
                } else {
                    inggredients.add(JSON_DEFAULT_DATA_TEXT);
                }

                return new Sandwich(mainName, otherNames, origin, desc, imgURL, inggredients);

            }

        } catch (JSONException e) {
            Log.d("parseSandwichJson", "Error in parsing json data: " + e.getMessage());
        }

        return null;
    }

    private static List<String> convertJSONArrayToArrayList(JSONArray data) throws JSONException {

        ArrayList<String> result = new ArrayList<>();

        for (int i = 0; i < data.length(); i++) {
            result.add(data.optString(i));
        }

        return result;
    }
}
