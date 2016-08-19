package com.example.pavel.githubusers.loaders;

import android.content.Context;

import com.example.pavel.githubusers.database.UsersDataSource;
import com.example.pavel.githubusers.model.User;

import java.util.List;

public class UserFromDBLoader extends ItemsAsyncLoader<User> {

    public UserFromDBLoader(Context context) {
        super(context);
    }

    @Override
    public List<User> loadInBackground() {
        List<User> items = UsersDataSource.getInstance().getUsers();
        return items;
    }
}
