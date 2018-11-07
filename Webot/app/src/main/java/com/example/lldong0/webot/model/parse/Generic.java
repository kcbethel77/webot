package com.example.lldong0.webot.model.parse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Generic {

    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("options")
    @Expose
    private List<Option> options = null;
    @SerializedName("response_type")
    @Expose
    private String responseType;
    @SerializedName("title")
    @Expose
    private String title;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Option> getOptions() {
        return options;
    }

    public void setOptions(List<Option> options) {
        this.options = options;
    }

    public String getResponseType() {
        return responseType;
    }

    public void setResponseType(String responseType) {
        this.responseType = responseType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}