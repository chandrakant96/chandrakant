package com.whr.user.booking.diagnostics.booking.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.whr.user.R;
import com.whr.user.booking.diagnostics.booking.activities.NewPathalogyActivity;

import java.util.ArrayList;

public class FilterRecyclerViewAdapter extends RecyclerView.Adapter<FilterRecyclerViewAdapter.CommentHolder> {
    private Context context;
    private ArrayList<String> list;
    LayoutInflater inflater;

    public FilterRecyclerViewAdapter(Context context, ArrayList<String> list) {
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);

    }

    @Override
    public CommentHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.recycle_item_filter, parent, false);
        CommentHolder holder = new CommentHolder(view);
        return holder;
    }


    @Override
    public void onBindViewHolder(final CommentHolder holder, final int position) {

        holder.txtFilter.setText(list.get(position));
        holder.layoutClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, list.size());
                if (list.size() == 0) {
                    Intent intent = new Intent(context, NewPathalogyActivity.class);
                    intent.putExtra("status", "");
                    context.startActivity(intent);
                }
            }

        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class CommentHolder extends RecyclerView.ViewHolder {

        TextView txtFilter;
        RelativeLayout layoutClose;


        public CommentHolder(View itemView) {
            super(itemView);

            txtFilter = itemView.findViewById(R.id.txtFilter);
            layoutClose = itemView.findViewById(R.id.layoutClose);
        }


    }

}
