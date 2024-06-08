package com.example.svhtcmobile.Controller;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.svhtcmobile.Adapter.CustomAdapterMonHocTheoGV;
import com.example.svhtcmobile.Api.ApiClient;
import com.example.svhtcmobile.Api.apiService.IQuanTriService;
import com.example.svhtcmobile.Model.LopTinChiTheoGV;
import com.example.svhtcmobile.Model.TTGiangVienAPI;
import com.example.svhtcmobile.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class DanhSachMonHocGV extends AppCompatActivity {
    private ListView lvMonHoc;
    private ImageButton imgBtnBack, imgBtnLogout;
    private TextView tvHoten, tvTenKhoa;
    private List<LopTinChiTheoGV> data = new ArrayList<>();
    CustomAdapterMonHocTheoGV adapterLTC;
    private TTGiangVienAPI thongTinGV;
    private SharedPreferences sharedPreferences;
    private IQuanTriService iQuanTriService;
    private String maGV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danh_sach_mon_hoc_gv);
        Init();
        DocDL();
        setEvent();
    }

    private void setEvent() {
        imgBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void DocDL() {
        //thông tin giảng viên
        iQuanTriService.thongTinGiangVien(maGV).enqueue(new Callback<TTGiangVienAPI>() {
            @Override
            public void onResponse(Call<TTGiangVienAPI> call, Response<TTGiangVienAPI> response) {
                thongTinGV = response.body();
                tvHoten.setText(thongTinGV.getHo() + " " + thongTinGV.getTen());
                tvTenKhoa.setText(thongTinGV.getTenKhoa());
            }

            @Override
            public void onFailure(Call<TTGiangVienAPI> call, Throwable throwable) {
                Toast.makeText(DanhSachMonHocGV.this, "Call API Error TTGV", Toast.LENGTH_LONG).show();
            }
        });

        // danh sách ltc
        iQuanTriService.danhSachLTCTheoMaGV(maGV).enqueue(new Callback<List<LopTinChiTheoGV>>() {
            @Override
            public void onResponse(Call<List<LopTinChiTheoGV>>call, Response<List<LopTinChiTheoGV>> response) {
                List<LopTinChiTheoGV> listLTC = response.body();
                if(!listLTC.isEmpty()){
                    data.clear();
                    data.addAll(listLTC);
                }
                setAdapterLvMonHoc();
            }

            @Override
            public void onFailure(Call<List<LopTinChiTheoGV>> call, Throwable throwable) {
            }
        });
    }

    private void setAdapterLvMonHoc() {
        adapterLTC = new CustomAdapterMonHocTheoGV(this, R.layout.layout_item_monhoc, data);
        lvMonHoc.setAdapter(adapterLTC);
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

        lvMonHoc = findViewById(R.id.lvMonHoc);
        imgBtnBack = findViewById(R.id.imgBtnBack);
        imgBtnLogout = findViewById(R.id.imgBtnLogout);
        tvHoten = findViewById(R.id.tvTenGiangVien);
        tvTenKhoa = findViewById(R.id.tvTenKhoa);
    }
}