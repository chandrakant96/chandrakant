package com.whr.user.com.WHR.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.whr.user.R;
import com.whr.user.pojo.FAQPOJO;

import java.util.List;

/**
 * Created by lenovo on 9/27/2017.
 */

public class NewFAQActivityAdapter extends RecyclerView.Adapter<NewFAQActivityAdapter.FAQClass> {
    private Context context;
    private List<FAQPOJO> list;
    private LayoutInflater inflater;

    public NewFAQActivityAdapter(Context context, List<FAQPOJO> list) {
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public FAQClass onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.faq_adapter_row, parent, false);
        FAQClass holder = new FAQClass(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(FAQClass holder, int position) {
        holder.que.setText(position + 1 + ") " + list.get(position).getQuestion());
        holder.ans.setText("   " + list.get(position).getAnswer());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class FAQClass extends RecyclerView.ViewHolder {
        TextView que, ans;

        public FAQClass(View itemView) {
            super(itemView);
            que = (TextView) itemView.findViewById(R.id.questionText);
            ans = (TextView) itemView.findViewById(R.id.answertext);
        }
    }
}
