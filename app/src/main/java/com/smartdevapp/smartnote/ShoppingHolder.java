package com.smartdevapp.smartnote;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
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
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
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
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.smartdevapp.smartnote.Models.Shopping;
import com.smartdevapp.smartnote.ShopAdapter.ShopListAdapter;
import com.smartdevapp.smartnote.ShopDatabase.ShopRoomDB;
import com.smartdevapp.smartnote.Util.AppTheme;
import com.smartdevapp.smartnote.otheractivities.SlipMode;
import com.smartdevapp.smartnote.otheractivities.TaskMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class ShoppingHolder extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    RecyclerView recyclerView;
    ShopListAdapter shopListAdapter;
    List<Shopping> shopping = new ArrayList<>();
    ShopRoomDB database;
    String deletedList,deletedTitle = null;

    FloatingActionButton addItemButton;
    ImageView iconMenu;
    MaterialButton getTotal;
    Toolbar toolbar;
    Shopping selectedItem;
    DrawerLayout drawerLayout;


    private View panelView;
    private TextView item, quantity, cost, price,title;
    private SwitchCompat themeModeSwitch;
    ThemeSettings settings;
    Menu menu;

    private CardView cardView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_holder);

        recyclerView = findViewById(R.id.itemrecycler);
        addItemButton = findViewById(R.id.additem);
        getTotal = findViewById(R.id.getTotal);
        iconMenu = findViewById(R.id.shopNav_icon);
        AdView mAdView = findViewById(R.id.adView);


        toolbar = findViewById(R.id.toolBar);
        drawerLayout = findViewById(R.id.shopDrawer);
        iniWidgetsForTheme();

        NavigationView navigationView = findViewById(R.id.shopNavigate);
        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(this);

        menu = navigationView.getMenu();
        themeModeSwitch = menu.findItem(R.id.onSwitchTheme).getActionView().findViewById(R.id.mySwitch);

        settings = (ThemeSettings) getApplication();
        laodSharePreferences();

        iniSwitchListener();

        database = ShopRoomDB.getInstance(this);
        shopping = database.shopDataAccessObject().getAll();
        updateRecycler(shopping);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
        setSupportActionBar(toolbar);


        addItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShoppingHolder.this, AddList.class);
                startActivityForResult(intent, 101);

            }
        });

        getTotal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTotalDialog();
            }
        });

        iconMenu.setOnClickListener(new View.OnClickListener() {
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

    private void showTotalDialog() {


        Dialog dialog = new Dialog(this, android.R.anim.linear_interpolator);
        dialog.getWindow().setBackgroundDrawableResource(R.color.night);
        dialog.setContentView(R.layout.total_dialog);
        dialog.setCanceledOnTouchOutside(true);
        //dialog

        dialog.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);

        TextView totalCost = dialog.findViewById(R.id.total);
        TextView totalItems = dialog.findViewById(R.id.totalItems);
        InsertTotal(totalCost,totalItems);

        /*dialog.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });*/


        dialog.show();
    }


    private void DeleteAllList() {
            database = Room.databaseBuilder(getApplicationContext(),
                    ShopRoomDB.class, "Shopping_Database").allowMainThreadQueries().build();
            database.clearAllTables();
        NotifyDataChange();
            Toast.makeText(this, "All List Deleted Successfully", Toast.LENGTH_SHORT).show();

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

        AppTheme shopTheme = new AppTheme(ShoppingHolder.this);


        if (settings.getCustomTheme().equals(ThemeSettings.DARK_THEME)) {

            shopTheme.shopNightTheme(true, panelView, drawerLayout, item,
                    quantity,price, title,
                    iconMenu, addItemButton,getTotal, menu,
                    themeModeSwitch);


        } else if (settings.getCustomTheme().equals(ThemeSettings.LIGHT_THEME)) {

            shopTheme.shopLightTheme(false, panelView, drawerLayout, item,
                    quantity,price, title,
                     iconMenu, addItemButton,getTotal, menu,
                    themeModeSwitch);


        } else {
            settings.reset(getApplicationContext());

        }
    }

    private void iniWidgetsForTheme() {

        panelView = findViewById(R.id.LT);
        item = findViewById(R.id.itemTheme);
        quantity = findViewById(R.id.quantityTheme);
        price = findViewById(R.id.priceTag);
        title = findViewById(R.id.title);


    }

    private void updateRecycler(List<Shopping> shopping) {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, LinearLayoutManager.VERTICAL));
        shopListAdapter = new ShopListAdapter(ShoppingHolder.this, shopping, shopListener);
        recyclerView.setAdapter(shopListAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 101) {
            if (resultCode == Activity.RESULT_OK) {
                Shopping new_item = (Shopping) data.getSerializableExtra("item");
                database.shopDataAccessObject().insert(new_item);
                shopping.clear();
                shopping.addAll(database.shopDataAccessObject().getAll());
                shopListAdapter.notifyDataSetChanged();

            }
        } else if (requestCode == 102) {
            if (resultCode == Activity.RESULT_OK) {
                Shopping new_item = (Shopping) data.getSerializableExtra("item");
                database.shopDataAccessObject().update(new_item.getID(), new_item.getItem(), new_item.getQuantity(), new_item.getPrice(), new_item.getTotalPrice());
                shopping.clear();
                shopping.addAll(database.shopDataAccessObject().getAll());
                shopListAdapter.notifyDataSetChanged();

            }
        }
    }

    private final ShopListener shopListener = new ShopListener() {
        @Override
        public void onClick(Shopping shopping) {
            Intent intent = new Intent(ShoppingHolder.this, AddList.class);
            intent.putExtra("old_item", shopping);
            startActivityForResult(intent, 102);

        }

        @Override
        public void onClick(Shopping shoppin, ImageButton imageButton) {
            /*database.shopDataAccessObject().delete(shoppin);
            shopping.remove(shoppin);
            shopListAdapter.notifyDataSetChanged();
            InsertTotal();
            Toast.makeText(getApplication(), "Item Deleted Successfully", Toast.LENGTH_SHORT).show();*/

            selectedItem = new Shopping();
            selectedItem = shoppin;

            if (selectedItem.isLongClicked()) {
                database.shopDataAccessObject().longClick(selectedItem.getID(), false);
                Toast.makeText(ShoppingHolder.this, "Low Priority", Toast.LENGTH_SHORT).show();

            } else {
                database.shopDataAccessObject().longClick(selectedItem.getID(), true);
                Toast.makeText(ShoppingHolder.this, "High Priority", Toast.LENGTH_SHORT).show();


            }
            NotifyDataChange();
        }

        @Override
        public void onClick(Shopping shopping, ImageView imageView) {
            if (shopping.isChecked()) {
                database.shopDataAccessObject().check(shopping.getID(), false);

            } else {
                database.shopDataAccessObject().check(shopping.getID(), true);

            }
            NotifyDataChange();
        }

        @Override
        public void onLongClick(Shopping shopping, RelativeLayout cardView) {
            /*selectedItem = new Shopping();
            selectedItem = shopping;

            if (selectedItem.isLongClicked()) {
                database.shopDataAccessObject().longClick(selectedItem.getID(), false);
                Toast.makeText(ShoppingHolder.this, "Navy Blue Applied", Toast.LENGTH_SHORT).show();

            } else {
                database.shopDataAccessObject().longClick(selectedItem.getID(), true);
                Toast.makeText(ShoppingHolder.this, "Dark Purple Applied", Toast.LENGTH_SHORT).show();

            }
            NotifyDataChange();*/

        }

    };
    @SuppressLint("NotifyDataSetChanged")
    public void NotifyDataChange() {
        shopping.clear();
        shopping.addAll(database.shopDataAccessObject().getAll());
        shopListAdapter.notifyDataSetChanged();
    }

    private void showPopup(CardView cardView) {


    }

    private void showPopup(RelativeLayout relativeLayout) {
        PopupMenu popupMenu = new PopupMenu(this, relativeLayout);
        //popupMenu.setOnMenuItemClickListener(this);
        popupMenu.inflate(R.menu.popup_menu);
        popupMenu.show();


    }

    public void InsertTotal(TextView totalAmount,TextView totalItems) {
        String string_1 = String.format("%.2f", database.shopDataAccessObject().getAllTotal());
        String string_2 = String.valueOf(database.shopDataAccessObject().getAllTotalItems());
        string_1 = string_1.replace(",", ".");
        totalAmount.setText(string_1);
        totalItems.setText(string_2);

    }
    private int getwindowwidth() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        return metrics.widthPixels;

    }
    private void confDeleteDialog() {

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

                if(shopping.size() !=0){
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


    @Override
    public void onBackPressed() {

        super.onBackPressed();
        finish();
        overridePendingTransition(0,android.R.anim.slide_out_right);
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
            List<Shopping> archiveShopList = new ArrayList<>();


            switch (direction){
                case ItemTouchHelper.LEFT:

                    selectedItem = shopListAdapter.getShopList(position);
                    deletedTitle = shopping.get(position).getItem();
                    shopping.remove(position);
                    database.shopDataAccessObject().delete(selectedItem);
                    shopListAdapter.notifyDataSetChanged();
                    //InsertTotal();

                    Snackbar snackbar = Snackbar.make(recyclerView, "DELETING "
                            +deletedTitle, 5000);
                    snackbar.setTextColor(getResources().getColor(R.color.lite_red));
                    snackbar.setBackgroundTint(getResources().getColor(R.color.Default));
                    snackbar.setAction("Undo", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            shopping.add(position,selectedItem);
                            database.shopDataAccessObject().insert(selectedItem);
                            shopListAdapter.notifyDataSetChanged();
                            //InsertTotal();
                            snackbar.dismiss();
                        }
                    });
                    snackbar.show();
                    break;

                case ItemTouchHelper.RIGHT:
                    selectedItem = shopListAdapter.getShopList(position);
                    archiveShopList.add(selectedItem);
                    shopping.remove(position);
                    //InsertTotal();
                    shopListAdapter.notifyDataSetChanged();

                    snackbar = Snackbar.make(recyclerView,
                            selectedItem.getItem()+" TEMPORARY SHIFTED ", 5000);
                    snackbar.setTextColor(getResources().getColor(R.color.dominant_color));
                    snackbar.setBackgroundTint(getResources().getColor(R.color.Default));
                    snackbar.setAction("Undo", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            archiveShopList.remove(archiveShopList.lastIndexOf(selectedItem));
                            shopping.add(position,selectedItem);
                            //InsertTotal();
                            shopListAdapter.notifyDataSetChanged();

                            snackbar.dismiss();
                        }
                    });
                    snackbar.show();

                    break;

            }

        }

        @Override
        public void onChildDraw(@NonNull Canvas c , @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

            new RecyclerViewSwipeDecorator.Builder(ShoppingHolder.this,c,recyclerView,viewHolder,dX,dY,actionState,isCurrentlyActive)
                    .addSwipeLeftActionIcon(R.drawable.ic_baseline_delete_sweep_24)
                    .addSwipeLeftBackgroundColor(ContextCompat.getColor(ShoppingHolder.this,R.color.halo_red))

                    .addSwipeRightActionIcon(R.drawable.ic_baseline_archive_24)
                    .addSwipeRightBackgroundColor(ContextCompat.getColor(ShoppingHolder.this,R.color.dominant_color))

                    .create()
                    .decorate();
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int SPLASH_LENGTH = 300;
        switch (item.getItemId()) {
            case R.id.home:

                break;

            case R.id.onNotes:

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(ShoppingHolder.this, MainActivity.class));
                        overridePendingTransition(0, android.R.anim.slide_out_right);
                        finish();
                    }
                }, SPLASH_LENGTH);

                break;

            case R.id.onTaskMode:

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(ShoppingHolder.this, TaskMode.class));
                        overridePendingTransition(0, android.R.anim.slide_out_right);

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

            case R.id.onDefaultTheme:
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

            case R.id.onReceipt:

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(ShoppingHolder.this, SlipMode.class));
                        overridePendingTransition(0, android.R.anim.slide_out_right);

                    }
                }, SPLASH_LENGTH);


                break;


        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;

    }


}
//ca-app-pub-7881857902310901/3682450900
