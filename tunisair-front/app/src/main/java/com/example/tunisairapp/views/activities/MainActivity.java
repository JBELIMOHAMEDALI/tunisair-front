package com.example.tunisairapp.views.activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tunisairapp.Constants;
import com.example.tunisairapp.R;

public class MainActivity extends AppCompatActivity {



    Animation topAnim, bottomAnim;
    TextView app_title, app_subtitle;
    ImageView app_logo;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //Animations
        topAnim = AnimationUtils.loadAnimation(this,R.anim.top_animation);
        bottomAnim = AnimationUtils.loadAnimation(this,R.anim.bottom_animation);

        //Widgets
        app_logo = findViewById(R.id.app_logo);
        app_title = findViewById(R.id.app_title);
        app_subtitle = findViewById(R.id.app_subtitle);

        //assign animation
        app_logo.setAnimation(topAnim);
        app_title.setAnimation(bottomAnim);
        app_subtitle.setAnimation(bottomAnim);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                redirectUser();

            }
        }, Constants.SPLASH_SCREEN);
    }

    private void redirectUser() {

        SharedPreferences preferences= getSharedPreferences(Constants.SHARED_PREFERENCE_NAME, MODE_PRIVATE);
        boolean is_connected = preferences.getBoolean(Constants.USER_CONNECTED, false);
        String userMail = preferences.getString(Constants.USER_MAIL, null);
        if(is_connected)
        {
            Intent i = new Intent(MainActivity.this, HomeActivity.class);
            startActivity(i);
        }
        else {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);

        }
        finish();

    }
}