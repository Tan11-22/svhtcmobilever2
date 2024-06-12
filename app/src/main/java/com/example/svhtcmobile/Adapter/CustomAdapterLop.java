package com.example.svhtcmobile.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.svhtcmobile.Api.ApiClient;
import com.example.svhtcmobile.Api.apiService.ILopService;
import com.example.svhtcmobile.Api.apiService.IQuanTriService;
import com.example.svhtcmobile.Controller.DanhSachLop;
import com.example.svhtcmobile.Model.ApiResponse;
import com.example.svhtcmobile.Model.He;
import com.example.svhtcmobile.Model.Lop;
import com.example.svhtcmobile.Model.LopDTO;
import com.example.svhtcmobile.Model.LopDTO1;
import com.example.svhtcmobile.R;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CustomAdapterLop extends ArrayAdapter {
    Context context;
    int resource;
    List<LopDTO1> data;
    private SharedPreferences sharedPreferences;
    private IQuanTriService iQuanTriService;
    private ILopService iLopService;
    private int check = 0;
    public CustomAdapterLop(@NonNull Context context, int resource, @NonNull List<LopDTO1> data) {
        super(context, resource, data);
        this.context = context;
        this.resource = resource;
        this.data = data;

        sharedPreferences = context.getSharedPreferences("Account", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "");
        String ho = sharedPreferences.getString("ho", "");
        String ten = sharedPreferences.getString("ten", "");
        String username = sharedPreferences.getString("username", "");
        String quyen = sharedPreferences.getString("quyen", "");
        Retrofit retrofit = ApiClient.getClient(token);
        iQuanTriService = retrofit.create(IQuanTriService.class);
        iLopService = retrofit.create(ILopService.class);
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(resource, null); // nap giao dien
        TextView tvMaLop = convertView.findViewById(R.id.tvMaLop);
        TextView tvTenLop = convertView.findViewById(R.id.tvTenLop);
        TextView tvKhoaHoc = convertView.findViewById(R.id.tvKhoaHoc);
        ImageButton imbtnChinhSua = convertView.findViewById(R.id.imbtnChinhSua);
        ImageButton imbtnXoa = convertView.findViewById(R.id.imbtnXoa);
        LopDTO1 lop = data.get(position);
        tvMaLop.setText(lop.getMaLop());
        tvTenLop.setText(lop.getTenLop());
        tvKhoaHoc.setText(lop.getKhoaHoc());
        imbtnChinhSua.setImageResource(R.drawable.edit);
        imbtnXoa.setImageResource(R.drawable.delete_24px);
        imbtnXoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialogXoaLop(lop);
            }
        });
        imbtnChinhSua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialogSuaLop(lop);
            }
        });
        return convertView;
    }

    private void openDialogXoaLop(LopDTO1 lop) {
        final Dialog dialog = new Dialog(this.context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_dialog_xoa_lop);

        Window window = dialog.getWindow();
        if(window == null) return;

        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAtrributes = window.getAttributes();
        windowAtrributes.gravity = Gravity.CENTER;
        window.setAttributes(windowAtrributes);
        TextView tvTenLop = dialog.findViewById(R.id.tvTenLop);
        Button btnOk = dialog.findViewById(R.id.btnOk);
        Button btnHuy = dialog.findViewById(R.id.btnHuy);

        tvTenLop.setText(lop.getTenLop());

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(view.getContext(), lop.getMaLop(), Toast.LENGTH_LONG).show();
                iLopService.xoaLop(lop.getMaLop()).enqueue(new Callback<ApiResponse>() {
                    @Override
                    public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                        ApiResponse apiResponse = response.body();
                        Toast.makeText(view.getContext(), apiResponse.getStatus(), Toast.LENGTH_LONG).show();
                        if( apiResponse.getCode() == 200) {
                            dialog.dismiss();
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse> call, Throwable throwable) {

                    }
                });
            }
        });
        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.setCancelable(true);
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                ((DanhSachLop) context).loadData();
            }
        });
        dialog.show();
    }

    private void openDialogSuaLop(LopDTO1 lop){
        final Dialog dialog = new Dialog(this.context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_dialog_them_lop);

        Window window = dialog.getWindow();
        if(window == null) return;

        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAtrributes = window.getAttributes();
        windowAtrributes.gravity = Gravity.CENTER;
        window.setAttributes(windowAtrributes);

        EditText edtMaLop = dialog.findViewById(R.id.edtMaLop);
        EditText edtTenLop = dialog.findViewById(R.id.edtTenLop);
        EditText edtNamBatDau = dialog.findViewById(R.id.edtNamBatDau);
        EditText edtNamKetThuc = dialog.findViewById(R.id.edtNamKetThuc);

        Button btnHuy = dialog.findViewById(R.id.btnHuy);
        Button btnOk = dialog.findViewById(R.id.btnOk);
        Spinner spKhoa = dialog.findViewById(R.id.spKhoa);
        Spinner spHeDaoTao = dialog.findViewById(R.id.spHeDaoTao);
        List<He> danhSachHeDaoTao = new ArrayList<>();
        List<String> danhSachKhoa = new ArrayList<>();

        iLopService.danhSachHe().enqueue(new Callback<ApiResponse<List<JsonObject>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<JsonObject>>> call, Response<ApiResponse<List<JsonObject>>> response) {
                ApiResponse<List<JsonObject>> apiResponse = response.body();
                if (apiResponse.getCode() == 200) {
                    List<JsonObject> hes = apiResponse.getData();
                    for (JsonObject he : hes) {
                        He he1 = new He(he.get("ID_HE").getAsInt(), he.get("TEN_HE").getAsString());
                        danhSachHeDaoTao.add(he1);
                        ArrayAdapter adapter_spHeDaoTao = new ArrayAdapter(dialog.getContext(), R.layout.layout_spinner, danhSachHeDaoTao);
                        spHeDaoTao.setAdapter(adapter_spHeDaoTao);
                    }
                }else {
                    Toast.makeText(context,"Không thể tải dữ liệu!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<JsonObject>>> call, Throwable throwable) {

            }
        });
        iLopService.danhSachKhoa().enqueue(new Callback<ApiResponse<List<JsonObject>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<JsonObject>>> call, Response<ApiResponse<List<JsonObject>>> response) {
                ApiResponse<List<JsonObject>> apiResponse = response.body();
                if (apiResponse.getCode() == 200) {
                    List<JsonObject> khoas = apiResponse.getData();
                    for(JsonObject khoa : khoas) {
                        String khoa1 = khoa.get("makhoa").getAsString().trim() + "-"+khoa.get("tenkhoa").getAsString();
                        danhSachKhoa.add(khoa1);
                        ArrayAdapter adapter_spKhoa = new ArrayAdapter(dialog.getContext(), R.layout.layout_spinner, danhSachKhoa);
                        spKhoa.setAdapter(adapter_spKhoa);
                    }
                }else {
                    Toast.makeText(context,"Không thể tải dữ liệu!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<JsonObject>>> call, Throwable throwable) {

            }
        });

        edtMaLop.setText(lop.getMaLop());
        edtTenLop.setText(lop.getTenLop());
        edtNamBatDau.setText(lop.getKhoaHoc().substring(0, 4));
        edtNamKetThuc.setText(lop.getKhoaHoc().substring(5));
        for (int i =0; i< danhSachHeDaoTao.size();i++) {
            if(lop.getTenHe().equals(danhSachHeDaoTao.get(i).getTenHe())) {
                spHeDaoTao.setSelection(i);
                break;
            }
        }
        for (int i=0; i< danhSachKhoa.size();i ++ ) {
            if(lop.getTenKhoa().equals(danhSachKhoa.get(i).split("-")[0])){
                spKhoa.setSelection(i);
                break;
            }
        }

        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                check = 0;
            }
        });

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String, Object> dataRequset = new HashMap<>();
                dataRequset.put("maLop", edtMaLop.getText().toString().trim());
                dataRequset.put("tenLop",edtTenLop.getText().toString().trim());
                dataRequset.put("khoaHoc1",edtNamBatDau.getText().toString().trim() );
                dataRequset.put("khoaHoc2", edtNamKetThuc.getText().toString().trim());
                String maKhoa = spKhoa.getSelectedItem().toString().split("-")[0];
                dataRequset.put("maKhoa", maKhoa);
                String tmp = spHeDaoTao.getSelectedItem().toString();
                for(He he : danhSachHeDaoTao){
                    if(tmp.equals(he.getTenHe())){
                        dataRequset.put("idHe", he.getId());
                    }
                }
                iLopService.updateLopHoc(dataRequset).enqueue(new Callback<ApiResponse>() {
                    @Override
                    public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                        ApiResponse apiResponse = response.body();
                        Toast.makeText(context,apiResponse.getStatus(),Toast.LENGTH_SHORT).show();
                        if (apiResponse.getCode() == 200) {
                            check = 1;
                            dialog.dismiss();
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse> call, Throwable throwable) {

                    }
                });


            }
        });
        dialog.setCancelable(true);
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                if(check == 1){
                    ((DanhSachLop) context).loadData();
                }

            }
        });
        dialog.show();
    }
}
