package com.example.svhtcmobile.Model;

public class GiangVienKhoa {
    private String magv;
    private String ho;
    private String ten;
    private String makhoa;
    public GiangVienKhoa() {
    }

    @Override
    public String toString() {
        return ho + " " + ten; // Display full name in Spinner
    }

    public GiangVienKhoa(String magv, String ho, String ten, String makhoa) {
        this.magv = magv;
        this.ho = ho;
        this.ten = ten;
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

    public String getMakhoa() {
        return makhoa;
    }

    public void setMakhoa(String makhoa) {
        this.makhoa = makhoa;
    }
}
