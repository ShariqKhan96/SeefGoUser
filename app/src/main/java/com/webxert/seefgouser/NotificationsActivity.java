package com.webxert.seefgouser;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.webxert.seefgouser.adapters.NotificaitonAdapter;
import com.webxert.seefgouser.models.Notification;

import java.util.ArrayList;
import java.util.List;

public class NotificationsActivity extends AppCompatActivity {

    RecyclerView notificationsList;
    NotificaitonAdapter adapter;
    List<Notification> notifications = new ArrayList<>();
    FrameLayout no_records;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parcels);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView toolbarTv = toolbar.findViewById(R.id.toolbarText);
        toolbarTv.setText("Parcel Details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        no_records = findViewById(R.id.no_records);
        notificationsList = findViewById(R.id.parcels_list);
        notificationsList.setLayoutManager(new LinearLayoutManager(this));
        //notificationsList.setAdapter(new NotificaitonAdapter(notifications, this));
        getData();

    }

    private void getData() {
        if (notifications.size() > 0)
            notificationsList.setAdapter(new NotificaitonAdapter(notifications, this));
        else no_records.setVisibility(View.VISIBLE);
    }
}
