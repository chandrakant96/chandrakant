package com.whr.user.booking.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.whr.user.model.CentralSearchModel;
import com.whr.user.R;

import java.util.ArrayList;

public class RecyclerAdapterCentralSearch extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    LayoutInflater inflater;
    private ArrayList<CentralSearchModel> mFilterList;

    String TAG = getClass().getSimpleName();

    public RecyclerAdapterCentralSearch(Context context, ArrayList<CentralSearchModel> list, int SEARCH_TYPE) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        mFilterList = list;
        this.SEARCH_TYPE = SEARCH_TYPE;
    }

    public static final int CENTRAL_SEARCH = 1;
    public static final int RECENT_SEARCH = 2;
    public static final int TOP_SEARCH = 3;
    private final int SEARCH_TYPE;

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view;
        RecyclerView.ViewHolder holder = null;
        switch (viewType) {
            case CENTRAL_SEARCH:
                view = inflater.inflate(R.layout.recycle_item_central_search, parent, false);
                holder = new CentralSearchViewHolder(view);
                break;
            case TOP_SEARCH:
                //recycler_item_doctor_specialities
                view = inflater.inflate(R.layout.recycler_item_doctor_specialities, parent, false);
                holder = new MyViewHolder(view);
                break;
            case RECENT_SEARCH:
                view = inflater.inflate(R.layout.recycle_item_recent_search, parent, false);
                holder = new RecentSearchViewHolder(view);
                break;
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder1, final int position) {
        switch (holder1.getItemViewType()) {

            case CENTRAL_SEARCH: {
                final CentralSearchViewHolder holder = (CentralSearchViewHolder) holder1;
                CentralSearchModel centralSearchModel = mFilterList.get(position);
                String name = centralSearchModel.getName();
                name = name.replace("[", "#");
                name = name.replace("]", "#");
                if (name.contains("#")) {
                    holder.txtCentralSearch.setText(name.split("#")[0]);
                } else {
                    holder.txtCentralSearch.setText(name);
                }

                switch (centralSearchModel.getType()) {
                    case "tbl_doctor": {
                        holder.txtCentralSearch.append(" in Doctor");
                        break;
                    }
                    case "tbl_doctorsSpeciality": {
                        holder.txtCentralSearch.append(" in Speciality");
                        break;
                    }

                    case "tbl_hospital":
                        holder.txtCentralSearch.append(" in Hospital");
                        break;

                    case "tbl_hospitalpackeges": {
                        holder.txtCentralSearch.append(" in Surgery Packages");
                        break;
                    }

                    case "tbl_treatment": {
                        holder.txtCentralSearch.append(" in Treatment");
                        break;
                    }

                    //up to here

                    case "tbl_pathologylab": {
                        holder.txtCentralSearch.setText(name);
                        String pathName = mFilterList.get(position).getTypeId();
                        if (pathName.contains("1")) {
                            holder.txtCentralSearch.append(" in Pathology");
                        } else if (pathName.contains("2")) {
                            holder.txtCentralSearch.append(" in Radiology");
                        }
                        break;
                    }

                    case "tbl_pathologylabTest": {
                        holder.txtCentralSearch.append(" in Test");
                        break;
                    }


                }

                holder.card_view.setOnClickListener(view -> {
                    CentralSearchModel centralSearchModel1 = mFilterList.get(holder.getLayoutPosition());
                    onClickListener.onClick(centralSearchModel1);
                });
                break;
            }

            case TOP_SEARCH: {
                final MyViewHolder holder = (MyViewHolder) holder1;
                CentralSearchModel centralSearchModel = mFilterList.get(position);
                String name = centralSearchModel.getName();
                name = name.replace("[", "#");
                name = name.replace("]", "#");
                if (name.contains("#")) {
                    holder.txtSpecialization.setText(name.split("#")[0]);
                } else {
                    holder.txtSpecialization.setText(name);
                }

                switch (centralSearchModel.getType()) {
                    case "tbl_doctor": {
                        holder.txtIllness.setText("(Doctors)");
                        break;
                    }
                    case "tbl_doctorsSpeciality": {
                        //  holder.txtCentralSearch.append(" in " + centralSearchModel.getType());
                        break;
                    }
                    case "tbl_pathologylab": {
                        String pathName = mFilterList.get(position).getTypeId();
                        if (pathName.contains("1")) {
                            holder.txtIllness.setText("(Pathology)");
                        } else if (pathName.contains("2")) {
                            holder.txtIllness.setText("(Radiology)");
                        }
                        break;
                    }
                    case "tbl_hospital":
                        holder.txtIllness.setText("(Hospital)");
                        break;

                    case "tbl_hospitalsSpeciality":
                        holder.txtIllness.setText("(Multi-speciality)");
                        break;

                    case "tbl_pathologylabTest": {
                        holder.txtIllness.setText("(Test)");
                        break;
                    }

                    case "tbl_hospitalpackeges": {
                        holder.txtIllness.setText("(Surgery)");
                        break;
                    }
                }

                holder.card_view.setOnClickListener(view -> {
                    CentralSearchModel centralSearchModel1 = mFilterList.get(holder.getLayoutPosition());
                    onClickListener.onClick(centralSearchModel1);
                });
                break;
            }

            case RECENT_SEARCH: {
                final RecentSearchViewHolder holder = (RecentSearchViewHolder) holder1;
                CentralSearchModel centralSearchModel = mFilterList.get(position);
                String name = centralSearchModel.getName();
                name = name.replace("[", "#");
                name = name.replace("]", "#");
                if (name.contains("#")) {
                    holder.txtCentralSearch.setText(name.split("#")[0]);
                } else {
                    holder.txtCentralSearch.setText(name);
                }
                holder.txtCentralSearch.setOnClickListener(view -> {
                    CentralSearchModel centralSearchModel1 = mFilterList.get(holder.getLayoutPosition());
                    onClickListener.onClick(centralSearchModel1);
                });
                break;
            }
        }

    }

    public int getItemCount() {
        return mFilterList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (SEARCH_TYPE == RECENT_SEARCH)
            return RECENT_SEARCH;
        else if (SEARCH_TYPE == TOP_SEARCH)
            return TOP_SEARCH;
        else if (SEARCH_TYPE == CENTRAL_SEARCH)
            return CENTRAL_SEARCH;
        return -1;
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtSpecialization, txtIllness;
        RelativeLayout card_view;

        public MyViewHolder(View itemView) {
            super(itemView);
            txtSpecialization = itemView.findViewById(R.id.txtSpecialization);
            txtIllness = itemView.findViewById(R.id.txtIllness);
            card_view = itemView.findViewById(R.id.relativeItem);
        }
    }

    public class RecentSearchViewHolder extends RecyclerView.ViewHolder {
        TextView txtCentralSearch;

        public RecentSearchViewHolder(View itemView) {
            super(itemView);
            txtCentralSearch = itemView.findViewById(R.id.txtCentralSearch);
        }
    }

    public class CentralSearchViewHolder extends RecyclerView.ViewHolder {
        TextView txtCentralSearch;
        RelativeLayout card_view;

        public CentralSearchViewHolder(View itemView) {
            super(itemView);
            txtCentralSearch = itemView.findViewById(R.id.txtCentralSearch);
            card_view = itemView.findViewById(R.id.card_view);
        }
    }

    OnClickListener onClickListener;

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public interface OnClickListener {
        void onClick(CentralSearchModel c);
    }
}
