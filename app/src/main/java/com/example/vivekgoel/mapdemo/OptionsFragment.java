package com.example.vivekgoel.mapdemo;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.vivekgoel.mapdemo.notification.AlertReceiver;
import com.example.vivekgoel.mapdemo.singletonclasses.ServerSingleton;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by vivekgoel on 3/26/16.
 */
public class OptionsFragment extends Fragment {
    Button buttonSetTimer;
    TextView tvAddress;
    public static Context context;
    public static Activity activity;
    public View view = null;
    public static DatePicker datePicker;
    GoogleMap mMap;

    ServerSingleton myObj = ServerSingleton.getInstance();

    public OptionsFragment() {
    }

    @Nullable
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.options_fragment, container, false);
        buttonSetTimer = (Button) view.findViewById(R.id.buttonSetTimer);
        buttonSetTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTimePickerDialog(getActivity());
            }
        });
        tvAddress = (TextView) view.findViewById(R.id.textViewAddress);
        String address = myObj.address.get(0).getAddressLine(0);
        tvAddress.setText(address);
        setCamera();
        activity = getActivity();
        return view;
    }
    public void setCamera() {
        //Finding current lattitude & longitude
        SupportMapFragment supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.optionFragmentMap);
        mMap = supportMapFragment.getMap();
        LatLng latLng = new LatLng(myObj.getLocation().getLatitude(), myObj.getLocation().getLongitude());

        //Setting camera properties
        mMap.setMyLocationEnabled(true);
        mMap.addMarker(new MarkerOptions().position(new LatLng(myObj.getLocation().getLatitude(), myObj.getLocation().getLongitude())).title("Parked"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setAllGesturesEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
    }

    public static void setTimePickerDialog(Activity activity) {
        final View dialogView = View.inflate(activity, R.layout.fragment_timepicker, null);
        final AlertDialog alertDialog = new AlertDialog.Builder(activity).create();
        alertDialog.setView(dialogView);
        alertDialog.show();
        dialogView.findViewById(R.id.time_set).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePicker timePicker = (TimePicker) dialogView.findViewById(R.id.time_picker);
                long pickerTimeMinute = timePicker.getCurrentHour() * 60 + timePicker.getMinute();
                Calendar cal = Calendar.getInstance();
                Date date = cal.getTime();
                long currentTimeMinute = date.getHours() * 60 + date.getMinutes();
                if (checkTime(pickerTimeMinute, currentTimeMinute)) {
                    alertDialog.dismiss();
                    setTimer(pickerTimeMinute - currentTimeMinute);
                } else
                    Toast.makeText(view.getContext(), "Invalid Time", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static boolean checkTime(long timePickerTime, long currentTimeMinute) {
        if (timePickerTime < currentTimeMinute)
            return false;
        return true;
    }
    public static void setTimer(long timeMinutes) {
        long timeMillis = timeMinutes*60*1000;
        Calendar cal = new GregorianCalendar();
        Intent activate = new Intent(activity, AlertReceiver.class);
        AlarmManager alarms ;
        PendingIntent alarmIntent = PendingIntent.getBroadcast(activity, 0, activate, 0);
        alarms = (AlarmManager) activity.getSystemService(Context.ALARM_SERVICE);
        alarms.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis() + timeMillis, alarmIntent);
    }
}


