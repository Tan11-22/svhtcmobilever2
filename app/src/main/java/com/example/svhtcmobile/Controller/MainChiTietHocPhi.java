package com.example.svhtcmobile.Controller;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.svhtcmobile.Model.DanhSachHocPhiLop;
import com.example.svhtcmobile.R;

public class MainChiTietHocPhi extends AppCompatActivity {

    TextView tvMasv, tvHoTen, tvHocPhi, tvNgayDong, tvSoTienDong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_chi_tiet_hoc_phi);
        setControl();
        setup();
    }

    public void setControl(){
        tvMasv = findViewById(R.id.tvMasv);
        tvHoTen = findViewById(R.id.tvHoTen);
        tvHocPhi = findViewById(R.id.tvHocPhi);
        tvNgayDong = findViewById(R.id.tvNgayDong);
        tvSoTienDong = findViewById(R.id.tvSoTienDong);
    }

    public void setup(){
        Intent intent = getIntent();
        if(intent.getSerializableExtra("hocphi") == null){
            System.out.println("không có hocphi");
        }
        DanhSachHocPhiLop danhSachHocPhiLop = (DanhSachHocPhiLop) intent.getSerializableExtra("hocphi");
        if (danhSachHocPhiLop == null){
            Toast.makeText(MainChiTietHocPhi.this, "Không có dữ liệu", Toast.LENGTH_SHORT).show();
            return;
        }
        tvMasv.setText(danhSachHocPhiLop.getMasv());
        tvHoTen.setText(danhSachHocPhiLop.getHo() +" "+danhSachHocPhiLop.getTen());
        tvHocPhi.setText(Integer.toString(danhSachHocPhiLop.getHocphi()));
        tvNgayDong.setText(danhSachHocPhiLop.getNgaydong());
        tvSoTienDong.setText(Integer.toString(danhSachHocPhiLop.getSotiendong()));
    }

    public void onBackPressed(View view){
        super.onBackPressed();
    }
}