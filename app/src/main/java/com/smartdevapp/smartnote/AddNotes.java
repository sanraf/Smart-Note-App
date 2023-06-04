package com.smartdevapp.smartnote;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;


import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.smartdevapp.smartnote.Models.Notes;
import com.smartdevapp.smartnote.Util.AppTheme;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.StringTokenizer;

public class AddNotes extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    EditText Add_title, AddLine_note,Add_note;
    TextView content;
    ImageView save,note_editing,iconMenu;
    RelativeLayout notesLayout;
    Notes notes;
    boolean isOldNote = false;
    private SwitchCompat switchEditText,themeModeSwitch;
    Toolbar toolbar;

    DrawerLayout drawerLayout;
    LinearLayout cardView;
    ScrollView lineScroll,plainScroll;

    private static final String MY_PREFS= "switch";
    private static final String EDITTEXT_STATUS = "line_on";
    private static final String SWITCH_STATUS = "switch_status";
    public static final String KEY_NAME= "note";

    boolean switch_status;
    boolean lineEdit_status;
    ThemeSettings settings;
    Menu menu;


    SharedPreferences myPreference;
    SharedPreferences.Editor myEditor;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_notes);

        Add_title = findViewById(R.id.edit_title);
        AddLine_note = findViewById(R.id.editLine_notes);
        Add_note = findViewById(R.id.edit_notes);
        notesLayout = findViewById(R.id.RLayout);

        lineScroll = findViewById(R.id.lineScroll);
        plainScroll = findViewById(R.id.plainScroll);

        cardView = findViewById(R.id.cardview);
        iconMenu = findViewById(R.id.addNotesNav_icon);
        note_editing = findViewById(R.id.edit);

        content = findViewById(R.id.content);
        save = findViewById(R.id.save);
        toolbar = findViewById(R.id.toolBa);
        drawerLayout = findViewById(R.id.addNoteDrawer);


        NavigationView navigationView = findViewById(R.id.addNotesNavigate);
        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(this);
        menu = navigationView.getMenu();
        switchEditText = menu.findItem(R.id.onSwitch).getActionView().findViewById(R.id.mySwitch);
        themeModeSwitch = menu.findItem(R.id.onAddNotesSwitchTheme).getActionView().findViewById(R.id.mySwitch);

        settings = (ThemeSettings) getApplication();
        loadSharePreferences();
        iniSwitchListener();



        myPreference =getSharedPreferences(MY_PREFS,MODE_PRIVATE);
        myEditor = getSharedPreferences(MY_PREFS,MODE_PRIVATE).edit();
        switch_status = myPreference.getBoolean(SWITCH_STATUS,false);
        lineEdit_status = myPreference.getBoolean(EDITTEXT_STATUS,true);

        switchEditText.setChecked(switch_status);
        if (lineEdit_status){
            lineScroll.setVisibility(View.VISIBLE);
            plainScroll.setVisibility(View.GONE);
        }else{
            lineScroll.setVisibility(View.GONE);
            plainScroll.setVisibility(View.VISIBLE);

        }


        setSupportActionBar(toolbar);

        switchEditText.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean SwitchIsChecked) {
                if(buttonView.isChecked()){
                    lineScroll.setVisibility(View.GONE);
                    plainScroll.setVisibility(View.VISIBLE);
                    Add_note.setText(AddLine_note.getText().toString());
                    AddLine_note.getText().clear();
                    myEditor.putBoolean(SWITCH_STATUS,true);
                    myEditor.putBoolean(EDITTEXT_STATUS,false);
                    switchEditText.setChecked(true);


                }else {
                    lineScroll.setVisibility(View.VISIBLE);
                    plainScroll.setVisibility(View.GONE);
                    AddLine_note.setText(Add_note.getText().toString());
                    Add_note.getText().clear();
                    myEditor.putBoolean(SWITCH_STATUS,false);
                    myEditor.putBoolean(EDITTEXT_STATUS,true);
                    switchEditText.setChecked(false);

                }
                myEditor.apply();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        finish();
                        overridePendingTransition(0,0);
                        startActivity(getIntent());
                        overridePendingTransition(0,0);

                    }
                }, 250);

            }

        });

        AddLine_note.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Copy();
                return true;
            }
        });

        Add_note.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Copy();
                return true;
            }
        });


        findViewById(R.id.addNotesNav_icon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });



        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.R && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1000);
        }
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE},
                PackageManager.PERMISSION_GRANTED);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());


        notes = new Notes();
        try{
            notes = (Notes) getIntent().getSerializableExtra(MainActivity.oldNote_KEY);



            if (lineScroll.getVisibility()==View.VISIBLE){

                AddLine_note.setText(notes.getNotes());
                AddLine_note.setCursorVisible(false);
                AddLine_note.setFocusable(false);


            }else {

                Add_note.setText(notes.getNotes());
                Add_note.setCursorVisible(false);
                Add_note.setFocusable(false);
                save.setVisibility(View.GONE);

            }
            Add_title.setText(notes.getTitle());
            save.setVisibility(View.GONE);
            Add_title.setCursorVisible(false);
            Add_title.setFocusableInTouchMode(false);
            content.setText(R.string.read_mode);
            isOldNote = true;

        }catch (Exception e){
            e.printStackTrace();
        }

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                @SuppressLint("SimpleDateFormat")
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MMM/yyyy HH:mm:ss");
                @SuppressLint("SimpleDateFormat")
                SimpleDateFormat simpleTitleFormat = new SimpleDateFormat("ss");
                Date date = new Date();

                String title = Add_title.getText().toString();
                String description;

                if (lineScroll.getVisibility()==View.VISIBLE){
                    description = AddLine_note.getText().toString();
                }else {
                     description = Add_note.getText().toString();
                }

                if(description.isEmpty()){
                    Snackbar snackbar = Snackbar.make(notesLayout, "Add Notes To Save ".toUpperCase(Locale.ROOT), Snackbar.LENGTH_LONG);
                    snackbar.setTextColor(getResources().getColor(R.color.lite_red));
                    snackbar.setBackgroundTint(getResources().getColor(R.color.yellow));
                    snackbar.setAction("DISMISS", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            snackbar.dismiss();
                        }
                    });
                    snackbar.show();

                }else {
                    if (title.isEmpty()){
                        title = "Notes "+ simpleTitleFormat.format(date);
                    }

                    Toast.makeText(AddNotes.this, "Created Successfully", Toast.LENGTH_SHORT).show();

                    if(!isOldNote){
                        notes = new Notes();
                    }

                    notes.setTitle(title);
                    notes.setNotes(description);
                    notes.setDate(simpleDateFormat.format(date));

                    Intent intent = new Intent();
                    intent.putExtra(KEY_NAME,notes);
                    setResult(Activity.RESULT_OK,intent);
                    finish();
                    overridePendingTransition(0,android.R.anim.slide_out_right);


                }
            }
        });

        makeFolder();

    }

    private void loadSharePreferences() {

        SharedPreferences sharedPreferences = getSharedPreferences(ThemeSettings.PREFERENCE, MODE_PRIVATE);
        String theme = sharedPreferences.getString(ThemeSettings.CUSTOM_THEME, ThemeSettings.CUSTOM_THEME);
        settings.setCustomTheme(theme);
        updateView();

    }

    private void iniSwitchListener() {
        themeModeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked) {
                    settings.setCustomTheme(ThemeSettings.DARK_THEME);
                } else {
                    settings.setCustomTheme(ThemeSettings.LIGHT_THEME);

                }
                SharedPreferences.Editor editor = getSharedPreferences(ThemeSettings.PREFERENCE, MODE_PRIVATE).edit();
                editor.putString(ThemeSettings.CUSTOM_THEME, settings.getCustomTheme());
                editor.apply();
                updateView();

            }
        });
    }

    private void updateView() {

        AppTheme addNoteTheme = new AppTheme(AddNotes.this);


        if (settings.getCustomTheme().equals(ThemeSettings.DARK_THEME)) {

            addNoteTheme.addNoteNightTheme(true, cardView, drawerLayout,content,
                     Add_title,AddLine_note, Add_note,
                     save,  iconMenu, menu, themeModeSwitch);


        } else if (settings.getCustomTheme().equals(ThemeSettings.LIGHT_THEME)) {

            addNoteTheme.addNoteLightTheme(false, cardView, drawerLayout,content,
                    Add_title,AddLine_note, Add_note,
                    save,  iconMenu, menu, themeModeSwitch);

        } else {
            ThemeSettings.reset(getApplicationContext());

        }
    }

    private void noteInfoDialog() {

        @SuppressLint("ResourceType")
        Dialog dialog = new Dialog(this, android.R.anim.linear_interpolator);
        dialog.getWindow().setBackgroundDrawableResource(R.color.night);
        dialog.setContentView(R.layout.information_dialog);
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.setCanceledOnTouchOutside(true);
        dialog.findViewById(R.id.infoHolder).setOnClickListener(view ->{
            Toast.makeText(this, "Touch outside to dismiss dialog", Toast.LENGTH_LONG).show();
        });

        TextView time = dialog.findViewById(R.id.createdtime);
        TextView day =  dialog.findViewById(R.id.createdday);
        TextView words =  dialog.findViewById(R.id.words);
        TextView characters =  dialog.findViewById(R.id.character);
        TextView charSpace =  dialog.findViewById(R.id.char_space);


        splitTime(day,time);
        countWords(words,characters,charSpace);

        dialog.show();
    }

    public void splitTime(TextView day,TextView time){
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MMM/yyyy HH:mm:ss");
        Date date = new Date();

        try{
            String[] split = notes.getDate().split(" ");
            day.setText(split[0]);
            time.setText(split[1]);
        }
        catch(Exception e){

            String[] nullNotes = simpleDateFormat.format(date).split(" ");
            day.setText(nullNotes[0]);
            time.setText(nullNotes[1]);
            e.printStackTrace();
        }

    }

    @SuppressLint("SetTextI18n")
    public void countWords(TextView words,TextView characters,TextView noteLen){

        try{

            String note = notes.getNotes();
            StringTokenizer stringTokenizer = new StringTokenizer(note);

            String cha = note
                    .replace(" ","")
                    .replace("\n","")
                    .replace("\t","")
                    .trim();

            words.setText(Integer.toString(stringTokenizer.countTokens()));
            characters.setText(Integer.toString(cha.length()));
            noteLen.setText(Integer.toString(note.trim().replace("\n","").length()));

        }catch(Exception e){

            words.setText("0");
            characters.setText("0");
            noteLen.setText("0");
            e.printStackTrace();
        }

    }

    public void EditNotes(){
        AddLine_note.setCursorVisible(true);
        AddLine_note.setFocusableInTouchMode(true);

        Add_note.setCursorVisible(true);
        Add_note.setFocusableInTouchMode(true);

        save.setVisibility(View.VISIBLE);
        note_editing.setVisibility(View.GONE);
        Add_title.setCursorVisible(true);
        Add_title.setFocusableInTouchMode(true);
        content.setText(R.string.edit_mode);
    }

    public void Share() {
        String fileName = Add_title.getText().toString();
        String bodyText ;
       if(!Add_note.getText().toString().equals("") || !AddLine_note.getText().toString().equals("")){

            Intent intent = new Intent(android.content.Intent.ACTION_SEND);
            intent.setType("text/plain");



            if (lineScroll.getVisibility()==View.VISIBLE){
                bodyText = AddLine_note.getText().toString();
            }else{
                bodyText = Add_note.getText().toString();
            }

            intent.putExtra(Intent.EXTRA_TEXT,fileName+"\n\n"+bodyText );
            startActivity(Intent.createChooser(intent,"Share Via"));
        }else{
            Toast.makeText(this, "Blank Notes", Toast.LENGTH_SHORT).show();
        }

    }

    public void Copy() {

        if(!Add_note.getText().toString().equals("") || !AddLine_note.getText().toString().equals("")){
            String copiedText;
            if (lineScroll.getVisibility()==View.VISIBLE){
                copiedText = Add_title.getText().toString()+"\n\n"+ AddLine_note.getText().toString();
            }else{
                copiedText = Add_title.getText().toString()+"\n\n"+ Add_note.getText().toString();
            }

            ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
            ClipData data = ClipData.newPlainText("Image Copied",copiedText);
            clipboardManager.setPrimaryClip(data);
            Toast.makeText(getApplicationContext(), "Copied", Toast.LENGTH_SHORT).show();

        }



    }

    public void Download(){

        if(!Add_note.getText().toString().equals("") || !AddLine_note.getText().toString().equals("")){
            String getNote;
            if (lineScroll.getVisibility() == View.VISIBLE) {
                getNote = AddLine_note.getText().toString();
            } else {
                getNote = Add_note.getText().toString();
            }
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(CreatePath());
                fileOutputStream.write(getNote.getBytes());
                fileOutputStream.close();

                Toast.makeText(AddNotes.this, " Saved to Smart Notes File", Toast.LENGTH_LONG).show();

            } catch (FileNotFoundException e) { 
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                e.printStackTrace();
            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }else{
            Toast.makeText(this, "Blank Notes", Toast.LENGTH_SHORT).show();
        }


    }

    public void makeFolder(){

        String state = Environment.getExternalStorageState();

        if(Environment.MEDIA_MOUNTED.equals(state)) {
            File roots = Environment.getExternalStorageDirectory();
            File dir = new File(roots.getAbsolutePath() + "/Smartnotes");

            if (!dir.exists()) {
                IGNORE_RESULT(dir.mkdir());
            }

        }
    }

    @SuppressWarnings("unused")
    private static void IGNORE_RESULT(boolean b){}

    public String CreatePath(){
        String state;
        String nameOfFile = Add_title.getText().toString();
        File file=null;

        state = Environment.getExternalStorageState();

        if(Environment.MEDIA_MOUNTED.equals(state)){
            File roots = Environment.getExternalStorageDirectory();
            File dir = new File(roots.getAbsolutePath()+"/Smartnotes/My Notes");
            if(!dir.exists()){
                IGNORE_RESULT(dir.mkdir());
            }
            file = new File(dir,nameOfFile.trim()+".txt");


        }else{
            Toast.makeText(getApplicationContext(), "SD card not Found", Toast.LENGTH_SHORT).show();
        }
        assert file != null;
        return file.getPath();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1000) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();

            }
        }

    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int SPLASH_LENGTH = 300;
        switch (item.getItemId()) {
            case R.id.home:
            case R.id.onSwitch:
                break;
            case R.id.onEdit:

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        EditNotes();
                    }
                }, SPLASH_LENGTH);

                break;

            case R.id.onWordCount:

                new Handler().postDelayed(new Runnable() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void run() {

                        noteInfoDialog();

                    }
                }, SPLASH_LENGTH);

                break;

            case R.id.onShare:

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Share();
                    }
                }, SPLASH_LENGTH);


                break;
            case R.id.onCopy:

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Copy();
                    }
                }, SPLASH_LENGTH);


                break;
            case R.id.onDownload:

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Download();
                    }
                }, SPLASH_LENGTH);


                break;
            case R.id.onAddNotesDefaultTheme:
                ThemeSettings.reset(getApplicationContext());

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        finish();
                        overridePendingTransition(0, 0);
                        startActivity(getIntent());
                        overridePendingTransition(0, 0);

                    }
                }, 250);

        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(0,android.R.anim.slide_out_right);
    }
}