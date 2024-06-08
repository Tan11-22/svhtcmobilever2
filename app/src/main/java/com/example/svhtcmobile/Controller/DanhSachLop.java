package com.example.svhtcmobile.Controller;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import com.example.svhtcmobile.Adapter.CustomAdapterLop;
import com.example.svhtcmobile.Api.ApiClient;
import com.example.svhtcmobile.Api.apiService.IQuanTriService;
import com.example.svhtcmobile.Model.He;
import com.example.svhtcmobile.Model.LopDTO;
import com.example.svhtcmobile.Model.TTGiangVienAPI;
import com.example.svhtcmobile.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class DanhSachLop extends AppCompatActivity {
    private List<String> trangThai = new ArrayList<>();
    private Spinner spTrangThai;
    private ArrayAdapter adapter_spTrangThai;
    private  List<LopDTO> data = new ArrayList<>();

    private  List<LopDTO> data_all = new ArrayList<>();
    private CustomAdapterLop adapterLop;
    private ListView lvLop;
    private ImageButton imgBtnThemLop, imgBtnBack;
    private String maKhoa, maGV;
    private SearchView searchViewLop;
    private TextView tvTenGiangVien, tvTenKhoa, tvTenKhoa1;
    private SharedPreferences sharedPreferences;
    private IQuanTriService iQuanTriService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danh_sach_lop);
        Init();
        DocDuLieuThonTinCaNhan();
        setDuLieuTrangThaiLop();
        DocDuLieuLop();
        setEvent();
    }


    private void setEvent() {
        imgBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        imgBtnThemLop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog(maKhoa);
            }
        });
        spTrangThai.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                DocDuLieuLop();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        searchViewLop.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                data.clear();
                if(newText.equals("")){
                    for(LopDTO lop : data_all){
                        if(lop.getTrangThai() == (getTrangThaiSpinner() == 1)){
                            data.add(lop);
                        }
                    }
                    setAdapterLop();
                } else {
                    for(LopDTO lop : data_all){
                        if(lop.getMaLop().toLowerCase().contains(newText.toLowerCase()) && lop.getTrangThai() == (getTrangThaiSpinner() == 1)){
                            data.add(lop);
                        }
                    }
                }
                setAdapterLop();
                return false;
            }
        });
    }

    private void openDialog(String maKhoa){
        final Dialog dialog = new Dialog(DanhSachLop.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_dialog_them_lop);

        Window window = dialog.getWindow();
        if(window == null) return;

        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAtrributes = window.getAttributes();
        windowAtrributes.gravity = Gravity.CENTER;
        window.setAttributes(windowAtrributes);

        Button btnHuy = dialog.findViewById(R.id.btnHuy);
        Button btnOk = dialog.findViewById(R.id.btnOk);
        EditText edtMaLop = dialog.findViewById(R.id.edtMaLop);
        EditText edtTenLop = dialog.findViewById(R.id.edtTenLop);
        EditText edtNamBatDau = dialog.findViewById(R.id.edtNamBatDau);
        EditText edtNamKetThuc = dialog.findViewById(R.id.edtNamKetThuc);
        Spinner spHeDaoTao = dialog.findViewById(R.id.spHeDaoTao);
        List<He> danhSachHeDaoTao = new ArrayList<>();

        iQuanTriService.danhSachHeDaoTao().enqueue(new Callback<List<He>>() {
            @Override
            public void onResponse(Call<List<He>> call, Response<List<He>> response) {
                if(response.code() == 200){
                    danhSachHeDaoTao.addAll(response.body());
                    ArrayAdapter adapter_spHeDaoTao = new ArrayAdapter(dialog.getContext(), R.layout.layout_spinner, danhSachHeDaoTao);
                    spHeDaoTao.setAdapter(adapter_spHeDaoTao);
                }
            }

            @Override
            public void onFailure(Call<List<He>> call, Throwable throwable) {

            }
        });

        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LopDTO lop = new LopDTO();
                lop.setMaLop(edtMaLop.getText().toString());
                lop.setTenLop(edtTenLop.getText().toString());
                lop.setTrangThai(true);
                lop.setMaKhoa(maKhoa);
                lop.setKhoaHoc(edtNamBatDau.getText().toString() + "-" + edtNamKetThuc.getText().toString());
                String tmp = spHeDaoTao.getSelectedItem().toString();
                for(He he : danhSachHeDaoTao){
                    if(tmp.equals(he.getTenHe())){
                        lop.setIdHe(he.getId());
                    }
                }
                iQuanTriService.themLop(lop).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if(response.code() == 200){
                            dialog.dismiss();
                            data.add(lop);
                            adapterLop.notifyDataSetChanged();
                            Toast.makeText(DanhSachLop.this, "Thêm lớp thành công", Toast.LENGTH_LONG).show();
                        } else if (response.code() == 400) {
                            Toast.makeText(DanhSachLop.this, "Thêm lớp thất bại", Toast.LENGTH_LONG).show();
                            dialog.dismiss();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable throwable) {

                    }
                });
                
            }
        });

        dialog.setCancelable(true);
        dialog.show();
    }

    private void setDuLieuTrangThaiLop() {
        trangThai.add("Mở");
        trangThai.add("Đóng");
        adapter_spTrangThai = new ArrayAdapter(DanhSachLop.this, R.layout.layout_spinner, trangThai);
        spTrangThai.setAdapter(adapter_spTrangThai);
    }
    public void DocDuLieuThonTinCaNhan(){
        iQuanTriService.thongTinGiangVien(maGV).enqueue(new Callback<TTGiangVienAPI>() {
            @Override
            public void onResponse(Call<TTGiangVienAPI> call, Response<TTGiangVienAPI> response) {
                if(response.code() == 200){
                    TTGiangVienAPI thongTin = response.body();
                    tvTenGiangVien.setText(thongTin .getHo() + " " + thongTin.getTen());
                    tvTenKhoa.setText(thongTin.getTenKhoa());
                    maKhoa = thongTin.getMaKhoa();
                    tvTenKhoa1.setText(thongTin.getTenKhoa());
                }
            }

            @Override
            public void onFailure(Call<TTGiangVienAPI> call, Throwable throwable) {

            }
        });
    }
    public void DocDuLieuLop(){
        iQuanTriService.danhSachLopCuaKhoa(maGV, getTrangThaiSpinner()).enqueue(new Callback<List<LopDTO>>() {
            @Override
            public void onResponse(Call<List<LopDTO>> call, Response<List<LopDTO>> response) {
                if(response.code() == 200){
                    data.clear();
                    data_all.clear();
                    List<LopDTO> listLop = response.body();
                    for(LopDTO tmp : listLop){
                        tmp.setTrangThai(getTrangThaiSpinner() == 1);
//                        Toast.makeText(DanhSachLop.this, tmp.getTenLop() + "", Toast.LENGTH_LONG).show();
                    }

                    data.addAll(listLop);
                    data_all.addAll(data);

                    setAdapterLop();
                }
            }
            @Override
            public void onFailure(Call<List<LopDTO>> call, Throwable throwable) {

            }
        });
    }

    private void setAdapterLop(){
        adapterLop = new CustomAdapterLop(this, R.layout.layout_item_lop, data);
        lvLop.setAdapter(adapterLop);
    }

    public int getTrangThaiSpinner(){
        if(spTrangThai.getSelectedItem().toString().equals("Mở"))
            return 1;
        return 0;
    }


    private void Init() {
        sharedPreferences = getSharedPreferences("Account", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "");
        String ho = sharedPreferences.getString("ho", "");
        String ten = sharedPreferences.getString("ten", "");
        String username = sharedPreferences.getString("username", "");
        String quyen = sharedPreferences.getString("quyen", "");
        Retrofit retrofit = ApiClient.getClient(token);
        iQuanTriService = retrofit.create(IQuanTriService.class);
        maGV = username;

        spTrangThai = findViewById(R.id.spTrangThai);
        lvLop = findViewById(R.id.lvLop);
        imgBtnThemLop = findViewById(R.id.imgBtnThemLop);
        searchViewLop = findViewById(R.id.searchViewLop);
        tvTenGiangVien = findViewById(R.id.tvTenGiangVien);
        tvTenKhoa = findViewById(R.id.tvTenKhoa);
        tvTenKhoa1 = findViewById(R.id.tvTenKhoa1);
        imgBtnBack = findViewById(R.id.imgBtnBack);
    }
}