package com.example.svhtcmobile.Controller;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.svhtcmobile.Adapter.CustomAdapterHe;
import com.example.svhtcmobile.Api.ApiClient;
import com.example.svhtcmobile.Api.apiService.IQuanTriService;
import com.example.svhtcmobile.Model.He;
import com.example.svhtcmobile.Model.TTGiangVienAPI;
import com.example.svhtcmobile.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class DanhSachHeDaoTao extends AppCompatActivity {
    private TextView tvTenGiangVien, tvTenKhoa;
    private ListView lvHeDaoTao;
    private CustomAdapterHe adapterHe;
    private ImageButton imgBtnThemHeDaoTao;
    private ImageButton imgBtnBack;
    List<He> data = new ArrayList<>();
    private String maGV;
    private SharedPreferences sharedPreferences;
    private IQuanTriService iQuanTriService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danh_sach_he_dao_tao);
        Init();
        DocDuLieuHeDaoTao();
        DocThongTinCaNhan();
        setEvent();
    }

    private void DocThongTinCaNhan() {
        iQuanTriService.thongTinGiangVien(maGV).enqueue(new Callback<TTGiangVienAPI>() {
            @Override
            public void onResponse(Call<TTGiangVienAPI> call, Response<TTGiangVienAPI> response) {
                if(response.code() == 200){
                    TTGiangVienAPI thongTin = response.body();
                    tvTenGiangVien.setText(thongTin.getHo() + " " + thongTin.getTen());
                    tvTenKhoa.setText(thongTin.getTenKhoa());
                }
            }

            @Override
            public void onFailure(Call<TTGiangVienAPI> call, Throwable throwable) {

            }
        });
    }

    private void setEvent() {
        imgBtnThemHeDaoTao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialogThemHeDaoTao();
            }
        });
        imgBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void DocDuLieuHeDaoTao() {
        iQuanTriService.danhSachHeDaoTao().enqueue(new Callback<List<He>>() {
            @Override
            public void onResponse(Call<List<He>> call, Response<List<He>> response) {
                if(response.code() == 200){
                    data.clear();
                    data.addAll(response.body());
                    setAdapterHeDaoTao();
//                    Toast.makeText(DanhSachHeDaoTao.this, "Size " + data.size(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<He>> call, Throwable throwable) {

            }
        });
    }
    private void openDialogThemHeDaoTao(){
        final Dialog dialog = new Dialog(DanhSachHeDaoTao.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_dialog_them_he_dao_tao);

        Window window = dialog.getWindow();
        if(window == null) return;

        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAtrributes = window.getAttributes();
        windowAtrributes.gravity = Gravity.CENTER;
        window.setAttributes(windowAtrributes);

        Button btnOk = dialog.findViewById(R.id.btnOk);
        Button btnHuy = dialog.findViewById(R.id.btnHuy);
        EditText edtTenHe = dialog.findViewById(R.id.edtTenheDaoTao);

        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                He he = new He();
                he.setTenHe(edtTenHe.getText().toString());
                iQuanTriService.themMoiHDT(he).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if(response.code() == 200){ // them thanh cong
                            dialog.dismiss();
                            updateAdapter(he);
                            Toast.makeText(DanhSachHeDaoTao.this, "Thêm hệ đào tạo thành công", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(DanhSachHeDaoTao.this, "Thêm hệ đào tạo thất bại", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable throwable) {

                    }
                });
                ;
            }
        });

        dialog.setCancelable(true);
        dialog.show();
    }

    private void setAdapterHeDaoTao() {
        adapterHe = new CustomAdapterHe(this, R.layout.layout_item_he_dao_tao, data);
        lvHeDaoTao.setAdapter(adapterHe);
    }

    private void updateAdapter(He he){
        iQuanTriService.timHeTheoTen(he.getTenHe()).enqueue(new Callback<He>() {
            @Override
            public void onResponse(Call<He> call, Response<He> response) {
                if(response.code() == 200){
                    he.setId(response.body().getId());
                    data.add(he);
                    adapterHe.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<He> call, Throwable throwable) {

            }
        });

    }

    private void Init() {
        sharedPreferences = getSharedPreferences("Account", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "");
        String ho = sharedPreferences.getString("ho", "");
        String ten = sharedPreferences.getString("ten", "");
        String username = sharedPreferences.getString("username", "");
        String quyen = sharedPreferences.getString("quyen", "");
        Retrofit retrofit = ApiClient.getClient(token);
        iQuanTriService = retrofit.create(IQuanTriService.class);
        maGV = username;

        tvTenGiangVien = findViewById(R.id.tvTenGiangVien);
        tvTenKhoa = findViewById(R.id.tvTenKhoa);
        lvHeDaoTao = findViewById(R.id.lvHeDaoTao);
        imgBtnThemHeDaoTao = findViewById(R.id.imgBtnThemHeDaoTao);
        imgBtnBack = findViewById(R.id.imgBtnBack);
    }
}