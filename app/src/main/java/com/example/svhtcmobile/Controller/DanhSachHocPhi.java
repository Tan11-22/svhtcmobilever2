package com.example.svhtcmobile.Controller;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.svhtcmobile.Adapter.AdapterDanhSachHocPhi;
import com.example.svhtcmobile.Adapter.AdapterLTC;
import com.example.svhtcmobile.Api.ApiClient;
import com.example.svhtcmobile.Api.apiService.IQuanTriThongTin;
import com.example.svhtcmobile.Model.HocPhiList;
import com.example.svhtcmobile.Model.LopTinChiDTO;
import com.example.svhtcmobile.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class DanhSachHocPhi extends AppCompatActivity {
    private ImageButton imgBtnBack, imgBtnLogout;
    private TextView tvVeNhaTruong, tvXemThongTinCaNhan;
    private ListView lvDanhSachHocPhi;
    private AdapterDanhSachHocPhi adapterDSHP;
    private SharedPreferences accountSharedPref;
    private IQuanTriThongTin iQuanTriThongTin;
    private List<String> DSNienKhoa = new ArrayList<>();
    private List<Integer> dshk = new ArrayList<>();
    private List<HocPhiList> hocphiList = new ArrayList<>();
    private Spinner spNienKhoa, spHocKi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dshk.add(1);
        dshk.add(2);
        setContentView(R.layout.activity_hoc_phi);
        setControl();
        setEvent();
    }

    private void setEvent() {
        imgBtnBack.setOnClickListener(view -> finish());
        imgBtnLogout.setOnClickListener(view -> finish());

        AdapterView.OnItemSelectedListener onItemSelectedListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                filterDanhSachHocPhi();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        };

        spNienKhoa.setOnItemSelectedListener(onItemSelectedListener);
        spHocKi.setOnItemSelectedListener(onItemSelectedListener);
    }

    private void filterDanhSachHocPhi() {
        String nienkhoa = (String) spNienKhoa.getSelectedItem();
        Integer hocki = (Integer) spHocKi.getSelectedItem();

        iQuanTriThongTin.danhSachHocPhi(nienkhoa, hocki).enqueue(new Callback<List<HocPhiList>>() {
            @Override
            public void onResponse(Call<List<HocPhiList>> call, Response<List<HocPhiList>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    hocphiList.clear();
                    hocphiList.addAll(response.body());
                    adapterDSHP.notifyDataSetChanged();
                } else {
                    Log.e("API Response", "Error: " + response.code() + " " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<HocPhiList>> call, Throwable throwable) {
                Log.e("API Response", "Error: " + throwable.getMessage());
            }
        });
    }

    private void setControl() {
        accountSharedPref = getSharedPreferences("Account", Context.MODE_PRIVATE);
        String token = accountSharedPref.getString("token", "");
        Retrofit retrofit = ApiClient.getClient(token);
        iQuanTriThongTin = retrofit.create(IQuanTriThongTin.class);

        imgBtnBack = findViewById(R.id.imgBtnBack);
        imgBtnLogout = findViewById(R.id.imgBtnLogout);
        tvVeNhaTruong = findViewById(R.id.tvVeNhaTruong);
        tvXemThongTinCaNhan = findViewById(R.id.tvXemThongTinCaNhan);
        lvDanhSachHocPhi = findViewById(R.id.lvDanhSachHocPhi);
        spNienKhoa = findViewById(R.id.spNienKhoa);
        spHocKi = findViewById(R.id.spHocKi);

        adapterDSHP = new AdapterDanhSachHocPhi(this, R.layout.layout_item_hocphi_list, hocphiList, iQuanTriThongTin);
        lvDanhSachHocPhi.setAdapter(adapterDSHP);
        ArrayAdapter adapter_spHocki = new ArrayAdapter(DanhSachHocPhi.this, R.layout.layout_spinner, dshk);
        spHocKi.setAdapter(adapter_spHocki);
        iQuanTriThongTin.locNienKhoaHocPhi().enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    DSNienKhoa.addAll(response.body());
                    ArrayAdapter<String> adapter_spNK = new ArrayAdapter<>(DanhSachHocPhi.this, R.layout.layout_spinner, DSNienKhoa);
                    spNienKhoa.setAdapter(adapter_spNK);
                } else {
                    Log.e("API Response", "Error: " + response.code() + " " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable throwable) {
                Log.e("API Response", "Error: " + throwable.getMessage());
            }
        });
    }
}
