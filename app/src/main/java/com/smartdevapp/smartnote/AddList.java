package com.smartdevapp.smartnote;

import androidx.appcompat.app.AppCompatActivity;

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

import com.google.android.material.textfield.TextInputLayout;
import com.smartdevapp.smartnote.Models.Shopping;


public class AddList extends AppCompatActivity {
    EditText Add_item,Add_quantity,Add_price;
    Button save;
    TextInputLayout textInputLayout;



    Shopping shopping;
    boolean isOldItem = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_list);

        Add_item = findViewById(R.id.edit_item);
        Add_quantity = findViewById(R.id.numberOfItems);
        Add_price = findViewById(R.id.priceOfItem);
        save = findViewById(R.id.floatingButton);
        textInputLayout = findViewById(R.id.textinput_counter);

        shopping = new Shopping();

        try{

            shopping = (Shopping) getIntent().getSerializableExtra("old_item");
            Add_item.setText(shopping.getItem());
            Add_quantity.setText(String.valueOf(shopping.getQuantity()) );
            //converting a price in to 2 decimal place and change comma to a dot
            Add_price.setText(String.format(getString(R.string.price_converter),
                    shopping.getPrice()).replace(",","."));
            isOldItem = true;


        }catch (Exception e){
            e.printStackTrace();
        }

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String item = Add_item.getText().toString();
                String quantity =Add_quantity.getText().toString();
                String  price = Add_price.getText().toString();

                    //closing the keypad after finishing the activity
                try{
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);

                }catch (Exception e){

                    e.printStackTrace();
                }



                if(item.isEmpty() || price.isEmpty() ){
                    Toast.makeText(AddList.this, "Empty Spaces Not Allowed", Toast.LENGTH_SHORT).show();

                }
                else if(Add_quantity.length()>=10){
                    Toast.makeText(AddList.this, "Quantity Over-Bound", Toast.LENGTH_SHORT).show();
                }
                else {

                    if(!isOldItem){
                        shopping = new Shopping();
                    }



                    try{
                         if(quantity.isEmpty()){
                             Add_quantity.setText("1");
                             Add_quantity.setTextColor(getResources().getColor(R.color.tran));

                             shopping.setQuantity(Integer.parseInt("1"));
                             double total = Double.parseDouble(Add_price.getText().toString()) * Double.parseDouble("1");
                             shopping.setTotalPrice(total);


                         }else{
                             shopping.setQuantity(Integer.parseInt(quantity));
                             double total = Double.parseDouble(Add_price.getText().toString()) * Double.parseDouble(Add_quantity.getText().toString());
                             shopping.setTotalPrice(total);

                         }

                        shopping.setItem(item);
                        shopping.setPrice(Double.parseDouble(price));
                        double totalItems = Double.parseDouble(Add_quantity.getText().toString());
                        shopping.setTotalItems(totalItems);
                        Toast.makeText(AddList.this, "Created Successfully", Toast.LENGTH_SHORT).show();


                    }catch (Exception e){
                        Toast.makeText(AddList.this,  "Something went wrong! : contact the developer", Toast.LENGTH_LONG).show();
                        return;
                    }


                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent();
                            intent.putExtra("item",shopping);
                            setResult(Activity.RESULT_OK,intent);
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
        params.y =-20;
        getWindow().setAttributes(params);
    }

    private void transWindowDimension() {

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            getWindow().setLayout((int)(width*.9),(int)(height*.5));
        }else {
            getWindow().setLayout((int)(width*.9),(int)(height*.7));
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(0,android.R.anim.slide_out_right);
    }
}