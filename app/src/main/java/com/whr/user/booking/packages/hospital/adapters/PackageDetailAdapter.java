package com.whr.user.booking.packages.hospital.adapters;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.whr.user.R;
import com.whr.user.booking.packages.hospital.model.SurgeryPackageInHospitals;
import com.whr.user.com.WHR.Utils.CustomSwipViewAdapter;
import com.whr.user.pojo.GlobalVar;

import java.text.DecimalFormat;
import java.util.List;

public class PackageDetailAdapter extends RecyclerView.Adapter<PackageDetailAdapter.PackageHolder> {

    Context ctx;
    LayoutInflater inflater;
    List<SurgeryPackageInHospitals> list;
    List<SurgeryPackageInHospitals> list1;
    private FragmentActivity activity;
    private Context context;

    public PackageDetailAdapter(Context context, List<SurgeryPackageInHospitals> rowListItem, FragmentActivity activity) {
        this.ctx = context;
        this.activity = activity;
        this.list = rowListItem;
        this.list1 = rowListItem;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public PackageHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.row_surgery_package_hospitals, parent, false);
        PackageHolder holder = new PackageHolder(view);
        return holder;
    }

    private String clinicArray[];

    @Override
    public void onBindViewHolder(final PackageHolder holder, final int position) {

        holder.txtHospitalName.setText(list.get(position).getHospitalname());
        holder.txtMinPrice.setText("Rs." + "\t" + GlobalVar.priceWithoutDecimal(Double.parseDouble(list.get(position).getMinfee())));
        //  holder.txtMinPrice.setText("Rs." + "\t" + String.valueOf(new DecimalFormat("#").format(Double.parseDouble(list.get(position).getMinfee()))));
        //+ " - " + "₹." + "\t" + String.valueOf(new DecimalFormat("#").format(Double.parseDouble(list.get(position).getMaxfee()))));
        holder.txtDistance.setText(String.valueOf(new DecimalFormat("##.##").format(Double.parseDouble(list.get(position).getDistance())) + "\t" + "km"));
        holder.txtAddress.setText(list.get(position).getAddress());

        holder.mainLayout.setOnClickListener(view -> PackageDetailAdapter.this.clickLister.SurgeryPackageClick(list.get(position)));

        List<SurgeryPackageInHospitals.Clinicimages> clinicimages = list.get(position).getClinicimages();

        if (clinicimages != null) {

            if (clinicimages.size() >= 3 && !clinicimages.get(0).getImagespath().isEmpty()
                    && !clinicimages.get(1).getImagespath().isEmpty() && !clinicimages.get(2).getImagespath().isEmpty()) {
                holder.imgFirst.setVisibility(View.VISIBLE);
                holder.imgSecond.setVisibility(View.VISIBLE);
                holder.imgThird.setVisibility(View.VISIBLE);
                Picasso.with(context).load(clinicimages.get(0).getImagespath())
                        .placeholder(R.drawable.user_blue).error(R.drawable.user_blue)
                        .fit().into(holder.imgFirst);
                Picasso.with(context).load(clinicimages.get(1).getImagespath())
                        .placeholder(R.drawable.user_blue).error(R.drawable.user_blue)
                        .fit().into(holder.imgSecond);
                Picasso.with(context).load(clinicimages.get(2).getImagespath())
                        .placeholder(R.drawable.user_blue).error(R.drawable.user_blue)
                        .fit().into(holder.imgThird);

            } else if (clinicimages.size() == 2 && !clinicimages.get(0).getImagespath().isEmpty()
                    && !clinicimages.get(1).getImagespath().isEmpty()) {
                holder.imgFirst.setVisibility(View.VISIBLE);
                holder.imgSecond.setVisibility(View.VISIBLE);
                Picasso.with(context).load(clinicimages.get(0).getImagespath())
                        .placeholder(R.drawable.user_blue).error(R.drawable.user_blue)
                        .fit().into(holder.imgFirst);
                Picasso.with(context).load(clinicimages.get(1).getImagespath())
                        .placeholder(R.drawable.user_blue).error(R.drawable.user_blue)
                        .fit().into(holder.imgSecond);
            } else if (clinicimages.size() == 1 && !clinicimages.get(0).getImagespath().isEmpty()) {
                holder.imgFirst.setVisibility(View.VISIBLE);
                Picasso.with(context).load(clinicimages.get(0).getImagespath())
                        .placeholder(R.drawable.user_blue).error(R.drawable.user_blue)
                        .fit().into(holder.imgFirst);
            }

            holder.lineImage.setOnClickListener(v -> {
                clinicArray = new String[clinicimages.size()];
                int counter = 0;
                for (SurgeryPackageInHospitals.Clinicimages clinicImage : clinicimages) {
                    clinicArray[counter] = clinicImage.getImagespath();
                    counter++;
                }
                final Dialog dialog = new Dialog(context, R.style.full_screen_dialog);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(true);
                dialog.setCanceledOnTouchOutside(true);
                dialog.setContentView(R.layout.layout_image_slider);
                Window window = dialog.getWindow();
                assert window != null;
                WindowManager.LayoutParams wlp = window.getAttributes();
                wlp.gravity = Gravity.BOTTOM;
                window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                window.setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                window.setAttributes(wlp);
                dialog.show();

                ViewPager viewPager = dialog.findViewById(R.id.pager);
                CustomSwipViewAdapter adapter = new CustomSwipViewAdapter(context,
                        clinicArray);
                viewPager.setAdapter(adapter);
                TextView txtOk = dialog.findViewById(R.id.txtOk);
                ImageView imgClose = dialog.findViewById(R.id.imgClose);

                txtOk.setOnClickListener(v1 -> {
                    dialog.cancel();
                    dialog.dismiss();
                });

                imgClose.setOnClickListener(v1 -> {
                    dialog.cancel();
                    dialog.dismiss();
                });
            });

        }

        if (clinicimages.size() < 3) {
            holder.txtMore.setVisibility(View.GONE);
        } else {
            holder.txtMore.setVisibility(View.VISIBLE);
        }

        holder.txtDistance.setOnClickListener(v -> {

            String strUri = "http://maps.google.com/maps?q=loc:"
                    + list.get(holder.getLayoutPosition()).getLat() + ","
                    + list.get(holder.getLayoutPosition()).getLog() +
                    " (" + list.get(holder.getLayoutPosition()).getHospitalname() + ")";
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse(strUri));
                intent.setComponent(new ComponentName("com.google.android.apps.maps",
                        "com.google.android.maps.MapsActivity"));
                context.startActivity(intent);
            } catch (ActivityNotFoundException e) {
                try {
                    context.startActivity(new Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("market://details?id=com.google.android.apps.maps")));
                } catch (android.content.ActivityNotFoundException anfe) {
                    context.startActivity(new Intent(
                            Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=com.google.android.apps.maps")));
                }
                e.printStackTrace();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class PackageHolder extends RecyclerView.ViewHolder {
        TextView txtHospitalName, txtDistance, txtAddress, txtMinPrice, txtMore;
        LinearLayout mainLayout;
        ImageView imgFirst, imgSecond, imgThird;
        LinearLayout lineImage;

        public PackageHolder(View itemView) {
            super(itemView);
            txtHospitalName = itemView.findViewById(R.id.txtHospitalName);
            txtAddress = itemView.findViewById(R.id.txtAddress);
            txtDistance = itemView.findViewById(R.id.txtDistance);
            txtMinPrice = itemView.findViewById(R.id.txtMinPrice);
            mainLayout = itemView.findViewById(R.id.mainLayout);
            imgFirst = itemView.findViewById(R.id.imafirst);
            imgSecond = itemView.findViewById(R.id.imasecond);
            imgThird = itemView.findViewById(R.id.imagethird);
            txtMore = itemView.findViewById(R.id.txtMore);
            lineImage = itemView.findViewById(R.id.lineImage);
        }
    }

    private ClickLister clickLister;

    public void setClickLister(ClickLister clickLister) {
        this.clickLister = clickLister;
    }

    public interface ClickLister {
        void SurgeryPackageClick(SurgeryPackageInHospitals s);
    }
}