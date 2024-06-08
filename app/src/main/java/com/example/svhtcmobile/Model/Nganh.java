package com.example.svhtcmobile.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Nganh implements Serializable {
    @SerializedName("ID_NGANH")
    private int idNganh;
    @SerializedName("TEN")
    private String tenNganh;

    public Nganh() {

    }
    public Nganh(int idNganh, String tenNganh) {
        this.idNganh = idNganh;
        this.tenNganh = tenNganh;
    }

    public int getIdNganh() {
        return idNganh;
    }

    public void setIdNganh(int idNganh) {
        this.idNganh = idNganh;
    }

    public String getTenNganh() {
        return tenNganh;
    }

    public void setTenNganh(String tenNganh) {
        this.tenNganh = tenNganh;
    }
}
