package com.example.hindware.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by SandeepY on 10122020
 **/

@JsonIgnoreProperties(ignoreUnknown = true)
public class SetLanguageRequestBean {

    @JsonProperty("_tk")
    private String _Tk;

    @JsonProperty("_ts")
    private String _Ts;

    @JsonProperty("_ln")
    private String _Ln;

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

    public String get_Ln() {
        return _Ln;
    }

    public void set_Ln(String _Ln) {
        this._Ln = _Ln;
    }
}
