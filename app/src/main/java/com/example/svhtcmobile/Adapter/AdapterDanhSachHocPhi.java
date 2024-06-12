package com.example.svhtcmobile.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.svhtcmobile.Api.apiService.IQuanTriThongTin;
import com.example.svhtcmobile.Model.HocPhiList;
import com.example.svhtcmobile.Model.LopTinChiDTO;
import com.example.svhtcmobile.R;

import java.util.List;

public class AdapterDanhSachHocPhi  extends ArrayAdapter<HocPhiList> {
    private android.content.Context Context;
    private int Resource;
    private List<HocPhiList> dshp; // Không cần khai báo danh sách mới ở đây
    IQuanTriThongTin iQuanTriThongTin;

    public AdapterDanhSachHocPhi(Context context, int resource, List<HocPhiList> objects, IQuanTriThongTin iQuanTriThongTin) {
        super(context, resource, objects);
        Context = context;
        Resource = resource;
        this.dshp = objects; // Sử dụng danh sách được truyền vào
        this.iQuanTriThongTin = iQuanTriThongTin;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(Context);
            convertView = inflater.inflate(Resource, parent, false);
        }
        HocPhiList hp = getItem(position);
        TextView tvMasv = convertView.findViewById(R.id.tvMaSV);
        TextView tvHoten = convertView.findViewById(R.id.tvHoten);
        TextView tvSoTien = convertView.findViewById(R.id.tvSoTien);
        TextView tvNgayDong = convertView.findViewById(R.id.tvNgayDong);

        tvMasv.setText(hp.getMasv());
        tvHoten.setText(hp.getHoten());
        tvSoTien.setText(String.valueOf(hp.getSotien()));
        if(hp.getDadong().equals("Chua"))
        {
            tvNgayDong.setText("Chưa đóng");
        }
        else tvNgayDong.setText(hp.getDadong());

        return convertView;
    }
}
