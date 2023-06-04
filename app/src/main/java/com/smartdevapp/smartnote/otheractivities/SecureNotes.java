package com.smartdevapp.smartnote.otheractivities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.smartdevapp.smartnote.Database.RoomDB;
import com.smartdevapp.smartnote.MainActivity;
import com.smartdevapp.smartnote.Models.Notes;
import com.smartdevapp.smartnote.R;

import java.util.ArrayList;
import java.util.List;

public class SecureNotes extends AppCompatActivity {
    boolean passwordVisible;
    EditText email,password,password1;
    Button protect,cancel;
    RoomDB database;
    List<Notes> note = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_secure_notee);

        protect = findViewById(R.id.protect);
        cancel = findViewById(R.id.cancel_p);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        password1 = findViewById(R.id.password1);

        password.setLongClickable(false);
        password.setLongClickable(false);

        database = RoomDB.getInstance(this);
        //note = database.mainDataAcessObject().getAll();

        int noteID = getIntent().getIntExtra(MainActivity.myID,0);

        password1.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                showPassword(view,event,password1);

                return false;
            }
        });

        password.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                showPassword(view,event,password);
                return false;
            }
        });

        protect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String em = email.getText().toString();
                String pass = password.getText().toString();
                String repass = password1.getText().toString();
                String regex = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+(?:\\.[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+)*@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$";

                if(em.equals("") ){
                    showError(email,"please enter email");

                }else if(!em.matches(regex)){

                    showError(email,"please enter valid email address");

                }
                else if(pass.equals("")) {
                    showError(password,"please enter password");
                }
                else if(password.length()>10 || password.length()<5){
                    showError(password,"5-10 characters allowed for password");
                }else if(repass.equals("")){
                    showError(password1,"please re-enter password");
                }else if(!pass.equals(repass)){
                    showError(password1,"password not matching");

                }else{

                    database.mainDataAcessObject().update_security(noteID,em,repass);
                    database.mainDataAcessObject().locked(noteID,true);


                    database.mainDataAcessObject().red(noteID,false);
                    database.mainDataAcessObject().green(noteID,false);
                    database.mainDataAcessObject().pink(noteID,false);
                    database.mainDataAcessObject().creamy(noteID,false);
                    database.mainDataAcessObject().yellow(noteID,false);
                    database.mainDataAcessObject().gray(noteID,false);
                    Toast.makeText(SecureNotes.this, "Notes Protected ", Toast.LENGTH_SHORT).show();
                    onBackPressed();

                }

            }

        });

        cancel.setOnClickListener(View -> {

            email.setText(null);
            password.setText(null);
            password1.setText(null);
            onBackPressed();
        });
    }

    private void showError(EditText input, String s) {

        input.setError(s);
        input.requestFocus();
    }
    public boolean showPassword(@Nullable View view, MotionEvent event, EditText pass){
        final int Right =2;
        if(event.getAction()==MotionEvent.ACTION_UP){
            if(event.getRawX()>=pass.getRight()-pass.getCompoundDrawables()[Right].getBounds().width()){
                int selection = pass.getSelectionEnd();
                if(passwordVisible){
                    pass.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.ic_baseline_visibility_off_24,0);
                    pass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    passwordVisible =false;
                }else{
                    pass.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.ic_baseline_remove_red_eye_24,0);
                    pass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    passwordVisible =true;
                }
                pass.setSelection(selection);
                return true;
            }
        }
        return false;
    }


    @Override
    public void onBackPressed() {

        Intent intent = new Intent(SecureNotes.this,MainActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(0,android.R.anim.slide_out_right);
    }
}//<a href="https://www.freepik.com/free-vector/security-safety-icon_4873540.htm#query=privacy%20icon&position=32&from_view=keyword&track=robertav1_2_sidr">Image by djvstock</a> on Freepik