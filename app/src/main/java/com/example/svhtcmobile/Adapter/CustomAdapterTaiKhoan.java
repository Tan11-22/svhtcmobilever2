package com.example.svhtcmobile.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import com.example.svhtcmobile.Api.apiService.ILoginService;
import com.example.svhtcmobile.Model.TaiKhoanDTO;
import com.example.svhtcmobile.R;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomAdapterTaiKhoan extends ArrayAdapter {

    Context context;
    int resource;
    List<TaiKhoanDTO> taiKhoans;

    ILoginService iLoginService;

    public CustomAdapterTaiKhoan(@NonNull Context context, int resource, List<TaiKhoanDTO> taiKhoans, ILoginService iLoginService) {
        super(context,resource,taiKhoans);
        this.context = context;
        this.resource = resource;
        this.taiKhoans = taiKhoans;
        this.iLoginService = iLoginService;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(resource, null);

        TextView tvTenDangNhap = convertView.findViewById(R.id.tvTenDangNhap);
        TextView tvQuyen = convertView.findViewById(R.id.tvQuyen);
        ImageView ivReset = convertView.findViewById(R.id.ivReset);
        ImageView ivStatus = convertView.findViewById(R.id.ivStatus);
        TaiKhoanDTO taiKhoan = taiKhoans.get(position);
        tvTenDangNhap.setText(taiKhoan.getUsername());
        tvQuyen.setText(taiKhoan.getTenQuyen());
        if (taiKhoan.getTrangThai()) {

            ivStatus.setImageResource(R.drawable.on);
        } else {
            ivStatus.setImageResource(R.drawable.off);
        }
        ivReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Bạn có muốn đặt lại mật khẩu cho tài khoản "+ taiKhoan.getUsername().trim()+" ?")
                        .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                iLoginService.resetPassword(taiKhoan.getUsername()).enqueue(new Callback<Boolean>() {
                                    @Override
                                    public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                                        if(response.body()){
                                            Toast.makeText(context, "Đặt lại mật khẩu thành công", Toast.LENGTH_SHORT).show();
                                            dialog.dismiss();
                                        }

                                    }

                                    @Override
                                    public void onFailure(Call<Boolean> call, Throwable throwable) {

                                    }
                                });
                            }
                        })
                        .setNegativeButton("Không", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        ivStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Bạn có muốn cấm tài khoản "+ taiKhoan.getUsername().trim()+" ?")
                        .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                iLoginService.setStatus(taiKhoan.getUsername(),!taiKhoan.getTrangThai()).enqueue(new Callback<Boolean>() {
                                    @Override
                                    public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                                        if(response.body()) {
                                            taiKhoan.setTrangThai(!taiKhoan.getTrangThai());
                                            notifyDataSetChanged();
                                            dialog.dismiss();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<Boolean> call, Throwable throwable) {

                                    }
                                });
                            }
                        })
                        .setNegativeButton("Không", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        return convertView;
    }
}
