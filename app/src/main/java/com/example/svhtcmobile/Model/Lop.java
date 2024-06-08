package com.example.svhtcmobile.Model;

import com.google.gson.annotations.SerializedName;

public class Lop {
    @SerializedName("malop")
    private String maLop;

    @SerializedName("tenlop")
    private String tenLop;

    @SerializedName("khoahoc")
    private String khoaHoc;
    @SerializedName("maKhoa")
    private String makhoa;

    @SerializedName("idHe")
    private int idHe;
    @SerializedName("trangThai")
    private boolean trangThai;

    public Lop() {
    }

    public Lop(String maLop, String tenLop, String khoaHoc, String makhoa, int idHe) {
        this.maLop = maLop;
        this.tenLop = tenLop;
        this.khoaHoc = khoaHoc;
        this.makhoa = makhoa;
        this.idHe = idHe;
    }

    public String getMaLop() {
        return maLop;
    }

    public void setMaLop(String maLop) {
        this.maLop = maLop;
    }

    public String getTenLop() {
        return tenLop;
    }

    public void setTenLop(String tenLop) {
        this.tenLop = tenLop;
    }

    public String getKhoaHoc() {
        return khoaHoc;
    }

    public void setKhoaHoc(String khoaHoc) {
        this.khoaHoc = khoaHoc;
    }

    public String getMakhoa() {
        return makhoa;
    }

    public void setMakhoa(String makhoa) {
        this.makhoa = makhoa;
    }

    public int getIdHe() {
        return idHe;
    }

    public void setIdHe(int idHe) {
        this.idHe = idHe;
    }

    public boolean isTrangThai() {
        return trangThai;
    }

    public void setTrangThai(boolean trangThai) {
        this.trangThai = trangThai;
    }
}
