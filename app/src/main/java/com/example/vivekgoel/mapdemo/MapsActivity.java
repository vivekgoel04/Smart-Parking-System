package com.example.vivekgoel.mapdemo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;

import com.example.vivekgoel.mapdemo.navigationdrawer.garage.GarageActivity;
import com.example.vivekgoel.mapdemo.navigationdrawer.history.ParkingHistory;
import com.example.vivekgoel.mapdemo.navigationdrawer.mobilepay.MobilePayment;
import com.example.vivekgoel.mapdemo.singletonclasses.ServerSingleton;
import com.google.android.gms.maps.GoogleMap;

public class MapsActivity extends AppCompatActivity {
    android.support.v7.app.ActionBar actionBar;
    ImageButton carParked;
    SharedPreferences walkBack;
    String Current = "Current Location";
    GoogleMap mMap;
    DrawerLayout drawerLayout;
    ServerSingleton myObj = ServerSingleton.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        myObj.setActivity(getApplicationContext());
        actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
        actionBar.setDisplayHomeAsUpEnabled(true);
        drawerLayout = (DrawerLayout) findViewById(R.id.navigation_drawer_layout);

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        if (navigationView != null) {
            setupNavigationDrawerContent(navigationView);
        }


        carParked = (ImageButton) findViewById(R.id.carParked);
        Log.d("###########", "***** Inside MApActivity *****");
        if(savedInstanceState==null) {
            getSupportFragmentManager().beginTransaction().add(R.id.mapContainer,new com.example.vivekgoel.mapdemo.MapFragment()).commit();
        }
    }
    private void setupNavigationDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        Intent intent;
                        switch (menuItem.getItemId()) {
                            case R.id.item_navigation_drawer_parking_history:
                                menuItem.setChecked(true);
                                drawerLayout.closeDrawer(GravityCompat.START);
                                intent = new Intent(MapsActivity.this, ParkingHistory.class);
                                startActivity(intent);
                                return true;
                            case R.id.item_navigation_drawer_mobile_payment:
                                menuItem.setChecked(true);
                                drawerLayout.closeDrawer(GravityCompat.START);
                                intent = new Intent(MapsActivity.this, MobilePayment.class);
                                startActivity(intent);
                                return true;
                            case R.id.item_navigation_drawer_garage_info:
                                menuItem.setChecked(true);
                                drawerLayout.closeDrawer(GravityCompat.START);
                                intent = new Intent(MapsActivity.this, GarageActivity.class);
                                startActivity(intent);
                                return true;

                        }
                        return true;
                    }
                });
    }
    @Override
    protected void onResume() {
        super.onResume();
    }

    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        walkBack = getSharedPreferences(Current, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = walkBack.edit();

        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.action_logOut:

                break;
        }
        return super.onOptionsItemSelected(item);
    }



}