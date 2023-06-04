package com.smartdevapp.smartnote.Util;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.widget.SwitchCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.smartdevapp.smartnote.LinedEditText;
import com.smartdevapp.smartnote.R;

public class AppTheme {

    final int black ;
    final int green;
    final int blue ;
    final int white;
    final int purple;
    final int primary;
    final int dark_black;
    final int transp;
    final int myteal;
    final int myyellow;

    final Drawable darkMode ;
    final Drawable lightMode ;
    final Drawable lightNotes;
    final Drawable darkNotes;
    final Drawable blackTran;
    final Drawable whiteTran;
    final Drawable nightTheme;
    final Drawable lightTheme;
    final Drawable night_theme_shop;
    final Drawable light_theme_shop;
    final Drawable night_theme_home;
    final Drawable light_theme_home;
    final Drawable light_theme_task;
    final Drawable night_theme_task;
    final Drawable panelBck_1;
    final Drawable panelBck_2;
    final Drawable pinDark;
    final Drawable listDark;
    final Drawable viewDark;
    final Drawable settingDark;
    final Drawable pinLight;
    final Drawable listLight;
    final Drawable viewLight;
    final Drawable settingLight;



    public AppTheme(Context context){
        black = ContextCompat.getColor(context.getApplicationContext(), R.color.dim_mode);
        green = ContextCompat.getColor(context.getApplicationContext(), R.color.matrix);
        blue = ContextCompat.getColor(context.getApplicationContext(), R.color.Bluer);
        white = ContextCompat.getColor(context.getApplicationContext(), R.color.white);
        purple = ContextCompat.getColor(context.getApplicationContext(), R.color.purple_mode);
        darkMode = ContextCompat.getDrawable(context.getApplicationContext(), R.drawable.mode_night_24);
        lightMode= ContextCompat.getDrawable(context.getApplicationContext(), R.drawable.brightness_24);
        lightNotes= ContextCompat.getDrawable(context.getApplicationContext(), R.drawable.whitenote);
        darkNotes = ContextCompat.getDrawable(context.getApplicationContext(), R.drawable.darknotes);


        primary= ContextCompat.getColor(context.getApplicationContext(), R.color.colorPrimary);
        dark_black = ContextCompat.getColor(context.getApplicationContext(), R.color.black);

        nightTheme = ContextCompat.getDrawable(context.getApplicationContext(), R.drawable.night_theme_add);
        lightTheme = ContextCompat.getDrawable(context.getApplicationContext(), R.drawable.light_theme_add);
        blackTran = ContextCompat.getDrawable(context.getApplicationContext(), R.drawable.black_tran);
         whiteTran = ContextCompat.getDrawable(context.getApplicationContext(), R.drawable.white_tran);
        night_theme_shop = ContextCompat.getDrawable(context.getApplicationContext(), R.drawable.night_theme_shop);
        light_theme_shop = ContextCompat.getDrawable(context.getApplicationContext(), R.drawable.light_theme_shop);

        transp = ContextCompat.getColor(context.getApplicationContext(), R.color.tran);
        night_theme_home = ContextCompat.getDrawable(context.getApplicationContext(), R.drawable.night_theme_home);
        light_theme_home = ContextCompat.getDrawable(context.getApplicationContext(), R.drawable.light_theme_home);


        panelBck_1 = ContextCompat.getDrawable(context.getApplicationContext(), R.drawable.home_bckg_littheme);
        panelBck_2 = ContextCompat.getDrawable(context.getApplicationContext(), R.drawable.home_bckg_litetheme_1);

        pinDark = ContextCompat.getDrawable(context.getApplicationContext(), R.drawable.circle_night);
        listDark = ContextCompat.getDrawable(context.getApplicationContext(), R.drawable.list_night);
        viewDark = ContextCompat.getDrawable(context.getApplicationContext(), R.drawable.view_night);
        settingDark = ContextCompat.getDrawable(context.getApplicationContext(), R.drawable.setting_night);

        pinLight = ContextCompat.getDrawable(context.getApplicationContext(), R.drawable.circle_light);
        listLight = ContextCompat.getDrawable(context.getApplicationContext(), R.drawable.list_light);
        viewLight = ContextCompat.getDrawable(context.getApplicationContext(), R.drawable.view_light);
        settingLight = ContextCompat.getDrawable(context.getApplicationContext(), R.drawable.setting_light);


        myteal = ContextCompat.getColor(context.getApplicationContext(), R.color.teal_tran);
        myyellow = ContextCompat.getColor(context.getApplicationContext(), R.color.yellowtran);

        light_theme_task = ContextCompat.getDrawable(context.getApplicationContext(), R.drawable.light_theme_task);
        night_theme_task = ContextCompat.getDrawable(context.getApplicationContext(), R.drawable.night_theme_task);





    }

