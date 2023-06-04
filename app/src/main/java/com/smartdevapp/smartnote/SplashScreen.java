package com.smartdevapp.smartnote;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.InstallStateUpdatedListener;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.InstallStatus;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.review.ReviewInfo;
import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.review.ReviewManagerFactory;
import com.google.android.play.core.tasks.OnCompleteListener;
import com.google.android.play.core.tasks.OnSuccessListener;
import com.google.android.play.core.tasks.Task;
import com.smartdevapp.smartnote.Util.AppTheme;
import com.smartdevapp.smartnote.otheractivities.TaskMode;

import java.text.SimpleDateFormat;
import java.util.Calendar;


@SuppressLint("CustomSplashScreen")
public class SplashScreen extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    Calendar calendar;
    String getDate, getWeekDay, getMonth;
    int getYear;
    SimpleDateFormat simpleMonthFormat, simpleDayFormat, simpleDateFormat;
    LinedEditText linedEditText;
    String designText = "Smart Note App";

    private DrawerLayout drawerLayout;
    private RelativeLayout homeButtonPanel;
    FloatingActionButton openNotes, openList, openTask, openDrawer;
    Button close;
    Toolbar toolbar;

    Menu menu;
    ThemeSettings settings;
    private SwitchCompat themeModeSwitch;

    TextView titleNote, titleList, titleTask, titleDrawer, homeScreenTitle;
    ImageView circlePin,listIcon,viewIcon,settingIcon;
    LinearLayout view_1,view_2,view_3,view_4;

    public static int UPDATE_CODE = 42;
    AppUpdateManager appUpdateManager;
    private ReviewInfo reviewInfo;
    private ReviewManager reviewManager;

    public static final String REWARD_AD_UNIT_ID = "ca-app-pub-3940256099942544/5224354917";//ca-app-pub-7881857902310901/3254314067

    private RewardedAd mRewardedAd;
    public static final String TAG = "SplashScreen ";


    @SuppressLint({"SimpleDateFormat", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        titleNote = findViewById(R.id.notes);
        titleList = findViewById(R.id.Pricenotes);
        titleTask = findViewById(R.id.taskNote);
        toolbar = findViewById(R.id.toolbar);
        titleDrawer = findViewById(R.id.nav_drawer);
        close = findViewById(R.id.back);
        homeScreenTitle = findViewById(R.id.homescreen_text);

        circlePin = findViewById(R.id.pin);
        listIcon = findViewById(R.id.list);
        viewIcon = findViewById(R.id.view);
        settingIcon = findViewById(R.id.settingIcon);

        homeButtonPanel = findViewById(R.id.homeButtonPanel);
        RelativeLayout cardView = findViewById(R.id.cardviewHome);
        AdView mAdView = findViewById(R.id.adView);

        view_1 = findViewById(R.id.lt1);
        view_2 = findViewById(R.id.lt2);
        view_3 = findViewById(R.id.lt3);
        view_4 = findViewById(R.id.lt4);

        linedEditText = findViewById(R.id.linedEditText);
        linedEditText.setEnabled(false);
        linedEditText.setFocusable(false);

        drawerLayout = findViewById(R.id.home_drawer);
        openNotes = findViewById(R.id.openNotes);
        openList = findViewById(R.id.openList);
        openTask = findViewById(R.id.openTask);
        openDrawer = findViewById(R.id.openDrawer);

        NavigationView navigationView = findViewById(R.id.navigate_home);
        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(this);

        menu = navigationView.getMenu();
        themeModeSwitch = menu.findItem(R.id.onHomeSwitchTheme).getActionView().findViewById(R.id.mySwitch);
        themeModeSwitch.setVisibility(View.GONE);


        settings = (ThemeSettings) getApplication();
        laodSharePreferences();
        iniSwitchListener();
        setSupportActionBar(toolbar);
        inAppUpdate();
        activeReviewInfo();


        calendar = Calendar.getInstance();

        simpleDayFormat = new SimpleDateFormat("EEEE");
        simpleMonthFormat = new SimpleDateFormat("MMMM");
        simpleDateFormat = new SimpleDateFormat("dd");
        getYear = calendar.get(Calendar.YEAR);
        getWeekDay = simpleDayFormat.format(calendar.getTime());
        getDate = simpleDateFormat.format(calendar.getTime());
        getMonth = simpleMonthFormat.format(calendar.getTime());


        String getAllDate = "\n" + designText + "\n\n" + getWeekDay + "\t\t" + getDate + "\n\n" + getYear + "\t\t" + getMonth;


        linedEditText.setText(getAllDate);

        openDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        openNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(SplashScreen.this, MainActivity.class));
                overridePendingTransition(0, android.R.anim.fade_out);
            }
        });

        openList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SplashScreen.this, ShoppingHolder.class));
                overridePendingTransition(0, android.R.anim.slide_out_right);
            }
        });

        openTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SplashScreen.this, TaskMode.class));
                overridePendingTransition(0, android.R.anim.slide_out_right);

            }
        });


        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(@NonNull InitializationStatus initializationStatus) {
            }
        });


        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        loadRewardAds();
    }

    private void showRewardAds(Dialog dialog){

        if(mRewardedAd != null){
            mRewardedAd.show(SplashScreen.this, new OnUserEarnedRewardListener() {
                @Override
                public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                    int amount = rewardItem.getAmount();
                    String type = rewardItem.getType();
                    Toast.makeText(SplashScreen.this, "Themes Unlocked ", Toast.LENGTH_LONG).show();
                    dialog.dismiss();
                }
            });
        }else{
            Toast.makeText(SplashScreen.this, "Video not ready: check internet connection", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadRewardAds() {

        AdRequest adRequest = new AdRequest.Builder().build();
        RewardedAd.load(this, REWARD_AD_UNIT_ID, adRequest, new RewardedAdLoadCallback() {
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
                        themeModeSwitch.setVisibility(View.VISIBLE);
                        themeModeSwitch.setThumbTintList(ColorStateList.valueOf(getResources().getColor(R.color.matrix)));
                        themeModeSwitch.setTrackTintList(ColorStateList.valueOf(getResources().getColor(R.color.lite_red)));

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

    public void appShare(){

        try{
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_SUBJECT,"Share Smart Note App");
            String shareMessage = "https://play.google.com/store/apps/details?id="+BuildConfig.APPLICATION_ID+"\n\n";
            intent.putExtra(Intent.EXTRA_TEXT,shareMessage);
            startActivity(Intent.createChooser(intent,"share by"));

        }
        catch(Exception e){
            Toast.makeText(SplashScreen.this, "Error occurred", Toast.LENGTH_SHORT).show();
        }
    }



    @SuppressLint({"NonConstantResourceId"})
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int SPLASH_LENGTH = 400;
        switch (item.getItemId()) {
            case R.id.home:
                drawerLayout.closeDrawer(GravityCompat.START);
                break;


            case R.id.onNotesmainMode:

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(SplashScreen.this, MainActivity.class));
                        overridePendingTransition(0, android.R.anim.slide_out_right);
                    }
                }, SPLASH_LENGTH);
                drawerLayout.closeDrawer(GravityCompat.START);

                break;

            case R.id.to_do:

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(SplashScreen.this, ShoppingHolder.class));
                        overridePendingTransition(0, android.R.anim.slide_out_right);
                    }
                }, SPLASH_LENGTH);
                drawerLayout.closeDrawer(GravityCompat.START);

                break;

            case R.id.onTaskmainMode:

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(SplashScreen.this, TaskMode.class));
                        overridePendingTransition(0, android.R.anim.slide_out_right);

                    }
                }, SPLASH_LENGTH);
                drawerLayout.closeDrawer(GravityCompat.START);

                break;

            case R.id.onHomeSwitchTheme:


                       videoDialog();


                break;


            case R.id.onHomeDefaultTheme:
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
                drawerLayout.closeDrawer(GravityCompat.START);

                break;

            case R.id.aboutApp:

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(SplashScreen.this, AboutTheApp.class));
                        overridePendingTransition(0, android.R.anim.slide_out_right);
                    }
                }, SPLASH_LENGTH);
                drawerLayout.closeDrawer(GravityCompat.START);

                break;

            case R.id.help:

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(SplashScreen.this, Help.class));
                        overridePendingTransition(0, android.R.anim.slide_out_right);
                    }
                }, SPLASH_LENGTH);
                drawerLayout.closeDrawer(GravityCompat.START);

                break;

            case R.id.rate:

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //startReviewFlow();
                        Toast.makeText(SplashScreen.this, "Not Available Util Publish", Toast.LENGTH_SHORT).show();
                        //overridePendingTransition(0, android.R.anim.slide_out_right);
                    }
                }, SPLASH_LENGTH);
                drawerLayout.closeDrawer(GravityCompat.START);

                break;
            case R.id.share:

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //appShare();
                        Toast.makeText(SplashScreen.this, "Not Available Util Publish ", Toast.LENGTH_SHORT).show();

                    }
                }, SPLASH_LENGTH);
                drawerLayout.closeDrawer(GravityCompat.START);

                break;

        }


        return true;

    }


    private void laodSharePreferences() {

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


                        finish();
                        overridePendingTransition(0,0);
                        startActivity(getIntent());
                        overridePendingTransition(0,0);

                drawerLayout.closeDrawer(GravityCompat.START);
                SharedPreferences.Editor editor = getSharedPreferences(ThemeSettings.PREFERENCE, MODE_PRIVATE).edit();
                editor.putString(ThemeSettings.CUSTOM_THEME, settings.getCustomTheme());
                editor.apply();
                updateView();

            }
        });

    }

    private void updateView() {

        AppTheme splashTheme = new AppTheme(SplashScreen.this);

        if (settings.getCustomTheme().equals(ThemeSettings.DARK_THEME)) {

            splashTheme.splashNightTheme(true , titleNote, titleList,  titleTask, titleDrawer, homeScreenTitle,
                    linedEditText, drawerLayout, homeButtonPanel,  openNotes, openList,openTask,
                    openDrawer, circlePin, listIcon, viewIcon, settingIcon,  view_1, view_2, view_3,
                    view_4,menu, themeModeSwitch);
            themeModeSwitch.setVisibility(View.GONE);



        } else if (settings.getCustomTheme().equals(ThemeSettings.LIGHT_THEME)) {

            splashTheme.splashLightTheme(false , titleNote, titleList,  titleTask, titleDrawer, homeScreenTitle,
                    linedEditText, drawerLayout, homeButtonPanel,  openNotes, openList,openTask,
                    openDrawer, circlePin, listIcon, viewIcon, settingIcon,  view_1, view_2, view_3,
                     view_4,menu, themeModeSwitch);
            themeModeSwitch.setVisibility(View.GONE);

        } else {
            ThemeSettings.reset(getApplicationContext());
            themeModeSwitch.setVisibility(View.GONE);

        }
    }

    private void videoDialog(){

        @SuppressLint("ResourceType")
        Dialog dialog = new Dialog(this, android.R.anim.linear_interpolator);
        dialog.getWindow().setBackgroundDrawableResource(R.color.tran);
        dialog.setContentView(R.layout.watch_video);
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.setCanceledOnTouchOutside(true);

        dialog.findViewById(R.id.watch).setOnClickListener(v->{
            Toast.makeText(this, "Nothing to Watch", Toast.LENGTH_SHORT).show();
        });

        dialog.findViewById(R.id.watch).setOnClickListener(v->{
            showRewardAds(dialog);
        });

        dialog.show();
    }






    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            drawerLayout.closeDrawer(GravityCompat.START);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    moveTaskToBack(true);
                    android.os.Process.killProcess(android.os.Process.myPid());
                    System.exit(1);
                }
            }, 1000);

            super.onBackPressed();


        }

        //showExitDialog();

    }

    private void inAppUpdate() {
        appUpdateManager = AppUpdateManagerFactory.create(this);

        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();
        appUpdateInfoTask.addOnSuccessListener(new OnSuccessListener<AppUpdateInfo>() {
            @Override
            public void onSuccess(AppUpdateInfo appUpdateInfo) {
                if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                        && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)) {
                    try {
                        appUpdateManager.startUpdateFlowForResult(appUpdateInfo, AppUpdateType.FLEXIBLE,
                                SplashScreen.this, UPDATE_CODE);


                    } catch (IntentSender.SendIntentException e) {
                        e.printStackTrace();
                        Log.d("UpdateError", "OnSuccess " + e);
                    }
                }
            }
        });
        appUpdateManager.registerListener(listener);
    }

    InstallStateUpdatedListener listener = installState -> {
        if (installState.installStatus() == InstallStatus.DOWNLOADED) {
            popDialog();
        }
    };

    @Override
    protected void onStop() {
        if (appUpdateManager != null) appUpdateManager.unregisterListener(listener);
        super.onStop();
    }

    private void popDialog() {
        Snackbar snackbar = Snackbar.make(drawerLayout, "Updating", Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction("Install", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appUpdateManager.completeUpdate();
            }
        });
        snackbar.setTextColor(Color.parseColor("#ff0000"));
        snackbar.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == UPDATE_CODE && requestCode != RESULT_OK) {
            Toast.makeText(this, "Cancel", Toast.LENGTH_SHORT).show();
        }
    }

    private void activeReviewInfo() {
        reviewManager = ReviewManagerFactory.create(this);
        Task<ReviewInfo> reviewInfoTask = reviewManager.requestReviewFlow();
        reviewInfoTask.addOnCompleteListener(new OnCompleteListener<ReviewInfo>() {
            @Override
            public void onComplete(@NonNull Task<ReviewInfo> task) {
                if (task.isSuccessful()){
                    reviewInfo = task.getResult();
                }else{
                    Toast.makeText(SplashScreen.this, "Review failed to launch", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
    private void startReviewFlow(){
        if (reviewInfo !=null){
            Task<Void> flow = reviewManager.launchReviewFlow(SplashScreen.this,reviewInfo);
            flow.addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Toast.makeText(SplashScreen.this, "Rating is Completed", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}

//App ID = ca-app-pub-7881857902310901~5138400169
//Ads ID = ca-app-pub-7881857902310901/8285391965
//feature ID = ca-app-pub-7881857902310901/3254314067