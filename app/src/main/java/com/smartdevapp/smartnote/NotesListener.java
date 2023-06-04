package com.smartdevapp.smartnote;

import android.widget.ImageButton;
import android.widget.ImageView;

import com.google.android.material.card.MaterialCardView;
import com.smartdevapp.smartnote.Models.Notes;

public interface NotesListener {
    void onClick(Notes notes);
    void onClick(Notes notes, ImageButton imageButton);
    void onClick(Notes notes, ImageView imageView);
    void randColor(Notes notes,MaterialCardView cardView);

}

