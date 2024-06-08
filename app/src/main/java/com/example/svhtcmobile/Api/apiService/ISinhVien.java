package com.example.svhtcmobile.Api.apiService;

import com.example.svhtcmobile.Model.DDKLTC;
import com.example.svhtcmobile.Model.DKLTC;
import com.example.svhtcmobile.Model.HocPhiHocKy;
import com.example.svhtcmobile.Model.HocPhiKeToan;
import com.example.svhtcmobile.Model.HocPhiSinhVien;
import com.example.svhtcmobile.Model.SinhVien;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ISinhVien {
    //Api về học phí
    @GET("hoc-phi/hoc-ky")
    Call<List<HocPhiHocKy>> getDSHocPhiHocKy(@Query("maSV")String maSV,
                                             @Query("nienKhoa")String nienKhoa,
                                             @Query("hocKy")int hocKy);
    @GET("hoc-phi/sinh-vien")
    Call<List<HocPhiSinhVien>> getDSHocPhiSinhVien(@Query("maSV")String maSV);
    @GET("hoc-phi/ke-toan")
    Call<List<HocPhiKeToan>> getDSHocPhiKeToan();
    @GET("hoc-phi/dong-hoc-phi")
    Call<Map<String,String>> dongHocPhi(@Query("maSV")String maSV,
                                        @Query("nienKhoa")String nienKhoa,
                                        @Query("hocKy")int hocKy,
                                        @Query("soTienDong")int soTienDong);
    @GET("hoc-phi/ap-hoc-phi")
    Call<Map<String,String>> apHocPhi(@Query("maSV")String maSV,
                                      @Query("nienKhoa")String nienKhoa,
                                      @Query("hocKy")int hocKy);
    //Api về đăng ký lớp tín chỉ
    @GET("lop-tin-chi/lopsv-dang-ky")
    Call<List<DKLTC>> getDSLopSVDangKy(@Query("maLop")String maLop,
                                       @Query("nienKhoa")String nienKhoa,
                                       @Query("hocKy")int hocKy);
    @GET("lop-tin-chi/ds-dang-ky")
    Call<List<DKLTC>> getDSDangKy( @Query("nienKhoa")String nienKhoa,
                                   @Query("hocKy")int hocKy);
    @GET("lop-tin-chi/da-dang-ky")
    Call<List<DDKLTC>> getDSDDKLTC(@Query("maSV")String maSV,
                                   @Query("nienKhoa")String nienKhoa,
                                   @Query("hocKy")int hocKy);

    @GET("lop-tin-chi/dang-ky")
    Call<Map<String, String>> dangKy(@Query("maSV")String maSV,
                                     @Query("maLTC")int maLTC);
    @GET("lop-tin-chi/bo-dang-ky")
    Call<Map<String, String>> boDangKy(@Query("maSV")String maSV,
                                       @Query("maLTC")int maLTC);

    //Về sinh viên
    @GET("sinh-vien/tim-sinh-vien")
    Call<SinhVien> timSV(@Query("ma-sv")String maSV);


}
