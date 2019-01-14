package com.firstbit.fifaworldcup2018highlights.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import com.firstbit.fifaworldcup2018highlights.R;
import com.firstbit.fifaworldcup2018highlights.adapters.MultiTypeDemoAdapter;
import com.firstbit.fifaworldcup2018highlights.data.Group;
import com.firstbit.fifaworldcup2018highlights.data.Standing;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.zakariya.stickyheaders.StickyHeaderLayoutManager;

import java.util.ArrayList;
import java.util.HashMap;

public class StandingsFragment extends Fragment {

    private View rootView;
    private ArrayList<String> leaguesList = new ArrayList<>();
    private static String TAG = StandingsFragment.class.getCanonicalName();
    private Spinner spinner;
    ArrayAdapter<String> leaguesAdapter;
    private ArrayList<Group> groupsList  = new ArrayList<>();
    private RecyclerView rvStandings;
    MultiTypeDemoAdapter multiTypeDemoAdapter;
    private RelativeLayout rlLabel;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.standing_layout, container, false);

        getLeagues();
        initSpinner();

        return rootView;
    }

    private void initStandingAdapter() {

        rvStandings = rootView.findViewById(R.id.rv_standings);

        rvStandings.setLayoutManager(new StickyHeaderLayoutManager());
        rvStandings.setHasFixedSize(false);
        multiTypeDemoAdapter = new MultiTypeDemoAdapter(groupsList);
        rvStandings.setAdapter(multiTypeDemoAdapter);

    }

    private void initSpinner() {
        rlLabel = rootView.findViewById(R.id.rl_label);
        spinner = rootView.findViewById(R.id.table_spinner);
        leaguesAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_item, leaguesList);
        leaguesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(leaguesAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
                String selectedTxt = leaguesList.get(pos);
                rlLabel.setVisibility(View.VISIBLE);
                getLeagueStanding(selectedTxt);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void getLeagueStanding(String selectedTxt) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        Query myRef;
        groupsList.clear();
        myRef = database.getReference("league_standings/"+selectedTxt);
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                    Group group = new Group();
                    HashMap<String, ArrayList<Standing>>grp= (HashMap)singleSnapshot.getValue();

                    String grpName = String.valueOf(grp.get("group"));
                    group.setGroup(grpName);
                    group.setStandings(grp.get("standings"));
                    groupsList.add(group);
                }
                initStandingAdapter();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("", "onCancelled", databaseError.toException());
            }
        });
    }

    private void getLeagues() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        Query myRef;
        myRef = database.getReference("league_standings");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                    leaguesList.add(String.valueOf(singleSnapshot.getKey()));

//                    HashMap<String, ArrayList<Group>>grp= (HashMap)singleSnapshot.getValue();
                    Log.e(TAG, "");
//                    GenericTypeIndicator<ArrayList<Group>> t = new GenericTypeIndicator<ArrayList<Group>>() {};
//                    ArrayList<Group> yourStringArray = singleSnapshot.getValue(t);
//                    groups.add(yourStringArray);
                }
                leaguesAdapter.notifyDataSetChanged();
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
