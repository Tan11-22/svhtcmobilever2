package com.example.svhtcmobile.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;


import com.example.svhtcmobile.Api.apiService.IQuanTriThongTin;
import com.example.svhtcmobile.Model.MonHoc;

import java.util.List;
import com.example.svhtcmobile.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdapterMonHoc extends ArrayAdapter<MonHoc> {
    private Context mContext;
    private int mResource;
    private List<MonHoc> danhSachMonHoc; // Không cần khai báo danh sách mới ở đây

    IQuanTriThongTin iQuanTriThongTin;

    public AdapterMonHoc(Context context, int resource, List<MonHoc> objects, IQuanTriThongTin iQuanTriThongTin) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
        danhSachMonHoc = objects; // Sử dụng danh sách được truyền vào
        this.iQuanTriThongTin = iQuanTriThongTin;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(mResource, parent, false);
        }

        MonHoc monHoc = getItem(position);

        TextView tvMaMH = convertView.findViewById(R.id.tvMaMH);
        TextView tvTenMH = convertView.findViewById(R.id.tvTenMH);
        TextView tvSoTietLT = convertView.findViewById(R.id.tvSoTietLT);
        TextView tvSoTietTH = convertView.findViewById(R.id.tvSoTietTH);
        TextView tvSoTinChi = convertView.findViewById(R.id.tvSoTinChi);
        ImageButton btnChinhSuaMH = convertView.findViewById(R.id.imbtnChinhSuaMH);
        ImageButton btnXoaMH = convertView.findViewById(R.id.imgBtnXoaMH);

        tvMaMH.setText(monHoc.getMaMH());
        tvTenMH.setText(monHoc.getTenMH());
        tvSoTietLT.setText(String.valueOf(monHoc.getSoTietLT()));
        tvSoTietTH.setText(String.valueOf(monHoc.getSoTietTH()));
        tvSoTinChi.setText(String.valueOf(monHoc.getSoTinChi()));

        btnChinhSuaMH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Hiển thị Dialog hoặc BottomSheetDialog để chỉnh sửa thông tin môn học
                showEditDialog(monHoc);
            }
        });

        // Bắt sự kiện khi nhấn vào nút Xóa
        btnXoaMH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Hiển thị hộp thoại xác nhận xóa môn học
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("Xác nhận xóa môn học");
                builder.setMessage("Bạn có chắc muốn xóa môn học này?");
                builder.setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Xử lý khi người dùng chấp nhận xóa môn học
                        // Gọi phương thức xóa môn học trong danh sách và cập nhật ListView

                        iQuanTriThongTin.xoaMonHoc(monHoc.getMaMH()).enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                if (response.isSuccessful()) {
                                    danhSachMonHoc.remove(position);
                                    notifyDataSetChanged();
                                    Toast.makeText(mContext, "Xóa thành công!", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(mContext, "Môn này đã mở lớp không thể xóa!", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                Toast.makeText(mContext, "Môn này đã mở lớp không thể xóa!", Toast.LENGTH_SHORT).show();
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
    private void showEditDialog(MonHoc monHoc) {
        // Tạo và hiển thị Dialog hoặc BottomSheetDialog để chỉnh sửa thông tin môn học
        // Sử dụng LayoutInflater để inflate giao diện từ file layout XML
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View dialogView = inflater.inflate(R.layout.edit_mon_hoc, null);

        // Tìm kiếm và ánh xạ các thành phần giao diện trong dialogView
        EditText edtMaMH = dialogView.findViewById(R.id.edtUpdateMaMH);
        EditText edtTenMH = dialogView.findViewById(R.id.edtUpdateTenMH);
        EditText edtSoTinChi = dialogView.findViewById(R.id.edtUpdateSoTinChi);
        EditText edtSoTietLT = dialogView.findViewById(R.id.edtUpdateSoTietLT);
        EditText edtSoTietTH = dialogView.findViewById(R.id.edtUpdateSoTietTH);
        // Tương tự với các thành phần khác
        Button btnLuu = dialogView.findViewById(R.id.btnLuuUpdateMH);
        Button btnHuy = dialogView.findViewById(R.id.btnHuyUpdateMH);
        // Hiển thị thông tin môn học trong các EditText

        edtMaMH.setText(monHoc.getMaMH());
        edtTenMH.setText(monHoc.getTenMH());
        edtSoTinChi.setText(String.valueOf(monHoc.getSoTinChi()));
        edtSoTietLT.setText(String.valueOf(monHoc.getSoTietLT()));
        edtSoTietTH.setText(String.valueOf(monHoc.getSoTietTH()));


        // Tạo và hiển thị Dialog hoặc BottomSheetDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();
        dialog.show();
        btnLuu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean check = true;
                // Lấy dữ liệu đã chỉnh sửa từ dialog
                String maMH = edtMaMH.getText().toString();
                String tenMH = edtTenMH.getText().toString();
                int soTinChi = Integer.parseInt(edtSoTinChi.getText().toString());
                int soTietLT = Integer.parseInt(edtSoTietLT.getText().toString());
                int soTietTH = Integer.parseInt(edtSoTietTH.getText().toString());
                if (tenMH.isEmpty()){
                    check=false;
                    Toast.makeText(mContext, "Không để trống dữ liệu!", Toast.LENGTH_SHORT).show();
                }
                if (check == true){
                    MonHoc monHoc1= new MonHoc();
                // Cập nhật thông tin của môn học
                monHoc1.setMaMH(maMH);
                monHoc1.setTenMH(tenMH);
                monHoc1.setSoTinChi(soTinChi);
                monHoc1.setSoTietLT(soTietLT);
                monHoc1.setSoTietTH(soTietTH);

                // Cập nhật giao diện nếu cần
                notifyDataSetChanged();
                iQuanTriThongTin.updateMonHoc(monHoc1).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            monHoc.setMaMH(maMH);
                            monHoc.setTenMH(tenMH);
                            monHoc.setSoTinChi(soTinChi);
                            monHoc.setSoTietLT(soTietLT);
                            monHoc.setSoTietTH(soTietTH);

                            // Cập nhật giao diện nếu cần
                            notifyDataSetChanged();
                            Toast.makeText(mContext, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                            // Đóng dialog
                            dialog.dismiss();
                        } else {
                            Toast.makeText(mContext, "Lỗi cập nhật!", Toast.LENGTH_SHORT).show();
                            // Đóng dialog
                            dialog.dismiss();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        // Xử lý khi có lỗi xảy ra
                    }
                });

                }
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

