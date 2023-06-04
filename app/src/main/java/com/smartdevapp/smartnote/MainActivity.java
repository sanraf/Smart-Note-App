package com.smartdevapp.smartnote;



import static android.app.PendingIntent.FLAG_UPDATE_CURRENT;

import static com.smartdevapp.smartnote.R.color.lite_red;
import static com.smartdevapp.smartnote.R.color.white;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.room.Room;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.os.Environment;
import android.os.Handler;
import android.provider.Settings;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;


import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.ICSVParser;
import com.opencsv.RFC4180Parser;
import com.smartdevapp.smartnote.Adapter.NoteListAdapter;
import com.smartdevapp.smartnote.Database.MainDataAcessObject;
import com.smartdevapp.smartnote.Database.RoomDB;
import com.smartdevapp.smartnote.Models.Notes;
import com.smartdevapp.smartnote.Util.AppTheme;
import com.smartdevapp.smartnote.otheractivities.NoteNotification;
import com.smartdevapp.smartnote.otheractivities.SecureNotes;
import com.smartdevapp.smartnote.otheractivities.TaskMode;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;


public class MainActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener,
        NavigationView.OnNavigationItemSelectedListener {



    RecyclerView recyclerView;
    NoteListAdapter noteListAdapter;
    List<Notes> note = new ArrayList<>();
    RoomDB database;
    FloatingActionButton addNotesButton;
    ImageView iconMenu;
    Toolbar toolbar;
    Notes selectedNotes;
    String deletedNotes,deletedTitle = null;
    boolean passwordVisible;

    RelativeLayout notesLayout,noteLockLayout;

    private DrawerLayout drawerLayout;


    private static final int STORAGE_PERMISSION_CODE = 100;
    public static final String oldNote_KEY = "old_note";

    Menu menu;
    MenuItem menuItem;
    CollapsingToolbarLayout toolbarLayout;
    ThemeSettings settings;
    SwitchCompat themeModeSwitch;
    ImageView noteIcon,completeCheck;
    TextView note_count;

    AdView mAdView ;
    public static final String myID = "myID";
    public static final String myTitle ="myTitle";
    public static final String myNote ="myNote";
    private RewardedAd mRewardedAd;
    public static final String TAG = "SplashScreen ";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recycler);
        addNotesButton = findViewById(R.id.add);
        toolbar = findViewById(R.id.toolbar);
        iconMenu = findViewById(R.id.notesNav_icon);
        notesLayout = findViewById(R.id.notesLayout);
        noteLockLayout = findViewById(R.id.noteLockLayout);
        drawerLayout = findViewById(R.id.drawer);
        toolbarLayout = findViewById(R.id.collapsing_toolbar);
        mAdView = findViewById(R.id.adView);
        noteIcon = findViewById(R.id.noteIcon);
        note_count = findViewById(R.id.noteCount);
        completeCheck = findViewById(R.id.check_complete);

        NavigationView navigationView = findViewById(R.id.navigate);
        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(this);

        menu = navigationView.getMenu();
        themeModeSwitch = menu.findItem(R.id.onNotesSwitchTheme).getActionView().findViewById(R.id.mySwitch);

        settings = (ThemeSettings) getApplication();
        loadSharePreferences();
        iniSwitchListener();

        database = RoomDB.getInstance(this);
        note = database.mainDataAcessObject().getAll();
        updateRecycler(note);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
        setSupportActionBar(toolbar);
        getNotesCount(note_count);



        CreateChanel();
        removeAlarmIcon();
        completeCheck.setVisibility(View.GONE);

        addNotesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,AddNotes.class);
                launchNotesEdit.launch(intent);
                overridePendingTransition(0, android.R.anim.slide_out_right);

            }
        });

        findViewById(R.id.notesNav_icon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        makeFolder();

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(@NonNull InitializationStatus initializationStatus) {
            }
        });
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        loadRewardAds();

    }


    ActivityResultLauncher<Intent> launchNotesEdit = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @SuppressLint("NotifyDataSetChanged")
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == Activity.RESULT_OK && result.getData() != null){

                            Notes new_notes = (Notes) result.getData().getSerializableExtra(AddNotes.KEY_NAME);
                            database.mainDataAcessObject().insert(new_notes);
                            note.clear();
                            note.addAll(database.mainDataAcessObject().getAll());
                            noteListAdapter.notifyDataSetChanged();

                    }


                }
            }
    );

    protected void removeAlarmIcon(){
        int id = getIntent().getIntExtra(AlarmReceiver.keyID,-1);
        database.mainDataAcessObject().alarmed(id, false);
        NotifyDataChange();
    }

    @SuppressLint("SetTextI18n")
    public void getNotesCount(TextView text){

        int size = noteListAdapter.getItemCount();

        if(size==0){
            text.setText("you do not have notes");
        }else if(size >1){
            text.setText("you have "+size+" notes");
        } else{

            text.setText("you have "+size+" note");
        }

    }


    private void filter(String newText) {
        List<Notes> filterlist = new ArrayList<>();
        for (Notes singleNote : note) {
            if (singleNote.getTitle().toLowerCase().contains(newText.toLowerCase())
                    || singleNote.getNotes().toLowerCase().contains(newText.toLowerCase())) {
                filterlist.add(singleNote);
            }
        }
        noteListAdapter.filterListList(filterlist);
    }


    private void updateRecycler(List<Notes> note) {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL));
        noteListAdapter = new NoteListAdapter(MainActivity.this, note, notesListener);
        recyclerView.setAdapter(noteListAdapter);
    }

    private void showError(EditText input, String s) {

        input.setError(s);
        input.requestFocus();
    }

    private final NotesListener notesListener = new NotesListener() {
        @Override
        public void onClick(Notes notes) {


            if(notes.isLocked()){

                @SuppressLint("ResourceType")
                Dialog dialog = new Dialog(MainActivity.this,android.R.anim.linear_interpolator);
                dialog.getWindow().setBackgroundDrawableResource(R.color.tran);
                dialog.setContentView(R.layout.note_login);
                dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                dialog.setCanceledOnTouchOutside(true);

                EditText password = dialog.findViewById(R.id.myPassword);
                EditText email = dialog.findViewById(R.id.myEmail);
                password.setLongClickable(false);
                email.setLongClickable(false);

                password.setOnTouchListener(new View.OnTouchListener() {
                    @SuppressLint("ClickableViewAccessibility")
                    @Override
                    public boolean onTouch(View view, MotionEvent event) {
                        showPassword(view,event,password);
                        return false;
                    }
                });


                dialog.findViewById(R.id.login).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                            if (password.getText().toString().equals("")){

                                showError(password,"please enter password");

                            }else if (!password.getText().toString().equals(notes.getPassword())){

                                showError(password,"incorrect password");

                            }else{
                                Intent intent = new Intent(MainActivity.this, AddNotes.class);
                                intent.putExtra(oldNote_KEY, notes);
                                launchNotesEdit.launch(intent);
                                overridePendingTransition(0, android.R.anim.slide_out_right);
                                dialog.dismiss();
                            }


                    }
                });

                dialog.findViewById(R.id.nremoveepass).setOnClickListener(v->{
                    password.setVisibility(View.VISIBLE);
                    email.setVisibility(View.GONE);


                    if (password.getText().toString().equals("")){

                        showError(password,"please enter password");

                    }else if (!password.getText().toString().equals(notes.getPassword())){

                        showError(password,"incorrect password");

                    }else{

                        database.mainDataAcessObject().locked(notes.getID(),false);
                        database.mainDataAcessObject().update_security(notes.getID(),"","");
                        dialog.dismiss();
                        NotifyDataChange();
                        finish();
                        overridePendingTransition(0, 0);
                        startActivity(getIntent());
                        overridePendingTransition(0, 0);

                    }
                });

                dialog.findViewById(R.id.forgotpass).setOnClickListener(v->{

                    password.setVisibility(View.GONE);
                    email.setVisibility(View.VISIBLE);

                    if (email.getText().toString().equals("")){

                        showError(email,"please enter email");
                    }
                    else if(notes.getEmail().equals(email.getText().toString())){

                        database.mainDataAcessObject().locked(notes.getID(),false);
                        database.mainDataAcessObject().update_security(notes.getID(),"","");
                        NotifyDataChange();
                        dialog.dismiss();
                        finish();
                        overridePendingTransition(0, 0);
                        startActivity(getIntent());
                        overridePendingTransition(0, 0);

                    }else{

                        showError(email,"invalid email");

                    }

                });

                dialog.show();

            }
            else {
                Intent intent = new Intent(MainActivity.this, AddNotes.class);
                intent.putExtra(oldNote_KEY, notes);
                launchNotesEdit.launch(intent);
                overridePendingTransition(0, android.R.anim.slide_out_right);


            }

        }

        @Override
        public void onClick(Notes notes, ImageButton imageButton) {
            selectedNotes = new Notes();
            selectedNotes = notes;
            showPopup(imageButton);

        }

        @Override
        public void onClick(Notes notes, ImageView imageView) {
            selectedNotes = new Notes();
            selectedNotes = notes;

            showColor(imageView);
        }

        @Override
        public void randColor(Notes notes,MaterialCardView cardView) {
            selectedNotes = new Notes();
            selectedNotes = notes;

            if (selectedNotes.isLongClicked()) {
                database.mainDataAcessObject().longClick(selectedNotes.getID(), false);

            } else {
                database.mainDataAcessObject().longClick(selectedNotes.getID(), true);
            }
            NotifyDataChange();

        }
    };

    private void showPopup(ImageButton imageButton) {
        PopupMenu popupMenu = new PopupMenu(this, imageButton);
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.inflate(R.menu.popup_menu);
        popupMenu.show();

    }

    private void showColor(ImageView imageView) {
        PopupMenu colorMenu = new PopupMenu(this, imageView);
        colorMenu.setOnMenuItemClickListener(this);
        colorMenu.inflate(R.menu.color_menu);
        colorMenu.show();
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



    @SuppressLint("UnspecifiedImmutableFlag")
    private void cancelAlarm() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(MainActivity.this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, selectedNotes.getID(), intent, FLAG_UPDATE_CURRENT);

        if (alarmManager == null) {
            alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        }
        alarmManager.cancel(pendingIntent);

        database.mainDataAcessObject().alarmed(selectedNotes.getID(), false);

    }

    private void CreateChanel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Smart Notes Reminder";
            String description = "Chanel for Alarm Manager";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("smartappdev", name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private int getwindowwidth() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        return metrics.widthPixels;

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

        AppTheme mainTheme = new AppTheme(MainActivity.this);

        if (settings.getCustomTheme().equals(ThemeSettings.DARK_THEME)) {

            mainTheme.noteNightMode( true,toolbarLayout, notesLayout,
                     menu,  iconMenu, addNotesButton,
                     noteIcon,  themeModeSwitch,note_count);




        } else if (settings.getCustomTheme().equals(ThemeSettings.LIGHT_THEME)) {

            mainTheme.noteLightMode( false,toolbarLayout, notesLayout,
                    menu, iconMenu, addNotesButton,
                    noteIcon,  themeModeSwitch,note_count);


        } else {
            ThemeSettings.reset(getApplicationContext());

        }
    }


    private void DeleteAllNotes() {
        if (note.size() != 0) {
            RoomDB delDatabase = Room.databaseBuilder(getApplicationContext(),
                    RoomDB.class, "NoteApp").allowMainThreadQueries().build();
            delDatabase.clearAllTables();
            finish();
            overridePendingTransition(0, 0);
            startActivity(getIntent());
            overridePendingTransition(0, 0);
            Toast.makeText(this, " Notes Deleted Successfully", Toast.LENGTH_SHORT).show();
            NotifyDataChange();

        } else {
            Snackbar snackbar = Snackbar.make(notesLayout, "No Data Found To Delete", Snackbar.LENGTH_LONG);
            snackbar.setTextColor(getResources().getColor(lite_red));
            snackbar.setBackgroundTint(getResources().getColor(R.color.yellow));
            snackbar.setAction("DISMISS", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    snackbar.dismiss() ;

                }
            });
            snackbar.show();
        }

    }

    private void showRewardAds(Dialog dialog){

        if(mRewardedAd != null){
            mRewardedAd.show(MainActivity.this, new OnUserEarnedRewardListener() {
                @Override
                public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                    int amount = rewardItem.getAmount();
                    String type = rewardItem.getType();
                    Toast.makeText(MainActivity.this, "Feature Unlocked ", Toast.LENGTH_LONG).show();
                    dialog.dismiss();

                }
            });
        }else{
            Toast.makeText(MainActivity.this, "Video not ready: check internet connection", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadRewardAds() {

        AdRequest adRequest = new AdRequest.Builder().build();
        RewardedAd.load(this, SplashScreen.REWARD_AD_UNIT_ID, adRequest, new RewardedAdLoadCallback() {
            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
                mRewardedAd = null;

            }

            @Override
            public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
                super.onAdLoaded(rewardedAd);
                mRewardedAd = rewardedAd;

                rewardedAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                    @Override
                    public void onAdClicked() {
                        super.onAdClicked();
                    }

                    @Override
                    public void onAdDismissedFullScreenContent() {
                        super.onAdDismissedFullScreenContent();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                Intent intent = new Intent(MainActivity.this, SecureNotes.class);
                                intent.putExtra(myID,selectedNotes.getID());
                                startActivity(intent);
                                finish();
                                overridePendingTransition(0,android.R.anim.slide_out_right);
                            }
                        }, 200);


                    }

                    @Override
                    public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                        super.onAdFailedToShowFullScreenContent(adError);
                    }

                    @Override
                    public void onAdImpression() {
                        super.onAdImpression();

                    }

                    @Override
                    public void onAdShowedFullScreenContent() {
                        super.onAdShowedFullScreenContent();

                    }
                });
            }
        });
    }

    private void lockVideoDialog(){

        @SuppressLint("ResourceType")
        Dialog dialog = new Dialog(this, android.R.anim.linear_interpolator);
        dialog.getWindow().setBackgroundDrawableResource(R.color.tran);
        dialog.setContentView(R.layout.lock_video);
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.setCanceledOnTouchOutside(true);


        dialog.findViewById(R.id.lock_watch).setOnClickListener(v->{
            showRewardAds(dialog);
        });

        dialog.show();
    }

   private void confDeleteDialog() {
       @SuppressLint("ResourceType")
        Dialog dialog = new Dialog(this,android.R.anim.linear_interpolator);
        dialog.getWindow().setBackgroundDrawableResource(R.color.readColor);
        dialog.setContentView(R.layout.confirm_deletion);
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.setCanceledOnTouchOutside(true);

        dialog.findViewById(R.id.backup).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView title = dialog.findViewById(R.id.TitleView);
                TextView message = dialog.findViewById(R.id.message);
                BackupNotes();
                if(note.size() !=0){
                    dialog.findViewById(R.id.backup).setVisibility(View.GONE);
                    message.setText(R.string.safe_to_delete);
                    title.setText(R.string.safe_deletion_text);
                    title.setTextColor(getResources().getColor(R.color.matrix));

                }
            }
        });


        dialog.findViewById(R.id.delete).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                DeleteAllNotes();
                dialog.dismiss();
            }

        });

        dialog.show();

    }

    private void permissionDialog() {
        @SuppressLint("ResourceType")
        Dialog dialog = new Dialog(this,android.R.anim.linear_interpolator);
        dialog.setContentView(R.layout.permission_dialog);
        dialog.getWindow().setBackgroundDrawableResource(R.color.tran);
        dialog.setCanceledOnTouchOutside(true);
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        dialog.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               dialog.dismiss();


                }

        });
        dialog.findViewById(R.id.permission).setOnClickListener(v->{
            RequestAppPermission();
            dialog.dismiss();
        });


        dialog.show();



    }

    @SuppressLint("NotifyDataSetChanged")
    public void NotifyDataChange() {
        note.clear();
        note.addAll(database.mainDataAcessObject().getAll());
        noteListAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {


        getMenuInflater().inflate(R.menu.search_bar, menu);
        menuItem = menu.findItem(R.id.search_mode);
        android.widget.SearchView searchView = (android.widget.SearchView) menuItem.getActionView();
        searchView.setQueryHint("Search Notes...");
        searchView.setBackgroundColor(getResources().getColor(R.color.grayl));




        searchView.setOnQueryTextListener(new android.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filter(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return true;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }


    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.pin:
                if (selectedNotes.isPinned()) {
                    database.mainDataAcessObject().pin(selectedNotes.getID(), false);
                    Toast.makeText(this, "Unpinned", Toast.LENGTH_SHORT).show();
                } else {
                    database.mainDataAcessObject().pin(selectedNotes.getID(), true);
                    Toast.makeText(this, "Pinned", Toast.LENGTH_SHORT).show();
                }
                NotifyDataChange();
                return true;

            case R.id.reminder:
                Intent alarmIntent = new Intent(MainActivity.this, NoteNotification.class);
                alarmIntent.putExtra(myID,selectedNotes.getID());
                alarmIntent.putExtra(myTitle,selectedNotes.getTitle());
                alarmIntent.putExtra(myNote,selectedNotes.getNotes());
                startActivity(alarmIntent);
                finish();
                overridePendingTransition(0,android.R.anim.slide_out_right);
                return true;
            case R.id.cancelReminder:
                cancelAlarm();
                NotifyDataChange();
                Toast.makeText(this, "Reminder Cancelled Successfully", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.flash:
                Toast.makeText(this, selectedNotes.getNotes(), Toast.LENGTH_LONG).show();
                return true;
            case R.id.lock:
                lockVideoDialog();


                return true;
            case R.id.color_1:
                database.mainDataAcessObject().red(selectedNotes.getID(),true);
                database.mainDataAcessObject().green(selectedNotes.getID(),false);
                database.mainDataAcessObject().pink(selectedNotes.getID(),false);
                database.mainDataAcessObject().creamy(selectedNotes.getID(),false);
                database.mainDataAcessObject().yellow(selectedNotes.getID(),false);
                database.mainDataAcessObject().gray(selectedNotes.getID(),false);
                NotifyDataChange();
                return true;
            case R.id.color_2:
                database.mainDataAcessObject().green(selectedNotes.getID(),true);
                database.mainDataAcessObject().red(selectedNotes.getID(),false);
                database.mainDataAcessObject().pink(selectedNotes.getID(),false);
                database.mainDataAcessObject().creamy(selectedNotes.getID(),false);
                database.mainDataAcessObject().yellow(selectedNotes.getID(),false);
                database.mainDataAcessObject().gray(selectedNotes.getID(),false);
                NotifyDataChange();
                return true;
            case R.id.color_3:
                database.mainDataAcessObject().pink(selectedNotes.getID(),true);
                database.mainDataAcessObject().green(selectedNotes.getID(),false);
                database.mainDataAcessObject().red(selectedNotes.getID(),false);
                database.mainDataAcessObject().creamy(selectedNotes.getID(),false);
                database.mainDataAcessObject().yellow(selectedNotes.getID(),false);
                database.mainDataAcessObject().gray(selectedNotes.getID(),false);
                NotifyDataChange();
                return true;
            case R.id.color_4:
                database.mainDataAcessObject().creamy(selectedNotes.getID(),true);
                database.mainDataAcessObject().pink(selectedNotes.getID(),false);
                database.mainDataAcessObject().green(selectedNotes.getID(),false);
                database.mainDataAcessObject().red(selectedNotes.getID(),false);
                database.mainDataAcessObject().yellow(selectedNotes.getID(),false);
                database.mainDataAcessObject().gray(selectedNotes.getID(),false);
                NotifyDataChange();
                return true;
            case R.id.color_5:
                database.mainDataAcessObject().yellow(selectedNotes.getID(),true);
                database.mainDataAcessObject().creamy(selectedNotes.getID(),false);
                database.mainDataAcessObject().pink(selectedNotes.getID(),false);
                database.mainDataAcessObject().green(selectedNotes.getID(),false);
                database.mainDataAcessObject().red(selectedNotes.getID(),false);
                database.mainDataAcessObject().gray(selectedNotes.getID(),false);
                NotifyDataChange();
                return true;
            case R.id.color_6:
                database.mainDataAcessObject().gray(selectedNotes.getID(),true);
                database.mainDataAcessObject().yellow(selectedNotes.getID(),false);
                database.mainDataAcessObject().creamy(selectedNotes.getID(),false);
                database.mainDataAcessObject().pink(selectedNotes.getID(),false);
                database.mainDataAcessObject().green(selectedNotes.getID(),false);
                database.mainDataAcessObject().red(selectedNotes.getID(),false);
                NotifyDataChange();
                return true;
            case R.id.mybold:
                database.mainDataAcessObject().bold(selectedNotes.getID(),true);
                database.mainDataAcessObject().italic(selectedNotes.getID(),false);
                database.mainDataAcessObject().cursive(selectedNotes.getID(),false);
                database.mainDataAcessObject().casual(selectedNotes.getID(),false);
                database.mainDataAcessObject().monospace(selectedNotes.getID(),false);
                NotifyDataChange();
                return true;
            case R.id.myItalic:
                database.mainDataAcessObject().italic(selectedNotes.getID(),true);
                database.mainDataAcessObject().bold(selectedNotes.getID(),false);
                database.mainDataAcessObject().cursive(selectedNotes.getID(),false);
                database.mainDataAcessObject().casual(selectedNotes.getID(),false);
                database.mainDataAcessObject().monospace(selectedNotes.getID(),false);
                NotifyDataChange();
                return true;
            case R.id.mycursive:
                database.mainDataAcessObject().cursive(selectedNotes.getID(),true);
                database.mainDataAcessObject().italic(selectedNotes.getID(),false);
                database.mainDataAcessObject().bold(selectedNotes.getID(),false);
                database.mainDataAcessObject().monospace(selectedNotes.getID(),false);
                database.mainDataAcessObject().casual(selectedNotes.getID(),false);
                NotifyDataChange();
                return true;
            case R.id.mycasual:
                database.mainDataAcessObject().casual(selectedNotes.getID(),true);
                database.mainDataAcessObject().monospace(selectedNotes.getID(),false);
                database.mainDataAcessObject().italic(selectedNotes.getID(),false);
                database.mainDataAcessObject().cursive(selectedNotes.getID(),false);
                database.mainDataAcessObject().bold(selectedNotes.getID(),false);
                NotifyDataChange();
                return true;
            case R.id.mymonospace:
                database.mainDataAcessObject().monospace(selectedNotes.getID(),true);
                database.mainDataAcessObject().casual(selectedNotes.getID(),false);
                database.mainDataAcessObject().italic(selectedNotes.getID(),false);
                database.mainDataAcessObject().cursive(selectedNotes.getID(),false);
                database.mainDataAcessObject().bold(selectedNotes.getID(),false);
                NotifyDataChange();
                return true;
            case R.id.reset:
                resetColor();
                return true;

            default:
                return false;
        }

    }

    private void resetColor() {


        database.mainDataAcessObject().red(selectedNotes.getID(),false);
        database.mainDataAcessObject().green(selectedNotes.getID(),false);
        database.mainDataAcessObject().pink(selectedNotes.getID(),false);
        database.mainDataAcessObject().creamy(selectedNotes.getID(),false);
        database.mainDataAcessObject().yellow(selectedNotes.getID(),false);
        database.mainDataAcessObject().gray(selectedNotes.getID(),false);

        database.mainDataAcessObject().bold(selectedNotes.getID(),false);
        database.mainDataAcessObject().italic(selectedNotes.getID(),false);
        database.mainDataAcessObject().cursive(selectedNotes.getID(),false);
        database.mainDataAcessObject().casual(selectedNotes.getID(),false);
        database.mainDataAcessObject().monospace(selectedNotes.getID(),false);
        NotifyDataChange();
    }

    @SuppressLint({"NonConstantResourceId"})
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {


        int SPLASH_LENGTH = 400;
        switch (item.getItemId()) {
            case R.id.home:

                break;

            case R.id.to_do:

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(MainActivity.this, ShoppingHolder.class));
                        overridePendingTransition(0, android.R.anim.slide_out_right);
                    }
                }, SPLASH_LENGTH);

                break;

            case R.id.onTaskmainMode:

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(MainActivity.this, TaskMode.class));
                        overridePendingTransition(0, android.R.anim.slide_out_right);

                    }
                }, SPLASH_LENGTH);

                break;
            case R.id.DeleteAll:
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        confDeleteDialog();

                    }
                }, SPLASH_LENGTH);

                break;

            case R.id.Backup:
                BackupNotes();

                break;

            case R.id.Restore:


                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (checkPermission()) {

                            importCSV();
                            onResume();


                        } else {

                            permissionDialog();

                        }

                    }
                }, SPLASH_LENGTH);

                break;

            case R.id.onNotesDefaultTheme:

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


                break;

        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;

    }

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT
            | ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @SuppressLint("NotifyDataSetChanged")
        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            int position = viewHolder.getAdapterPosition();
            List<Notes> archiveNoteList = new ArrayList<>();

            switch (direction){
                case ItemTouchHelper.LEFT:

                    selectedNotes = noteListAdapter.getNotes(position);
                    deletedNotes = note.get(position).getNotes();
                    deletedTitle = note.get(position).getTitle();
                    note.remove(position);
                    database.mainDataAcessObject().delete(selectedNotes);
                    NotifyDataChange();
                    cancelAlarm();

                    Snackbar snackbar = Snackbar.make(mAdView, "DELETING "+deletedTitle.toUpperCase(), 5000);
                    snackbar.setTextColor(getResources().getColor(lite_red));
                    snackbar.setBackgroundTint(getResources().getColor(R.color.Default));
                    NotifyDataChange();
                    cancelAlarm();
                    snackbar.setAction("Undo", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            note.add(position,selectedNotes);
                            database.mainDataAcessObject().insert(selectedNotes);
                            noteListAdapter.notifyDataSetChanged();
                            snackbar.dismiss();
                            NotifyDataChange();
                          
                        }
                    });
                    snackbar.show();
                    break;

                case ItemTouchHelper.RIGHT:

                    selectedNotes = noteListAdapter.getNotes(position);
                    archiveNoteList.add(selectedNotes);
                    note.remove(position);
                    noteListAdapter.notifyDataSetChanged();

                    snackbar = Snackbar.make(mAdView,
                            selectedNotes.getTitle()+" TEMPORARY SHIFTED ", 5000);
                    snackbar.setTextColor(getResources().getColor(R.color.dominant_color));
                    snackbar.setBackgroundTint(getResources().getColor(R.color.Default));
                    snackbar.setAction("Undo", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            archiveNoteList.remove(archiveNoteList.lastIndexOf(selectedNotes));
                            note.add(position,selectedNotes);
                            noteListAdapter.notifyDataSetChanged();


                            snackbar.dismiss();
                        }
                    });
                    snackbar.show();


                    break;

            }

        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

            new RecyclerViewSwipeDecorator.Builder(MainActivity.this,c,recyclerView,viewHolder,dX,dY,actionState,isCurrentlyActive)
                    .addSwipeLeftActionIcon(R.drawable.ic_baseline_delete_sweep_24)
                    .addSwipeLeftBackgroundColor(ContextCompat.getColor(MainActivity.this,R.color.halo_red))
                    .addSwipeRightActionIcon(R.drawable.ic_baseline_archive_24)
                    .addSwipeRightBackgroundColor(ContextCompat.getColor(MainActivity.this,R.color.dominant_color))
                    .create()
                    .decorate();
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }

    };


    //backup , restore and app permission//////////////////////////////////////////////////////////////////////////////////////////////////

    @SuppressLint("MissingSuperCall")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0) {
                boolean write = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                boolean read = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                if (write && read) {
                    Toast.makeText(this, "Storage Permission Granted", Toast.LENGTH_SHORT).show();
                } else {
                    permissionDialog();

                }
            }
        }

    }



    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public void RequestAppPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {

            try {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                Uri uri = Uri.fromParts("package", this.getPackageName(), null);
                intent.setData(uri);
                storageActivityResultLauncher.launch(intent);



            } catch (Exception e) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                storageActivityResultLauncher.launch(intent);



            }
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission
                    .WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        }
    }

    private final ActivityResultLauncher<Intent> storageActivityResultLauncher = registerForActivityResult(new
            ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {


        @Override
        public void onActivityResult(ActivityResult result) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                if (Environment.isExternalStorageManager()) {

                    makeFolder();
                    Toast.makeText(MainActivity.this, "Storage Permission Granted", Toast.LENGTH_LONG).show();
                } else {

                    permissionDialog();

                }
            } else {
                Toast.makeText(MainActivity.this, "Your Android Device Version Is Below 11", Toast.LENGTH_LONG).show();
            }
        }
    });

    public boolean checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            return Environment.isExternalStorageManager();
        } else {
            int write = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            int read = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
            return write == PackageManager.PERMISSION_GRANTED && read == PackageManager.PERMISSION_GRANTED;

        }
    }

    public void makeFolder() {

        String state = Environment.getExternalStorageState();

        if (Environment.MEDIA_MOUNTED.equals(state)) {
            File roots = Environment.getExternalStorageDirectory();
            File dir = new File(roots.getAbsolutePath() + "/Smartnotes");

            if (!dir.exists()) {
                IGNORE_RESULT(dir.mkdir());
            }

        }
    }
    @SuppressWarnings("unused")
    private static void IGNORE_RESULT(boolean b){}

    private void importCSV() {
        String filePathAndName = Environment.getExternalStorageDirectory() + "/" + "Smartnotes/notesBackup/" + "Notes_Backup.csv.eslock";
        File csvFile = new File(filePathAndName);
        EncryptsDecrypt encr = new EncryptsDecrypt();
        if (csvFile.exists() && checkPermission()) {

            try {
                CSVReader csvReader = new CSVReader(new FileReader(csvFile.getAbsoluteFile()));
                String[] nextLine;
                while ((nextLine = csvReader.readNext()) != null) {
                    int IDs = Integer.parseInt(nextLine[0]);
                    String note_title = nextLine[1].replace("╪",",").replace("\b","\n");
                    String note_description = nextLine[2].replace("╪",",").replace("\b","\n");
                    String note_time = nextLine[3];
                    String note_email = encr.emailDecryption(nextLine[4]);
                    String note_password = encr.passwordDecryption(nextLine[5]);

                    RoomDB db = Room.databaseBuilder(getApplicationContext(),
                            RoomDB.class, "NoteApp").allowMainThreadQueries().build();
                    MainDataAcessObject mainDataAcessObject = db.mainDataAcessObject();
                    mainDataAcessObject.insert(new Notes(IDs, note_title, note_description , note_time));
                    mainDataAcessObject.update_security(IDs, note_email, note_password);

                    if (note_password.length()>4){
                        database.mainDataAcessObject().locked(IDs,true);
                    }else {
                        database.mainDataAcessObject().locked(IDs,false);
                    }

                }
                finish();
                overridePendingTransition(0, 0);
                startActivity(getIntent());
                overridePendingTransition(0, 0);
                Toast.makeText(this, "Backup Restored Successfully", Toast.LENGTH_SHORT).show();
                completeCheck.setVisibility(View.VISIBLE);

            } catch (Exception e) {
                Toast.makeText(this,e.getMessage(), Toast.LENGTH_LONG).show();
            }
        } else {
            Snackbar snackbar = Snackbar.make(notesLayout, "No Backup File Found", Snackbar.LENGTH_LONG);
            snackbar.setTextColor(getResources().getColor(lite_red));
            snackbar.setBackgroundTint(getResources().getColor(R.color.yellow));
            snackbar.setAction("DISMISS", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    snackbar.dismiss();
                }
            });
            snackbar.show();

        }

    }

    private void exportCSV() {
            EncryptsDecrypt encr = new EncryptsDecrypt();
        File folder = new File(Environment.getExternalStorageDirectory() + "/" + "Smartnotes/notesBackup");

        if (!folder.exists()) {

            IGNORE_RESULT(folder.mkdir());

        }

        String csvFileName = "Notes_Backup.csv.eslock";
        String filePathName = folder + "/" + csvFileName;

        ArrayList<Notes> recordList;
         //recordList.clear();
        RoomDB roomDB = Room.databaseBuilder(getApplicationContext(),
                RoomDB.class, "NoteApp").allowMainThreadQueries().build();
        MainDataAcessObject mainDataAcessObject = roomDB.mainDataAcessObject();
        List<Notes> dbNotes = mainDataAcessObject.getAll();
        recordList = (ArrayList<Notes>) dbNotes;

        try {
            

            ICSVParser parser = new RFC4180Parser();


            FileWriter fileWriter = new FileWriter(filePathName);
            for (int i = 0; i < recordList.size(); i++) {
                fileWriter.append("").append(String.valueOf(recordList.get(i).getID()));
                fileWriter.append(",");
                fileWriter.append("").append(recordList.get(i).getTitle().replace(",","╪").
                        replace("\n","\b"));
                fileWriter.append(",");
                fileWriter.append("").append(recordList.get(i).getNotes().replace(",","╪").
                        replace("\n","\b"));
                fileWriter.append(",");
                fileWriter.append("").append(recordList.get(i).getDate());
                fileWriter.append(",");
                String em = recordList.get(i).getEmail();
                fileWriter.append("").append(encr.emailEncryption(em));
                fileWriter.append(",");
                String pass  = recordList.get(i).getPassword();
                fileWriter.append("").append(encr.passwordEncryption(pass));
                fileWriter.append("\n");



            }
            fileWriter.flush();
            fileWriter.close();
            Toast.makeText(this, "Backup File to " + filePathName, Toast.LENGTH_LONG).show();
        } catch (Exception er) {
            Toast.makeText(this, er.getMessage(), Toast.LENGTH_LONG).show();

        }

    }

    private void BackupNotes() {

        try{

            if (note.size() != 0) {

                if (checkPermission()) {

                    exportCSV();
                } else {
                    permissionDialog();

                }

            } else {
                Snackbar snackbar = Snackbar.make(notesLayout, "No Data Found To Backup".toUpperCase(Locale.ROOT), Snackbar.LENGTH_LONG);
                snackbar.setTextColor(getResources().getColor(lite_red));
                snackbar.setBackgroundTint(getResources().getColor(R.color.yellow));
                snackbar.setAction("DISMISS", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        snackbar.dismiss();
                    }
                });
                snackbar.show();
            }
        }catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(0,android.R.anim.slide_out_right);
    }



}
//Ads ID = ca-app-pub-7881857902310901/4818604454
//alarm ads ID = ca-app-pub-7881857902310901/4508716912
