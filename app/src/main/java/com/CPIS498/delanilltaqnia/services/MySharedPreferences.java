package com.CPIS498.delanilltaqnia.services;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class MySharedPreferences {
    SharedPreferences sharedPref;
    Context mContext;
    private static final String PREFERNCE_NAME="myPref";

    public MySharedPreferences(Context mContext) {
        this.mContext = mContext;
         sharedPref = ((Activity) mContext).getSharedPreferences(PREFERNCE_NAME, Context.MODE_PRIVATE)  ;

    }

    public void setIsLogged(boolean value){
        //set is logged to true
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean("isLogged", value);
        editor.apply();
    }

    public boolean getIsLogged(){
       return sharedPref.getBoolean("isLogged",false);
    }

}