    public void taskNightTheme(boolean check, View panelView, DrawerLayout drawerLayout, TextView taskTitle,
                               Menu menu, ImageView iconMenu, MaterialButton addTaskButton,SwitchCompat themeModeSwitch){

        panelView.setBackground(night_theme_task);
        drawerLayout.setBackgroundColor(black);
        taskTitle.setTextColor(white);
        menu.findItem(R.id.onTaskSwitchTheme).setIcon(darkMode);
        iconMenu.setColorFilter(white);
        addTaskButton.setBackgroundTintList(ColorStateList.valueOf(myyellow));
        themeModeSwitch.setChecked(check);
    }

    public void taskLightTheme(boolean check, View panelView, DrawerLayout drawerLayout, TextView taskTitle,
                               Menu menu, ImageView iconMenu, MaterialButton addTaskButton,SwitchCompat themeModeSwitch){

        panelView.setBackground(light_theme_task);
        drawerLayout.setBackgroundColor(white);

        taskTitle.setTextColor(purple);
        menu.findItem(R.id.onTaskSwitchTheme).setIcon(lightMode);
        iconMenu.setColorFilter(purple);
        addTaskButton.setBackgroundTintList(ColorStateList.valueOf(myteal));
        themeModeSwitch.setChecked(check);
    }
    
    public void splashNightTheme(boolean check ,TextView titleNote, TextView titleList, TextView titleTask, TextView titleDrawer,
                                 TextView homeScreenTitle, LinedEditText linedEditText,DrawerLayout drawerLayout,
                                 RelativeLayout homeButtonPanel, FloatingActionButton openNotes,FloatingActionButton openList,
                                 FloatingActionButton openTask,FloatingActionButton openDrawer,ImageView circlePin,ImageView listIcon,
                                 ImageView viewIcon,ImageView settingIcon, LinearLayout view_1,LinearLayout view_2,
                                 LinearLayout view_3,
                                 LinearLayout view_4,Menu menu,SwitchCompat themeModeSwitch){

        titleNote.setTextColor(green);
        titleList.setTextColor(green);
        titleTask.setTextColor(green);
        titleDrawer.setTextColor(green);
        linedEditText.setTextColor(green);
        homeScreenTitle.setTextColor(green);

        drawerLayout.setBackground(night_theme_home);
        //cardView.setCardBackgroundColor(black);
        linedEditText.setBackground(blackTran);
        homeButtonPanel.setBackgroundColor(transp);

        openNotes.setBackgroundTintList(ColorStateList.valueOf(black));
        openList.setBackgroundTintList(ColorStateList.valueOf(black));
        openTask.setBackgroundTintList(ColorStateList.valueOf(black));
        openDrawer.setBackgroundTintList(ColorStateList.valueOf(black));

        openNotes.setColorFilter(green);
        openList.setColorFilter(green);
        openTask.setColorFilter(green);
        openDrawer.setColorFilter(green);

        circlePin.setImageDrawable(pinDark);
        listIcon.setImageDrawable(listDark);
        viewIcon.setImageDrawable(viewDark);
        settingIcon.setImageDrawable(settingDark);

        view_1.setBackground(panelBck_1);
        view_2.setBackground(panelBck_2);
        view_3.setBackground(panelBck_2);
        view_4.setBackground(panelBck_2);


        menu.findItem(R.id.onHomeSwitchTheme).setIcon(darkMode);

        themeModeSwitch.setChecked(check);
    }

