package com.example.svhtcmobile.Model;

public class LTCDTO {
    private Integer maLTC;
    private String maMH;
    private String tenMH;
    private Integer nhom;
    private Integer soTC;
    private String maLop;
    private Integer soSVToiDa;
    private String maGV;
    private Integer conLai;
    private boolean active = false;

    public LTCDTO() {
    }

    public LTCDTO(Integer maLTC, String maMH, String tenMH, Integer nhom, Integer soTC, String maLop, Integer soSVToiDa,
                  String maGV, Integer conLai) {
        this.maLTC = maLTC;
        this.maMH = maMH;
        this.tenMH = tenMH;
        this.nhom = nhom;
        this.soTC = soTC;
        this.maLop = maLop;
        this.soSVToiDa = soSVToiDa;
        this.maGV = maGV;
        this.conLai = conLai;
    }

    public Integer getMaLTC() {
        return maLTC;
    }

    public void setMaLTC(Integer maLTC) {
        this.maLTC = maLTC;
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

    public Integer getNhom() {
        return nhom;
    }

    public void setNhom(Integer nhom) {
        this.nhom = nhom;
    }

    public Integer getSoTC() {
        return soTC;
    }

    public void setSoTC(Integer soTC) {
        this.soTC = soTC;
    }

    public String getMaLop() {
        return maLop;
    }

    public void setMaLop(String maLop) {
        this.maLop = maLop;
    }

    public Integer getSoSVToiDa() {
        return soSVToiDa;
    }

    public void setSoSVToiDa(Integer soSVToiDa) {
        this.soSVToiDa = soSVToiDa;
    }

    public String getMaGV() {
        return maGV;
    }

    public void setMaGV(String maGV) {
        this.maGV = maGV;
    }

    public Integer getConLai() {
        return conLai;
    }

    public void setConLai(Integer conLai) {
        this.conLai = conLai;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

}
