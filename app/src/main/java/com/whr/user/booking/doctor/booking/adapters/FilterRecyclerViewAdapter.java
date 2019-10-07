package com.whr.user.booking.doctor.booking.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.whr.user.R;

import java.util.ArrayList;

public class FilterRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    ArrayList<String> list;

    public FilterRecyclerViewAdapter(Context context, ArrayList<String> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View v2 = inflater.inflate(R.layout.recycle_item_filter, viewGroup, false);
        return new LoadingVH(v2);

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        LoadingVH vh = (LoadingVH) viewHolder;
        vh.txtFilter.setText(list.get(i));
        vh.txtFilter.setOnClickListener(view -> onClickListener.onClick(list.get(vh.getLayoutPosition())));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    protected class LoadingVH extends RecyclerView.ViewHolder {
        TextView txtFilter;

        public LoadingVH(View itemView) {
            super(itemView);
            txtFilter = itemView.findViewById(R.id.txtFilter);
        }
    }

    OnClickListener onClickListener;

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public interface OnClickListener {
        void onClick(String s);
    }
}
