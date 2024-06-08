package com.example.svhtcmobile.Controller;

import static com.example.svhtcmobile.Controller.Function.layHocKyHienTai;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.example.svhtcmobile.Api.ApiClient;
import com.example.svhtcmobile.Api.apiService.ISinhVien;
import com.example.svhtcmobile.Model.SinhVien;
import com.paypal.checkout.approve.Approval;
import com.paypal.checkout.approve.OnApprove;
import com.paypal.checkout.createorder.CreateOrder;
import com.paypal.checkout.createorder.CreateOrderActions;
import com.paypal.checkout.createorder.CurrencyCode;
import com.paypal.checkout.createorder.OrderIntent;
import com.paypal.checkout.createorder.UserAction;
import com.paypal.checkout.order.Amount;
import com.paypal.checkout.order.AppContext;
import com.paypal.checkout.order.CaptureOrderResult;
import com.paypal.checkout.order.OnCaptureComplete;
import com.paypal.checkout.order.OrderRequest;
import com.paypal.checkout.order.PurchaseUnit;
import com.paypal.checkout.paymentbutton.PaymentButtonContainer;

import com.example.svhtcmobile.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class ChiTietThanhToanMain extends AppCompatActivity {
    private Button btnThanhToan, btnTroVe;
    private ImageButton imgBtnBack;
    private TextView tvTTHoTen, tvTTMSSV,tvTTMaLop;
    private TextView tvMaSV, tvMaLop, tvNienKhoa, tvHocKy, tvSoTienDong;
    private TableRow paymentRow;
    private Spinner spHinhThuc;
    private ImageView ivHinh;
    private String maSV, maLop, hoTen;
    private String nienKhoa;
    private int hocKy, soTienDong;
    private List<String> hinhThucData = new ArrayList<>();
    private ArrayAdapter adapter_lht;
    //paypal
    private PaymentButtonContainer paymentButtonContainer ;
    SharedPreferences accountSharedPref;
    ISinhVien iSinhVien;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_thanh_toan_main);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        init();
        setEvent();
    }
    @Override
    protected void onResume() {
        super.onResume();
        paymentRow.setVisibility(View.GONE);
        btnThanhToan.setVisibility(View.GONE);
    }

    public void init(){
        paymentButtonContainer=findViewById(R.id.payment_button_container);
        btnThanhToan=findViewById(R.id.btnThanhToan);
        btnTroVe=findViewById(R.id.btnTroVe);
        imgBtnBack= findViewById(R.id.imgBtnBack);
        tvTTHoTen=findViewById(R.id.tvTTHoTen);
        tvTTMSSV=findViewById(R.id.tvTTMSSV);
        tvTTMaLop=findViewById(R.id.tvTTMaLop);
        tvMaLop=findViewById(R.id.tvMaLop);
        tvMaSV = findViewById(R.id.tvMaSV);
        tvNienKhoa=findViewById(R.id.tvNienKhoa);
        tvHocKy=findViewById(R.id.tvHocKy);
        tvSoTienDong=findViewById(R.id.tvSoTienDong);
        spHinhThuc=findViewById(R.id.spHinhThuc);
        ivHinh=findViewById(R.id.ivHinh);
        paymentRow=findViewById(R.id.payment_row);
        hinhThucData.add("paypal");
        paymentRow.setVisibility(View.GONE);
        btnThanhToan.setVisibility(View.GONE);
        accountSharedPref = getSharedPreferences("Account", Context.MODE_PRIVATE);
        String token = accountSharedPref.getString("token", "");
        String ho = accountSharedPref.getString("ho", "");
        String ten = accountSharedPref.getString("ten", "");
        String username = accountSharedPref.getString("username", "");
        String quyen = accountSharedPref.getString("quyen", "");
        Retrofit retrofit = ApiClient.getClient(token);
        iSinhVien = retrofit.create(ISinhVien.class);

        //Set thông tin sinh viên
        maSV=username;
        hoTen=ho+" "+ten;
        DocSV();
        tvTTHoTen.setText(hoTen);
        tvTTMSSV.setText(maSV);
    }
    public void DocSV(){
        iSinhVien.timSV(maSV).enqueue(new Callback<SinhVien>() {
            @Override
            public void onResponse(Call<SinhVien> call, Response<SinhVien> response) {
                SinhVien sinhVien=response.body();
                maLop=sinhVien.getMalop();
                tvTTMaLop.setText(maLop);

            }

            @Override
            public void onFailure(Call<SinhVien> call, Throwable throwable) {
                System.out.println(throwable.getMessage().toString());
                Toast.makeText(ChiTietThanhToanMain.this, throwable.getMessage().toString(),Toast.LENGTH_SHORT).show();
            }
        });
    }
    public float convertVNDToUSD(int soTien){
        float tyThoi= 25000.00F;//VND
        return soTien/tyThoi;
    }
    public void setEvent(){
        nienKhoa = (String) getIntent().getSerializableExtra("nienKhoa");
        hocKy= Integer.parseInt((String)getIntent().getSerializableExtra("hocKy"));
        soTienDong=Integer.parseInt((String)getIntent().getSerializableExtra("soTienDong"));
        maSV=(String) getIntent().getSerializableExtra("ttMSSV");;
        maLop=(String) getIntent().getSerializableExtra("ttMaLop");
        tvTTHoTen.setText((String) getIntent().getSerializableExtra("ttHoTen"));
        tvTTMSSV.setText(maSV);
        tvTTMaLop.setText(maLop);
        tvMaSV.setText(maSV);
        tvMaLop.setText(maLop);
        tvNienKhoa.setText(nienKhoa);
        tvHocKy.setText(String.valueOf(hocKy));
        tvSoTienDong.setText(String.valueOf(soTienDong));


        adapter_lht = new ArrayAdapter(this, android.R.layout.simple_list_item_1, hinhThucData);
        spHinhThuc.setAdapter(adapter_lht);
        spHinhThuc.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(spHinhThuc.getSelectedItem().equals("paypal")){

                    ivHinh.setImageResource(R.drawable.paypal);
                    paymentRow.setVisibility(View.VISIBLE);
                    btnThanhToan.setVisibility(View.GONE);
                    requestPaypal(String.valueOf(convertVNDToUSD(soTienDong)));//gia
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        imgBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        btnTroVe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        btnThanhToan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(spHinhThuc.getSelectedItem().toString().equals("zalo")){
                    Toast.makeText(ChiTietThanhToanMain.this,"Đang bảo trì",Toast.LENGTH_SHORT).show();
//                    requestZalo(gia);
                }
            }
        });

    }
    public void dongHocPhi(){
        iSinhVien.dongHocPhi(maSV,nienKhoa,hocKy,soTienDong).enqueue(new Callback<Map<String, String>>() {
            @Override
            public void onResponse(Call<Map<String, String>> call, retrofit2.Response<Map<String, String>> response) {
                Map<String,String> result=response.body();
                String status = result.get("status");
                String message = result.get("message");
                if(status.equals("1")){
                    Toast.makeText(ChiTietThanhToanMain.this, "Thêm dữ liệu thanh toán thành công", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(ChiTietThanhToanMain.this, message, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Map<String, String>> call, Throwable t) {
                Toast.makeText(ChiTietThanhToanMain.this, "Lỗi: "+t.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void requestPaypal(String gia){
        paymentButtonContainer.setup(
                new CreateOrder() {
                    @Override
                    public void create(@NotNull CreateOrderActions createOrderActions) {
                        ArrayList<PurchaseUnit> purchaseUnits = new ArrayList<>();
                        purchaseUnits.add(
                                new PurchaseUnit.Builder()
                                        .amount(
                                                new Amount.Builder()
                                                        .currencyCode(CurrencyCode.USD)
                                                        .value(gia)
                                                        .build()
                                        )
                                        .build()
                        );
                        OrderRequest order = new OrderRequest(
                                OrderIntent.CAPTURE,
                                new AppContext.Builder()
                                        .userAction(UserAction.PAY_NOW)
                                        .build(),
                                purchaseUnits
                        );
                        createOrderActions.create(order, (CreateOrderActions.OnOrderCreated) null);
                    }
                },
                new OnApprove() {
                    @Override
                    public void onApprove(@NotNull Approval approval) {
                        approval.getOrderActions().capture(new OnCaptureComplete() {
                            @Override
                            public void onCaptureComplete(@NotNull CaptureOrderResult result) {
                                Toast.makeText(ChiTietThanhToanMain.this,"Thanh toán thành công",Toast.LENGTH_SHORT).show();
                                dongHocPhi();
                                Log.i("CaptureOrder", String.format("CaptureOrderResult: %s", result));
                                onBackPressed();
                            }
                        });
                    }
                }
        );
    }
}