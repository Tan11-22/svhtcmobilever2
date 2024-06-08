package com.example.svhtcmobile.Api.apiService;

import com.example.svhtcmobile.Model.DiemHocKi;
import com.example.svhtcmobile.Model.DiemSinhVienLTC;
import com.example.svhtcmobile.Model.He;
import com.example.svhtcmobile.Model.Lop;
import com.example.svhtcmobile.Model.LopDTO;
import com.example.svhtcmobile.Model.LopTinChi;
import com.example.svhtcmobile.Model.LopTinChiTheoGV;
import com.example.svhtcmobile.Model.MonHocDTO;
import com.example.svhtcmobile.Model.Nganh;
import com.example.svhtcmobile.Model.SinhVienDTO;
import com.example.svhtcmobile.Model.SinhVienLTC;
import com.example.svhtcmobile.Model.TTGiangVienAPI;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface IQuanTriService {
    @GET("lop-tin-chi")
    Call<List<LopTinChiTheoGV>> danhSachLTCTheoMaGV(@Query("ma-gv") String maGV);

    @GET("giang-vien/thong-tin-ca-nhan")
    Call<TTGiangVienAPI> thongTinGiangVien(@Query("ma-gv") String maGV);

    @GET("lop-tin-chi/danh-sach-sinh-vien")
    Call<List<SinhVienLTC>> danhSachSinhVienLTC(@Query("ma-ltc") int maLTC);

    @GET("lop-tin-chi/xem-diem")
    Call<DiemSinhVienLTC> diemSinhVien(@Query("ma-ltc") int maLTC, @Query("ma-sv") String maSV);

    @POST("lop-tin-chi/nhap-diem")
    Call<Void> capNhatDiem(@Body DiemSinhVienLTC diemSinhVienLTC);

    @GET("xem-diem/danh-sach-hoc-ki")
    Call<List<String>> danhSachHK(@Query("ma-sinh-vien") String maSV, @Query("nien-khoa") String nienKhoa);

    @GET("xem-diem/danh-sach-nam-hoc")
    Call<List<String>> danhSachNamHoc(@Query("ma-sinh-vien") String maSV);

    @GET("xem-diem/diem-hoc-ki")
    Call<List<DiemHocKi>> diemHocKi(@Query("ma-sinh-vien") String maSV, @Query("nien-khoa") String nienKhoa, @Query("hoc-ki") int hocKi);

    @GET("sinh-vien/thong-tin-ca-nhan")
    Call<SinhVienDTO> thongTinCaNhan(@Query("ma-sv") String maSV);

    @GET("lop/danh-sach-lop")
    Call<List<LopDTO>> danhSachLopCuaKhoa(@Query("ma-gv") String maGV, @Query("trang-thai") int trangThai);

    @GET("he-dao-tao/danh-sach")
    Call<List<He>> danhSachHeDaoTao();

    @POST("lop/them-lop")
    Call<Void> themLop(@Body LopDTO lop);

    @PATCH("lop/cap-nhat")
    Call<Void> capNhatLop(@Body LopDTO lop);

    @DELETE("lop/xoa-lop")
    Call<Void> xoaLop(@Query("ma-lop") String maLop);

    @GET("lop-tin-chi/danh-sach-lop-tin-chi")
    Call<List<LopTinChi>> danhSachLTC(@Query("ma-khoa") String maKhoa);

    @GET("lop-tin-chi/danh-sach-dieu-kien-loc-ltc")
    Call<List<String>> danhSachDieuKienLocLTC(@Query("loai") int loai, @Query("ma-khoa") String maKhoa);

    @GET("lop-tin-chi/danh-sach-loc-them-ltc")
    Call<List<String>> danhSachLocThemLTC(@Query("loai") int loai, @Query("ma-khoa") String maKhoa);

    @POST("lop-tin-chi/them-ltc")
    Call<LopTinChi> themLTC(@Body LopTinChi ltc);

    @GET("lop-tin-chi/lay-ma-ltc")
    Call<LopTinChi> layMaLTC(@Query("ten-mh") String tenMH,
                             @Query("nien-khoa") String nienKhoa,
                             @Query("nhom") int nhom,
                             @Query("hoc-ki") int hocKi);

    @PATCH("lop-tin-chi/cap-nhat")
    Call<Void> capNhatLTC(@Body LopTinChi ltc);

    @GET("mon-hoc/danh-sach-mon-hoc")
    Call<List<MonHocDTO>> danhSachMonHoc();

    @GET("mon-hoc/so-tc")
    Call<MonHocDTO> soTC(@Query("ten-mh") String tenMH);

    @DELETE("lop-tin-chi/xoa-ltc")
    Call<Void> xoaLTC(@Query("ma-ltc") int maLTC);

    @POST("he-dao-tao/them-moi")
    Call<Void> themMoiHDT(@Body He he);

    @GET("he-dao-tao/tim-he")
    Call<He> timHeTheoTen(@Query("ten-he") String tenHe);

    @PATCH("he-dao-tao/cap-nhat")
    Call<Void> capNhatHDT(@Body He he);

    @DELETE("he-dao-tao/xoa-he")
    Call<Void> xoaHDT(@Query("id") int id);

    @GET("nganh/danh-sach-nganh")
    Call<List<Nganh>> danhSachNganh();

    @POST("nganh/them-moi")
    Call<Void> themMoiNganh(@Body Nganh nganh);

    @PATCH("nganh/cap-nhat")
    Call<Void> capNhatNganh(@Body Nganh nganh);

    @DELETE("nganh/xoa-nganh")
    Call<Void> xoaNganh(@Query("id") int id);

    @GET("nganh/tim-nganh")
    Call<Nganh> timNganhTheoTen(@Query("ten-nganh") String tenNganh);
}
