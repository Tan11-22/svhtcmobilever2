package com.example.svhtcmobile.Model;

public class HocPhiSinhVien {
    private String nienKhoa;
    private int hocKy;
    private int hocPhi;
    private int daDong;
    private int no;

    public HocPhiSinhVien(String nienKhoa, int hocKy, int hocPhi, int daDong, int no) {
        this.nienKhoa = nienKhoa;
        this.hocKy = hocKy;
        this.hocPhi = hocPhi;
        this.daDong = daDong;
        this.no = no;
    }

    public HocPhiSinhVien(){

    }
    public String getNienKhoa() {
        return nienKhoa;
    }

    public void setNienKhoa(String nienKhoa) {
        this.nienKhoa = nienKhoa;
    }
    public int getHocKy() {
        return hocKy;
    }

    public void setHocKy(int hocKy) {
        this.hocKy = hocKy;
    }
    public int getHocPhi() {
        return hocPhi;
    }

    public void setHocPhi(int hocPhi) {
        this.hocPhi = hocPhi;
    }
    public int getDaDong() {
        return daDong;
    }

    public void setDaDong(int daDong) {
        this.daDong = daDong;
    }
    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }
}
