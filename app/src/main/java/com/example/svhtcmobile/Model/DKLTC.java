package com.example.svhtcmobile.Model;
public class DKLTC {

    int soTinChi;
    String maMH;
    String tenMH;
    int nhom;
    int soSVToiThieu;
    int maLTC;
    String maLop;

    public DKLTC(){}
    public DKLTC(int maLTC, String maMH, String tenMH, int nhom, int soSVToiThieu, int soTinChi, String maLop) {
        this.maLTC=maLTC;
        this.maMH = maMH;
        this.tenMH=tenMH;
        this.nhom = nhom;
        this.soSVToiThieu = soSVToiThieu;
        this.soTinChi = soTinChi;
        this.maLop=maLop;
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

    public int getSoSVToiThieu() {
        return soSVToiThieu;
    }
    public void setSoSVToiThieu(int soSVToiThieu) {
        this.soSVToiThieu = soSVToiThieu;
    }

    public int getSoTinChi() {
        return soTinChi;
    }
    public void setSoTinChi(int soTinChi) {
        this.soTinChi = soTinChi;
    }
    public int getMaLTC() {
        return maLTC;
    }
    public void setMaLTC(int maLTC) {
        this.maLTC = maLTC;
    }
    public String getMaLop() {
        return maLop;
    }
    public void setMaLop(String maLop) {
        this.maLop = maLop;
    }

}
