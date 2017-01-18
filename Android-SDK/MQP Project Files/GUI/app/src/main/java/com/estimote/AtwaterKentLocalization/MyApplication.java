package com.estimote.AtwaterKentLocalization;

import android.app.Application;

import com.estimote.sdk.EstimoteSDK;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;

import java.util.UUID;

//
// Running into any issues? Drop us an email to: contact@estimote.com
//


public class MyApplication extends Application {

    private BeaconManager beaconManager;
    @Override
    public void onCreate() {
        super.onCreate();

        beaconManager = new BeaconManager(getApplicationContext());

        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                beaconManager.startMonitoring(new Region("monitored region",
                        UUID.fromString("B9407F30-F5F8-466E-AFF9-25556B57FE6D"), 20303, 16948));
            }
        });


        // TODO: put your App ID and App Token here
        // You can get them by adding your app on https://cloud.estimote.com/#/apps
        EstimoteSDK.initialize(getApplicationContext(), "<#App ID#>", "<#App Token#>");

        // uncomment to enable debug-level logging
        // it's usually only a good idea when troubleshooting issues with the Estimote SDK
//        EstimoteSDK.enableDebugLogging(true);
    }
}
