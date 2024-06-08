package com.example.svhtcmobile.Api.apiService;

import com.example.svhtcmobile.Model.GiangVien;
import com.example.svhtcmobile.Model.Lop;
import com.example.svhtcmobile.Model.MonHoc;
import com.example.svhtcmobile.Model.SinhVien;
import com.google.gson.JsonObject;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Query;

public interface IQuanTriThongTin {
    @GET("sinh-vien/tim-sinh-vien")
    Call<SinhVien> timSinhVien(@Query("ma-sv") String masv);
    @GET("sinh-vien/danh-sach-lop")
    Call<List<Lop>> danhSachLop();
    @GET("sinh-vien/loc-ma-lop")
    Call<List<String>> locMaLop();
    @GET("sinh-vien/danh-sach-sv-lop")
    Call<List<SinhVien>> listSVByMaLop(@Query("ma-lop") String malop);

    @Multipart
    @POST("sinh-vien/them-sinh-vien-moi")
    Call<JsonObject> themSinhVien(@Part("sv") RequestBody sinhVien,
                                  @Part MultipartBody.Part filePart);
    @POST("sinh-vien/update-sinh-vien")
    Call<Void> updateSinhVien(@Body SinhVien sinhVien);
    @POST("sinh-vien/xoa-sinh-vien")
    Call<Void> xoaSinhVien(@Query("ma-sv") String masv);
    @GET("mon-hoc/danh-sach-mon-hoc")
    Call<List<MonHoc>> danhSachMonHoc() ;
    @GET("mon-hoc/tim-mon-hoc")
    Call<MonHoc> timMonHoc(@Query("ma-mon-hoc") String mamh);
    @POST("mon-hoc/them-mon-hoc-moi")
    Call<Void> themMonHocMoi(@Body MonHoc monHoc);
    @POST("mon-hoc/update-mon-hoc")
    Call<Void> updateMonHoc(@Body MonHoc monHoc);
    @POST("mon-hoc/xoa-mon-hoc")
    Call<Void> xoaMonHoc(@Query("mamh") String mamh);
    @POST("sinh-vien/da-nghi-hoc")
    Call<Void> updateDaNghiHoc(@Query("masv") String masv);
    @POST("sinh-vien/quen-mat-khau")
    Call<Void> quenMatKhau(@Query("email") String email,@Query("password") String password);
    @POST("sinh-vien/doi-mat-khau")
    Call<Void> doiMatKhau(@Query("username") String username,@Query("password") String password);
    @POST("sinh-vien/encode-ten-anh")
    Call<Map<String,String>> encodeTenAnh(@Query("ten-anh") String tenanh);
    @GET("sinh-vien/tai-khoan-by-email")
    Call<Map<String, Object>> taiKhoanByEmail(@Query("Email") String Email);

    @GET("giang-vien/tim-giang-vien")
    Call<GiangVien> timGiangVien(@Query("ma-gv") String magv);

    @GET("giang-vien/loc-ma-khoa")
    Call<List<String>> locMaKhoa();
    @GET("giang-vien/danh-sach-gv-khoa")
    Call<List<GiangVien>> listGVByMaKhoa(@Query("ma-khoa") String makhoa);
    @Multipart
    @POST("giang-vien/them-giang-vien-moi")
    Call<JsonObject> themGiangVien(@Part("gv") RequestBody giangVien,
                             @Part MultipartBody.Part filePart);
    @POST("giang-vien/update-giang-vien")
    Call<Void> updateGiangVien(@Body GiangVien giangVien);
    @POST("giang-vien/xoa-giang-vien")
    Call<Void> xoaGiangVien(@Query("ma-gv") String magv);
}
