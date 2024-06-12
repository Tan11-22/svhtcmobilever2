package com.example.svhtcmobile.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.svhtcmobile.Api.apiService.IQuanTriThongTin;
import com.example.svhtcmobile.Model.HocPhiSV;
import com.example.svhtcmobile.R;

import java.util.List;

public class AdapterHocPhiSV extends ArrayAdapter<HocPhiSV> {
    private Context context;
    private int resource;
    private List<HocPhiSV> dshp;
    IQuanTriThongTin iQuanTriThongTin;

    public AdapterHocPhiSV(Context context, int resource, List<HocPhiSV> objects, IQuanTriThongTin iQuanTriThongTin) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.dshp = objects;
        this.iQuanTriThongTin = iQuanTriThongTin;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(resource, parent, false);
        }

        HocPhiSV hp = getItem(position);
        TextView tvNienKhoa = convertView.findViewById(R.id.tvNienKhoa);
        TextView tvHocki = convertView.findViewById(R.id.tvHocKy);
        TextView tvHocPhi = convertView.findViewById(R.id.tvHocPhi);
        TextView tvDaDong = convertView.findViewById(R.id.tvDaDong);
        TextView tvConNo = convertView.findViewById(R.id.tvNo);

        tvNienKhoa.setText(hp.getNienkhoa());
        tvHocki.setText(String.valueOf(hp.getHocki()));
        tvHocPhi.setText(String.valueOf(hp.getSotien()));
        tvDaDong.setText(String.valueOf(hp.getDathu()));
        tvConNo.setText(String.valueOf(hp.getConno()));

        return convertView;
    }
}