    public void splashLightTheme(boolean check ,TextView titleNote, TextView titleList, TextView titleTask, TextView titleDrawer,
                                 TextView homeScreenTitle, LinedEditText linedEditText,DrawerLayout drawerLayout,
                                 RelativeLayout homeButtonPanel, FloatingActionButton openNotes,FloatingActionButton openList,
                                 FloatingActionButton openTask,FloatingActionButton openDrawer,ImageView circlePin,ImageView listIcon,
                                 ImageView viewIcon,ImageView settingIcon, LinearLayout view_1,LinearLayout view_2,
                                 LinearLayout view_3,
                                 LinearLayout view_4,Menu menu,SwitchCompat themeModeSwitch){

        titleNote.setTextColor(white);
        titleList.setTextColor(white);
        titleTask.setTextColor(white);
        titleDrawer.setTextColor(white);
        linedEditText.setTextColor(white);
        homeScreenTitle.setTextColor(blue);

        drawerLayout.setBackground(light_theme_home);
        //cardView.setCardBackgroundColor(white);
        linedEditText.setBackground(whiteTran);
        homeButtonPanel.setBackgroundColor(transp);

        openNotes.setBackgroundTintList(ColorStateList.valueOf(white));
        openList.setBackgroundTintList(ColorStateList.valueOf(white));
        openTask.setBackgroundTintList(ColorStateList.valueOf(white));
        openDrawer.setBackgroundTintList(ColorStateList.valueOf(white));
        menu.findItem(R.id.onHomeSwitchTheme).setIcon(lightMode);

        openNotes.setColorFilter(blue);
        openList.setColorFilter(blue);
        openTask.setColorFilter(blue);
        openDrawer.setColorFilter(blue);

        circlePin.setImageDrawable(pinLight);
        listIcon.setImageDrawable(listLight);
        viewIcon.setImageDrawable(viewLight);
        settingIcon.setImageDrawable(settingLight);

        view_1.setBackground(panelBck_1);
        view_2.setBackground(panelBck_2);
        view_3.setBackground(panelBck_2);
        view_4.setBackground(panelBck_2);

        themeModeSwitch.setChecked(check);
    }

    public void shopNightTheme(boolean check, View panelView,DrawerLayout drawerLayout,TextView item,
                               TextView quantity,TextView price,TextView title,
                               ImageView iconMenu,FloatingActionButton addItemButton,MaterialButton getTotal, Menu menu,
                               SwitchCompat themeModeSwitch){

        panelView.setBackground(night_theme_shop);
        drawerLayout.setBackgroundColor(black);

        item.setTextColor(green);
        quantity.setTextColor(green);
        price.setTextColor(green);
        title.setTextColor(green);

        iconMenu.setColorFilter(green);
        addItemButton.setBackgroundTintList(ColorStateList.valueOf(black));
        getTotal.setBackgroundTintList(ColorStateList.valueOf(black));
        getTotal.setTextColor(green);
        addItemButton.setColorFilter(green);
        menu.findItem(R.id.onSwitchTheme).setIcon(darkMode);
        themeModeSwitch.setChecked(check);
    }

