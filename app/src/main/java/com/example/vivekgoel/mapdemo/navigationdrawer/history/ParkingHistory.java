package com.example.vivekgoel.mapdemo.navigationdrawer.history;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.vivekgoel.mapdemo.R;
import com.example.vivekgoel.mapdemo.singletonclasses.DatabaseSingleton;
import com.example.vivekgoel.mapdemo.singletonclasses.ServerSingleton;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ParkingHistory extends AppCompatActivity {
    private ListView list;
    private PopupWindow pwindo;
    private ArrayAdapter<CustomList> adapter;
    private List<CustomList> myList = new ArrayList<CustomList>();
    TextView text;
    ServerSingleton myObj = ServerSingleton.getInstance();
    DatabaseSingleton myDb = DatabaseSingleton.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parking_history);
        addHistory();

    }

    @Override
    protected void onResume() {
        super.onResume();
        openDB();
    }

    private void addHistory() {
        openDB();
        list = (ListView) findViewById(R.id.listview);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view,
                                    final int position, long id) {
                // TODO Auto-generated method stub
                AlertDialog.Builder a_builder = new AlertDialog.Builder(ParkingHistory.this);
                a_builder.setMessage("What do you want to do?");
                a_builder.setCancelable(false);
                a_builder.setPositiveButton("Get Direction",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                long rowId = myDb.getRowId(position);
                                String latlng = myDb.getLattitude(rowId) + "," + myDb.getLongitude(rowId);
                                String uri = String.format(Locale.ENGLISH, "http://maps.google.com/maps?daddr=%s", latlng);
                                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                                        Uri.parse(uri));
                                startActivity(intent);
                            }
                        });
                a_builder.setNegativeButton("Delete",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                long rowId = myDb.getRowId(position);
                                myDb.deleteRow(rowId);
                                adapter.remove(adapter.getItem(position));
                                adapter.notifyDataSetChanged();
                            }
                        });
                a_builder.setNeutralButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                // TODO Auto-generated method stub
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = a_builder.create();
                alert.setTitle("Alert !!!");
                alert.show();
            }
        });

        Cursor cursor = myDb.getAllRows();
        displayRecordSet(cursor);
    }

    private void displayRecordSet(Cursor cursor) {
        if (cursor.moveToFirst()) {
            do {
                text = (TextView) findViewById(R.id.viewText1);
                text.setVisibility(View.GONE);
                // Process the data
                int id = cursor.getInt(myDb.COL_ROWID);
                String face = cursor.getString(myDb.COL_FACE);
                String lattitude = cursor.getString(myDb.COL_LATTITUDE);
                String longitude = cursor.getString(myDb.COL_LONGITUDE);
                String slotno = cursor.getString(myDb.COL_SLOTNO);

                String address = getAddress(lattitude,longitude);
                populateCustomList(face, address, slotno);
                populateListView();
            } while (cursor.moveToNext());
        }
        cursor.close();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        closeDB();
    }
    // Function called to populate CustomList
    public void populateCustomList(String face, String address, String slotno) {
        myList.add(new CustomList(R.drawable.parking_icon, address, slotno));
    }

    private void populateListView() {
        adapter = new MyListAdapter();
        list = (ListView) findViewById(R.id.listview);
        list.setAdapter(adapter);
    }

    public class MyListAdapter extends ArrayAdapter<CustomList> {
        public MyListAdapter() {
            super(ParkingHistory.this, R.layout.list_view, myList);
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub

            View itemView = convertView;
            if (itemView == null) {
                itemView = getLayoutInflater().inflate(R.layout.list_view,
                        parent, false);
            }
            // Find the list to work with
            CustomList currentList = myList.get(position);

            // Fill the view
            ImageView imageView = (ImageView) itemView.findViewById(R.id.face);
            imageView.setImageResource(currentList.getIconID());


            // Location & Date & Time:
            TextView dateTime = (TextView) itemView.findViewById(R.id.textViewAddress);
            dateTime.setText("" + currentList.getAddress());

            // Message:
            TextView message = (TextView) itemView.findViewById(R.id.textViewSlotNo);
            message.setText(currentList.getSlotno());

            return itemView;
        }
    }
    protected void onNewIntent(Intent intent) {
        myList.clear();
        addHistory();
    }
    public String getAddress(String lattitude,String longitude) {
        Geocoder geoCoder = new Geocoder(getBaseContext(), Locale.getDefault());
        Double dlattitude = Double.parseDouble(lattitude);
        Double dlongitude = Double.parseDouble(longitude);
        String address = "";
        try {
            List<Address> temp = geoCoder.getFromLocation(dlattitude, dlongitude, 1);
            address = temp.get(0).getAddressLine(0)+", "+temp.get(0).getLocality()+", "+temp.get(0).getCountryName();
        } catch (IOException e) {
            // Handle IOException
            System.out.println("Exception : " + e);
        }
        return address;
    }
    private void openDB() {
        myDb.Helper(this);
        myDb = myDb.open();
    }

    private void closeDB() {
        myDb.close();
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_parking_history, menu);
        return true;
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
