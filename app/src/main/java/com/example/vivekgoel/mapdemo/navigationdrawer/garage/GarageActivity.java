package com.example.vivekgoel.mapdemo.navigationdrawer.garage;

import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.example.vivekgoel.mapdemo.R;
import com.example.vivekgoel.mapdemo.singletonclasses.ServerSingleton;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class GarageActivity extends AppCompatActivity {
    GoogleMap mMap;
    ServerSingleton myObj = ServerSingleton.getInstance();
    Location location;
    double latitude = 0, longitude = 0;
    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters

    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000 * 10; // 10 sec
    double a[][] = {{37.332376,-121.891410},{37.336395,-121.893341},{37.336683,-121.892238},{37.334556,-121.876456},{37.331864,-121.882183}};
    String rate[] = {"$3","$5","$7","&4","$3"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_garage);
        setCamera();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_garage, menu);
        return true;
    }

    public void setCamera() {
        //Finding current lattitude & longitude
        SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.GarageMap);
        mMap = supportMapFragment.getMap();
        LatLng latLng = new LatLng(myObj.getLocation().getLatitude(), myObj.getLocation().getLongitude());

        //Setting camera properties
        mMap.setMyLocationEnabled(true);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setAllGesturesEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);

        for(int i=0;i<a.length;i++) {
            mMap.addMarker(new MarkerOptions().position(new LatLng(a[i][0],a[i][1])).title(rate[i]));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
