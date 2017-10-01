package com.example.sarth.quakereport;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.app.LoaderManager;
import android.content.Loader;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class EarthquakeActivity extends AppCompatActivity  implements LoaderManager.LoaderCallbacks<List<word>>
{
    WordAdapter arrayAdapter = null;
    ListView list;
    private static final String USGS_REQUEST_URL = "https://earthquake.usgs.gov/fdsnws/event/1/query";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("Earthquake","onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        list =(ListView) findViewById(R.id.list);
        TextView empty = (TextView)findViewById(R.id.empty_view);
        list.setEmptyView(empty);
        ConnectivityManager cm = (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        if(isConnected)
        {
            getLoaderManager().initLoader(0, null, this);
        }
        else
        {
            empty.setText(R.string.noInternet_string);
            findViewById(R.id.loading_spinner).setVisibility(View.GONE);
        }
    }

    @Override
    public Loader<List<word>> onCreateLoader(int id, Bundle args) {
        Log.i("EarthquakeActivity","oncreateLoader");
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        String minMagnitude = sharedPrefs.getString(
                getString(R.string.settings_min_magnitude_key),
                getString(R.string.settings_min_magnitude_default));

        String orderBy = sharedPrefs.getString(
                getString(R.string.settings_order_by_key),
                getString(R.string.settings_order_by_default)
        );

        Uri baseUri = Uri.parse(USGS_REQUEST_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        uriBuilder.appendQueryParameter("format", "geojson");
        uriBuilder.appendQueryParameter("limit", "10");
        uriBuilder.appendQueryParameter("minmag", minMagnitude);
        uriBuilder.appendQueryParameter("orderby", orderBy);

        return new EarthquakeListLoader(this, uriBuilder.toString());
    }


    @Override
    public void onLoadFinished(Loader<List<word>> loader, List<word> data) {
        Log.i("EarthquakeActivity","onLoadFinished");
        display(data);
    }

    @Override
    public void onLoaderReset(Loader<List<word>> loader) {
        Log.i("EarthquakeActivity","onLoaderReset");
        arrayAdapter.clear();
    }

    private void display(List<word> data)
    {

        findViewById(R.id.loading_spinner).setVisibility(View.GONE);
        arrayAdapter =new WordAdapter(getApplicationContext(),data);
        list.setAdapter(arrayAdapter);
        TextView empty = (TextView)findViewById(R.id.empty_view);
        empty.setText(R.string.empty_string);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String url = QueryUtils.extractUrl(i);
                Intent intent=new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
