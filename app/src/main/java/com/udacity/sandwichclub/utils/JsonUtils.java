package com.udacity.sandwichclub.utils;

import android.util.Log;

import com.udacity.sandwichclub.model.Sandwich;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonUtils {

    public static Sandwich parseSandwichJson(String json) {

        JSONObject sandwichJSON;
        String defaultText = "Not Available";

        try {
            sandwichJSON = new JSONObject(json);

            JSONObject sandwichName = sandwichJSON.getJSONObject("name");

            String mainName = sandwichName.getString("mainName").isEmpty() ? defaultText : sandwichName.getString("mainName");

            JSONArray alsoKnownAs = sandwichName.getJSONArray("alsoKnownAs");

            List<String> otherNames = new ArrayList<>();

            if (alsoKnownAs.length() != 0) {
                otherNames = convertJSONArrayToArrayList(alsoKnownAs);
            } else {
                otherNames.add(defaultText);
            }

            String origin = sandwichJSON.getString("placeOfOrigin").isEmpty() ? defaultText : sandwichJSON.getString("placeOfOrigin");

            String desc = sandwichJSON.getString("description").isEmpty() ? defaultText : sandwichJSON.getString("description");

            String imgURL = sandwichJSON.getString("image");

            JSONArray ingredientsJSONArray = sandwichJSON.getJSONArray("ingredients");

            List<String> inggredients = new ArrayList<>();

            if (ingredientsJSONArray.length() != 0) {
                inggredients = convertJSONArrayToArrayList(ingredientsJSONArray);
            } else {
                inggredients.add(defaultText);
            }

            return new Sandwich(mainName, otherNames, origin, desc, imgURL, inggredients);

        } catch (JSONException e) {
            Log.d("parseSandwichJson", "Error in parsing json data: " + e.getMessage());
        }

        return null;
    }

    private static List<String> convertJSONArrayToArrayList(JSONArray otherNamesArray) throws JSONException {

        ArrayList<String> result = new ArrayList<>();

        for (int i = 0; i < otherNamesArray.length(); i++) {
            result.add(otherNamesArray.getString(i));
        }

        return result;
    }
}
