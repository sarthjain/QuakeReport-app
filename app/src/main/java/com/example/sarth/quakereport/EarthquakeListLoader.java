package com.example.sarth.quakereport;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.util.List;

/**
 * Created by sarth on 9/7/2017.
 */

public class EarthquakeListLoader extends AsyncTaskLoader<List<word>> {

    String url;

    EarthquakeListLoader(Context context, String attr)
    {
        super(context);
        url=attr;
    }

    @Override
    protected void onStartLoading() {
        Log.i("EarthquakeListLoader","onstartLoading");
        forceLoad();
    }

    @Override
    public List<word> loadInBackground() {
        Log.i("EarthquakeListLoader","loadInBackground");
        String result = QueryUtils.getResponse(url);
        List<word> data = QueryUtils.extractEarthquakes(result);
        return data;
    }
}
