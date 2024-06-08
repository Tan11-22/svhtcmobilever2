package com.example.svhtcmobile.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.svhtcmobile.Model.DDKLTC;
import com.example.svhtcmobile.R;


import java.util.List;

public class CustomDaDangKyAdapter extends ArrayAdapter {
    Context context;
    int resource;
    List<DDKLTC> data;
    public CustomDaDangKyAdapter(@NonNull Context context, int resource, List<DDKLTC> data) {
        super(context, resource, data);
        this.context = context;
        this.resource = resource;
        this.data = data;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(resource, null);
        TextView tvMaLTC=convertView.findViewById(R.id.tvMaLTC);
        TextView tvMaMH = convertView.findViewById(R.id.tvMaMH);
        TextView tvTenMH = convertView.findViewById(R.id.tvTenMH);
        TextView tvNhom=convertView.findViewById(R.id.tvNhom);
        TextView tvSTC = convertView.findViewById(R.id.tvSTC);
        DDKLTC x = data.get(position);
        tvMaLTC.setText(String.valueOf(x.getMaLTC()));
        tvMaMH.setText(x.getMaMH());
        tvTenMH.setText(x.getTenMH());
        tvNhom.setText(String.valueOf(x.getNhom()));
        tvSTC.setText(String.valueOf(x.getSoTinChi()));
        return convertView;
    }
}
