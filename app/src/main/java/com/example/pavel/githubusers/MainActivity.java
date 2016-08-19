package com.example.pavel.githubusers;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import com.example.pavel.githubusers.ui.fragments.UserListFragment;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private static final UserListFragment userListFragment = new UserListFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onLoadFinished:");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.base_content_frame, userListFragment, UserListFragment.TAG).commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        Log.d(TAG, "onOptionsItemSelected: menuItem=" + getResources().getResourceName(menuItem.getItemId()));

        switch (menuItem.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }
}
