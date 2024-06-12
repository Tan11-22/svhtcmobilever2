package com.example.svhtcmobile.Model;

public class LopDTO1 {
    private int stt;
    private String maLop;
    private String tenLop;
    private String khoaHoc;
    private String tenHe;
    private String tenKhoa;

    public LopDTO1() {}
    public LopDTO1(int stt, String maLop, String tenLop, String khoaHoc, String tenHe, String tenKhoa) {
        this.stt = stt;
        this.maLop = maLop;
        this.tenLop = tenLop;
        this.khoaHoc = khoaHoc;
        this.tenHe = tenHe;
        this.tenKhoa = tenKhoa;
    }

    public int getStt() {
        return stt;
    }

    public void setStt(int stt) {
        this.stt = stt;
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

    public String getTenHe() {
        return tenHe;
    }

    public void setTenHe(String tenHe) {
        this.tenHe = tenHe;
    }

    public String getTenKhoa() {
        return tenKhoa;
    }

    public void setTenKhoa(String tenKhoa) {
        this.tenKhoa = tenKhoa;
    }
}
