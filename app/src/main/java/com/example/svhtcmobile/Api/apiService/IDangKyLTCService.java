package com.example.svhtcmobile.Api.apiService;

import com.example.svhtcmobile.Model.ApiResponse;
import com.example.svhtcmobile.Model.LTCDTO;
import com.example.svhtcmobile.Model.MonHoc;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface IDangKyLTCService {
    @GET("lop-tin-chi/dang-ky/danh-sach-ltc-de-dang-ky")
    Call<ApiResponse<List<LTCDTO>>> getDSLTCDeDK(@Query("malop") String maLop, @Query("masv") String maSV);

    @GET("lop-tin-chi/dang-ky/danh-sach-thong-tin-lop")
    Call<ApiResponse<List<Map<String,Object>>>> getDanhSachThongTinLop();

    @GET("lop-tin-chi/dang-ky/danh-sach-ltc-da-dang-ky")
    Call<ApiResponse<List<LTCDTO>>> getDSLTCDaDK( @Query("masv") String maSV);

    @POST("lop-tin-chi/dang-ky/dang-ky-moi")
    Call<ApiResponse> dangKyLTC(@Body Map<String,Object> data);

    @POST("lop-tin-chi/dang-ky/huy-dang-ky")
    Call<ApiResponse> huyDangKyLTC(@Body Map<String,Object> data);

}
