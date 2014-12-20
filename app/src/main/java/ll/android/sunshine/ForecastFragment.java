package ll.android.sunshine;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class ForecastFragment extends Fragment {
    protected static ArrayAdapter<String> mForecastAdapter;
    public ForecastFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true); //allow OptionsMenu to show
    }

    //create OptionsMenu
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.forecastfragment, menu);
    }
    //fill items to OptionsMenu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                FetchWeatherTask weatherTask = new FetchWeatherTask();
                weatherTask.execute("Fremont,CA");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

/*
 *      fake data
 */
        String[] forecastArray = {
                "Today - sunny - 51/65",
                "Tomorrow - rainy - 46/55",
                "Thurs - cloudy - 51/60",
                "Fri - sunny - 55/65",
                "Sat - sunny - 55/70",
                "Sun - sunny - 55/68",
        };

        List<String> weekForecast = new ArrayList<String>(Arrays.asList(forecastArray));

        mForecastAdapter = new ArrayAdapter<String>(
                getActivity(),
                R.layout.list_item_forecast,
                R.id.list_item_forecast_textview,
                weekForecast
        );

        ListView listView = (ListView)rootView.findViewById(R.id.listview_forecast);
        listView.setAdapter(mForecastAdapter);

        return rootView;
    }
}