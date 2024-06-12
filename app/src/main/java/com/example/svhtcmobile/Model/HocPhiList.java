package com.example.svhtcmobile.Model;

public class HocPhiList {
    private String masv ;
    private String hoten;
    private String nienkhoa;
    private int hocki ;
    private int sotien;
    private String dadong;
    public HocPhiList(){

    }

    public HocPhiList(String masv, String hoten, String nienkhoa, int hocki, int sotien, String dadong) {
        this.masv = masv;
        this.hoten = hoten;
        this.nienkhoa = nienkhoa;
        this.hocki = hocki;
        this.sotien = sotien;
        this.dadong = dadong;
    }

    public String getMasv() {
        return masv;
    }

    public void setMasv(String masv) {
        this.masv = masv;
    }

    public String getHoten() {
        return hoten;
    }

    public void setHoten(String hoten) {
        this.hoten = hoten;
    }

    public String getNienkhoa() {
        return nienkhoa;
    }

    public void setNienkhoa(String nienkhoa) {
        this.nienkhoa = nienkhoa;
    }

    public int getHocki() {
        return hocki;
    }

    public void setHocki(int hocki) {
        this.hocki = hocki;
    }

    public int getSotien() {
        return sotien;
    }

    public void setSotien(int sotien) {
        this.sotien = sotien;
    }

    public String getDadong() {
        return dadong;
    }

    public void setDadong(String dadong) {
        this.dadong = dadong;
    }
}
