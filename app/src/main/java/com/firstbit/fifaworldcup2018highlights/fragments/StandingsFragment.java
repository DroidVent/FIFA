package com.firstbit.fifaworldcup2018highlights.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firstbit.fifaworldcup2018highlights.R;
import com.firstbit.fifaworldcup2018highlights.data.Standing;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class StandingsFragment extends Fragment{

    private View rootView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.standing_layout, container, false);
//        init();

        return rootView;
    }

    private void init() {
         DatabaseReference mDatabase;
        mDatabase = FirebaseDatabase.getInstance().getReference();
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
                        Log.e("","success");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("","fail");
                    }
                });
    }
}
