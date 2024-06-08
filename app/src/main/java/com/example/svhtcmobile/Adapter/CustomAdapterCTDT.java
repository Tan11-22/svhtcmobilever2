package com.example.svhtcmobile.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import com.example.svhtcmobile.Api.apiService.ILoginService;
import com.example.svhtcmobile.Model.CTDTDTO;

import java.util.List;
import com.example.svhtcmobile.R;
public class CustomAdapterCTDT extends ArrayAdapter {
    Context context;
    int resource;
    List<CTDTDTO> ctdtdtos;

    ILoginService iLoginService;

    public CustomAdapterCTDT(@NonNull Context context, int resource, List<CTDTDTO> ctdtdtos, ILoginService iLoginService) {
        super(context, resource, ctdtdtos);
        this.context = context;
        this.resource = resource;
        this.ctdtdtos = ctdtdtos;
        this.iLoginService = iLoginService;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(resource, null);
        TextView tvMaMon = convertView.findViewById(R.id.tvMaMon);
        TextView tvTenMon = convertView.findViewById(R.id.tvTenMon);
        TextView tvHocKy = convertView.findViewById(R.id.tvHocKy);


        ImageView ivXoa = convertView.findViewById(R.id.ivXoa);
        CTDTDTO ctdtdto = ctdtdtos.get(position);

        tvMaMon.setText(ctdtdto.getMaMH());
        tvTenMon.setText(ctdtdto.getTenMH());
        tvHocKy.setText(String.valueOf(ctdtdto.getHocKy()));

        return convertView;
    }
}
