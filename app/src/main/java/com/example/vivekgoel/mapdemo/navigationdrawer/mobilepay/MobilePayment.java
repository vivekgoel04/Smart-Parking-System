package com.example.vivekgoel.mapdemo.navigationdrawer.mobilepay;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.vivekgoel.mapdemo.MapsActivity;
import com.example.vivekgoel.mapdemo.R;

public class MobilePayment extends AppCompatActivity {
    Button btPay,btExit;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_payment);
        btPay = (Button) findViewById(R.id.buttonPay);
        btPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getBaseContext(), "Payment Successful", Toast.LENGTH_SHORT).show();
                intent = new Intent(MobilePayment.this, MapsActivity.class);
                startActivity(intent);
                finish();
            }
        });
        btExit = (Button) findViewById(R.id.buttonExit);
        btExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(MobilePayment.this, MapsActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_mobile_payment, menu);
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
