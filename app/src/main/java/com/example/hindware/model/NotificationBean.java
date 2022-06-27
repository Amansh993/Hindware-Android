package com.example.hindware.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by SandeepY on 17032021
 **/

public class NotificationBean {

    private String notiIcon;

    private String notiName;

    public String getNotiIcon() {
        return notiIcon;
    }

    public void setNotiIcon(String notiIcon) {
        this.notiIcon = notiIcon;
    }

    public String getNotiName() {
        return notiName;
    }

    public void setNotiName(String notiName) {
        this.notiName = notiName;
    }
}
