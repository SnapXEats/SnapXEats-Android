package com.snapxeats.ui.location;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.widget.ListView;

import com.snapxeats.R;
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
    ProgressDialog mDialog;

    public PlaceAPI(Context context) {
        mDialog = new ProgressDialog(context);
        mDialog.setMessage(context.getString(R.string.please_wait));
        mDialog.setCanceledOnTouchOutside(false);
    }

    public List<Prediction> autocomplete(String input) {
        List<String> resultList = null;
        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();


        try {
            StringBuilder sb = new StringBuilder(PLACES_API_BASE);
            sb.append("&input=" + URLEncoder.encode(input, "utf8"));

            URL url = new URL(sb.toString());
            SnapXToast.debug("Prediction url:"+url);
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
//            return resultList;
            return predictionList;
        } catch (IOException e) {
            Log.e(TAG, "Nw Error connecting to Places API", e);
//            return resultList;
            return predictionList;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        try {
            // Log.d(TAG, jsonResults.toString());

            // Create a JSON object hierarchy from the results
            JSONObject jsonObj = new JSONObject(jsonResults.toString());
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
            mDialog.dismiss();
        } catch (JSONException e) {
            Log.e(TAG, "Cannot process JSON results", e);
        }

//        return resultList;
        return predictionList;
    }

    public List<Prediction> getPredictionList() {
        return predictionList;
    }
}