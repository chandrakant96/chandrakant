package com.whr.user.adapter;

import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.squareup.picasso.Picasso;
import com.whr.user.com.WHR.Utils.CustomJSONObjectRequest;
import com.whr.user.com.WHR.Utils.CustomVolleyRequestQueue;
import com.whr.user.R;
import com.whr.user.com.WHR.Utils.PreferenceUtils;
import com.whr.user.com.WHR.Utils.ConnectionDector;
import com.whr.user.com.WHR.com.WHR.Activities.PrescriptionHistoryActivity;
import com.whr.user.pojo.GlobalVar;
import com.whr.user.model.NotificationActivityPojo;
import com.whr.user.activities.SuggestTestTabActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class NotificationActivityAdapter extends RecyclerView.Adapter<NotificationActivityAdapter.NotificationViewHolder> {
    private Context context;
    private LayoutInflater inflater;
    private List<NotificationActivityPojo> list;
    private AppCompatActivity activity;
    private RequestQueue mQueue;
    private ConnectionDector connectionDector;
    private PreferenceUtils pref;
    private String TAG = getClass().getSimpleName();

    public NotificationActivityAdapter(Context context, List<NotificationActivityPojo> list, AppCompatActivity activity) {
        this.context = context;
        this.activity = activity;
        pref = new PreferenceUtils(activity);
        inflater = LayoutInflater.from(context);
        this.list = list;
        mQueue = CustomVolleyRequestQueue.getInstance(context).getRequestQueue();
        connectionDector = new ConnectionDector(activity);
    }

    @Override
    public NotificationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.recycler_item_notification, parent, false);
        return new NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final NotificationViewHolder holder, final int position) {

        if (list.get(holder.getLayoutPosition()).isNoti_readFlag()) {
            holder.mainNotificationLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
        } else {
            holder.mainNotificationLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.notification_background));
        }

        holder.mainNotificationLayout.setOnClickListener(v -> {
            if (connectionDector.isConnectingToInternet()) {
                if (list.get(holder.getLayoutPosition()).isNoti_readFlag()) {
                    MarkNotificationRead(list.get(holder.getLayoutPosition()).getNoti_Id(), holder.mainNotificationLayout, holder.getLayoutPosition());
                    if (list.get(holder.getLayoutPosition()).getType().equals("Appointment")) {

                    } else if (list.get(holder.getLayoutPosition()).getType().equals("Emergency")) {
                        if (list.get(holder.getLayoutPosition()).getUserUrl() != null) {
                            if (list.get(holder.getLayoutPosition()).getUserUrl().length() > 0) {
                                String strUri = list.get(holder.getLayoutPosition()).getUserUrl();
                                String str = strUri.replace("\\", "");
                                try {
                                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(str));
                                    intent.setComponent(new ComponentName("com.google.android.apps.maps",
                                            "com.google.android.maps.MapsActivity"));
                                    activity.startActivity(intent);
                                } catch (ActivityNotFoundException e) {
                                    try {
                                        activity.startActivity(new Intent(Intent.ACTION_VIEW,
                                                Uri.parse("market://details?id=com.google.android.apps.maps")));
                                    } catch (ActivityNotFoundException anfe) {
                                        activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=com.google.android.apps.maps")));
                                    }
                                    e.printStackTrace();
                                }
                            }
                        }
                    } else if (list.get(holder.getLayoutPosition()).getType().equals("Prescription")) {
                        Intent mIntent = new Intent(context.getApplicationContext(), PrescriptionHistoryActivity.class);
                        Bundle mBundle = new Bundle();
                        mBundle.putInt("doctorId", list.get(position).getDoctorId());
                        mBundle.putLong("patientid", Long.parseLong(pref.getUID()));
                        mIntent.putExtras(mBundle);
                        mIntent.setFlags(Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY);
                        context.startActivity(mIntent);

                      /*  Intent mIntent = new Intent(context.getApplicationContext(), PrescriptionListActivity.class);
                        Bundle mBundle = new Bundle();
                        mBundle.putInt("docterIdkey", list.get(position).getDoctorId());
                        mBundle.putLong("familyid",Long.parseLong(pref.getUID()));
                        mIntent.putExtras(mBundle);
                        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                        context.startActivity(mIntent);
                        */
                    } else if (list.get(holder.getLayoutPosition()).getType().equals("Test")) {
                        Intent mIntent = new Intent(context.getApplicationContext(), SuggestTestTabActivity.class);
                        mIntent.setFlags(Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY);
                        context.startActivity(mIntent);
                    } else if (list.get(holder.getLayoutPosition()).getType().equals("Treatment")) {
                        Intent mIntent = new Intent(context.getApplicationContext(), SuggestTestTabActivity.class);
                        mIntent.setFlags(Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY);
                        context.startActivity(mIntent);
                    }
                    //TODO else part of chaking isNoti_readFlag true or false
                } else {
                    if (list.get(holder.getLayoutPosition()).getType().equals("Appointment")) {

                    } else if (list.get(holder.getLayoutPosition()).getType().equals("Emergency")) {
                        if (list.get(holder.getLayoutPosition()).getUserUrl() != null) {
                            if (list.get(holder.getLayoutPosition()).getUserUrl().length() > 0) {
                                String strUri = list.get(holder.getLayoutPosition()).getUserUrl();
                                String str = strUri.replace("\\", "");
                                try {
                                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(str));
                                    intent.setComponent(new ComponentName("com.google.android.apps.maps",
                                            "com.google.android.maps.MapsActivity"));
                                    activity.startActivity(intent);
                                } catch (ActivityNotFoundException e) {

                                    try {
                                        activity.startActivity(new Intent(Intent.ACTION_VIEW,
                                                Uri.parse("market://details?id=com.google.android.apps.maps")));
                                    } catch (ActivityNotFoundException anfe) {
                                        activity.startActivity(new Intent(Intent.ACTION_VIEW,
                                                Uri.parse("http://play.google.com/store/apps/details?id=com.google.android.apps.maps")));
                                    }
                                    e.printStackTrace();
                                }
                            }
                        }
                    } else if (list.get(holder.getLayoutPosition()).getType().equals("Prescription")) {
                        /*Intent mIntent = new Intent(context.getApplicationContext(), PrescriptionListActivity.class);
                        Bundle mBundle = new Bundle();
                        mBundle.putInt("docterIdkey", list.get(position).getDoctorId());
                        mBundle.putLong("familyid",Long.parseLong(pref.getUID()));
                        mIntent.putExtras(mBundle);
                        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                        context.startActivity(mIntent);
                        */
                        Intent mIntent = new Intent(context.getApplicationContext(), PrescriptionHistoryActivity.class);
                        Bundle mBundle = new Bundle();
                        mBundle.putInt("doctorId", list.get(position).getDoctorId());
                        mBundle.putLong("patientid", Long.parseLong(pref.getUID()));
                        mIntent.putExtras(mBundle);
                        mIntent.setFlags(Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY);
                        context.startActivity(mIntent);
                    } else if (list.get(holder.getLayoutPosition()).getType().equals("Test")) {
                        Intent mIntent = new Intent(context.getApplicationContext(), SuggestTestTabActivity.class);
                        mIntent.setFlags(Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY);
                        context.startActivity(mIntent);
                    } else if (list.get(holder.getLayoutPosition()).getType().equals("Treatment")) {
                        Intent mIntent = new Intent(context.getApplicationContext(), SuggestTestTabActivity.class);
                        mIntent.setFlags(Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY);
                        context.startActivity(mIntent);
                    }
                }
            } else {
            }
        });


        String profile_img = list.get(holder.getLayoutPosition()).getImage();

        if (profile_img != null) {
            if (profile_img.length() > 0) {
                String str = profile_img.replace("\\", "");
                if (str != null) {
                    Picasso.with(context)
                            .load(str).placeholder(R.drawable.user_blue).error(R.drawable.user_blue)
                            .into(holder.notificationImageView);
                } else {
                    Picasso.with(context).load(R.drawable.user_blue)
                            .placeholder(R.drawable.user_blue).error(R.drawable.user_blue)
                            .into(holder.notificationImageView);
                }
            } else {
                Picasso.with(context).load(R.drawable.user_blue).placeholder(R.drawable.user_blue)
                        .error(R.drawable.user_blue).into(holder.notificationImageView);
            }
        } else {
            Picasso.with(context).load(R.drawable.user_blue).placeholder(R.drawable.user_blue)
                    .error(R.drawable.user_blue).into(holder.notificationImageView);
        }
        holder.type.setText(list.get(position).getType());
        holder.title.setText(list.get(position).getTitle());
        holder.message.setText(list.get(position).getMessage());
        holder.time.setText(list.get(position).getTime());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class NotificationViewHolder extends RecyclerView.ViewHolder {
        ImageView notificationImageView;
        TextView type, title, message, time;
        LinearLayout mainNotificationLayout;

        public NotificationViewHolder(View itemView) {
            super(itemView);
            notificationImageView =  itemView.findViewById(R.id.notificationImageView);
            type =  itemView.findViewById(R.id.type);
            title =  itemView.findViewById(R.id.title);
            message =  itemView.findViewById(R.id.message);
            time =  itemView.findViewById(R.id.time);
            mainNotificationLayout =  itemView.findViewById(R.id.mainNotificationLayout);
        }
    }

    private void MarkNotificationRead(int noti_id, final LinearLayout mainNotificationLayout, final int layoutPosition) {
        GlobalVar.showProgressDialog(context, "Loading...", false);
        String url = GlobalVar.ServerAddress + "doctor/MarkNotificationRead";
        JSONObject obj = new JSONObject();
        try {
            obj.put("noti_id", noti_id);
            GlobalVar.errorLog(TAG, "url", url);
            GlobalVar.errorLog(TAG, "obj", obj.toString());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        final CustomJSONObjectRequest jsonRequest = new CustomJSONObjectRequest(Request.Method.POST,
                url, obj,
                response -> {
                    GlobalVar.hideProgressDialog();
                    GlobalVar.errorLog(TAG, "MarkNotificationRead :=>", response.toString());
                    try {
                        if (response.getBoolean("result")) {
                            mainNotificationLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.notification_background));
                            list.get(layoutPosition).setNoti_readFlag(false);
                            notifyItemRangeChanged(layoutPosition, list.size());
                        } else {
                            mainNotificationLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> GlobalVar.hideProgressDialog());
        jsonRequest.setTag("get");
        mQueue.add(jsonRequest);
    }
}