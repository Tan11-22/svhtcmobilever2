package com.example.svhtcmobile.Controller;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.example.svhtcmobile.Adapter.CustomAdapterKhoa;
import com.example.svhtcmobile.Api.ApiClient;
import com.example.svhtcmobile.Api.apiService.ILoginService;
import com.example.svhtcmobile.Model.Khoa;
import com.example.svhtcmobile.Model.Nganh;
import com.example.svhtcmobile.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class KhoaController extends AppCompatActivity {

    List<Khoa> dataKhoa = new ArrayList<>();
    ListView lvKhoa;
    SharedPreferences accountSharedPref;
    ILoginService iLoginService;
    CustomAdapterKhoa customAdapterKhoa;

    TextView tvTen, tvMa;

    ImageView ivThemKhoa;
    ImageView ivOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_khoa);
        setControl();
        setEvent();

    }

    private void setEvent() {
        ivThemKhoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogAddKhoa();
            }
        });
        ivOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private void showDialogAddKhoa() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_add_khoa);

        Window window = dialog.getWindow();
        if(window == null) return;

        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAtrributes = window.getAttributes();
        windowAtrributes.gravity = Gravity.CENTER;
        window.setAttributes(windowAtrributes);
        ImageView ivClose = dialog.findViewById(R.id.ivClose);
        EditText edtMaKhoa =dialog.findViewById(R.id.edtMaKhoa);
        EditText edtTenKhoa =dialog.findViewById(R.id.edtTenKhoa);
        Spinner spNganh =dialog.findViewById(R.id.spNganh);
        Button btnTaoMoi =dialog.findViewById(R.id.btnTaoMoi);

        List<String> khoaNganhs = new ArrayList<>();

        iLoginService.getDanhSachNganh().enqueue(new Callback<List<Nganh>>() {
            @Override
            public void onResponse(Call<List<Nganh>> call, Response<List<Nganh>> response) {
                if(response.code()==200) {
                    List<Nganh> nganhs = response.body();
                    for (Nganh nganh : nganhs) {
                        khoaNganhs.add(String.valueOf(nganh.getIdNganh())+ " - "+nganh.getTenNganh());
                    }
                    ArrayAdapter adapterNganh = new ArrayAdapter(KhoaController.this, android.R.layout.simple_list_item_1,khoaNganhs);
                    spNganh.setAdapter(adapterNganh);
                }
            }

            @Override
            public void onFailure(Call<List<Nganh>> call, Throwable throwable) {

            }
        });
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btnTaoMoi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String maKhoa = edtMaKhoa.getText().toString().trim();
                String tenKhoa = edtTenKhoa.getText().toString().trim();
                String nganh = spNganh.getSelectedItem().toString();
                if (maKhoa.equals("")) {
                    edtMaKhoa.setError("Chưa nhập mã khoa!");
                    return;
                }
                if (tenKhoa.equals("")) {
                    edtTenKhoa.setError("Chưa nhập tên khoa!");
                    return;
                }
                int idNganh = Integer.parseInt(nganh.split("-")[0].trim());
                iLoginService.addKhoa(maKhoa,tenKhoa,idNganh).enqueue(new Callback<Boolean>() {
                    @Override
                    public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                        if(response.code() == 200) {
                            if(response.body()){
                                dataKhoa.clear();
                                iLoginService.getDanhSachKhoa().enqueue(new Callback<List<Khoa>>() {
                                    @Override
                                    public void onResponse(Call<List<Khoa>> call, Response<List<Khoa>> response) {
                                        if(response.code()==200) {
                                            dataKhoa.addAll(response.body());
                                            customAdapterKhoa.notifyDataSetChanged();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<List<Khoa>> call, Throwable throwable) {

                                    }
                                });
                                dialog.dismiss();
                            } else {
                                Toast.makeText(KhoaController.this,"Mã khoa đã trùng, tạo khoa thất bại!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Boolean> call, Throwable throwable) {

                    }
                });
            }
        });

        dialog.setCancelable(true);
        dialog.show();
    }
    private void setControl() {
        lvKhoa = findViewById(R.id.lvKhoa);
        tvMa = findViewById(R.id.tvMa);
        tvTen = findViewById(R.id.tvTen);
        ivOut= findViewById(R.id.ivOut);
        ivThemKhoa = findViewById(R.id.ivThemKhoa);
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
        iLoginService.getDanhSachKhoa().enqueue(new Callback<List<Khoa>>() {
            @Override
            public void onResponse(Call<List<Khoa>> call, Response<List<Khoa>> response) {
                if(response.code()==200) {
                    dataKhoa.addAll(response.body());
                    customAdapterKhoa = new CustomAdapterKhoa(KhoaController.this,R.layout.layout_item_khoa,dataKhoa,iLoginService);
                    lvKhoa.setAdapter(customAdapterKhoa);
                }
            }

            @Override
            public void onFailure(Call<List<Khoa>> call, Throwable throwable) {

            }
        });

    }
}