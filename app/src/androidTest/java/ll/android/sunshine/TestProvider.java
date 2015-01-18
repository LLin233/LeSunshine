package ll.android.sunshine;

/**
 * Created by Le on 2015/1/12.
 */

import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.test.AndroidTestCase;
import android.util.Log;

import ll.android.sunshine.data.WeatherDBHelper;
import ll.android.sunshine.data.WeatherContract.LocationEntry;
import ll.android.sunshine.data.WeatherContract.WeatherEntry;


public class TestProvider extends AndroidTestCase {

    public static final String LOG_TAG = TestProvider.class.getSimpleName();

    public void testDeleteDb() throws Throwable {
        mContext.deleteDatabase(WeatherDBHelper.DATABASE_NAME);
    }

    //    public void testInsertReadDb() {
//
//        WeatherDBHelper dbHelper = new WeatherDBHelper(mContext);
//        SQLiteDatabase db = dbHelper.getWritableDatabase();
//
//        ContentValues testValues = TestDB.createNorthPoleLocationValues();
//
//        long locationRowId;
//        locationRowId = db.insert(LocationEntry.TABLE_NAME, null, testValues);
//
//        // Verify we got a row back.
//        assertTrue(locationRowId != -1);
//        Log.d(LOG_TAG, "New row id: " + locationRowId);
//
//
//        // A cursor is your primary interface to the query results.
//        Cursor cursor = db.query(
//                LocationEntry.TABLE_NAME,  // Table to Query
//                null, // all columns
//                null, // Columns for the "where" clause
//                null, // Values for the "where" clause
//                null, // columns to group by
//                null, // columns to filter by row groups
//                null // sort order
//        );
//
//        TestDB.validateCursor(cursor, testValues);
//
//        ContentValues weatherValues = TestDB.createWeatherValues(locationRowId);
//
//        long weatherRowId = db.insert(WeatherEntry.TABLE_NAME, null, weatherValues);
//        assertTrue(weatherRowId != -1);
//
//        // A cursor is your primary interface to the query results.
//        Cursor weatherCursor = db.query(
//                WeatherEntry.TABLE_NAME,  // Table to Query
//                null, // leaving "columns" null just returns all the columns.
//                null, // cols for "where" clause
//                null, // values for "where" clause
//                null, // columns to group by
//                null, // columns to filter by row groups
//                null  // sort order
//        );
//
//        TestDB.validateCursor(weatherCursor, weatherValues);
//
//        // Add the location values in with the weather data so that we can make
//        // sure that the join worked and we actually get all the values back
//        addAllContentValues(weatherValues, testValues);
//
//        // Get the joined Weather and Location data
//        weatherCursor = mContext.getContentResolver().query(
//                WeatherEntry.buildWeatherLocation(TestDB.TEST_LOCATION),
//                null, // leaving "columns" null just returns all the columns.
//                null, // cols for "where" clause
//                null, // values for "where" clause
//                null  // sort order
//        );
//        TestDB.validateCursor(weatherCursor, weatherValues);
//
//        // Get the joined Weather and Location data with a start date
//        weatherCursor = mContext.getContentResolver().query(
//                WeatherEntry.buildWeatherLocationWithStartDate(
//                        TestDB.TEST_LOCATION, TestDB.TEST_DATE),
//                null, // leaving "columns" null just returns all the columns.
//                null, // cols for "where" clause
//                null, // values for "where" clause
//                null  // sort order
//        );
//        TestDB.validateCursor(weatherCursor, weatherValues);
//
//
//        dbHelper.close();
//    }
    public void testInsertReadProvider() {

        ContentValues testValues = TestDB.createNorthPoleLocationValues();

        Uri locationUri = mContext.getContentResolver().insert(LocationEntry.CONTENT_URI, testValues);
        long locationRowId = ContentUris.parseId(locationUri);

        // Verify we got a row back.
        assertTrue(locationRowId != -1);

        // Data's inserted.  IN THEORY.  Now pull some out to stare at it and verify it made
        // the round trip.

        // A cursor is your primary interface to the query results.
        Cursor cursor = mContext.getContentResolver().query(
                LocationEntry.CONTENT_URI,
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null  // sort order
        );

        TestDB.validateCursor(cursor, testValues);

        // Now see if we can successfully query if we include the row id
        cursor = mContext.getContentResolver().query(
                LocationEntry.buildLocationUri(locationRowId),
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null  // sort order
        );

        TestDB.validateCursor(cursor, testValues);

        // Fantastic.  Now that we have a location, add some weather!
        ContentValues weatherValues = TestDB.createWeatherValues(locationRowId);

        Uri weatherInsertUri = mContext.getContentResolver()
                .insert(WeatherEntry.CONTENT_URI, weatherValues);
        assertTrue(weatherInsertUri != null);

        // A cursor is your primary interface to the query results.
        Cursor weatherCursor = mContext.getContentResolver().query(
                WeatherEntry.CONTENT_URI,  // Table to Query
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null // columns to group by
        );

        TestDB.validateCursor(weatherCursor, weatherValues);


        // Add the location values in with the weather data so that we can make
        // sure that the join worked and we actually get all the values back
        addAllContentValues(weatherValues, testValues);

        // Get the joined Weather and Location data
        weatherCursor = mContext.getContentResolver().query(
                WeatherEntry.buildWeatherLocation(TestDB.TEST_LOCATION),
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null  // sort order
        );
        TestDB.validateCursor(weatherCursor, weatherValues);

        // Get the joined Weather and Location data with a start date
        weatherCursor = mContext.getContentResolver().query(
                WeatherEntry.buildWeatherLocationWithStartDate(
                        TestDB.TEST_LOCATION, TestDB.TEST_DATE),
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null  // sort order
        );
        TestDB.validateCursor(weatherCursor, weatherValues);

        // Get the joined Weather data for a specific date
        weatherCursor = mContext.getContentResolver().query(
                WeatherEntry.buildWeatherLocationWithDate(TestDB.TEST_LOCATION, TestDB.TEST_DATE),
                null,
                null,
                null,
                null
        );
        TestDB.validateCursor(weatherCursor, weatherValues);
    }

