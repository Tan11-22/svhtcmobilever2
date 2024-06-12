package com.example.svhtcmobile.Api.apiService;

import com.example.svhtcmobile.Model.ApiResponse;
import com.example.svhtcmobile.Model.LopDTO;
import com.example.svhtcmobile.Model.LopDTO1;
import com.example.svhtcmobile.Model.MonHoc;
import com.google.gson.JsonObject;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ILopService {
    @GET("thong-tin/lop-hoc/danh-sach-lop-hoc")
    Call<ApiResponse<List<LopDTO1>>> danhSachLopHoc(@Query("start") int start, @Query("size") int size) ;

    @GET("thong-tin/lop-hoc/danh-sach-khoa")
    Call<ApiResponse<List<JsonObject>>> danhSachKhoa() ;

    @GET("thong-tin/lop-hoc/danh-sach-he")
    Call<ApiResponse<List<JsonObject>>> danhSachHe() ;

    @POST("thong-tin/lop-hoc/them-lop-moi")
    Call<ApiResponse> themLopHocMoi(@Body Map<String,Object> lop);
    @POST("thong-tin/lop-hoc/thay-doi-lop")
    Call<ApiResponse> updateLopHoc(@Body Map<String,Object> lop);
    @GET("thong-tin/lop-hoc/xoa-lop-hoc")
    Call<ApiResponse> xoaLop(@Query("malop") String malop);
}
