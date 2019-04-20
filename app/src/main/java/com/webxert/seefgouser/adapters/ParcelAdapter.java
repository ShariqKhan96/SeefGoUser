package com.webxert.seefgouser.adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.webxert.seefgouser.R;
import com.webxert.seefgouser.common.ConstantManager;
import com.webxert.seefgouser.models.Parcel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hp on 4/1/2019.
 */

public class ParcelAdapter extends RecyclerView.Adapter<ParcelAdapter.MyVH> {
    List<Parcel> parcels = new ArrayList<>();
    Context context;

    public ParcelAdapter(List<Parcel> parcels, Context context) {
        this.parcels = parcels;
        this.context = context;
    }

    @NonNull
    @Override
    public MyVH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new MyVH(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.parcel_layout, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyVH myVH, int i) {
        Parcel parcel = parcels.get(i);
        myVH.price.setText(parcel.getPackage_price());
        myVH.source.setText("From: " + parcel.getStart_point());
        myVH.destination.setText("To: " + parcel.getEnd_point());
        myVH.date.setText(parcel.getDate_time());
        myVH.itemname.setText(parcel.getPackage_name());

        if (parcel.getStatus().equals("0")) {
            myVH.cancel.setVisibility(View.VISIBLE);
            myVH.status.setBackground(ContextCompat.getDrawable(context, R.drawable.declined));
            myVH.status.setText("Order placed");
            myVH.status.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        } else if (parcel.getStatus().equals("1")) {
            myVH.cancel.setVisibility(View.GONE);
            myVH.status.setBackground(ContextCompat.getDrawable(context, R.drawable.accept_round_view));
            myVH.status.setText("Driver Assigned");
            myVH.status.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        } else if (parcel.getStatus().equals("2")) {
            myVH.cancel.setVisibility(View.GONE);
            myVH.status.setBackground(ContextCompat.getDrawable(context, R.drawable.pending_round_view));
            myVH.status.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            myVH.status.setText("In transit");

        } else {
            myVH.cancel.setVisibility(View.GONE);
            myVH.status.setBackground(ContextCompat.getDrawable(context, R.drawable.accept_round_view));
            myVH.status.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            myVH.status.setText("Parcel delivered");
        }


    }

    @Override
    public int getItemCount() {
        return parcels.size();
    }

    public class MyVH extends RecyclerView.ViewHolder {
        TextView source, destination, itemname, date, price, status;
        FrameLayout cancel;


        public MyVH(@NonNull View itemView) {
            super(itemView);
            cancel = itemView.findViewById(R.id.cancel_order);
            status = itemView.findViewById(R.id.status);
            source = itemView.findViewById(R.id.source);
            destination = itemView.findViewById(R.id.destination);
            itemname = itemView.findViewById(R.id.itemname);
            date = itemView.findViewById(R.id.date);
            price = itemView.findViewById(R.id.price);

            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    cancelOrder(getAdapterPosition());
                }
            });

        }
    }

    private void cancelOrder(final int adapterPosition) {
        final ProgressDialog dialog = new ProgressDialog(context);
        dialog.setTitle("");
        dialog.setMessage("Please wait..");
        dialog.show();

        StringRequest request = new StringRequest(Request.Method.POST, ConstantManager.BASE_URL + "cancel.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        dialog.dismiss();
                        try {
                            JSONObject root = new JSONObject(response);
                            if (root.getString("status").equals("1")) {
                                Toast.makeText(context, "Parcel Cancelled", Toast.LENGTH_SHORT).show();

                                Parcel parcel = parcels.get(adapterPosition);
                                parcels.remove(parcel);
                                notifyItemRemoved(adapterPosition);
                            } else {
                                Toast.makeText(context, "" + root.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            Toast.makeText(context, "" + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "" + error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                // map.put("id", parcels.get(adapterPosition).)
                return map;
            }
        };
        Volley.newRequestQueue(context).add(request);
    }
}
