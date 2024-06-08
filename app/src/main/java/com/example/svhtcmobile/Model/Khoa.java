package com.example.svhtcmobile.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Khoa implements Serializable {

    @SerializedName("MAKHOA")
    private String maKhoa;
    @SerializedName("TENKHOA")
    private String tenKhoa;

    @SerializedName("ID_NGANH")
    private int idNganh;

    public Khoa() {
    }

    public Khoa(String maKhoa, String tenKhoa, int idNganh) {
        this.maKhoa = maKhoa;
        this.tenKhoa = tenKhoa;
        this.idNganh = idNganh;
    }

    public int getIdNganh() {
        return idNganh;
    }

    public void setIdNganh(int idNganh) {
        this.idNganh = idNganh;
    }

    public String getMaKhoa() {
        return maKhoa;
    }

    public void setMaKhoa(String maKhoa) {
        this.maKhoa = maKhoa;
    }

    public String getTenKhoa() {
        return tenKhoa;
    }

    public void setTenKhoa(String tenKhoa) {
        this.tenKhoa = tenKhoa;
    }
}
