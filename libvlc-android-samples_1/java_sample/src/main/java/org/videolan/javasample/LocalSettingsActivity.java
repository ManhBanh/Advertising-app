package org.videolan.javasample;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import org.videolan.javasample.storagechooser.FileUtils;

public class LocalSettingsActivity extends AppCompatActivity {

    private static final String TAG = "LocalSettingsActivity";

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    private static final int FOLDERPICKER_PERMISSIONS = 1, FILEPICKER_PERMISSIONS = 1, VIDEO_REQUEST_CODE = 1, IMAGE_REQUEST_CODE = 2;
    private ViewGroup actionBarLayout;
    private ActionBar actionBar;
    private Button btnHome, btnSettings, btnStatus;
    private CheckBox cbStartUp;
    private PopupMenu textAnimPopupMenu;
    private String splashScreenPath;

    private RelativeLayout rlSelectVideo, rlSelectWeb, rlReplaceSplashScreen, rlTextAnimation, rlActionBarDisplayDuration, rlStartDelay;
    private TextView tvSelectVideoExplanation, tvSelectWebExplanation, tvReplaceSplashScreenExplanation, tvDuration, tvStartDelayDuration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_settings);

        //Share preferences
        sharedPreferences = getSharedPreferences("share_result", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        actionBarLayout = (ViewGroup) getLayoutInflater().inflate(R.layout.action_bar, null);
        actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(actionBarLayout);
        btnHome = findViewById(R.id.btnHome);
        btnSettings = findViewById(R.id.btnSettings);
        btnStatus = findViewById(R.id.btnStatus);
        cbStartUp = findViewById(R.id.cbStartUp);

        rlSelectVideo = findViewById(R.id.rlSelectVideo);
        tvSelectVideoExplanation = findViewById(R.id.tvSelectVideoExplanation);
        tvSelectVideoExplanation.setText(sharedPreferences.getString("share_videopath", getString(R.string.select_video_explanation)));
        rlSelectWeb = findViewById(R.id.rlSelectWeb);
        tvSelectWebExplanation = findViewById(R.id.tvSelectWebExplanation);
        tvSelectWebExplanation.setText(sharedPreferences.getString("share_url", getString(R.string.select_web_explanation)));
        rlReplaceSplashScreen = findViewById(R.id.rlReplaceSplashScreen);
        tvReplaceSplashScreenExplanation = findViewById(R.id.tvReplaceSplashScreenExplanation);
        rlTextAnimation = findViewById(R.id.rlTextAnimation);
        rlStartDelay = findViewById(R.id.rlStartDelay);
        tvStartDelayDuration = findViewById(R.id.tvStartDelayDuration);
        tvStartDelayDuration.setText(String.valueOf(sharedPreferences.getLong("share_startdelayduration", 10)));
        splashScreenPath = sharedPreferences.getString("share_splashscreenpath", "file:///android_asset/splash_screen.png");
        tvReplaceSplashScreenExplanation.setText(splashScreenPath);
        tvDuration = findViewById(R.id.tvDuration);
        tvDuration.setText(String.valueOf(sharedPreferences.getLong("share_delayMilis", 3000) / (long) 1000));


        /**
         *Chọn một video để phát
         */
        rlSelectVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] PERMISSIONS = {
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                };
                if (hasPermissions(LocalSettingsActivity.this, PERMISSIONS)) {
                    showVideoChooser();
                } else {
                    requestPermissions(PERMISSIONS, FILEPICKER_PERMISSIONS);
                }
            }
        });
        rlSelectVideo.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (rlSelectVideo.hasOnClickListeners())
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            rlSelectVideo.setBackground(getDrawable(R.drawable.gradient_background));
                            break;
                        case MotionEvent.ACTION_UP:
