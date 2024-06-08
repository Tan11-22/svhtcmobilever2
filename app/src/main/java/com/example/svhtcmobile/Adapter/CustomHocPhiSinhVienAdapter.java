package com.example.svhtcmobile.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.svhtcmobile.Api.ApiClient;
import com.example.svhtcmobile.Api.apiService.ILoginService;
import com.example.svhtcmobile.R;
import com.example.svhtcmobile.Model.HocPhiSinhVien;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CustomHocPhiSinhVienAdapter extends ArrayAdapter {
    Context context;
    int resource;
    List<HocPhiSinhVien> data;
    SharedPreferences accountSharedPref;

    List<JsonObject> data1 = new ArrayList<>();
    public CustomHocPhiSinhVienAdapter(@NonNull Context context, int resource, List<HocPhiSinhVien> data) {
        super(context, resource, data);
        this.context = context;
        this.resource = resource;
        this.data = data;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(resource, null);
        TextView tvNienKhoa= convertView.findViewById(R.id.tvNienKhoa);
        TextView tvHocKy = convertView.findViewById(R.id.tvHocKy);
        TextView tvHocPhi = convertView.findViewById(R.id.tvHocPhi);
        TextView tvDaDong = convertView.findViewById(R.id.tvDaDong);
        TextView tvNo=convertView.findViewById(R.id.tvNo);
        ImageView ivXuatPDF = convertView.findViewById(R.id.ivXuatPDF);
        HocPhiSinhVien hp = data.get(position);
        tvNienKhoa.setText(hp.getNienKhoa());
        tvHocKy.setText(String.valueOf(hp.getHocKy()));
        tvHocPhi.setText(String.valueOf(hp.getHocPhi()));
        tvDaDong.setText(String.valueOf(hp.getDaDong()));
        tvNo.setText(String.valueOf(hp.getNo()));

        accountSharedPref = context.getSharedPreferences("Account", Context.MODE_PRIVATE);
        String token = accountSharedPref.getString("token", "");
        String username = accountSharedPref.getString("username", "");
        String ho = accountSharedPref.getString("ho", "");
        String ten = accountSharedPref.getString("ten", "");

        String filePDFName = username.trim()+"_"+hp.getNienKhoa()+"_"+String.valueOf(hp.getHocKy())+".pdf";
        Log.e("logchecktoken", token);
        Retrofit retrofit = ApiClient.getClient(token);
        ILoginService iLoginService = retrofit.create(ILoginService.class);
        ivXuatPDF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApiClient.askPermissions((Activity) context);
                View view = LayoutInflater.from(context).inflate(R.layout.layout_hoa_don,null);
                TextView tvHoTen = view.findViewById(R.id.tvHoTen);
                TextView tvMaSV = view.findViewById(R.id.tvMaSV);
                ListView lvCTDHP = view.findViewById(R.id.lvCTDHP);
                lvCTDHP.setVisibility(View.VISIBLE);
                TextView tvTongTien = view.findViewById(R.id.tvTongTien);


                iLoginService.getDanhSachCTHP(username, hp.getNienKhoa(), hp.getHocKy()).enqueue(new Callback<List<JsonObject>>() {
                    @Override
                    public void onResponse(Call<List<JsonObject>> call, Response<List<JsonObject>> response) {
                        if(response.code() == 200) {
                            tvHoTen.setText(tvHoTen.getText().toString()+" "+ho+" "+ten);
                            tvMaSV.setText(tvMaSV.getText().toString()+" "+ username);
                            data1 = response.body();
                            CustomAdapterHoaDon customAdapterHoaDon = new CustomAdapterHoaDon(context,R.layout.layout_item_hoa_don, data1);
                            lvCTDHP.setAdapter(customAdapterHoaDon);
                            int tongTien = 0;
                            for (JsonObject js: response.body()){
                                int soTien = js.get("SOTIENDONG").getAsInt();
                                tongTien += soTien;
                            }
                            tvTongTien.setText(String.valueOf(tongTien));
                            Log.d("myloghocphi", data1.toString());
                            ApiClient.convertXMLToPDF((Activity) context,view,filePDFName);
                            Toast.makeText(context, "Save PDF thanh Công", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(context, "Bug", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<JsonObject>> call, Throwable throwable) {

                    }
                });

            }
        });
        return convertView;
    }
}
