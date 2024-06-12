package com.example.svhtcmobile.Model;

public class MonHocMapDTO {
    private String mamh;
    private String tenmh;
    public MonHocMapDTO()
    {

    }

    @Override
    public String toString() {
        return tenmh; // This will be displayed in the Spinner
    }

    public MonHocMapDTO(String mamh, String tenmh) {
        this.mamh = mamh;
        this.tenmh = tenmh;
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
}
