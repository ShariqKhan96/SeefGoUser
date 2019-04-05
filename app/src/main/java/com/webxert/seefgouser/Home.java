package com.webxert.seefgouser;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.webxert.seefgouser.common.Common;
import com.webxert.seefgouser.common.ConstantManager;
import com.webxert.seefgouser.interfaces.ProceedVisibilityListener;
import com.webxert.seefgouser.models.User;
import com.webxert.seefgouser.models.Warehouse;
import com.webxert.seefgouser.network.VolleySingleton;
import com.webxert.seefgouser.services.LocationService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.paperdb.Paper;

public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback, ProceedVisibilityListener {
    private static final int REQUEST = 100;
    private GoogleMap mMap;
    private static final int REQUEST_CHECK_SETTINGS = 1000;
    //    PlaceAutocompleteFragment dropOffET;
//    Place dropOffPlace;
    String warehouse_1_Names[];
    String warehouse_Ids[];
    //String warehouse_2_Names[] = {"Karachi", "Lahore", "Islamabad", "Multan", "Kashmir"};

    FrameLayout proceedBtn, wareHouse1Btn, warehouse2Btn;
    TextView warehouse1TV;
    TextView warehouse2TV;
    ProceedVisibilityListener proceedVisibilityListener;
    String selectedFromWarehouse, selectedToWareshouse, source, destination;
    private TextView name;
    private TextView email;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initUi();
        registerCallBacks();

        getWarehouses();

        proceedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home.this, FormActivity.class);
                intent.putExtra("warehouse_one", selectedFromWarehouse);
                intent.putExtra("warehouse_two", selectedToWareshouse);
                intent.putExtra("source", source);
                intent.putExtra("destination", destination);
                startActivity(intent);


            }
        });
        wareHouse1Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(Home.this);
                dialog.setTitle("Select Warehouse");
                dialog.setItems(warehouse_1_Names, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        source = warehouse_Ids[i];
                        selectedFromWarehouse = warehouse_1_Names[i];
                        warehouse1TV.setText(warehouse_1_Names[i]);
                        dialogInterface.dismiss();

                    }
                });
                dialog.show();
            }
        });
        warehouse2Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(Home.this);
                dialog.setTitle("Select Warehouse");
                dialog.setItems(warehouse_1_Names, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        destination = warehouse_Ids[i];
                        selectedToWareshouse = warehouse_1_Names[i];
                        warehouse2TV.setText(warehouse_1_Names[i]);
                        dialogInterface.dismiss();

                        if ((!selectedFromWarehouse.isEmpty() && !selectedFromWarehouse.equals("")) &&
                                (!selectedToWareshouse.isEmpty() && !selectedToWareshouse.equals(""))) {
                            proceedVisibilityListener.onFieldsFilled();
                        }


                    }
                });
                dialog.show();
            }
        });
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);
        name = headerView.findViewById(R.id.name);
        email = headerView.findViewById(R.id.email);

        name.setText(getSharedPreferences(ConstantManager.SHARED_PREFERENCES, MODE_PRIVATE).getString(ConstantManager.NAME, "Shariq Khan"));
        email.setText(getSharedPreferences(ConstantManager.SHARED_PREFERENCES, MODE_PRIVATE).getString(ConstantManager.EMAIL, "Shariqmack@gmail.com"));


