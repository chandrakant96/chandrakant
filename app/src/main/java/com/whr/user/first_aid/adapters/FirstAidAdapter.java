package com.whr.user.first_aid.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.CardView;
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
import com.squareup.picasso.Picasso;
import com.whr.user.R;
import com.whr.user.first_aid.activities.FirstDetailActivity;
import com.whr.user.first_aid.model.FirstAidPojo;
import com.whr.user.pojo.Suggested_TreatementPojo;


import java.util.ArrayList;
import java.util.List;

public class FirstAidAdapter extends RecyclerView.Adapter<FirstAidAdapter.VideoInfoHolder> {

    Context ctx;
    public static final String DEVELOPER_KEY = "AIzaSyDkNZ3XH9BPlhxxqWT3TX99a9S5oUPOYCY";
    LayoutInflater inflater;
    List<FirstAidPojo> list;
    List<FirstAidPojo> list1;
    private FragmentActivity activity;

    public FirstAidAdapter(Context context, List<FirstAidPojo> rowListItem, FragmentActivity activity) {
        this.ctx = context;
        this.activity = activity;
        this.list = rowListItem;
        this.list1 = rowListItem;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public VideoInfoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.first_aid_row, parent, false);
        VideoInfoHolder holder = new VideoInfoHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final VideoInfoHolder holder, final int position) {

        holder.txtDoctorName.setText(list.get(position).getDoctorName());
        holder.txtSpecilization.setText(list.get(position).getFirstAidType());

        holder.playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (YouTubeApiServiceUtil.isYouTubeApiServiceAvailable(ctx).equals(YouTubeInitializationResult.SUCCESS)) {
                    Intent intent = YouTubeStandalonePlayer.createVideoIntent(activity, DEVELOPER_KEY, list.get(holder.getLayoutPosition()).getVideoPath());
                    activity.startActivity(intent);
                } else {
                    Toast.makeText(ctx, "You tube is not installed", Toast.LENGTH_LONG).show();
                }
            }
        });

        holder.card_view.setOnClickListener(v -> {
            Intent intent = new Intent(ctx, FirstDetailActivity.class);
            intent.putExtra("id", list.get(position).getId());
            intent.putExtra("type", list.get(position).getFirstAidType());
            ctx.startActivity(intent);

        });


        final YouTubeThumbnailLoader.OnThumbnailLoadedListener onThumbnailLoadedListener = new YouTubeThumbnailLoader.OnThumbnailLoadedListener() {
            @Override
            public void onThumbnailError(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader.ErrorReason errorReason) {

            }

            @Override
            public void onThumbnailLoaded(YouTubeThumbnailView youTubeThumbnailView, String s) {
                youTubeThumbnailView.setVisibility(View.VISIBLE);
                holder.relativeLayoutOverYouTubeThumbnailView.setVisibility(View.VISIBLE);
            }
        };

        holder.youTubeThumbnailView.initialize(DEVELOPER_KEY, new YouTubeThumbnailView.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader youTubeThumbnailLoader) {
                youTubeThumbnailLoader.setVideo(list.get(position).getVideoPath());
                youTubeThumbnailLoader.setOnThumbnailLoadedListener(onThumbnailLoadedListener);
            }

            @Override
            public void onInitializationFailure(YouTubeThumbnailView youTubeThumbnailView, YouTubeInitializationResult youTubeInitializationResult) {
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class VideoInfoHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        RelativeLayout relativeLayoutOverYouTubeThumbnailView;
        YouTubeThumbnailView youTubeThumbnailView;
        ImageView playButton;
        TextView txtDoctorName, txtSpecilization;
        CardView card_view;


        public VideoInfoHolder(View itemView) {
            super(itemView);
            youTubeThumbnailView = itemView.findViewById(R.id.youtube_thumbnail);
            relativeLayoutOverYouTubeThumbnailView = itemView.findViewById(R.id.relativeLayout_over_youtube_thumbnail);
            playButton = (ImageView) itemView.findViewById(R.id.btnYoutube_player);
            playButton.setOnClickListener(this);
            relativeLayoutOverYouTubeThumbnailView = (RelativeLayout) itemView.findViewById(R.id.relativeLayout_over_youtube_thumbnail);
            youTubeThumbnailView = (YouTubeThumbnailView) itemView.findViewById(R.id.youtube_thumbnail);
            txtDoctorName = itemView.findViewById(R.id.txtDoctorName);
            txtSpecilization = itemView.findViewById(R.id.txtSpecilization);
            card_view = itemView.findViewById(R.id.card_view);
        }

        @Override
        public void onClick(View v) {
            if (YouTubeApiServiceUtil.isYouTubeApiServiceAvailable(ctx).equals(YouTubeInitializationResult.SUCCESS)) {
                Intent intent = YouTubeStandalonePlayer.createVideoIntent((Activity) ctx, DEVELOPER_KEY, list.get(getPosition()).getDoctorName());
                ctx.startActivity(intent);
            } else {
                Toast.makeText(ctx, "You tube is not installed", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void getFilterList(String searchKey) {
        list = new ArrayList<>();
        for (FirstAidPojo firstAidPojo : list1) {
            if (firstAidPojo.getFirstAidType().toLowerCase().trim().contains(searchKey) || firstAidPojo.getDoctorName().toLowerCase().trim().contains(searchKey)) {
                list.add(firstAidPojo);
            }
        }
        notifyDataSetChanged();
    }

    private boolean isPackageInstalled(String packagename, PackageManager packageManager) {
        try {
            packageManager.getPackageInfo(packagename, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }
}