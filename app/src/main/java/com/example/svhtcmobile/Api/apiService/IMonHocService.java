package com.example.svhtcmobile.Api.apiService;

import com.example.svhtcmobile.Model.ApiResponse;
import com.example.svhtcmobile.Model.MonHoc;
import com.google.gson.JsonObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface IMonHocService {

    @GET("thong-tin/mon-hoc/danh-sach-mon-hoc")
    Call<ApiResponse<List<JsonObject>>> danhSachMonHoc(@Query("start") int start, @Query("size") int size) ;

    @POST("thong-tin/mon-hoc/them-mon-hoc")
    Call<ApiResponse> themMonHocMoi(@Body MonHoc monHoc);
    @POST("thong-tin/mon-hoc/chinh-sua-mon-hoc")
    Call<ApiResponse> updateMonHoc(@Body MonHoc monHoc);
    @GET("thong-tin/mon-hoc/xoa-mon-hoc")
    Call<ApiResponse> xoaMonHoc(@Query("mamh") String mamh);
}
