package com.example.lldong0.webot.model.parse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Input {

    @SerializedName("text")
    @Expose
    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

}