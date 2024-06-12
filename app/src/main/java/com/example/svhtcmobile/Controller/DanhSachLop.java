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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import com.example.svhtcmobile.Adapter.CustomAdapterLop;
import com.example.svhtcmobile.Api.ApiClient;
import com.example.svhtcmobile.Api.apiService.ILopService;
import com.example.svhtcmobile.Api.apiService.IQuanTriService;
import com.example.svhtcmobile.Model.ApiResponse;
import com.example.svhtcmobile.Model.He;
import com.example.svhtcmobile.Model.LopDTO;
import com.example.svhtcmobile.Model.LopDTO1;
import com.example.svhtcmobile.Model.TTGiangVienAPI;
import com.example.svhtcmobile.R;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class DanhSachLop extends AppCompatActivity {
    private List<String> trangThai = new ArrayList<>();
    private Spinner spTrangThai;
    private ArrayAdapter adapter_spTrangThai;
    private  List<LopDTO1> data = new ArrayList<>();

    private  List<LopDTO1> data_all = new ArrayList<>();
    private CustomAdapterLop adapterLop;
    private ListView lvLop;
    private ImageButton imgBtnThemLop, imgBtnBack;
    private String maKhoa, maGV;
    private SearchView searchViewLop;
    private TextView tvTenGiangVien, tvTenKhoa, tvTenKhoa1;
    private SharedPreferences sharedPreferences;
    private IQuanTriService iQuanTriService;

    private ILopService iLopService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danh_sach_lop);
        Init();

        setEvent();
    }


    private void setEvent() {
        imgBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        imgBtnThemLop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog(maKhoa);
            }
        });

        searchViewLop.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                data.clear();
                for (LopDTO1 lop: data_all) {
                    if(lop.getMaLop().toLowerCase().contains(newText.toLowerCase())) {
                        data.add(lop);
                    }
                }
                adapterLop.notifyDataSetChanged();
                return false;
            }
        });
    }

    private void openDialog(String maKhoa){
        final Dialog dialog = new Dialog(DanhSachLop.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_dialog_them_lop);

        Window window = dialog.getWindow();
        if(window == null) return;

        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAtrributes = window.getAttributes();
        windowAtrributes.gravity = Gravity.CENTER;
        window.setAttributes(windowAtrributes);

        Button btnHuy = dialog.findViewById(R.id.btnHuy);
        Button btnOk = dialog.findViewById(R.id.btnOk);
        EditText edtMaLop = dialog.findViewById(R.id.edtMaLop);
        EditText edtTenLop = dialog.findViewById(R.id.edtTenLop);
        EditText edtNamBatDau = dialog.findViewById(R.id.edtNamBatDau);
        EditText edtNamKetThuc = dialog.findViewById(R.id.edtNamKetThuc);
        Spinner spHeDaoTao = dialog.findViewById(R.id.spHeDaoTao);
        Spinner spKhoa = dialog.findViewById(R.id.spKhoa);
        List<He> danhSachHeDaoTao = new ArrayList<>();
        List<String> danhSachKhoa = new ArrayList<>();

        iLopService.danhSachHe().enqueue(new Callback<ApiResponse<List<JsonObject>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<JsonObject>>> call, Response<ApiResponse<List<JsonObject>>> response) {
                ApiResponse<List<JsonObject>> apiResponse = response.body();
                if (apiResponse.getCode() == 200) {
                    List<JsonObject> hes = apiResponse.getData();
                    for (JsonObject he : hes) {
                        He he1 = new He(he.get("ID_HE").getAsInt(), he.get("TEN_HE").getAsString());
                        danhSachHeDaoTao.add(he1);
                        ArrayAdapter adapter_spHeDaoTao = new ArrayAdapter(dialog.getContext(), R.layout.layout_spinner, danhSachHeDaoTao);
                        spHeDaoTao.setAdapter(adapter_spHeDaoTao);
                    }
                }else {
                    Toast.makeText(DanhSachLop.this,"Không thể tải dữ liệu!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<JsonObject>>> call, Throwable throwable) {

            }
        });
        iLopService.danhSachKhoa().enqueue(new Callback<ApiResponse<List<JsonObject>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<JsonObject>>> call, Response<ApiResponse<List<JsonObject>>> response) {
                ApiResponse<List<JsonObject>> apiResponse = response.body();
                if (apiResponse.getCode() == 200) {
                   List<JsonObject> khoas = apiResponse.getData();
                   for(JsonObject khoa : khoas) {
                       String khoa1 = khoa.get("makhoa").getAsString().trim() + "-"+khoa.get("tenkhoa").getAsString();
                       danhSachKhoa.add(khoa1);
                       ArrayAdapter adapter_spKhoa = new ArrayAdapter(dialog.getContext(), R.layout.layout_spinner, danhSachKhoa);
                       spKhoa.setAdapter(adapter_spKhoa);
                   }
                }else {
                    Toast.makeText(DanhSachLop.this,"Không thể tải dữ liệu!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<JsonObject>>> call, Throwable throwable) {

            }
        });
//

        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String, Object> dataRequset = new HashMap<>();
                dataRequset.put("maLop", edtMaLop.getText().toString().trim());
                dataRequset.put("tenLop",edtTenLop.getText().toString().trim());
                dataRequset.put("khoaHoc1",edtNamBatDau.getText().toString().trim() );
                dataRequset.put("khoaHoc2", edtNamKetThuc.getText().toString().trim());
                String maKhoa = spKhoa.getSelectedItem().toString().split("-")[0];
                dataRequset.put("maKhoa", maKhoa);
                String tmp = spHeDaoTao.getSelectedItem().toString();
                for(He he : danhSachHeDaoTao){
                    if(tmp.equals(he.getTenHe())){
                        dataRequset.put("idHe", he.getId());
                    }
                }
                iLopService.themLopHocMoi(dataRequset).enqueue(new Callback<ApiResponse>() {
                    @Override
                    public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                        ApiResponse apiResponse = response.body();
                        Toast.makeText(DanhSachLop.this,apiResponse.getStatus(),Toast.LENGTH_SHORT).show();
                        if (apiResponse.getCode() == 200) {
                            loadData();
                            dialog.dismiss();
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse> call, Throwable throwable) {

                    }
                });
                
            }
        });

        dialog.setCancelable(true);
        dialog.show();
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
        iLopService = retrofit.create(ILopService.class);
        maGV = username;

