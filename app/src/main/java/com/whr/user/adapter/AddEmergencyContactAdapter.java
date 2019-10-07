package com.whr.user.adapter;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.squareup.picasso.Picasso;
import com.whr.user.com.WHR.Utils.CommanUtils;
import com.whr.user.com.WHR.Utils.CustomJSONObjectRequest;
import com.whr.user.com.WHR.Utils.CustomVolleyRequestQueue;
import com.whr.user.R;
import com.whr.user.com.WHR.Utils.PreferenceUtils;
import com.whr.user.model.EmergencyNumberMode;
import com.whr.user.pojo.GlobalVar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AddEmergencyContactAdapter extends RecyclerView.Adapter<AddEmergencyContactAdapter.MyEmergencyHolder> {

    private final RequestQueue mQueue;
    private final PreferenceUtils pref;
    private Context context;
    private Dialog dialog;
    private LayoutInflater inflater;
    String TAG = getClass().getSimpleName();

    private List<EmergencyNumberMode> list;
    private AppCompatActivity activity;
    private LinearLayout coordinatorLayout;

    public AddEmergencyContactAdapter(Context context, List<EmergencyNumberMode> rowListItem, AppCompatActivity activity, LinearLayout coordinatorLayout) {
        this.context = context;
        this.list = rowListItem;
        this.activity = activity;
        inflater = LayoutInflater.from(context);
        dialog = new Dialog(context);
        pref = new PreferenceUtils(context);
        this.coordinatorLayout = coordinatorLayout;
        mQueue = CustomVolleyRequestQueue.getInstance(context.getApplicationContext()).getRequestQueue();

    }


    @Override
    public MyEmergencyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.add_emergency_contact_row, parent, false);
        MyEmergencyHolder holder = new MyEmergencyHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyEmergencyHolder holder, final int position) {
        holder.EmergencyContactName.setText(list.get(position).getEmergencyContactName());
        String getEmgNumbe = list.get(position).getEmgNumber();
        String str = getEmgNumbe + "";

        String ss = str.replace("+", "");
        // holder.EmergencyContactNumber.setText(list.get(position).getEmgNumber() + "");
        holder.EmergencyContactNumber.setText(ss.replaceAll("\\s", "") + "");
        if (CommanUtils.isEmpty(list.get(position).getRelationShip())) {
            //   holder.relationShiptext.setVisibility(View.GONE);
            holder.relationShiptext.setText("");
        } else {
            holder.relationShiptext.setText(list.get(position).getRelationShip());
        }


        if (CommanUtils.isEmpty(list.get(position).getEmgContactimage())) {

        } else {
            Picasso.with(context).load(list.get(position).getEmgContactimage()).fit().into(holder.emergency_image);
        }


        holder.Call.setOnClickListener(v -> {
            PermissionListener permissionlistener = new PermissionListener() {
                @Override
                public void onPermissionGranted() {
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:+" + list.get(position).getEmgNumber()));
                    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    context.startActivity(callIntent);
                }

                @Override
                public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                    Toast.makeText(context, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
                }
            };
            new TedPermission(context)
                    .setPermissionListener(permissionlistener)
                    .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                    .setPermissions(Manifest.permission.CALL_PHONE)
                    .check();


        });


        holder.delectContact.setOnClickListener(v -> {


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
                deleteEmergencyContact(position);
            });

            noBtn.setOnClickListener(v13 -> {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
            });
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyEmergencyHolder extends RecyclerView.ViewHolder {
        TextView EmergencyContactName, EmergencyContactNumber, relationShiptext;
        ImageView Call;
        ImageView emergency_image, delectContact;


        public MyEmergencyHolder(View itemView) {
            super(itemView);
            EmergencyContactName = itemView.findViewById(R.id.emergencyContact_NametextView);
            EmergencyContactNumber = itemView.findViewById(R.id.concat_Number_textView);
            relationShiptext = itemView.findViewById(R.id.relationShipwthContact);
            Call = itemView.findViewById(R.id.emergencyCallbtn);
            emergency_image = itemView.findViewById(R.id.emergency_image);
            delectContact = itemView.findViewById(R.id.delectContact);
        }
    }


    private void deleteEmergencyContact(final int adapterPosition) {
        GlobalVar.showProgressDialog(context, "Loading", false);
        JSONObject params = new JSONObject();
        try {
            params.put("eid", list.get(adapterPosition).getEid());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String url = GlobalVar.ServerAddress + "AndroidNew/RemoveEmergency";
        final CustomJSONObjectRequest jsonRequest = new CustomJSONObjectRequest(Request.Method.POST,
                url, params, response -> {
            GlobalVar.hideProgressDialog();
            GlobalVar.errorLog(TAG, "deleteEmergencyContact", response.toString());
            try {
                boolean result = response.getBoolean("result");
                if (result) {
                    list.remove(adapterPosition);
                    notifyItemRemoved(adapterPosition);
                    notifyItemRangeChanged(adapterPosition, list.size());
                    GlobalVar.showSnackBarLinearLayout(coordinatorLayout, context.getString(R.string.EmergencyNumberDeleted), context, R.color.green);
                } else {
                    GlobalVar.showSnackBarLinearLayout(coordinatorLayout, context.getString(R.string.PleaseTryAgain), context, R.color.red);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            GlobalVar.hideProgressDialog();
            GlobalVar.showSnackBarLinearLayout(coordinatorLayout, context.getString(R.string.PleaseTryAgain), context, R.color.red);
        });
        jsonRequest.setTag("get");
        mQueue.add(jsonRequest);
    }
}
