package org.videolan.javasample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.File;


public class SplashScreenActivity extends AppCompatActivity {

    Handler handler;
    private ImageView imgSplashScreen;
    SharedPreferences sharedPreferences;
    String splashScreenPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        imgSplashScreen = findViewById(R.id.imgSplashScreen);
        sharedPreferences = getSharedPreferences("share_result", Context.MODE_PRIVATE);

        splashScreenPath = sharedPreferences.getString("share_splashscreenpath", "file:///android_asset/splash_screen.png");
//        File file = new File("/storage/emulated/0/DCIM/Camera/00000PORTRAIT_00000_BURST20200126113417365.jpg");
        File file = new File(splashScreenPath);
        Glide.with(getBaseContext())
                .load(file)
                .placeholder(R.drawable.splash_screen)
                .into(imgSplashScreen);
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
//                Toast.makeText(SplashScreenActivity.this, "" + splashScreenPath, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SplashScreenActivity.this, JavaActivity.class);
                startActivity(intent);
                finish();
            }
        }, 2000);
    }

    @Override
    protected void onStop() {
        splashScreenPath = sharedPreferences.getString("share_splashscreenpath", "file:///android_asset/splash_screen.png");
        super.onStop();
    }
}
