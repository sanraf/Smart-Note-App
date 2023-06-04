package com.smartdevapp.smartnote.otheractivities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.TextView;

import com.smartdevapp.smartnote.Models.Shopping;
import com.smartdevapp.smartnote.R;
import com.smartdevapp.smartnote.ShopAdapter.ShopListAdapter;
import com.smartdevapp.smartnote.ShopDatabase.ShopRoomDB;
import com.smartdevapp.smartnote.SlipAdapter.SlipListAdapter;

import java.util.ArrayList;
import java.util.List;

public class SlipMode extends AppCompatActivity {
    RecyclerView recyclerView;
    SlipListAdapter slipListAdapter;
    List<Shopping> shopping = new ArrayList<>();
    ShopRoomDB database;

    TextView total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slip_mode);

        total = findViewById(R.id.slip_total);
        recyclerView = findViewById(R.id.slip_recycler);


        database = ShopRoomDB.getInstance(this);
        shopping = database.shopDataAccessObject().getAll();
        updateRecycler(shopping);
        InsertTotal();
    }

    private void updateRecycler(List<Shopping> shopping) {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, LinearLayoutManager.VERTICAL));
        slipListAdapter = new SlipListAdapter(SlipMode.this, shopping);
        recyclerView.setAdapter(slipListAdapter);
    }
    @SuppressLint("DefaultLocale")
    public void InsertTotal() {
        String string = String.format("%.2f", database.shopDataAccessObject().getAllTotal());
        string = string.replace(",", ".");
        total.setText(string);

    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
        finish();
        overridePendingTransition(0,android.R.anim.slide_out_right);
    }
}