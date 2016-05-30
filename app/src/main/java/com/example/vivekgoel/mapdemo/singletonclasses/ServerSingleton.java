package com.example.vivekgoel.mapdemo.singletonclasses;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * Created by vivekgoel on 4/22/16.
 */
public class ServerSingleton {
    public HashMap<String, String> sensorLocation = new HashMap<>();
    public String[] availability = new String[100];
    public String[] disability = new String[100];
    public String[] tariff = new String[100];
    public Double[] lat = new Double[100];
    public Double[] lng = new Double[100];
    public int NoOfSpaces;
    public String currentCity = "";
    private long timer = 0;
    private Location location;
    public List<Address> address;
    private  Context context;
    private Activity activity;
    private SharedPreferences parkingSharedPreferences;
    private static ServerSingleton myObject = new ServerSingleton();
    private ServerSingleton() {
    }

    public static ServerSingleton getInstance() {
        return myObject;
    }

    public void setTimerTime(long time) {
        timer = time;
    }
    public long getTimerTime() {
        return timer;
    }
    public void setLocation(Location location) {
        this.location = location;
    }
    public Location getLocation() {
        return location;
    }
    public void setCurrentCityName(Activity activity) {
        Geocoder geoCoder = new Geocoder(activity, Locale.getDefault());
        try {
            this.address = geoCoder.getFromLocation(getLocation().getLatitude(),getLocation().getLongitude(), 1);
            currentCity = address.get(0).getLocality();
        } catch (IOException e) {
            // Handle IOException
            System.out.println("Exception : " + e);
        }
    }
    public void setCurrentCityName(String city) {
        currentCity = city;
    }
    public String getCurrentCityName() {
        return currentCity;
    }
    public void setActivity(Context context) {
        this.context = context;
    }
    public void setParkingSharedPreference() {
        parkingSharedPreferences = context.getSharedPreferences("Parking History",Context.MODE_PRIVATE);
    }
    public SharedPreferences getParkingSharedPreference(Context context) {
        parkingSharedPreferences = context.getSharedPreferences("Parking History", Context.MODE_PRIVATE);
        return parkingSharedPreferences;
    }

}