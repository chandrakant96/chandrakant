package com.whr.user.com.WHR.adapters;

//

import android.content.Context;
import android.graphics.Paint;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.whr.user.R;
import com.whr.user.pojo.DoctorTreatmentPojo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Etech001 on 11/11/2016.
 */

public class TreatmentChargeCheckBookAdapter extends RecyclerView.Adapter<TreatmentChargeCheckBookAdapter.MyviewHolder> {


    Context context;

    LayoutInflater inflater;

    private List<DoctorTreatmentPojo> list;
    private List<DoctorTreatmentPojo> totalChargeList;
    private AppCompatActivity activity;
    private AlertDialog removeAlert;
    private boolean isChecked;
    public static double totalval = 0.0f;


    public TreatmentChargeCheckBookAdapter(Context context, List<DoctorTreatmentPojo> list, AppCompatActivity activity) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.list = list;
        this.activity = activity;
        totalChargeList = new ArrayList<>();
        totalval = 0.0f;
    }

    @Override
    public MyviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.treatment_charge_check_box_row, parent, false);
        MyviewHolder holder = new MyviewHolder(view);
        return holder;

    }

    @Override
    public void onBindViewHolder(final MyviewHolder holder, final int position) {
        // holder.Treatment_image.setVisibility(View.GONE);
//        totalval=0;
        holder.Tretment_name.setText("");
        holder.fee.setText("");


        holder.Tretment_name.setText(list.get(position).getTreatmentname());

        holder.fee.setText("₹. " + list.get(position).getDiscount_price() + "");
        //  holder.whrdiscount.setText(list.get(position).getWhrdiscount()+"");

        holder.check_box.setChecked(list.get(position).isCheck());
        holder.check_box.setTag(list.get(position));

        holder.actual_price.setText("₹. " + list.get(position).getFee() + "");

        if (list.get(position).getWhrdiscount() == 0) {
            holder.discount.setVisibility(View.GONE);
        } else {
            holder.discount.setText("Discount " + list.get(position).getWhrdiscount() + "%");
        }

        if (list.get(position).getWhrdiscount() > 0) {
            holder.actual_price.setPaintFlags(holder.actual_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }


        if (list.get(position).getWhrdiscount() == 0) {
            holder.actual_price.setVisibility(View.GONE);
        }

        holder.whrdiscount.setVisibility(View.GONE);

        holder.txtDescription.setText(list.get(holder.getLayoutPosition()).getDescription());

        holder.check_box.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!list.get(position).isCheck()) {
                    DoctorTreatmentPojo doctorTreatmentPojo = new DoctorTreatmentPojo();
                    list.get(position).setCheck(true);

                    final AlertDialog.Builder removiewBuilder = new AlertDialog.Builder(activity);
                    LayoutInflater inflater = activity.getLayoutInflater();
                    final View dview = inflater.inflate(R.layout.treatment_description_popup, null);
                    removiewBuilder.setView(dview);

                    TextView expandableTextView;
                    expandableTextView = (TextView) dview.findViewById(R.id.expandable_text);

                    // TextView descriptionTextView  = (TextView) dview .findViewById(R.id.descriptionTextView);
                    ImageView description_close = (ImageView) dview.findViewById(R.id.description_close);
                    expandableTextView.setText(list.get(holder.getLayoutPosition()).getDescription());
                    removeAlert = removiewBuilder.create();

                    if (list.get(holder.getLayoutPosition()).getDescription() != null) {
                        if (list.get(holder.getLayoutPosition()).getDescription().length() > 0) {
                            removeAlert.show();
                        }
                    }
                    description_close.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (removeAlert != null && removeAlert.isShowing()) {
                                removeAlert.dismiss();
                            }
                        }
                    });
                    totalval += Double.parseDouble(list.get(position).getDiscount_price() + " ");
                    Toast.makeText(context, "Total =" + totalval, Toast.LENGTH_SHORT).show();
                } else {
                    list.get(position).setCheck(false);
                    totalval -= Double.parseDouble(list.get(position).getDiscount_price() + " ");
                    Toast.makeText(context, "Total =" + totalval, Toast.LENGTH_SHORT).show();
                }
                notifyDataSetChanged();
            }
        });


    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public boolean getChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public int getItemCount() {
        return list.size();
    }


    class MyviewHolder extends RecyclerView.ViewHolder {
        ImageView Treatment_image;
        TextView Tretment_name, fee, discount_price, discount, actual_price, whrdiscount, txtDescription;
        CheckBox check_box;

        public MyviewHolder(View itemView) {
            super(itemView);
            Tretment_name = (TextView) itemView.findViewById(R.id.treatment_checkboxbookText);
            fee = (TextView) itemView.findViewById(R.id.textView5);
            //    discount_price = (TextView) itemView.findViewById(R.id.discount_price);
            actual_price = (TextView) itemView.findViewById(R.id.actual_price);
            discount = (TextView) itemView.findViewById(R.id.discount);
            whrdiscount = (TextView) itemView.findViewById(R.id.whrdiscount);
            //   Treatment_image = (ImageView) itemView.findViewById(R.id.treatmentChargeCheckBoxIamgeView);
            check_box = (CheckBox) itemView.findViewById(R.id.checkBox4);
            txtDescription = itemView.findViewById(R.id.txtDescription);

        }


    }


    public List<DoctorTreatmentPojo> getTotalList() {
        return list;

    }


}
