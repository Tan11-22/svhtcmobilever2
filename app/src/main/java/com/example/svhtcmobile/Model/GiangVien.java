package com.example.svhtcmobile.Model;

public class GiangVien {
    private String magv;
    private String ho;
    private String ten;
    private  String hocham;
    private String hocvi;
    private String chuyenmon;
    private String sdt;
    private String hinhanh;
    private String email;
    private String makhoa;

    public GiangVien(String magv, String ho, String ten, String hocham, String hocvi, String chuyenmon, String sdt, String hinhanh, String email, String makhoa) {
        this.magv = magv;
        this.ho = ho;
        this.ten = ten;
        this.hocham = hocham;
        this.hocvi = hocvi;
        this.chuyenmon = chuyenmon;
        this.sdt = sdt;
        this.hinhanh = hinhanh;
        this.email = email;
        this.makhoa = makhoa;
    }

    public String getMagv() {
        return magv;
    }

    public void setMagv(String magv) {
        this.magv = magv;
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

    public String getHocham() {
        return hocham;
    }

    public void setHocham(String hocham) {
        this.hocham = hocham;
    }

    public String getHocvi() {
        return hocvi;
    }

    public void setHocvi(String hocvi) {
        this.hocvi = hocvi;
    }

    public String getChuyenmon() {
        return chuyenmon;
    }

    public void setChuyenmon(String chuyenmon) {
        this.chuyenmon = chuyenmon;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public String getHinhanh() {
        return hinhanh;
    }

    public void setHinhanh(String hinhanh) {
        this.hinhanh = hinhanh;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMakhoa() {
        return makhoa;
    }

    public void setMakhoa(String makhoa) {
        this.makhoa = makhoa;
    }
}
