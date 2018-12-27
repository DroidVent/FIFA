package com.firstbit.fifaworldcup2018highlights;

import android.app.ProgressDialog;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.MediaController;
import android.widget.VideoView;

import com.devbrackets.android.exomedia.listener.OnPreparedListener;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

public class VideoPlayerActivity extends AppCompatActivity implements OnPreparedListener {
    private com.devbrackets.android.exomedia.ui.widget.VideoView myVideoView;
    private int position = 0;
    private ProgressDialog progressDialog;
    private MediaController mediaControls;
    String path;
    private InterstitialAd mInterstitialAd;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_player_layout);
        initAds();
        MobileAds.initialize(this, getString(R.string.full_screen_ad_id));
        //set the media controller buttons
        if (mediaControls == null) {
            mediaControls = new MediaController(this);
        }
        getBundle();
        //initialize the VideoView
        myVideoView = (com.devbrackets.android.exomedia.ui.widget.VideoView) findViewById(R.id.video_view);
        setupVideoView();


    }
    private void setupVideoView() {
        // Make sure to use the correct VideoView import
        myVideoView.setOnPreparedListener(this);

        //For now we just picked an arbitrary item to play
        myVideoView.setVideoURI(Uri.parse(path));
    }
    private void initAds() {
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getString(R.string.full_screen_ad_id));
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        mInterstitialAd.setAdListener(new AdListener() {
            public void onAdLoaded() {

            }
        });
    }
    private void showInterstitial() {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }
    }
    private void getBundle() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null)
            this.path = bundle.getString("path");
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        showInterstitial();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (myVideoView != null)
            myVideoView.stopPlayback();
    }

    @Override
    public void onPrepared() {
        myVideoView.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        myVideoView.release();
    }
}
