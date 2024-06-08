package com.example.svhtcmobile.Controller;

import static com.example.svhtcmobile.Controller.Function.layHocKyHienTai;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.svhtcmobile.Adapter.CustomHocPhiHocKyAdapter;
import com.example.svhtcmobile.Adapter.CustomHocPhiSinhVienAdapter;
import com.example.svhtcmobile.Api.ApiClient;
import com.example.svhtcmobile.Api.apiService.ISinhVien;
import com.example.svhtcmobile.Model.HocPhiHocKy;
import com.example.svhtcmobile.Model.HocPhiSinhVien;
import com.example.svhtcmobile.Model.SinhVien;
import com.example.svhtcmobile.R;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class HocPhiSinhVienMain extends AppCompatActivity {
    private TextView tvTTHoTen, tvTTMSSV, tvTTMaLop;
    private TextView tvTongTien;
    private ImageButton imgBtnBack;
    private Spinner spNienKhoa, spHocKy;
    private ListView lvHP, lvHPSV;
    private List<HocPhiHocKy> data = new ArrayList<>();

    private List<HocPhiSinhVien> dataSV= new ArrayList<>();
    private List<String> dsNienKhoa=new ArrayList<>();
    private List<Integer> dsHocKy = new ArrayList<>();
    CustomHocPhiHocKyAdapter adapter_hp;
    CustomHocPhiSinhVienAdapter adapter_hpsv;
    ArrayAdapter adapter_dsNienKhoa, adapter_dsHocKy;
    private int index;
    private String maSV, maLop, hoTen;
    private String nienKhoa;
    private int hocKy;

    SharedPreferences accountSharedPref;
    ISinhVien iSinhVien;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hoc_phi_sinh_vien);
        init();
        setEvent();
    }

    public void init(){
        tvTTHoTen=findViewById(R.id.tvTTHoTen);
        tvTTMSSV=findViewById(R.id.tvTTMSSV);
        tvTTMaLop=findViewById(R.id.tvTTMaLop);
        tvTongTien=findViewById(R.id.tvTongTien);
        spNienKhoa=findViewById(R.id.spNienKhoa);
        spHocKy=findViewById(R.id.spHocKy);
        lvHP=findViewById(R.id.lvHP);
        lvHPSV=findViewById(R.id.lvHPSV);
        imgBtnBack=findViewById(R.id.imgBtnBack);
        maSV=tvTTMSSV.getText().toString();


        accountSharedPref = getSharedPreferences("Account", Context.MODE_PRIVATE);
        String token = accountSharedPref.getString("token", "");
        String ho = accountSharedPref.getString("ho", "");
        String ten = accountSharedPref.getString("ten", "");
        String username = accountSharedPref.getString("username", "");
        String quyen = accountSharedPref.getString("quyen", "");
        Retrofit retrofit = ApiClient.getClient(token);
        iSinhVien = retrofit.create(ISinhVien.class);

        //Set thông tin niên khóa học kỳ
        Map<String,Object> thongTin=layHocKyHienTai();
        nienKhoa= (String)thongTin.get("nienKhoa");
        hocKy= (Integer)thongTin.get("hocKy");
        //Set thông tin sinh viên
        maSV=username;
        hoTen=ho+" "+ten;
        DocSV();
        tvTTHoTen.setText(hoTen);
        tvTTMSSV.setText(maSV);
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
                Toast.makeText(HocPhiSinhVienMain.this, throwable.getMessage().toString(),Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void DocDL(){
        iSinhVien.getDSHocPhiHocKy(maSV,nienKhoa,hocKy).enqueue(new Callback<List<HocPhiHocKy>>() {
            @Override
            public void onResponse(Call<List<HocPhiHocKy>> call, retrofit2.Response<List<HocPhiHocKy>> response) {
                data.clear();
//                Toast.makeText(HocPhiSinhVienMain.this,"Call api hphk success",Toast.LENGTH_SHORT).show();
                int tongTienHocPhi=0;
                for (HocPhiHocKy x: response.body()){
                    data.add(x);
                    tongTienHocPhi = tongTienHocPhi + x.getTien();
                }
                adapter_hp.notifyDataSetChanged();
                tvTongTien.setText(String.valueOf(tongTienHocPhi)+" VND");
            }

            @Override
            public void onFailure(Call<List<HocPhiHocKy>> call, Throwable t) {
                Toast.makeText(HocPhiSinhVienMain.this,"Call api hphk fail",Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void DocDLHPSV(){
        iSinhVien.getDSHocPhiSinhVien(maSV).enqueue(new Callback<List<HocPhiSinhVien>>() {
            @Override
            public void onResponse(Call<List<HocPhiSinhVien>> call, retrofit2.Response<List<HocPhiSinhVien>> response) {
                dataSV.clear();
                dsHocKy.clear();
                dsNienKhoa.clear();
                dsNienKhoa.add("<DEFAULT>");
//                Toast.makeText(HocPhiSinhVienMain.this,"Call api hpsv success",Toast.LENGTH_SHORT).show();
                for (HocPhiSinhVien x: response.body()){
                    dataSV.add(x);
                    if(!dsNienKhoa.contains(x.getNienKhoa())){
                        dsNienKhoa.add(x.getNienKhoa());
                    }
                    if(!dsHocKy.contains(x.getHocKy())){
                        dsHocKy.add(x.getHocKy());
                    }
                }
                adapter_hpsv.notifyDataSetChanged();
                adapter_dsHocKy.notifyDataSetChanged();
                adapter_dsNienKhoa.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<HocPhiSinhVien>> call, Throwable t) {
                Toast.makeText(HocPhiSinhVienMain.this,"Call api hpsv fail",Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        DocDL();
        DocDLHPSV();
    }


    public void setEvent(){

        adapter_hp= new CustomHocPhiHocKyAdapter(this,R.layout.layout_item_hocphihocky,data);
        adapter_hpsv=new CustomHocPhiSinhVienAdapter(this,R.layout.layout_item_hocphisinhvien,dataSV);
        adapter_dsNienKhoa=new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, dsNienKhoa);
        spNienKhoa.setAdapter(adapter_dsNienKhoa);
        adapter_dsHocKy=new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, dsHocKy);
        spHocKy.setAdapter(adapter_dsHocKy);
        lvHP.setAdapter(adapter_hp);
        lvHPSV.setAdapter(adapter_hpsv);
        spNienKhoa.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(spNienKhoa.getSelectedItem().equals("<DEFAULT>")){
                    Map<String,Object> thongTin=layHocKyHienTai();
                    nienKhoa= (String)thongTin.get("nienKhoa");
                    hocKy= (Integer)thongTin.get("hocKy");
                    DocDL();
                }
                else{
                    nienKhoa=spNienKhoa.getSelectedItem().toString();
                    hocKy=Integer.parseInt(spHocKy.getSelectedItem().toString());
                    DocDL();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spHocKy.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                hocKy=Integer.parseInt(spHocKy.getSelectedItem().toString());
                if(!spNienKhoa.getSelectedItem().toString().equals("<DEFAULT>")){
                    DocDL();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        imgBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        lvHP.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });
        lvHP.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                return false;
            }
        });
        lvHPSV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                HocPhiSinhVien hp = dataSV.get(i);
                index=i;
                if (hp.getNo() != 0){
                    Intent intent = new Intent(HocPhiSinhVienMain.this, ChiTietThanhToanMain.class);
                    intent.putExtra("nienKhoa",hp.getNienKhoa());
                    intent.putExtra("hocKy",String.valueOf(hp.getHocKy()));
                    intent.putExtra("soTienDong",String.valueOf(hp.getNo()));
                    intent.putExtra("ttHoTen",tvTTHoTen.getText().toString());
                    intent.putExtra("ttMSSV",tvTTMSSV.getText().toString());
                    intent.putExtra("ttMaLop",tvTTMaLop.getText().toString());
                    startActivity(intent);
                }
                else{
                    Toast.makeText(HocPhiSinhVienMain.this,"Bạn đã thanh toán học phí trên",Toast.LENGTH_SHORT).show();
                }
            }
        });
        lvHPSV.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                return false;
            }
        });
    }
}