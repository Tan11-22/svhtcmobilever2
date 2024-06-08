package com.example.svhtcmobile.Model;

public class HocPhiHocKy {
    private String maMH;

    private String tenMH;
    private int soTC;
    private int hocLai;
    private int tien;

    public HocPhiHocKy(){

    }

    public HocPhiHocKy(String maMH, String tenMH, int soTC, int hocLai, int tien) {
        this.maMH = maMH;
        this.tenMH = tenMH;
        this.soTC = soTC;
        this.hocLai = hocLai;
        this.tien = tien;
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

    public int getSoTC() {
        return soTC;
    }

    public void setSoTC(int soTC) {
        this.soTC = soTC;
    }

    public int getHocLai() {
        return hocLai;
    }

    public void setHocLai(int hocLai) {
        this.hocLai = hocLai;
    }

    public int getTien() {
        return tien;
    }

    public void setTien(int tien) {
        this.tien = tien;
    }

}

