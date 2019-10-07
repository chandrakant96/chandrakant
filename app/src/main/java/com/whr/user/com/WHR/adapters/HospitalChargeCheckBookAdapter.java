package com.whr.user.com.WHR.adapters;

/**
 * Created by lenovo on 9/6/2017.
 */


import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

//import com.whr.user.NewHospitalPackage.HospitalPackageDescription;
import com.whr.user.R;
import com.whr.user.pojo.DoctorTreatmentPojo;
import com.whr.user.pojo.GlobalVar;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

//import com.github.barteksc.pdfviewer.PDFView;


public class HospitalChargeCheckBookAdapter extends RecyclerView.Adapter<HospitalChargeCheckBookAdapter.MyviewHolder> {


    Context context;

    LayoutInflater inflater;

    private List<DoctorTreatmentPojo> list;
    private List<DoctorTreatmentPojo> mFilterList;


    private List<ArrayList> lists;
    private List<DoctorTreatmentPojo> totalChargeList;
    private AppCompatActivity activity;
    private AlertDialog removeAlert;

    // PDFView pdfView;
    public static String urlpdf;
    public static double packagefee = 0.0f;
    public static double doublefee = 0.0f;
    public static double doubleactuaclprice = 0.0f;


    DecimalFormat df = new DecimalFormat("#.##");

    private boolean isChecked;


    public HospitalChargeCheckBookAdapter(Context context, List<DoctorTreatmentPojo> list, AppCompatActivity activity) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.list = list;
        this.mFilterList = list;
        this.activity = activity;
        totalChargeList = new ArrayList<>();

    }

    @Override
    public MyviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // View view = inflater.inflate(R.layout.test_treat_adapter_row, parent, false);
        View view = inflater.inflate(R.layout.treatment_charge_check_box_row, parent, false);
        // View view = inflater.inflate(R.layout.test_path_adapter_row, parent, false);
        MyviewHolder holder = new MyviewHolder(view);
        return holder;
    }

    String TAG = getClass().getSimpleName();

    @Override
    public void onBindViewHolder(final MyviewHolder holder, final int position) {


        holder.Test_name.setText("");
        holder.fee.setText("");

        holder.Test_name.setText("");
        holder.fee.setText("");

        holder.Test_name.setText(mFilterList.get(position).getTreatmentname());
        // packagefee = Double.parseDouble(list.get(position).getDiscount_price()+"");
        // holder.fee.setText(new DecimalFormat("##.##").format(packagefee));
        doublefee = Double.parseDouble(mFilterList.get(position).getDiscount_price() + "");
        holder.fee.setText("₹. " + df.format(doublefee));
        holder.discount.setText(mFilterList.get(position).getDiscount() + "");

        if (mFilterList.get(position).isCheck()) {
            holder.check_box.setChecked(true);
        } else {
            holder.check_box.setChecked(false);
        }
        holder.check_box.setTag(mFilterList.get(position));

        doubleactuaclprice = Double.parseDouble(mFilterList.get(position).getFee() + "");
        holder.actual_price.setText("₹. " + df.format(doubleactuaclprice));

        if (mFilterList.get(position).getWhrdiscount() == 0) {
            holder.discount.setVisibility(View.GONE);
        } else {
            holder.discount.setText("Discount " + mFilterList.get(position).getWhrdiscount() + "%");
        }

        if (mFilterList.get(position).getWhrdiscount() > 0) {
            holder.actual_price.setPaintFlags(holder.actual_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }

        if (mFilterList.get(position).getWhrdiscount() == 0) {
            holder.actual_price.setVisibility(View.GONE);
        }


        holder.whrdiscount.setVisibility(View.GONE);
        holder.txtDescription.setVisibility(View.GONE);

        holder.check_box.setOnClickListener(v -> {
            if (!mFilterList.get(position).isCheck()) {
                mFilterList.get(position).setCheck(true);
                packagefee += Double.parseDouble(mFilterList.get(position).getDiscount_price() + " ");

                //condition for pdf and latest view packages
                Intent i = null;
                GlobalVar.errorLog(TAG, "DoctorTreatmentPojo", mFilterList.get(position).toString());

                if (!mFilterList.get(position).getDescription().contains("pdf")) {
//                    i = new Intent(context, HospitalPackageDescription.class);
//                    i.putExtra("HospitalPackageDescription", mFilterList.get(position));
//                    context.startActivity(i);
                } else {
                    urlpdf = mFilterList.get(position).getDescription();

                }
            } else {
                mFilterList.get(position).setCheck(false);
                packagefee -= Double.parseDouble(mFilterList.get(position).getDiscount_price() + " ");
            }

            notifyDataSetChanged();
        });





    }


    @Override
    public long getItemId(int position) {
        return position;
    }


    public void getFilter(String query) {
        mFilterList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {

            String text = list.get(i).getTreatmentname().toLowerCase();

            if (text.contains(query)) {
                mFilterList.add(list.get(i));
            }
        }
        notifyDataSetChanged();
    }

    public List<DoctorTreatmentPojo> getSelectedList() {
        mFilterList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {

            boolean text = list.get(i).isCheck();
            if (text) {
                mFilterList.add(list.get(i));
            }
        }
        return mFilterList;
    }


    public boolean getChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    /*
       class RetrivePDf extends AsyncTask<String,Void,InputStream>
       {

           @Override
           protected InputStream doInBackground(String... strings) {
               InputStream inputStream=null;
               try
               {
                   URL url=new URL(strings[0]);
                   HttpURLConnection httpURLConnection=
                           (HttpURLConnection) url.openConnection();
                   if (httpURLConnection.getResponseCode()==200)
                   {
                       inputStream=new
                               BufferedInputStream(httpURLConnection.getInputStream());

                   }
               }catch (IOException e)
               {
                   return null;
               }

               return inputStream;
           }

           @Override
           protected void onPostExecute(InputStream inputStream) {

               pdfView.fromStream(inputStream).load();
               super.onPostExecute(inputStream);
           }
       }
   */
    @Override
    public int getItemCount() {
        return mFilterList.size();
    }

    class MyviewHolder extends RecyclerView.ViewHolder {
        // TextView tName, tfee;
        CheckBox check_box;
        TextView Test_name, fee, discount_price, discount, actual_price, whrdiscount, txtDescription;


        public MyviewHolder(View itemView) {
            super(itemView);

            Test_name = (TextView) itemView.findViewById(R.id.treatment_checkboxbookText);
            fee = (TextView) itemView.findViewById(R.id.textView5);
            actual_price = (TextView) itemView.findViewById(R.id.actual_price);
            discount = (TextView) itemView.findViewById(R.id.discount);
            whrdiscount = (TextView) itemView.findViewById(R.id.whrdiscount);
            check_box = (CheckBox) itemView.findViewById(R.id.checkBox4);
            txtDescription = itemView.findViewById(R.id.txtDescription);


        }

    }


    public List<DoctorTreatmentPojo> getTotalList() {
        return mFilterList;

    }


}




