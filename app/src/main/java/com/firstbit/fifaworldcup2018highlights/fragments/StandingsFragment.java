package com.firstbit.fifaworldcup2018highlights.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.firstbit.fifaworldcup2018highlights.R;
import com.firstbit.fifaworldcup2018highlights.data.Standing;
import com.firstbit.fifaworldcup2018highlights.data.Video;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class StandingsFragment extends Fragment {

    private View rootView;
    private ArrayList<String> leagues = new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.standing_layout, container, false);
        init();
        initSpinner();
        return rootView;
    }

    private void initSpinner() {
        Spinner spinner = rootView.findViewById(R.id.table_spinner);
        ArrayList<String> list =  new ArrayList<>();
        list.add("Mercury");
        list.add("Mercury");
        list.add("Mercury");
        list.add("Mercury");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    private void init() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        Query myRef;
        myRef = database.getReference("league_standings");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                    Object object = singleSnapshot.getValue();
                    Log.e("", "");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("", "onCancelled", databaseError.toException());
            }
        });
    }
     /*   DatabaseReference mDatabase;
        mDatabase = FirebaseDatabase.getInstance().getReference("standings");
        Standing standing = new Standing();
        standing.setD("0");
        standing.setGA("0");
        standing.setGD("0");
        standing.setGF("0");
        standing.setL("0");
        standing.setMP("0");
        standing.setW("0");
        standing.setPts("0");
        mDatabase.child("standings").child("Group H").child("Japan").setValue(standing)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.e("", "success");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("", "fail");
                    }
                });
    }*/
}
