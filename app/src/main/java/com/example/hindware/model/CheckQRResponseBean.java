package com.example.hindware.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * Created by SandeepY on 16122020
 **/

@JsonIgnoreProperties(ignoreUnknown = true)
public class CheckQRResponseBean extends BaseResponseBean implements Serializable {

    @JsonProperty("qrc_code")
    private String Qrc_code;

    @JsonProperty("qrc_prdname")
    private String Qrc_prdname;

    @JsonProperty("qrc_description")
    private String Qrc_description;

    @JsonProperty("qrc_points")
    private String Qrc_points;

    @JsonProperty("qrc_balance")
    private String Qrc_balance;

    @JsonProperty("qrc_sku")
    private String Qrc_sku;

    @JsonProperty("qrc_skucode")
    private String Qrc_skucode;

    public String getQrc_code() {
        return Qrc_code;
    }

    public void setQrc_code(String qrc_code) {
        Qrc_code = qrc_code;
    }

    public String getQrc_prdname() {
        return Qrc_prdname;
    }

    public void setQrc_prdname(String qrc_prdname) {
        Qrc_prdname = qrc_prdname;
    }

    public String getQrc_description() {
        return Qrc_description;
    }

    public void setQrc_description(String qrc_description) {
        Qrc_description = qrc_description;
    }

    public String getQrc_points() {
        return Qrc_points;
    }

    public void setQrc_points(String qrc_points) {
        Qrc_points = qrc_points;
    }

    public String getQrc_balance() {
        return Qrc_balance;
    }

    public void setQrc_balance(String qrc_balance) {
        Qrc_balance = qrc_balance;
    }

    public String getQrc_sku() {
        return Qrc_sku;
    }

    public void setQrc_sku(String qrc_sku) {
        Qrc_sku = qrc_sku;
    }

    public String getQrc_skucode() {
        return Qrc_skucode;
    }

    public void setQrc_skucode(String qrc_skucode) {
        Qrc_skucode = qrc_skucode;
    }
}
