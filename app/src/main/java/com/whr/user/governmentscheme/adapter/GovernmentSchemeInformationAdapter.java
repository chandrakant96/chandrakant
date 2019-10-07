package com.whr.user.governmentscheme.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.whr.user.R;
import com.whr.user.governmentscheme.SchemeFormOne;
import com.whr.user.governmentscheme.models.GovernmentSchemeInformationPojo;

import java.util.List;

public class GovernmentSchemeInformationAdapter extends RecyclerView.Adapter<GovernmentSchemeInformationAdapter.SchemeInfoHolder> {

    Context ctx;
    LayoutInflater inflater;
    List<GovernmentSchemeInformationPojo> list;

    public GovernmentSchemeInformationAdapter(Context context, List<GovernmentSchemeInformationPojo> list) {
        this.ctx = context;
        this.list = list;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public GovernmentSchemeInformationAdapter.SchemeInfoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.row_government_scheme_information, parent, false);
        GovernmentSchemeInformationAdapter.SchemeInfoHolder holder = new GovernmentSchemeInformationAdapter.SchemeInfoHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final GovernmentSchemeInformationAdapter.SchemeInfoHolder holder, final int position) {
        holder.txtName.setText(list.get(position).getName());
        holder.txtMobile.setText("Contact No : " + list.get(position).getContactNumber());
        holder.txtAddress.setText("Address : " + list.get(position).getAddress());

        holder.txtApply.setOnClickListener(v -> {
            Intent intent = new Intent(ctx, SchemeFormOne.class);
            intent.putExtra("id", list.get(position).getId());
            intent.putExtra("name", list.get(position).getName());
            ctx.startActivity(intent);

        });

        holder.card_view.setOnClickListener(v -> {
            Intent intent = new Intent(ctx, SchemeFormOne.class);
            intent.putExtra("id", list.get(position).getId());
            intent.putExtra("name", list.get(position).getName());
            ctx.startActivity(intent);

        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class SchemeInfoHolder extends RecyclerView.ViewHolder {
        TextView txtName, txtMobile, txtAddress, txtApply;
        CardView card_view;

        public SchemeInfoHolder(View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtName);
            txtMobile = itemView.findViewById(R.id.txtMobile);
            txtAddress = itemView.findViewById(R.id.txtAddress);
            txtApply = itemView.findViewById(R.id.txtApply);
            card_view = itemView.findViewById(R.id.card_view);

        }
    }
}