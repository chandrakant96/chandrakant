package com.whr.user.adapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.whr.user.com.WHR.Utils.CustomJSONObjectRequest;
import com.whr.user.com.WHR.Utils.CustomVolleyRequestQueue;
import com.whr.user.R;
import com.whr.user.com.WHR.Utils.PreferenceUtils;
import com.whr.user.model.AddFamilyMemberPojo;
import com.whr.user.pojo.GlobalVar;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class AddFamilyAdapter extends RecyclerView.Adapter<AddFamilyAdapter.FamillyMemberHolder> {
    private static final int SELECT_FILE = 100;


    LayoutInflater inflater;

    List<AddFamilyMemberPojo> list;
    Context context;
    AddFamilyAdapter adapter;
    private String userChoosenTask;


    private final RequestQueue mQueue;
    private final PreferenceUtils pref;

    AppCompatActivity activity;
    private LinearLayout coordinatorLayout;
    String TAG = getClass().getSimpleName();

    public AddFamilyAdapter(Context context, List<AddFamilyMemberPojo> rowListItem, Context activity, AppCompatActivity activity1, LinearLayout coordinatorLayout) {
        this.context = context;
        this.list = rowListItem;
        inflater = LayoutInflater.from(context);
        this.context = activity;
        this.activity = activity1;
        this.coordinatorLayout = coordinatorLayout;
        pref = new PreferenceUtils(context);
        mQueue = CustomVolleyRequestQueue.getInstance(context).getRequestQueue();
    }

    @Override
    public FamillyMemberHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.add_familly_adapter_row, parent, false);
        FamillyMemberHolder holder = new FamillyMemberHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(FamillyMemberHolder holder, final int position) {
        holder.name.setText(list.get(position).getFamilyName());
        holder.Relation.setText(list.get(position).getRelation());
        holder.dob.setText(list.get(position).getDob());
        holder.cno.setText(list.get(position).getCno());

        String profile_img = list.get(holder.getLayoutPosition()).getProfile();

        if (profile_img != null) {
            if (profile_img.length() > 0) {
                String str = profile_img.replace("\\", "");
                if (str != null) {
                    Picasso.with(context).load(str).fit().into(holder.imageView);
                }
            }

        }

        holder.remove.setOnClickListener(v -> {

            final Dialog dialog = new Dialog(activity, R.style.full_screen_dialog);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(true);
            dialog.setCanceledOnTouchOutside(true);
            dialog.setContentView(R.layout.famliy_edit_dialog_box);
            Window window = dialog.getWindow();
            assert window != null;
            WindowManager.LayoutParams wlp = window.getAttributes();
            wlp.gravity = Gravity.BOTTOM;
            window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            window.setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            window.setAttributes(wlp);

            Button yesBtn = dialog.findViewById(R.id.yesBtn);
            Button noBtn = dialog.findViewById(R.id.noBtn);
            TextView txtRelation = dialog.findViewById(R.id.Relationtext);
            txtRelation.setText(R.string.Doyouwantoremovefamily);
            ImageView closeBtn = dialog.findViewById(R.id.closeBtn);
            dialog.show();

            closeBtn.setOnClickListener(v1 -> {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
            });

            yesBtn.setOnClickListener(v12 -> {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                deleteFamilyMember(position);
            });
            noBtn.setOnClickListener(v13 -> {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
            });
        });
    }


    public void removeAt(int position) {
        list.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, list.size());
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    class FamillyMemberHolder extends RecyclerView.ViewHolder {
        TextView name, Relation, dob, cno;
        ImageView imageView;
        ImageButton remove;

        public FamillyMemberHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.familymenberNametext);
            Relation = itemView.findViewById(R.id.Relationtext);
            cno = itemView.findViewById(R.id.cno);
            dob = itemView.findViewById(R.id.dob);
            imageView = itemView.findViewById(R.id.familymemberimageview);
            remove = itemView.findViewById(R.id.familyremovie);
        }
    }

    private void deleteFamilyMember(final int adapterPosition) {
        GlobalVar.showProgressDialog(activity, "", false);
        JSONObject params = new JSONObject();
        try {
            params.put("id", list.get(adapterPosition).getFamilyId());
            params.put("uid", pref.getUID());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String url = GlobalVar.ServerAddress + "AndroidNew/deleatFamilyMember";
        final CustomJSONObjectRequest jsonRequest = new CustomJSONObjectRequest(Request.Method.POST, url, params,
                response -> {
                    GlobalVar.hideProgressDialog();
                    Log.e("deleteFamilyMember>", response.toString());
                    try {
                        boolean result = response.getBoolean("result");
                        if (result) {
                            list.remove(adapterPosition);
                            notifyItemRemoved(adapterPosition);
                            notifyItemRangeChanged(adapterPosition, list.size());
                            GlobalVar.showSnackBarLinearLayout(coordinatorLayout, context.getString(R.string.FamilyMemberDeleted), context, R.color.red);
                        } else {
                            GlobalVar.showSnackBarLinearLayout(coordinatorLayout, context.getString(R.string.UnableToDeleateFamilyMemberPleasetryAgain), context, R.color.red);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {
            GlobalVar.hideProgressDialog();
            GlobalVar.showSnackBarLinearLayout(coordinatorLayout, context.getString(R.string.UnableToDeleateFamilyMemberPleasetryAgain), context, R.color.red);
        });
        jsonRequest.setTag("get");
        mQueue.add(jsonRequest);
    }


}

