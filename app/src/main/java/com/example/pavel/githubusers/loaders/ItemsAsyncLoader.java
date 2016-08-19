package com.example.pavel.githubusers.loaders;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import java.util.List;

public class ItemsAsyncLoader<D> extends AsyncTaskLoader<List<D>> {

    private List<D> mData;

    public ItemsAsyncLoader(Context context) {
        super(context);
    }

    @Override
    public List<D> loadInBackground() {
        return null;
    }

    @Override
    public void deliverResult(List<D> data) {
        if (isReset()) {
            return;
        }
        mData = data;
        super.deliverResult(data);
    }

    @Override
    protected void onStartLoading() {
        if (mData != null) {
            deliverResult(mData);
        }
        if (takeContentChanged() || mData == null) {
            forceLoad();
        }
    }

    @Override
    protected void onStopLoading() {
        cancelLoad();
    }

    @Override
    protected void onReset() {
        super.onReset();
        onStopLoading();
        mData = null;
    }
}
