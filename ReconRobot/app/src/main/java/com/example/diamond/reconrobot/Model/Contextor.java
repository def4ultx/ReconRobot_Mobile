package com.example.diamond.reconrobot.Model;

import android.content.Context;

/**
 * Created by diamond on 24/2/2018 AD.
 */

public class Contextor {
    private static Contextor instance;
    private Context context;

    private Contextor () {

    }

    public static Contextor getInstance() {
        if (instance == null) {
            instance = new Contextor();
        }
        return instance;
    }

    public void init (Context context) {
        this.context = context;
    }

    public Context getContext(){
        return context;
    }

    public static void clear(){
        instance = null;
    }
}
