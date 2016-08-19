package com.example.pavel.githubusers.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pavel.githubusers.App;
import com.example.pavel.githubusers.MainActivity;
import com.example.pavel.githubusers.R;
import com.example.pavel.githubusers.model.User;
import com.squareup.picasso.Picasso;

public class UserDetailsFragment extends Fragment {

    public static final String TAG = UserDetailsFragment.class.getSimpleName();

    public static final String USER_PARAM = "userParam";

    private TextView mLogin;
    private TextView mUrl;
    private ImageView mAvatar;

    public static UserDetailsFragment getInstance(User user) {
        Log.d(TAG, "getInstance:");
        UserDetailsFragment frg = new UserDetailsFragment();
        Bundle args = new Bundle();
        args.putSerializable(UserDetailsFragment.USER_PARAM, user);
        frg.setArguments(args);
        return frg;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView:");
        View view = inflater.inflate(R.layout.fragment_user_details, container, false);
        mLogin = (TextView) view.findViewById(R.id.user_details_login);
        mAvatar = (ImageView) view.findViewById(R.id.user_details_avatar);
        mUrl = (TextView) view.findViewById(R.id.user_details_html_url);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            Context context = App.getContext();
            User user = (User) bundle.getSerializable(UserDetailsFragment.USER_PARAM);
            if (user != null) {
                mLogin.setText(user.getLogin());
                mUrl.setText(user.getHtmlUrl());
                Picasso.with(context).load(user.getAvatarUrl())
                        .placeholder(R.drawable.ic_sentiment_satisfied_black_24dp).into(mAvatar);
            }
        }
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Log.d(TAG, "onActivityCreated:");
        super.onActivityCreated(savedInstanceState);
        getActivity().setTitle(R.string.user_details_title);
        ((MainActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
