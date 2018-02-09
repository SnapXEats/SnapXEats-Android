package com.snapxeats.ui.location;

import android.util.Log;

import com.snapxeats.common.constants.SnapXToast;
import com.snapxeats.common.constants.WebConstants;
import com.snapxeats.common.model.Prediction;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Snehal Tembare on 31/1/18.
 */

public class PlaceAPI {

    private static final String TAG = PlaceAPI.class.getSimpleName();

    private static final String PLACES_API_BASE = WebConstants.GOOGLE_BASE_URL +
            WebConstants.PREDICTION_LIST;

    List<Prediction> predictionList = null;
    HttpURLConnection conn = null;
    StringBuilder jsonResults = new StringBuilder();

    public List<Prediction> autocomplete(String input) {

        StringBuilder result = getData(input);
        if (result != null) {
            predictionList = parseData(result);
        }
        return predictionList;
    }


    /**
     * Create a JSON object hierarchy from the results
     */
    private List<Prediction> parseData(StringBuilder result) {
        List<String> resultList = null;
        try {
            JSONObject jsonObj = new JSONObject(result.toString());
            JSONArray predsJsonArray = jsonObj.getJSONArray("predictions");

            // Extract the Place descriptions from the results
            resultList = new ArrayList<>(predsJsonArray.length());
            predictionList = new ArrayList<>(predsJsonArray.length());
            Prediction prediction;
            for (int i = 0; i < predsJsonArray.length(); i++) {
                prediction = new Prediction(predsJsonArray.getJSONObject(i).getString("description"),
                        predsJsonArray.getJSONObject(i).getString("place_id"));
                resultList.add(predsJsonArray.getJSONObject(i).getString("description"));
                predictionList.add(prediction);
            }
        } catch (JSONException e) {
            Log.e(TAG, "Cannot process JSON results", e);
        }
        return predictionList;
    }

    /**
     * Call place Prediction API
     */

    StringBuilder getData(String input) {
        try {
            StringBuilder sb = new StringBuilder(PLACES_API_BASE);
            sb.append("&input=" + URLEncoder.encode(input, "utf8"));

            URL url = new URL(sb.toString());
            SnapXToast.debug("Prediction url:" + url);
            conn = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream());

            // Load the results into a StringBuilder
            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);
            }
        } catch (MalformedURLException e) {
            Log.e(TAG, "Error processing Places API URL", e);

        } catch (IOException e) {
            Log.e(TAG, "Nw Error connecting to Places API", e);
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        return jsonResults;
    }
}