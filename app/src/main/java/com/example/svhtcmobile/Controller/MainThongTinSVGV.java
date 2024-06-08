package com.example.svhtcmobile.Controller;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.example.svhtcmobile.Api.ApiClient;
import com.example.svhtcmobile.Api.apiService.IThongKe;
import com.example.svhtcmobile.Model.SinhVien;
import com.example.svhtcmobile.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;
import java.util.Date;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainThongTinSVGV extends AppCompatActivity {
    private EditText edtMasv, edtHo, edtTen, edtDiaChi, edtNgaysinh, edtLop;
    private CheckBox ckbDangHoc;
    private RadioGroup rBtnPhai;
    private RadioButton rbtnNam, rbtnNu;
    private ImageView imgAvatar;
    private Button btnCapnhat, btnSuaAnh;
    private Uri selectedImageUri = null;
    private Bitmap photo;
    private String picturePath;

    private static final int REQUEST_CODE_PICK_IMAGE = 101;

    SharedPreferences accountSharedPref;

    IThongKe iThongKe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_thong_tin_svgv);
        setControl();
        setEvent();
        String username = accountSharedPref.getString("username", "");
        Log.d("user",username);
        iThongKe.getSinhVienById(username).enqueue(new Callback<SinhVien>() {
            @Override
            public void onResponse(Call<SinhVien> call, Response<SinhVien> response) {
                SinhVien sinhVien = response.body();
                if (sinhVien == null) {
                    Toast.makeText(MainThongTinSVGV.this, "Sinh viên không tồn tại", Toast.LENGTH_SHORT).show();
                } else {
                    setup(sinhVien);
                }
            }

            @Override
            public void onFailure(Call<SinhVien> call, Throwable throwable) {
                Toast.makeText(MainThongTinSVGV.this, "Lấy thông tin thất bại", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void setup(SinhVien sinhVien) {
        edtMasv.setText(sinhVien.getMasv());
        edtHo.setText(sinhVien.getHo());
        edtTen.setText(sinhVien.getTen());
        edtLop.setText(sinhVien.getLop());
        edtDiaChi.setText(sinhVien.getDiachi());
        edtNgaysinh.setText(sinhVien.getNgaysinh());
        if (!sinhVien.getPhai()) {
            rbtnNam.setChecked(true);
        } else {
            rbtnNu.setChecked(true);
        }
        if (sinhVien.getImageResource() != null) {
            byte[] imgdata = new byte[0];
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                imgdata = Base64.getDecoder().decode(sinhVien.getImageResource());
            }
            Bitmap bitmap = BitmapFactory.decodeByteArray(imgdata, 0, imgdata.length);
            imgAvatar.setImageBitmap(bitmap);
        } else {
            Toast.makeText(MainThongTinSVGV.this, "Không có thông tin hình ảnh", Toast.LENGTH_SHORT).show();
        }
    }

    public void setControl() {
        btnCapnhat = findViewById(R.id.buttonCapNhat);
        imgAvatar = findViewById(R.id.imageViewHinhAnh);
        edtDiaChi = findViewById(R.id.editTextDiaChi);
        edtMasv = findViewById(R.id.editTextMaSV);
        edtHo = findViewById(R.id.editTextHo);
        edtTen = findViewById(R.id.editTextTen);
        edtNgaysinh = findViewById(R.id.editTextNgaySinh);
        edtLop = findViewById(R.id.editTextLop);
        rbtnNam = findViewById(R.id.radioButtonNam);
        rbtnNu = findViewById(R.id.radioButtonNu);
        imgAvatar = findViewById(R.id.imageViewHinhAnh);
        btnSuaAnh = findViewById(R.id.buttonSuaAnh);

        accountSharedPref = getSharedPreferences("Account", Context.MODE_PRIVATE);
        String token = accountSharedPref.getString("token", "");
        Retrofit retrofit = ApiClient.getClient(token);
        iThongKe = retrofit.create(IThongKe.class);
    }

    public void setEvent() {
        btnSuaAnh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImageFromGallery();
            }
        });
        btnCapnhat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SinhVien sinhVien = new SinhVien();
                sinhVien.setMasv(edtMasv.getText().toString());
                sinhVien.setDiachi(edtDiaChi.getText().toString());
                if (selectedImageUri != null) {
                    File file = new File(selectedImageUri.getPath());
                    sinhVien.setHinhanh(file.getName()+".jpg");
                    String uri = selectedImageUri.toString();
                    System.out.println(uri);
                    try {
                        InputStream iStream = getContentResolver().openInputStream(selectedImageUri);
                        byte[] imgSend = getBytes(iStream);
                        String base64 = null;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            base64 = Base64.getEncoder().encodeToString(imgSend);
                        }
                        sinhVien.setImageResource(base64);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
                Log.e("checksinhvien", sinhVien.toString());
                iThongKe.updateSinhVien(sinhVien).enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        Toast.makeText(MainThongTinSVGV.this,"Cap nhat thanh cong",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable throwable) {
                        Log.e("checksinhvien", throwable.getMessage());
                        onResume();
                    }
                });
            }
        });
    }

    public void pickImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        Intent chooser = Intent.createChooser(intent, "Chọn ảnh từ");

        // Kiểm tra xem có ứng dụng nào có thể xử lý intent không
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(chooser, REQUEST_CODE_PICK_IMAGE);
        } else {
            // Xử lý khi không có ứng dụng nào có thể xử lý intent
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.getData();
            Toast.makeText(MainThongTinSVGV.this, "Lay anh thanh cong", Toast.LENGTH_SHORT).show();
            imgAvatar.setImageURI(selectedImageUri);
        }
    }

    //    public byte[] convertImageUriToByteArray(String imageUri) throws IOException {
//        URL url = new URL(imageUri);
//        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//        try {
//            connection.setDoInput(true);
//            connection.connect();
//
//            try (InputStream input = connection.getInputStream();
//                 ByteArrayOutputStream output = new ByteArrayOutputStream()) {
//
//                byte[] buffer = new byte[4096];
//                int bytesRead;
//                while ((bytesRead = input.read(buffer)) != -1) {
//                    output.write(buffer, 0, bytesRead);
//                }
//                return output.toByteArray();
//            }
//        } finally {
//            connection.disconnect();
//        }
//    }
    public byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }
}