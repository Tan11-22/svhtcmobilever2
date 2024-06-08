package com.example.svhtcmobile.Model;

public class SinhVien {
    private String masv;
    private String ho;
    private String ten;
    private  Boolean phai;
    private String diachi;
    private String ngaysinh;
    private String malop;
    private String lop;
    private Boolean danghihoc ;
    private String sdt;
    private String hinhanh;
    private String email;

    private String imgResource;

    private String imgSend;
    public SinhVien()
    {

    }


    public SinhVien(String masv, String ho, String ten, Boolean phai, String diachi, String ngaysinh, String malop, Boolean danghihoc, String sdt, String hinhanh, String email) {
        this.masv = masv;
        this.ho = ho;
        this.ten = ten;
        this.phai = phai;
        this.diachi = diachi;
        this.ngaysinh = ngaysinh;
        this.malop = malop;
        this.danghihoc = danghihoc;
        this.sdt = sdt;
        this.hinhanh = hinhanh;
        this.email = email;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHinhanh() {
        return hinhanh;
    }

    public void setHinhanh(String hinhanh) {
        this.hinhanh = hinhanh;
    }

    public String getMasv() {
        return masv;
    }

    public void setMasv(String masv) {
        this.masv = masv;
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

    public Boolean getPhai() {
        return phai;
    }

    public void setPhai(Boolean phai) {
        this.phai = phai;
    }

    public String getDiachi() {
        return diachi;
    }

    public void setDiachi(String diachi) {
        this.diachi = diachi;
    }

    public String getNgaysinh() {
        return ngaysinh;
    }

    public void setNgaysinh(String ngaysinh) {
        this.ngaysinh = ngaysinh;
    }

    public String getMalop() {
        return malop;
    }

    public void setMalop(String malop) {
        this.malop = malop;
    }

    public Boolean getDanghihoc() {
        return danghihoc;
    }

    public void setDanghihoc(Boolean danghihoc) {
        this.danghihoc = danghihoc;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public String getImageResource() {
        return imgResource;
    }

    public void setImageResource(String imgResource) {
        this.imgResource = imgResource;
    }

    public String getLop() {
        return lop;
    }

    public void setLop(String lop) {
        this.lop = lop;
    }

    @Override
    public String toString() {
        return "SinhVien{" +
                "masv='" + masv + '\'' +
                ", ho='" + ho + '\'' +
                ", ten='" + ten + '\'' +
                ", phai=" + phai +
                ", diachi='" + diachi + '\'' +
                ", ngaysinh='" + ngaysinh + '\'' +
                ", malop='" + malop + '\'' +
                ", lop='" + lop + '\'' +
                ", danghihoc=" + danghihoc +
                ", sdt='" + sdt + '\'' +
                ", hinhanh='" + hinhanh + '\'' +
                ", email='" + email + '\'' +
                ", imgResource='" + imgResource + '\'' +
                '}';
    }

    public String getImgResource() {
        return imgResource;
    }

    public void setImgResource(String imgResource) {
        this.imgResource = imgResource;
    }

    public String getImgSend() {
        return imgSend;
    }

    public void setImgSend(String imgSend) {
        this.imgSend = imgSend;
    }
}
