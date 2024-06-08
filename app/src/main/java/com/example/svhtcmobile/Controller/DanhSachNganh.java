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

import com.example.svhtcmobile.Adapter.CustomAdapterNganh;
import com.example.svhtcmobile.Api.ApiClient;
import com.example.svhtcmobile.Api.apiService.IQuanTriService;
import com.example.svhtcmobile.Model.Nganh;
import com.example.svhtcmobile.Model.TTGiangVienAPI;
import com.example.svhtcmobile.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class DanhSachNganh extends AppCompatActivity {
    private ListView lvNganh;
    private ImageButton imgBtnThemNganh, imgBtnBack;
    private TextView tvTenGiangVien, tvTenKhoa;
    private List<Nganh> data = new ArrayList<>();
    private String maGV;
    CustomAdapterNganh adapterNganh;

    private SharedPreferences sharedPreferences;
    private IQuanTriService iQuanTriService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danh_sach_nganh);
        Init();
        DocDuLieuNganh();
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

    private void DocDuLieuNganh() {
        iQuanTriService.danhSachNganh().enqueue(new Callback<List<Nganh>>() {
            @Override
            public void onResponse(Call<List<Nganh>> call, Response<List<Nganh>> response) {
                if(response.code() == 200){
                    data.addAll(response.body());
                    setAdapterNganh();
                }
            }

            @Override
            public void onFailure(Call<List<Nganh>> call, Throwable throwable) {

            }
        });
    }
    private void setAdapterNganh(){
        adapterNganh = new CustomAdapterNganh(this, R.layout.layout_item_nganh, data);
        lvNganh.setAdapter(adapterNganh);
    }

    private void setEvent(){
        imgBtnThemNganh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDiaLogThemNganh();
            }
        });
        imgBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
    private void openDiaLogThemNganh(){
        final Dialog dialog = new Dialog(DanhSachNganh.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_dialog_them_nganh);

        Window window = dialog.getWindow();
        if(window == null) return;

        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAtrributes = window.getAttributes();
        windowAtrributes.gravity = Gravity.CENTER;
        window.setAttributes(windowAtrributes);

        Button btnOk = dialog.findViewById(R.id.btnOk);
        Button btnHuy = dialog.findViewById(R.id.btnHuy);
        EditText edtTenNganh = dialog.findViewById(R.id.edtTenNganh);

        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Nganh nganh = new Nganh();
                nganh.setTenNganh(edtTenNganh.getText().toString());
                iQuanTriService.themMoiNganh(nganh).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if(response.code() == 200){
                            Toast.makeText(DanhSachNganh.this, "Thêm hệ ngành thành công", Toast.LENGTH_LONG).show();
                            dialog.dismiss();
                            updateAdapter(nganh);
                        } else {
                            Toast.makeText(DanhSachNganh.this, "Thêm hệ ngành thất bại", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable throwable) {

                    }
                });
            }
        });
        dialog.setCancelable(true);
        dialog.show();
    }
    private void updateAdapter(Nganh nganh){
        iQuanTriService.timNganhTheoTen(nganh.getTenNganh()).enqueue(new Callback<Nganh>() {
            @Override
            public void onResponse(Call<Nganh> call, Response<Nganh> response) {
                if(response.code() == 200){
                    nganh.setIdNganh(response.body().getIdNganh());
                    data.add(nganh);
                    adapterNganh.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<Nganh> call, Throwable throwable) {

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

        lvNganh = findViewById(R.id.lvNganh);
        imgBtnThemNganh = findViewById(R.id.imgBtnThemNganh);
        tvTenGiangVien = findViewById(R.id.tvTenGiangVien);
        tvTenKhoa = findViewById(R.id.tvTenKhoa);
        imgBtnBack = findViewById(R.id.imgBtnBack);
    }
}