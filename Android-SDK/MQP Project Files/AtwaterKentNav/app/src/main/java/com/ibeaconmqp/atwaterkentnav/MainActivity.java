package com.ibeaconmqp.atwaterkentnav;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

import com.estimote.sdk.Utils;
import com.estimote.sdk.SystemRequirementsChecker;
import com.estimote.sdk.cloud.model.Color;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;
import com.ibeaconmqp.atwaterkentnav.estimote.BeaconNotificationsManager;
import com.ibeaconmqp.atwaterkentnav.estimote.BeaconID;
import com.ibeaconmqp.atwaterkentnav.estimote.EstimoteCloudBeaconDetails;
import com.ibeaconmqp.atwaterkentnav.estimote.EstimoteCloudBeaconDetailsFactory;
import com.ibeaconmqp.atwaterkentnav.estimote.ProximityContentManager;

import org.ejml.ops.CommonOps;
import org.ejml.ops.NormOps;
import org.ejml.simple.*;
import org.ejml.ops.CommonOps.*;
import org.ejml.ops.NormOps.*;

import java.io.FileDescriptor;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static java.lang.Math.abs;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private Button userLocation;
    //distances to the beacons

    private double distance1,distance2,distance3;

    private BeaconManager beaconManager;
    private Region region;
    private boolean beaconNotificationsEnabled = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //this.setTitle(R.string.instructions);

        setContentView(R.layout.activity_main);

        beaconManager = new BeaconManager(this);
        beaconManager.setRangingListener(new BeaconManager.RangingListener() {
            @Override
            public void onBeaconsDiscovered(Region region, List<Beacon> list) {
                String text;
                Beacon firstBeacon, secondBeacon, thirdBeacon;
                //Only one beacon in range
                if (list.size()==1) {
                    firstBeacon = list.get(0);
                    distance1 = Utils.computeAccuracy(firstBeacon);

                    text = (Integer.toString(firstBeacon.getMajor()) + ": " + Double.toString((double)Math.round(distance1 * 100d) / 100d) + " Only 1 beacon in range");
                }

                else if(list.size()==2){
                    //Only two beacons in range
                    firstBeacon = list.get(0);
                    distance1 = Utils.computeAccuracy(firstBeacon);

                    secondBeacon = list.get(1);
                    distance2 = Utils.computeAccuracy(secondBeacon);

                    text = (Integer.toString(firstBeacon.getMajor()) + ": " + Double.toString((double)Math.round(distance1 * 100d) / 100d) + ", \n" +
                            Integer.toString(secondBeacon.getMajor()) + ": " +Double.toString((double)Math.round(distance2 * 100d) / 100d) + " Only 2 beacons in range.");
                }

                else if(list.size()>=3){

                    firstBeacon = list.get(0);
                    distance1 = Utils.computeAccuracy(firstBeacon);

                    secondBeacon = list.get(1);
                    distance2 = Utils.computeAccuracy(secondBeacon);

                    thirdBeacon = list.get(2);
                    distance3 = Utils.computeAccuracy(thirdBeacon);

                    //***************Start LMS Functionality*****************************
                    LMSBeacon[] beacons = new LMSBeacon[3]; //set the size to 3 at first
                    beacons[0]=null;
                    beacons[1]=null;
                    beacons[2]=null;
                    int numBeacons = beacons.length;

                    //create Beacons
                    LMSBeacon beacon1 = new LMSBeacon(-45, 0);//Set this as candy with major 20303
                    LMSBeacon beacon2 = new LMSBeacon(-40, -8);//Set this as beetroot with 8897
                    LMSBeacon beacon3 = new LMSBeacon(-30, 0);//Set this as lemon with 61665
                    LMSBeacon beacon4 = new LMSBeacon(-40, -23);//Set this as candy2 with major 12070
                    LMSBeacon beacon5 = new LMSBeacon(-30, -30);//Set this as beetroot2 with 53500
                    LMSBeacon beacon6 = new LMSBeacon(-37, -15);//Set this as lemon2 with 34226
                    LMSBeacon beacon7 = new LMSBeacon(-53, -28);//Set this as candy3 with major 11911
                    LMSBeacon beacon8 = new LMSBeacon(-60, -30);//Set this as beetroot3 with 48542
                    LMSBeacon beacon9 = new LMSBeacon(-45, -30);//Set this as lemon3 with 22098

                    //array of LMSBeacon objects

                    //add Beacons to array, based on Major and Minor
                    //First closest Beacon
                    int firstMajor = firstBeacon.getMajor();

                    if(firstMajor== 20303)
                        beacons[0] = beacon1;
                    else if(firstMajor== 8897)
                        beacons[0] = beacon2;
                    else if(firstMajor== 61665)
                        beacons[0] = beacon3;
                    else if(firstMajor== 12070) {
                        beacons[0] = beacon4;
                    }
                    else if(firstMajor== 53500) {
                        beacons[0] = beacon5;
                    }
                    else if(firstMajor== 34226) {
                        beacons[0] = beacon6;
                    }
                    else if(firstMajor== 11911) {
                        beacons[0] = beacon7;
                    }
                    else if(firstMajor== 48542) {
                        beacons[0] = beacon8;
                    }
                    else if(firstMajor== 22098) {
                        beacons[0] = beacon9;
                    }

                    //Second closest Beacon
                    int secondMajor = secondBeacon.getMajor();

                    if(secondMajor== 20303)
                        beacons[1] = beacon1;
                    else if(secondMajor== 8897)
                        beacons[1] = beacon2;
                    else if(secondMajor == 61665)
                        beacons[1] = beacon3;
                    else if(secondMajor == 12070)
                        beacons[1] = beacon4;
                    else if(secondMajor == 53500)
                        beacons[1] = beacon5;
                    else if(secondMajor == 34226)
                        beacons[1] = beacon6;
                    else if(secondMajor== 11911)
                        beacons[1] = beacon7;
                    else if(secondMajor== 48542)
                        beacons[1] = beacon8;
                    else if(secondMajor== 22098)
                        beacons[1] = beacon9;

                    //Third closest beacon
                    int thirdMajor = thirdBeacon.getMajor();

                    if(thirdMajor== 20303)
                        beacons[2] = beacon1;
                    else if(thirdMajor== 8897)
                        beacons[2] = beacon2;
                    else if(thirdMajor== 61665)
                        beacons[2] = beacon3;
                    else if(thirdMajor == 12070)
                        beacons[2] = beacon4;
                    else if(thirdMajor== 53500)
                        beacons[2] = beacon5;
                    else if(thirdMajor== 34226)
                        beacons[2] = beacon6;
                    else if(thirdMajor== 11911)
                        beacons[2] = beacon7;
                    else if(thirdMajor== 48542)
                        beacons[2] = beacon8;
                    else if(thirdMajor== 22098)
                        beacons[2] = beacon9;



                        //set initial guess
                        double guessX = beacons[0].getX();
                        double guessY = beacons[0].getY();
                        double[][] station = new double[][]{{guessX, guessY}};
                        SimpleMatrix matStation = new SimpleMatrix(station);
                        double[][] mo = new double[][]{{-1.0, -1.0}};
                        SimpleMatrix minusOne = new SimpleMatrix(mo);

                        //calculate the estimation error
                        double estimationError = 0;
                        double[] distances = {distance1, distance2, distance3};
                        for (int i = 0; i < numBeacons; i++) {
                            LMSBeacon thisBeacon = beacons[i];
                            //double d = getDistance(thisBeacon.getX(), thisBeacon.getY(), guessX, guessY);
                            double d = distances[i];
                            double f = abs(Math.pow(thisBeacon.getX() - guessX, 2) + Math.pow(thisBeacon.getY() - guessY, 2) - Math.pow(d, 2));
                            estimationError = estimationError + f;
                        }

                        //create a Jacobian matrix of size [number_of_beacons][2]
                        double[][] jacobianMatrix = new double[numBeacons][2];
                        double[][] matF = new double[numBeacons][1];
                        while (estimationError > 0.01) {
                            //for loop happens here
                            //the condition for, for loop ->
                            for (int i = 0; i < numBeacons; i++) { //3 is the number of beacons
                                //we calculate the jacobian matrix here
                                LMSBeacon b = beacons[i];
                                for (int j = 0; j < 2; j++) {
                                    if (j == 0) {
                                        jacobianMatrix[i][j] = -2 * (b.getX() - guessX);
                                    } else {
                                        jacobianMatrix[i][j] = -2 * (b.getY() - guessY);
                                    }
                                }
                                matF[i][0] = Math.pow(b.getX() - guessX, 2) + Math.pow(b.getY() - guessY, 2) - Math.pow(distances[i], 2);
                            }
                            SimpleMatrix matrixJacobian = new SimpleMatrix(jacobianMatrix);
                            SimpleMatrix matrixF = new SimpleMatrix(matF);
                            //here goes the matrix inverse operation
                            // estimationError = -inv(jacobianMatrix' * jacobianMatrix) * (jacobianMatrix') * F'
                            SimpleMatrix first = (matrixJacobian.transpose().mult(matrixJacobian)).invert();
                            SimpleMatrix second = (matrixJacobian.transpose().mult(matrixF));
                            SimpleMatrix matrixError = first.mult(second);
                            matrixError = matrixError.negative();
                            matStation = matStation.plus(matrixError.transpose());
                            estimationError = matrixError.elementSum();
                            if((int)abs(matStation.get(0,0))>80 || (int)abs(matStation.get(0,1))>80)//This line avoids nonconvergence
                                break;

                        }
                    //*******************End LMS Functionality*******************************************
                        //Creates a UserPosition to convert map coordinates to a position on map
                        UserPosition position = new UserPosition((int)abs(matStation.get(0,0)),(int)abs(matStation.get(0,1)));

                        userLocation = (Button)findViewById(R.id.userLocation);
                        userLocation.setX(position.getXpos());
                        userLocation.setY(position.getYpos());

                        //Displays the coordinates given by algorithm
                        TextView distance = (TextView)findViewById(R.id.math);
                        distance.setText(Double.toString(matStation.get(0,0)) + " ,  " +
                                Double.toString(matStation.get(0,1)));

                    //Displays the majors of three closest beacons, as well as the distance to them
                    text = (Integer.toString(firstMajor)+ ": " + Double.toString((double)Math.round(distance1 * 100d) / 100d)+ ", \n" +
                            Integer.toString(secondMajor)+ ": " +Double.toString((double)Math.round(distance2 * 100d) / 100d)+ ", \n" +
                            Integer.toString(thirdMajor)+ ": " +Double.toString((double)Math.round(distance3 * 100d) / 100d));
                    ((TextView) findViewById(R.id.textView)).setText(text);


                }

                else {
                    text = "No beacons in range.";
                }

                ((TextView) findViewById(R.id.textView)).setText(text);
                //((TextView) findViewById(R.id.distances)).setText(Double.toString(distance1));

            }
        });
        region = new Region("ranged region", UUID.fromString("6EE4D6A9-DD8E-550E-FF81-783E445F9C5B"), null, null);

        //Set the locations of each of the beacons, moving left to right and top to bottom
        Button beacon1 = (Button)findViewById(R.id.beacon1);
        beacon1.setX(200);
        beacon1.setY(410);

        Button beacon2 = (Button)findViewById(R.id.beacon2);
        beacon2.setX(295);
        beacon2.setY(385);

        Button beacon3 = (Button)findViewById(R.id.beacon3);
        beacon3.setX(360);
        beacon3.setY(160);

        Button beacon4 = (Button)findViewById(R.id.beacon4);
        beacon4.setX(360);
        beacon4.setY(400);

        Button beacon5 = (Button)findViewById(R.id.beacon5);
        beacon5.setX(370);
        beacon5.setY(230);

        Button beacon6 = (Button)findViewById(R.id.beacon6);
        beacon6.setX(370);
        beacon6.setY(320);

        Button beacon7 = (Button)findViewById(R.id.beacon7);
        beacon7.setX(420);
        beacon7.setY(275);

        Button beacon8 = (Button)findViewById(R.id.beacon8);
        beacon8.setX(470);
        beacon8.setY(170);

        Button beacon9 = (Button)findViewById(R.id.beacon9);
        beacon9.setX(470);
        beacon9.setY(410);

        Button beacon10 = (Button)findViewById(R.id.beacon10);
        beacon10.setX(520);
        beacon10.setY(190);

        Button beacon11 = (Button)findViewById(R.id.beacon11);
        beacon11.setX(520);
        beacon11.setY(390);

        Button beacon12 = (Button)findViewById(R.id.beacon12);
        beacon12.setX(570);
        beacon12.setY(170);

        Button beacon13 = (Button)findViewById(R.id.beacon13);
        beacon13.setX(570);
        beacon13.setY(410);

        Button beacon14 = (Button)findViewById(R.id.beacon14);
        beacon14.setX(670);
        beacon14.setY(230);

        Button beacon15 = (Button)findViewById(R.id.beacon15);
        beacon15.setX(670);
        beacon15.setY(320);

        Button beacon16 = (Button)findViewById(R.id.beacon16);
        beacon16.setX(720);
        beacon16.setY(170);

        Button beacon17 = (Button)findViewById(R.id.beacon17);
        beacon17.setX(720);
        beacon17.setY(275);

        Button beacon18 = (Button)findViewById(R.id.beacon18);
        beacon18.setX(720);
        beacon18.setY(410);

        Button beacon19 = (Button)findViewById(R.id.beacon19);
        beacon19.setX(780);
        beacon19.setY(385);

        Button beacon20 = (Button)findViewById(R.id.beacon20);
        beacon20.setX(840);
        beacon20.setY(410);

        init();

    }



    //coordinate 1: x1, y1; coordinate 2: x2, y2
    public static double getDistance(int x1, int y1, int x2, int y2) {
        double d = Math.sqrt(Math.pow(x2-x1, 2) + Math.pow(y2-y1, 2));
        return d;
    }

    //Notification toggling code is included in here
    @Override
    protected void onResume() {
        super.onResume();

        SystemRequirementsChecker.checkWithDefaultDialogs(this);

        //Button to toggle notifications
        final Button notifsOff = (Button)findViewById(R.id.notifsOff);
        notifsOff.setX(500);
        notifsOff.setY(1200);
        notifsOff.setBackgroundColor(0xFFFF0000);
        notifsOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                beaconNotificationsEnabled = !beaconNotificationsEnabled;
                if(beaconNotificationsEnabled){
                    notifsOff.setText("Notifications Off");
                    notifsOff.setBackgroundColor(0xFFFF0000);
                }
                else {
                    notifsOff.setText("Notifications On");
                    notifsOff.setBackgroundColor(0xFF00FF00);
                }
            }
        });

        if (!isBeaconNotificationsEnabled()) {
            Log.d(TAG, "Enabling beacon notifications");
            enableBeaconNotifications();
        }
        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                beaconManager.startRanging(region);

            }
        });
    }

    @Override
    protected void onPause() {
        beaconManager.stopRanging(region);

        super.onPause();
    }

    //Creates android notifications when the user enters the range of beacons
    public void enableBeaconNotifications() {
        if (beaconNotificationsEnabled) { return; }

        BeaconNotificationsManager beaconNotificationsManager = new BeaconNotificationsManager(this);
        beaconNotificationsManager.addNotification(//Beetroot 3
                new BeaconID("6EE4D6A9-DD8E-550E-FF81-783E445F9C5B",48542,60126),
                "Room 320 is the CWINS lab",
                null);
        beaconNotificationsManager.startMonitoring();

        beaconNotificationsEnabled = true;
    }

    public boolean isBeaconNotificationsEnabled() {
        return beaconNotificationsEnabled;
    }

    //Method to change screens when a button is clicked
    private void init(){
        Button switchScreen = (Button)findViewById(R.id.switchScreen);
        switchScreen.setX(500);
        switchScreen.setY(900);
        switchScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nextScreen = new Intent(getApplicationContext(), ProfessorTableActivity.class);
                startActivity(nextScreen);
            }
        });

    }

}
