package com.example.svhtcmobile.Controller;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Properties;
import java.util.Random;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.example.svhtcmobile.Api.ApiClient;
import com.example.svhtcmobile.Api.apiService.IQuanTriThongTin;
import com.example.svhtcmobile.Model.GiangVien;
import com.example.svhtcmobile.Model.SinhVien;
import com.example.svhtcmobile.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class DoiMatKhauActivity extends AppCompatActivity {
    EditText edtMK0 ,edtMK1,edtMK2,edtOTP;
    Button btnHuy ,btnLuu,btnLayMa;
    String otp;
    String email;


    SharedPreferences accountSharedPref;
    IQuanTriThongTin iQuanTriThongTin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doi_mat_khau);
        setControl();
        setEvent();
    }

    private void setEvent() {
        SinhVien sinhVien ;


    }

    private void setControl() {

//        edtMK0 = findViewById(R.id.edtMK0);
        edtMK1 = findViewById(R.id.edtMK1);
        edtMK2 = findViewById(R.id.edtMK2);
        btnHuy = findViewById(R.id.btnHuyDMK);
        btnLuu = findViewById(R.id.btnLuuDMK);
        edtOTP = findViewById(R.id.edtMaOTP);
        btnLayMa = findViewById(R.id.btnLayMa);
        accountSharedPref = getSharedPreferences("Account", Context.MODE_PRIVATE);
        String token = accountSharedPref.getString("token", "");
        String username = accountSharedPref.getString("username", "");
        String quyen = accountSharedPref.getString("quyen", "");

        Retrofit retrofit = ApiClient.getClient(token);
        iQuanTriThongTin = retrofit.create(IQuanTriThongTin.class);
        layEmail(username,quyen);
        btnLayMa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                otp = randomOTP();
                sendEmail(email,otp);
            }
        });

        btnLuu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                String MK0 = edtMK0.getText().toString();
                String MK1 = edtMK1.getText().toString();
                String MK2 = edtMK2.getText().toString();
                String maOPT = edtOTP.getText().toString();
                if (MK1.equals("")||MK2.equals("")||maOPT.equals("")) {
                    Toast.makeText(DoiMatKhauActivity.this, "Không để trống dữ liệu", Toast.LENGTH_SHORT).show();
                }
                else {
//                    if (MK0.equals(password)) {
                        if (MK1.equals(MK2)) {
                            if (maOPT.equals(otp)) {
                                iQuanTriThongTin.doiMatKhau(username,MK1).enqueue(new Callback<Void>() {
                                    @Override
                                    public void onResponse(Call<Void> call, Response<Void> response) {
                                        if (response.isSuccessful()) {
                                            Toast.makeText(DoiMatKhauActivity.this, "Đổi mật khẩu thành công", Toast.LENGTH_SHORT).show();
//
                                            finish();
                                        } else {
                                            Toast.makeText(DoiMatKhauActivity.this, "Có vấn đề! Mật khẩu chưa được lưu.", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<Void> call, Throwable t) {
                                        // Xử lý khi có lỗi xảy ra
                                    }
                                });
                            } else
                                Toast.makeText(DoiMatKhauActivity.this, "Mã OTP nhập sai!", Toast.LENGTH_SHORT).show();
                        } else
                            Toast.makeText(DoiMatKhauActivity.this, "Mật khẩu mới nhập lại chưa khớp!", Toast.LENGTH_SHORT).show();
                    }
            }
        });
        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    public String layEmail(String masv, String quyen)
    {

        if (quyen.equals("SINHVIEN"))
        {
        iQuanTriThongTin.timSinhVien(masv.trim()).enqueue(new Callback<SinhVien>() {
            @Override
            public void onResponse(Call<SinhVien> call, Response<SinhVien> response) {
                if (response.isSuccessful()) {
                    SinhVien sinhVien = response.body();
                    email = sinhVien.getEmail();

                } else {
                    Toast.makeText(DoiMatKhauActivity.this, "Có vấn đề!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SinhVien> call, Throwable t) {
                // Xử lý khi có lỗi xảy ra
            }
        });}

        else
        {
            iQuanTriThongTin.timGiangVien(masv.trim()).enqueue(new Callback<GiangVien>() {
                @Override
                public void onResponse(Call<GiangVien> call, Response<GiangVien> response) {
                    if (response.isSuccessful()) {
                        GiangVien giangVien = response.body();
                        email = giangVien.getEmail();

                    } else {
                        Toast.makeText(DoiMatKhauActivity.this, "Có vấn đề!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<GiangVien> call, Throwable t) {
                    // Xử lý khi có lỗi xảy ra
                }
            });
        }
        return email;
    }
    public void sendEmail(String email, String OTP) {
        final String username = "baternguyen.2407@gmail.com";
        final String password = "sttw uafa ttke cbzd";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.ssl.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "465");

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    MimeMessage message = new MimeMessage(session);
                    message.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
                    message.setSubject("Mã OTP ");
                    message.setText("Mã OTP của bạn là: " + OTP);
                    Transport.send(message);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(DoiMatKhauActivity.this, "Đã gửi mã đến email.", Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (MessagingException e) {
                    e.printStackTrace();
                    final String errorMessage = e.getMessage(); // Lấy thông điệp lỗi từ exception
                    Log.e("EmailError", "Gửi mail thất bại!", e);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(DoiMatKhauActivity.this, "Gửi mail thất bại!" + errorMessage, Toast.LENGTH_SHORT).show();

                        }
                    });
                }
            }
        }).start();
    }




    public static String randomOTP() {
        // Khởi tạo một đối tượng Random
        Random rand = new Random();

        // Tạo một chuỗi gồm 6 chữ số
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            // Sinh một chữ số ngẫu nhiên từ 0 đến 9 và thêm vào chuỗi
            int digit = rand.nextInt(10);
            sb.append(digit);
        }
        String out = sb.toString();

        return out;
    }
}
