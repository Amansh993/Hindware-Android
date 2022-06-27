package com.example.hindware.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by SandeepY on 15122020
 **/

@JsonIgnoreProperties(ignoreUnknown = true)
public class ValidateQRRequestBean {

    @JsonProperty("_tk")
    private String _Tk;

    @JsonProperty("_ts")
    private String _Ts;

    @JsonProperty("_qd")
    private String _Qd;

    public String get_Tk() {
        return _Tk;
    }

    public void set_Tk(String _Tk) {
        this._Tk = _Tk;
    }

    public String get_Ts() {
        return _Ts;
    }

    public void set_Ts(String _Ts) {
        this._Ts = _Ts;
    }

    public String get_Qd() {
        return _Qd;
    }

    public void set_Qd(String _Qd) {
        this._Qd = _Qd;
    }
}
