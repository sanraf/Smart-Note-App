package com.smartdevapp.smartnote;


import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.android.material.card.MaterialCardView;
import com.smartdevapp.smartnote.Models.Task;

public interface TaskListener {
    void onClick(Task task,RelativeLayout relativeLayout);
    void onClickEdit(Task task, ImageButton imageButton);
    void onClickCopy(Task task, ImageButton imageButton);
    void onClick(Task task, ImageView imageView);
    void onLongClick(Task task, RelativeLayout relativeLayout);

}
