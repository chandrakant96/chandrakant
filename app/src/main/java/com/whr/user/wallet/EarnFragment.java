package com.whr.user.wallet;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.whr.user.R;
import com.whr.user.activities.NoDataAvailableActivity;
import com.whr.user.pojo.GlobalVar;

import java.util.ArrayList;
import java.util.List;

public class EarnFragment extends Fragment {

    private WallatAdapter adapter;
    private Context context;
    private RecyclerView recyclerView;
    private LinearLayoutManager lLayout;

    private List<WallatPojo> rowListItem = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            rowListItem = bundle.getParcelableArrayList("list");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.ahorizontal, null, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        context = getActivity();
        intit(v);
        return v;
    }

    private void intit(View v) {
        recyclerView = v.findViewById(R.id.inner_recyclerView);
        lLayout = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(lLayout);

        if (rowListItem.size() > 0) {
            adapter = new WallatAdapter(context, rowListItem, 1);
            recyclerView.setAdapter(adapter);
        } else {
            startActivity(new Intent(context,NoDataAvailableActivity.class));
        }
    }
}