package com.firstbit.fifaworldcup2018highlights.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.firstbit.fifaworldcup2018highlights.R;
import com.firstbit.fifaworldcup2018highlights.adapters.ScheduleAdapter;
import com.firstbit.fifaworldcup2018highlights.data.Match;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class ScheduleFragment extends Fragment {
    private View rootView;
    private ArrayList<Match> matches = new ArrayList<>();
    ScheduleAdapter scheduleAdapter;
    private RecyclerView rvSchedule;
    private ProgressBar progressBar;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.schedule, container, false);
        init();
        getSchedule();
        return rootView;
    }

    private void init() {
        scheduleAdapter = new ScheduleAdapter(getContext(), matches);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rvSchedule = (RecyclerView)rootView.findViewById(R.id.rv_schedule);
        rvSchedule.setLayoutManager(linearLayoutManager);
        rvSchedule.setAdapter(scheduleAdapter);
        rvSchedule.addItemDecoration(new DividerItemDecoration(rvSchedule.getContext(), DividerItemDecoration.VERTICAL));
        progressBar  = (ProgressBar)rootView.findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.VISIBLE);
    }

    private void getSchedule() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("schedule");

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                    matches.add(singleSnapshot.getValue(Match.class));
                }
                progressBar.setVisibility(View.GONE);
                scheduleAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("", "onCancelled", databaseError.toException());
            }
        });

    }
}
