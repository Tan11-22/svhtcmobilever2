package com.example.svhtcmobile.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.svhtcmobile.Controller.DanhSachSinhVienLTC;
import com.example.svhtcmobile.Model.LopTinChiTheoGV;
import com.example.svhtcmobile.R;

import java.util.List;

public class CustomAdapterMonHocTheoGV extends ArrayAdapter {
    Context context;
    int resource;
    List<LopTinChiTheoGV> data;

    public CustomAdapterMonHocTheoGV(@NonNull Context context, int resource, @NonNull List<LopTinChiTheoGV> data) {
        super(context, resource, data);
        this.context = context;
        this.resource = resource;
        this.data = data;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(resource, null); // nap giao dien
        TextView tvMaMH = convertView.findViewById(R.id.tvMaMH);
        TextView tvTenMH = convertView.findViewById(R.id.tvTenMH);
        TextView tvNhom = convertView.findViewById(R.id.tvNhom);
        ImageButton imbtnChinhSua = convertView.findViewById(R.id.imbtnChinhSua);
        LopTinChiTheoGV ltc = data.get(position);
        tvMaMH.setText(ltc.getMaMH());
        tvTenMH.setText(ltc.getTenMH());
        tvNhom.setText(ltc.getNhom() + "");
        imbtnChinhSua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DanhSachSinhVienLTC.class);
                intent.putExtra("LTC", ltc);
                context.startActivity(intent);
            }
        });
        return convertView;
    }
}
