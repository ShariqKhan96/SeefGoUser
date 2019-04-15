package com.webxert.seefgouser;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.webxert.seefgouser.common.ConstantManager;
import com.webxert.seefgouser.models.DriverLatLng;

import java.util.HashMap;

public class TrackActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    String s_lat, s_lng, d_lat, d_lng, driver_id;
    HashMap<String, Marker> markers = new HashMap<>();
    ProgressDialog dialog;
    ValueEventListener valueEventListener;
    DatabaseReference driversRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

        dialog = new ProgressDialog(this);
        dialog.setTitle("Setting up the tracking");
        dialog.setMessage("Please Wait");
        dialog.show();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        s_lat = getIntent().getStringExtra("start_lat");
        s_lng = getIntent().getStringExtra("start_long");
        d_lat = getIntent().getStringExtra("end_lat");
        d_lng = getIntent().getStringExtra("end_long");
        driver_id = getIntent().getStringExtra("driver_id");

        driversRef = FirebaseDatabase.getInstance().getReference(ConstantManager.DRIVER_DB_NAME).child(driver_id);

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        // Add a marker in Sydney and move the camera
        final LatLng source = new LatLng(Double.parseDouble(s_lat), Double.parseDouble(s_lng));
        final LatLng destination = new LatLng(Double.parseDouble(d_lat), Double.parseDouble(d_lng));
        markers.put("SOURCE", mMap.addMarker(new MarkerOptions().position(source).title("Source")));
        markers.put("DEST", mMap.addMarker(new MarkerOptions().position(destination).title("Destination")));
        Log.e("KEY", FirebaseDatabase.getInstance().getReference(ConstantManager.DRIVER_DB_NAME).getKey());

        Log.e("DRIVER_ID", driver_id);

        driversRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dialog.isShowing())
                    dialog.dismiss();
                valueEventListener = this;
                Log.e("datasnapshot", dataSnapshot.toString());
                DriverLatLng latLng = dataSnapshot.getValue(DriverLatLng.class);
                // LatLng lll = dataSnapshot.getValue(LatLng.class);
                LatLng d_latlng = new LatLng(latLng.getLatitude(), latLng.getLongitude());
                //mMap.clear();
                if (!markers.containsKey(driver_id)) {
                    markers.put(driver_id, mMap.addMarker(new MarkerOptions().position(d_latlng).title("Driver").snippet(driver_id).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))));

                } else markers.get(driver_id).setPosition(d_latlng);
                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                for (Marker marker : markers.values()) {
                    builder.include(marker.getPosition());
                }
                mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 300));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        driversRef.removeEventListener(valueEventListener);
    }
}
