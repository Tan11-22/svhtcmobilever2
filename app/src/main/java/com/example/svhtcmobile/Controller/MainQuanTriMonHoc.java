package com.example.svhtcmobile.Controller;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


import com.example.svhtcmobile.Adapter.AdapterMonHoc;
import com.example.svhtcmobile.Api.ApiClient;
import com.example.svhtcmobile.Api.apiService.ILoginService;
import com.example.svhtcmobile.Api.apiService.IQuanTriThongTin;
import com.example.svhtcmobile.Model.MonHoc;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import com.example.svhtcmobile.R;
public class MainQuanTriMonHoc extends AppCompatActivity {

    private ImageButton imgBtnBack, imgBtnLogout, btnAddMH;
    private TextView tvVeNhaTruong, tvXemThongTinCaNhan;
    private ListView lvMonHoc;
    ArrayList<MonHoc> danhSachMonHoc = new ArrayList<>();
    AdapterMonHoc adapter;
    SharedPreferences accountSharedPref;
    IQuanTriThongTin iQuanTriThongTin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quan_tri_mon_hoc);
        setControl();
        setEvent();
    }

    private void setControl() {
        // Ánh xạ các thành phần giao diện
        imgBtnBack = findViewById(R.id.imgBtnBack);
        imgBtnLogout = findViewById(R.id.imgBtnLogout);
        btnAddMH = findViewById(R.id.btnAddMH);
        tvVeNhaTruong = findViewById(R.id.tvVeNhaTruong);
        tvXemThongTinCaNhan = findViewById(R.id.tvXemThongTinCaNhan);
        lvMonHoc = findViewById(R.id.lvMonHoc);
        accountSharedPref = getSharedPreferences("Account", Context.MODE_PRIVATE);
        String token = accountSharedPref.getString("token", "");
        Retrofit retrofit = ApiClient.getClient(token);
        iQuanTriThongTin = retrofit.create(IQuanTriThongTin.class);

        iQuanTriThongTin.danhSachMonHoc().enqueue(new Callback<List<MonHoc>>() {
            @Override
            public void onResponse(Call<List<MonHoc>> call, Response<List<MonHoc>> response) {
                if (response.isSuccessful() && response.body() != null) {


                    danhSachMonHoc.addAll(response.body());
                    adapter = new AdapterMonHoc(MainQuanTriMonHoc.this, R.layout.mon_hoc_item, danhSachMonHoc,iQuanTriThongTin);
                    lvMonHoc.setAdapter(adapter);
                    Log.e("API Response", "Success: " + response.message());
                } else {
                    Log.e("API Response", "Error: " + response.message());
                }

            }

            @Override
            public void onFailure(Call<List<MonHoc>> call, Throwable throwable) {
                Log.e("API Response", "Error: " + throwable.getMessage());

            }  });
    }
    private void setEvent() {
        imgBtnBack.setOnClickListener(view -> finish());

        // Xử lý sự kiện khi nhấn nút đăng xuất
        imgBtnLogout.setOnClickListener(view -> finish());

        // Xử lý sự kiện khi nhấn nút thêm môn học mới
        btnAddMH.setOnClickListener(view -> {
            // Tạo AlertDialog.Builder
            AlertDialog.Builder builder = new AlertDialog.Builder(MainQuanTriMonHoc.this);

            // Gắn layout cho dialog
            View dialogView = getLayoutInflater().inflate(R.layout.add_mon_hoc, null);
            builder.setView(dialogView);

            // Lấy ra các button từ layout của dialog
            Button btnHuy = dialogView.findViewById(R.id.btnHuyMH);
            Button btnLuu = dialogView.findViewById(R.id.btnLuuMH);

            // Tạo và hiển thị dialog
            AlertDialog dialog = builder.create();
            dialog.show();

            // Thiết lập hành động cho nút "Lưu"
            btnLuu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    EditText edtMaMH = dialogView.findViewById(R.id.edtAddMaMH);
                    EditText edtTenMH = dialogView.findViewById(R.id.edtAddTenMH);
                    EditText edtSoTinChi = dialogView.findViewById(R.id.edtAddSoTinChi);
                    EditText edtSoTietLT = dialogView.findViewById(R.id.edtAddSoTietLT);
                    EditText edtSoTietTH = dialogView.findViewById(R.id.edtAddSoTietTH);

                    String maMH = edtMaMH.getText().toString();
                    Boolean check = true;

                    for(MonHoc mh : danhSachMonHoc)
                    {
                        if (maMH.equals(mh.getMaMH().trim()))
                        {
                            check=false;
                            Toast.makeText(getApplicationContext(), "Mã môn học đã tồn tại!", Toast.LENGTH_SHORT).show();
                        }

                    }
                    String tenMH = edtTenMH.getText().toString();

                    int soTinChi = Integer.parseInt(edtSoTinChi.getText().toString());
                    int soTietLT = Integer.parseInt(edtSoTietLT.getText().toString());
                    int soTietTH = Integer.parseInt(edtSoTietTH.getText().toString());

                    if (maMH.isEmpty()||tenMH.isEmpty()){
                        check=false;
                        Toast.makeText(getApplicationContext(), "Không để trống dữ liệu!", Toast.LENGTH_SHORT).show();
                    }
                    if (check == true){
                        MonHoc monHocMoi = new MonHoc(maMH,tenMH, soTietLT, soTietTH,soTinChi);

                        iQuanTriThongTin.themMonHocMoi(monHocMoi).enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                if (response.isSuccessful()) {
                                    danhSachMonHoc.add(monHocMoi);

                                    // Cập nhật giao diện ListView
                                    adapter.notifyDataSetChanged();
                                    Toast.makeText(getApplicationContext(), "Thêm thành công!", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                } else {
                                    Toast.makeText(getApplicationContext(), "Thêm không thành công!", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                }
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                // Xử lý khi có lỗi xảy ra
                            }
                        });
                    }
                }

            });

            // Thiết lập hành động cho nút "Hủy"
            btnHuy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    dialog.dismiss();
                }
            });
        });

        // Xử lý sự kiện khi nhấn vào nút "Về nhà trường"
        tvVeNhaTruong.setOnClickListener(view -> {

        });

        // Xử lý sự kiện khi nhấn vào nút "Xem thông tin cá nhân"
        tvXemThongTinCaNhan.setOnClickListener(view -> {

        });
    }



}