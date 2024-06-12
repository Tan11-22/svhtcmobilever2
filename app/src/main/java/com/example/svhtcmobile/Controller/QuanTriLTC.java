package com.example.svhtcmobile.Controller;

import static android.provider.MediaStore.ACTION_IMAGE_CAPTURE;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.svhtcmobile.Adapter.AdapterLTC;
import com.example.svhtcmobile.Adapter.AdapterSinhVien;
import com.example.svhtcmobile.Api.ApiClient;
import com.example.svhtcmobile.Api.apiService.IQuanTriThongTin;
import com.example.svhtcmobile.Model.GiangVienKhoa;
import com.example.svhtcmobile.Model.LopTinChiDTO;
import com.example.svhtcmobile.Model.MonHocMapDTO;
import com.example.svhtcmobile.Model.SinhVien;
import com.example.svhtcmobile.R;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class QuanTriLTC extends AppCompatActivity {

    private ImageButton btnAddLTC, imgBtnBack, imgBtnLogout;
    private TextView tvVeNhaTruong, tvXemThongTinCaNhan;
    private ListView lvLopTC;
    AdapterLTC adapterLTC;
    String makhoa ,nienkhoa;
    int hocki;
    String magvtemp ="";
    String hotengv = "";
    String mamhtemp = "";
    String tenMHtemp = "";
    SharedPreferences accountSharedPref;
    IQuanTriThongTin iQuanTriThongTin;
    List<String> DSK = new ArrayList<>();
    List<String> DSNienKhoa = new ArrayList<>();
    List<Integer> dshk = new ArrayList<>();
    List<LopTinChiDTO> DSLTC = new ArrayList<>();
    Spinner spKhoa,spNienKhoa,spHocKi;
    Spinner spMH ,spGV ,spLop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dshk.add(1);
        dshk.add(2);
        setContentView(R.layout.activity_lop_tin_chi);
        setControl();
        setEvent();


    }

    private void setEvent() {
        // Xử lý sự kiện khi nhấn nút quay lại
        imgBtnBack.setOnClickListener(view -> finish());

        // Xử lý sự kiện khi nhấn nút đăng xuất
        imgBtnLogout.setOnClickListener(view -> finish());
        AdapterView.OnItemSelectedListener onItemSelectedListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                filterLopTinChi();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Không làm gì khi không có mục nào được chọn
            }
        };

        spKhoa.setOnItemSelectedListener(onItemSelectedListener);
        spNienKhoa.setOnItemSelectedListener(onItemSelectedListener);
        spHocKi.setOnItemSelectedListener(onItemSelectedListener);

        btnAddLTC.setOnClickListener(view -> {
            List<MonHocMapDTO> monhocs = new ArrayList<>();
            List<GiangVienKhoa> giangviens = new ArrayList<>();
            List<String> lops = new ArrayList<>();
            String malopAdd="";

            // Tạo AlertDialog.Builder
            AlertDialog.Builder builder = new AlertDialog.Builder(QuanTriLTC.this);

            // Gắn layout cho dialog
            View dialogView = getLayoutInflater().inflate(R.layout.layout_dialog_them_ltc, null);
            builder.setView(dialogView);
            EditText edtMaKhoa;
            edtMaKhoa = dialogView.findViewById(R.id.loadMaKhoa);
            edtMaKhoa.setText(makhoa);
            EditText edtNienKhoa = dialogView.findViewById(R.id.loadNienKhoa);
            edtNienKhoa.setText(nienkhoa);
            EditText edtHocKi = dialogView.findViewById(R.id.loadHocKi);
            edtHocKi.setText(String.valueOf(hocki));
            spMH = dialogView.findViewById(R.id.spMonHoc);
            spLop = dialogView.findViewById(R.id.spLop);
            spGV = dialogView.findViewById(R.id.spGiangVien);
            // Lấy ra các button từ layout của dialog
            Button btnHuy = dialogView.findViewById(R.id.btnHuy);
            Button btnLuu = dialogView.findViewById(R.id.btnLuuLTC);
            //gán spinner
            iQuanTriThongTin.locMonHocLTC().enqueue(new Callback<List<MonHocMapDTO>>() {
                @Override
                public void onResponse(Call<List<MonHocMapDTO>> call, Response<List<MonHocMapDTO>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        if (response.code() == 200) {
                            monhocs.addAll(response.body());
                            Log.e("MHLIST", monhocs.get(0).getMamh());

                            ArrayAdapter<MonHocMapDTO> adapter_spMH = new ArrayAdapter<>(QuanTriLTC.this, R.layout.layout_spinner, monhocs);
                            spMH.setAdapter(adapter_spMH);
                        }
                    } else {
                        Log.e("API Response", "Error: " + response.message());
                    }
                }

                @Override
                public void onFailure(Call<List<MonHocMapDTO>> call, Throwable throwable) {
                    Log.e("API Response", "Error: " + throwable.getMessage());
                }
            });

            if(spMH!=null){
            spMH.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    MonHocMapDTO selectedMonHoc = (MonHocMapDTO) parent.getItemAtPosition(position);
                    mamhtemp = selectedMonHoc.getMamh();
                    tenMHtemp = selectedMonHoc.getTenmh();
                    // Use magvtemp and hotengvtemp as needed
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    // Handle case when nothing is selected if needed
                }
            });}
            else Log.e("Spinner Initialization", "Spinner spMH is null");
            iQuanTriThongTin.locGVKhoa(makhoa).enqueue(new Callback<List<GiangVienKhoa>>() {
                @Override
                public void onResponse(Call<List<GiangVienKhoa>> call, Response<List<GiangVienKhoa>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        if (response.code() == 200) {
                            giangviens.addAll(response.body());
                            Log.e("GVLIST", giangviens.get(0).getMagv());
                            ArrayAdapter<GiangVienKhoa> adapter_spGV = new ArrayAdapter<>(QuanTriLTC.this, R.layout.layout_spinner, giangviens);
                            spGV.setAdapter(adapter_spGV);
                        }
                    } else {
                        Log.e("API Response", "Error: " + response.message());
                    }
                }

                @Override
                public void onFailure(Call<List<GiangVienKhoa>> call, Throwable throwable) {
                    Log.e("API Response", "Error: " + throwable.getMessage());
                }
            });

