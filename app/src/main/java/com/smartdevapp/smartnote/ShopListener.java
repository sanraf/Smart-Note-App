package com.smartdevapp.smartnote;


import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.cardview.widget.CardView;


import com.google.android.material.card.MaterialCardView;
import com.smartdevapp.smartnote.Models.Shopping;

public interface ShopListener {
    void onClick(Shopping shopping);
    void onClick(Shopping shopping, ImageButton imageButton);
    void onClick(Shopping shopping, ImageView imageView);
    void onLongClick(Shopping shopping, RelativeLayout cardView);

}
