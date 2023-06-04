 package com.smartdevapp.smartnote;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

public class AboutTheApp extends AppCompatActivity {
TextView textView,aboutApp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_the_app);

        textView = findViewById(R.id.link);
        aboutApp = findViewById(R.id.aboutApp);
        textView.setMovementMethod(LinkMovementMethod.getInstance());


        String string = getResources().getString(R.string.about_app_info);
        aboutApp.setText(getSpannedText(string));

    }

    private Spanned getSpannedText(String text){
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.N){
            return Html.fromHtml(text,Html.FROM_HTML_MODE_COMPACT);
        }else {
            //noinspection deprecation
            return Html.fromHtml(text);
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
overridePendingTransition(0,android.R.anim.slide_out_right);
    }
}