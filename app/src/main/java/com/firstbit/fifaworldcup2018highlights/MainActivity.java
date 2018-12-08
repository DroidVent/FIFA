package com.firstbit.fifaworldcup2018highlights;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.firstbit.fifaworldcup2018highlights.fragments.StandingsFragment;
import com.firstbit.fifaworldcup2018highlights.fragments.VideosFragment;
import com.firstbit.fifaworldcup2018highlights.fragments.ScheduleFragment;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private int currentMenuItem;
    private Fragment fragmentCurrent;
    private Fragment videoFragment = new VideosFragment();
    private TextView tvUser;
    private ImageView ivUser;
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        initAd();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        tvUser = (TextView) navigationView.getHeaderView(0).findViewById(R.id.tv_username);
        ivUser = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.iv_profile);
        setUserData();
        currentMenuItem = R.id.nav_highlights;
        if (savedInstanceState == null) {
            addFragment(videoFragment);
        }

    }

    private void initAd() {
        mAdView = (AdView) findViewById(R.id.adView);

        AdRequest adRequest = new AdRequest.Builder()
                .build();
        mAdView.loadAd(adRequest);
    }

    private void setUserData() {
        SharedPreferences sharedPref = getSharedPreferences("user", Context.MODE_PRIVATE);
        String username = sharedPref.getString("username", "");
        String photo = sharedPref.getString("photo", "");
        if (photo != null && !photo.equals("")) {
            RequestOptions requestOptions = new RequestOptions();
            requestOptions.centerCrop();
            requestOptions.placeholder(R.mipmap.user_placeholder);
            Glide.with(this)
                    .load(photo)
                    .apply(requestOptions)
                    .into(ivUser);
        }
        if (username != null && !username.equals(""))
            tvUser.setText(username);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (fragmentCurrent.equals(videoFragment)) {
                getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                super.onBackPressed();
            } else {
                getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                launchFragment(videoFragment, "highlights");
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
  /*      int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_signout) {
            FirebaseAuth.getInstance().signOut();
            clearPrefernce();
            launchSignIn();
            finish();
            return true;
        }
*/
        return super.onOptionsItemSelected(item);
    }

    private void launchSignIn() {
        Intent mIntent = new Intent(this, SigninActivity.class);
        startActivity(mIntent);
    }

    private void clearPrefernce() {
        SharedPreferences sharedPref = getSharedPreferences("user", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.remove("email").apply();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (id == currentMenuItem) {
            drawer.closeDrawer(GravityCompat.START);
            return false;
        }
        if (id == R.id.nav_schedule) {
            launchFragment(new ScheduleFragment(), "schedule");
        } else if (id == R.id.nav_highlights) {
            launchFragment(new VideosFragment(), "highlights");
        } else if (id == R.id.nav_standings)
            launchFragment(new StandingsFragment(), "standings");
        currentMenuItem = id;

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void addFragment(Fragment fragment) {
        fragmentCurrent = fragment;
        getSupportFragmentManager().beginTransaction().add(R.id.fl_container, fragment).commit();
    }

    private void launchFragment(Fragment fragment, String tag) {
        fragmentCurrent = fragment;
        FragmentManager manager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.fl_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
