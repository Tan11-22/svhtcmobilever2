package com.example.svhtcmobile.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class UserInfo implements Serializable {
    @SerializedName("username")
    private String username;
    @SerializedName("ho")
    private String ho;
    @SerializedName("ten")
    private String ten;
    @SerializedName("tenQuyen")
    private String tenQuyen;
    @SerializedName("password")
    private String password;
    @SerializedName("idQuyen")
    private Integer idQuyen;
    @SerializedName("ma")
    private String ma;
    @SerializedName("phai")
    private Boolean phai = false;
    @SerializedName("sdt")
    private String sdt ;
    @SerializedName("email")
    private String email ;

    public UserInfo() {

    }

    public UserInfo(String username, String ho, String ten, String tenQuyen, String password, Integer idQuyen, String ma, Boolean phai, String sdt,String email) {
        this.username = username;
        this.ho = ho;
        this.ten = ten;
        this.tenQuyen = tenQuyen;
        this.password = password;
        this.idQuyen = idQuyen;
        this.ma = ma;
        this.phai = phai;
        this.sdt = sdt;
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public String getTenQuyen() {
        return tenQuyen;
    }

    public void setTenQuyen(String tenQuyen) {
        this.tenQuyen = tenQuyen;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getIdQuyen() {
        return idQuyen;
    }

    public void setIdQuyen(Integer idQuyen) {
        this.idQuyen = idQuyen;
    }

    public String getMa() {
        return ma;
    }

    public void setMa(String ma) {
        this.ma = ma;
    }

    public Boolean getPhai() {
        return phai;
    }

    public void setPhai(Boolean phai) {
        this.phai = phai;
    }



    @Override
    public String toString() {
        return "UserInfo{" +
                "username='" + username + '\'' +
                ", ho='" + ho + '\'' +
                ", ten='" + ten + '\'' +
                ", tenQuyen='" + tenQuyen + '\'' +
                ", password='" + password + '\'' +
                ", idQuyen=" + idQuyen +
                ", ma='" + ma + '\'' +
                ", phai=" + phai +
                ", sdt=" + sdt +
                '}';
    }
}
