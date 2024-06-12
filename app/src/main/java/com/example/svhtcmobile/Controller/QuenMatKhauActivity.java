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
import com.example.svhtcmobile.Model.ApiResponse;
import com.example.svhtcmobile.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class QuenMatKhauActivity extends AppCompatActivity {
    EditText edtEmailKhoiPhuc;
    EditText edtTaiKhoan;
    Button btnHuyKP, btnTiepTucKP;

    String email;

    String username;

    IQuanTriThongTin iQuanTriThongTin;
    ILoginService iLoginService;

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
        edtTaiKhoan = findViewById(R.id.edtTaiKhoan);
        Retrofit retrofit = ApiClient.getClient("");
        iQuanTriThongTin = retrofit.create(IQuanTriThongTin.class);
        iLoginService = retrofit.create(ILoginService.class);
    }

    private void setEvent() {
        btnTiepTucKP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = edtEmailKhoiPhuc.getText().toString();
                username = edtTaiKhoan.getText().toString().toString();
                iLoginService.quenMatKhau(username,email).enqueue(new Callback<ApiResponse<Object>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<Object>> call, Response<ApiResponse<Object>> response) {
                        ApiResponse<Object> apiResponse = response.body();
                        Toast.makeText(QuenMatKhauActivity.this,apiResponse.getStatus(),Toast.LENGTH_SHORT).show();
                        if(apiResponse.getCode()==200) {
                            finish();
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<Object>> call, Throwable throwable) {

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

}