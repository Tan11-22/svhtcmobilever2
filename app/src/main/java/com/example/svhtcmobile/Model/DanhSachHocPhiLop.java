package com.example.svhtcmobile.Model;

import java.io.Serializable;

public class DanhSachHocPhiLop implements Serializable {
    private String masv;
    private String nienkhoa;
    private int hocky;
    private String ho;
    private String ten;
    private int hocphi;
    private String ngaydong;
    private int sotiendong;

    public DanhSachHocPhiLop(String masv, String nienkhoa, int hocky, String ho, String ten, int hocphi, String ngaydong, int sotiendong) {
        this.masv = masv;
        this.nienkhoa = nienkhoa;
        this.hocky = hocky;
        this.ho = ho;
        this.ten = ten;
        this.hocphi = hocphi;
        this.ngaydong = ngaydong;
        this.sotiendong = sotiendong;
    }

    public DanhSachHocPhiLop() {
    }

    public String getMasv() {
        return masv;
    }

    public void setMasv(String masv) {
        this.masv = masv;
    }

    public String getNienkhoa() {
        return nienkhoa;
    }

    public void setNienkhoa(String nienkhoa) {
        this.nienkhoa = nienkhoa;
    }

    public int getHocky() {
        return hocky;
    }

    public void setHocky(int hocky) {
        this.hocky = hocky;
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

    public int getHocphi() {
        return hocphi;
    }

    public void setHocphi(int hocphi) {
        this.hocphi = hocphi;
    }

    public String getNgaydong() {
        return ngaydong;
    }

    public void setNgaydong(String ngaydong) {
        this.ngaydong = ngaydong;
    }

    public int getSotiendong() {
        return sotiendong;
    }

    public void setSotiendong(int sotiendong) {
        this.sotiendong = sotiendong;
    }
}
