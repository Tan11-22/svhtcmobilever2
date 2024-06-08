package com.example.svhtcmobile.Model;

public class DDKLTC {
    int maLTC;
    String maMH;
    String tenMH;
    int nhom;
    int soTinChi;

    public DDKLTC() {
    }

    public DDKLTC(int maLTC, String maMH, String tenMH, int nhom, int soTinChi) {
        this.maLTC = maLTC;
        this.maMH = maMH;
        this.tenMH = tenMH;
        this.nhom = nhom;
        this.soTinChi = soTinChi;
    }

    public int getMaLTC() {
        return maLTC;
    }
    public void setMaLTC(int maLTC) {
        this.maLTC = maLTC;
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
    public int getNhom() {
        return nhom;
    }
    public void setNhom(int nhom) {
        this.nhom = nhom;
    }
    public int getSoTinChi() {
        return soTinChi;
    }
    public void setSoTinChi(int soTinChi) {
        this.soTinChi = soTinChi;
    }

}

