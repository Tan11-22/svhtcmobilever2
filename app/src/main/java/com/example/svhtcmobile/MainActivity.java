package com.example.svhtcmobile;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.example.svhtcmobile.Api.ApiClient;
import com.example.svhtcmobile.Api.apiService.ILoginService;
import com.example.svhtcmobile.Controller.CTDTController;
import com.example.svhtcmobile.Controller.DKLopTinChiMain;
import com.example.svhtcmobile.Controller.DanhSachHeDaoTao;
import com.example.svhtcmobile.Controller.DanhSachHocPhi;
import com.example.svhtcmobile.Controller.DanhSachLop;
//import com.example.svhtcmobile.Controller.DanhSachLopTinChi;
import com.example.svhtcmobile.Controller.DanhSachMonHocGV;
import com.example.svhtcmobile.Controller.DanhSachNganh;
//import com.example.svhtcmobile.Controller.DoiMatKhauActivity;
//import com.example.svhtcmobile.Controller.HocPhiController;
import com.example.svhtcmobile.Controller.DoiMatKhauActivity;
import com.example.svhtcmobile.Controller.HocPhiSinhVienMain;
import com.example.svhtcmobile.Controller.HocPhiSinhVienView;
import com.example.svhtcmobile.Controller.KhoaController;
import com.example.svhtcmobile.Controller.MainQuanTriGiangVien;
//import com.example.svhtcmobile.Controller.MainQuanTriMonHoc;
import com.example.svhtcmobile.Controller.MainQuanTriMonHoc;
import com.example.svhtcmobile.Controller.MainQuanTriSinhVien;
import com.example.svhtcmobile.Controller.MainThongTinSVGV;
import com.example.svhtcmobile.Controller.QuanTriLTC;
//import com.example.svhtcmobile.Controller.QuenMatKhauActivity;
import com.example.svhtcmobile.Controller.QuenMatKhauActivity;
import com.example.svhtcmobile.Controller.TaiKhoanController;
import com.example.svhtcmobile.Controller.XemDiem;
import com.example.svhtcmobile.Model.ApiResponse;
import com.example.svhtcmobile.Model.UserInfo;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

    public static boolean checkDoiMK = false;
    ImageView ivDangNhap;
    private boolean passwordShowing;
    private boolean loginStatus = false;
    private UserInfo userInfo;
    SharedPreferences accountSharedPref;
    TextView tvTen, tvMa, tvChuaDangNhap, tvDangNhap;


    LinearLayout llGV , llSV, llChuaDN;
    ImageView ivKhoa, ivAccount , ivLTC, ivHP, ivMonHoc, ivSinhVien, ivGiangVien,ivDoiMatKhau,
            ivHinhAnh,ivHPGV,ivXemDiem, ivNhapDiem, ivLopTinChi, ivLop, ivHe, ivNganh, ivCTDT;

    ILoginService iLoginService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setControl();
        setEvent();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(checkDoiMK){
            logout();
        }
    }

    private void setEvent() {
        ivDangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!loginStatus){
                    showDialogLogin();
                } else {
                    logout();
                }
            }
        });
//        ivKhoa.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(MainActivity.this, KhoaController.class));
//            }
//        });

        ivSinhVien.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, MainQuanTriSinhVien.class));
            }
        });
        ivDoiMatKhau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, DoiMatKhauActivity.class));
            }
        });
        ivGiangVien.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, MainQuanTriGiangVien.class));
            }
        });
        ivLTC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, DKLopTinChiMain.class));
            }
        });
        ivHP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, HocPhiSinhVienView.class));
            }
        });
        ivMonHoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, MainQuanTriMonHoc.class));
            }
        });
//        ivAccount.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(MainActivity.this, TaiKhoanController.class));
//            }
//        });
        ivHinhAnh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(loginStatus){
                    startActivity(new Intent(MainActivity.this, MainThongTinSVGV.class));
                }
            }
        });
        ivHPGV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, DanhSachHocPhi.class));
            }
        });
        ivXemDiem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, XemDiem.class));
            }
        });
        ivNhapDiem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, DanhSachMonHocGV.class));
            }
        });
        ivLopTinChi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, QuanTriLTC.class));
            }
        });
        ivLop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, DanhSachLop.class));
            }
        });
