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

import java.util.HashMap;
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

import com.example.svhtcmobile.Api.ApiClient;
import com.example.svhtcmobile.Api.apiService.ILoginService;
import com.example.svhtcmobile.Api.apiService.ILopService;
import com.example.svhtcmobile.Api.apiService.IQuanTriThongTin;
import com.example.svhtcmobile.MainActivity;
import com.example.svhtcmobile.Model.ApiResponse;
import com.example.svhtcmobile.Model.GiangVien;
import com.example.svhtcmobile.Model.SinhVien;
import com.example.svhtcmobile.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class DoiMatKhauActivity extends AppCompatActivity {
    EditText edtMK0 ,edtMK1,edtMK2;
    Button btnHuy ,btnLuu;


    SharedPreferences accountSharedPref;
    ILoginService iLoginService;
    String username;

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

        edtMK0 = findViewById(R.id.edtMK0);
        edtMK1 = findViewById(R.id.edtMK1);
        edtMK2 = findViewById(R.id.edtMK2);
        btnHuy = findViewById(R.id.btnHuyDMK);
        btnLuu = findViewById(R.id.btnLuuDMK);

        accountSharedPref = getSharedPreferences("Account", Context.MODE_PRIVATE);
        String token = accountSharedPref.getString("token", "");
        username = accountSharedPref.getString("username", "");
        String quyen = accountSharedPref.getString("quyen", "");

        Retrofit retrofit = ApiClient.getClient(token);
        iLoginService = retrofit.create(ILoginService.class);
//        layEmail(username,quyen);
//        btnLayMa.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                otp = randomOTP();
//                sendEmail(email,otp);
//            }
//        });

        btnLuu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mk = edtMK0.getText().toString().trim();
                String mk1 = edtMK1.getText().toString().trim();
                String mk2 = edtMK2.getText().toString().trim();
                if (!mk1.equals(mk2)) {
                    Toast.makeText(DoiMatKhauActivity.this,"Nhập lại mật khẩu mới không chính xác!", Toast.LENGTH_SHORT).show();
                    return;
                }
                Map<String, String> data = new HashMap<>();
                data.put("username", username);
                data.put("password",mk);
                data.put("newpassword", mk1);

                iLoginService.doiMatKhau(data).enqueue(new Callback<ApiResponse<Object>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<Object>> call, Response<ApiResponse<Object>> response) {
                        ApiResponse<Object> apiResponse = response.body();
                        Toast.makeText(DoiMatKhauActivity.this,apiResponse.getStatus(), Toast.LENGTH_SHORT).show();
                        if(apiResponse.getCode() == 200) {
                            MainActivity.checkDoiMK = true;
                            finish();
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<Object>> call, Throwable throwable) {

                    }
                });
            }
        });
        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void clearSharedPref(SharedPreferences pref){
        pref.edit().clear().apply();
    }

}
