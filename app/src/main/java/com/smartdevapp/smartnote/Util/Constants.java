package com.smartdevapp.smartnote.Util;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;

import com.smartdevapp.smartnote.R;

@RequiresApi(api = Build.VERSION_CODES.M)
public class Constants {
    Context context;
    public  int black;
    Constants(Context context){
        this.context = context;

    }

    {

        black = ContextCompat.getColor(context, R.color.black);
    }

    /*final int transp = ContextCompat.getColor(this, R.color.tran);
    final Drawable nightTheme = ContextCompat.getDrawable(this, R.drawable.night_theme_home);
    final Drawable lightTheme = ContextCompat.getDrawable(this, R.drawable.light_theme_home);
    final int purple = ContextCompat.getColor(this, R.color.teal_200);
    final int green = ContextCompat.getColor(this, R.color.matrix);
    final int blue = ContextCompat.getColor(this, R.color.Bluer);
    final int white = ContextCompat.getColor(this, R.color.white);




    final Drawable darkMode = ContextCompat.getDrawable(this, R.drawable.mode_night_24);
    final Drawable lightMode = ContextCompat.getDrawable(this, R.drawable.brightness_24);
    final Drawable blackTran = ContextCompat.getDrawable(this, R.drawable.black_tran);
    final Drawable whiteTran = ContextCompat.getDrawable(this, R.drawable.white_tran);
    final Drawable panelBck_1 = ContextCompat.getDrawable(this, R.drawable.home_bckg_littheme);
    final Drawable panelBck_2 = ContextCompat.getDrawable(this, R.drawable.home_bckg_litetheme_1);

    final Drawable pinDark = ContextCompat.getDrawable(this, R.drawable.circle_night);
    final Drawable listDark = ContextCompat.getDrawable(this, R.drawable.list_night);
    final Drawable viewDark = ContextCompat.getDrawable(this, R.drawable.view_night);
    final Drawable settingDark = ContextCompat.getDrawable(this, R.drawable.setting_night);

    final Drawable pinLight = ContextCompat.getDrawable(this, R.drawable.circle_light);
    final Drawable listLight = ContextCompat.getDrawable(this, R.drawable.list_light);
    final Drawable viewLight = ContextCompat.getDrawable(this, R.drawable.view_light);
    final Drawable settingLight = ContextCompat.getDrawable(this, R.drawable.setting_light);


    public static final String DEFAULT_THEME_TEXT = "white";
    public static final String DEFAULT_THEME = "White";

    public static final String THEME_WHITE = "White";
    public static final String THEME_BLACK = "Black";
    public static final String THEME_DARK = "Dark";

    @Override
    public Context getApplicationContext() {
        return null;
    }*/
}
