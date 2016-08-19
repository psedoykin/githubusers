package com.example.pavel.githubusers.utils;

import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.example.pavel.githubusers.App;
import com.example.pavel.githubusers.R;

public class ToastUtil {
    private static final Handler handler = new Handler(Looper.getMainLooper());
    private static Toast toast;

    public static void showToast(int messageId) {
        showToast(App.getContext().getString(messageId));
    }

    public static void showToast(final String message) {
        handler.post(new Runnable() {

            public void run() {
                if (toast != null) {
                    toast.cancel();
                }
                if (message == null) {
                    toast = Toast.makeText(App.getContext(), App.getContext().getString(R.string.unknown_internal_error), Toast.LENGTH_LONG);
                } else {
                    toast = Toast.makeText(App.getContext(), message, Toast.LENGTH_LONG);
                }
                toast.show();
            }
        });
    }
}
