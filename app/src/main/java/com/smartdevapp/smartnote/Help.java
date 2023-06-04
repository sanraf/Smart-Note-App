package com.smartdevapp.smartnote;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.TextView;

import com.smartdevapp.smartnote.HelpAdapter.HelpAdapter;
import com.smartdevapp.smartnote.Models.HelpModel;

import java.util.ArrayList;
import java.util.List;

public class Help extends AppCompatActivity  {

    private HelpAdapter mHelpAdapter;
    private List<HelpModel> modelList;


    RecyclerView myRecyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        myRecyclerView = findViewById(R.id.myRecycler);

        initFAQs();

        initHelpRecyclerView();
    }

    private void initFAQs() {
        modelList = new ArrayList<>();
        String[] questionAnswers = getResources().getStringArray(R.array.help_question_answers);
        HelpModel helpModel;
        for (String questionAnswer : questionAnswers) {
            String[] questionAnswerSplit = questionAnswer.split("#####");
            helpModel = new HelpModel(questionAnswerSplit[0], questionAnswerSplit[1]);
            modelList.add(helpModel);
        }
    }

    private void initHelpRecyclerView() {
        mHelpAdapter = new HelpAdapter(modelList, helpListener);
        myRecyclerView.setAdapter(mHelpAdapter);
    }

 private final HelpListener helpListener = new HelpListener() {
     @SuppressLint("NotifyDataSetChanged")
     @Override
     public void onClick(HelpModel helpModel, TextView textView) {
         helpModel.setExpanded(!helpModel.isExpanded());
         mHelpAdapter.notifyDataSetChanged();

     }

 };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(0,android.R.anim.slide_out_right);
    }


}