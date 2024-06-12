package com.example.svhtcmobile.Adapter;

import static android.app.Activity.RESULT_OK;
import static android.provider.MediaStore.ACTION_IMAGE_CAPTURE;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.example.svhtcmobile.Api.ApiClient;
import com.example.svhtcmobile.Api.apiService.IQuanTriThongTin;
import com.example.svhtcmobile.Controller.MainQuanTriSinhVien;
import com.example.svhtcmobile.Model.SinhVien;
import com.example.svhtcmobile.R;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdapterSinhVien extends ArrayAdapter<SinhVien> {

    private Context context;
    private int mResource;
    private String base64 = "";
    private String base64String;
    private ImageView ivAnhSV;
    private IQuanTriThongTin iQuanTriThongTin;
    private Bitmap photo;
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int REQUEST_IMAGE_CAPTURE = 2;
    private Activity activity;
    private List<SinhVien> DSSV;

    public AdapterSinhVien(Context context, int resource, List<SinhVien> objects, IQuanTriThongTin iQuanTriThongTin, Activity activity) {
        super(context, resource, objects);
        this.context = context;
        this.mResource = resource;
        this.DSSV = objects;
        this.iQuanTriThongTin = iQuanTriThongTin;
        this.activity = activity;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(mResource, parent, false);
        }

        SinhVien sinhVien = getItem(position);

        TextView tvMaSV = convertView.findViewById(R.id.tvMaSV);
        TextView tvHoSV = convertView.findViewById(R.id.tvHoSV);
        TextView tvTenSV = convertView.findViewById(R.id.tvTenSV);
        TextView tvSDT = convertView.findViewById(R.id.tvSDT);
        ImageButton btnChinhSuaSV = convertView.findViewById(R.id.imbtnChinhSuaSV);
        ImageButton btnXoaSV = convertView.findViewById(R.id.imgBtnXoaSV);
        ImageButton btnDaNghiHoc = convertView.findViewById(R.id.imbtnDaNghiHoc);

        if (sinhVien.getDanghihoc()) {
            btnDaNghiHoc.setImageResource(R.drawable.baseline_toggle_off_24);
        } else {
            btnDaNghiHoc.setImageResource(R.drawable.baseline_toggle_on_24);
        }

        tvMaSV.setText(sinhVien.getMasv());
        tvHoSV.setText(sinhVien.getHo());
        tvTenSV.setText(sinhVien.getTen());
        tvSDT.setText(sinhVien.getSdt());

        btnDaNghiHoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iQuanTriThongTin.updateDaNghiHoc(sinhVien.getMasv()).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            SinhVien sv = DSSV.get(position);
                            sv.setDanghihoc(!sv.getDanghihoc());
                            DSSV.set(position, sv);
                            notifyDataSetChanged();
                            Toast.makeText(context, "Thành công!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "Không thành công!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        // Xử lý khi có lỗi xảy ra
                    }
                });
            }
        });

        btnChinhSuaSV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditDialog(sinhVien);
            }
        });

        btnXoaSV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Xác nhận xóa sinh viên");
                builder.setMessage("Bạn có chắc muốn xóa sinh viên này?");
                builder.setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        iQuanTriThongTin.xoaSinhVien(sinhVien.getMasv()).enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                if (response.isSuccessful()) {
                                    DSSV.remove(position);
                                    notifyDataSetChanged();
                                    Toast.makeText(context, "Xóa thành công!", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(context, "Xóa không thành công!", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                // Xử lý khi có lỗi xảy ra
                            }
                        });
                    }
                });
                builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        return convertView;
    }

    private void showEditDialog(SinhVien sinhVien) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.edit_sinh_vien, null);

        EditText edtMaLop = dialogView.findViewById(R.id.loadMaLop);
        EditText edtMaSV = dialogView.findViewById(R.id.edtMaSVUpdate);
        EditText edtHoSV = dialogView.findViewById(R.id.edtHoSVUpdate);
        EditText edtTenSV = dialogView.findViewById(R.id.edtTenSVUpdate);
        RadioGroup radioGroupPhai = dialogView.findViewById(R.id.radioGroupPhaiUpdate);
        EditText edtDiaChi = dialogView.findViewById(R.id.edtDiaChiUpdate);
        EditText edtSDT = dialogView.findViewById(R.id.edtSDTUpdate);
        EditText edtNgaySinh = dialogView.findViewById(R.id.edtNgaySinhUpdate);
        EditText edtEmail = dialogView.findViewById(R.id.edtEmailUpdate);
        ImageButton btnChonAnh = dialogView.findViewById(R.id.btnChonAnhUpdate);
        ImageButton btnChupAnh = dialogView.findViewById(R.id.btnCaptureUpdate);
        ivAnhSV = dialogView.findViewById(R.id.ivAnhSinhVienUpdate);
        Button btnLuu = dialogView.findViewById(R.id.btnLuuUpdate);
        Button btnHuy = dialogView.findViewById(R.id.btnHuyUpdate);

        edtMaLop.setText(sinhVien.getMalop());
        edtMaSV.setText(sinhVien.getMasv());
        edtHoSV.setText(sinhVien.getHo());
        edtTenSV.setText(sinhVien.getTen());

        if (sinhVien.getPhai()) {
            radioGroupPhai.check(R.id.radioButtonFemale);
        } else {
            radioGroupPhai.check(R.id.radioButtonMale);
        }

        if (sinhVien.getHinhanh() != null) {

            Glide.with(context)
                    .load(ApiClient.getBaseUrl() + "thong-tin/sinh-vien/get-img?name=" + sinhVien.getHinhanh())
                    .into(ivAnhSV);
        }

        edtDiaChi.setText(sinhVien.getDiachi());
        edtSDT.setText(sinhVien.getSdt());
        edtEmail.setText(sinhVien.getEmail());

        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy");
        String ngaySinh = null;
        try {
            Date date = inputFormat.parse(sinhVien.getNgaysinh());
            ngaySinh = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        edtNgaySinh.setText(ngaySinh);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();
        dialog.show();

        btnLuu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String maLop = edtMaLop.getText().toString();
                String maSV = edtMaSV.getText().toString();
                String hoSV = edtHoSV.getText().toString();
                String tenSV = edtTenSV.getText().toString();
                String diaChiSV = edtDiaChi.getText().toString();
                String sdtSV = edtSDT.getText().toString();
                String email = edtEmail.getText().toString();
                String inputDate = edtNgaySinh.getText().toString();

                String ngaySinh = "";
                SimpleDateFormat inputFormat = new SimpleDateFormat("dd-MM-yyyy");
                SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");

                try {
                    Date date = inputFormat.parse(inputDate);
                    ngaySinh = outputFormat.format(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                Boolean phai;
                int selectedGenderId = radioGroupPhai.getCheckedRadioButtonId();
                if (selectedGenderId == R.id.radioButtonFemale) {
                    phai = true;
                } else {
                    phai = false;
                }
                String finalNgaySinh = ngaySinh;
                SinhVien sinhVien1 = sinhVien;
                sinhVien1.setMasv(maSV);
                sinhVien1.setHo(hoSV);
                sinhVien1.setTen(tenSV);
                sinhVien1.setDiachi(diaChiSV);
                sinhVien1.setSdt(sdtSV);
                sinhVien1.setPhai(phai);
                sinhVien1.setNgaysinh(ngaySinh);
                sinhVien1.setEmail(email);


                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                photo.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                byte[] byteArray = byteArrayOutputStream.toByteArray();
                RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), byteArray);
                MultipartBody.Part in = MultipartBody.Part.createFormData("img", "", requestBody);
                Gson gson = new Gson();
                String dataSV = gson.toJson(sinhVien1);
                RequestBody sv1 = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), dataSV);
                iQuanTriThongTin.updateSinhVien(sv1,in).enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        JsonObject jsOb= response.body();
                        if (response.code() == 200) {

                            sinhVien.setHo(hoSV);
                            sinhVien.setTen(tenSV);
                            sinhVien.setDiachi(diaChiSV);
                            sinhVien.setSdt(sdtSV);
                            sinhVien.setPhai(phai);
                            sinhVien.setNgaysinh(finalNgaySinh);
                            sinhVien.setEmail(email);
                            sinhVien.setHinhanh(jsOb.get("filename").getAsString());

                            notifyDataSetChanged();
                            Toast.makeText(context, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        } else {
                            Toast.makeText(context, "Cập nhật không thành công! ERROR: "+jsOb.get("status").getAsString(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        // Xử lý khi có lỗi xảy ra
                        Log.d("ERORlog", t.getMessage());
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

        btnChonAnh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                activity.startActivityForResult(intent, PICK_IMAGE_REQUEST);
            }
        });

        btnChupAnh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(ACTION_IMAGE_CAPTURE);
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CAMERA}, 1);
                    return;
                }
                activity.startActivityForResult(cameraIntent, 99);
            }
        });
    }

    public void handleActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            try {
                photo = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
                if (ivAnhSV != null && photo != null) {
                    ivAnhSV.setImageBitmap(photo);
                }
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("TAG", "Error loading image: " + e.getMessage());
            }
        }

        if( requestCode == 99 && resultCode == Activity.RESULT_OK) {
            photo = (Bitmap) data.getExtras().get("data");
            ivAnhSV.setImageBitmap(photo);
        }
    }
}
