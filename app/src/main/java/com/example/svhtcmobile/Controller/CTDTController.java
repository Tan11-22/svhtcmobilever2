package com.example.svhtcmobile.Controller;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.svhtcmobile.Api.ApiClient;
import com.example.svhtcmobile.R;
import com.example.svhtcmobile.Adapter.CustomAdapterCTDT;
import com.example.svhtcmobile.Api.apiService.ILoginService;
import com.example.svhtcmobile.Model.CTDTDTO;
import com.example.svhtcmobile.Model.LopDTO;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CTDTController extends AppCompatActivity {

    TextView tvMa, tvTen;
    ListView lvMonHoc;
    ImageView ivOut;

    SharedPreferences accountSharedPref;
    ILoginService iLoginService;

    Spinner spLop, spNienKhoa;
    CustomAdapterCTDT customAdapterCTDT;

    List<CTDTDTO> dataCtdtdtos = new ArrayList<>();

    List<JsonObject> lops = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ctdt);

        setControl();
        setEvent();
    }

    private void setEvent() {
        ivOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        spLop.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String maLop = spLop.getSelectedItem().toString().split(" - ")[0];
                String nienKhoa = "";
                for (JsonObject lop : lops){
                    if (maLop.equals(lop.get("MALOP").getAsString().trim())){
                        nienKhoa = lop.get("KHOAHOC").getAsString();
                        break;
                    }
                }
                if(!nienKhoa.equals("")) setSpNienKhoa(nienKhoa);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spNienKhoa.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String maLop = spLop.getSelectedItem().toString().split(" - ")[0].trim();
                String nienKhoa = spNienKhoa.getSelectedItem().toString();
                dataCtdtdtos.clear();
                iLoginService.getCTDT(maLop,nienKhoa).enqueue(new Callback<List<CTDTDTO>>() {
                    @Override
                    public void onResponse(Call<List<CTDTDTO>> call, Response<List<CTDTDTO>> response) {
                        if(response.code()==200) {
                            dataCtdtdtos.addAll(response.body());
                            customAdapterCTDT.notifyDataSetChanged();
                        } else if(response.code()==400) {
                            Toast.makeText(CTDTController.this,"Không tìm thấy!",Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<CTDTDTO>> call, Throwable throwable) {

                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setControl() {
        lvMonHoc = findViewById(R.id.lvMonHoc);
        tvMa = findViewById(R.id.tvMa);
        tvTen = findViewById(R.id.tvTen);
        spLop = findViewById(R.id.spLop);
        spNienKhoa = findViewById(R.id.spNienKhoa);
        ivOut= findViewById(R.id.ivOut);

        accountSharedPref = getSharedPreferences("Account", Context.MODE_PRIVATE);
        String token = accountSharedPref.getString("token", "");
        String ho = accountSharedPref.getString("ho", "");
        String ten = accountSharedPref.getString("ten", "");
        String username = accountSharedPref.getString("username", "");
        String quyen = accountSharedPref.getString("quyen", "");

        tvTen.setText(ho + " "+ten);
        tvMa.setText("Mã: "+ username+ " - "+quyen);



        List<String> dsLop = new ArrayList<>();

        Retrofit retrofit = ApiClient.getClient(token);
        iLoginService = retrofit.create(ILoginService.class);
        customAdapterCTDT = new CustomAdapterCTDT(CTDTController.this,R.layout.layout_item_ctdt,dataCtdtdtos,iLoginService);
        lvMonHoc.setAdapter(customAdapterCTDT);
        iLoginService.getDanhSachLop().enqueue(new Callback<List<JsonObject>>() {
            @Override
            public void onResponse(Call<List<JsonObject>> call, Response<List<JsonObject>> response) {

                for(JsonObject lop: response.body()){
                    String lopString = lop.get("MALOP").getAsString().trim() + " - " + lop.get("TENLOP").getAsString() ;
                    dsLop.add(lopString);
                    lops.add(lop);

                }
                spLop.setAdapter(new ArrayAdapter(CTDTController.this, android.R.layout.simple_list_item_1,dsLop));
                String maLop = spLop.getSelectedItem().toString().split(" - ")[0];

                String nienKhoa = "";
                for (JsonObject lop : lops){
                    if (maLop.equals(lop.get("MALOP").getAsString().trim())){
                        nienKhoa = lop.get("KHOAHOC").getAsString();
                        break;
                    }
                }

                if(!nienKhoa.equals("")) setSpNienKhoa(nienKhoa);
            }

            @Override
            public void onFailure(Call<List<JsonObject>> call, Throwable throwable) {

            }
        });




    }

    private void setSpNienKhoa(String nienKhoa) {



        int start = Integer.parseInt(nienKhoa.split("-")[0]);
        int end = Integer.parseInt(nienKhoa.split("-")[1]);
        List<String> namHoc = new ArrayList<>();
        for (int i = start ; i<end ; i++) {
            String nk = String.valueOf(i) + "-"+String.valueOf(i+1);
            namHoc.add(nk);
        }
        spNienKhoa.setAdapter(new ArrayAdapter(CTDTController.this, android.R.layout.simple_list_item_1,namHoc));

    }
}