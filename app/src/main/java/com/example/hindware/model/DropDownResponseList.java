package com.example.hindware.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DropDownResponseList {

        @JsonProperty("_Id")
        public String get_Id() {
            return this._Id; }
        public void set_Id(String _Id) {
            this._Id = _Id; }
        String _Id;
        @JsonProperty("_Value")
        public String get_Value() {
            return this._Value; }
        public void set_Value(String _Value) {
            this._Value = _Value; }
        String _Value;
}
