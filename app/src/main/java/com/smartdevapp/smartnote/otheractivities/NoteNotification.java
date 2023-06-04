package com.smartdevapp.smartnote.otheractivities;

import static android.app.PendingIntent.FLAG_UPDATE_CURRENT;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.smartdevapp.smartnote.AlarmReceiver;
import com.smartdevapp.smartnote.Database.RoomDB;
import com.smartdevapp.smartnote.MainActivity;
import com.smartdevapp.smartnote.Models.Notes;
import com.smartdevapp.smartnote.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class NoteNotification extends AppCompatActivity {

    int hour, minutes;
    TimePicker timePicker;

    Notes selectedNotes;
    RoomDB database;
    List<Notes> note = new ArrayList<>();

    AdView adViewalarm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_notification);

        timePicker = findViewById(R.id.timePicker);
        Button setAlarm = findViewById(R.id.setAlarm);
        Button back = findViewById(R.id.back);
        adViewalarm = findViewById(R.id.adViewalarm);

        database = RoomDB.getInstance(this);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(@NonNull InitializationStatus initializationStatus) {
            }
        });
        AdRequest adRequest = new AdRequest.Builder().build();
        adViewalarm.loadAd(adRequest);

        timePicker.setIs24HourView(true);

        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                hour = hourOfDay;
                minutes = minute;

            }
        });

        setAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAlarm();
                onBackPressed();

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
    private void setAlarm() {
        int noteID = getIntent().getIntExtra(MainActivity.myID,0);
        String title = getIntent().getStringExtra(MainActivity.myTitle);
        String note = getIntent().getStringExtra(MainActivity.myNote);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Date date = new Date();
        Calendar calendar_now = Calendar.getInstance();
        Calendar calendar = Calendar.getInstance();
        calendar_now.setTime(date);
        calendar.setTime(date);

        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minutes);
        calendar.set(Calendar.SECOND, 0);


        if (calendar.before(calendar_now)) {
            calendar.add(Calendar.DATE, 1);
        }


        Intent intent = new Intent(NoteNotification.this, AlarmReceiver.class);
        intent.putExtra("getTitle", title);
        intent.putExtra("getNotes", note);
        intent.putExtra("getID", noteID);
        @SuppressLint("UnspecifiedImmutableFlag") PendingIntent pendingIntent = PendingIntent.getBroadcast(NoteNotification.this, noteID,
                intent, FLAG_UPDATE_CURRENT);
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        database.mainDataAcessObject().alarmed(noteID, true);
        Toast.makeText(this, "Reminder set Successfully", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(NoteNotification.this,MainActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(0,android.R.anim.slide_out_right);
    }
}