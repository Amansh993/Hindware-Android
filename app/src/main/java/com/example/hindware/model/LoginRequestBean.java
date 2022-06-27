package com.example.hindware.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by SandeepY on 08122020
 **/

@JsonIgnoreProperties(ignoreUnknown = true)
public class LoginRequestBean {

    @JsonProperty("_u")
    private String _u;

    @JsonProperty("_p")
    private String _p;

    @JsonProperty("_c")
    private String _c;

    @JsonProperty("_ts")
    private String _ts;

    @JsonProperty("_mtk")
    private String _mtk;

    @JsonProperty("_mtype")
    private String _mtype;

    public String get_u() {
        return _u;
    }

    public void set_u(String _u) {
        this._u = _u;
    }

    public String get_p() {
        return _p;
    }

    public void set_p(String _p) {
        this._p = _p;
    }

    public String get_c() {
        return _c;
    }

    public void set_c(String _c) {
        this._c = _c;
    }

    public String get_ts() {
        return _ts;
    }

    public void set_ts(String _ts) {
        this._ts = _ts;
    }

    public String get_mtk() {
        return _mtk;
    }

    public void set_mtk(String _mtk) {
        this._mtk = _mtk;
    }

    public String get_mtype() {
        return _mtype;
    }

    public void set_mtype(String _mtype) {
        this._mtype = _mtype;
    }
}
