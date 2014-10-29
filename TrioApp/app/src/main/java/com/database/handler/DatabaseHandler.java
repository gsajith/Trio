package com.database.handler;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.volley.model.Event;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gsajith on 10/4/2014.
 */
public class DatabaseHandler extends SQLiteOpenHelper{
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "savedEvents";
    private static final String TABLE_EVENTS = "events";

    private static final String KEY_ID = "id";
    private static final String KEY_ID_NUM = "idnum";
    private static final String KEY_TITLE = "title";
    private static final String KEY_LOCATION = "location";
    private static final String KEY_TIME = "time";
    private static final String KEY_URL = "url";
    private static final String KEY_PRICE = "price";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_EVENTS_TABLE = "CREATE TABLE " + TABLE_EVENTS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_ID_NUM + " TEXT," + KEY_TITLE + " TEXT,"
                + KEY_LOCATION + " TEXT,"+ KEY_TIME + " TEXT,"
                + KEY_URL + " TEXT,"+ KEY_PRICE + " TEXT" + ")";
        sqLiteDatabase.execSQL(CREATE_EVENTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENTS);
        onCreate(sqLiteDatabase);
    }

    public void addEvent(Event event) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID_NUM, DatabaseUtils.sqlEscapeString(event.getId()));
        values.put(KEY_TITLE, DatabaseUtils.sqlEscapeString(event.getTitle()));
        values.put(KEY_LOCATION, DatabaseUtils.sqlEscapeString(event.getLocation()));
        values.put(KEY_TIME, DatabaseUtils.sqlEscapeString(event.getTime()));
        values.put(KEY_URL, DatabaseUtils.sqlEscapeString(event.getThumbnailUrl()));
        values.put(KEY_PRICE, DatabaseUtils.sqlEscapeString(event.getPrice()));
        db.insert(TABLE_EVENTS, null, values);
        db.close();
    }

    public Event getEvent(String id) {
      String selectQuery = "SELECT  * FROM " + TABLE_EVENTS;

      SQLiteDatabase db = this.getWritableDatabase();
      Cursor cursor = db.rawQuery(selectQuery, null);
      Log.d("EVENTTT", "still nothin " + id);
      if (cursor.moveToFirst()) {
        Log.d("EVENTTT", "nothing broke");
        do {
          Log.d("EVENTTT", "still good comparing cursor: " + cursor.getString(1) + " to " + id);
          if (sqlUnescape(cursor.getString(1)).equals(id)) {
            Log.d("EVENTTT", sqlUnescape(cursor.getString(2)) + " " + sqlUnescape(cursor.getString(5)) + " " +
              sqlUnescape(cursor.getString(6)) + " " + sqlUnescape(cursor.getString(3)) + " " +
              sqlUnescape(cursor.getString(4)) + " " + sqlUnescape(cursor.getString(1)));
            Event event = new Event();
            event.setId(sqlUnescape(cursor.getString(1)));
            event.setTitle(sqlUnescape(cursor.getString(2)));
            event.setLocation(sqlUnescape(cursor.getString(3)));
            event.setTime(sqlUnescape(cursor.getString(4)));
            event.setThumbnailUrl(sqlUnescape(cursor.getString(5)));
            event.setPrice(sqlUnescape(cursor.getString(6)));
            return event;
          }
        } while (cursor.moveToNext());
      }
      return null;
    }

    private String sqlUnescape(String str) {
        if (str.length() > 2) {
            return str.substring(1, str.length()-1);
        } else {
            return "";
        }
    }

    public List<Event> getAllEvents() {
        List<Event> eventList = new ArrayList<Event>();
        String selectQuery = "SELECT  * FROM " + TABLE_EVENTS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {

              Log.d("EVENTTTO", sqlUnescape(cursor.getString(2)) + " " +  sqlUnescape(cursor.getString(5)) + " " +
                sqlUnescape(cursor.getString(6)) + " " +  sqlUnescape(cursor.getString(3)) + " " +
                sqlUnescape(cursor.getString(4)) + " " +  sqlUnescape(cursor.getString(1)));
                Event event = new Event();
                event.setId(sqlUnescape(cursor.getString(1)));
                event.setTitle(sqlUnescape(cursor.getString(2)));
                event.setLocation(sqlUnescape(cursor.getString(3)));
                event.setTime(sqlUnescape(cursor.getString(4)));
                event.setThumbnailUrl(sqlUnescape(cursor.getString(5)));
                event.setPrice(sqlUnescape(cursor.getString(6)));
                eventList.add(event);
            } while (cursor.moveToNext());
        }

        return eventList;
    }

    public int getEventCount() {
        String countQuery = "SELECT  * FROM " + TABLE_EVENTS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        return cursor.getCount();
    }

    public void deleteAllEvents() {
        SQLiteDatabase db = this.getReadableDatabase();
        db.delete(TABLE_EVENTS, null, null);
    }

    public int updateEvent(Event event) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID_NUM, DatabaseUtils.sqlEscapeString(event.getId()));
        values.put(KEY_TITLE, DatabaseUtils.sqlEscapeString(event.getTitle()));
        values.put(KEY_LOCATION, DatabaseUtils.sqlEscapeString(event.getLocation()));
        values.put(KEY_TIME, DatabaseUtils.sqlEscapeString(event.getTime()));
        values.put(KEY_URL, DatabaseUtils.sqlEscapeString(event.getThumbnailUrl()));
        values.put(KEY_PRICE, DatabaseUtils.sqlEscapeString(event.getPrice()));

        return db.update(TABLE_EVENTS, values, KEY_ID_NUM + " = ?",
                new String[] {event.getId()});
    }

    public void deleteEvent(Event event) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_EVENTS, KEY_ID_NUM + " = ?",
                new String[] { DatabaseUtils.sqlEscapeString(event.getId())});
        db.close();
    }

}
