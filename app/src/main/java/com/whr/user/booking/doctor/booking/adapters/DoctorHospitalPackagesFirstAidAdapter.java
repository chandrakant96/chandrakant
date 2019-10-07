package com.whr.user.booking.doctor.booking.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.balysv.materialripple.MaterialRippleLayout;
import com.squareup.picasso.Picasso;
import com.whr.user.R;
import com.whr.user.booking.doctor.booking.models.DoctorModel;
import com.whr.user.booking.doctor.booking.models.FirstAid;
import com.whr.user.booking.doctor.booking.models.HospitalModel;
import com.whr.user.booking.doctor.booking.models.HospitalProfile;
import com.whr.user.booking.doctor.booking.models.SurgeryPackages;
import com.whr.user.pojo.GlobalVar;

import java.util.ArrayList;

public class DoctorHospitalPackagesFirstAidAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = DoctorHospitalPackagesFirstAidAdapter.class.getSimpleName();
    private Context context;
    private ArrayList<Object> items;
    private final int LOADING = 1;
    private final int DOCTOR = 2;
    private final int HOSPITAL = 3;
    private final int FIRST_AID = 4;
    private final int SURGERY_PACKAGES = 5;
    private final int HOSPITAL_DOCTOR = 6;
    private final int HOSPITAL_PACKAGES = 7;
    private RecyclerView.RecycledViewPool viewPool;

    public DoctorHospitalPackagesFirstAidAdapter(Context context, ArrayList<Object> items) {
        this.context = context;
        this.items = items;
        viewPool = new RecyclerView.RecycledViewPool();
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view;
        RecyclerView.ViewHolder holder = null;
        switch (viewType) {
            case DOCTOR:
                view = inflater.inflate(R.layout.recycler_item_doctor, parent, false);
                holder = new DoctorViewHolder(view);
                break;

            case HOSPITAL:
                view = inflater.inflate(R.layout.recycler_item_hospital, parent, false);
                holder = new HospitalViewHolder(view);
                break;

            case FIRST_AID:
                view = inflater.inflate(R.layout.recycler_item_first_aid, parent, false);
                holder = new FirstAidViewHolder(view);
                break;

            case SURGERY_PACKAGES:
                view = inflater.inflate(R.layout.ahorizontal, parent, false);
                holder = new SurgeryPackagesViewHolder(view);
                ((SurgeryPackagesViewHolder) holder).inner_recyclerView.setRecycledViewPool(viewPool);
                break;

            case HOSPITAL_DOCTOR:
                view = inflater.inflate(R.layout.recycler_item_doctor, parent, false);
                holder = new HospitalDoctorViewHolder(view);
                break;

            case HOSPITAL_PACKAGES:
                view = inflater.inflate(R.layout.recycler_item_hospital_package, parent, false);
                holder = new HospitalPackagesViewHolder(view);
                ((HospitalPackagesViewHolder) holder).inner_recyclerView.setRecycledViewPool(viewPool);
                break;

            default:
                view = inflater.inflate(R.layout.dialog_loading, parent, false);
                holder = new LoadingVH(view);
                break;
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        Object o = items.get(position);
        switch (holder.getItemViewType()) {
            case DOCTOR: {
                DoctorModel d = (DoctorModel) o;
                DoctorViewHolder doctorViewHolder = (DoctorViewHolder) holder;
                doctorViewHolder.txtDoctorName.setText(d.getName());
                String imgDoctor = d.getProfile_img().replaceAll(" ", "%20");
                if (imgDoctor != null && !imgDoctor.isEmpty()) {
                    Picasso.with(context).load(imgDoctor)
                            .placeholder(R.drawable.user_blue).error(R.drawable.user_blue)
                            .fit().into(doctorViewHolder.imageDoctor);
                } else
                    doctorViewHolder.imageDoctor.setImageResource(R.drawable.user_blue);

                doctorViewHolder.txtDoctorName.setText(d.getName());
                doctorViewHolder.txtEducation.setText(d.getEducation());
                doctorViewHolder.txtSpecialization.setText(d.getSpecial());
                doctorViewHolder.txtExperience.setText(d.getExperience());

                if (((DoctorModel) o).getClinicname().size() > 0) {
                    doctorViewHolder.txtPersonalHospitalName.setText(d.getClinicname().get(0).getClinicname());
                    if (((DoctorModel) o).getClinicname().size() > 1) {
                        doctorViewHolder.txtPersonalHospitalName.setText(d.getClinicname().get(0).getClinicname() + " & more");
                    }
                }
                doctorViewHolder.txtLikes.setText(String.valueOf(d.getLikes()) + "\nLikes");
                doctorViewHolder.txtReviews.setText(String.valueOf(d.getReviews()) + "\nReviews");
                doctorViewHolder.txtDistance.setText(String.valueOf(d.getDistance()) + "\nKm");

                if (d.isAvailablestatus()) {
                    doctorViewHolder.txtAvailableToday.setText("Slots available for Today");
                    doctorViewHolder.txtAvailableToday.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_appointment_available, 0, 0, 0);
                } else {
                    doctorViewHolder.txtAvailableToday.setText("Slots Not available for Today");
                    doctorViewHolder.txtAvailableToday.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_appointment_not_available, 0, 0, 0);
                }

                doctorViewHolder.txtDiscountedFee.setText("  Rs " + GlobalVar.priceWithoutDecimal(Double.valueOf(d.getWhrdiscountedfee())));
                doctorViewHolder.txtFees.setText(" Rs " + d.getFee());

                doctorViewHolder.relativeDetails.setOnClickListener(view -> this.clickLister.doctorDetailClick((DoctorModel) items.get(position)));

                doctorViewHolder.txtBook.setOnClickListener(view -> this.clickLister.doctorBookClick((DoctorModel) items.get(position)));

                doctorViewHolder.card_view.setOnClickListener(view -> this.clickLister.doctorDetailClick((DoctorModel) items.get(position)));
                break;
            }
            case HOSPITAL: {
                HospitalViewHolder hospitalViewHolder = (HospitalViewHolder) holder;
                hospitalViewHolder.txtHospitalName.setText(((HospitalModel) o).getHospitalName());

                String imgHospital = ((HospitalModel) o).getProfileImg().replaceAll(" ", "%20");
                if (imgHospital != null && !imgHospital.isEmpty()) {
                    Picasso.with(context).load(imgHospital)
                            .placeholder(R.drawable.ic_hospital).error(R.drawable.ic_hospital)
                            .fit().into(hospitalViewHolder.imageHospital);
                } else {
                    Picasso.with(context).load(R.drawable.ic_hospital)
                            .placeholder(R.drawable.ic_hospital).error(R.drawable.ic_hospital)
                            .fit().into(hospitalViewHolder.imageHospital);
                }

                hospitalViewHolder.txtDoctorCounts.setText(String.valueOf(((HospitalModel) o).getAvailabledoctor()));
                hospitalViewHolder.txtLikes.setText(String.valueOf(((HospitalModel) o).getLikes()) + "\n Likes");
                hospitalViewHolder.txtReviews.setText(String.valueOf(((HospitalModel) o).getReviews()) + "\n Reviews");
                hospitalViewHolder.txtDistance.setText(String.valueOf(((HospitalModel) o).getDistance()) + "\n Km");


                if (((HospitalModel) o).getBedcount().equalsIgnoreCase("0")) {
                    hospitalViewHolder.txtBedCount.setVisibility(View.GONE);
                    hospitalViewHolder.txtBedCountTxt.setVisibility(View.GONE);
                } else {
                    hospitalViewHolder.txtBedCount.setText(((HospitalModel) o).getBedcount());
                }

                if (((HospitalModel) o).getAvailablepackege() == 0) {
                    hospitalViewHolder.txtPackagesCount.setVisibility(View.GONE);
                    hospitalViewHolder.txtSurgeryPackage.setVisibility(View.GONE);
                    hospitalViewHolder.materialLayoutPackage.setVisibility(View.GONE);
                    hospitalViewHolder.divider.setVisibility(View.GONE);
                    hospitalViewHolder.txtStartingPrice.setVisibility(View.GONE);
                    hospitalViewHolder.txtDiscount.setVisibility(View.GONE);
                } else {
                    hospitalViewHolder.txtSurgeryPackage.setVisibility(View.VISIBLE);
                    hospitalViewHolder.txtPackagesCount.setVisibility(View.VISIBLE);
                    hospitalViewHolder.txtPackagesCount.setText(((HospitalModel) o).getAvailablepackege() + " packages");
                    if (((HospitalModel) o).getMinpackege() != null && !((HospitalModel) o).getMinpackege().isEmpty()) {
                        hospitalViewHolder.txtPackagesCount.append(", starting from");
                        // GlobalVar.priceWithoutDecimal(Double.valueOf(((HospitalModel) o).getMinpackege())));
                        hospitalViewHolder.txtStartingPrice.setText("Rs. " + GlobalVar.priceWithoutDecimal(Double.valueOf(((HospitalModel) o).getMinpackege())));
                        hospitalViewHolder.txtStartingPrice.setVisibility(View.VISIBLE);
                    } else {
                        hospitalViewHolder.txtStartingPrice.setVisibility(View.GONE);
                    }

                    if (((HospitalModel) o).getPackdiscount() != null && !((HospitalModel) o).getPackdiscount().isEmpty()) {
                        hospitalViewHolder.txtDiscount.setText(((HospitalModel) o).getPackdiscount() + "% Discount");
                        hospitalViewHolder.txtDiscount.setVisibility(View.VISIBLE);
                    } else {
                        hospitalViewHolder.txtDiscount.setVisibility(View.GONE);
                    }
                }


                hospitalViewHolder.card_view.setOnClickListener(view -> this.clickLister.hospitalDetailClick((HospitalModel) items.get(position)));

                hospitalViewHolder.relativeDetails.setOnClickListener(view -> this.clickLister.hospitalDetailClick((HospitalModel) items.get(position)));

                hospitalViewHolder.txtBook.setOnClickListener(view -> this.clickLister.hospitalBookClick((HospitalModel) items.get(position)));

                hospitalViewHolder.txtSurgeryPackage.setOnClickListener(view -> this.clickLister.hospitalSurgeryClick((HospitalModel) items.get(position)));
                break;
            }
            case FIRST_AID: {
                FirstAidViewHolder firstAidViewHolder = (FirstAidViewHolder) holder;
                Picasso.with(context).load(((FirstAid) o).getPath())
                        .placeholder(R.drawable.user_blue).error(R.drawable.user_blue)
                        .fit().into(firstAidViewHolder.imgFirstAid);
                firstAidViewHolder.txtDescription.setText(((FirstAid) o).getFirstaid());
                break;
            }

            case SURGERY_PACKAGES:
                SurgeryPackagesViewHolder surgeryPackagesViewHolder = (SurgeryPackagesViewHolder) holder;
                SurgeryPackagesHorizontalAdapter adapter = new SurgeryPackagesHorizontalAdapter(context, ((SurgeryPackages) o).getSurgeryPackages());
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
                linearLayoutManager.setInitialPrefetchItemCount(3);
                surgeryPackagesViewHolder.inner_recyclerView.setLayoutManager(linearLayoutManager);
                surgeryPackagesViewHolder.inner_recyclerView.setHasFixedSize(true);
                surgeryPackagesViewHolder.inner_recyclerView.setAdapter(adapter);
                break;
            case HOSPITAL_DOCTOR: {
                HospitalDoctorViewHolder doctorViewHolder = (HospitalDoctorViewHolder) holder;
                doctorViewHolder.txtDoctorName.setText(((HospitalProfile.DoctorList) o).getName());
                if (((HospitalProfile.DoctorList) o).getProfileimage() != null &&
                        !((HospitalProfile.DoctorList) o).getProfileimage().isEmpty()) {
                    Picasso.with(context).load(((HospitalProfile.DoctorList) o).getProfileimage())
                            .placeholder(R.drawable.user_blue).error(R.drawable.user_blue)
                            .fit().into(doctorViewHolder.imageDoctor);
                } else
                    doctorViewHolder.imageDoctor.setImageResource(R.drawable.user_blue);

                doctorViewHolder.txtDoctorName.setText(((HospitalProfile.DoctorList) o).getName());
                doctorViewHolder.txtEducation.setText(((HospitalProfile.DoctorList) o).getEducation());
                doctorViewHolder.txtSpecialization.setText(((HospitalProfile.DoctorList) o).getSpecial());
                doctorViewHolder.txtExperience.setText(((HospitalProfile.DoctorList) o).getExperience());
                doctorViewHolder.txtLikes.setText(String.valueOf(((HospitalProfile.DoctorList) o).getLikes()) + "\n Likes");
                //  doctorViewHolder.txtReviews.setText(String.valueOf(((HospitalProfile.DoctorList) o).getReviews()));
                doctorViewHolder.txtReviews.setText(String.valueOf((0)) + "\nReviews");
                doctorViewHolder.txtDistance.setVisibility(View.GONE);

                if (((HospitalProfile.DoctorList) o).isAvailablestatus()) {
                    doctorViewHolder.txtAvailableToday.setText("Slots available for Today");
                } else {
                    doctorViewHolder.txtAvailableToday.setText("Slots Not available for Today");
                }

                doctorViewHolder.txtDiscountedFee.setText("  Rs " + ((HospitalProfile.DoctorList) o).getWhrdiscountedfee());
                if (((HospitalProfile.DoctorList) o).getFee().equals("0")) {
                    doctorViewHolder.txtFees.setVisibility(View.GONE);
                } else {
                    doctorViewHolder.txtFees.setText(" Rs " + ((HospitalProfile.DoctorList) o).getFee());
                }

                //doctorViewHolder.card_view.set
                break;
            }

            case HOSPITAL_PACKAGES:
                HospitalPackagesViewHolder hospitalPackagesViewHolder = (HospitalPackagesViewHolder) holder;
                hospitalPackagesViewHolder.txtPackageName.setText(((HospitalProfile.HospitalPackages) o).getPackagename());
                //surgeryPackagesViewHolder.inner_recyclerView
               /* SurgeryPackagesHorizontalAdapter adapter = new SurgeryPackagesHorizontalAdapter(context, ((SurgeryPackages) o).getSurgeryPackages());
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
                linearLayoutManager.setInitialPrefetchItemCount(3);
                surgeryPackagesViewHolder.inner_recyclerView.setLayoutManager(linearLayoutManager);
                surgeryPackagesViewHolder.inner_recyclerView.setHasFixedSize(true);
                surgeryPackagesViewHolder.inner_recyclerView.setAdapter(adapter);*/
                break;

            case LOADING:
                //  LoadingVH loadingVH = (LoadingVH) holder;
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if ((items.size() == 0 || position == items.size() - 1) && isLoadingAdded) {
            return LOADING;
        } else {
            if (items.get(position) instanceof DoctorModel)
                return DOCTOR;
            if (items.get(position) instanceof HospitalModel)
                return HOSPITAL;
            if (items.get(position) instanceof FirstAid)
                return FIRST_AID;
            if (items.get(position) instanceof SurgeryPackages)
                return SURGERY_PACKAGES;
            if (items.get(position) instanceof HospitalProfile.DoctorList)
                return HOSPITAL_DOCTOR;
            if (items.get(position) instanceof HospitalProfile.HospitalPackages)
                return HOSPITAL_PACKAGES;
        }
        return -1;
    }

    public class DoctorViewHolder extends RecyclerView.ViewHolder {
        CardView card_view;
        ImageView imageDoctor;
        TextView txtDoctorName, txtEducation, txtSpecialization, txtExperience, txtBook,
                txtPersonalHospitalName, txtFees, txtDiscountedFee,
                txtLikes, txtReviews, txtDistance, txtAvailableToday;
        LinearLayout relativeDetails;

        DoctorViewHolder(View itemView) {
            super(itemView);
            card_view = itemView.findViewById(R.id.card_view);
            imageDoctor = itemView.findViewById(R.id.imageDoctor);
            txtBook = itemView.findViewById(R.id.txtBook);
            txtDoctorName = itemView.findViewById(R.id.txtDoctorName);
            txtEducation = itemView.findViewById(R.id.txtEducation);
            txtSpecialization = itemView.findViewById(R.id.txtSpecialization);
            txtExperience = itemView.findViewById(R.id.txtExperience);
            txtPersonalHospitalName = itemView.findViewById(R.id.txtPersonalHospitalName);
            txtFees = itemView.findViewById(R.id.txtFees);
            txtDiscountedFee = itemView.findViewById(R.id.txtDiscountedFee);
            txtLikes = itemView.findViewById(R.id.txtLikes);
            txtReviews = itemView.findViewById(R.id.txtReviews);
            txtDistance = itemView.findViewById(R.id.txtDistance);
            txtAvailableToday = itemView.findViewById(R.id.txtAvailableToday);
            relativeDetails = itemView.findViewById(R.id.relativeDetails);
        }
    }

    public class HospitalViewHolder extends RecyclerView.ViewHolder {
        TextView txtHospitalName, txtStartingPrice, divider;
        ImageView imageHospital;
        CardView card_view;
        LinearLayout relativeDetails;
        TextView txtDoctorCounts, txtPackagesCount, txtLikes, txtReviews, txtDistance, txtBook, txtSurgeryPackage, txtDiscount, txtBedCountTxt, txtBedCount;
        MaterialRippleLayout materialLayoutPackage;

        HospitalViewHolder(View itemView) {
            super(itemView);
            materialLayoutPackage = itemView.findViewById(R.id.materialLayoutPackage);
            txtStartingPrice = itemView.findViewById(R.id.txtStartingPrice);
            txtHospitalName = itemView.findViewById(R.id.txtHospitalName);
            imageHospital = itemView.findViewById(R.id.imageHospital);
            card_view = itemView.findViewById(R.id.card_view);
            relativeDetails = itemView.findViewById(R.id.relativeDetails);
            txtDoctorCounts = itemView.findViewById(R.id.txtDoctorCounts);
            txtPackagesCount = itemView.findViewById(R.id.txtPackagesCount);
            txtLikes = itemView.findViewById(R.id.txtLikes);
            txtReviews = itemView.findViewById(R.id.txtReviews);
            txtDistance = itemView.findViewById(R.id.txtDistance);
            txtBook = itemView.findViewById(R.id.txtBook);
            txtSurgeryPackage = itemView.findViewById(R.id.txtSurgeryPackage);
            txtDiscount = itemView.findViewById(R.id.txtDiscount);
            txtBedCount = itemView.findViewById(R.id.txtBedCount);
            txtBedCountTxt = itemView.findViewById(R.id.txtBedCountTxt);
            divider = itemView.findViewById(R.id.divider);
        }
    }

    public class LoadingVH extends RecyclerView.ViewHolder {
        ProgressBar progressBar;

        LoadingVH(View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.pb_loading);
            progressBar.incrementProgressBy(10);
            progressBar.setIndeterminate(true);
        }
    }

    public class SurgeryPackagesViewHolder extends RecyclerView.ViewHolder {
        RecyclerView inner_recyclerView;

        SurgeryPackagesViewHolder(View itemView) {
            super(itemView);
            inner_recyclerView = itemView.findViewById(R.id.inner_recyclerView);
        }
    }

    public class FirstAidViewHolder extends RecyclerView.ViewHolder {
        ImageView imgFirstAid;
        TextView txtDescription;

        FirstAidViewHolder(View itemView) {
            super(itemView);
            imgFirstAid = itemView.findViewById(R.id.imgFirstAid);
            txtDescription = itemView.findViewById(R.id.txtDescription);
        }
    }

    public class HospitalDoctorViewHolder extends RecyclerView.ViewHolder {
        CardView card_view;
        ImageView imageDoctor;
        TextView txtDoctorName, txtEducation, txtSpecialization, txtExperience, txtBook,
                txtPersonalHospitalName, txtFees, txtDiscountedFee,
                txtLikes, txtReviews, txtDistance, txtAvailableToday;
        LinearLayout relativeDetails;

        HospitalDoctorViewHolder(View itemView) {
            super(itemView);
            card_view = itemView.findViewById(R.id.card_view);
            imageDoctor = itemView.findViewById(R.id.imageDoctor);
            txtBook = itemView.findViewById(R.id.txtBook);
            txtDoctorName = itemView.findViewById(R.id.txtDoctorName);
            txtEducation = itemView.findViewById(R.id.txtEducation);
            txtSpecialization = itemView.findViewById(R.id.txtSpecialization);
            txtExperience = itemView.findViewById(R.id.txtExperience);
            txtPersonalHospitalName = itemView.findViewById(R.id.txtPersonalHospitalName);
            txtFees = itemView.findViewById(R.id.txtFees);
            txtDiscountedFee = itemView.findViewById(R.id.txtDiscountedFee);
            txtLikes = itemView.findViewById(R.id.txtLikes);
            txtReviews = itemView.findViewById(R.id.txtReviews);
            txtDistance = itemView.findViewById(R.id.txtDistance);
            txtAvailableToday = itemView.findViewById(R.id.txtAvailableToday);
            relativeDetails = itemView.findViewById(R.id.relativeDetails);
        }
    }

    private boolean isLoadingAdded = false;

    public void addLoadingFooter() {
        isLoadingAdded = true;
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;
    }

    private ClickLister clickLister;

    public void setClickLister(ClickLister clickLister) {
        this.clickLister = clickLister;
    }

    public interface ClickLister {
        void doctorBookClick(DoctorModel doctorModel);

        void doctorDetailClick(DoctorModel doctorModel);

        void hospitalBookClick(HospitalModel hospitalModel);

        void hospitalDetailClick(HospitalModel hospitalModel);

        void hospitalSurgeryClick(HospitalModel hospitalModel);
    }

    public class HospitalPackagesViewHolder extends RecyclerView.ViewHolder {
        RecyclerView inner_recyclerView;
        TextView txtPackageName;

        HospitalPackagesViewHolder(View itemView) {
            super(itemView);
            inner_recyclerView = itemView.findViewById(R.id.inner_recyclerView);
            txtPackageName = itemView.findViewById(R.id.txtPackageName);
        }
    }
}