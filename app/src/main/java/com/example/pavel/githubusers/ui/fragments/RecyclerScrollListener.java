package com.example.pavel.githubusers.ui.fragments;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public class RecyclerScrollListener extends RecyclerView.OnScrollListener {

    public interface ScrollViewListener {
        void onLoadMore();
    }

    private static final int VISIBLE_THRESHOLD = 5;
    private ScrollViewListener mScrollViewListener;
    private int previousTotalItemCount;
    private boolean loadingMode = true;

    public RecyclerScrollListener(ScrollViewListener listener) {
        mScrollViewListener = listener;
    }

    public void init() {
        previousTotalItemCount = 0;
        loadingMode = true;
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        int visibleItemCount = layoutManager.getChildCount();
        int totalItemCount = layoutManager.getItemCount();
        int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
        if (newState == RecyclerView.SCROLL_STATE_SETTLING) {
            if (loadingMode) {
                if (totalItemCount >= previousTotalItemCount) {
                    loadingMode = false;
                    previousTotalItemCount = totalItemCount;
                }
            }
            if (!loadingMode && visibleItemCount + firstVisibleItemPosition >= totalItemCount - VISIBLE_THRESHOLD) {
                loadingMode = true;
                if (mScrollViewListener != null) {
                    mScrollViewListener.onLoadMore();
                }
            }
        }
    }
}
