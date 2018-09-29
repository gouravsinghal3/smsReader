package com.vrs.smsapp;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

public class MainClassApplication extends Application {
    public static int rowCount = 0;
    public static String Shared_IDENTIFIER = "id_ident";
    public static Context mContext;
    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }

    public static void SetRowCount(Context iActivity, int count) {
        final SharedPreferences prefs = iActivity.getSharedPreferences(Shared_IDENTIFIER, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("count", count);
        editor.commit();
    }

    public static int GetrowCount(Context iActivity) {
        int count = 0;
        final SharedPreferences prefs = mContext.getSharedPreferences(Shared_IDENTIFIER, Context.MODE_PRIVATE);

        count = prefs.getInt("count", 0);
        return count;
    }
}
