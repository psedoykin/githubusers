package com.example.pavel.githubusers.ui.fragments;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.ScrollView;

import com.example.pavel.githubusers.MainActivity;
import com.example.pavel.githubusers.R;
import com.example.pavel.githubusers.database.UsersDataSource;
import com.example.pavel.githubusers.loaders.ItemsAsyncLoader;
import com.example.pavel.githubusers.loaders.UserFromDBLoader;
import com.example.pavel.githubusers.loaders.UserLoader;
import com.example.pavel.githubusers.model.User;
import com.example.pavel.githubusers.ui.adapters.ListAdapter;
import com.example.pavel.githubusers.utils.NetworkUtil;
import com.example.pavel.githubusers.utils.ToastUtil;

import java.util.List;


public class UserListFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<User>>,
        RecyclerScrollListener.ScrollViewListener {

    public static final String TAG = UserListFragment.class.getSimpleName();
    private static final int LOADER_ID = 0;
    private ProgressBar mProgressBar;
    private RecyclerScrollListener mScrollListener;
    private ScrollView mEmptyScrollView;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ListAdapter mListAdapter;
    private int mLastUserId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView:");
        View view = inflater.inflate(R.layout.fragment_user_list, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        mProgressBar = (ProgressBar) view.findViewById(R.id.recycler_view_progress_bar);
        mProgressBar.getIndeterminateDrawable().setColorFilter(Color.BLUE, PorterDuff.Mode.MULTIPLY);
        mEmptyScrollView = (ScrollView) view.findViewById(R.id.empty_list_text);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        layoutManager.setSmoothScrollbarEnabled(true);
        mRecyclerView.setLayoutManager(layoutManager);
        mScrollListener = new RecyclerScrollListener(this);
        mRecyclerView.addOnScrollListener(mScrollListener);
        if (mListAdapter == null) {
            mListAdapter = new ListAdapter();
            refresh(null, false);
        } else if (mListAdapter.getItemCount() == 0) {
            refresh(null, false);
        }

        mRecyclerView.setAdapter(mListAdapter);
        mListAdapter.setOnItemClickListener(new ListAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(View view) {
                if (isResumed()) {
                    User user = mListAdapter.getItem(mRecyclerView.getChildAdapterPosition(view));
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    UserDetailsFragment detailsFragment = UserDetailsFragment.getInstance(user);
                    fragmentManager.beginTransaction().replace(R.id.base_content_frame,
                            detailsFragment, UserDetailsFragment.TAG).addToBackStack(UserDetailsFragment.TAG).commit();
                }
            }
        });

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.d(TAG, "Refresh");
                refresh(null, true);
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
        return view;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        Log.d(TAG, "onActivityCreated:");
        super.onActivityCreated(savedInstanceState);
        getActivity().setTitle(R.string.user_list_title);
        ((MainActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }

    @Override
    public void onDestroyView() {
        Log.d(TAG, "onDestroyView:");
        mRecyclerView.setAdapter(null);
        mRecyclerView.setLayoutManager(null);
        mRecyclerView.addOnScrollListener(null);
        super.onDestroyView();
    }

    @Override
    public Loader<List<User>> onCreateLoader(int id, Bundle args) {
        Log.d(TAG, "onCreateLoader:");
        showProgress(true);
        AsyncTaskLoader loader;

        if (NetworkUtil.haveNetworkConnection()) {
            loader = new UserLoader(getContext(), args);
        } else {
            ToastUtil.showToast(R.string.get_data_network_error);
            if (mListAdapter.getItemCount() == 0) {
                loader = new UserFromDBLoader(getContext());
            } else {
                loader = new ItemsAsyncLoader(getContext());
            }
        }
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<List<User>> loader, List<User> userList) {
        Log.d(TAG, "onLoadFinished:");
        if (userList != null && !userList.isEmpty()) {
            mListAdapter.addList(userList);
            mLastUserId = userList.get(userList.size() - 1).getId();
        }
        showProgress(false);
        getLoaderManager().destroyLoader(loader.getId());
    }

    @Override
    public void onLoaderReset(Loader<List<User>> loader) {
        Log.d(TAG, "onLoaderReset:");
    }

    protected void showProgress(boolean isInProgress) {
        if (isInProgress) {
            mEmptyScrollView.setVisibility(View.GONE);
            mProgressBar.setVisibility(View.VISIBLE);
        } else {
            mProgressBar.setVisibility(View.GONE);
            mEmptyScrollView.setVisibility(mListAdapter.getItemCount() > 0 ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public void onLoadMore() {
        Log.d(TAG, "onLoadMore:");
        Bundle bundle = new Bundle();
        bundle.putInt(UserLoader.LAST_USER_ID_PARAM, mLastUserId);
        refresh(bundle, false);
    }

    private void refresh(Bundle args, boolean clearData) {
        Log.d(TAG, "refresh: args=" + args + ", clearData=" + clearData);
        if (clearData) {
            mListAdapter.clearData();
            mScrollListener.init();
            UsersDataSource.getInstance().clear();
        }
        getLoaderManager().restartLoader(LOADER_ID, args, this);
    }
}
