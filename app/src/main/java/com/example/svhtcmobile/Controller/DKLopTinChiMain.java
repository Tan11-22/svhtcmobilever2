package com.example.svhtcmobile.Controller;

import static com.example.svhtcmobile.Controller.Function.layHocKyHienTai;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
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
import com.example.svhtcmobile.Api.apiService.ISinhVien;
import com.example.svhtcmobile.Model.DDKLTC;
import com.example.svhtcmobile.Model.DKLTC;
import com.example.svhtcmobile.Model.SinhVien;
import com.example.svhtcmobile.R;

import java.util.ArrayList;
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
    private List<DKLTC> data= new ArrayList<>();
    private List<DKLTC> all_data= new ArrayList<>();
    private List<DDKLTC> dataDDK=new ArrayList<>();
    private List<String> dsDDKMM=new ArrayList<>();
    private List<Integer> dsDDKMLTC=new ArrayList<>();
    private List<String> dsDKTenMH=new ArrayList<>();
    private List<String> dsDKMaLop=new ArrayList<>();
    private ArrayAdapter adapter_dsDKTenMH, adapter_dsDKMaLop;
    CustomDKLTCAdapter adapter_dkltc;
    CustomDaDangKyAdapter adapter_ddkltc;

    SharedPreferences accountSharedPref;
    ISinhVien iSinhVien;
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
        spTenMH=findViewById(R.id.spTenMH);

        accountSharedPref = getSharedPreferences("Account", Context.MODE_PRIVATE);
        String token = accountSharedPref.getString("token", "");
        String ho = accountSharedPref.getString("ho", "");
        String ten = accountSharedPref.getString("ten", "");
        String username = accountSharedPref.getString("username", "");
        String quyen = accountSharedPref.getString("quyen", "");
        Retrofit retrofit = ApiClient.getClient(token);
        iSinhVien = retrofit.create(ISinhVien.class);
        //Set thông tin
        maSV=username;
        hoTen=ho+" "+ten;
        DocSV();
        tvTTHoTen.setText(hoTen);
        tvTTMSSV.setText(maSV);

        //Set nienKhoa va hocKy
        Map<String,Object> thongTin=layHocKyHienTai();
        nienKhoa= (String)thongTin.get("nienKhoa");
        hocKy= (Integer)thongTin.get("hocKy");
    }
    public void DocSV(){
        iSinhVien.timSV(maSV).enqueue(new Callback<SinhVien>() {
            @Override
            public void onResponse(Call<SinhVien> call, Response<SinhVien> response) {
                SinhVien sinhVien=response.body();
                maLop=sinhVien.getMalop();
                tvTTMaLop.setText(maLop);

            }

            @Override
            public void onFailure(Call<SinhVien> call, Throwable throwable) {
                System.out.println(throwable.getMessage().toString());
                Toast.makeText(DKLopTinChiMain.this, throwable.getMessage().toString(),Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void apHocPhi(){
        iSinhVien.apHocPhi(maSV,nienKhoa,hocKy).enqueue(new Callback<Map<String, String>>() {
            @Override
            public void onResponse(Call<Map<String, String>> call, Response<Map<String, String>> response) {
                Map<String,String> result=response.body();
                String message = result.get("message");
                Toast.makeText(DKLopTinChiMain.this,message,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Map<String, String>> call, Throwable t) {
                Toast.makeText(DKLopTinChiMain.this,"Lỗi: "+t.getMessage().toString(),Toast.LENGTH_SHORT).show();
                System.out.println("Lỗi: "+t.getMessage().toString());
            }
        });
    }
    public void setEvent(){
        adapter_ddkltc=new CustomDaDangKyAdapter(this,R.layout.layout_item_ddkltc,dataDDK);
        lvDDKLTC.setAdapter(adapter_ddkltc);
        adapter_dkltc=new CustomDKLTCAdapter(this,R.layout.layout_item_dkltc,data,dsDDKMM,dsDDKMLTC);
        lvDKLTC.setAdapter(adapter_dkltc);
        adapter_dsDKMaLop=new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, dsDKMaLop);
        spMaLop.setAdapter(adapter_dsDKMaLop);
        adapter_dsDKTenMH=new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,dsDKTenMH);
        spTenMH.setAdapter(adapter_dsDKTenMH);
        spMaLop.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                List<DKLTC> temp=new ArrayList<>();
                if(spMaLop.getSelectedItem().equals("<DEFAULT>")){
                    DocDL();
                }
                else if(spMaLop.getSelectedItem().equals("<ALL>")){
                    DocDLDK(0,null);
                }
                else if(spMaLop.getSelectedItem().equals("<NONE>")){
                    DocDLDK(1,null);
                }
                else{
                    String maLopFilter = spMaLop.getSelectedItem().toString();
                    DocDLDK(1,maLopFilter);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spTenMH.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(spTenMH.getSelectedItem().equals("<DEFAULT>")){
                    data.clear();
                    data.addAll(all_data);
                    adapter_dkltc.notifyDataSetChanged();
                }
                else if(spTenMH.getSelectedItem().equals("<NONE>")){
                    data.clear();
                    for(DKLTC x: all_data){
                        if(x.getTenMH()==null){
                            data.add(x);
                        }
                    }
                    adapter_dkltc.notifyDataSetChanged();
                }
                else{
                    data.clear();
                    String tenMHFilter = spTenMH.getSelectedItem().toString();
                    for(DKLTC x: all_data){
                        if(x.getTenMH()==null){
                            continue;
                        }
                        if(x.getTenMH().equals(tenMHFilter)){
                            data.add(x);
                        }
                    }
                    adapter_dkltc.notifyDataSetChanged();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        lvDKLTC.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long l) {
                DKLTC x= data.get(i);
                System.out.println(String.valueOf(x.getMaLTC()));
                System.out.println(maSV);
                iSinhVien.dangKy(maSV,x.getMaLTC()).enqueue(new Callback<Map<String, String>>() {
                    @Override
                    public void onResponse(Call<Map<String, String>> call, Response<Map<String, String>> response) {
                        Map<String,String> result=response.body();
                        String status = result.get("status");
                        String message = result.get("message");
                        if(status.equals("1")){
                            Toast.makeText(DKLopTinChiMain.this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                            DocDLDDK();
                            apHocPhi();
                        }
                        else{
                            Toast.makeText(DKLopTinChiMain.this, message, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Map<String, String>> call, Throwable t) {

                    }
                });
            }
        });
        lvDDKLTC.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long l) {
                DDKLTC x= dataDDK.get(i);
                System.out.println(String.valueOf(x.getMaLTC()));
                System.out.println(maSV);
                iSinhVien.boDangKy(maSV,x.getMaLTC()).enqueue(new Callback<Map<String, String>>() {
                    @Override
                    public void onResponse(Call<Map<String, String>> call, Response<Map<String, String>> response) {
                        Map<String,String> result=response.body();
                        String status = result.get("status");
                        String message = result.get("message");
                        if(status.equals("1")){
                            Toast.makeText(DKLopTinChiMain.this, "Hủy đăng ký thành công", Toast.LENGTH_SHORT).show();
                            DocDLDDK();
                            apHocPhi();
                        }
                        else{
                            Toast.makeText(DKLopTinChiMain.this, message, Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<Map<String, String>> call, Throwable t) {

                    }
                } );
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
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void DocDLDDK(){
        iSinhVien.getDSDDKLTC(maSV,nienKhoa,hocKy).enqueue(new Callback<List<DDKLTC>>() {
            @Override
            public void onResponse(Call<List<DDKLTC>> call, Response<List<DDKLTC>> response) {
                dataDDK.clear();
                dsDDKMLTC.clear();
                dsDDKMM.clear();
                for(DDKLTC x: response.body()){
                    dataDDK.add(x);
                    if(!dsDDKMLTC.contains(x.getMaLTC())){
                        dsDDKMLTC.add(x.getMaLTC());
                    }
                    if(!dsDDKMM.contains(x.getMaMH())){
                        dsDDKMM.add(x.getMaMH());
                    }
                }
                adapter_ddkltc.notifyDataSetChanged();
                System.out.println("Call api dkltc success");
//                Toast.makeText(DKLopTinChiMain.this,"Call api ddkltc success",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<List<DDKLTC>> call, Throwable t) {
                Toast.makeText(DKLopTinChiMain.this,"Call api ddkltc fail",Toast.LENGTH_SHORT).show();
            }
        });
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void DocDLDK(int mode,String maLopKT){
        iSinhVien.getDSDangKy(nienKhoa,hocKy).enqueue(new Callback<List<DKLTC>>() {
            @Override
            public void onResponse(Call<List<DKLTC>> call, Response<List<DKLTC>> response) {
                String temp;
                data.clear();
                all_data.clear();
                dsDKMaLop.clear();
                dsDKTenMH.clear();
                dsDKMaLop.add("<DEFAULT>");
                dsDKTenMH.add("<DEFAULT>");
                dsDKMaLop.add("<ALL>");
                System.out.println("Call api dkltc success");
//                Toast.makeText(DKLopTinChiMain.this,"Call api dkltc success",Toast.LENGTH_SHORT).show();
                for (DKLTC x: response.body()){
                    temp=x.getMaLop();
                    if(temp==null){
                        temp="<NONE>";
                    }
                    if(!dsDKMaLop.contains(temp)){
                        dsDKMaLop.add(temp);
                    }
                    if(mode!=0){
                        if(maLopKT==null){
                            if(x.getMaLop()!=null){
                                continue;
                            }
                        }
                        else{
                            if(x.getMaLop()==null||!x.getMaLop().equals(maLopKT)){
                                continue;
                            }
                        }
                    }
                    data.add(x);
                    all_data.add(x);

                    temp=x.getTenMH();
                    if(temp==null){
                        temp="<NONE>";
                    }
                    if(!dsDKTenMH.contains(temp)){
                        dsDKTenMH.add(temp);
                    }
                }
                adapter_dkltc.notifyDataSetChanged();
                adapter_dsDKTenMH.notifyDataSetChanged();
                adapter_dsDKMaLop.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<DKLTC>> call, Throwable t) {
                Toast.makeText(DKLopTinChiMain.this,"Call api dkltc fail",Toast.LENGTH_SHORT).show();
            }
        });
    }
    public List<DKLTC> sapXepUuTien(List<DKLTC> list){
        List<String> dsMon=new ArrayList<>();
        List<DKLTC> dsLuu=new ArrayList<>();
        for(DKLTC x: list){
            if(x.getMaLop()!=null&&x.getMaLop().equals(maLop)){
                dsLuu.add(x);
                if(!dsMon.contains(x.getMaMH()))dsMon.add(x.getMaMH());
            }
        }
        for(DKLTC x: list){
            if(!dsMon.contains(x.getMaMH())){
                dsLuu.add(x);
            }
        }
        dsMon.clear();
        list.clear();
        list.addAll(dsLuu);
        return list;
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void DocDL(){
        iSinhVien.getDSLopSVDangKy(maLop,nienKhoa,hocKy).enqueue(new Callback<List<DKLTC>>() {
            @Override
            public void onResponse(Call<List<DKLTC>> call, Response<List<DKLTC>> response) {
                String temp;
                data.clear();
                all_data.clear();
                dsDKTenMH.clear();
                dsDKTenMH.add("<DEFAULT>");
                System.out.println("Call api dkltc success");
//                Toast.makeText(DKLopTinChiMain.this,"Call api dkltc success",Toast.LENGTH_SHORT).show();
                for (DKLTC x: response.body()){
                    data.add(x);
                    temp=x.getTenMH();
                    if(temp==null){
                        temp="<NONE>";
                    }
                    if(!dsDKTenMH.contains(temp)){
                        dsDKTenMH.add(temp);
                    }
                }
                data=sapXepUuTien(data);
                all_data.addAll(data);
                adapter_dkltc.notifyDataSetChanged();
                adapter_dsDKTenMH.notifyDataSetChanged();

            }

            @Override
            public void onFailure(Call<List<DKLTC>> call, Throwable t) {
                Toast.makeText(DKLopTinChiMain.this,"Call api dkltc fail",Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            DocDLDDK();
            DocDLDK(0,null);
        }
    }
}