//        spTrangThai = findViewById(R.id.spTrangThai);
        lvLop = findViewById(R.id.lvLop);
        imgBtnThemLop = findViewById(R.id.imgBtnThemLop);
        searchViewLop = findViewById(R.id.searchViewLop);
        tvTenGiangVien = findViewById(R.id.tvTenGiangVien);
        tvTenKhoa = findViewById(R.id.tvTenKhoa);
//        tvTenKhoa1 = findViewById(R.id.tvTenKhoa1);
        imgBtnBack = findViewById(R.id.imgBtnBack);
        adapterLop = new CustomAdapterLop(this, R.layout.layout_item_lop, data);
        lvLop.setAdapter(adapterLop);
        loadData();
    }

    public void loadData() {
        iLopService.danhSachLopHoc(0,10000).enqueue(new Callback<ApiResponse<List<LopDTO1>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<LopDTO1>>> call, Response<ApiResponse<List<LopDTO1>>> response) {
                ApiResponse<List<LopDTO1>> apiResponse = response.body();
                if (apiResponse.getCode() == 200) {
                    List<LopDTO1> lops = apiResponse.getData();
                    data.clear();
                    data_all.clear();
                    data.addAll(lops);
                    data_all.addAll(lops);
                    adapterLop.notifyDataSetChanged();
                }
                else {
                    Toast.makeText(DanhSachLop.this,"Load data lớp thất bại!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<LopDTO1>>> call, Throwable throwable) {

            }
        });
    }
}