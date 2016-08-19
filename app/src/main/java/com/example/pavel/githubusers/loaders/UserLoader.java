package com.example.pavel.githubusers.loaders;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.example.pavel.githubusers.R;
import com.example.pavel.githubusers.database.UsersDataSource;
import com.example.pavel.githubusers.github.GitHubService;
import com.example.pavel.githubusers.model.User;
import com.example.pavel.githubusers.utils.ToastUtil;

import java.io.IOException;
import java.util.List;

import retrofit2.Response;

public class UserLoader extends ItemsAsyncLoader<User> {

    public static final String LAST_USER_ID_PARAM = "lastUserIdParam";
    private static final String TAG = UserLoader.class.getSimpleName();
    private Bundle mArgs;

    public UserLoader(Context context, Bundle args) {
        super(context);
        mArgs = args;
    }

    @Override
    public List<User> loadInBackground() {
        List<User> items = null;
        Response<List<User>> response = null;
        boolean isFirstRequest = false;
        try {
            if (mArgs != null && mArgs.getInt(LAST_USER_ID_PARAM) > 0) {
                response = GitHubService.getClient().getUsersSinceID(mArgs.getInt(LAST_USER_ID_PARAM)).execute();
            } else {
                isFirstRequest = true;
                response = GitHubService.getClient().getUsers().execute();
            }
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
            ToastUtil.showToast(R.string.get_data_network_error);
        }

        if (response != null) {
            if (response.code() == 200) {
                items = response.body();
                Log.d(TAG, "getUsers: items.size()=" + items.size());
            } else {
                Log.e(TAG, "getUsers: error=" + response.message());
                ToastUtil.showToast(R.string.get_data_network_error);
            }
        }
        if (isFirstRequest) {
            if (items == null) {
                items = UsersDataSource.getInstance().getUsers();
            } else {
                UsersDataSource.getInstance().clear();
                UsersDataSource.getInstance().addUsers(items);
            }
        } else {
            UsersDataSource.getInstance().addUsers(items);
        }
        return items;
    }
}
