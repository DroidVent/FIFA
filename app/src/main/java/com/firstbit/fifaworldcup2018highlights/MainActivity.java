package com.firstbit.fifaworldcup2018highlights;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.firstbit.fifaworldcup2018highlights.data.League;
import com.firstbit.fifaworldcup2018highlights.data.Video;
import com.firstbit.fifaworldcup2018highlights.fragments.StandingsFragment;
import com.firstbit.fifaworldcup2018highlights.fragments.VideosFragment;
import com.firstbit.fifaworldcup2018highlights.fragments.ScheduleFragment;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;



public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Fragment fragmentCurrent;
    private Fragment videoFragment = new VideosFragment();
    private AdView mAdView;
    private InterstitialAd mInterstitialAd;
    private boolean doubleBackToExitPressedOnce;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initBannerAd();
        MobileAds.initialize(this, getString(R.string.full_screen_ad_id));
        initFullScreenAds();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        if (savedInstanceState == null) {
            addFragment(videoFragment, "latest");
        }
        Menu menu = navigationView.getMenu();
        Menu submenu = menu.addSubMenu("Leagues and Cups");
        getLeagues(submenu);
    }

    private void initBannerAd() {
        mAdView = (AdView) findViewById(R.id.adView);

        AdRequest adRequest = new AdRequest.Builder()
                .build();

        mAdView.loadAd(adRequest);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        int ot = getResources().getConfiguration().orientation;
        switch (ot) {

            case Configuration.ORIENTATION_LANDSCAPE:

                Log.d("my orient", "ORIENTATION_LANDSCAPE");
                break;
            case Configuration.ORIENTATION_PORTRAIT:
                showInterstitial();
                break;
            default:
                Log.d("my orient", "default val");
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

/*        MenuItem search_item = menu.findItem(R.id.search);

        SearchView searchView = (SearchView) search_item.getActionView();
        searchView.setFocusable(false);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {


            @Override
            public boolean onQueryTextSubmit(String s) {

                //clear the previous data in search arraylist if exist
                search_result_arraylist.clear();

                keyword = s.toUpperCase();

                //checking language arraylist for items containing search keyword

                for(int i =0 ;i < languagesarraylist.size();i++){
                    if(languagesarraylist.get(i).contains(keyword)){
                        search_result_arraylist.add(languagesarraylist.get(i).toString());
                    }
                }

                language_adapter = new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_list_item_1,search_result_arraylist);
                lv_languages.setAdapter(language_adapter);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });*/
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        int groupId = item.getGroupId();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (id == R.id.nav_schedule) {
            launchFragment(new ScheduleFragment(), "schedule");
        } else if (id == R.id.nav_highlights) {
            launchFragment(new VideosFragment(), "videos");
        } else if (id == R.id.nav_all_leagues)
            launchFragment(new VideosFragment(), "latest");
        else if (groupId == R.id.leagues) {
            launchFragment(new VideosFragment(), item.toString());
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void addFragment(Fragment fragment, String tag) {
        fragmentCurrent = fragment;
        Bundle bundle = new Bundle();
        bundle.putString("tag", tag);
        fragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().add(R.id.fl_container, fragment).commit();
    }

    private void launchFragment(Fragment fragment, String tag) {
        Bundle bundle = new Bundle();
        bundle.putString("tag", tag);
        fragmentCurrent = fragment;
        fragment.setArguments(bundle);
        FragmentManager manager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.fl_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void getLeagues(final Menu submenu) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        Query myRef = database.getReference("leagues_cups");

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                    submenu.add(R.id.leagues, Menu.NONE, Menu.NONE, singleSnapshot.getValue(League.class).getLeague_name());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("", "onCancelled", databaseError.toException());
            }
        });

    }

    private void initFullScreenAds() {
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
}
