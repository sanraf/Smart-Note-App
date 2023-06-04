package com.smartdevapp.smartnote;

import static android.app.PendingIntent.FLAG_UPDATE_CURRENT;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;


import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

public class AlarmReceiver extends BroadcastReceiver {
    public static final String keyID ="notesID";
    @Override
    public void onReceive(Context context, Intent intent) {

        String title = intent.getStringExtra("getTitle");
        String notes = intent.getStringExtra("getNotes");
        int id = intent.getIntExtra("getID",-1);


        Intent intent1 = new Intent(context,MainActivity.class);
        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent1.putExtra(keyID,id);

        @SuppressLint("UnspecifiedImmutableFlag") PendingIntent pendingIntent =
                PendingIntent.getActivity(context, id,intent1, FLAG_UPDATE_CURRENT);

        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),R.drawable.noteicon);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context,"smartappdev")
                .setSmallIcon(R.drawable.ic_baseline_sticky_note_2_24)
                .setLargeIcon(bitmap)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(notes)
                        .setBigContentTitle("Notes Content")
                        .setSummaryText(": You Have Notes Schedule"))
                .setContentTitle(title)
                .setColorized(true)
                .setAutoCancel(true)
                .setColor(ContextCompat.getColor(context,R.color.purple_mode))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setContentIntent(pendingIntent);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(id,builder.build());

    }
}
