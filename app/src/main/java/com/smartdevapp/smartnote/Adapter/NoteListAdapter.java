package com.smartdevapp.smartnote.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.smartdevapp.smartnote.Models.Notes;
import com.smartdevapp.smartnote.NotesListener;
import com.smartdevapp.smartnote.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NoteListAdapter extends RecyclerView.Adapter<NoteViewHolder>{
    Context context;
    List<Notes> list;
    NotesListener listener;


    public NoteListAdapter(Context context, List<Notes> list, NotesListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }



    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NoteViewHolder(LayoutInflater.from(context).inflate(R.layout.note_list,parent,false));
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {

        holder.title.setText(list.get(position).getTitle());
        holder.title.setSelected(true);

        holder.note.setText(list.get(position).getNotes());

        holder.date.setText(list.get(position).getDate());
        holder.date.setSelected(true);



        if(list.get(position).isPinned()){
            holder.image_pin.setImageResource(R.drawable.pin);
        }
        else {
        holder.image_pin.setImageResource(0);
    }

        if (list.get(position).isAlarmed()){
            holder.alarmIcon.setImageResource(R.drawable.ic_baseline_alarm_on_24);
            holder.note.setTypeface(Typeface.create("cursive",Typeface.NORMAL));
        }else {
            holder.alarmIcon.setImageResource(0);
        }

        if (list.get(position).isLocked()){
            holder.lockIcon.setBackgroundResource(R.drawable.securenotess);
            holder.note.setVisibility(View.INVISIBLE);
            holder.date.setVisibility(View.INVISIBLE);
            holder.alarmIcon.setVisibility(View.INVISIBLE);
            holder.image_pin.setVisibility(View.INVISIBLE);
            holder.menuDots.setVisibility(View.GONE);
            holder.colorMenu.setVisibility(View.GONE);

        }else {
            holder.lockIcon.setBackgroundResource(0);
            holder.note.setVisibility(View.VISIBLE);
            holder.date.setVisibility(View.VISIBLE);
            holder.alarmIcon.setVisibility(View.VISIBLE);
            holder.image_pin.setVisibility(View.VISIBLE);
            holder.menuDots.setVisibility(View.VISIBLE);
            holder.colorMenu.setVisibility(View.VISIBLE);

        }

        if (list.get(position).isLongClicked()){
            holder.note_holder.setCardBackgroundColor(holder.itemView.getResources().getColor(RandomColor(),null));
            holder.note_holder.setStrokeColor(ContextCompat.getColor(context,R.color.litepink_stroke));
        }else {
            holder.note_holder.setCardBackgroundColor(holder.itemView.getResources().getColor(R.color.cardview));
            holder.note_holder.setStrokeColor(ContextCompat.getColor(context,R.color.dark_mode));
        }


        if(list.get(position).isRed()){
        holder.note_holder.setCardBackgroundColor(holder.itemView.getResources().getColor(R.color.litered));
            holder.note_holder.setStrokeColor(ContextCompat.getColor(context,R.color.litered_stroke));
    }
        else if(list.get(position).isPink()){
            holder.note_holder.setCardBackgroundColor(holder.itemView.getResources().getColor(R.color.litepink));
            holder.note_holder.setStrokeColor(ContextCompat.getColor(context,R.color.litepink_stroke));
        }
        else if(list.get(position).isYellow()){
            holder.note_holder.setCardBackgroundColor(holder.itemView.getResources().getColor(R.color.liteyellow));
            holder.note_holder.setStrokeColor(ContextCompat.getColor(context,R.color.liteyellow_stroke));
        }
        else if(list.get(position).isCreamy()){
            holder.note_holder.setCardBackgroundColor(holder.itemView.getResources().getColor(R.color.cream));
            holder.note_holder.setStrokeColor(ContextCompat.getColor(context,R.color.cream_stroke));
        }
        else if(list.get(position).isGray()){
            holder.note_holder.setCardBackgroundColor(holder.itemView.getResources().getColor(R.color.R));
            holder.note_holder.setStrokeColor(ContextCompat.getColor(context,R.color.R_stroke));
        }
        else if(list.get(position).isGreen()){
            holder.note_holder.setCardBackgroundColor(holder.itemView.getResources().getColor(R.color.litegreen));
            holder.note_holder.setStrokeColor(ContextCompat.getColor(context,R.color.litegreen_stroke));
        }
        else if (list.get(position).isLongClicked()){
            holder.note_holder.setCardBackgroundColor(holder.itemView.getResources().getColor(RandomColor(),null));
            holder.note_holder.setStrokeColor(ContextCompat.getColor(context,R.color.tran));
        }
        else {
            holder.note_holder.setCardBackgroundColor(holder.itemView.getResources().getColor(R.color.cardview));
            holder.note_holder.setStrokeColor(ContextCompat.getColor(context,R.color.dark_mode));
        }

        if (list.get(position).isBold()){
            holder.note.setTypeface(Typeface.DEFAULT_BOLD);
            holder.title.setTypeface(Typeface.DEFAULT_BOLD);
            holder.note.setTextSize(16);
            holder.title.setTextSize(16);
        }else if(list.get(position).isItalic()){
            holder.note.setTypeface(Typeface.defaultFromStyle(Typeface.ITALIC));
            holder.title.setTypeface(Typeface.defaultFromStyle(Typeface.ITALIC));
            holder.note.setTextSize(16);
            holder.title.setTextSize(16);
        }else if (list.get(position).isCursive()){
            holder.note.setTypeface(Typeface.create("cursive",Typeface.BOLD));
            holder.title.setTypeface(Typeface.create("cursive",Typeface.BOLD));
            holder.note.setTextSize(18);
            holder.title.setTextSize(18);
        }else if (list.get(position).isCasual()){
            holder.note.setTypeface(Typeface.create("casual",Typeface.NORMAL));
            holder.title.setTypeface(Typeface.create("casual",Typeface.NORMAL));
            holder.note.setTextSize(16);
            holder.title.setTextSize(16);

        }else if(list.get(position).isMonospace()){
            holder.note.setTypeface(Typeface.create("monospace",Typeface.NORMAL));
            holder.title.setTypeface(Typeface.create("monospace",Typeface.NORMAL));
            holder.note.setTextSize(16);
            holder.title.setTextSize(16);
        }else {
            holder.note.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
            holder.title.setTypeface(Typeface.DEFAULT_BOLD);
            holder.note.setTextSize(16);
            holder.title.setTextSize(16);

        }

        holder.note_holder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(list.get(holder.getAdapterPosition()));
            }
        });

        holder.menuDots.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(list.get(holder.getAdapterPosition()),holder.menuDots);
            }
        });

        holder.colorMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(list.get(holder.getAdapterPosition()),holder.colorMenu);
            }
        });

        holder.note_holder.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                listener.randColor(list.get(holder.getAdapterPosition()),holder.note_holder);
                return true;
            }
        });




    }



    public int RandomColor(){
        List<Integer> colorCode = new ArrayList<>();

        colorCode.add(R.color.white);
        colorCode.add(R.color.purple_200);
        colorCode.add(R.color.purple_700);
        colorCode.add(R.color.teal_200);
        colorCode.add(R.color.khaki);
        colorCode.add(R.color.brown);

        Random random = new Random();
        int random_color =random.nextInt(colorCode.size());

        return colorCode.get(random_color);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    @SuppressLint("NotifyDataSetChanged")
    public void filterListList(List<Notes> filterList){
        list = filterList;
        notifyDataSetChanged();
    }

    public Notes getNotes(int position){
        return list.get(position);
    }



}
class NoteViewHolder extends RecyclerView.ViewHolder{

    MaterialCardView note_holder;
    ImageButton menuDots;
    TextView title;
    TextView note;
    TextView date;
    ImageView image_pin,alarmIcon,colorMenu,lockIcon;
    public NoteViewHolder(@NonNull View itemView) {
        super(itemView);
        note_holder =itemView.findViewById(R.id.note_holder);
        lockIcon = itemView.findViewById(R.id.lockNote);
        title =itemView.findViewById(R.id.title);
        note =itemView.findViewById(R.id.text_notes);
        date =itemView.findViewById(R.id.text_date);
        image_pin =itemView.findViewById(R.id.pin);
        menuDots =itemView.findViewById(R.id.threeDots);
        colorMenu =itemView.findViewById(R.id.paint);
        alarmIcon = itemView.findViewById(R.id.alertTitle);

    }
}
