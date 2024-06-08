package com.example.svhtcmobile.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.svhtcmobile.Api.ApiClient;
import com.example.svhtcmobile.Model.HocPhiHocKy;
import com.example.svhtcmobile.R;
import java.util.List;

public class CustomHocPhiHocKyAdapter extends ArrayAdapter {
    Context context;
    int resource;
    List<HocPhiHocKy> data;
    public CustomHocPhiHocKyAdapter(@NonNull Context context, int resource, List<HocPhiHocKy> data) {
        super(context, resource, data);
        this.context = context;
        this.resource = resource;
        this.data = data;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(resource, null);
        TextView tvMaMH = convertView.findViewById(R.id.tvMaMH);
        TextView tvTenMH = convertView.findViewById(R.id.tvTenMH);
        TextView tvSTC = convertView.findViewById(R.id.tvSTC);
        TextView tvHocLai = convertView.findViewById(R.id.tvHocLai);
        TextView tvTT = convertView.findViewById(R.id.tvTT);

        HocPhiHocKy hp = data.get(position);
        tvMaMH.setText(hp.getMaMH());
        tvTenMH.setText(hp.getTenMH());
        tvSTC.setText(String.valueOf(hp.getSoTC()));
        tvHocLai.setText(String.valueOf(hp.getHocLai()));
        tvTT.setText(String.valueOf(hp.getTien()));

        return convertView;
    }
}
