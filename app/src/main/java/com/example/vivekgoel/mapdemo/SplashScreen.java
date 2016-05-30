package com.example.vivekgoel.mapdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.example.vivekgoel.mapdemo.signinSignup.SignIn;
import com.example.vivekgoel.mapdemo.singletonclasses.ServerSingleton;

public class SplashScreen extends AppCompatActivity {
    ImageView imageView;
    ActionBar actionBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ServerSingleton myObj = ServerSingleton.getInstance();
        getSupportActionBar().hide();
        setContentView(R.layout.activity_splash_screen);

        actionBar = getSupportActionBar();


        imageView = (ImageView) findViewById(R.id.splashImage);
        final Animation an = AnimationUtils.loadAnimation(getBaseContext(),R.anim.splashanim);
        final Animation an1 = AnimationUtils.loadAnimation(getBaseContext(),R.anim.splashanim);

        imageView.startAnimation(an);

        an.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {


            }

            @Override
            public void onAnimationEnd(Animation animation) {
                imageView.startAnimation(an1);

                finish();
                Intent intent = new Intent(SplashScreen.this, SignIn.class);
                startActivity(intent);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }
    public void onStop() {
        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_splash_screen, menu);
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