    public void testGetType() {
        // content://ll.android.sunshine/weather/
        String type = mContext.getContentResolver().getType(WeatherEntry.CONTENT_URI);
        // vnd.android.cursor.dir/ll.android.sunshine/weather
        assertEquals(WeatherEntry.CONTENT_TYPE, type);

        String testLocation = "94074";
        // content://ll.android.sunshine/weather/94074
        type = mContext.getContentResolver().getType(
                WeatherEntry.buildWeatherLocation(testLocation));
        // vnd.android.cursor.dir/ll.android.sunshine/weather
        assertEquals(WeatherEntry.CONTENT_TYPE, type);

        String testDate = "20140612";
        // content://ll.android.sunshine/weather/94074/20140612
        type = mContext.getContentResolver().getType(
                WeatherEntry.buildWeatherLocationWithDate(testLocation, testDate));
        // vnd.android.cursor.item/ll.android.sunshine/weather
        assertEquals(WeatherEntry.CONTENT_ITEM_TYPE, type);

        // content://ll.android.sunshine/location/
        type = mContext.getContentResolver().getType(LocationEntry.CONTENT_URI);
        // vnd.android.cursor.dir/ll.android.sunshine/location
        assertEquals(LocationEntry.CONTENT_TYPE, type);

        // content://ll.android.sunshine/location/1
        type = mContext.getContentResolver().getType(LocationEntry.buildLocationUri(1L));
        // vnd.android.cursor.item/ll.android.sunshine/location
        assertEquals(LocationEntry.CONTENT_ITEM_TYPE, type);
    }

    // in a test it's fine to assume a higher target.
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    void addAllContentValues(ContentValues destination, ContentValues source) {
        for (String key : source.keySet()) {
            destination.put(key, source.getAsString(key));
        }
    }
}
