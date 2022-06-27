package com.example.hindware.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by SandeepY on 08122020
 **/

@JsonIgnoreProperties(ignoreUnknown = true)
public class CheckAppVersionRequestBean {

    @JsonProperty("_ts")
    private String _Ts;

    @JsonProperty("_ver")
    private String _Ver;

    @JsonProperty("_mtype")
    private String _Mtype;

    @JsonProperty("_c")
    private String _c;

    public String get_Ts() {
        return _Ts;
    }

    public void set_Ts(String _Ts) {
        this._Ts = _Ts;
    }

    public String get_Ver() {
        return _Ver;
    }

    public void set_Ver(String _Ver) {
        this._Ver = _Ver;
    }

    public String get_Mtype() {
        return _Mtype;
    }

    public void set_Mtype(String _Mtype) {
        this._Mtype = _Mtype;
    }

    public String get_c() {
        return _c;
    }

    public void set_c(String _c) {
        this._c = _c;
    }
}
