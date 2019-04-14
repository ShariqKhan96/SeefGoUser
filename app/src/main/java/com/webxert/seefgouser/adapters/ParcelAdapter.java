package com.webxert.seefgouser.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.webxert.seefgouser.R;
import com.webxert.seefgouser.models.Parcel;

import java.util.ArrayList;
import java.util.List;

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
            myVH.status.setBackground(ContextCompat.getDrawable(context, R.drawable.declined));
            myVH.status.setText("Order placed");
            myVH.status.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        } else if (parcel.getStatus().equals("1")) {
            myVH.status.setBackground(ContextCompat.getDrawable(context, R.drawable.accept_round_view));
            myVH.status.setText("Driver Assigned");
            myVH.status.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        } else if (parcel.getStatus().equals("2")) {
            myVH.status.setBackground(ContextCompat.getDrawable(context, R.drawable.pending_round_view));
            myVH.status.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            myVH.status.setText("In transit");

        } else {
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

        public MyVH(@NonNull View itemView) {
            super(itemView);
            status = itemView.findViewById(R.id.status);
            source = itemView.findViewById(R.id.source);
            destination = itemView.findViewById(R.id.destination);
            itemname = itemView.findViewById(R.id.itemname);
            date = itemView.findViewById(R.id.date);
            price = itemView.findViewById(R.id.price);


        }
    }
}
