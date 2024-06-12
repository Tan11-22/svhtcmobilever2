// HocPhiSinhVienView.java
package com.example.svhtcmobile.Controller;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.svhtcmobile.Adapter.AdapterHocPhiSV;
import com.example.svhtcmobile.Api.ApiClient;
import com.example.svhtcmobile.Api.apiService.IQuanTriThongTin;
import com.example.svhtcmobile.Model.HocPhiSV;
import com.example.svhtcmobile.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class HocPhiSinhVienView extends AppCompatActivity {
    private ImageButton imgBtnBack, imgBtnLogout;
    private TextView tvVeNhaTruong, tvXemThongTinCaNhan;
    private ListView lvHocPhiSV;
    private AdapterHocPhiSV adapter;
    private SharedPreferences accountSharedPref;
    private IQuanTriThongTin iQuanTriThongTin;
    private List<HocPhiSV> hocPhiSV = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.xem_hoc_phi);
        setControl();
        setEvent();
    }

    private void setEvent() {
        imgBtnBack.setOnClickListener(view -> finish());
        imgBtnLogout.setOnClickListener(view -> finish());
    }

    private void setControl() {
        accountSharedPref = getSharedPreferences("Account", Context.MODE_PRIVATE);
        String token = accountSharedPref.getString("token", "");
        String username = accountSharedPref.getString("username", "");
        Retrofit retrofit = ApiClient.getClient(token);
        iQuanTriThongTin = retrofit.create(IQuanTriThongTin.class);

        imgBtnBack = findViewById(R.id.imgBtnBack);
        imgBtnLogout = findViewById(R.id.imgBtnLogout);
        tvVeNhaTruong = findViewById(R.id.tvVeNhaTruong);
        tvXemThongTinCaNhan = findViewById(R.id.tvXemThongTinCaNhan);
        lvHocPhiSV = findViewById(R.id.lvHocPhiSV);

        adapter = new AdapterHocPhiSV(HocPhiSinhVienView.this, R.layout.layout_item_hocphisinhvien, hocPhiSV, iQuanTriThongTin);
        lvHocPhiSV.setAdapter(adapter);

        iQuanTriThongTin.hocphisv(username).enqueue(new Callback<List<HocPhiSV>>() {
            @Override
            public void onResponse(Call<List<HocPhiSV>> call, Response<List<HocPhiSV>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    hocPhiSV.clear();
                    hocPhiSV.addAll(response.body());
                    adapter.notifyDataSetChanged();
                } else {
                    Log.e("API Response", "Error: " + response.code() + " " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<HocPhiSV>> call, Throwable throwable) {
                Log.e("API Response", "Error: " + throwable.getMessage());
            }
        });
    }
}
