package com.example.sarth.quakereport;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

/**
 * Created by sarth on 9/7/2017.
 */

public class QueryUtils {

    static String result=null;
  /*
        private static String generateString()
        {
            URL url = createUrl("https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&starttime=2017-08-29&limit=10");

            // Perform HTTP request to the URL and receive a JSON response back
            String jsonResponse = "";
            try {
                jsonResponse = makeHttpRequest(url);
            } catch (IOException e) {
                // TODO Handle the IOException
            }
            return jsonResponse;
        }*/


  public static String getResponse(String ur) {
        // Create URL object

        URL url = createUrl(ur);

        // Perform HTTP request to the URL and receive a JSON response back
        try {
            result = makeHttpRequest(url);
            Log.v("string",result);
        } catch (IOException e) {
            // TODO Handle the IOException
        }
        return result;
    }



    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException exception) {
            Log.e("EarthquakeActivity", "Error with creating URL", exception);
            return null;
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.connect();
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            }
            else
            {
                Log.e("QueryUtils",Integer.toString(urlConnection.getResponseCode()));
            }
        }catch (IOException e) {
            // TODO: Handle the exception
            Log.e("QueryUtils","Exception occured:",e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // function must handle java.io.IOException here
                inputStream.close();
            }
        }
        Log.v("ssss",jsonResponse);
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    @NonNull
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        Log.v("VVV",output.toString());
        return output.toString();
    }




    public static ArrayList<word> extractEarthquakes(String result1) {

        // Create an empty ArrayList that we can start adding earthquakes to
        ArrayList<word> earthquakes = new ArrayList<>();

        // Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {
            Thread.sleep(2000);
            if(result1 != "") {
                JSONObject json = new JSONObject(result1);
                JSONArray features = json.getJSONArray("features");
                for (int i = 0; i < features.length(); i++) {
                    JSONObject element = features.getJSONObject(i);
                    JSONObject properties = element.getJSONObject("properties");
                    double mag = properties.optDouble("mag");
                    String place = properties.optString("place");
                    long time = properties.optLong("time");
                    earthquakes.add(new word(mag, place, time));
                }
            }
        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        } catch(InterruptedException e)
        {
            e.printStackTrace();
        }

        // Return the list of earthquakes
        return earthquakes;
    }

    public static String extractUrl(int i)
    {
        String url="";
        try{
            JSONObject json = new JSONObject(result);
            JSONArray features = json.getJSONArray("features");
            JSONObject element = features.getJSONObject(i);
            JSONObject properties = element.getJSONObject("properties");
            url = properties.getString("url");
        }
        catch(JSONException e)
        {
            Log.e("QueryUtils","Problem parsing the url", e);
        }
        return url;
    }
}