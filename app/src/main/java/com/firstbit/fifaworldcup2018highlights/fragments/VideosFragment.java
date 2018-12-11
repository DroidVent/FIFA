package com.firstbit.fifaworldcup2018highlights.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.firstbit.fifaworldcup2018highlights.R;
import com.firstbit.fifaworldcup2018highlights.adapters.VideosAdapter;
import com.firstbit.fifaworldcup2018highlights.data.Video;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class VideosFragment extends Fragment {
    private View rootView;
    private ArrayList<Video> videos = new ArrayList<>();
    VideosAdapter videosAdapter;
    private RecyclerView rvVideos;
    private ProgressBar progressBar;
    private String videosTag;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.videos_layout, container, false);
        videosTag = getArguments().getString("tag");
        init();
        getVideos();
        return rootView;
    }

    private void init() {
        videosAdapter = new VideosAdapter(getContext(), videos);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rvVideos = (RecyclerView) rootView.findViewById(R.id.rv_videos);
        rvVideos.setLayoutManager(linearLayoutManager);
        rvVideos.setAdapter(videosAdapter);
        progressBar = (ProgressBar) rootView.findViewById(R.id.progress_bar);

    }

    private void getVideos() {
        progressBar.setVisibility(View.VISIBLE);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        Query myRef = database.getReference(videosTag).orderByChild("mtime");

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                    videos.add(singleSnapshot.getValue(Video.class));
                }
                progressBar.setVisibility(View.GONE);
                videosAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("", "onCancelled", databaseError.toException());
            }
        });

    }

}

