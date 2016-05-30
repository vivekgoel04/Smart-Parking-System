package com.example.vivekgoel.mapdemo;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.vivekgoel.mapdemo.singletonclasses.DatabaseSingleton;
import com.example.vivekgoel.mapdemo.singletonclasses.ServerSingleton;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends Fragment {
    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters

    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000 * 10; // 10 sec
    ImageButton carParked;
    View view;
    public GoogleMap mMap; // Might be null if Google Play services APK is not available.
    public Location location;
    ServerSingleton myObj = ServerSingleton.getInstance();
    DatabaseSingleton myDb = DatabaseSingleton.getInstance();
    String slotno = "20";
    LocationManager locationManager;
    Context context = getContext();
    Double latitude, longitude;

    public MapFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d("###########", "***** Inside MApFragment *****");
        view = inflater.inflate(R.layout.fragment_maps, container, false);
        carParked = (ImageButton) view.findViewById(R.id.carParked);
        carParked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLocation();
                getFragmentManager().beginTransaction().replace(R.id.optionsContainer, new com.example.vivekgoel.mapdemo.OptionsFragment()).addToBackStack(null).commit();
            }
        });
        return view;
    }

    public void onResume() {
        super.onResume();
        setLocationAndCityName();
        setCamera();
        new ServerConnection().execute();
        setUpMapIfNeeded();
    }

    Boolean canGetLocation = false;

    //Finding current location
    public Location getLocation() {
        android.location.LocationListener locationListener = new android.location.LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };
        try {
            locationManager = (LocationManager) getContext().getSystemService(context.LOCATION_SERVICE);
            // getting GPS status
            boolean isGPSEnabled = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);
            // getting network status
            boolean isNetworkEnabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {
                // no network provider is enabled
            } else {
                this.canGetLocation = true;
                if (isNetworkEnabled) {
                    locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, locationListener);
                    Log.d("Network", "Network Enabled");
                    if (locationManager != null) {
                        location = locationManager
                                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }
                    }
                }
                // if GPS Enabled get lat/long using GPS Services
                if (isGPSEnabled) {
                    if (location == null) {
                        locationManager.requestLocationUpdates(
                                LocationManager.NETWORK_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, locationListener);
                        Log.d("GPS", "GPS Enabled");
                        if (locationManager != null) {
                            location = locationManager
                                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                            }
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return location;
    }

    public void setLocationAndCityName() {
        myObj.setLocation(getLocation());
        myObj.setCurrentCityName(getActivity());
    }

    public void setCamera() {
        //Finding current lattitude & longitude
        SupportMapFragment supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mMap = supportMapFragment.getMap();


        LatLng latLng = new LatLng(myObj.getLocation().getLatitude(), myObj.getLocation().getLongitude());

        //Setting camera properties
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(13));
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setAllGesturesEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
    }

    //Get parking spot data from cloud
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    private void setUpMap() {
        mMap.setMyLocationEnabled(true);
        for (int i = 0; i < myObj.NoOfSpaces; i++) {
            if (myObj.availability[i].equals("0"))
                mMap.addMarker(new MarkerOptions().position(new LatLng(myObj.lat[i], myObj.lng[i])).title(myObj.tariff[i]).alpha(0.9f));
            else if ((myObj.disability[i].equals("1")) && (myObj.availability[i].equals("1"))) {
                final LatLng MELBOURNE = new LatLng(myObj.lat[i], myObj.lng[i]);
                mMap.addMarker(new MarkerOptions()
                        .position(MELBOURNE)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)).title(myObj.tariff[i]).alpha(0.9f));
            } else {
                final LatLng MELBOURNE = new LatLng(myObj.lat[i], myObj.lng[i]);
                mMap.addMarker(new MarkerOptions()
                        .position(MELBOURNE)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)).title(myObj.tariff[i]).alpha(0.9f));

            }
        }
    }

    private void setLocation() {

        Double dlatitude = location.getLatitude();
        String lattitude = Double.toString(dlatitude);
        Double dlongitude = location.getLongitude();
        String longitude = Double.toString(dlongitude);

        saveData(lattitude,longitude);
    }
    public void saveData(String latitude, String longitude) {
        myDb.Helper(getContext());
        myDb = myDb.open();
        myDb.insertRow("1", latitude, longitude, "20");
    }


    class ServerConnection extends AsyncTask<Void, Void, Void> {
        InputStream is = null;
        String line = null;
        String result = null;
        String location = myObj.getCurrentCityName();
        ProgressDialog progressDialog;

        public ServerConnection() {
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Processing...");
            progressDialog.setCancelable(false);
            progressDialog.show();

        }

        @Override
        protected Void doInBackground(Void... params) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            ArrayList<NameValuePair> dataToSend = new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("location", location));
            try {
                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost("http://smartparksj.com/parkdatafetch.php");
                httpPost.setEntity(new UrlEncodedFormEntity(dataToSend));
                HttpResponse response = httpClient.execute(httpPost);
                HttpEntity entity = response.getEntity();
                is = entity.getContent();
            } catch (Exception e) {
                System.out.println("1st Exception caught : " + e);
            }
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
                StringBuilder sb = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                result = sb.toString();
                is.close();
                Log.d("********", "" + result);
            } catch (Exception e) {
                System.out.println("Exception caught" + e);
            }
            try {
                JSONArray jsonArray = new JSONArray(result);
                myObj.NoOfSpaces = jsonArray.length();
                System.out.println("------------" + myObj.NoOfSpaces);
                String[] latlng;
                for (int i = 0; i < myObj.NoOfSpaces; i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    JSONObject newObject = jsonObject.getJSONObject("park");
                    myObj.sensorLocation.put(newObject.getString("sensorId"), newObject.getString("gpsLocation"));
                    myObj.availability[i] = newObject.getString("availability");
                    myObj.tariff[i] = newObject.getString("tarrif");
                    myObj.disability[i] = newObject.getString("flagDisability");
                    latlng = newObject.getString("gpsLocation").split(",");
                    myObj.lat[i] = Double.parseDouble(latlng[0]);
                    myObj.lng[i] = Double.parseDouble(latlng[1]);
                }

            } catch (Exception e) {
                System.out.println("3rd Exception caught");
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            setUpMap();
            progressDialog.dismiss();
        }
    }

}
