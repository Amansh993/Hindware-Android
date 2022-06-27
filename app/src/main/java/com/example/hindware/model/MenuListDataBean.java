package com.example.hindware.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by SandeepY on 29092020
 **/

@JsonIgnoreProperties(ignoreUnknown = true)
public class MenuListDataBean {

    @JsonProperty("MDL_ID")
    private String mDL_ID;

    @JsonProperty("MDL_PARENTNAME")
    private String mDL_PARENTNAME;

    @JsonProperty("MDL_PARENTTITLE")
    private String mDL_PARENTTITLE;

    @JsonProperty("MDL_NAME")
    private String mDL_NAME;

    @JsonProperty("MDL_TITLE")
    private String mDL_TITLE;

    @JsonProperty("MDL_SUBTITLE")
    private String mDL_SUBTITLE;

    @JsonProperty("MDL_ACTION")
    private String mDL_ACTION;

    @JsonProperty("MDL_CONTROLLER")
    private String mDL_CONTROLLER;

    @JsonProperty("MDL_AREA")
    private String mDL_AREA;

    @JsonProperty("MDL_SUBTITLE1")
    private String mDL_SUBTITLE1;

    @JsonProperty("MDL_SUBTITLE2")
    private String mDL_SUBTITLE2;

    @JsonProperty("MDL_PARENT_SRNO")
    private String mDL_PARENT_SRNO;

    @JsonProperty("MDL_IMAGEURL")
    private String mDL_IMAGEURL;

    public String getmDL_ID() {
        return mDL_ID;
    }

    public void setmDL_ID(String mDL_ID) {
        this.mDL_ID = mDL_ID;
    }

    public String getmDL_PARENTNAME() {
        return mDL_PARENTNAME;
    }

    public void setmDL_PARENTNAME(String mDL_PARENTNAME) {
        this.mDL_PARENTNAME = mDL_PARENTNAME;
    }

    public String getmDL_PARENTTITLE() {
        return mDL_PARENTTITLE;
    }

    public void setmDL_PARENTTITLE(String mDL_PARENTTITLE) {
        this.mDL_PARENTTITLE = mDL_PARENTTITLE;
    }

    public String getmDL_NAME() {
        return mDL_NAME;
    }

    public void setmDL_NAME(String mDL_NAME) {
        this.mDL_NAME = mDL_NAME;
    }

    public String getmDL_TITLE() {
        return mDL_TITLE;
    }

    public void setmDL_TITLE(String mDL_TITLE) {
        this.mDL_TITLE = mDL_TITLE;
    }

    public String getmDL_SUBTITLE() {
        return mDL_SUBTITLE;
    }

    public void setmDL_SUBTITLE(String mDL_SUBTITLE) {
        this.mDL_SUBTITLE = mDL_SUBTITLE;
    }

    public String getmDL_ACTION() {
        return mDL_ACTION;
    }

    public void setmDL_ACTION(String mDL_ACTION) {
        this.mDL_ACTION = mDL_ACTION;
    }

    public String getmDL_CONTROLLER() {
        return mDL_CONTROLLER;
    }

    public void setmDL_CONTROLLER(String mDL_CONTROLLER) {
        this.mDL_CONTROLLER = mDL_CONTROLLER;
    }

    public String getmDL_AREA() {
        return mDL_AREA;
    }

    public void setmDL_AREA(String mDL_AREA) {
        this.mDL_AREA = mDL_AREA;
    }

    public String getmDL_SUBTITLE1() {
        return mDL_SUBTITLE1;
    }

    public void setmDL_SUBTITLE1(String mDL_SUBTITLE1) {
        this.mDL_SUBTITLE1 = mDL_SUBTITLE1;
    }

    public String getmDL_SUBTITLE2() {
        return mDL_SUBTITLE2;
    }

    public void setmDL_SUBTITLE2(String mDL_SUBTITLE2) {
        this.mDL_SUBTITLE2 = mDL_SUBTITLE2;
    }

    public String getmDL_PARENT_SRNO() {
        return mDL_PARENT_SRNO;
    }

    public void setmDL_PARENT_SRNO(String mDL_PARENT_SRNO) {
        this.mDL_PARENT_SRNO = mDL_PARENT_SRNO;
    }

    public String getmDL_IMAGEURL() {
        return mDL_IMAGEURL;
    }

    public void setmDL_IMAGEURL(String mDL_IMAGEURL) {
        this.mDL_IMAGEURL = mDL_IMAGEURL;
    }
}
