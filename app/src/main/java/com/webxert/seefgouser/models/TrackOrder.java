package com.webxert.seefgouser.models;

/**
 * Created by hp on 4/14/2019.
 */

public class TrackOrder {
    public String driver_id ;
    public String package_id ;
    public String package_name ;
    public String package_width ;
    public String package_height ;
    public String date_time ;
    public String user_id ;
    public String weight_id ;
    public String package_comment ;
    public String package_price ;
    public String start_point ;
    public String start_lat ;
    public String start_long ;
    public String status ;
    public String end_point ;
    public String end_lat ;
    public String end_long ;

    public TrackOrder(String driver_id, String package_id, String package_name, String package_width, String package_height, String date_time, String user_id, String weight_id, String package_comment, String package_price, String start_point, String start_lat, String start_long, String status, String end_point, String end_lat, String end_long) {
        this.driver_id = driver_id;
        this.package_id = package_id;
        this.package_name = package_name;
        this.package_width = package_width;
        this.package_height = package_height;
        this.date_time = date_time;
        this.user_id = user_id;
        this.weight_id = weight_id;
        this.package_comment = package_comment;
        this.package_price = package_price;
        this.start_point = start_point;
        this.start_lat = start_lat;
        this.start_long = start_long;
        this.status = status;
        this.end_point = end_point;
        this.end_lat = end_lat;
        this.end_long = end_long;
    }

    public TrackOrder() {
    }

    public String getDriver_id() {
        return driver_id;
    }

    public void setDriver_id(String driver_id) {
        this.driver_id = driver_id;
    }

    public String getPackage_id() {
        return package_id;
    }

    public void setPackage_id(String package_id) {
        this.package_id = package_id;
    }

    public String getPackage_name() {
        return package_name;
    }

    public void setPackage_name(String package_name) {
        this.package_name = package_name;
    }

    public String getPackage_width() {
        return package_width;
    }

    public void setPackage_width(String package_width) {
        this.package_width = package_width;
    }

    public String getPackage_height() {
        return package_height;
    }

    public void setPackage_height(String package_height) {
        this.package_height = package_height;
    }

    public String getDate_time() {
        return date_time;
    }

    public void setDate_time(String date_time) {
        this.date_time = date_time;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getWeight_id() {
        return weight_id;
    }

    public void setWeight_id(String weight_id) {
        this.weight_id = weight_id;
    }

    public String getPackage_comment() {
        return package_comment;
    }

    public void setPackage_comment(String package_comment) {
        this.package_comment = package_comment;
    }

    public String getPackage_price() {
        return package_price;
    }

    public void setPackage_price(String package_price) {
        this.package_price = package_price;
    }

    public String getStart_point() {
        return start_point;
    }

    public void setStart_point(String start_point) {
        this.start_point = start_point;
    }

    public String getStart_lat() {
        return start_lat;
    }

    public void setStart_lat(String start_lat) {
        this.start_lat = start_lat;
    }

    public String getStart_long() {
        return start_long;
    }

    public void setStart_long(String start_long) {
        this.start_long = start_long;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getEnd_point() {
        return end_point;
    }

    public void setEnd_point(String end_point) {
        this.end_point = end_point;
    }

    public String getEnd_lat() {
        return end_lat;
    }

    public void setEnd_lat(String end_lat) {
        this.end_lat = end_lat;
    }

    public String getEnd_long() {
        return end_long;
    }

    public void setEnd_long(String end_long) {
        this.end_long = end_long;
    }
}
