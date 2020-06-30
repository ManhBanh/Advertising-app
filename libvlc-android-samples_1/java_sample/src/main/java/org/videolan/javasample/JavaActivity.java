/*****************************************************************************
 * JavaActivity.java
 *****************************************************************************
 * Copyright (C) 2016-2019 VideoLAN
 * All rights reserved.
 *
 * This software may be modified and distributed under the terms
 * of the BSD license. See the LICENSE file for details.
 *****************************************************************************/

package org.videolan.javasample;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import org.videolan.javasample.fragment.VideoFragment;
import org.videolan.javasample.fragment.WebFragment;
import org.videolan.javasample.fragment.YoutubeVideoFragment;

public class JavaActivity extends AppCompatActivity implements GestureDetector.OnGestureListener {

    private static final String TAG = "JavaActivity";
    private static final int REQUEST_CODE_INFO = 1000;
    private long delayMilis;

    SharedPreferences sharedPreferences;

    private TextView tvNews;
    private Animation animation;

    GestureDetector gestureDetector;

    private ViewGroup actionBarLayout;
    private ActionBar actionBar;
    private Button btnHome, btnSettings, btnStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        gestureDetector = new GestureDetector(this, this);

        //Share preferences
        sharedPreferences = getSharedPreferences("share_result", Context.MODE_PRIVATE);

        actionBarLayout = (ViewGroup) getLayoutInflater().inflate(R.layout.action_bar, null);
        actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(actionBarLayout);
        btnHome = findViewById(R.id.btnHome);
        btnSettings = findViewById(R.id.btnSettings);
        btnStatus = findViewById(R.id.btnStatus);
        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(JavaActivity.this, LocalSettingsActivity.class);
                startActivity(intent);
            }
        });

        tvNews = findViewById(R.id.tvNews);
        startTextAnimation(
                sharedPreferences.getInt("share_anim", R.anim.textview_anim_right_to_left),
                sharedPreferences.getLong("share_text_delay", 10000));
        hideActionBar();
        try {
            getFragment(VideoFragment.newInstance(
                    sharedPreferences.getString("share_videopath", "bbb.m4v")), R.id.video_container);
            getFragment(WebFragment.newInstance(
                    sharedPreferences.getString("share_url", "https://www.24h.com.vn")), R.id.web_container);
//            getFragment(YoutubeVideoFragment.newInstance(), R.id.youtubevideo_container);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
//        startTextAnimation(
//                sharedPreferences.getInt("share_anim", R.anim.textview_anim_right_to_left),
//                sharedPreferences.getLong("share_text_delay", 10000));
//        hideActionBar();
        try {
            getFragment(VideoFragment.newInstance(
                    sharedPreferences.getString("share_videopath", "bbb.m4v")), R.id.video_container);
            getFragment(WebFragment.newInstance(
                    sharedPreferences.getString("share_url", "https://www.24h.com.vn")), R.id.web_container);
//            getFragment(YoutubeVideoFragment.newInstance(), R.id.youtubevideo_container);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
//        startTextAnimation(
//                sharedPreferences.getInt("share_anim", R.anim.textview_anim_right_to_left),
//                sharedPreferences.getLong("share_text_delay", 10000));
//        hideActionBar();
//        try {n
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        );
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @Override
    protected void onStop() {
        super.onStop();
//        closeFragment();
        stopTextAnimation();
        actionBar.hide();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        closeFragment();
        stopTextAnimation();
        actionBar.hide();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_option, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuLicense:
                break;
            case R.id.menuAbout:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        this.gestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        if (e1.getY() < e2.getY())
            actionBar.show();
        if (actionBar.isShowing()) {
            hideActionBar();
        }
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }

    public void getFragment(Fragment fragment, int view) {
        try {
            getSupportFragmentManager().beginTransaction()
                    .replace(view, fragment)
                    .commit();
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "getFragment: " + e.getMessage());
        }
    }

    public void closeFragment() {
//        getFragmentManager().popBackStack();
//        getSupportFragmentManager().popBackStack();
        Fragment videoFragment = getSupportFragmentManager().findFragmentById(R.id.video_container);
        getSupportFragmentManager().beginTransaction()
                .remove(videoFragment).commit();
        Fragment webFragment = getSupportFragmentManager().findFragmentById(R.id.web_container);
        getSupportFragmentManager().beginTransaction()
                .remove(webFragment).commit();
    }

    public void hideActionBar() {
        delayMilis = sharedPreferences.getLong("share_delayMilis", 3000);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                actionBar.hide();
//                handler.postDelayed(this, 4000);
            }
        }, delayMilis);
    }

    public void startTextAnimation(int anim, final long delayTime) {
        animation = AnimationUtils.loadAnimation(this, anim);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                tvNews.startAnimation(animation);
                handler.postDelayed(this, delayTime);
            }
        }, 100);
    }

    public void stopTextAnimation() {
        tvNews.clearAnimation();
    }
}
