package com.example.svhtcmobile.Model;

import java.time.LocalDate;

public class HocPhiKeToan {

    private String maLop;
    private String maSV;
    private String hoTen;
    private String nienKhoa;
    private int hocKy;
    private int hocPhi;
    private LocalDate ngayDong;
    private int soTienDong;

    public HocPhiKeToan(){

    }

    public HocPhiKeToan(String maLop, String maSV, String hoTen, String nienKhoa, int hocKy, int hocPhi,
                        LocalDate ngayDong, int soTienDong) {
        this.maLop = maLop;
        this.maSV = maSV;
        this.hoTen = hoTen;
        this.nienKhoa = nienKhoa;
        this.hocKy = hocKy;
        this.hocPhi = hocPhi;
        this.ngayDong = ngayDong;
        this.soTienDong = soTienDong;
    }
    public String getMaLop() {
        return maLop;
    }

    public void setMaLop(String maLop) {
        this.maLop = maLop;
    }
    public String getMaSV() {
        return maSV;
    }

    public void setMaSV(String maSV) {
        this.maSV = maSV;
    }
    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
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
    public LocalDate getNgayDong() {
        return ngayDong;
    }
    public void setNgayDong(LocalDate ngayDong){
        this.ngayDong=ngayDong;
    }
//    public void setNgayDong(Object ngayDong) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            if(ngayDong.getClass() == LocalDate.class){
//                this.ngayDong=(LocalDate) ngayDong;
//            }
//            else if (ngayDong.getClass()==String.class){
//                this.ngayDong= LocalDate.parse((String)ngayDong);
//            }
//        }else{
//            this.ngayDong = null;
//        }
//
//    }
    public int getSoTienDong() {
        return soTienDong;
    }

    public void setSoTienDong(int soTienDong) {
        this.soTienDong = soTienDong;
    }
}
