package com.example.svhtcmobile.Adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.svhtcmobile.Model.Nganh;
import com.example.svhtcmobile.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import com.example.svhtcmobile.Api.apiService.ILoginService;
import com.example.svhtcmobile.Model.Khoa;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomAdapterKhoa extends ArrayAdapter {
    Context context;
    int resource;
    List<Khoa> khoas;

    ILoginService iLoginService;

    public CustomAdapterKhoa(@NonNull Context context, int resource, List<Khoa> khoas, ILoginService iLoginService) {
        super(context,resource,khoas);
        this.context = context;
        this.resource = resource;
        this.khoas = khoas;
        this.iLoginService = iLoginService;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(resource, null);

        TextView tvMa = convertView.findViewById(R.id.tvMaKhoa);
        TextView tvTen = convertView.findViewById(R.id.tvTenKhoa);
        ImageView ivChinhSua = convertView.findViewById(R.id.ivChinhSua);
        ImageView ivXoa = convertView.findViewById(R.id.ivXoa);
        Khoa khoa = khoas.get(position);
        tvMa.setText(khoa.getMaKhoa());
        tvTen.setText(khoa.getTenKhoa());
        ivChinhSua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogEditKhoa(khoa);
            }
        });
        ivXoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Bạn có muốn xoá "+ khoa.getTenKhoa().trim()+" ?")
                        .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                iLoginService.deleteKhoa(khoa.getMaKhoa()).enqueue(new Callback<Boolean>() {
                                    @Override
                                    public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                                        if(response.code() == 200) {
                                            if(response.body()) {
                                                khoas.remove(position);
                                                notifyDataSetChanged();
                                                Toast.makeText(context,"Xoá khoa "+ khoa.getTenKhoa()+ " thành công!", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(context,"Xoá khoa "+ khoa.getTenKhoa()+ " thất bại!", Toast.LENGTH_SHORT).show();
                                            }
                                            dialog.dismiss();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<Boolean> call, Throwable throwable) {

                                    }
                                });
                            }
                        }).setNegativeButton("Không", new DialogInterface.OnClickListener() {
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

    private void showDialogEditKhoa(Khoa khoa) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_add_khoa);

        Window window = dialog.getWindow();
        if(window == null) return;

        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAtrributes = window.getAttributes();
        windowAtrributes.gravity = Gravity.CENTER;
        window.setAttributes(windowAtrributes);
        ImageView ivClose = dialog.findViewById(R.id.ivClose);
        EditText edtMaKhoa =dialog.findViewById(R.id.edtMaKhoa);
        EditText edtTenKhoa =dialog.findViewById(R.id.edtTenKhoa);
        Spinner spNganh =dialog.findViewById(R.id.spNganh);
        Button btnTaoMoi =dialog.findViewById(R.id.btnTaoMoi);

        List<String> khoaNganhs = new ArrayList<>();

        iLoginService.getDanhSachNganh().enqueue(new Callback<List<Nganh>>() {
            @Override
            public void onResponse(Call<List<Nganh>> call, Response<List<Nganh>> response) {
                if(response.code()==200) {
                    List<Nganh> nganhs = response.body();
                    int position = -1;
                    for (int i = 0; i < nganhs.size(); i++) {
                        khoaNganhs.add(String.valueOf(nganhs.get(i).getIdNganh())+ " - "+nganhs.get(i).getTenNganh());
                        if(nganhs.get(i).getIdNganh() == khoa.getIdNganh()){
                            position = i;
                        }
                    }
                    ArrayAdapter adapterNganh = new ArrayAdapter(context, android.R.layout.simple_list_item_1,khoaNganhs);
                    spNganh.setAdapter(adapterNganh);
                    spNganh.setSelection(position);
                }
            }

            @Override
            public void onFailure(Call<List<Nganh>> call, Throwable throwable) {

            }
        });
        edtMaKhoa.setText(khoa.getMaKhoa());
        edtMaKhoa.setInputType(InputType.TYPE_NULL);
        edtMaKhoa.setKeyListener(null);
        edtTenKhoa.setText(khoa.getTenKhoa());


        btnTaoMoi.setText("Thay đổi");
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btnTaoMoi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String maKhoa = edtMaKhoa.getText().toString().trim();
                String tenKhoa = edtTenKhoa.getText().toString().trim();
                String nganh = spNganh.getSelectedItem().toString();
                if (tenKhoa.equals("")) {
                    edtTenKhoa.setError("Chưa nhập tên khoa!");
                    return;
                }
                int idNganh = Integer.parseInt(nganh.split("-")[0].trim());
                if(tenKhoa.equals(khoa.getTenKhoa()) && idNganh == khoa.getIdNganh()) {
                    Toast.makeText(context,"Bạn chưa thay đổi thông tin nào cả!", Toast.LENGTH_SHORT).show();
                    return;
                }
                iLoginService.editKhoa(maKhoa,tenKhoa,idNganh).enqueue(new Callback<Boolean>() {
                    @Override
                    public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                        if (response.code() == 200) {
                            if (response.body()){
                                khoa.setMaKhoa(maKhoa);
                                khoa.setTenKhoa(tenKhoa);
                                Toast.makeText(context,"Thay đổi thông tin khoa thành công!", Toast.LENGTH_SHORT).show();
                                notifyDataSetChanged();
                                dialog.dismiss();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Boolean> call, Throwable throwable) {

                    }
                });
            }
        });
        dialog.setCancelable(true);
        dialog.show();
    }
}
