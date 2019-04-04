package com.webxert.seefgouser.models;

/**
 * Created by hp on 4/4/2019.
 */

public class Weights {
    String weight_id;
    String weight_range;
    String price_range;

    public Weights() {
    }

    public Weights(String weight_id, String weight_range, String price_range) {
        this.weight_id = weight_id;
        this.weight_range = weight_range;
        this.price_range = price_range;
    }

    public String getWeight_id() {
        return weight_id;
    }

    public void setWeight_id(String weight_id) {
        this.weight_id = weight_id;
    }

    public String getWeight_range() {
        return weight_range;
    }

    public void setWeight_range(String weight_range) {
        this.weight_range = weight_range;
    }

    public String getPrice_range() {
        return price_range;
    }

    public void setPrice_range(String price_range) {
        this.price_range = price_range;
    }
}