//
//        dropOffET = (PlaceAutocompleteFragment) getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
//        dropOffET.getView().findViewById(R.id.place_autocomplete_search_button).setVisibility(View.GONE);
//        ((EditText) dropOffET.getView().findViewById(R.id.place_autocomplete_search_input)).setHint("Select drop off location");
//        ((EditText) dropOffET.getView().findViewById(R.id.place_autocomplete_search_input)).setTextSize(18);
//
//
//        dropOffET.setOnPlaceSelectedListener(new PlaceSelectionListener() {
//            @Override
//            public void onPlaceSelected(Place place) {
//                ((EditText) dropOffET.getView().findViewById(R.id.place_autocomplete_search_input)).setPadding(10, -10, 10, -10);
//                dropOffPlace = place;
//                Log.e("PlacePicked: ", place.getAddress().toString());
//            }
//
//            @Override
//            public void onError(Status status) {
//                Toast.makeText(Home.this, "Error :" + status.getStatusMessage(), Toast.LENGTH_SHORT).show();
//
//            }
//        });
    }

    private void getWarehouses() {
        final ProgressDialog dialog = new ProgressDialog(Home.this, R.style.MyAlertDialogStyle);
        dialog.setTitle("Getting warehouses");
        dialog.setMessage("Please Wait");
        dialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ConstantManager.BASE_URL + "warehouse.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        dialog.dismiss();

                        try {
                            JSONArray array = new JSONArray(response);
                            warehouse_1_Names = new String[array.length()];
                            warehouse_Ids = new String[array.length()];
                            for (int i = 0; i < array.length(); i++) {
                                Warehouse warehouse = new Gson().fromJson(array.getJSONObject(i).toString(), Warehouse.class);
                                warehouse_1_Names[i] = warehouse.getWarehouse_name();
                                warehouse_Ids[i] = warehouse.getWarehouse_id();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
//                        Gson gson = new Gson();
//                        Type listType = new TypeToken<List<Warehouse>>() {
//                        }.getType();
//                        List<Warehouse> warehouses = gson.fromJson(response, listType);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dialog.dismiss();
                        Toast.makeText(Home.this, "" + error.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                    }
                }) {

        };
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    private void checkForPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            //Toast.makeText(this, "Permission already Granted", Toast.LENGTH_SHORT).show();
            displayLocationSettingsRequest(this);
            Log.e("checkForPermissions", "Here");

            return;
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
                displayLocationSettingsRequest(this);
                Log.e("onPermissionsResult", "Here");
            } else {
                Toast.makeText(this, "Permission denied!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void displayLocationSettingsRequest(Context context) {
        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API).build();
        googleApiClient.connect();

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(10000 / 2);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        Log.e("Here Success", "All location settings are satisfied.");
                        Intent intent = new Intent(Home.this, LocationService.class);
                        startService(intent);
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        Log.e(Home.class.getSimpleName(), "Location settings are not satisfied. Show the user a dialog to upgrade location settings ");

                        try {
                            // Show the dialog by calling startResolutionForResult(), and check the result
                            // in onActivityResult().
                            status.startResolutionForResult(Home.this, REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                            Log.e(Home.class.getSimpleName(), "PendingIntent unable to execute request.");
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        Log.i(Home.class.getSimpleName(), "Location settings are inadequate, and cannot be fixed here. Dialog not created.");
                        break;
                }
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CHECK_SETTINGS) {
            Toast.makeText(this, "Location on", Toast.LENGTH_SHORT).show();
        }
    }

    private void registerCallBacks() {
        proceedVisibilityListener = this;
    }


    private void initUi() {
        warehouse1TV = findViewById(R.id.warehouse_1TV);
        warehouse2TV = findViewById(R.id.warehouse_2TV);
        proceedBtn = findViewById(R.id.proceed_btn);
        wareHouse1Btn = findViewById(R.id.warehouse_1);
        warehouse2Btn = findViewById(R.id.warehouse_2);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            AlertDialog.Builder builder = new AlertDialog.Builder(Home.this);
            builder.setTitle("Confirmation");
            builder.setMessage("Are you sure?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Common.resetPrefs(Home.this);
                    Intent intent = new Intent(Home.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    dialog.dismiss();
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();

                }
            });
            builder.show();


            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_packages) {
            Intent intent = new Intent(this, ParcelsActivity.class);
            startActivity(intent);
            // Handle the camera action
        } else if (id == R.id.nav_notificaitons) {
//            Intent intent = new Intent(this, NotificationsActivity.class);
//            startActivity(intent);
            Toast.makeText(this, "Will be added later", Toast.LENGTH_SHORT).show();

        } else if (id == R.id.nav_profile) {
            Intent intent = new Intent(this, ProfileActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_help) {

        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ConstantManager.CURRENT_LATLNG == null) {
            final ProgressDialog dialog = new ProgressDialog(Home.this);
            dialog.setTitle("Please Wait");
            dialog.setMessage("Getting your location");
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    dialog.dismiss();
                    if (ConstantManager.CURRENT_LATLNG == null) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(Home.this);
                        builder.setTitle("Warning");
                        int permission = ContextCompat.checkSelfPermission(Home.this, Manifest.permission.ACCESS_FINE_LOCATION);
                        if (permission == PackageManager.PERMISSION_GRANTED)
                            builder.setMessage("App will misbehave due to denial of requested permissions completely!");
                        else builder.setMessage("Slow internet connection");
                        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialog.dismiss();
                            }
                        });

                        builder.show();

                    } else {

                        LatLng sydney = new LatLng(ConstantManager.CURRENT_LATLNG.latitude, ConstantManager.CURRENT_LATLNG.longitude);
                        mMap.addMarker(new MarkerOptions().position(sydney).title("You"));
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney, 12.0f));
                    }

                }
            }, 0);
        } else {
            LatLng sydney = new LatLng(ConstantManager.CURRENT_LATLNG.latitude, ConstantManager.CURRENT_LATLNG.longitude);
            mMap.addMarker(new MarkerOptions().position(sydney).title("You"));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney, 12.0f));
        }
    }

    @Override
    public void onFieldsFilled() {
        proceedBtn.setVisibility(View.VISIBLE);
    }
}
