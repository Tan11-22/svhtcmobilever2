package com.example.svhtcmobile.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import com.example.svhtcmobile.Model.DanhSachHocPhiLop;
import com.example.svhtcmobile.R;

import java.util.List;

public class DanhSachHocPhiLopAdapter extends ArrayAdapter {
    Context context;
    int resource;
    List<DanhSachHocPhiLop> data;

    public DanhSachHocPhiLopAdapter(@NonNull Context context, int resource, List<DanhSachHocPhiLop> data) {
        super(context, resource, data);
        this.context = context;
        this.resource = resource;
        this.data = data;
    }

    @NonNull
    @Override
    public View getView(int positon, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(resource, null);
        TextView tvMasv = convertView.findViewById(R.id.tvMasv);
        TextView tvHoTen = convertView.findViewById(R.id.tvHoTen);
        TextView tvHocPhi = convertView.findViewById(R.id.tvHocPhi);
        TextView tvDaDong = convertView.findViewById(R.id.tvDaDong);
        DanhSachHocPhiLop danhSachHocPhiLop = data.get(positon);
        if (danhSachHocPhiLop != null) {
            tvMasv.setText(danhSachHocPhiLop.getMasv());
            tvHoTen.setText(danhSachHocPhiLop.getHo() + " " + danhSachHocPhiLop.getTen());
            tvHocPhi.setText(String.valueOf(danhSachHocPhiLop.getHocphi()));
            tvDaDong.setText(String.valueOf(danhSachHocPhiLop.getSotiendong()));
        }


        return convertView;
    }
}
