package com.example.svhtcmobile.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.example.svhtcmobile.R;
import com.example.svhtcmobile.Model.HocPhiSinhVien;
import com.google.gson.JsonObject;

import java.util.List;

public class CustomAdapterHoaDon extends ArrayAdapter {

    Context context;
    int resource;
    List<JsonObject> data;
    public CustomAdapterHoaDon(@NonNull Context context, int resource,  List<JsonObject> data) {
        super(context, resource,data);
        this.context = context;
        this.resource = resource;
        this.data = data;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        convertView = LayoutInflater.from(context).inflate(resource, null);
        JsonObject jsOb = data.get(position);
        TextView tvSTT = convertView.findViewById(R.id.tvSTT);
        TextView tvNienKhoa = convertView.findViewById(R.id.tvNienKhoa);
        TextView tvHocKy = convertView.findViewById(R.id.tvHocKy);
        TextView tvNgayDong = convertView.findViewById(R.id.tvNgayDong);
        TextView tvSoTien = convertView.findViewById(R.id.tvSoTien);
        String nienKhoa = jsOb.get("NIENKHOA").getAsString();
        String hocKy = jsOb.get("HOCKY").getAsString();
        String ngayDong = jsOb.get("NGAYDONG").getAsString();
        String soTien = jsOb.get("SOTIENDONG").getAsString();
        tvSTT.setText(String.valueOf(position));
        tvNienKhoa.setText(nienKhoa);
        tvHocKy.setText(hocKy);
        tvSoTien.setText(soTien);
        tvNgayDong.setText(ngayDong);
        Log.d("myloghocphi123", data.toString());
        return convertView;
    }
}
