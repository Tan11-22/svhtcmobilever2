package com.example.svhtcmobile.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class TaiKhoanDTO implements Serializable {

    @SerializedName("USERNAME")
    private String username;

    @SerializedName("TRANGTHAI")
    private Boolean trangThai;
    @SerializedName("TENQUYEN")
    private String tenQuyen;

    public TaiKhoanDTO() {
    }

    public TaiKhoanDTO(String username, Boolean trangThai, String tenQuyen) {
        this.username = username;
        this.trangThai = trangThai;
        this.tenQuyen = tenQuyen;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Boolean getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(Boolean trangThai) {
        this.trangThai = trangThai;
    }

    public String getTenQuyen() {
        return tenQuyen;
    }

    public void setTenQuyen(String tenQuyen) {
        this.tenQuyen = tenQuyen;
    }
}
