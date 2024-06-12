package com.example.svhtcmobile.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.example.svhtcmobile.Api.apiService.IQuanTriThongTin;
import com.example.svhtcmobile.Controller.QuanTriLTC;
import com.example.svhtcmobile.Model.GiangVien;
import com.example.svhtcmobile.Model.GiangVienKhoa;
import com.example.svhtcmobile.Model.LopTinChiDTO;
import com.example.svhtcmobile.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdapterLTC extends ArrayAdapter<LopTinChiDTO> {
    private Context Context;
    private int Resource;
    private List<LopTinChiDTO> dsltc; // Không cần khai báo danh sách mới ở đây
    String magvtemp ="";
    String hotengv ="";
    Spinner spGV ,spLop;
    IQuanTriThongTin iQuanTriThongTin;

    public AdapterLTC(Context context, int resource, List<LopTinChiDTO> objects, IQuanTriThongTin iQuanTriThongTin) {
        super(context, resource, objects);
        Context = context;
        Resource = resource;
        this.dsltc = objects; // Sử dụng danh sách được truyền vào
        this.iQuanTriThongTin = iQuanTriThongTin;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(Context);
            convertView = inflater.inflate(Resource, parent, false);
        }

        LopTinChiDTO ltc = getItem(position);
        TextView tvTenMH = convertView.findViewById(R.id.tvTenMH);
        TextView tvMaMH = convertView.findViewById(R.id.tvMaMH);
        TextView tvNhom = convertView.findViewById(R.id.tvNhom);
        TextView tvSoSVTT = convertView.findViewById(R.id.tvSoSVTT);
        TextView tvSoSVTD = convertView.findViewById(R.id.tvSoSVTD);
        ImageButton btnChinhSua = convertView.findViewById(R.id.imbtnChinhSua);
        ImageButton btnXoa = convertView.findViewById(R.id.imbtnXoa);

        tvTenMH.setText(ltc.getTenmh());
        tvMaMH.setText(ltc.getMamh());
        tvNhom.setText(ltc.getNhom().toString());
        tvSoSVTT.setText(ltc.getSosvtt().toString());
        tvSoSVTD.setText(ltc.getSosvtd().toString());



        btnChinhSua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Hiển thị Dialog hoặc BottomSheetDialog để chỉnh sửa thông tin môn học

                showEditDialog(ltc);

            }
        });
                btnXoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Hiển thị hộp thoại xác nhận xóa môn học
                AlertDialog.Builder builder = new AlertDialog.Builder(Context);
                builder.setTitle("Xác nhận xóa lớp tín chỉ");
                builder.setMessage("Bạn có chắc muốn xóa lớp tín chỉ môn học "+ltc.getTenmh() +"-Nhóm"+ltc.getNhom().toString()+" ?");
                builder.setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Xử lý khi người dùng chấp nhận xóa môn học
                        // Gọi phương thức xóa môn học trong danh sách và cập nhật ListView

                        iQuanTriThongTin.xoaloptinchi(ltc.getMaltc()).enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                if (response.isSuccessful()) {
                                    dsltc.remove(position);
                                    notifyDataSetChanged();
                                    Toast.makeText(Context, "Xóa thành công!", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(Context, "Lớp tín chỉ đã có sinh viên đăng kí không thể xóa!", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                Toast.makeText(Context, "Xóa thất bại!", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                });
                builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Xử lý khi người dùng hủy bỏ thao tác xóa
                        dialog.dismiss();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
        return convertView;
    }
    private void showEditDialog(LopTinChiDTO ltc ){
        // Tạo và hiển thị Dialog hoặc BottomSheetDialog để chỉnh sửa thông tin môn học
        // Sử dụng LayoutInflater để inflate giao diện từ file layout XML
        List<GiangVienKhoa> giangviens = new ArrayList<>();
        List<String> lops = new ArrayList<>();
        LayoutInflater inflater = LayoutInflater.from(Context);
        View dialogView = inflater.inflate(R.layout.layout_dialog_sua_ltc, null);

        // Tìm kiếm và ánh xạ các thành phần giao diện trong dialogView
        EditText edtMaKhoa;
        edtMaKhoa = dialogView.findViewById(R.id.loadMaKhoa);
        edtMaKhoa.setText(ltc.getMakhoa());
        EditText edtNienKhoa = dialogView.findViewById(R.id.loadNienKhoa);
        edtNienKhoa.setText(ltc.getNienkhoa());
        EditText edtHocKi = dialogView.findViewById(R.id.loadHocKi);
        edtHocKi.setText(String.valueOf(ltc.getHocki()));
        EditText edtMonhoc = dialogView.findViewById(R.id.loadMonHoc);
        edtMonhoc.setText(String.valueOf(ltc.getTenmh()));
        // Tương tự với các thành phần khác
        Button btnHuy = dialogView.findViewById(R.id.btnHuy);
        Button btnLuu = dialogView.findViewById(R.id.btnLuuLTC);
        // Hiển thị thông tin môn học trong các EditText
        spLop = dialogView.findViewById(R.id.spLop);
        spGV = dialogView.findViewById(R.id.spGiangVien);
//        RadioGroup radioTrangThai = dialogView.findViewById(R.id.rdGroup);
        EditText edtnhom = dialogView.findViewById(R.id.edtNhom);
        EditText edtsoSVTT = dialogView.findViewById(R.id.edtSoSVTT);
        EditText edtsoSVTD = dialogView.findViewById(R.id.edtSoSVTD);
        edtnhom.setText(String.valueOf(ltc.getNhom()));
        edtsoSVTT.setText(String.valueOf(ltc.getSosvtt()));
        edtsoSVTD.setText(String.valueOf(ltc.getSosvtd()));

        //gan spinner
        iQuanTriThongTin.locGVKhoa(ltc.getMakhoa()).enqueue(new Callback<List<GiangVienKhoa>>() {
            @Override
            public void onResponse(Call<List<GiangVienKhoa>> call, Response<List<GiangVienKhoa>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.code() == 200) {
                        giangviens.addAll(response.body());
                        Log.e("GVLIST", giangviens.get(0).getMagv());
                        ArrayAdapter<GiangVienKhoa> adapter_spGV = new ArrayAdapter<>(Context, R.layout.layout_spinner, giangviens);
                        spGV.setAdapter(adapter_spGV);
                        int position = -1;
                        for (int i = 0; i < giangviens.size(); i++) {
                            if (giangviens.get(i).getMagv().equals(ltc.getMagv())) {
                                position = i;
                                break;
                            }
                        }
                        if (position != -1) {
                            spGV.setSelection(position);
                        } else {
                            Log.e("Spinner Selection", "Giảng viên không tìm thấy trong danh sách");
                        }
                    }
                } else {
                    Log.e("API Response", "Error: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<GiangVienKhoa>> call, Throwable throwable) {
                Log.e("API Response", "Error: " + throwable.getMessage());
            }
        });

// Capture selected item and set magvtemp and hotengvtemp
        if (spGV != null) {
            spGV.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    GiangVienKhoa selectedGiangVien = (GiangVienKhoa) parent.getItemAtPosition(position);
                    magvtemp = selectedGiangVien.getMagv();
                    hotengv = selectedGiangVien.getHo() + " " + selectedGiangVien.getTen();
                    // Use magvtemp and hotengvtemp as needed
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    // Handle case when nothing is selected if needed
                }
            });
        } else {
            Log.e("Spinner Initialization", "Spinner spGV is null");
        }

        iQuanTriThongTin.locLopKhoa(ltc.getMakhoa()).enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                if (response.isSuccessful() && response.body() != null) {

                    if(response.code() == 200){
                        lops.addAll(response.body());
                        Log.e("LOPLIST", lops.get(0));
                        ArrayAdapter adapter_spLop= new ArrayAdapter(Context, R.layout.layout_spinner, lops);
                        spLop.setAdapter(adapter_spLop);
                        int position = lops.indexOf(ltc.getMalop());
                        if (position != -1) {
                            spLop.setSelection(position);
                        } else {
                            Log.e("Spinner Selection", "Value not found in the list");
                        }

                    }
                } else {
                    Log.e("API Response", "Error: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable throwable) {
                Log.e("API Response", "Error: " + throwable.getMessage());

            }  });
        // Tạo và hiển thị Dialog hoặc BottomSheetDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(Context);
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();
        dialog.show();
        btnLuu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText edtnhom = dialogView.findViewById(R.id.edtNhom);
                EditText edtsoSVTT = dialogView.findViewById(R.id.edtSoSVTT);
                EditText edtsoSVTD = dialogView.findViewById(R.id.edtSoSVTD);
                int nhom = Integer.parseInt( edtnhom.getText().toString());
                int soSVTT =  Integer.parseInt( edtsoSVTT.getText().toString());
                int soSVTD =  Integer.parseInt( edtsoSVTD.getText().toString());

                for (int i = 0; i< dsltc.size();i++)
                {
                    if((dsltc.get(i).getMakhoa().equals(ltc.getMakhoa())&&dsltc.get(i).getNienkhoa().equals(ltc.getNienkhoa())&&
                        dsltc.get(i).getHocki()==ltc.getHocki()&&dsltc.get(i).getNhom()==nhom)&& dsltc.get(i).getMaltc()!=ltc.getMaltc()
                    )
                    {
                        Toast.makeText(Context, "Môn học có nhóm này đã tồn tại!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                LopTinChiDTO ltcUpdate = new LopTinChiDTO(ltc.getMaltc(),ltc.getMakhoa(),ltc.getNienkhoa(),ltc.getHocki(),nhom,soSVTT,soSVTD,
                        spLop.getSelectedItem().toString(),false,ltc.getMamh(),magvtemp,ltc.getTenmh(),hotengv);
                iQuanTriThongTin.updateloptinchi(ltcUpdate).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            ltc.setNhom(nhom);
                            ltc.setSosvtt(soSVTT);
                            ltc.setSosvtd(soSVTD);
                            ltc.setMalop(spLop.getSelectedItem().toString());
                            ltc.setMagv(magvtemp);
                            ltc.setTengiangvien(hotengv);
                            // Cập nhật giao diện ListView
                            notifyDataSetChanged();
                            Toast.makeText(Context, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        } else {
                            Toast.makeText(Context, "Cập nhật không thành công!", Toast.LENGTH_SHORT).show();

                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        // Xử lý khi có lỗi xảy ra
                    }
                });
            }
        });
        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Đóng dialog
                dialog.dismiss();
            }
        });
}
}
