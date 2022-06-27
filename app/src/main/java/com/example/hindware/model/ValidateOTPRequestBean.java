package com.example.hindware.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by SandeepY on 10122020
 **/

@JsonIgnoreProperties(ignoreUnknown = true)
public class ValidateOTPRequestBean {

    @JsonProperty("_m")
    private String _M;

    @JsonProperty("_c")
    private String _C;

    @JsonProperty("_otp")
    private String _Otp;

    @JsonProperty("_mtk")
    private String _Mtk;

    @JsonProperty("_mtype")
    private String _Mtype;

    public String get_M() {
        return _M;
    }

    public void set_M(String _M) {
        this._M = _M;
    }

    public String get_C() {
        return _C;
    }

    public void set_C(String _C) {
        this._C = _C;
    }

    public String get_Otp() {
        return _Otp;
    }

    public void set_Otp(String _Otp) {
        this._Otp = _Otp;
    }

    public String get_Mtk() {
        return _Mtk;
    }

    public void set_Mtk(String _Mtk) {
        this._Mtk = _Mtk;
    }

    public String get_Mtype() {
        return _Mtype;
    }

    public void set_Mtype(String _Mtype) {
        this._Mtype = _Mtype;
    }
}
