package com.smartdevapp.smartnote.Models;

public class HelpModel {

    private String mQuestion;
    private String mAnswer;
    private boolean mIsExpanded;

    public HelpModel(String question, String answer) {
        this.mQuestion = question;
        this.mAnswer = answer;
        mIsExpanded = false;
    }

    public boolean isExpanded() {
        return mIsExpanded;
    }

    public void setExpanded(boolean expanded) {
        mIsExpanded = expanded;
    }

    public String getQuestion() {
        return mQuestion;
    }

    public String getAnswer() {
        return mAnswer;
    }


}
