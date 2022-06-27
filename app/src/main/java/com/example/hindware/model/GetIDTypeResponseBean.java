package com.example.hindware.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GetIDTypeResponseBean {

    @JsonProperty("DropdownList")
    private ArrayList<DropDownResponseList> dropDownResponseList;

    @JsonProperty("ResponseCode")
    private String responseCode;

    @JsonProperty("Message")
    private String message;

    public ArrayList<DropDownResponseList> getDropDownResponseList() {
        return dropDownResponseList;
    }

    public void setDropDownResponseList(ArrayList<DropDownResponseList> dropDownResponseList) {
        this.dropDownResponseList = dropDownResponseList;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}



