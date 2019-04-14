package com.webxert.seefgouser;

import android.app.ProgressDialog;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.webxert.seefgouser.adapters.NotificaitonAdapter;
import com.webxert.seefgouser.adapters.ParcelAdapter;
import com.webxert.seefgouser.common.ConstantManager;
import com.webxert.seefgouser.models.Notification;
import com.webxert.seefgouser.models.Parcel;
import com.webxert.seefgouser.models.TrackOrder;
import com.webxert.seefgouser.models.User;
import com.webxert.seefgouser.network.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.paperdb.Paper;

public class NotificationsActivity extends AppCompatActivity {

    RecyclerView notificationsList;
    NotificaitonAdapter adapter;
    List<Notification> notifications = new ArrayList<>();
    FrameLayout no_records;
    User user;
    SwipeRefreshLayout swipe_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parcels);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView toolbarTv = toolbar.findViewById(R.id.toolbarText);
        toolbarTv.setText("Your Orders");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        no_records = findViewById(R.id.no_records);
        swipe_layout = findViewById(R.id.swipe_layout);
        int Colors[] = {android.R.color.holo_red_dark, android.R.color.holo_orange_light};
        swipe_layout.setColorSchemeResources(Colors);
        user = Paper.book().read(ConstantManager.CURRENT_USER);
        notificationsList = findViewById(R.id.parcels_list);
        notificationsList.setLayoutManager(new LinearLayoutManager(this));
        //notificationsList.setAdapter(new NotificaitonAdapter(notifications, this));
        getData();

        swipe_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipe_layout.setRefreshing(true);
                getData();
            }
        });

    }

    private void getData() {
        final ProgressDialog dialog = new ProgressDialog(NotificationsActivity.this, R.style.MyAlertDialogStyle);
        dialog.setTitle("Getting Orders");
        dialog.setMessage("Please Wait");
        // dialog.show();

        swipe_layout.setRefreshing(true);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ConstantManager.BASE_URL + "trackorder.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //dialog.dismiss();
                        try {
                            swipe_layout.setRefreshing(false);
                            Log.e("orders: ", response);
                            JSONArray array = new JSONArray(response);
                            if (array.length() > 0) {
                                Gson gson = new Gson();
                                Type listType = new TypeToken<List<TrackOrder>>() {
                                }.getType();
                                List<TrackOrder> parcels = gson.fromJson(response, listType);
                                notificationsList.setAdapter(new NotificaitonAdapter(parcels, NotificationsActivity.this));

                            } else {
                                no_records.setVisibility(View.VISIBLE);
                            }
                        } catch (JSONException e) {
                            Log.e("JSONException", e.getMessage());
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dialog.dismiss();
                        Toast.makeText(NotificationsActivity.this, "" + error.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("id", user.getUser_id());
                return map;
            }
        };
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }
}
