package com.pmovil.soccer;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.ToggleButton;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class SettingNotificationActivity extends Activity {

    public RelativeLayout containerBanner;
    private ToggleButton soundCheckBox;
    private ToggleButton vibrationCheckBox;
    private AdView bannerAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_notification);
        soundCheckBox = (ToggleButton) findViewById(R.id.sound_notification_checkbox);
        vibrationCheckBox = (ToggleButton) findViewById(R.id.vibration_notification_checkbox);
        View view = findViewById(R.id.iv_menu_icon_actionbar);
        if (view != null)
            view.setVisibility(View.INVISIBLE);

        containerBanner = (RelativeLayout) findViewById(R.id.container_banner);

        //ADMOB
        bannerAdView = (AdView) findViewById(R.id.bannerView);
        // Create an ad request. Check logcat output for the hashed device ID to
        // get test ads on a physical device.
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        // Start loading the ad in the background.
        bannerAdView.loadAd(adRequest);

        soundCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                GoogleCloudMessagingUtilities.setSoundNotification(
                        getApplicationContext(), isChecked);
            }
        });
        vibrationCheckBox
                .setOnCheckedChangeListener(new OnCheckedChangeListener() {

                    public void onCheckedChanged(CompoundButton buttonView,
                                                 boolean isChecked) {
                        GoogleCloudMessagingUtilities.setVibrationNotification(
                                getApplicationContext(), isChecked);

                    }
                });
        soundCheckBox.setChecked(GoogleCloudMessagingUtilities
                .isSoundNotification(getApplicationContext()));
        vibrationCheckBox.setChecked(GoogleCloudMessagingUtilities
                .isVibrationNotification(getApplicationContext()));

    }

}
