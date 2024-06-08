package com.example.svhtcmobile.Controller;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


import java.util.Map;
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

import com.example.svhtcmobile.Adapter.CustomAdapterKhoa;
import com.example.svhtcmobile.Api.ApiClient;
import com.example.svhtcmobile.Api.apiService.ILoginService;
import com.example.svhtcmobile.Api.apiService.IQuanTriThongTin;
import com.example.svhtcmobile.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class QuenMatKhauActivity extends AppCompatActivity {
    EditText edtEmailKhoiPhuc ,edtOTP, edtNewPassword,edtConfirmPassword;
    Button btnHuyKP , btnTiepTucKP ,btnHuyKP2 ,btnLuuKP;
    String opt;
    String email;

    String username;

    IQuanTriThongTin iQuanTriThongTin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quen_mat_khau);
        setControl();
        setEvent();
    }

    private void setControl() {
        edtEmailKhoiPhuc = findViewById(R.id.edtEmailKhoiPhuc);
        btnTiepTucKP = findViewById((R.id.btnTiepTucKP));
        btnHuyKP = findViewById(R.id.btnHuyKP);

        Retrofit retrofit = ApiClient.getClient("");
        iQuanTriThongTin = retrofit.create(IQuanTriThongTin.class);
    }
    private void setEvent() {
        btnTiepTucKP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = edtEmailKhoiPhuc.getText().toString();
                iQuanTriThongTin.taiKhoanByEmail(email).enqueue(new Callback<Map<String, Object>>() {
                    @Override
                    public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                        if (response.isSuccessful()) {
                            Map<String, Object> responseBody = response.body();
                            if (responseBody != null && responseBody.containsKey("ID")) {
                                username = responseBody.get("ID").toString();
                                 opt = randomOTP();
                                 sendEmail(email, opt);
                                Toast.makeText(QuenMatKhauActivity.this, "Email Hợp Lệ", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(QuenMatKhauActivity.this, "Email không tồn tại", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(QuenMatKhauActivity.this, "Có lỗi xảy ra khi kiểm tra email!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                        // Xử lý khi có lỗi xảy ra
                        Log.e("checkloiquenpass", t.getMessage());
                        Toast.makeText(QuenMatKhauActivity.this, "Có lỗi xảy ra khi kiểm tra email!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        btnHuyKP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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
                    message.setSubject("Mã OTP khôi phục");
                    message.setText("Mã OTP khôi phục mật khẩu của bạn là: " + OTP);
                    Transport.send(message);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(QuenMatKhauActivity.this, "Đã gửi mã đến email.", Toast.LENGTH_SHORT).show();
                            showPasswordInputDialog();
                        }
                    });
                } catch (MessagingException e) {
                    e.printStackTrace();
                    final String errorMessage = e.getMessage(); // Lấy thông điệp lỗi từ exception
                    Log.e("EmailError", "Failed to send email", e);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(QuenMatKhauActivity.this, "Failed to send email: " + errorMessage, Toast.LENGTH_SHORT).show();

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
    private void showPasswordInputDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.cap_nhat_lai_mat_khau, null);
        builder.setView(view);
        edtOTP = view.findViewById(R.id.edtNhapMa);
        edtNewPassword = view.findViewById(R.id.edtMKLan1);
        edtConfirmPassword = view.findViewById(R.id.edtMKLan2);
        Button btnSave = view.findViewById(R.id.btnLuuKP);
        Button btnCancel = view.findViewById(R.id.btnHuyKP2);

        AlertDialog dialog = builder.create();
        dialog.show();

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newPassword = edtNewPassword.getText().toString();
                String confirmPassword = edtConfirmPassword.getText().toString();
                if (edtOTP.getText().toString().equals(opt)) {
                    if (newPassword.equals(confirmPassword)) {

                        iQuanTriThongTin.doiMatKhau(username,newPassword).enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                if (response.isSuccessful()) {
                                    Toast.makeText(QuenMatKhauActivity.this, "Mật khẩu đã được lưu.", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                    finish();
                                } else {
                                    Toast.makeText(QuenMatKhauActivity.this, "Có vấn đề! Mật khẩu chưa được lưu.", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                // Xử lý khi có lỗi xảy ra
                                Log.e("checkloiquenpass",t.getMessage());
                            }
                        });


                    } else {
                        Toast.makeText(QuenMatKhauActivity.this, "Mật khẩu không khớp. Vui lòng nhập lại.", Toast.LENGTH_SHORT).show();
                    }
                }
                else Toast.makeText(QuenMatKhauActivity.this, "Mã OTP không chính xác.", Toast.LENGTH_SHORT).show();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }
}
