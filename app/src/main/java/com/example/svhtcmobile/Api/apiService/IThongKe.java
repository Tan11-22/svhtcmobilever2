package com.example.svhtcmobile.Api.apiService;

import com.example.svhtcmobile.Model.DanhSachHocPhiLop;
import com.example.svhtcmobile.Model.SinhVien;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface IThongKe {
    @GET("ds-hocphi-theolop")
    Call<List<DanhSachHocPhiLop>> getDanhSachHocPhiTheoLop(@Query("lop") String lop,
                                                           @Query("nienkhoa") String nienkhoa,
                                                           @Query("hocky") int hocky);
    @GET("ds-lop")
    Call<List<String>> getDanhSachMaLop();

    @GET("ds-nienkhoa")
    Call<List<String>> getNienKhoa();

    @GET("sinhvien/{id}")
    Call<SinhVien> getSinhVienById(@Path("id") String masv);

    @POST("sinhvien/update")
    Call<String> updateSinhVien(@Body SinhVien sinhVien);
}
