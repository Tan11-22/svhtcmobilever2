package com.example.svhtcmobile.Api.apiService;

import com.example.svhtcmobile.Model.GiangVien;
import com.example.svhtcmobile.Model.GiangVienKhoa;
import com.example.svhtcmobile.Model.HocPhiList;
import com.example.svhtcmobile.Model.HocPhiSV;
import com.example.svhtcmobile.Model.Lop;
import com.example.svhtcmobile.Model.LopTinChiDTO;
import com.example.svhtcmobile.Model.MonHoc;
import com.example.svhtcmobile.Model.MonHocMapDTO;
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
    @GET("thong-tin/-sinh-vien/tim-sinh-vien")
    Call<SinhVien> timSinhVien(@Query("ma-sv") String masv);
    @GET("thong-tin/sinh-vien/danh-sach-lop")
    Call<List<Lop>> danhSachLop();
    @GET("thong-tin/sinh-vien/loc-ma-lop")
    Call<List<String>> locMaLop();
    @GET("thong-tin/sinh-vien/danh-sach-sv-lop")
    Call<List<SinhVien>> listSVByMaLop(@Query("ma-lop") String malop);

    @Multipart
    @POST("thong-tin/sinh-vien/them-sinh-vien-moi")
    Call<JsonObject> themSinhVien(@Part("sv") RequestBody sinhVien,
                                  @Part MultipartBody.Part filePart);
    @Multipart
    @POST("thong-tin/sinh-vien/update-sv")
    Call<JsonObject> updateSinhVien(@Part("sv") RequestBody sinhVien,
                                    @Part MultipartBody.Part filePart);
    @GET("thong-tin/sinh-vien/xoa-sinh-vien")
    Call<Void> xoaSinhVien(@Query("ma-sv") String masv);
    @POST("thong-tin/sinh-vien/da-nghi-hoc")
    Call<Void> updateDaNghiHoc(@Query("masv") String masv);
    @GET("thong-tin/giang-vien/tim-giang-vien")
    Call<GiangVien> timGiangVien(@Query("ma-gv") String magv);

    @GET("thong-tin/giang-vien/loc-ma-khoa")
    Call<List<String>> locMaKhoa();

    @GET("thong-tin/giang-vien/loc-gv-khoa")
    Call<List<GiangVien>> listGVByMaKhoa(@Query("ma-khoa") String makhoa);
    @Multipart
    @POST("thong-tin/giang-vien/them-giang-vien")
    Call<JsonObject> themGiangVien(@Part("gv") RequestBody giangVien,
                                   @Part MultipartBody.Part filePart);
    @Multipart
    @POST("thong-tin/giang-vien/update-gv")
    Call<JsonObject> updateGiangVien(@Part("gv") RequestBody giangVien,
                                   @Part MultipartBody.Part filePart);
    @GET("thong-tin/giang-vien/xoa-giang-vien")
    Call<Void> xoaGiangVien(@Query("ma-gv") String magv);
    @GET("lop-tin-chi/loc-ma-khoa")
    Call<List<String>> locMaKhoaLTC();
    @GET("lop-tin-chi/loc-nien-khoa")
    Call<List<String>> locNienKhoaLTC();
    @GET("lop-tin-chi/loc-mon-hoc")
    Call<List<MonHocMapDTO>> locMonHocLTC();
    @GET("lop-tin-chi/loc-gv-khoa")
    Call<List<GiangVienKhoa>> locGVKhoa(@Query("ma-khoa") String makhoa);
    @GET("lop-tin-chi/loc-lop-khoa")
    Call<List<String>> locLopKhoa(@Query("ma-khoa") String makhoa);
    @GET("lop-tin-chi/loc-ltc")
    Call<List<LopTinChiDTO>> locLTC(@Query("ma-khoa") String makhoa,@Query("nien-khoa") String nienkhoa,@Query("hoc-ki") int hocki);
    @POST("lop-tin-chi/them-ltc")
    Call<Void> themloptinchi(@Body LopTinChiDTO lopTinChiDTO);
    @POST("lop-tin-chi/update-ltc")
    Call<Void> updateloptinchi(@Body LopTinChiDTO lopTinChiDTO);
    @GET("lop-tin-chi/xoa-ltc")
    Call<Void> xoaloptinchi(@Query("maltc") int maltc);
    @GET("thanh-toan/thong-tin/xem-dssv-hoc-phi")
    Call<List<HocPhiList>> danhSachHocPhi(@Query("nienkhoa") String nienkhoa, @Query("hocki") int hocki);
    @GET("thanh-toan/thong-tin/xem-hoc-phi-sv")
    Call<List<HocPhiSV>> hocphisv(@Query("masv") String masv);
    @GET("thanh-toan/thong-tin/loc-nien-khoa")
    Call<List<String>> locNienKhoaHocPhi();
}