    public void shopLightTheme(boolean check, View panelView,DrawerLayout drawerLayout,TextView item,
                               TextView quantity,TextView price,TextView title,
                               ImageView iconMenu,FloatingActionButton addItemButton,MaterialButton getTotal, Menu menu,
                               SwitchCompat themeModeSwitch){

        panelView.setBackground(light_theme_shop);
        drawerLayout.setBackgroundColor(black);

        item.setTextColor(black);
        quantity.setTextColor(black);
        price.setTextColor(black);
        title.setTextColor(black);

        iconMenu.setColorFilter(black);
        addItemButton.setBackgroundTintList(ColorStateList.valueOf(black));
        getTotal.setBackgroundTintList(ColorStateList.valueOf(black));
        menu.findItem(R.id.onSwitchTheme).setIcon(lightMode);
        themeModeSwitch.setChecked(check);
    }

    public void noteNightMode(boolean check,CollapsingToolbarLayout toolbarLayout, RelativeLayout notesLayout,
                              Menu menu, ImageView iconMenu, FloatingActionButton addNotesButton,
                              ImageView noteIcon, SwitchCompat themeModeSwitch,TextView note_count){
        toolbarLayout.setBackgroundColor(black);
        notesLayout.setBackgroundColor(black);
        iconMenu.setColorFilter(green);
        addNotesButton.setColorFilter(green);
        addNotesButton.setBackgroundTintList(ColorStateList.valueOf(white));
        toolbarLayout.setCollapsedTitleTextColor(white);
        note_count.setTextColor(white);
        menu.findItem(R.id.onNotesSwitchTheme).setIcon(darkMode);
        noteIcon.setBackground(lightNotes);
        themeModeSwitch.setChecked(check);
    }
    public void noteLightMode(boolean check,CollapsingToolbarLayout toolbarLayout, RelativeLayout notesLayout,
                              Menu menu, ImageView iconMenu, FloatingActionButton addNotesButton,
                              ImageView noteIcon, SwitchCompat themeModeSwitch,TextView note_count){
        toolbarLayout.setBackgroundColor(white);
        notesLayout.setBackgroundColor(white);
        toolbarLayout.setCollapsedTitleTextColor(purple);
        note_count.setTextColor(purple);
        iconMenu.setColorFilter(blue);
        menu.findItem(R.id.onNotesSwitchTheme).setIcon(lightMode);
        noteIcon.setBackground(darkNotes);
        addNotesButton.setColorFilter(blue);
        addNotesButton.setBackgroundTintList(ColorStateList.valueOf(black));
        themeModeSwitch.setChecked(check);
    }

    public void addNoteNightTheme(boolean check,LinearLayout cardView, DrawerLayout drawerLayout, TextView content,
                                  EditText Add_title,EditText AddLine_note,EditText Add_note,
                                  ImageView save, ImageView iconMenu,Menu menu,SwitchCompat themeModeSwitch){

        cardView.setBackground(blackTran);
        drawerLayout.setBackground(nightTheme);
        content.setTextColor(primary);
        AddLine_note.setTextColor(green);
        Add_note.setTextColor(green);
        Add_title.setTextColor(green);
        iconMenu.setColorFilter(primary);
        save.setColorFilter(primary);
        menu.findItem(R.id.onAddNotesSwitchTheme).setIcon(darkMode);
        themeModeSwitch.setChecked(check);
    }

    public void addNoteLightTheme(boolean check,LinearLayout cardView, DrawerLayout drawerLayout, TextView content,
                                  EditText Add_title,EditText AddLine_note,EditText Add_note,
                                  ImageView save, ImageView iconMenu,Menu menu,SwitchCompat themeModeSwitch){

        cardView.setBackground(whiteTran);
        drawerLayout.setBackground(lightTheme);
        content.setTextColor(black);
        content.setTextColor(white);
        AddLine_note.setTextColor(dark_black);
        Add_note.setTextColor(dark_black);
        Add_title.setTextColor(dark_black);
        iconMenu.setColorFilter(white);
        save.setColorFilter(white);
        menu.findItem(R.id.onAddNotesSwitchTheme).setIcon(lightMode);
        themeModeSwitch.setChecked(check);
    }


}
