package com.example.svhtcmobile.Controller;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


import com.example.svhtcmobile.Adapter.CustomAdapterTaiKhoan;
import com.example.svhtcmobile.Api.ApiClient;
import com.example.svhtcmobile.Api.apiService.ILoginService;
import com.example.svhtcmobile.Model.TaiKhoanDTO;
import com.example.svhtcmobile.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class TaiKhoanController extends AppCompatActivity {

    TextView tvMa, tvTen;
    ListView lvTaiKhoan;

    SharedPreferences accountSharedPref;
    ILoginService iLoginService;
    List<TaiKhoanDTO> dataTaiKhoan = new ArrayList<>();

    CustomAdapterTaiKhoan customAdapterTaiKhoan;
    ImageView ivOut;
    Spinner spVaiTro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tai_khoan_controller);
        setControl();
        setEvent();
    }

    private void setEvent() {
        ivOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setControl() {
        lvTaiKhoan = findViewById(R.id.lvTaiKhoan);
        tvMa = findViewById(R.id.tvMa);
        tvTen = findViewById(R.id.tvTen);
        ivOut= findViewById(R.id.ivOut);
        spVaiTro = findViewById(R.id.spVaiTro);
        List<String> vaiTros = new ArrayList<>();
        vaiTros.add("1 - Sinh viên");
        vaiTros.add(("2 - Giảng viên"));
        spVaiTro.setAdapter(new ArrayAdapter(TaiKhoanController.this, android.R.layout.simple_list_item_1,vaiTros));
        accountSharedPref = getSharedPreferences("Account", Context.MODE_PRIVATE);
        String token = accountSharedPref.getString("token", "");
        String ho = accountSharedPref.getString("ho", "");
        String ten = accountSharedPref.getString("ten", "");
        String username = accountSharedPref.getString("username", "");
        String quyen = accountSharedPref.getString("quyen", "");

        tvTen.setText(ho + " "+ten);
        tvMa.setText("Mã: "+ username+ " - "+quyen);

        Log.e("logchecktoken", token);
        Retrofit retrofit = ApiClient.getClient(token);
        iLoginService = retrofit.create(ILoginService.class);
        customAdapterTaiKhoan = new CustomAdapterTaiKhoan(TaiKhoanController.this,R.layout.layout_item_tai_khoan,dataTaiKhoan,iLoginService);
        lvTaiKhoan.setAdapter(customAdapterTaiKhoan);
        spVaiTro.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int idSelect = Integer.parseInt(spVaiTro.getSelectedItem().toString().split(" - ")[0]);
                iLoginService.getDanhSachTaiKhoan(idSelect).enqueue(new Callback<List<TaiKhoanDTO>>() {
                    @Override
                    public void onResponse(Call<List<TaiKhoanDTO>> call, Response<List<TaiKhoanDTO>> response) {
                        if(response.code() == 200) {
                            dataTaiKhoan.clear();
                            dataTaiKhoan.addAll(response.body());
                            customAdapterTaiKhoan.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<TaiKhoanDTO>> call, Throwable throwable) {
Log.e("uygh",throwable.getMessage());
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }
}