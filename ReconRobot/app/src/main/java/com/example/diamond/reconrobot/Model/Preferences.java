package com.example.diamond.reconrobot.Model;

/**
 * Created by diamond on 24/2/2018 AD.
 */

public class Preferences {
    public static Preferences preferences = null;


    public Preferences(){

    }
    public static Preferences getInstance(){
        if(preferences == null){
            preferences = new Preferences();
        }
        return preferences;
    }


}
