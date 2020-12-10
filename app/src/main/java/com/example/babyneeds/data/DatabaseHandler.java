package com.example.babyneeds.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.babyneeds.model.Item;
import com.example.babyneeds.util.Settings;

import java.nio.channels.SeekableByteChannel;
import java.sql.SQLInput;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {

    private final Context context;

    public DatabaseHandler(@Nullable Context context) {
        super(context, Settings.DB_NAME, null, Settings.DB_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_BABIES_TABLE = "create table " + Settings.BABIES_TABLE + "(" +
                Settings.KEY_ID + " integer primary key," +
                Settings.KEY_ITEM + " text," +
                Settings.KEY_COLOR + " text," +
                Settings.KEY_QUANTITY + " integer," +
                Settings.KEY_SIZE + " text," +
                Settings.KEY_DATE_NAME + " long);";

        db.execSQL(CREATE_BABIES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("drop table if exists " + Settings.BABIES_TABLE);

        onCreate(db);
    }

    // CRUD Operations
    public void addItem(Item item) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Settings.KEY_ITEM, item.getName());
        values.put(Settings.KEY_COLOR, item.getColor());
        values.put(Settings.KEY_QUANTITY, item.getQuantity());
        values.put(Settings.KEY_SIZE, item.getSize());
        values.put(Settings.KEY_DATE_NAME, java.lang.System.currentTimeMillis());

        // Insert the row
        db.insert(Settings.BABIES_TABLE, null, values);

        Log.d("DB", " added item: " + item);
    }

    // Get an Item
    public Item getItem(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

//        Cursor cursor = db.query(Settings.BABIES_TABLE,
//                new String[]{Settings.KEY_ID, Settings.KEY_COLOR, Settings.KEY_QUANTITY, Settings.KEY_SIZE,
//                        Settings.KEY_DATE_NAME},
//                Settings.KEY_ID + "=?",
//                new String[]{String.valueOf(id)},
//                null,
//                null,
//                null
//        );
       Cursor cursor = db.rawQuery("select * from " + Settings.BABIES_TABLE + " where " + Settings.KEY_ID + " = " + String.valueOf(id), null);

        Item item = new Item();

        if (cursor != null) {
            cursor.moveToFirst();

            item.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Settings.KEY_ID))));
            item.setName(cursor.getString(cursor.getColumnIndex(Settings.KEY_ITEM)));
            item.setSize(cursor.getString(cursor.getColumnIndex(Settings.KEY_SIZE)));

            // convert Timestamp to something readable
            DateFormat dateFormat = DateFormat.getInstance();
            String formattedDate = dateFormat.format(new Date(cursor.getLong(cursor.getColumnIndex(Settings.KEY_DATE_NAME))).getTime());

            item.setDateItemAdded(formattedDate);
        }

        return item;
    }

    public List<Item> getAllItems() {
        SQLiteDatabase db = this.getReadableDatabase();

        List<Item> items = new ArrayList<>();

        Cursor cursor = db.rawQuery("select * from " + Settings.BABIES_TABLE + " order by " + Settings.KEY_DATE_NAME + " desc" ,null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                Item item = new Item();
                item.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Settings.KEY_ID))));
                item.setName(cursor.getString(cursor.getColumnIndex(Settings.KEY_ITEM)));
                item.setSize(cursor.getString(cursor.getColumnIndex(Settings.KEY_SIZE)));
                item.setQuantity(cursor.getInt(cursor.getColumnIndex(Settings.KEY_QUANTITY)));
                item.setColor(cursor.getString(cursor.getColumnIndex(Settings.KEY_COLOR)));

                // convert Timestamp to something readable
                DateFormat dateFormat = DateFormat.getInstance();
                String formattedDate = dateFormat.format(new Date(cursor.getLong(cursor.getColumnIndex(Settings.KEY_DATE_NAME))).getTime());

                item.setDateItemAdded(formattedDate);

                // add to array list
                items.add(item);
            } while (cursor.moveToNext());
        }

        return items;
    }

    public int updateItem(Item item) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Settings.KEY_ITEM, item.getName());
        values.put(Settings.KEY_COLOR, item.getColor());
        values.put(Settings.KEY_QUANTITY, item.getQuantity());
        values.put(Settings.KEY_SIZE, item.getSize());

        // Insert the row
        int itemID = db.update(Settings.BABIES_TABLE, values, Settings.KEY_ID + " =?", new String[]{String.valueOf(item.getId())});

        return item.getId();
    }

    public void deleteItem(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Settings.BABIES_TABLE,
                Settings.KEY_ID + "=?",
                new String[] {String.valueOf(id)});
    }

    public int getItemsCount() {
        String countQuery = "select * from " + Settings.BABIES_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(countQuery, null);

        return cursor.getCount();
    }
}
