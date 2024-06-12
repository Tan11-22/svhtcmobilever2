//package com.example.svhtcmobile.Controller;
//
//import android.app.Dialog;
//import android.content.Context;
//import android.content.SharedPreferences;
//import android.graphics.Color;
//import android.graphics.drawable.ColorDrawable;
//import android.os.Bundle;
//import android.view.Gravity;
//import android.view.View;
//import android.view.Window;
//import android.view.WindowManager;
//import android.widget.AdapterView;
//import android.widget.ArrayAdapter;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ImageButton;
//import android.widget.ListView;
//import android.widget.Spinner;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.example.svhtcmobile.Adapter.CustomAdapterLTC;
//import com.example.svhtcmobile.Api.ApiClient;
//import com.example.svhtcmobile.Api.apiService.IQuanTriService;
//import com.example.svhtcmobile.Model.LopTinChi;
//import com.example.svhtcmobile.Model.MonHocDTO;
//import com.example.svhtcmobile.Model.TTGiangVienAPI;
//import com.example.svhtcmobile.R;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//import retrofit2.Retrofit;
//
//
//public class DanhSachLopTinChi extends AppCompatActivity {
//    private ListView lvLopTC;
//    private String maKhoa;
//    private String maGV;
//    private List<LopTinChi> data = new ArrayList<>();
//    private List<LopTinChi> data_locdl = new ArrayList<>();
//    private CustomAdapterLTC adapterLTC;
//
//    private Spinner spLocTheo, spDkLoc;
//    private ArrayAdapter adapter_spLocTheo, adapter_spDkLoc, adapter_spMonHoc, adapter_spLop, adapter_spGiangVien;;
//    private List<String> data_spLocTheo = new ArrayList<>();
//
//    private List<String> data_spDkLoc = new ArrayList<>();
//    private List<String> dataMonHoc = new ArrayList<>();
//    private List<String> dataLop = new ArrayList<>();
//    private List<String> dataGiangVien = new ArrayList<>();
//    private int pos1 = 0, check1 = 0, check2 = 0;
//    private SharedPreferences sharedPreferences;
//    private IQuanTriService iQuanTriService;
//    private ImageButton imgBtnThemLTC, imgBtnBack;
//    private TextView tvTenGiangVien;
//    private TextView tvTenKhoa;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_lop_tin_chi);
//        Init();
//        setAdapterSpLocTheo();
//        setEvent();
//        DocDuLieuLTC();
//    }
//
//    private void setEvent() {
//        imgBtnBack.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                onBackPressed();
//            }
//        });
//        spLocTheo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                int x = -1;
//                String s = getTrangThaiSpLocTheo();
//                data_spDkLoc.clear();
//                if(s.equals("Lớp")){
//                    x = 1;
//                } else if (s.equals("Giảng viên")) {
//                    x = 2;
//                } else if(s.equals("Niên khóa")){
//                    x = 3;
//                } else if(s.equals("Môn học")){
//                    x = 4;
//                } else if(s.equals("Học kì")){
//                    x = 5;
//                } else if (s.equals("Trạng thái")) {
//                    x = 6;
//                }
//                pos1 = x;
//                if(x != 6){
//                    iQuanTriService.danhSachDieuKienLocLTC(x, maKhoa).enqueue(new Callback<List<String>>() {
//                        @Override
//                        public void onResponse(Call<List<String>> call, Response<List<String>> response) {
//                            if(response.code() == 200){
//                                data_spDkLoc.addAll(response.body());
//                                setAdapterSpDkLoc();
////                                DocDuLieuLTC();
//                            }
//                        }
//
//                        @Override
//                        public void onFailure(Call<List<String>> call, Throwable throwable) {
//
//                        }
//                    });
//                } else {
//                    data_spDkLoc.add("Lớp mở");
//                    data_spDkLoc.add("Lớp đã hủy");
//                    setAdapterSpDkLoc();
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });
//        spDkLoc.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//
//                locDL();
//                setAdapterLTC();
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });
//
//        imgBtnThemLTC.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                openDialog();
//            }
//        });
//    }
//    private void locDL(){
//        data_locdl.clear();
//        if(getTrangThaiSpLocTheo().equals("Lớp")){
//            for(LopTinChi ltc : data){
//                if(ltc.getTenLop().equals(getTrangThaiSpDkLoc())){
//                    data_locdl.add(ltc);
//                }
//            }
//        } else if(getTrangThaiSpLocTheo().equals("Giảng viên")){
//            for(LopTinChi ltc : data){
//                if(ltc.getTenGV().equals(getTrangThaiSpDkLoc())){
//                    data_locdl.add(ltc);
//                }
//            }
//        } else if(getTrangThaiSpLocTheo().equals("Niên khóa")){
//            for(LopTinChi ltc : data){
//                if(ltc.getNienKhoa().equals(getTrangThaiSpDkLoc())){
//                    data_locdl.add(ltc);
//                }
//            }
//        } else if(getTrangThaiSpLocTheo().equals("Môn học")){
//            for(LopTinChi ltc : data){
//                if(ltc.getTenMH().equals(getTrangThaiSpDkLoc())){
//                    data_locdl.add(ltc);
//                }
//            }
//        } else if(getTrangThaiSpLocTheo().equals("Học kì")){
//            for(LopTinChi ltc : data){
//                if(ltc.getHocKY() == Integer.parseInt(getTrangThaiSpDkLoc())){
//                    data_locdl.add(ltc);
//                }
//            }
//        } else if(getTrangThaiSpLocTheo().equals("Trạng thái")){
//            if(getTrangThaiSpDkLoc().equals("Lớp mở")){
//                for(LopTinChi ltc : data){
//                    if(!ltc.isHuyLop()){
//                        data_locdl.add(ltc);
//                    }
//                }
//            }  else if(getTrangThaiSpDkLoc().equals("Lớp đã hủy")){
//                for(LopTinChi ltc : data){
//                    if(ltc.isHuyLop()){
//                        data_locdl.add(ltc);
//                    }
//                }
//            }
//        }
//    }
//    private void openDialog(){
//        final Dialog dialog = new Dialog(this);
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.setContentView(R.layout.layout_dialog_them_ltc);
//
//        Window window = dialog.getWindow();
//        if(window == null) return;
//
//        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
//        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//
//        WindowManager.LayoutParams windowAtrributes = window.getAttributes();
//        windowAtrributes.gravity = Gravity.CENTER;
//        window.setAttributes(windowAtrributes);
//
//        Spinner spMonHoc = dialog.findViewById(R.id.spMonHoc);
//        Spinner spLop = dialog.findViewById(R.id.spLop);
//        Spinner spGiangVien = dialog.findViewById(R.id.spGiangVien);
//        //set adapter cho cac spinner
//        setAdapterSpDialog(spMonHoc, spLop, spGiangVien);
//        EditText edtNamBatDau = dialog.findViewById(R.id.edtNamBatDau);
//        EditText edtNamKetThuc = dialog.findViewById(R.id.edtNamKetThuc);
//        EditText edtNhom = dialog.findViewById(R.id.edtNhom);
//        EditText edtSoSV = dialog.findViewById(R.id.edtSoSV);
//        EditText edtHocKi = dialog.findViewById(R.id.edtHocKi);
//        Button btnHuy = dialog.findViewById(R.id.btnHuy);
//        Button btnOk = dialog.findViewById(R.id.btnOk);
//        btnHuy.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dialog.dismiss();
//            }
//        });
//        btnOk.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                LopTinChi lopTinChi = new LopTinChi();
//                lopTinChi.setTenMH(spMonHoc.getSelectedItem().toString());
//                lopTinChi.setTenLop(spLop.getSelectedItem().toString());
//                lopTinChi.setTenGV(spGiangVien.getSelectedItem().toString());
//                lopTinChi.setNienKhoa(edtNamBatDau.getText() + "-" + edtNamKetThuc.getText());
//                lopTinChi.setNhom(Integer.parseInt(edtNhom.getText().toString()));
//                lopTinChi.setSoSVToiThieu(Integer.parseInt(edtSoSV.getText().toString()));
//                lopTinChi.setHocKY(Integer.parseInt(edtHocKi.getText().toString()));
//                lopTinChi.setMaKhoa(maKhoa);
//
//                iQuanTriService.themLTC(lopTinChi).enqueue(new Callback<LopTinChi>() {
//                    @Override
//                    public void onResponse(Call<LopTinChi> call, Response<LopTinChi> response) {
//                        if(response.code() == 200){
//                            // lay ma lop vua them vao
////                            setDuLieuListViewLTCThemMoi(lopTinChi);
//                            Toast.makeText(DanhSachLopTinChi.this, "Thêm lớp tín chỉ thành công", Toast.LENGTH_LONG).show();
//                            dialog.dismiss();
//                        } else {
//                            Toast.makeText(DanhSachLopTinChi.this, "Thêm lớp tín chỉ thất bại", Toast.LENGTH_LONG).show();
//                        }
//
//                    }
//
//                    @Override
//                    public void onFailure(Call<LopTinChi> call, Throwable throwable) {
//
//                    }
//                });
//
//                iQuanTriService.soTC(lopTinChi.getTenMH()).enqueue(new Callback<MonHocDTO>() {
//                    @Override
//                    public void onResponse(Call<MonHocDTO> call, Response<MonHocDTO> response) {
//                        if(response.code() == 200){
//                            lopTinChi.setSoTC(response.body().getSoTinChi());
////                            Toast.makeText(DanhSachLopTinChi.this, "So TC: " + ltc.getSoTC(), Toast.LENGTH_LONG).show();
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<MonHocDTO> call, Throwable throwable) {
//
//                    }
//                });
//                // lay ra ma moi them
//                iQuanTriService.layMaLTC(lopTinChi.getTenMH(), lopTinChi.getNienKhoa(),
//                                                lopTinChi.getNhom(), lopTinChi.getHocKY()).enqueue(new Callback<LopTinChi>() {
//                    @Override
//                    public void onResponse(Call<LopTinChi> call, Response<LopTinChi> response) {
//                        if(response.code() == 200){
//                            lopTinChi.setMaLTC(response.body().getMaLTC());
//                            setDuLieuListViewLTCThemMoi(lopTinChi);
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<LopTinChi> call, Throwable throwable) {
//
//                    }
//                });
//            }
//        });
//        dialog.setCancelable(true);
//        dialog.show();
//    }
//
//    private void setDuLieuListViewLTCThemMoi(LopTinChi ltc){
//        data.add(ltc);
//        if(getTrangThaiSpLocTheo().equals("Lớp") && ltc.getTenLop().equals(getTrangThaiSpDkLoc())){
//            data_locdl.add(ltc);
//        }
//        if(getTrangThaiSpLocTheo().equals("Giảng viên") && ltc.getTenGV().equals(getTrangThaiSpDkLoc())){
//            data_locdl.add(ltc);
//        }
//        if(getTrangThaiSpLocTheo().equals("Niên khóa") && ltc.getNienKhoa().equals(getTrangThaiSpDkLoc())){
//            data_locdl.add(ltc);
//        }
//        if(getTrangThaiSpLocTheo().equals("Môn học") && ltc.getTenMH().equals(getTrangThaiSpDkLoc())){
//            data_locdl.add(ltc);
//        }
//        if(getTrangThaiSpLocTheo().equals("Học kì") && ltc.getHocKY() == Integer.parseInt(getTrangThaiSpDkLoc())){
//            data_locdl.add(ltc);
//        }
//        if(getTrangThaiSpLocTheo().equals("Trạng thái") && !ltc.isHuyLop()){
//            data_locdl.add(ltc);
//        }
//        adapterLTC.notifyDataSetChanged();
//    }
//
//    public void setDuLieuListViewLTCXoa(LopTinChi ltc){
//        data.remove(ltc);
//        if(getTrangThaiSpLocTheo().equals("Lớp") && ltc.getTenLop().equals(getTrangThaiSpDkLoc())){
//            data_locdl.remove(ltc);
//        }
//        if(getTrangThaiSpLocTheo().equals("Giảng viên") && ltc.getTenGV().equals(getTrangThaiSpDkLoc())){
//            data_locdl.remove(ltc);
//        }
//        if(getTrangThaiSpLocTheo().equals("Niên khóa") && ltc.getNienKhoa().equals(getTrangThaiSpDkLoc())){
//            data_locdl.remove(ltc);
//        }
//        if(getTrangThaiSpLocTheo().equals("Môn học") && ltc.getTenMH().equals(getTrangThaiSpDkLoc())){
//            data_locdl.remove(ltc);
//        }
//        if(getTrangThaiSpLocTheo().equals("Học kì") && ltc.getHocKY() == Integer.parseInt(getTrangThaiSpDkLoc())){
//            data_locdl.remove(ltc);
//        }
//        if(getTrangThaiSpLocTheo().equals("Trạng thái") && !ltc.isHuyLop()){
//            data_locdl.remove(ltc);
//        }
//        adapterLTC.notifyDataSetChanged();
//    }
//
//    private void DocDuLieuLTC() {
//        data.clear();
//        iQuanTriService.thongTinGiangVien(maGV).enqueue(new Callback<TTGiangVienAPI>() {
//            @Override
//            public void onResponse(Call<TTGiangVienAPI> call, Response<TTGiangVienAPI> response) {
//                if(response.code() == 200){
//                    TTGiangVienAPI thongTin = response.body();
//                    tvTenGiangVien.setText(thongTin.getHo() + " " + thongTin.getTen());
//                    tvTenKhoa.setText(thongTin.getTenKhoa());
//                    maKhoa = thongTin.getMaKhoa();
//                    iQuanTriService.danhSachLTC(maKhoa).enqueue(new Callback<List<LopTinChi>>() {
//                        @Override
//                        public void onResponse(Call<List<LopTinChi>> call, Response<List<LopTinChi>> response) {
//                            if(response.code() == 200){
//                                data.addAll(response.body());
//                                for(LopTinChi ltc : data){
//                                    ltc.setMaKhoa(maKhoa);
//                                }
//                            }
//                        }
//
//                        @Override
//                        public void onFailure(Call<List<LopTinChi>> call, Throwable throwable) {
//
//                        }
//                    });
//                }
//            }
//
//            @Override
//            public void onFailure(Call<TTGiangVienAPI> call, Throwable throwable) {
//
//            }
//        });
//    }
//
//    private void setAdapterSpDialog(Spinner spMonHoc, Spinner spLop, Spinner spGiangVien){
//        dataLop.clear();
//        dataGiangVien.clear();
//        dataMonHoc.clear();
//        // set dataLop
//        iQuanTriService.danhSachLocThemLTC(2, maKhoa).enqueue(new Callback<List<String>>() {
//            @Override
//            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
//                if(response.code() == 200){
//                    dataLop.addAll(response.body());
//                    adapter_spLop = new ArrayAdapter(DanhSachLopTinChi.this, R.layout.layout_spinner, dataLop);
//                    spLop.setAdapter(adapter_spLop);
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<String>> call, Throwable throwable) {
//
//            }
//        });
//        //set dataMonHoc
//        iQuanTriService.danhSachLocThemLTC(1, maKhoa).enqueue(new Callback<List<String>>() {
//            @Override
//            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
//                if(response.code() == 200){
//                    dataMonHoc.addAll(response.body());
//                    adapter_spMonHoc = new ArrayAdapter(DanhSachLopTinChi.this, R.layout.layout_spinner, dataMonHoc);
//                    spMonHoc.setAdapter(adapter_spMonHoc);
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<String>> call, Throwable throwable) {
//
//            }
//        });
//        //set dataGiangVien
//        iQuanTriService.danhSachLocThemLTC(3, maKhoa).enqueue(new Callback<List<String>>() {
//            @Override
//            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
//                if(response.code() == 200){
//                    dataGiangVien.addAll(response.body());
//                    adapter_spGiangVien = new ArrayAdapter(DanhSachLopTinChi.this, R.layout.layout_spinner, dataGiangVien);
//                    spGiangVien.setAdapter(adapter_spGiangVien);
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<String>> call, Throwable throwable) {
//
//            }
//        });
//    }
//    private void setAdapterLTC(){
//        adapterLTC = new CustomAdapterLTC(this, R.layout.layout_item_ltc, data_locdl);
//        lvLopTC.setAdapter(adapterLTC);
//    }
//
//    private void setAdapterSpLocTheo(){
//        adapter_spLocTheo = new ArrayAdapter(DanhSachLopTinChi.this, R.layout.layout_spinner, data_spLocTheo);
//        spLocTheo.setAdapter(adapter_spLocTheo);
//    }
//
//    private void setAdapterSpDkLoc(){
//        adapter_spDkLoc = new ArrayAdapter(DanhSachLopTinChi.this, R.layout.layout_spinner_dkloc, data_spDkLoc);
//        spDkLoc.setAdapter(adapter_spDkLoc);
//    }
//
//    private String getTrangThaiSpLocTheo(){
//        String s = spLocTheo.getSelectedItem().toString();
//        return s;
//    }
//
//    private String getTrangThaiSpDkLoc(){
//        String s = spDkLoc.getSelectedItem().toString();
//        return s;
//    }
//
//    public void updateAll(){
//        spLocTheo.setSelection(pos1 - 1);
//        spDkLoc.setSelection(0);
//        locDL();
//        setAdapterLTC();
//    }
//    private void Init() {
//        data_spLocTheo.add("Lớp");
//        data_spLocTheo.add("Giảng viên");
//        data_spLocTheo.add("Niên khóa");
//        data_spLocTheo.add("Môn học");
//        data_spLocTheo.add("Học kì");
//        data_spLocTheo.add("Trạng thái");
//
//        sharedPreferences = getSharedPreferences("Account", Context.MODE_PRIVATE);
//        String token = sharedPreferences.getString("token", "");
//        String ho = sharedPreferences.getString("ho", "");
//        String ten = sharedPreferences.getString("ten", "");
//        String username = sharedPreferences.getString("username", "");
//        String quyen = sharedPreferences.getString("quyen", "");
//        Retrofit retrofit = ApiClient.getClient(token);
//        iQuanTriService = retrofit.create(IQuanTriService.class);
//        maGV = username;
//
//        lvLopTC = findViewById(R.id.lvLopTC);
//        spDkLoc = findViewById(R.id.spDkLoc);
//        spLocTheo = findViewById(R.id.spLocTheo);
//        imgBtnThemLTC = findViewById(R.id.imgBtnThemLTC);
//        tvTenGiangVien = findViewById(R.id.tvTenGiangVien);
//        tvTenKhoa = findViewById(R.id.tvTenKhoa);
//        imgBtnBack = findViewById(R.id.imgBtnBack);
//    }
//}