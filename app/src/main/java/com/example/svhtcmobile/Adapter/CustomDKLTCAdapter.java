package com.example.svhtcmobile.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import com.example.svhtcmobile.Model.DKLTC;
import com.example.svhtcmobile.R;
import java.util.List;

public class CustomDKLTCAdapter extends ArrayAdapter {
    Context context;
    int resource;
    List<DKLTC> data;
    List<String> dsDDKMM;
    List<Integer> dsDDKMLTC;
    public CustomDKLTCAdapter(@NonNull Context context, int resource, List<DKLTC> data, List<String> dsDDKMM, List<Integer>dsDDKMLTC) {
        super(context, resource, data);
        this.context = context;
        this.resource = resource;
        this.data = data;
        this.dsDDKMM=dsDDKMM;
        this.dsDDKMLTC=dsDDKMLTC;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(resource, null);
        TextView tvMaLTC=convertView.findViewById(R.id.tvMaLTC);
        TextView tvMaMH = convertView.findViewById(R.id.tvMaMH);
        TextView tvTenMH = convertView.findViewById(R.id.tvTenMH);
        TextView tvNhom=convertView.findViewById(R.id.tvNhom);
        TextView tvSoSVToiThieu = convertView.findViewById(R.id.tvSoSVToiThieu);
        TextView tvSTC = convertView.findViewById(R.id.tvSTC);
        DKLTC x = data.get(position);
        tvMaMH.setText(x.getMaMH());
        tvTenMH.setText(x.getTenMH());
        tvNhom.setText(String.valueOf(x.getNhom()));
        tvSoSVToiThieu.setText(String.valueOf(x.getSoSVToiThieu()));
        tvSTC.setText(String.valueOf(x.getSoTinChi()));
        tvMaLTC.setText(String.valueOf(x.getMaLTC()));
        if(dsDDKMM.contains(x.getMaMH())){
            convertView.setEnabled(false);
            convertView.setOnClickListener(null);
            if(dsDDKMLTC.contains(x.getMaLTC())){
                convertView.setBackgroundColor(Color.GREEN);
            }
            else{
                convertView.setBackgroundColor(Color.LTGRAY);
            }
        }
        return convertView;
    }
}
