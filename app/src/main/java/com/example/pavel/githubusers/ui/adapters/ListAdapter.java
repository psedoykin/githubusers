package com.example.pavel.githubusers.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pavel.githubusers.App;
import com.example.pavel.githubusers.R;
import com.example.pavel.githubusers.model.User;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(View view);
    }

    private List<User> mList;
    private OnItemClickListener mOnItemClickListener;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new UserViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        UserViewHolder vh = (UserViewHolder) holder;
        Context context = App.getContext();
        User user = mList.get(position);
        vh.mLogin.setText(user.getLogin());
        Picasso.with(context).load(user.getAvatarUrl()).placeholder(R.drawable.ic_sentiment_satisfied_black_24dp)
                .into(vh.mAvatar);
    }

    @Override
    public int getItemCount() {
        return mList != null ? mList.size() : 0;
    }

    public void addList(List<User> list) {
        if (mList == null) {
            mList = new ArrayList<>();
        }
        mList.addAll(list);
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    public void clearData() {
        if (mList != null) {
            mList.clear();
        }
        notifyDataSetChanged();
    }

    public User getItem(int index) {
        return mList != null && index < mList.size() ? mList.get(index) : null;
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {

        private TextView mLogin;
        private ImageView mAvatar;

        private UserViewHolder(final View view) {
            super(view);
            mLogin = (TextView) view.findViewById(R.id.user_login);
            mAvatar = (ImageView) view.findViewById(R.id.user_avatar);
            view.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(view);
                    }
                }
            });
        }
    }
}
