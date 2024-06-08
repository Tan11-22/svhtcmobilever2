package com.example.svhtcmobile.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CTDTDTO implements Serializable {
    @SerializedName("MAMH")
    private String maMH;

    @SerializedName("TENMH")
    private String tenMH;

    @SerializedName("HOCKY")
    private int hocKy;

    public CTDTDTO() {
    }
    public CTDTDTO(String maMH, String tenMH, int hocKy) {
        this.maMH = maMH;
        this.tenMH = tenMH;
        this.hocKy = hocKy;
    }

    public String getMaMH() {
        return maMH;
    }

    public void setMaMH(String maMH) {
        this.maMH = maMH;
    }

    public String getTenMH() {
        return tenMH;
    }

    public void setTenMH(String tenMH) {
        this.tenMH = tenMH;
    }

    public int getHocKy() {
        return hocKy;
    }

    public void setHocKy(int hocKy) {
        this.hocKy = hocKy;
    }
}
