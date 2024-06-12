package com.example.svhtcmobile.Model;

public class LopTinChiDTO {
    private Integer maltc;
    private String makhoa;
    private String nienkhoa;

    private Integer hocki;
    private Integer nhom;
    private Integer sosvtt;
    private Integer sosvtd;
    private String malop;
    private boolean donglop;
    private String mamh;
    private String tenmh;
    private String magv;
    private String tengiangvien;


    public LopTinChiDTO() {
    }

    public LopTinChiDTO(Integer maltc,String makhoa, String nienkhoa, Integer hocki, Integer nhom, Integer sosvtt, Integer sosvtd, String malop, boolean donglop, String mamh,String magv, String tenmh,  String tengiangvien) {
        this.maltc = maltc;
        this.makhoa = makhoa;
        this.nienkhoa = nienkhoa;
        this.hocki = hocki;
        this.nhom = nhom;
        this.sosvtt = sosvtt;
        this.sosvtd = sosvtd;
        this.malop = malop;
        this.donglop = donglop;
        this.mamh = mamh;
        this.tenmh = tenmh;
        this.magv = magv;
        this.tengiangvien = tengiangvien;
    }

    public String getTengiangvien() {
        return tengiangvien;
    }

    public void setTengiangvien(String tengiangvien) {
        this.tengiangvien = tengiangvien;
    }

    public Integer getMaltc() {
        return maltc;
    }

    public void setMaltc(Integer maltc) {
        this.maltc = maltc;
    }

    public String getMakhoa() {
        return makhoa;
    }

    public void setMakhoa(String makhoa) {
        this.makhoa = makhoa;
    }

    public String getNienkhoa() {
        return nienkhoa;
    }

    public void setNienkhoa(String nienkhoa) {
        this.nienkhoa = nienkhoa;
    }

    public Integer getHocki() {
        return hocki;
    }

    public void setHocki(Integer hocki) {
        this.hocki = hocki;
    }

    public Integer getNhom() {
        return nhom;
    }

    public void setNhom(Integer nhom) {
        this.nhom = nhom;
    }

    public Integer getSosvtt() {
        return sosvtt;
    }

    public void setSosvtt(Integer sosvtt) {
        this.sosvtt = sosvtt;
    }

    public Integer getSosvtd() {
        return sosvtd;
    }

    public void setSosvtd(Integer sosvtd) {
        this.sosvtd = sosvtd;
    }

    public String getMalop() {
        return malop;
    }

    public void setMalop(String malop) {
        this.malop = malop;
    }

    public boolean isDonglop() {
        return donglop;
    }

    public void setDonglop(boolean donglop) {
        this.donglop = donglop;
    }

    public String getMamh() {
        return mamh;
    }

    public void setMamh(String mamh) {
        this.mamh = mamh;
    }

    public String getTenmh() {
        return tenmh;
    }

    public void setTenmh(String tenmh) {
        this.tenmh = tenmh;
    }

    public String getMagv() {
        return magv;
    }

    public void setMagv(String magv) {
        this.magv = magv;
    }

    @Override
    public String toString() {
        return "LopTinChiDTO{" +
                "maltc=" + maltc +
                ", makhoa='" + makhoa + '\'' +
                ", nienkhoa='" + nienkhoa + '\'' +
                ", hocki=" + hocki +
                ", nhom=" + nhom +
                ", sosvtt=" + sosvtt +
                ", sosvtd=" + sosvtd +
                ", malop='" + malop + '\'' +
                ", donglop=" + donglop +
                ", mamh='" + mamh + '\'' +
                ", tenmh='" + tenmh + '\'' +
                ", magv='" + magv + '\'' +
                ", tengiangvien='" + tengiangvien + '\'' +
                '}';
    }
}
