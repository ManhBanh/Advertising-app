package org.videolan.javasample;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;

public class BootUpReceiver extends BroadcastReceiver {

    SharedPreferences sharedPreferences;

    @Override
    public void onReceive(Context context, Intent intent) {
        sharedPreferences = context.getSharedPreferences("share_result", Context.MODE_PRIVATE);
//        Khởi chạy ứng dụng
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
//            if (sharedPreferences.getBoolean("share_startup", false)) {
                Intent i = new Intent(context, JavaActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                PendingIntent pi = PendingIntent.getActivity(context, 0, i, PendingIntent.FLAG_ONE_SHOT);
//            PendingIntent pi = PendingIntent.getService(context, 0, new Intent(context, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
                AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
//            if (LocalSettingsActivity.cbStartUp.isChecked()) {
                Toast.makeText(context, "Khởi chạy thành công!", Toast.LENGTH_LONG).show();
                am.set(AlarmManager.RTC_WAKEUP,
                        System.currentTimeMillis() + sharedPreferences.getLong("share_startdelayduration", 10) * 1000, pi);
//            }
        }
    }
}

