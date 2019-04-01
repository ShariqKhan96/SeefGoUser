package com.webxert.seefgouser;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.webxert.seefgouser.common.Common;
import com.webxert.seefgouser.interfaces.ProceedVisibilityListener;

public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback, ProceedVisibilityListener {

    private GoogleMap mMap;
    //    PlaceAutocompleteFragment dropOffET;
//    Place dropOffPlace;
    String warehouse_1_Names[] = {"Karachi", "Lahore", "Islamabad", "Multan", "Kashmir"};
    String warehouse_2_Names[] = {"Karachi", "Lahore", "Islamabad", "Multan", "Kashmir"};
    FrameLayout proceedBtn, wareHouse1Btn, warehouse2Btn;
    TextView warehouse1TV;
    TextView warehouse2TV;
    ProceedVisibilityListener proceedVisibilityListener;
    String selectedFromWarehouse, selectedToWareshouse;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initUi();
        registerCallBacks();
        proceedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home.this, FormActivity.class);
                intent.putExtra("warehouse_one", selectedFromWarehouse);
                intent.putExtra("warehouse_two", selectedToWareshouse);
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
                        selectedFromWarehouse = warehouse_1_Names[i];
                        warehouse1TV.setText(selectedFromWarehouse);
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
                dialog.setItems(warehouse_2_Names, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        selectedToWareshouse = warehouse_2_Names[i];
                        warehouse2TV.setText(selectedToWareshouse);
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
            Common.resetPrefs(this);
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
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
            // Handle the camera action
        } else if (id == R.id.nav_notificaitons) {

        } else if (id == R.id.nav_profile) {

        } else if (id == R.id.nav_help) {

        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    @Override
    public void onFieldsFilled() {
        proceedBtn.setVisibility(View.VISIBLE);
    }
}
