package com.smartdevapp.smartnote.otheractivities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.room.Room;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.button.MaterialButton;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.smartdevapp.smartnote.MainActivity;
import com.smartdevapp.smartnote.Models.Task;
import com.smartdevapp.smartnote.R;
import com.smartdevapp.smartnote.ShoppingHolder;
import com.smartdevapp.smartnote.TaskAdapter.TaskListAdapter;
import com.smartdevapp.smartnote.TaskDatabase.TaskRoomDB;
import com.smartdevapp.smartnote.TaskListener;
import com.smartdevapp.smartnote.ThemeSettings;
import com.smartdevapp.smartnote.Util.AppTheme;

import java.util.ArrayList;
import java.util.List;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class TaskMode extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    RecyclerView recyclerView;
    TaskListAdapter taskListAdapter;
    List<Task> tasks = new ArrayList<>();
    TaskRoomDB database;
    MaterialButton addTaskButton;
    ImageView iconMenu;
    Toolbar toolbar;
    Task selectedTask;
    String deletedTask,deletedTitle = null;
    DrawerLayout drawerLayout;
    TextView taskTitle;
    @SuppressLint("NotifyDataSetChanged")

    private View panelView;
    ThemeSettings settings;
    private SwitchCompat themeModeSwitch;
    Menu menu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_mode);


        recyclerView = findViewById(R.id.taskrecycler);
        addTaskButton = findViewById(R.id.addtask);
        iconMenu = findViewById(R.id.taskNav_icon);


        toolbar = findViewById(R.id.toolBar);
        drawerLayout = findViewById(R.id.taskDrawer);
        panelView = findViewById(R.id.Task_LT);
        taskTitle = findViewById(R.id.taskTheme);
        AdView mAdView = findViewById(R.id.adView);



        NavigationView navigationView = findViewById(R.id.taskNavigate);
        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(this);

        menu = navigationView.getMenu();
        themeModeSwitch = menu.findItem(R.id.onTaskSwitchTheme).getActionView().findViewById(R.id.mySwitch);



        settings = (ThemeSettings) getApplication();
        laodSharePreferences();
        iniSwitchListener();

        database = TaskRoomDB.getInstance(this);
        tasks = database.taskDataAccessObject().getAll();
        updateRecycler(tasks);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
        setSupportActionBar(toolbar);



        addTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TaskMode.this, AddTaskMode.class);
                startActivityForResult(intent, 101);

            }
        });
        findViewById(R.id.taskNav_icon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(@NonNull InitializationStatus initializationStatus) {
            }
        });

        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    private void updateRecycler(List<Task> task) {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, LinearLayoutManager.VERTICAL));
        taskListAdapter = new TaskListAdapter(TaskMode.this, task, taskListener);
        recyclerView.setAdapter(taskListAdapter);
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
                SharedPreferences.Editor editor = getSharedPreferences(ThemeSettings.PREFERENCE, MODE_PRIVATE).edit();
                editor.putString(ThemeSettings.CUSTOM_THEME, settings.getCustomTheme());
                editor.apply();
                updateView();

            }
        });
    }


    private void updateView() {

        AppTheme taskTheme = new AppTheme(TaskMode.this);


        if (settings.getCustomTheme().equals(ThemeSettings.DARK_THEME)) {

            taskTheme.taskNightTheme(true, panelView, drawerLayout,taskTitle, menu, iconMenu, addTaskButton,themeModeSwitch);


        } else if (settings.getCustomTheme().equals(ThemeSettings.LIGHT_THEME)) {

            taskTheme.taskLightTheme(false, panelView, drawerLayout,taskTitle, menu, iconMenu, addTaskButton,themeModeSwitch);


        } else {
            ThemeSettings.reset(getApplicationContext());

        }
    }

    private final TaskListener taskListener = new TaskListener() {
        @SuppressLint("NotifyDataSetChanged")

        @Override
        public void onClick(Task task, RelativeLayout relativeLayout) {
            task.setExpandable(!task.isExpandable());
            taskListAdapter.notifyDataSetChanged();
        }

        @Override
        public void onClickEdit(Task task, ImageButton imageButton) {

             Intent intent = new Intent(TaskMode.this, AddTaskMode.class);;
            intent.putExtra("old_task", task);
            startActivityForResult(intent, 102);
        }

        @Override
        public void onClickCopy(Task task, ImageButton imageButton) {

                String copiedText;

                if(task.getTask().equals("")){
                    Toast.makeText(getApplicationContext(), "blank task", Toast.LENGTH_SHORT).show();
                }else{

                    copiedText = task.getTask();
                    ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                    ClipData data = ClipData.newPlainText("Text Copied",copiedText);
                    clipboardManager.setPrimaryClip(data);
                    Toast.makeText(getApplicationContext(), "Copied", Toast.LENGTH_SHORT).show();

                }

        }

        @Override
        public void onClick(Task task, ImageView imageView) {

            if (task.isTaskchecked()) {
                database.taskDataAccessObject().check(task.getID(), false);

            } else {
                database.taskDataAccessObject().check(task.getID(), true);

            }
            NotifyDataChange();


        }

        @Override
        public void onLongClick(Task task, RelativeLayout relativeLayout) {
            selectedTask = new Task();
            selectedTask = task;

            if (selectedTask.isTasklongClicked()) {
                database.taskDataAccessObject().longClick(selectedTask.getID(), false);

            } else {
                database.taskDataAccessObject().longClick(selectedTask.getID(), true);

            }
            NotifyDataChange();


        }

    };

    @SuppressLint("NotifyDataSetChanged")
    public void NotifyDataChange() {
        tasks.clear();
        tasks.addAll(database.taskDataAccessObject().getAll());
        taskListAdapter.notifyDataSetChanged();
    }

    private int getwindowwidth() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        return metrics.widthPixels;

    }

    private void confDeleteDialog() {

        @SuppressLint("ResourceType")
        Dialog dialog = new Dialog(this, android.R.anim.linear_interpolator);
        dialog.getWindow().setBackgroundDrawableResource(R.color.readColor);
        dialog.setContentView(R.layout.confirm_deletion_shop);
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.setCanceledOnTouchOutside(true);
        dialog.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();

            }
        });

        dialog.findViewById(R.id.delete).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if(tasks.size() !=0){
                    DeleteAllList();

                }else {
                    Snackbar snackbar = Snackbar.make(recyclerView, "No Data Found To Delete", Snackbar.LENGTH_LONG);
                    snackbar.setTextColor(getResources().getColor(R.color.lite_red));
                    snackbar.setBackgroundTint(getResources().getColor(R.color.yellow));
                    snackbar.setAction("DISMISS", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            snackbar.dismiss() ;

                        }
                    });
                    snackbar.show();

                }

                dialog.dismiss();
            }

        });

        dialog.show();

    }

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT
    | ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }


        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            int position = viewHolder.getAdapterPosition();
            List<Task> archiveTaskList = new ArrayList<>();

            switch (direction){
                case ItemTouchHelper.LEFT:

                    selectedTask = taskListAdapter.getTask(position);
                    deletedTask = tasks.get(position).getTask();
                    deletedTitle = tasks.get(position).getTitle();
                    tasks.remove(position);
                    database.taskDataAccessObject().delete(selectedTask);
                    taskListAdapter.notifyDataSetChanged();

                    Snackbar snackbar = Snackbar.make(recyclerView, "DELETING "+deletedTitle,5000);
                    snackbar.setTextColor(getResources().getColor(R.color.lite_red));
                    snackbar.setBackgroundTint(getResources().getColor(R.color.Default));
                    snackbar.setAction("Undo", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            tasks.add(position,selectedTask);
                            database.taskDataAccessObject().insert(selectedTask);
                            NotifyDataChange();
                            snackbar.dismiss();
                        }
                    });
                    snackbar.show();
                    break;

                case ItemTouchHelper.RIGHT:

                    selectedTask = taskListAdapter.getTask(position);
                    archiveTaskList.add(selectedTask);
                    tasks.remove(position);
                    taskListAdapter.notifyDataSetChanged();

                    snackbar = Snackbar.make(recyclerView,
                            selectedTask.getTitle()+" TEMPORARY SHIFTED ",5000);
                    snackbar.setTextColor(getResources().getColor(R.color.dominant_color));
                    snackbar.setBackgroundTint(getResources().getColor(R.color.Default));
                    snackbar.setAction("Undo", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            archiveTaskList.remove(archiveTaskList.lastIndexOf(selectedTask));
                            tasks.add(position,selectedTask);
                            taskListAdapter.notifyDataSetChanged();


                            snackbar.dismiss();
                        }
                    });
                    snackbar.show();


                    break;

            }

        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

            new RecyclerViewSwipeDecorator.Builder(TaskMode.this,c,recyclerView,viewHolder,dX,dY,actionState,isCurrentlyActive)
                    .addSwipeLeftActionIcon(R.drawable.ic_baseline_delete_sweep_24)
                    .addSwipeLeftBackgroundColor(ContextCompat.getColor(TaskMode.this,R.color.halo_red))

                    .addSwipeRightActionIcon(R.drawable.ic_baseline_archive_24)
                    .addSwipeRightBackgroundColor(ContextCompat.getColor(TaskMode.this,R.color.dominant_color))

                    .create()
                    .decorate();
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 101) {
            if (resultCode == Activity.RESULT_OK) {
                assert data != null;
                Task new_task = (Task) data.getSerializableExtra("task");
                database.taskDataAccessObject().insert(new_task);
                tasks.clear();
                tasks.addAll(database.taskDataAccessObject().getAll());
                taskListAdapter.notifyDataSetChanged();

            }
        } else if (requestCode == 102) {
            if (resultCode == Activity.RESULT_OK) {
                Task new_task = (Task) data.getSerializableExtra("task");
                database.taskDataAccessObject().update(new_task.getID(), new_task.getTitle(),new_task.getTask());
                tasks.clear();
                tasks.addAll(database.taskDataAccessObject().getAll());
                taskListAdapter.notifyDataSetChanged();

            }
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int SPLASH_LENGTH = 300;
        switch (item.getItemId()) {
            case R.id.homeTask:

                break;

            case R.id.onTask:

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(TaskMode.this, MainActivity.class));
                        overridePendingTransition(0, android.R.anim.slide_out_right);
                        finish();
                    }
                }, SPLASH_LENGTH);

                break;

            case R.id.onPriceMode:
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(TaskMode.this, ShoppingHolder.class));
                    overridePendingTransition(0, android.R.anim.slide_out_right);
                    finish();
                }
            }, SPLASH_LENGTH);

            break;

            case R.id.onDeleteAll:

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        confDeleteDialog();

                    }
                }, SPLASH_LENGTH);


                break;

            case R.id.onTaskDefaultTheme:
                settings.reset(getApplicationContext());
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

    private void DeleteAllList() {
            database = Room.databaseBuilder(getApplicationContext(),
                    TaskRoomDB.class, "task_Database").allowMainThreadQueries().build();
            database.clearAllTables();
        NotifyDataChange();
            Toast.makeText(this, "All Task Deleted Successfully", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(0,android.R.anim.slide_out_right);

    }
}

//ca-app-pub-7881857902310901/7514154205