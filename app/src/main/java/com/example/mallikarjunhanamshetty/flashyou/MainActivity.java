package com.example.mallikarjunhanamshetty.flashyou;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;



public class MainActivity extends AppCompatActivity {

    public static final int MY_PERMISSIONS_REQUEST_CAMERA = 100;
    private CameraManager mCameraManager;
    private String mCameraId;
    private Button mTorchOnOffButton;
    private Button sosButton;
    private Boolean isTorchOn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTorchOnOffButton = (Button) findViewById(R.id.mTorchOnOffButton);
        sosButton =(Button) findViewById(R.id.sos);
        isTorchOn= false;
        Boolean isFlashAvailable = getApplicationContext().getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);

        if (!isFlashAvailable) {

            AlertDialog alert = new AlertDialog.Builder(MainActivity.this)
                    .create();
            alert.setTitle("Error !!");
            alert.setMessage("Your device doesn't support flash light!");
            alert.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // closing the application
                    finish();
                    System.exit(0);
                }
            });
            alert.show();
            return;
        }

        else
        {
            mCameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
            try {
                mCameraId = mCameraManager.getCameraIdList()[0];
            } catch (Exception e) {
                e.printStackTrace();
            }


            mTorchOnOffButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    /*
                    Can be made as utility method to avoid execution everytime

                     */

                    checkAndRequestPermissions(MainActivity.this);




                    try {
                        if (isTorchOn) {
                            turnOffFlashLight();
                            isTorchOn = false;
                        } else {
                            turnOnFlashLight();
                            isTorchOn = true;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });



        }

        sosButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sosMode();
            }
        });


    }



    public void sosMode()
    {
        String myString = "0101010101";
        long blinkDelay = 50; //Delay in ms
        for (int i = 0; i < myString.length(); i++) {
            try {
                if (myString.charAt(i) == '0') {
                    mCameraManager.setTorchMode(mCameraId, true);
                } else {
                    mCameraManager.setTorchMode(mCameraId, false);
                }
                try {
                    Thread.sleep(blinkDelay);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }

        }

    }


    public void turnOnFlashLight() {

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                mCameraManager.setTorchMode(mCameraId, true);

                mTorchOnOffButton.setBackgroundResource(R.drawable.red);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




    public void turnOffFlashLight() {

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                mCameraManager.setTorchMode(mCameraId, false);

                mTorchOnOffButton.setBackgroundResource(R.drawable.green2);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private  void checkAndRequestPermissions(Activity activity) {

        if (ContextCompat.checkSelfPermission(activity,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                    Manifest.permission.CAMERA)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(activity,
                        new String[]{Manifest.permission.CAMERA},
                        MY_PERMISSIONS_REQUEST_CAMERA);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CAMERA: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }



}

