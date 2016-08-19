package com.example.pavel.githubusers.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.example.pavel.githubusers.App;
import com.example.pavel.githubusers.model.User;

import java.util.ArrayList;
import java.util.List;

public class UsersDataSource {

    private static final String TAG = UsersDataSource.class.getSimpleName();

    private static final Object lock = new Object();
    private static volatile UsersDataSource instance;

    private DatabaseHelper dbHelper;

    private String[] allColumns = {DatabaseHelper.COLUMN_ID,
            DatabaseHelper.COLUMN_USER_ID,
            DatabaseHelper.COLUMN_LOGIN,
            DatabaseHelper.COLUMN_AVATAR,
            DatabaseHelper.COLUMN_URL
    };

    private UsersDataSource() {
        dbHelper = new DatabaseHelper(App.getContext());
    }

    public static UsersDataSource getInstance() {
        UsersDataSource usersDataSource = instance;
        if (usersDataSource == null) {
            synchronized (lock) {
                usersDataSource = instance;
                if (usersDataSource == null) {
                    usersDataSource = new UsersDataSource();
                    instance = usersDataSource;
                }
            }
        }
        return usersDataSource;
    }

    public synchronized void addUsers(List<User> userList) {
        Log.d(TAG, "addUsers:");
        if (userList != null && !userList.isEmpty()) {
            SQLiteDatabase database = null;
            try {
                database = dbHelper.getWritableDatabase();
                for (User user : userList) {
                    ContentValues values = new ContentValues();
                    values.put(DatabaseHelper.COLUMN_USER_ID, user.getId());
                    values.put(DatabaseHelper.COLUMN_LOGIN, user.getLogin());
                    values.put(DatabaseHelper.COLUMN_AVATAR, user.getAvatarUrl());
                    values.put(DatabaseHelper.COLUMN_URL, user.getHtmlUrl());

                    long insertId = database.insert(DatabaseHelper.TABLE_USERS, null, values);

                    Log.d(TAG, "user inserted" + String.valueOf(insertId));
                }
            } catch (SQLiteException exc) {
                Log.w(TAG, "Error while opening database", exc);
            } finally {
                if (database != null) {
                    database.close();
                }
            }
        }
    }

    public synchronized void clear() {
        Log.d(TAG, "clear:");
        SQLiteDatabase database = null;
        try {
            database = dbHelper.getWritableDatabase();
            database.execSQL("delete from " + DatabaseHelper.TABLE_USERS);
        } catch (SQLiteException exc) {
            Log.w(TAG, "Error while opening database", exc);
        } finally {
            if (database != null) {
                database.close();
            }
        }
    }

    public synchronized List<User> getUsers() {
        Log.d(TAG, "getUsers:");
        List<User> userList = new ArrayList<>();
        SQLiteDatabase database = null;
        Cursor cursor = null;

        try {
            database = dbHelper.getReadableDatabase();
            cursor = database.query(DatabaseHelper.TABLE_USERS, allColumns, null, null, null, null, null);

            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                User user = new User();
                user.setId(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_USER_ID)));
                user.setLogin(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_LOGIN)));
                user.setAvatarUrl(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_AVATAR)));
                user.setHtmlUrl(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_URL)));
                userList.add(user);
                cursor.moveToNext();
            }
        } catch (SQLiteException exc) {
            Log.w(TAG, "Error while opening database", exc);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (database != null) {
                database.close();
            }
        }
        return userList;
    }
}
