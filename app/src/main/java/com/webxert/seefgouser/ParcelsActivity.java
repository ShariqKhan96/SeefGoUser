package com.webxert.seefgouser;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.webxert.seefgouser.adapters.ParcelAdapter;
import com.webxert.seefgouser.models.Parcel;

import java.util.ArrayList;
import java.util.List;

public class ParcelsActivity extends AppCompatActivity {

    RecyclerView parcelList;
    List<Parcel> parcels = new ArrayList<>();
    ParcelAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parcels);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView toolbarTv = toolbar.findViewById(R.id.toolbarText);
        toolbarTv.setText("Parcel History");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        parcelList = findViewById(R.id.parcels_list);
        parcelList.setLayoutManager(new LinearLayoutManager(this));
        parcelList.setAdapter(new ParcelAdapter(parcels, this));


    }

    private List<Parcel> getList() {
        return null;
    }
}
