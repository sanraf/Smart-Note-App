package com.smartdevapp.smartnote;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

public class ThemeSettings extends Application {
    public static  final String PREFERENCE = "preference";
    public static  final String CUSTOM_THEME = "customTheme";
    public static  final String LIGHT_THEME = "lightTheme";
    public static  final String DARK_THEME = "darkTheme";
    private  String customTheme;

    public String getCustomTheme() {
        return customTheme;
    }

    public void setCustomTheme(String customTheme) {
        this.customTheme = customTheme;
    }

    public static void reset(Context context){
        SharedPreferences preferences = context.getSharedPreferences(ThemeSettings.PREFERENCE,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove(ThemeSettings.DARK_THEME);
        editor.remove(ThemeSettings.LIGHT_THEME);
        editor.clear();
        editor.apply();

    }
}
