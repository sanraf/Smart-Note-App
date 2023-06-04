package com.smartdevapp.smartnote.TaskAdapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.smartdevapp.smartnote.Models.Task;
import com.smartdevapp.smartnote.R;
import com.smartdevapp.smartnote.TaskListener;

import java.util.List;


public class TaskListAdapter extends RecyclerView.Adapter<TaskViewHolder>{

    Context context;
    List<Task> tasks;
    TaskListener listener;

    public TaskListAdapter(Context context, List<Task> list, TaskListener listener) {
        this.context = context;
        this.tasks = list;
        this.listener = listener;
    }
    public TaskListAdapter(Context context) {
        this.context = context;
    }

    @Override
    public TaskViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
         return new TaskViewHolder(LayoutInflater.from(context).inflate(R.layout.task_list,parent,false));
    }
    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.M)

    @Override
    public void onBindViewHolder( TaskViewHolder holder, int position) {

        boolean isExpandable = tasks.get(position).isExpandable();
        holder.expandingLayout.setVisibility(isExpandable ?View.VISIBLE:View.GONE);


        //set margin for title//////////////////////////////////////////////////////
        RelativeLayout.LayoutParams Titleparams =new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        if (tasks.get(position).isExpandable()) {

           Titleparams.setMargins(10,20,10,5);
           holder.expandArrow.setForeground(expandArrowLess());


        }else {

            Titleparams.setMargins(10,20,10,20);
            holder.expandArrow.setForeground(expandArrowMore());


        }
        holder.titleLayout.setLayoutParams(Titleparams);
        if (tasks.get(position).getTask().equals("")) {
            holder.task.setText(getSpannedText(context.getResources().getString(R.string.no_task_added)));

        } else {
            holder.task.setText(tasks.get(position).getTask());
        }
        //holder.task.setText(tasks.get(position).getTask());
        holder.date.setText(tasks.get(position).getDate());
        holder.title.setText(tasks.get(position).getTitle());

        if(tasks.get(position).isTaskchecked()){
            holder.check.setImageResource(R.drawable.green_check);

        }
        else {
            holder.check.setImageResource(0);

        }

        if (tasks.get(position).isTasklongClicked()){
            holder.colorHolder.setBackground(ContextCompat.getDrawable(context,R.drawable.taskcardbck_2));
            //holder.task_holder.setStrokeColor(ContextCompat.getColor(context,R.color.red_mode_stroke));

        }else {
            holder.colorHolder.setBackground(ContextCompat.getDrawable(context,R.drawable.taskcardbck_1));
            //holder.task_holder.setStrokeColor(ContextCompat.getColor(context,R.color.m));
        }


        holder.editTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClickEdit(tasks.get(holder.getAdapterPosition()),holder.editTask);
            }
        });
        holder.copyTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClickCopy(tasks.get(holder.getAdapterPosition()),holder.copyTask);
            }
        });

        holder.checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(tasks.get(holder.getAdapterPosition()),holder.checkbox);
            }
        });


        holder.titleLayout.setOnClickListener(new View.OnClickListener() {
            public Activity activity;
            @Override
            public void onClick(View v) {

                listener.onClick(tasks.get(holder.getAdapterPosition()),holder.titleLayout);

            }
        });

        holder.titleLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                listener.onLongClick(tasks.get(holder.getAdapterPosition()),holder.titleLayout);
                return true;
            }
        });




    }

    private Drawable expandArrowMore(){
        return ContextCompat.getDrawable(context, R.drawable.ic_baseline_expand_more_24);
    }
    private Drawable expandArrowLess(){
        return ContextCompat.getDrawable(context, R.drawable.ic_baseline_expand_less_24);
    }


    @Override
    public int getItemCount() {
        return tasks.size();
    }
    @SuppressLint("NotifyDataSetChanged")
    public void filterShopList(List<Task> filterList){
        tasks = filterList;
        notifyDataSetChanged();
    }

    public Task getTask(int position){
        return tasks.get(position);
    }

    private Spanned getSpannedText(String text){
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.N){
            return Html.fromHtml(text,Html.FROM_HTML_MODE_COMPACT);
        }else {
            return Html.fromHtml(text);
        }
    }


}
class TaskViewHolder extends RecyclerView.ViewHolder {


    RelativeLayout task_holder;
    TextView task,date,title,task_Description;
    ImageView check,checkbox,expandArrow;
    ImageButton editTask,copyTask;
    RelativeLayout expandingLayout,titleLayout,colorHolder;

    public TaskViewHolder(View itemView) {

        super(itemView);
        task_holder = itemView.findViewById(R.id.taskHolder);
        title = itemView.findViewById(R.id.title);
        task = itemView.findViewById(R.id.task);
        task_Description = itemView.findViewById(R.id.title_task);
        date = itemView.findViewById(R.id.time);
        check = itemView.findViewById(R.id.task_check);
        expandArrow = itemView.findViewById(R.id.task_expand);
        editTask = itemView.findViewById(R.id.editTask);
        copyTask = itemView.findViewById(R.id.copyTask);
        checkbox = itemView.findViewById(R.id.task_checkbox);
        expandingLayout = itemView.findViewById(R.id.expanding_layout);
        titleLayout = itemView.findViewById(R.id.titleLayout);
        colorHolder = itemView.findViewById(R.id.colorHolder);

    }

}
