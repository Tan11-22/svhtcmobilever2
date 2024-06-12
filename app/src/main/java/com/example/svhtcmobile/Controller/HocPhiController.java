//package com.example.svhtcmobile.Controller;
//
//import android.content.Context;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.graphics.Paint;
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.widget.AdapterView;
//import android.widget.ArrayAdapter;
//import android.widget.Button;
//import android.widget.LinearLayout;
//import android.widget.ListView;
//import android.widget.Spinner;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//
//import com.example.svhtcmobile.Adapter.DanhSachHocPhiLopAdapter;
//import com.example.svhtcmobile.Api.ApiClient;
//import com.example.svhtcmobile.Api.apiService.IThongKe;
//import com.example.svhtcmobile.Model.DanhSachHocPhiLop;
//import com.example.svhtcmobile.R;
//import com.github.mikephil.charting.charts.PieChart;
//import com.github.mikephil.charting.components.Description;
//import com.github.mikephil.charting.data.PieData;
//import com.github.mikephil.charting.data.PieDataSet;
//import com.github.mikephil.charting.data.PieEntry;
//import com.github.mikephil.charting.utils.ColorTemplate;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//import retrofit2.Retrofit;
//
//public class HocPhiController extends AppCompatActivity {
//
//    private Spinner spDslop, spNienKhoa, spHocKy;
//    private ListView lvDanhSachHocPhi;
//    private Button btnXemDs, btnThongKe, btnXuatPDF;
//    private LinearLayout llDanhSachHocPhi;
//    private PieChart chartDanhSachHocPhi;
//    private DanhSachHocPhiLopAdapter danhSachHocPhiLopAdapter;
//
//    private List<DanhSachHocPhiLop> data = new ArrayList<>();
//
//    private static List<String> dataLop = new ArrayList<>();
//    private List<String> dataNienKhoa = new ArrayList<>();
//    private List<Integer> dataHocKy = new ArrayList<>();
//    private ArrayAdapter dataLopAdapter;
//    private ArrayAdapter dataNienKhoaAdapter;
//    private ArrayAdapter dataHocKyAdapter;
//
//    SharedPreferences accountSharedPref;
//
//    IThongKe iThongKe;
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_hoc_phi);
//        setControl();
//        setup();
//        setEvent();
//    }
//
//    public void setControl() {
//        spDslop = findViewById(R.id.spDsLop);
//        lvDanhSachHocPhi = findViewById(R.id.lvDanhSachHocPhi);
//        spNienKhoa = findViewById(R.id.spNienKhoa);
//        spHocKy = findViewById(R.id.spHocKy);
//        btnXemDs = findViewById(R.id.btnXemDs);
//        llDanhSachHocPhi = findViewById(R.id.llDanhSachHocPhi);
//        chartDanhSachHocPhi = findViewById(R.id.chartDanhSach);
//        btnThongKe = findViewById(R.id.btnThongKe);
//        btnXuatPDF = findViewById(R.id.btnXuatPDF);
//
//        accountSharedPref = getSharedPreferences("Account", Context.MODE_PRIVATE);
//        String token = accountSharedPref.getString("token", "");
//        Retrofit retrofit = ApiClient.getClient(token);
//        iThongKe = retrofit.create(IThongKe.class);
//    }
//
//    public void setEvent() {
//
//        btnXemDs.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                chartDanhSachHocPhi.setVisibility(View.GONE);
//                llDanhSachHocPhi.setVisibility(View.VISIBLE);
//                lvDanhSachHocPhi.setVisibility(View.VISIBLE);
//                if (spDslop.getSelectedItem() == null || spNienKhoa.getSelectedItem() == null) {
//                    Toast.makeText(HocPhiController.this, "Vui lòng chọn lớp và niên khoá để hiển thị", Toast.LENGTH_SHORT).show();
//                } else {
//                    String lop = spDslop.getSelectedItem().toString();
//                    String nienkhoa = spNienKhoa.getSelectedItem().toString();
//                    int hocky = Integer.parseInt(spHocKy.getSelectedItem().toString());
//                    getDanhSachHocPhi(lop, nienkhoa, hocky);
//                }
//            }
//        });
//
//        btnThongKe.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                chartDanhSachHocPhi.setVisibility(View.VISIBLE);
//                llDanhSachHocPhi.setVisibility(View.GONE);
//                lvDanhSachHocPhi.setVisibility(View.GONE);
//                if (spDslop.getSelectedItem() == null || spNienKhoa.getSelectedItem() == null) {
//                    Toast.makeText(HocPhiController.this, "Vui lòng chọn lớp và niên khoá để hiển thị", Toast.LENGTH_SHORT).show();
//                } else {
//                    String lop = spDslop.getSelectedItem().toString();
//                    String nienkhoa = spNienKhoa.getSelectedItem().toString();
//                    int hocky = Integer.parseInt(spHocKy.getSelectedItem().toString());
//                    getChartThongKeHocPhi(lop, nienkhoa, hocky);
//                }
//            }
//        });
//
//        lvDanhSachHocPhi.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                DanhSachHocPhiLop danhSachHocPhiLop = data.get(position);
//                Intent intent = new Intent(HocPhiController.this, MainChiTietHocPhi.class);
//                intent.putExtra("hocphi",danhSachHocPhiLop);
//                startActivity(intent);
//            }
//        });
//        btnXuatPDF.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String lop = spDslop.getSelectedItem().toString();
//                String nienkhoa = spNienKhoa.getSelectedItem().toString();
//                int hocky = Integer.parseInt(spHocKy.getSelectedItem().toString());
//                ApiClient.askPermissions(HocPhiController.this);
//                String fileName = "hoc_phi_"+lop+"_"+nienkhoa+"_"+String.valueOf(hocky)+".pdf";
//                View view = LayoutInflater.from(HocPhiController.this).inflate(R.layout.layout_pdf_thong_ke_hoc_phi,null);
//                PieChart chartPDF = view.findViewById(R.id.chartDanhSachPDF2);
//                ListView lvDanhSachHocPhi1 = view.findViewById(R.id.lvDanhSachHocPhi);
//                TextView tvLop = view.findViewById(R.id.tvLop);
//                TextView tvNienKhoa = view.findViewById(R.id.tvNienKhoa);
//                TextView tvHocKy = view.findViewById(R.id.tvHocKy);
//                tvLop.setText(tvLop.getText().toString()+ " "+ lop);
//                tvNienKhoa.setText(tvNienKhoa.getText().toString()+ " "+ nienkhoa);
//                tvHocKy.setText(tvHocKy.getText().toString()+ " "+ String.valueOf(hocky));
//                iThongKe.getDanhSachHocPhiTheoLop(lop, nienkhoa, hocky).enqueue(new Callback<List<DanhSachHocPhiLop>>() {
//                    @Override
//                    public void onResponse(Call<List<DanhSachHocPhiLop>> call, Response<List<DanhSachHocPhiLop>> response) {
//                        if (response.code() == 204) {
//                            Toast.makeText(HocPhiController.this, "Không có dữ liệu", Toast.LENGTH_SHORT).show();
//                        } else {
//                            lvDanhSachHocPhi1.setVisibility(View.VISIBLE);
//                            data = response.body();
//                            danhSachHocPhiLopAdapter = new DanhSachHocPhiLopAdapter(HocPhiController.this, R.layout.layout_item_hocphi_list, data);
//                            lvDanhSachHocPhi1.setAdapter(danhSachHocPhiLopAdapter);
//                            List<PieEntry> entries = getPieEntry(data);
//                            PieDataSet dataSet = new PieDataSet(entries, "Danh sách học phí");
//                            dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
//
//                            PieData data = new PieData(dataSet);
//                            chartPDF.setData(data);
//                            Description description = new Description();
//                            description.setText("Biểu đồ học phí");
//                            description.setTextAlign(Paint.Align.CENTER);
//                            chartPDF.setDescription(description);
//
//                            chartPDF.invalidate();
//                            ApiClient.convertXMLToPDF(HocPhiController.this,view,fileName);
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<List<DanhSachHocPhiLop>> call, Throwable throwable) {
//                        Toast.makeText(HocPhiController.this, "Lấy dữ liệu học phí thất bại", Toast.LENGTH_SHORT).show();
//                    }
//                });
//            }
//        });
//    }
//
//    private void getChartThongKeHocPhi(String lop, String nienkhoa, int hocky) {
//        iThongKe.getDanhSachHocPhiTheoLop(lop, nienkhoa, hocky).enqueue(new Callback<List<DanhSachHocPhiLop>>() {
//            @Override
//            public void onResponse(Call<List<DanhSachHocPhiLop>> call, Response<List<DanhSachHocPhiLop>> response) {
//                if (response.code() == 204) {
//                    System.out.println("Không có dữ liệu chart");
//                    Toast.makeText(HocPhiController.this, "Không có dữ liệu", Toast.LENGTH_SHORT).show();
//                    chartDanhSachHocPhi.setVisibility(View.GONE);
//                } else {
//                    data = response.body();
//                    List<PieEntry> entries = getPieEntry(data);
//                    viewChart(entries);
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<DanhSachHocPhiLop>> call, Throwable throwable) {
//                Toast.makeText(HocPhiController.this, "Lấy dữ liệu học phí thất bại", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//    private void getDanhSachHocPhi(String lop, String nienkhoa, int hocky) {
//        if (lop == null || nienkhoa == null) {
//            Toast.makeText(HocPhiController.this, "Vui lòng nhập đầy đủ dữ liệu", Toast.LENGTH_SHORT).show();
//        }
//        iThongKe.getDanhSachHocPhiTheoLop(lop, nienkhoa, hocky).enqueue(new Callback<List<DanhSachHocPhiLop>>() {
//            @Override
//            public void onResponse(Call<List<DanhSachHocPhiLop>> call, Response<List<DanhSachHocPhiLop>> response) {
//                if (response.code() == 204) {
//                    Toast.makeText(HocPhiController.this, "Không có dữ liệu", Toast.LENGTH_SHORT).show();
//                    lvDanhSachHocPhi.setVisibility(View.GONE);
//                } else {
//                    data = response.body();
//                    danhSachHocPhiLopAdapter = new DanhSachHocPhiLopAdapter(HocPhiController.this, R.layout.layout_item_hocphi_list, data);
//                    lvDanhSachHocPhi.setAdapter(danhSachHocPhiLopAdapter);
//                    danhSachHocPhiLopAdapter.notifyDataSetChanged();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<DanhSachHocPhiLop>> call, Throwable t) {
//                Toast.makeText(HocPhiController.this, "Lấy dữ liệu học phí thất bại", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//    public List<PieEntry> getPieEntry(List<DanhSachHocPhiLop> data) {
//        int percentChuaDong = 0, percentDaDong = 0, daDong = 0, chuaDong = 0, total = 0;
//        List<PieEntry> entries = new ArrayList<>();
//        for (DanhSachHocPhiLop item : data) {
//            if (item == null) {
//                return entries;
//            }
//            System.out.println(item.getSotiendong());
//            total++;
//            if (item.getSotiendong() != 0) {
//                daDong++;
//            } else {
//                chuaDong++;
//            }
//        }
//
//        entries.add(new PieEntry(daDong, "Đã đóng"));
//        entries.add(new PieEntry(chuaDong, "Chưa đóng"));
//        return entries;
//    }
//
//    public void viewChart(List<PieEntry> entries) {
//
//        PieDataSet dataSet = new PieDataSet(entries, "Danh sách học phí");
//        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
//
//        PieData data = new PieData(dataSet);
//        chartDanhSachHocPhi.setData(data);
//        Description description = new Description();
//        description.setText("Biểu đồ học phí");
//        description.setTextAlign(Paint.Align.CENTER);
//        chartDanhSachHocPhi.setDescription(description);
//
//        chartDanhSachHocPhi.invalidate(); // refresh biểu đồ
//    }
//
//    public void getDsLop() {
//        iThongKe.getDanhSachMaLop().enqueue(new Callback<List<String>>() {
//            @Override
//            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
//                if (response.code() == 204) {
//                    Toast.makeText(HocPhiController.this, "Danh sách lớp không tồn tại", Toast.LENGTH_SHORT).show();
//                } else {
//                    dataLop = response.body();
//                    dataLopAdapter = new ArrayAdapter(HocPhiController.this, R.layout.layout_spinner, dataLop);
//                    dataLopAdapter.notifyDataSetChanged();
//                    spDslop.setAdapter(dataLopAdapter);
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<String>> call, Throwable t) {
//                Toast.makeText(HocPhiController.this, "Lấy dữ liệu lớp thất bại", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//    public void getNienKhoa() {
//        iThongKe.getNienKhoa().enqueue(new Callback<List<String>>() {
//            @Override
//            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
//                if (response.code() == 204) {
//                    Toast.makeText(HocPhiController.this, "Danh sách niên khoá không tồn tại", Toast.LENGTH_SHORT).show();
//                } else {
//                    dataNienKhoa = response.body();
//                    dataNienKhoaAdapter = new ArrayAdapter(HocPhiController.this, R.layout.layout_spinner, dataNienKhoa);
//                    spNienKhoa.setAdapter(dataNienKhoaAdapter);
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<String>> call, Throwable throwable) {
//                Toast.makeText(HocPhiController.this, "Lấy dữ liệu niên khoá thất bại", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//    public void setup() {
//        dataHocKy.add(1);
//        dataHocKy.add(2);
//        dataHocKy.add(3);
//
//        getNienKhoa();
//        getDsLop();
//        dataHocKyAdapter = new ArrayAdapter(this, R.layout.layout_spinner, dataHocKy);
//        spHocKy.setAdapter(dataHocKyAdapter);
//    }
//}