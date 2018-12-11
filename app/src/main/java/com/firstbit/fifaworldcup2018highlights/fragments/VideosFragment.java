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
import com.firstbit.fifaworldcup2018highlights.adapters.VideosAdapter;
import com.firstbit.fifaworldcup2018highlights.data.Match;
import com.firstbit.fifaworldcup2018highlights.data.Video;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
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
    private int mPostsPerPage=10;
    private int mTotalItemCount,mLastVisibleItemPosition;
    private boolean mIsLoading;

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
        rvVideos = (RecyclerView)rootView.findViewById(R.id.rv_videos);
        rvVideos.setLayoutManager(linearLayoutManager);
        rvVideos.setAdapter(videosAdapter);
        progressBar  = (ProgressBar)rootView.findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.VISIBLE);
        rvVideos.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                mTotalItemCount = linearLayoutManager.getItemCount();
                mLastVisibleItemPosition = linearLayoutManager.findLastVisibleItemPosition();

                if (!mIsLoading && mTotalItemCount <= (mLastVisibleItemPosition
                        + mPostsPerPage)) {
                    getVideos();
                    mIsLoading = true;
                }
            }
        });
    }

    private void getVideos() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        Query myRef = database.getReference(videosTag).orderByChild("mtime").limitToFirst(mPostsPerPage);

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                    videos.add(singleSnapshot.getValue(Video.class));
                }
                progressBar.setVisibility(View.GONE);
                videosAdapter.notifyDataSetChanged();
                mIsLoading = false;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("", "onCancelled", databaseError.toException());
                mIsLoading = false;
            }
        });

    }

}

