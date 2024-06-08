package com.example.svhtcmobile.Controller;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.svhtcmobile.Adapter.CustomAdapterSinhVienLTC;
import com.example.svhtcmobile.Api.ApiClient;
import com.example.svhtcmobile.Api.apiService.IQuanTriService;
import com.example.svhtcmobile.Model.LopTinChiTheoGV;
import com.example.svhtcmobile.Model.SinhVienLTC;
import com.example.svhtcmobile.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class DanhSachSinhVienLTC extends AppCompatActivity {
    private ImageButton imgBtnBack;
    private TextView tvTenMon, tvMaMon, tvNhom;
    private ListView lvSinhVienLTC;
    private List<SinhVienLTC> data = new ArrayList<>();
    CustomAdapterSinhVienLTC adapterSinhVienLTC;
    private IQuanTriService iQuanTriService;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danh_sach_sinh_vien_ltc);
        Init();
        DocDL();
        setEvent();
    }

    public void DocDL() {
        data.clear();
        LopTinChiTheoGV ltc = (LopTinChiTheoGV) getIntent().getSerializableExtra("LTC");
        tvTenMon.setText(ltc.getTenMH().trim() + "");
        tvMaMon.setText(ltc.getMaMH().trim() + "");
        tvNhom.setText(ltc.getNhom() + "");
        iQuanTriService.danhSachSinhVienLTC(ltc.getMaLTC()).enqueue(new Callback<List<SinhVienLTC>>() {
            @Override
            public void onResponse(Call<List<SinhVienLTC>> call, Response<List<SinhVienLTC>> response) {
                List<SinhVienLTC> sinhVienLTCS = response.body();
                if(!sinhVienLTCS.isEmpty())
                {
                    data.clear();
                    data.addAll(sinhVienLTCS);
                }

                setAdapterSinhVienLTC();
            }
            @Override
            public void onFailure(Call<List<SinhVienLTC>> call, Throwable throwable) {

            }
        });
    }

    private void setAdapterSinhVienLTC(){
        adapterSinhVienLTC = new CustomAdapterSinhVienLTC(this, R.layout.layout_item_sinhvienltc, data);
        lvSinhVienLTC.setAdapter(adapterSinhVienLTC);
    }
    private void setEvent() {
        imgBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
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
        imgBtnBack = findViewById(R.id.imgBtnBack);
        tvTenMon = findViewById(R.id.tvTenMH);
        tvMaMon = findViewById(R.id.tvMaMH);
        tvNhom = findViewById(R.id.tvNhom);
        lvSinhVienLTC = findViewById(R.id.lvSinhVienLTC);
    }
}