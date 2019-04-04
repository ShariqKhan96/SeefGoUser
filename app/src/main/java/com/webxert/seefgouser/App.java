package com.webxert.seefgouser;

import android.app.Application;

import io.paperdb.Paper;

/**
 * Created by hp on 4/4/2019.
 */

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Paper.init(this);
    }
}
