package com.whr.user.booking.diagnostics.booking.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.balysv.materialripple.MaterialRippleLayout;
import com.squareup.picasso.Picasso;
import com.whr.user.R;
import com.whr.user.booking.diagnostics.booking.model.PathologyListPojo;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class PathologyListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = PathologyListAdapter.class.getSimpleName();
    private Context context;
    private ArrayList<Object> items;
    private final int LOADING = 1;
    private RecyclerView.RecycledViewPool viewPool;
    private static final int ITEM = 0;
    DecimalFormat d = new DecimalFormat("##.##");

    public PathologyListAdapter(Context context, ArrayList<Object> items) {
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
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case ITEM:
                viewHolder = getViewHolder(parent, inflater);
                break;
        }
        return viewHolder;
    }

    @NonNull
    private RecyclerView.ViewHolder getViewHolder(ViewGroup parent, LayoutInflater inflater) {
        RecyclerView.ViewHolder viewHolder;
        View v1 = inflater.inflate(R.layout.recycler_item_pathology, parent, false);
        viewHolder = new PathologyViewHolder(v1);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        Object o = items.get(position);
        switch (getItemViewType(position)) {
            case ITEM:
                PathologyViewHolder pathologyViewHolder = (PathologyViewHolder) holder;
                PathologyListPojo pojo = (PathologyListPojo) o;

                if (pojo.getProfile_img() != null && !pojo.getProfile_img().isEmpty()) {
                    Picasso.with(context).load(pojo.getProfile_img())
                            .placeholder(R.drawable.ic_microscope).error(R.drawable.ic_microscope)
                            .fit().into(pathologyViewHolder.imagePathology);
                } else
                    pathologyViewHolder.imagePathology.setImageResource(R.drawable.ic_microscope);

                pathologyViewHolder.txtPathologyName.setText(pojo.getPathologyName());


                if ((pojo.getWorkingfrom()) == null) {
                    pathologyViewHolder.txtWorkingFrom.setVisibility(View.GONE);
                    pathologyViewHolder.txtWorkingHours.setVisibility(View.GONE);
                } else {
                    pathologyViewHolder.txtWorkingFrom.setText(pojo.getWorkingfrom() + " - " + pojo.getWorkingto());
                }
                pathologyViewHolder.txtAddress.setText(pojo.getAddress());
                pathologyViewHolder.txtlike.setText(pojo.getLikes() + " Likes");
                // pathologyViewHolder.txtreview.setText(pojo.getReviews() + " Reviews");
                String dis = String.valueOf(pojo.getDistance());
                pathologyViewHolder.txtdistance.setText(d.format(Double.parseDouble(dis)) + " Km");
                pathologyViewHolder.txtType.setText(String.valueOf(pojo.getType()));

                if ((pojo.getAvailabletestName() == null) || (pojo.getAvailabletestName().equals("NOTTEST"))) {
                    pathologyViewHolder.layoutAvailbale.setVisibility(View.GONE);

                } else {
                    pathologyViewHolder.layoutAvailbale.setVisibility(View.VISIBLE);
                    pathologyViewHolder.txtAvailableTest.setText(String.valueOf(pojo.getAvailabletestName()));
                }


                ArrayList<PathologyListPojo.testname> arrayList = pojo.getTestnames();
                if (arrayList != null && !arrayList.isEmpty()) {
                    pathologyViewHolder.txtTest1.setText(pojo.getTestnames().get(0).getTestname());
                    pathologyViewHolder.txtTest2.setText(pojo.getTestnames().get(1).getTestname());
                    pathologyViewHolder.txtTest3.setText(pojo.getTestnames().get(2).getTestname());
                    pathologyViewHolder.txtTestCount.setText("+" + String.valueOf(pojo.getRemainingcount()) + " more");
                } else {
                    pathologyViewHolder.lineTest.setVisibility(View.GONE);
                }

                if (pojo.isFavoriteStatus()) {
                    pathologyViewHolder.imgStar.setImageResource(R.drawable.ic_star_liked_state);
                } else {
                    pathologyViewHolder.imgStar.setImageResource(R.drawable.ic_star_unliked_state);
                }

                if (pojo.isLikeStatus()) {
                    pathologyViewHolder.txtlike.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_like_liked_state, 0, 0);
                } else {
                    pathologyViewHolder.txtlike.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_like_unliked_state, 0, 0);
                }

                pathologyViewHolder.txtBook.setOnClickListener(view -> this.clickLister.PathologyBookClick((PathologyListPojo) items.get(position)));

                pathologyViewHolder.txtAddReview.setOnClickListener(view -> this.clickLister.PathologyrReviewClick((PathologyListPojo) items.get(position)));

                pathologyViewHolder.txtlike.setOnClickListener(view -> this.clickLister.PathologyLikeClick((PathologyListPojo) items.get(position)));

                pathologyViewHolder.imgStar.setOnClickListener(view -> this.clickLister.PathologyFaveClick((PathologyListPojo) items.get(position)));

                pathologyViewHolder.txtreview.setOnClickListener(view -> this.clickLister.PathologyShareClick((PathologyListPojo) items.get(position)));

                pathologyViewHolder.lineMain.setOnClickListener(view -> this.clickLister.PathologyBookClick((PathologyListPojo) items.get(position)));

                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        return (position == items.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
    }


    public class PathologyViewHolder extends RecyclerView.ViewHolder {

        ImageView imagePathology;
        TextView txtBook, txtPathologyName, txtAddress, txtlike, txtreview, txtdistance, txtWorkingFrom, txtWorkingHours, txtType, txtAvailableTest;
        CardView relativeDetails;
        LinearLayout layoutAvailbale, lineMain;
        TextView txtTest1, txtTest3, txtTest2, txtTestCount, txtAddReview;
        LinearLayout lineTest;
        ImageView imgStar;

        PathologyViewHolder(View itemView) {
            super(itemView);

            lineMain = itemView.findViewById(R.id.lineMain);
            imagePathology = itemView.findViewById(R.id.imageDoctor);
            txtPathologyName = itemView.findViewById(R.id.txtHospitalName);
            txtWorkingFrom = itemView.findViewById(R.id.txtworkingFrom);
            txtWorkingHours = itemView.findViewById(R.id.txtWorkingHours);
            txtAddress = itemView.findViewById(R.id.txtAddress);
            txtlike = itemView.findViewById(R.id.txtLikes);
            txtreview = itemView.findViewById(R.id.txtReviews);
            txtdistance = itemView.findViewById(R.id.txtDistance);
            txtType = itemView.findViewById(R.id.txtType);
            txtBook = itemView.findViewById(R.id.bookAppoentmentbtn);
            relativeDetails = itemView.findViewById(R.id.relativeDetails);
            txtAvailableTest = itemView.findViewById(R.id.txtAvailableTest);
            layoutAvailbale = itemView.findViewById(R.id.layoutAvailbale);

            txtTest1 = itemView.findViewById(R.id.txtTest1);
            txtTest3 = itemView.findViewById(R.id.txtTest3);
            txtTest2 = itemView.findViewById(R.id.txtTest2);
            txtTestCount = itemView.findViewById(R.id.txtTestCount);
            txtAddReview = itemView.findViewById(R.id.txtAddReview);
            lineTest = itemView.findViewById(R.id.lineTest);
            imgStar = itemView.findViewById(R.id.imgStar);
        }
    }


    private ClickLister clickLister;

    public void setClickLister(ClickLister clickLister) {
        this.clickLister = clickLister;
    }

    private boolean isLoadingAdded = false;

    public interface ClickLister {
        void PathologyBookClick(PathologyListPojo pathologyListPojo);

        void PathologyrReviewClick(PathologyListPojo pathologyListPojo);

        void PathologyLikeClick(PathologyListPojo pathologyListPojo);

        void PathologyFaveClick(PathologyListPojo pathologyListPojo);

        void PathologyShareClick(PathologyListPojo pathologyListPojo);
    }
}