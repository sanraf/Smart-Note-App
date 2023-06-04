package com.smartdevapp.smartnote.otheractivities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.smartdevapp.smartnote.AddList;
import com.smartdevapp.smartnote.AddNotes;
import com.smartdevapp.smartnote.Models.Notes;
import com.smartdevapp.smartnote.Models.Shopping;
import com.smartdevapp.smartnote.Models.Task;
import com.smartdevapp.smartnote.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AddTaskMode extends AppCompatActivity {
    EditText Add_task, Add_title;
    Button save;
    Task task;
    boolean isOldTask = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task_mode);

        Add_task = findViewById(R.id.edit_task);
        Add_title = findViewById(R.id.title_task);
        save = findViewById(R.id.floatingButton);


        try {

            task = (Task) getIntent().getSerializableExtra("old_task");
            Add_task.setText(task.getTask());
            Add_title.setText(task.getTitle());
            isOldTask = true;

        } catch (Exception e) {
            e.printStackTrace();
        }

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = Add_title.getText().toString();
                String description;


                description = Add_task.getText().toString();

                //closing the keypad after finishing the activity
                try {
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

                } catch (Exception e) {

                    e.printStackTrace();
                }

                if (title.isEmpty()) {
                    Toast.makeText(AddTaskMode.this, "Title Required", Toast.LENGTH_SHORT).show();
                }else if(Add_task.length() >600|| Add_task.getLayout().getLineCount()>=30){
                    Toast.makeText(AddTaskMode.this, "Character Over-Bound", Toast.LENGTH_SHORT).show();
                } else {


                    @SuppressLint("SimpleDateFormat")
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("ddMMMyyyy \nHH:mm:ss");
                    Date date = new Date();
                    Toast.makeText(AddTaskMode.this, "Created Successfully", Toast.LENGTH_SHORT).show();

                    if (!isOldTask) {
                        task = new Task();
                    }

                    task.setTitle(title.trim());
                    task.setTask(description.trim());
                    task.setDate(simpleDateFormat.format(date));

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent();
                            intent.putExtra("task", task);
                            setResult(Activity.RESULT_OK, intent);
                            overridePendingTransition(0, android.R.anim.slide_out_right);
                            finish();

                        }
                    }, 300);


                }
            }
        });

        transWindowDimension();

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.CENTER;
        params.x = 0;
        params.y = -20;
        getWindow().setAttributes(params);
    }

    private void transWindowDimension() {

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            getWindow().setLayout((int) (width * .9), (int) (height * .4));
        } else {
            getWindow().setLayout((int) (width * .9), (int) (height * .75));
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}