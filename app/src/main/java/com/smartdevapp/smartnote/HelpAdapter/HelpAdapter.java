package com.smartdevapp.smartnote.HelpAdapter;

import android.os.Build;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.smartdevapp.smartnote.Models.HelpModel;
import com.smartdevapp.smartnote.HelpListener;
import com.smartdevapp.smartnote.R;

import java.util.List;

public class HelpAdapter extends RecyclerView.Adapter<HelpAdapter.HelpViewHolder> {

    private final List<HelpModel> helpModelList;

    HelpListener listener;

    public HelpAdapter(List<HelpModel> faqs, HelpListener listener) {
        this.helpModelList = faqs;
        this.listener = listener;
    }


    @NonNull
    @Override
    public HelpViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.help_list, viewGroup, false);
        return new HelpViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull HelpViewHolder viewHolder, int position) {

        HelpModel helpModel = helpModelList.get(position);
        viewHolder.question.setText(helpModel.getQuestion());
        viewHolder.answer.setText(getSpannedText(helpModel.getAnswer()));
        boolean isExpanded = helpModel.isExpanded();
        viewHolder.expandableView.setVisibility(isExpanded ? View.VISIBLE : View.GONE);

        viewHolder.question.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(helpModelList.get(viewHolder.getAdapterPosition()),viewHolder.question);
            }
        });
    }

    private Spanned getSpannedText(String text){
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.N){
            return Html.fromHtml(text,Html.FROM_HTML_MODE_COMPACT);
        }else {
            //noinspection deprecation
            return Html.fromHtml(text);
        }
    }

    @Override
    public int getItemCount() {
        return helpModelList.size();
    }


    static class HelpViewHolder extends RecyclerView.ViewHolder {


        TextView question;
        TextView answer;
        ConstraintLayout expandableView;

        public HelpViewHolder(View itemView) {
            super(itemView);

            question = itemView.findViewById(R.id.question);
            answer = itemView.findViewById(R.id.answer);
            expandableView = itemView.findViewById(R.id.expandable_view);



        }




    }


}