// Capture selected item and set magvtemp and hotengvtemp
            if (spGV != null) {
                spGV.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        GiangVienKhoa selectedGiangVien = (GiangVienKhoa) parent.getItemAtPosition(position);
                        magvtemp = selectedGiangVien.getMagv();
                        hotengv = selectedGiangVien.getHo() + " " + selectedGiangVien.getTen();
                        // Use magvtemp and hotengvtemp as needed
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        // Handle case when nothing is selected if needed
                    }
                });
            } else {
                Log.e("Spinner Initialization", "Spinner spGV is null");
            }

            iQuanTriThongTin.locLopKhoa(makhoa).enqueue(new Callback<List<String>>() {
                @Override
                public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                    if (response.isSuccessful() && response.body() != null) {

                        if(response.code() == 200){
                            lops.addAll(response.body());
                            Log.e("LOPLIST", lops.get(0));
                            ArrayAdapter adapter_spLop= new ArrayAdapter(QuanTriLTC.this, R.layout.layout_spinner, lops);
                            spLop.setAdapter(adapter_spLop);


                        }
                    } else {
                        Log.e("API Response", "Error: " + response.message());
                    }
                }

                @Override
                public void onFailure(Call<List<String>> call, Throwable throwable) {
                    Log.e("API Response", "Error: " + throwable.getMessage());

                }  });
            // Tạo và hiển thị dialog
            AlertDialog dialog = builder.create();
            dialog.show();

            // Thiết lập hành động cho nút "Lưu"
            btnLuu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    EditText edtnhom = dialogView.findViewById(R.id.edtNhom);
                    EditText edtsoSVTT = dialogView.findViewById(R.id.edtSoSVTT);
                    EditText edtsoSVTD = dialogView.findViewById(R.id.edtSoSVTD);
                    int nhom = Integer.parseInt( edtnhom.getText().toString());
                    int soSVTT =  Integer.parseInt( edtsoSVTT.getText().toString());
                    int soSVTD =  Integer.parseInt( edtsoSVTD.getText().toString());
                    for (int i = 0; i< DSLTC.size();i++)
                    {
                        if((DSLTC.get(i).getMakhoa().equals(makhoa)&&DSLTC.get(i).getNienkhoa().equals(nienkhoa)&&
                                DSLTC.get(i).getHocki()==hocki&&DSLTC.get(i).getNhom()==nhom)
                        )
                        {
                            Toast.makeText(QuanTriLTC.this, "Môn học có nhóm này đã tồn tại!", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                    LopTinChiDTO ltcNew = new LopTinChiDTO(null,makhoa,nienkhoa,hocki,nhom,soSVTT,soSVTD,
                            spLop.getSelectedItem().toString(),false,mamhtemp,magvtemp,tenMHtemp,hotengv);
                    iQuanTriThongTin.themloptinchi(ltcNew).enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if (response.isSuccessful()) {
                                DSLTC.add(ltcNew);

                                // Cập nhật giao diện ListView
                                adapterLTC.notifyDataSetChanged();
                                Toast.makeText(getApplicationContext(), "Thêm thành công!", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            } else {
                                Toast.makeText(getApplicationContext(), "Thêm không thành công!", Toast.LENGTH_SHORT).show();

                            }
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            // Xử lý khi có lỗi xảy ra
                        }
                    });
                }
            });

            // Thiết lập hành động cho nút "Hủy"
            btnHuy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Xử lý khi nhấn nút "Hủy"
                    // Ví dụ: Đóng dialog
                    dialog.dismiss();
                }
            });


        });
    }
    private void filterLopTinChi() {
        makhoa = (String) spKhoa.getSelectedItem();
        nienkhoa = (String) spNienKhoa.getSelectedItem();
        hocki = (Integer) spHocKi.getSelectedItem();

        iQuanTriThongTin.locLTC(makhoa, nienkhoa, hocki).enqueue(new Callback<List<LopTinChiDTO>>() {
            @Override
            public void onResponse(Call<List<LopTinChiDTO>> call, Response<List<LopTinChiDTO>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    DSLTC.clear();
                    DSLTC.addAll(response.body());
                    adapterLTC.notifyDataSetChanged();
                } else {
                    Log.e("API Response", "Error: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<LopTinChiDTO>> call, Throwable throwable) {
                Log.e("API Response", "Error: " + throwable.getMessage());
            }
        });
    }
    private void setControl() {
        accountSharedPref = getSharedPreferences("Account", Context.MODE_PRIVATE);
        String token = accountSharedPref.getString("token", "");
        Retrofit retrofit = ApiClient.getClient(token);
        iQuanTriThongTin = retrofit.create(IQuanTriThongTin.class);
        // Ánh xạ các thành phần giao diện
        imgBtnBack = findViewById(R.id.imgBtnBack);
        imgBtnLogout = findViewById(R.id.imgBtnLogout);
        btnAddLTC = findViewById(R.id.imgBtnThemLTC);

        tvVeNhaTruong = findViewById(R.id.tvVeNhaTruong);
        tvXemThongTinCaNhan = findViewById(R.id.tvXemThongTinCaNhan);
        lvLopTC = findViewById(R.id.lvLopTC);

        spKhoa = findViewById(R.id.spKhoa);
        spNienKhoa = findViewById(R.id.spNienKhoaLTC);
        spHocKi = findViewById(R.id.spHocKi);
        adapterLTC = new AdapterLTC(QuanTriLTC.this, R.layout.layout_item_ltc, DSLTC, iQuanTriThongTin);
        lvLopTC.setAdapter(adapterLTC);
        ArrayAdapter adapter_spHocki = new ArrayAdapter(QuanTriLTC.this, R.layout.layout_spinner, dshk);
        spHocKi.setAdapter(adapter_spHocki);
        iQuanTriThongTin.locMaKhoaLTC().enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                if (response.isSuccessful() && response.body() != null) {

                    if(response.code() == 200){
                        DSK.addAll(response.body());
                        ArrayAdapter adapter_spKhoa= new ArrayAdapter(QuanTriLTC.this, R.layout.layout_spinner, DSK);
                        spKhoa.setAdapter(adapter_spKhoa);


                    }
                } else {
                    Log.e("API Response", "Error: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable throwable) {
                Log.e("API Response", "Error: " + throwable.getMessage());

            }  });
        iQuanTriThongTin.locNienKhoaLTC().enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                if (response.isSuccessful() && response.body() != null) {

                    if(response.code() == 200){
                        DSNienKhoa.addAll(response.body());
                        ArrayAdapter adapter_spNK= new ArrayAdapter(QuanTriLTC.this, R.layout.layout_spinner, DSNienKhoa);
                        spNienKhoa.setAdapter(adapter_spNK);


                    }
                } else {
                    Log.e("API Response", "Error: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable throwable) {
                Log.e("API Response", "Error: " + throwable.getMessage());

            }  });
    }

}
