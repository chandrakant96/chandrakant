package com.whr.user.governmentscheme.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.balysv.materialripple.MaterialRippleLayout;
import com.whr.user.R;
import com.whr.user.governmentscheme.GovernmentSchemeInformtion;
import com.whr.user.governmentscheme.models.GovernmentSchemePojo;
import com.whr.user.model.PaymentReceiptModel;

import java.util.List;

public class GovermentSchemeAdapter extends RecyclerView.Adapter<GovermentSchemeAdapter.SchemeInfoHolder> {

    Context ctx;
    LayoutInflater inflater;
    List<GovernmentSchemePojo> list;

    public GovermentSchemeAdapter(Context context, List<GovernmentSchemePojo> list) {
        this.ctx = context;
        this.list = list;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public GovermentSchemeAdapter.SchemeInfoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.row_government_scheme, parent, false);
        GovermentSchemeAdapter.SchemeInfoHolder holder = new GovermentSchemeAdapter.SchemeInfoHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final GovermentSchemeAdapter.SchemeInfoHolder holder, final int position) {
        holder.txtSpecialization.setText(list.get(position).getName());

        holder.layoutClick.setOnClickListener(v -> {
            Intent intent = new Intent(ctx, GovernmentSchemeInformtion.class);
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
        TextView txtSpecialization;
        MaterialRippleLayout layoutClick;

        public SchemeInfoHolder(View itemView) {
            super(itemView);
            txtSpecialization = itemView.findViewById(R.id.txtSpecialization);
            layoutClick = itemView.findViewById(R.id.layoutClick);

        }
    }
}