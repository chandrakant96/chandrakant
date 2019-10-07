package com.whr.user.com.WHR.adapters;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.whr.user.R;
import com.whr.user.pojo.Suggested_TreatementPojo;

import java.util.List;

/**
 * Created by Etech001 on 18/11/2016.
 */

public class Suggested_TreatementAdapter extends RecyclerView.Adapter<Suggested_TreatementAdapter.Suggested_TreatementAdapterViewHolder> {

    private Context context;
    private List<Suggested_TreatementPojo> list;
    private LayoutInflater inflater;
    private AlertDialog removeAlert;

    private AppCompatActivity activity;

    public Suggested_TreatementAdapter(Context context, List<Suggested_TreatementPojo> list, AppCompatActivity activity) {
        this.context = context;
        this.list = list;
        this.activity = activity;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public Suggested_TreatementAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.suggested_treatement_adapter_row, parent, false);
        Suggested_TreatementAdapterViewHolder holder = new Suggested_TreatementAdapterViewHolder(view);
        return holder;

    }

    @Override
    public void onBindViewHolder(final Suggested_TreatementAdapterViewHolder holder, final int position) {

        holder.suggestDate.setText("");
        holder.suggestTreatmenttext.setText("");
        holder.suggestTreatmenttext.setText(list.get(position).getTname());
        holder.suggestDate.setText(list.get(position).getDate());

        if (list.get(position).isChecked()) {
            holder.check_box.setChecked(true);
        } else {
            holder.check_box.setChecked(false);
        }

        holder.check_box.setOnClickListener(v -> {
            if (list.get(position).isChecked()) {
                list.get(position).setChecked(false);
            } else {
                list.get(position).setChecked(true);
                //totalChAargeList.remove(position);
            }
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class Suggested_TreatementAdapterViewHolder extends RecyclerView.ViewHolder {
        TextView suggestDate, suggestTreatmenttext;
        CheckBox check_box;

        public Suggested_TreatementAdapterViewHolder(View itemView) {
            super(itemView);
            suggestDate = itemView.findViewById(R.id.suggestDate);
            suggestTreatmenttext = itemView.findViewById(R.id.suggestTreatmenttext);
            check_box = itemView.findViewById(R.id.checkBox4);

        }
    }


    public List<Suggested_TreatementPojo> getTotaTreatmentlList() {
        return list;
    }
}