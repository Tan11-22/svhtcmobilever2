package com.example.svhtcmobile.Controller;

import static com.example.svhtcmobile.Controller.Function.layHocKyHienTai;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.svhtcmobile.Adapter.CustomDKLTCAdapter;
import com.example.svhtcmobile.Adapter.CustomDaDangKyAdapter;
import com.example.svhtcmobile.Api.ApiClient;
import com.example.svhtcmobile.Api.apiService.IDangKyLTCService;
import com.example.svhtcmobile.Api.apiService.ISinhVien;
import com.example.svhtcmobile.Model.ApiResponse;
import com.example.svhtcmobile.Model.DDKLTC;
import com.example.svhtcmobile.Model.DKLTC;
import com.example.svhtcmobile.Model.LTCDTO;
import com.example.svhtcmobile.Model.SinhVien;
import com.example.svhtcmobile.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class DKLopTinChiMain extends AppCompatActivity {
    private TextView tvTTHoTen, tvTTMSSV, tvTTMaLop;
    private ImageButton imgBtnBack, imgBtnLogout;
    private Spinner spMaLop, spTenMH;
    private ListView lvDKLTC, lvDDKLTC;
    private String maLop, maSV, hoTen;
    private String nienKhoa;
    private int hocKy;
    private List<LTCDTO> data= new ArrayList<>();
    private List<LTCDTO> all_data= new ArrayList<>();
    private List<LTCDTO> dataDDK=new ArrayList<>();
    private List<String> dsDDKMM=new ArrayList<>();
    private List<Integer> dsDDKMLTC=new ArrayList<>();
    private List<String> dsDKTenMH=new ArrayList<>();
    private List<String> dsDKMaLop=new ArrayList<>();
    private ArrayAdapter adapter_dsDKTenMH, adapter_dsDKMaLop;
    CustomDKLTCAdapter adapter_dkltc;
    CustomDaDangKyAdapter adapter_ddkltc;

    SharedPreferences accountSharedPref;
    ISinhVien iSinhVien;

    IDangKyLTCService iDangKyLTCService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dklop_tin_chi_main);
        init();
        setEvent();
    }



    public void init(){
        tvTTHoTen=findViewById(R.id.tvTTHoTen);
        tvTTMSSV=findViewById(R.id.tvTTMSSV);
        tvTTMaLop=findViewById(R.id.tvTTMaLop);
        lvDKLTC=findViewById(R.id.lvDKLTC);
        lvDDKLTC=findViewById(R.id.lvDDKLTC);
        imgBtnBack=findViewById(R.id.imgBtnBack);
        imgBtnLogout=findViewById(R.id.imgBtnLogout);
        spMaLop=findViewById(R.id.spMaLop);
//        spTenMH=findViewById(R.id.spTenMH);

        accountSharedPref = getSharedPreferences("Account", Context.MODE_PRIVATE);
        String token = accountSharedPref.getString("token", "");
        String ho = accountSharedPref.getString("ho", "");
        String ten = accountSharedPref.getString("ten", "");
        String username = accountSharedPref.getString("username", "");
        String quyen = accountSharedPref.getString("quyen", "");
        Retrofit retrofit = ApiClient.getClient(token);
        iSinhVien = retrofit.create(ISinhVien.class);
        iDangKyLTCService = retrofit.create(IDangKyLTCService.class);
        //Set thông tin
        maSV=username;
        hoTen=ho+" "+ten;
//        DocSV();
        tvTTHoTen.setText(hoTen);
        tvTTMSSV.setText(maSV);

        //Set nienKhoa va hocKy
        Map<String,Object> thongTin=layHocKyHienTai();
        nienKhoa= (String)thongTin.get("nienKhoa");
        hocKy= (Integer)thongTin.get("hocKy");

        adapter_ddkltc=new CustomDaDangKyAdapter(this,R.layout.layout_item_ddkltc,dataDDK);
        lvDDKLTC.setAdapter(adapter_ddkltc);
        adapter_dkltc=new CustomDKLTCAdapter(this,R.layout.layout_item_dkltc,data);
        lvDKLTC.setAdapter(adapter_dkltc);
        adapter_dsDKMaLop=new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, dsDKMaLop);
        spMaLop.setAdapter(adapter_dsDKMaLop);
