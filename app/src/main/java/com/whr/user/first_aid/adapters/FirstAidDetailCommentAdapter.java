package com.whr.user.first_aid.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeApiServiceUtil;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeStandalonePlayer;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;
import com.whr.user.R;
import com.whr.user.first_aid.model.FirstAidDetailPojo;

import java.util.ArrayList;
import java.util.List;

public class FirstAidDetailCommentAdapter extends RecyclerView.Adapter<FirstAidDetailCommentAdapter.CommentHolder> {

    Context ctx;
    LayoutInflater inflater;
    List<FirstAidDetailPojo> list;
    private FragmentActivity activity;

    public FirstAidDetailCommentAdapter(Context context, List<FirstAidDetailPojo> rowListItem, FragmentActivity activity) {
        this.ctx = context;
        this.activity = activity;
        this.list = rowListItem;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public CommentHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.first_aid_detail_row, parent, false);
        CommentHolder holder = new CommentHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final CommentHolder holder, final int position) {

        holder.txtUserName.setText(list.get(position).getUsername());
        holder.txtUserReview.setText(" - "+list.get(position).getUserreview());
        holder.txtDate.setText("("+"posted on - "+list.get(position).getReviewdate()+")");

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class CommentHolder extends RecyclerView.ViewHolder{

        TextView txtUserName,txtUserReview,txtDate;


        public CommentHolder(View itemView) {
            super(itemView);

            txtUserName = itemView.findViewById(R.id.txtUserName);
            txtUserReview = itemView.findViewById(R.id.txtReview);
            txtDate = itemView.findViewById(R.id.txtDate);
        }


    }

}
