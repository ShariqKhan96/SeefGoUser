package com.webxert.seefgouser.services;

import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.FirebaseDatabase;
import com.webxert.seefgouser.common.ConstantManager;

import java.sql.Driver;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by hp on 4/11/2019.
 */

public class LocationUpdatePriveder extends Service {
    private static final String TAG = "LocationService";
    LocationRequest request;
    FusedLocationProviderClient client;
    LocationCallback locationCallback;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("onCreate", "onCreate");
//        String client_id = getSharedPreferences(ConstantManager.SHARED_PREFERENCES, MODE_PRIVATE).getString("client_id", "01");
//        String emp_id = getSharedPreferences(ConstantManager.SHARED_PREFERENCES, MODE_PRIVATE).getString("employee_id", "01");

        requestLocationUpdates();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        client.removeLocationUpdates(locationCallback);
        stopSelf();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    private void requestLocationUpdates() {
        request = new LocationRequest();
        // Random r = new Random();
        int intervalTime = ThreadLocalRandom.current().nextInt(5000, 7000);
        // int intervalTime = r.nextInt(7000 - 5000) + 5000;
        int fastestInterval = intervalTime - ThreadLocalRandom.current().nextInt(2000, 3000);
        Log.e("intervals", "fastest " + fastestInterval + " intervalTime " + intervalTime);
        request.setInterval(intervalTime);

        request.setFastestInterval(fastestInterval);
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        request.setSmallestDisplacement(10f);
        client = LocationServices.getFusedLocationProviderClient(this);


        int permission = ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION);


        if (permission == PackageManager.PERMISSION_GRANTED) {

            client.requestLocationUpdates(request, new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {

                    locationCallback = this;
                    Location location = locationResult.getLastLocation();
                    Log.e(TAG, (location.getProvider()));
                    if (location != null) {
                        ConstantManager.CURRENT_LATLNG = new LatLng(location.getLatitude(), location.getLongitude());
                        Log.e(TAG, "location update " + ConstantManager.CURRENT_LATLNG.toString());

                        pushLocationToFirebase(location);
//                        geoFire.setLocation("You", new GeoLocation(location.getLatitude(), location.getLongitude()), new GeoFire.CompletionListener() {
//                            @Override
//                            public void onComplete(String key, DatabaseError error) {
//                                Log.e("PushedValue", key);
//                            }
//                        });

                    }
                }
            }, Looper.myLooper());
        } else {
            Toast.makeText(this, "Turn on the device location from device settings!!", Toast.LENGTH_SHORT).show();
        }


    }

    private void pushLocationToFirebase(Location location) {


    }


}
