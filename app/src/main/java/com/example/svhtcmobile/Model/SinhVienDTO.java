package com.example.svhtcmobile.Model;

import com.google.gson.annotations.SerializedName;

public class SinhVienDTO {
    @SerializedName("masv")
    private String maSV;

    @SerializedName("ho")
    private String ho;

    @SerializedName("ten")
    private String ten;

    public SinhVienDTO() {
    }

    public SinhVienDTO(String maSV, String ho, String ten) {
        this.maSV = maSV;
        this.ho = ho;
        this.ten = ten;
    }

    public String getMaSV() {
        return maSV;
    }

    public void setMaSV(String maSV) {
        this.maSV = maSV;
    }

    public String getHo() {
        return ho;
    }

    public void setHo(String ho) {
        this.ho = ho;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }
}
