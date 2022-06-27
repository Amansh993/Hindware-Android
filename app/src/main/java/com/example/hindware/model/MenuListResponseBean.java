package com.example.hindware.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

/**
 * Created by SandeepY on 29092020
 **/

@JsonIgnoreProperties(ignoreUnknown = true)
public class MenuListResponseBean extends BaseResponseBean {

    @JsonProperty("MenuResponseList")
    private ArrayList<MenuListDataBean> menuResponseList;

    @JsonProperty("ClientName")
    private String clientName;

    @JsonProperty("ImageName")
    private String imageName;

    @JsonProperty("LogoUrl")
    private String logoUrl;

    @JsonProperty("firstname")
    private String firstName;

    @JsonProperty("lastname")
    private String lastName;

    @JsonProperty("imageURL")
    private String ImageURL;

    @JsonProperty("pointBalance")
    private String PointBalance;

    public ArrayList<MenuListDataBean> getMenuResponseList() {
        return menuResponseList;
    }

    public void setMenuResponseList(ArrayList<MenuListDataBean> menuResponseList) {
        this.menuResponseList = menuResponseList;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getImageURL() {
        return ImageURL;
    }

    public void setImageURL(String imageURL) {
        ImageURL = imageURL;
    }

    public String getPointBalance() {
        return PointBalance;
    }

    public void setPointBalance(String pointBalance) {
        PointBalance = pointBalance;
    }
}
