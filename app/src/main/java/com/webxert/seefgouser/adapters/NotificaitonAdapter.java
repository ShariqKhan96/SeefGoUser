package com.webxert.seefgouser.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.webxert.seefgouser.NotificationsActivity;
import com.webxert.seefgouser.R;
import com.webxert.seefgouser.TrackActivity;
import com.webxert.seefgouser.models.Notification;
import com.webxert.seefgouser.models.TrackOrder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hp on 4/1/2019.
 */

public class NotificaitonAdapter extends RecyclerView.Adapter<NotificaitonAdapter.MyVH> {
    List<TrackOrder> notifications = new ArrayList<>();
    Context context;

    public NotificaitonAdapter(List<TrackOrder> notifications, Context context) {
        this.notifications = notifications;
        this.context = context;
    }

    @NonNull
    @Override
    public MyVH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new MyVH(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.notification_view, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyVH myVH, int i) {
        final TrackOrder notification = notifications.get(i);
        myVH.price.setText(notification.getPackage_comment());
        myVH.source.setText("From: " + notification.getStart_point());
        myVH.destination.setText("To: " + notification.getEnd_point());
        myVH.date.setText(notification.getDate_time());
        myVH.itemname.setText(notification.getPackage_name());

        myVH.track_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, TrackActivity.class);
                intent.putExtra("start_lat", notification.getStart_lat());
                intent.putExtra("start_long", notification.getStart_long());
                intent.putExtra("end_lat", notification.getEnd_lat());
                intent.putExtra("end_long", notification.getEnd_long());
                intent.putExtra("driver_id", notification.getDriver_id());

                context.startActivity(intent);

            }
        });
        //myVH.status.setText(parcel.getStatus());
    }

    private void callService(String id) {

    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }

    public class MyVH extends RecyclerView.ViewHolder {
        TextView source, destination, itemname, date, price, status;
        FrameLayout track_order;

        public MyVH(@NonNull View itemView) {
            super(itemView);
            track_order = itemView.findViewById(R.id.track_order);
            source = itemView.findViewById(R.id.source);
            destination = itemView.findViewById(R.id.destination);
            itemname = itemView.findViewById(R.id.itemname);
            date = itemView.findViewById(R.id.date);
            price = itemView.findViewById(R.id.price);
        }
    }
}