//        ivHe.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(MainActivity.this, DanhSachHeDaoTao.class));
//            }
//        });
//        ivNganh.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(MainActivity.this, DanhSachNganh.class));
//            }
//        });
//
//        ivCTDT.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(MainActivity.this, CTDTController.class));
//            }
//        });
    }

    private void setControl() {
        ivDangNhap = findViewById(R.id.ivDangNhap1);
        tvTen = findViewById(R.id.tvTen);
        tvMa = findViewById(R.id.tvMa);
        tvChuaDangNhap = findViewById(R.id.tvChuaDangNhap);
        tvDangNhap = findViewById(R.id.tvDangNhap);
//        ivKhoa = findViewById(R.id.ivKhoa);
//        ivAccount = findViewById(R.id.ivAccount);
        ivLTC = findViewById(R.id.ivLTC);
        ivHP=findViewById(R.id.ivHP);
        ivMonHoc=findViewById(R.id.ivMonHoc);
        ivSinhVien=findViewById(R.id.ivSinhVien);
        ivGiangVien=findViewById(R.id.ivGiangVien);
        ivDoiMatKhau=findViewById(R.id.ivDoiMatKhau);
        ivHinhAnh=findViewById(R.id.ivHinhAnh);
        ivHPGV=findViewById(R.id.ivHPGV);
        ivXemDiem = findViewById(R.id.ivXemDiem);
        ivNhapDiem = findViewById(R.id.ivNhapDiem);
        ivLopTinChi = findViewById(R.id.ivLopTinChi);
        ivLop = findViewById(R.id.ivLop);
//        ivHe = findViewById(R.id.ivHe);
//        ivNganh = findViewById(R.id.ivNganh);
//        ivCTDT = findViewById(R.id.ivCTDT);
        llGV = findViewById(R.id.llGV);
        llSV = findViewById(R.id.llSV);
        llChuaDN = findViewById(R.id.llChuaDN);
        Retrofit retrofit = ApiClient.getClient("");
        iLoginService = retrofit.create(ILoginService.class);
    }

    private void showDialogLogin() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.activity_login);

        Window window = dialog.getWindow();
        if(window == null) return;

        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAtrributes = window.getAttributes();
        windowAtrributes.gravity = Gravity.CENTER;
        window.setAttributes(windowAtrributes);

        passwordShowing = false;

        EditText edtUsername = dialog.findViewById(R.id.edtUsername);
        EditText edtPassword = dialog.findViewById(R.id.edtPassword);
        ImageView passwordIcon = dialog.findViewById(R.id.passwordIcon);
        Button btnDangNhap = dialog.findViewById(R.id.btnDangNhap);
        TextView tvQuenMatKhau = dialog.findViewById(R.id.tvQuenMatKhau);
        tvQuenMatKhau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, QuenMatKhauActivity.class));

            }
        });


        passwordIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (passwordShowing) {
                    passwordShowing = false;
                    edtPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    passwordIcon.setImageResource(R.drawable.password_show);
                } else {
                    passwordShowing = true;
                    edtPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    passwordIcon.setImageResource(R.drawable.password_hide);
                }
                edtPassword.setSelection(edtPassword.length());
            }
        });

        btnDangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = edtUsername.getText().toString().trim();
                String password = edtPassword.getText().toString().trim();
                Map<String, String> data = new HashMap<>();
                data.put("username", username);
                data.put("password", password);
                iLoginService.login(data).enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        if (response.code() == 200) {
                            JsonObject jsonObject= response.body();
                            userInfo = mapJsonObjectToUI(jsonObject);

                            saveTokenAccount(username, jsonObject.get("token").getAsString(),jsonObject.get("role").getAsString());
                            if(jsonObject.get("role").getAsString().equals("GIANGVIEN")){
                                            llGV.setVisibility(View.VISIBLE);
                            } else if (jsonObject.get("role").getAsString().equals(("SINHVIEN"))){
                                            llSV.setVisibility(View.VISIBLE);
                            }
                            llChuaDN.setVisibility(View.GONE);

                            Retrofit retrofit = ApiClient.getClient(jsonObject.get("token").getAsString());
                            iLoginService = retrofit.create(ILoginService.class);
                            iLoginService.getThongTin(username,jsonObject.get("role").getAsString()).enqueue(new Callback<ApiResponse<JsonObject>>() {
                                @Override
                                public void onResponse(Call<ApiResponse<JsonObject>> call, Response<ApiResponse<JsonObject>> response) {
                                    ApiResponse<JsonObject> apiResponse = response.body();
                                    if (apiResponse.getCode()==200){
                                        JsonObject jO1 = apiResponse.getData();
                                        String ho = jO1.get("HO").getAsString();
                                        String ten = jO1.get("TEN").getAsString();
                                        userInfo.setHo(ho);
                                        userInfo.setTen(ten);
                                        saveNameAccount(ho,ten);
                                        showInfo(userInfo);
                                    }
                                }

                                @Override
                                public void onFailure(Call<ApiResponse<JsonObject>> call, Throwable throwable) {

                                }
                            });

//                            iLoginService.getInfo(username).enqueue(new Callback<JsonObject>() {
//                                @Override
//                                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
//                                    if(response.code() == 200) {
//                                        JsonObject jsonObject = response.body();
//                                        userInfo = mapJsonObjectToUI(jsonObject);
//                                        saveNameAccountandRole(userInfo.getHo(), userInfo.getTen(), userInfo.getTenQuyen());
//                                        showInfo(userInfo);
//                                        if(userInfo.getTenQuyen().equals("GIANGVIEN")){
//                                            llGV.setVisibility(View.VISIBLE);
//                                        } else if (userInfo.getTenQuyen().equals(("SINHVIEN"))){
//                                            llSV.setVisibility(View.VISIBLE);
//                                        }
//                                        llChuaDN.setVisibility(View.GONE);
//                                    }
//
//                                }
//                                @Override
//                                public void onFailure(Call<JsonObject> call, Throwable throwable) {
//                                    Toast.makeText(MainActivity.this,"Đăng nhập thất bại!", Toast.LENGTH_SHORT).show();
//                                }
//                            });
                            dialog.dismiss();
                        } else if(response.code()==400){
                            Toast.makeText(MainActivity.this,"Tài khoản của bạn không hoạt động!", Toast.LENGTH_SHORT).show();
                        } else
                        {
                            Toast.makeText(MainActivity.this,"Đăng nhập thất bại!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable throwable) {
                        Toast.makeText(MainActivity.this,"Đăng nhập thất bại!" + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        dialog.setCancelable(true);
        dialog.show();
    }

    private UserInfo mapJsonObjectToUI(JsonObject jsonObject) {
        UserInfo ui = new UserInfo();
        ui.setUsername(jsonObject.get("user").getAsString());
//        ui.setHo(jsonObject.get("ho").getAsString());
//        ui.setTen(jsonObject.get("ten").getAsString());
        ui.setTenQuyen(jsonObject.get("role").getAsString());
//        ui.setIdQuyen(jsonObject.get("idQuyen").getAsInt());
        return ui;
    }
    private void showInfo(UserInfo userInfo) {

        tvTen.setText(userInfo.getHo() + " "+userInfo.getTen());
        tvMa.setText("Mã: "+ userInfo.getUsername()+ " - "+userInfo.getTenQuyen());
        tvTen.setVisibility(View.VISIBLE);
        tvMa.setVisibility(View.VISIBLE);
        tvChuaDangNhap.setVisibility(View.GONE);
        tvDangNhap.setText("Đăng xuất");
        ivDangNhap.setImageResource(R.drawable.logout);
        loginStatus = true;
        ivDoiMatKhau.setVisibility(View.VISIBLE);
    }

    private void logout() {
        loginStatus = false;
        tvTen.setText("");
        tvMa.setText("");
        tvTen.setVisibility(View.GONE);
        tvMa.setVisibility(View.GONE);
        tvChuaDangNhap.setVisibility(View.VISIBLE);
        tvDangNhap.setText("Đăng nhập");
        ivDangNhap.setImageResource(R.drawable.login);
        if(userInfo.getTenQuyen().equals("GIANGVIEN")) {
            llGV.setVisibility(View.GONE);
        } else if(userInfo.getTenQuyen().equals("SINHVIEN")) {
            llSV.setVisibility(View.GONE);
        }
        llChuaDN.setVisibility(View.VISIBLE);
        userInfo = null;
        ivDoiMatKhau.setVisibility(View.GONE);
        clearSharedPref(accountSharedPref);
    }

    private void saveTokenAccount(String username, String token, String quyen){
        accountSharedPref = getSharedPreferences("Account", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = accountSharedPref.edit();
        editor.putString("username", username);
        editor.putString("token", token);
        editor.putString("quyen", quyen);
        editor.apply();
    }
    private void saveNameAccount(String ho, String ten){
        accountSharedPref = getSharedPreferences("Account", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = accountSharedPref.edit();
        editor.putString("ho", ho);
        editor.putString("ten", ten);
        editor.apply();
    }

    private void clearSharedPref(SharedPreferences pref){
        pref.edit().clear().apply();
    }


}