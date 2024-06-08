package com.example.svhtcmobile.Controller;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.svhtcmobile.Adapter.CustomAdapterDiemHocKi;
import com.example.svhtcmobile.Api.ApiClient;
import com.example.svhtcmobile.Api.apiService.IQuanTriService;
import com.example.svhtcmobile.Model.DiemHocKi;
import com.example.svhtcmobile.Model.SinhVienDTO;
import com.example.svhtcmobile.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class XemDiem extends AppCompatActivity {
    private Spinner spNamHoc, spHocKi;
    private TextView tvSTCDki, tvSTCDat, tvSTCKhongDat, tvDiemTk10, tvDiemTk4;
    private TextView tvHoTenSv, tvMssv;
    private ArrayAdapter adapter_namHoc, adapter_hocKi;

    private List<String> danhSachNamHoc = new ArrayList<>();
    private List<String> danhSachHocKi = new ArrayList<>();

    private List<DiemHocKi> data = new ArrayList<>();

    private CustomAdapterDiemHocKi adapterDiemHocKi;

    private ListView lvDiem;

    private String maSV;
    private SharedPreferences sharedPreferences;
    private IQuanTriService iQuanTriService;
    private ImageButton imgBtnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_xem_diem);
            Init();
            DocThongTinCaNhan();
            DocDuLieuSpinner();
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

    private void DocThongTinCaNhan() {
            iQuanTriService.thongTinCaNhan(maSV).enqueue(new Callback<SinhVienDTO>() {
                @Override
                public void onResponse(Call<SinhVienDTO> call, Response<SinhVienDTO> response) {
                    if(response.code() == 200){
                        SinhVienDTO sv = response.body();
                        tvHoTenSv.setText(sv.getHo() + " " + sv.getTen());
                        tvMssv.setText(sv.getMaSV());

                    }
                }

                @Override
            public void onFailure(Call<SinhVienDTO> call, Throwable throwable) {

            }
        });
    }

    private void DocDuLieuDiem(String maSV) {

       iQuanTriService.diemHocKi(maSV, spNamHoc.getSelectedItem().toString(), Integer.parseInt(spHocKi.getSelectedItem().toString())).enqueue(new Callback<List<DiemHocKi>>() {
            @Override
            public void onResponse(Call<List<DiemHocKi>> call, Response<List<DiemHocKi>> response) {
                if(response.code() == 200){
                    data.clear();
                    data.addAll(response.body());
                    setAdapterDiemHocKi();
                    setThongKe(data);
                }
            }

            @Override
            public void onFailure(Call<List<DiemHocKi>> call, Throwable throwable) {

            }
        });
    }

    private void setAdapterDiemHocKi(){
        adapterDiemHocKi = new CustomAdapterDiemHocKi(this, R.layout.layout_item_diemhocki, data);
        lvDiem.setAdapter(adapterDiemHocKi);
    }

    private void DocDuLieuSpinner() {
        // Gan du lieu cho nam hoc
        iQuanTriService.danhSachNamHoc(maSV).enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                danhSachNamHoc.clear();
                List<String> duLieu = response.body();
                danhSachNamHoc.addAll(duLieu);
                adapter_namHoc = new ArrayAdapter(XemDiem.this, R.layout.layout_spinner, danhSachNamHoc);
                spNamHoc.setAdapter(adapter_namHoc);
            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable throwable) {

            }
        });
        spNamHoc.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                iQuanTriService.danhSachHK(maSV, spNamHoc.getSelectedItem().toString()).enqueue(new Callback<List<String>>() {
                    @Override
                    public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                        danhSachHocKi.clear();
                        List<String> duLieu = response.body();
                        danhSachHocKi.addAll(duLieu);
                        adapter_hocKi = new ArrayAdapter(XemDiem.this, R.layout.layout_spinner, danhSachHocKi);
                        spHocKi.setAdapter(adapter_hocKi);
                        DocDuLieuDiem(maSV);
                    }

                    @Override
                    public void onFailure(Call<List<String>> call, Throwable throwable) {

                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spHocKi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                data.clear();
                DocDuLieuDiem(maSV);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
    private void setThongKe(List<DiemHocKi> data){
        int tongSTC = 0, stcDat = 0, stcKhongDat = 0;
        float diemTk10 = 0, diemTk4 = 0, tongDiem10 = 0, tongDiem4 = 0;
        for(DiemHocKi dhk : data){
            dhk.setDiemTK4VaDiemTKC(dhk.getDiemTK10());
            tongSTC += dhk.getSoTinChi();
            if(dhk.getKetQua()){
                stcDat += dhk.getSoTinChi();
            }
            tongDiem10 += dhk.getDiemTK10();
            tongDiem4 += dhk.getDiemTK4();
        }
        stcKhongDat = tongSTC - stcDat;
        diemTk10 = (float) (Math.round(tongDiem10 / data.size() * 100.0) / 100.0);
        diemTk4 = (float) (Math.round(tongDiem4 /data.size() * 100.0) / 100.0);

        tvSTCDki.setText(tongSTC + "");
        tvSTCDat.setText(stcDat + "");
        tvSTCKhongDat.setText(stcKhongDat + "");
        tvDiemTk10.setText(diemTk10 + "");
        tvDiemTk4.setText(diemTk4 + "");
    }

    private void Init() {
        data.clear();

        sharedPreferences = getSharedPreferences("Account", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "");
        String ho = sharedPreferences.getString("ho", "");
        String ten = sharedPreferences.getString("ten", "");
        String username = sharedPreferences.getString("username", "");
        String quyen = sharedPreferences.getString("quyen", "");
        Retrofit retrofit = ApiClient.getClient(token);
        iQuanTriService = retrofit.create(IQuanTriService.class);
        maSV = username;

        tvHoTenSv = findViewById(R.id.tvTenSV);
        tvMssv = findViewById(R.id.tvMSSV);
        spNamHoc = findViewById(R.id.spNamHoc);
        spHocKi = findViewById(R.id.spHocKi);
        lvDiem = findViewById(R.id.lvDiem);
        tvSTCDki = findViewById(R.id.tvSTCDki);
        tvSTCDat = findViewById(R.id.tvSTCDat);
        tvSTCKhongDat = findViewById(R.id.tvSTCKhongDat);
        tvDiemTk10 = findViewById(R.id.tvDiemTk10);
        tvDiemTk4 = findViewById(R.id.tvDiemTk4);
        imgBtnBack = findViewById(R.id.imgBtnBack);
    }
}