//                            rlSelectVideo.setBackground(getResources().getDrawable(R.color.trongsuot));
                            rlSelectVideo.setBackground(getDrawable(R.color.trongsuot));
                            break;
                    }
                return false;
            }
        });

        /**
         * Chọn URL để tải trang web
         */
        rlSelectWeb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(LocalSettingsActivity.this);
                alertDialog.setTitle(R.string.enter_an_url);
                final EditText eURL = new EditText(LocalSettingsActivity.this);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                eURL.setLayoutParams(lp);
                eURL.requestFocus();
                eURL.setText("https://");
                alertDialog.setView(eURL);
                alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        tvSelectWebExplanation.setText(eURL.getText().toString());
                        editor.putString("share_url", tvSelectWebExplanation.getText().toString());
                        editor.commit();
                    }
                });
                alertDialog.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                alertDialog.show();
            }
        });
        rlSelectWeb.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (rlSelectWeb.hasOnClickListeners())
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            rlSelectWeb.setBackground(getResources().getDrawable(R.drawable.gradient_background));
                            break;
                        case MotionEvent.ACTION_UP:
                            rlSelectWeb.setBackground(getResources().getDrawable(R.color.trongsuot));
                            break;
                    }
                return false;
            }
        });

        /**
         * Thay đổi màn hình khởi chạy
         */
        rlReplaceSplashScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] PERMISSIONS = {
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                };
                if (hasPermissions(LocalSettingsActivity.this, PERMISSIONS)) {
                    showImageChooser();
                } else {
                    requestPermissions(PERMISSIONS, FILEPICKER_PERMISSIONS);
                }
            }
        });
        rlReplaceSplashScreen.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (rlReplaceSplashScreen.hasOnClickListeners()) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            rlReplaceSplashScreen.setBackground(getResources().getDrawable(R.drawable.gradient_background));
                            break;
                        case MotionEvent.ACTION_UP:
                            rlReplaceSplashScreen.setBackground(getResources().getDrawable(R.color.trongsuot));
                            break;
                    }
                }
                return false;
            }
        });

        /**
         * Tùy chỉnh hiệu ứng cho text
         */
        rlTextAnimation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textAnimPopupMenu = new PopupMenu(LocalSettingsActivity.this, v);
                MenuInflater inflater = textAnimPopupMenu.getMenuInflater();
                inflater.inflate(R.menu.menu_text_anim, textAnimPopupMenu.getMenu());
                textAnimPopupMenu.getMenu().getItem(sharedPreferences.getInt("item_check", 2)).setChecked(true);
                textAnimPopupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.mnTopToBottom:
                                editor.putInt("item_check", 0);
                                editor.putLong("share_text_delay", 5000);
                                editor.putInt("share_anim", R.anim.textview_anim_top_to_bottom);
                                editor.commit();
                                break;
                            case R.id.mnbottomoTop:
                                editor.putInt("item_check", 1);
                                editor.putLong("share_text_delay", 5000);
                                editor.putInt("share_anim", R.anim.textview_anim_bottom_to_top);
                                editor.commit();
                                break;
                            case R.id.mnRightToLeft:
                                editor.putInt("item_check", 2);
                                editor.putLong("share_text_delay", 10000);
                                editor.putInt("share_anim", R.anim.textview_anim_right_to_left);
                                editor.commit();
                                break;
                            case R.id.mnLeftToRight:
                                editor.putInt("item_check", 3);
                                editor.putLong("share_text_delay", 10000);
                                editor.putInt("share_anim", R.anim.textview_anim_left_to_right);
                                editor.commit();
                                break;
                            default:
                                return false;
                        }
                        return false;
                    }
                });
                textAnimPopupMenu.show();
            }
        });
        rlTextAnimation.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (rlTextAnimation.hasOnClickListeners())
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            rlTextAnimation.setBackground(getResources().getDrawable(R.drawable.gradient_background));
                            break;
                        case MotionEvent.ACTION_UP:
                            rlTextAnimation.setBackground(getResources().getDrawable(R.color.trongsuot));
                            break;
                    }
                return false;
            }
        });

        /**
         *Set chế độ khởi động cùng thiết bị
         */
        cbStartUp.setChecked(sharedPreferences.getBoolean("share_startup", false));
        cbStartUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cbStartUp.isChecked()){
                    editor.putBoolean("share_startup", true);
                    editor.commit();
                }else{
                    editor.putBoolean("share_startup", false);
                    editor.commit();
                }
            }
        });

        /**
         * Nhập thời gian hiển thị thanh actionbar
         */
        rlActionBarDisplayDuration = findViewById(R.id.rlActionBarDisplayDuration);
        rlActionBarDisplayDuration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(LocalSettingsActivity.this);
                alertDialog.setTitle(R.string.enter_the_duration);
                final EditText eDuration = new EditText(LocalSettingsActivity.this);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                eDuration.setLayoutParams(lp);
                eDuration.setInputType(InputType.TYPE_CLASS_NUMBER);
                eDuration.requestFocus();
                alertDialog.setView(eDuration);
                alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        tvDuration.setText(eDuration.getText().toString());
                        editor.putLong("share_delayMilis", Long.parseLong(tvDuration.getText().toString()) * 1000);
                        editor.commit();
                    }
                });
                alertDialog.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                alertDialog.show();
            }
        });
        rlActionBarDisplayDuration.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (rlActionBarDisplayDuration.hasOnClickListeners())
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            rlActionBarDisplayDuration.setBackground(getResources().getDrawable(R.drawable.gradient_background));
                            break;
                        case MotionEvent.ACTION_UP:
                            rlActionBarDisplayDuration.setBackground(getResources().getDrawable(R.color.trongsuot));
                            break;
                    }
                return false;
            }
        });

        /**
         *Độ trễ khi khởi chạy ứng dụng
         */
        rlStartDelay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(LocalSettingsActivity.this);
                alertDialog.setTitle(R.string.enter_the_duration);
                final EditText eDuration = new EditText(LocalSettingsActivity.this);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                eDuration.setLayoutParams(lp);
                eDuration.setInputType(InputType.TYPE_CLASS_NUMBER);
                eDuration.requestFocus();
                alertDialog.setView(eDuration);
                alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        tvStartDelayDuration.setText(eDuration.getText().toString());
                        editor.putLong("share_startdelayduration", Long.parseLong(tvStartDelayDuration.getText().toString()) * 1000);
                        editor.commit();
                    }
                });
                alertDialog.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                alertDialog.show();
            }
        });

        /**
         * Trở về trang chủ
         */
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case VIDEO_REQUEST_CODE:
                // If the file selection was successful
                if (resultCode == RESULT_OK) {
                    if (data != null) {
                        // Get the URI of the selected file
                        final Uri uri = data.getData();
                        Log.i(TAG, "Uri = " + uri.toString());
                        try {
                            // Get the file path from the URI
                            final String path = FileUtils.getPath(this, uri);
                            tvSelectVideoExplanation.setText(path);
                            editor.putString("share_videopath", path);
                            editor.commit();
                        } catch (Exception e) {
                            Log.e("Error:\t", "File select error", e);
                        }
                    }
                }
                break;
            case IMAGE_REQUEST_CODE:
                // If the file selection was successful
                if (resultCode == RESULT_OK) {
                    if (data != null) {
                        // Get the URI of the selected file
                        final Uri uri = data.getData();
                        Log.i(TAG, "Uri = " + uri.toString());
                        try {
                            // Get the file path from the URI
                            final String path = FileUtils.getPath(this, uri);
                            tvReplaceSplashScreenExplanation.setText(path);
                            editor.putString("share_splashscreenpath", path);
                            editor.commit();
                        } catch (Exception e) {
                            Log.e("Error:\t", "File select error", e);
                        }
                    }
                }
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case FILEPICKER_PERMISSIONS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(
                            LocalSettingsActivity.this,
                            R.string.toast_permission_granted,
                            Toast.LENGTH_SHORT
                    ).show();
                } else {
                    Toast.makeText(
                            LocalSettingsActivity.this,
                            R.string.toast_permission_denied,
                            Toast.LENGTH_SHORT
                    ).show();
                }

                return;
            }
        }
    }

    public void showImageChooser() {
        // Use the GET_CONTENT intent from the utility class
        Intent target = FileUtils.createGetContentIntent();
        // Create the chooser Intent
        Intent intent = Intent.createChooser(
                target, getString(R.string.chooser_title));
        try {
            startActivityForResult(intent, IMAGE_REQUEST_CODE);
        } catch (ActivityNotFoundException e) {
            // The reason for the existence of aFileChooser
        }
    }

    public void showVideoChooser() {
        // Use the GET_CONTENT intent from the utility class
        Intent target = FileUtils.createGetContentIntent();
        // Create the chooser Intent
        Intent intent = Intent.createChooser(
                target, getString(R.string.chooser_title));
        try {
            startActivityForResult(intent, VIDEO_REQUEST_CODE);
        } catch (ActivityNotFoundException e) {
            // The reason for the existence of aFileChooser
        }
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }
}