//        adapter_dsDKTenMH=new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,dsDKTenMH);
//        spTenMH.setAdapter(adapter_dsDKTenMH);
        iDangKyLTCService.getDanhSachThongTinLop().enqueue(new Callback<ApiResponse<List<Map<String, Object>>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Map<String, Object>>>> call, Response<ApiResponse<List<Map<String, Object>>>> response) {
                ApiResponse<List<Map<String, Object>>> apiResponse = response.body();
                List<Map<String, Object>> data  = apiResponse.getData();
                for (Map<String, Object> lp : data) {
                    String maLop = (String) lp.get("MALOP");
                    Log.d("LOG=MALOP : ", maLop);
                    dsDKMaLop.add(maLop);
                }
                adapter_dsDKMaLop.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Map<String, Object>>>> call, Throwable throwable) {

            }
        });
        loadDSLTCDaDK();
    }


    public void setEvent(){

        spMaLop.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                maLop = spMaLop.getSelectedItem().toString();
                loadDSLTCDeDk(maLop);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        lvDKLTC.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long l) {
                LTCDTO x= data.get(i);
                Log.d("LOG=DKMH", x.getMaMH());
                dangKyLTC(x.getMaLTC(),x.getSoSVToiDa());

            }
        });
        lvDDKLTC.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long l) {
                LTCDTO x= dataDDK.get(i);
                Log.d("LOG=DKMH", x.getMaMH());
                huyDangKyLTC(x.getMaLTC());
            }
        });
        imgBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        imgBtnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            DocDLDDK();
//            DocDLDK(0,null);
//        }
//    }

    private void loadDSLTCDeDk(String maLop) {
        iDangKyLTCService.getDSLTCDeDK(maLop,maSV).enqueue(new Callback<ApiResponse<List<LTCDTO>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<LTCDTO>>> call, Response<ApiResponse<List<LTCDTO>>> response) {
                if (response.code() == 200) {
                    ApiResponse<List<LTCDTO>> apiResponse = response.body();
                    List<LTCDTO> ltcdtos = apiResponse.getData();
                    data.clear();
                    data.addAll(ltcdtos);
                }
                adapter_dkltc.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<ApiResponse<List<LTCDTO>>> call, Throwable throwable) {

            }
        });
    }

    private void loadDSLTCDaDK() {
        iDangKyLTCService.getDSLTCDaDK(maSV).enqueue(new Callback<ApiResponse<List<LTCDTO>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<LTCDTO>>> call, Response<ApiResponse<List<LTCDTO>>> response) {
                ApiResponse<List<LTCDTO>> apiResponse = response.body();
                List<LTCDTO> data123 = apiResponse.getData();
                dataDDK.clear();
                dataDDK.addAll(data123);
                adapter_ddkltc.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<ApiResponse<List<LTCDTO>>> call, Throwable throwable) {

            }
        });
    }

    private void dangKyLTC(int maltc, int soSVToiDa) {
        Map<String,Object> data = new HashMap<>();
        data.put("maltc",maltc);
        data.put("masv", maSV);
        data.put("svtoida",soSVToiDa);
        iDangKyLTCService.dangKyLTC(data).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                ApiResponse apiResponse = response.body();
                if (apiResponse.getCode() == 202) {
                    Toast.makeText(DKLopTinChiMain.this,"Đăng ký thất bại", Toast.LENGTH_SHORT).show();
                } else if(apiResponse.getCode() == 300) {
                    Toast.makeText(DKLopTinChiMain.this,"Lỗi lưu service", Toast.LENGTH_SHORT).show();
                } else {
                    loadDSLTCDeDk(maLop);
                    loadDSLTCDaDK();
                    Toast.makeText(DKLopTinChiMain.this,"Đăng kí thành công", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable throwable) {

            }
        });
    }

    private void huyDangKyLTC(int maltc) {
        Map<String,Object> data = new HashMap<>();
        data.put("maltc",maltc);
        data.put("masv", maSV);
        iDangKyLTCService.huyDangKyLTC(data).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                ApiResponse apiResponse = response.body();
                if(apiResponse.getCode() == 300) {
                    Toast.makeText(DKLopTinChiMain.this,"Lỗi lưu service", Toast.LENGTH_SHORT).show();
                } else {
                    loadDSLTCDeDk(maLop);
                    loadDSLTCDaDK();
                    Toast.makeText(DKLopTinChiMain.this,"Huỷ đăng kí thành công", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable throwable) {

            }
        });
    }
}