package com.example.hindware.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by SandeepY on 09122020
 **/

@JsonIgnoreProperties(ignoreUnknown = true)
public class GetLanguageRequestBean {

    @JsonProperty("_ts")
    private String _Ts;

    @JsonProperty("_c")
    private String _c;

    public String get_Ts() {
        return _Ts;
    }

    public void set_Ts(String _Ts) {
        this._Ts = _Ts;
    }

    public String get_c() {
        return _c;
    }

    public void set_c(String _c) {
        this._c = _c;
    }
}
