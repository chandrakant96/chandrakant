package com.whr.user.com.WHR.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.whr.user.R;
import com.whr.user.com.SuggesatNewActivityPackage.PathalogyListFragmentActivity;
import com.whr.user.pojo.DoctorPathalogyListPojo;
import com.whr.user.pojo.FamilyMemberListpojo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 1/30/2017.
 */

public class DoctorPathalogyListFromTidAdapter extends RecyclerView.Adapter<DoctorPathalogyListFromTidAdapter.PathalogyListFromTreatmentViewHolder> {

    private Context context;
    private List<DoctorPathalogyListPojo> list;
    private List<Integer> totalChargeList;
    private LayoutInflater inflater;
    private String value, tratmentIdListstr;
    private String suggestValue;
    private long familyid;
    private int selected_doctorId = 0;
    private String clinicArray[];
    ArrayList<FamilyMemberListpojo> familyMemberList;

    public DoctorPathalogyListFromTidAdapter(Context context, List<DoctorPathalogyListPojo> list, String tratmentIdListstr
            , String suggestValue, long familyid, int doctorId, ArrayList<FamilyMemberListpojo> familyMemberList
    ) {
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);
        this.value = value;
        this.familyid = familyid;
        this.tratmentIdListstr = tratmentIdListstr;
        totalChargeList = new ArrayList<>();
        this.suggestValue = suggestValue;
        this.selected_doctorId = doctorId;
        this.familyMemberList = familyMemberList;
    }

    @Override
    public PathalogyListFromTreatmentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.pathalogy_list_from_treatment_id_adapter_row, parent, false);
        PathalogyListFromTreatmentViewHolder holder = new PathalogyListFromTreatmentViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final PathalogyListFromTreatmentViewHolder holder, final int position) {
        holder.pathalogyDocName.setText(list.get(position).getDocPathName());
        holder.pathlogyDocAddress.setText(list.get(position).getDocPathAddress());

        Picasso.with(context).load(list.get(holder.getLayoutPosition()).getDocPathProfileImage()).
                error(R.drawable.ctg_pathology).into(holder.imgProfile);


        holder.bookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(context, PathalogyListFragmentActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("tratmentIdListstr", tratmentIdListstr);
                bundle.putString("suggest", suggestValue);
                bundle.putString("doctorName", list.get(position).getDocPathName());
                bundle.putInt("doctorId", list.get(position).getDocPathId());
                bundle.putLong("familyid", familyid);
                bundle.putString("profileImage", list.get(holder.getLayoutPosition()).getDocPathAddress());
                bundle.putInt("selected_doctorId", selected_doctorId);
                mIntent.putParcelableArrayListExtra("familyMemberList", familyMemberList);
                mIntent.putExtras(bundle);
                mIntent.setFlags(Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY);
                context.startActivity(mIntent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class PathalogyListFromTreatmentViewHolder extends RecyclerView.ViewHolder {
        TextView pathalogyDocName, pathlogyDocAddress;
        ImageView imgProfile;
        Button bookButton;
        private ImageView imasecond;
        private ImageView imagethird;
        ImageButton show, hide;
        ImageView category_Image, imafirst, sahrebutton, likeButton, addToFavourite;
        RelativeLayout imageViewLayout, thirdImageViewLayout;

        public PathalogyListFromTreatmentViewHolder(View itemView) {
            super(itemView);
            pathalogyDocName = (TextView) itemView.findViewById(R.id.pathlogyDocName);
            pathlogyDocAddress = (TextView) itemView.findViewById(R.id.pathlogyDocAddress);
            bookButton = (Button) itemView.findViewById(R.id.bookButton);
            imgProfile = (ImageView) itemView.findViewById(R.id.pathologycategory_image);
            imafirst = (ImageView) itemView.findViewById(R.id.imafirst);
            imasecond = (ImageView) itemView.findViewById(R.id.imasecond);
            imagethird = (ImageView) itemView.findViewById(R.id.imagethird);
            imageViewLayout = (RelativeLayout) itemView.findViewById(R.id.imageViewLayout);
            thirdImageViewLayout = (RelativeLayout) itemView.findViewById(R.id.thirdImageViewLayout);
            show = itemView.findViewById(R.id.show);
            hide = itemView.findViewById(R.id.hide);
        }
    }
}





