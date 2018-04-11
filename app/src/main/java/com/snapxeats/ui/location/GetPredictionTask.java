package com.snapxeats.ui.location;

import android.os.AsyncTask;
import android.util.Log;

import com.snapxeats.common.constants.UIConstants;
import com.snapxeats.common.constants.WebConstants;
import com.snapxeats.common.model.location.Prediction;

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
 * Created by Snehal Tembare on 12/2/18.
 */

public class GetPredictionTask extends AsyncTask<String, Void, List<Prediction>> {

    HttpURLConnection conn = null;
    StringBuilder jsonResults = new StringBuilder();
    private List<Prediction> predictionList;
    private OnTaskCompleted onTaskCompleted;

    public GetPredictionTask(OnTaskCompleted onTaskCompleted) {
        this.onTaskCompleted = onTaskCompleted;
    }

    @Override
    protected List<Prediction> doInBackground(String... strings) {
        StringBuilder result = getData(strings[0]);
        if (null != result) {
            predictionList = parseData(result);
        }
        return predictionList;
    }

    @Override
    protected void onPostExecute(List<Prediction> predictions) {
        super.onPostExecute(predictions);
        if (null != predictionList) {
            onTaskCompleted.onTaskCompleted(predictions);
        }
    }

    /**
     * Create a JSON object hierarchy from the results
     */
    private List<Prediction> parseData(StringBuilder result) {
        List<String> resultList = null;
        List<Prediction> predictionList = null;
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
        }
        return predictionList;
    }

    /**
     * Call place Prediction API
     */

    StringBuilder getData(String input) {
        try {
            StringBuilder sb = new StringBuilder(UIConstants.PLACES_API_BASE);
            sb.append("&input=" + URLEncoder.encode(input, "utf8"));

            URL url = new URL(sb.toString());
            conn = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream());

            // Load the results into a StringBuilder
            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);
            }
        } catch (MalformedURLException e) {
            Log.e(UIConstants.TAG, "Error processing Places API URL", e);

        } catch (IOException e) {
            Log.e(UIConstants.TAG, "Nw Error connecting to Places API", e);
        } finally {
            if (null != conn) {
                conn.disconnect();
            }
        }

        return jsonResults;
    }
}