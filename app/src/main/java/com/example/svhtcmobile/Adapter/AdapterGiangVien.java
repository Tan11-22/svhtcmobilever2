package com.example.svhtcmobile.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.bumptech.glide.Glide;
import com.example.svhtcmobile.Api.ApiClient;
import com.example.svhtcmobile.Api.apiService.IQuanTriThongTin;
import com.example.svhtcmobile.Model.GiangVien;
import com.example.svhtcmobile.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AdapterGiangVien extends ArrayAdapter<GiangVien> {

    private Context mContext;
    private int mResource;
    ArrayAdapter gvAdapter;
    String base64 = "";


    ImageView ivAnhGV;
    IQuanTriThongTin iQuanTriThongTin;

    private List<GiangVien> DSGV; // Không cần khai báo danh sách mới ở đây

    private List<GiangVien> filteredGiangVienList;
    public AdapterGiangVien(Context context, int resource, List<GiangVien> objects , IQuanTriThongTin iQuanTriThongTin) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
        DSGV = objects; // Sử dụng danh sách được truyền vào
        filteredGiangVienList = new ArrayList<>(objects);
        this.iQuanTriThongTin = iQuanTriThongTin;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(mResource, parent, false);
        }

        GiangVien giangVien = getItem(position);

        TextView tvMaGV = convertView.findViewById(R.id.tvMaGV);
        TextView tvHoGV = convertView.findViewById(R.id.tvHoGV);
        TextView tvTenGV = convertView.findViewById(R.id.tvTenGV);
        TextView tvChuyenMon = convertView.findViewById(R.id.tvChuyenMon);
        TextView tvSTD = convertView.findViewById(R.id.tvSDTGV);
        ImageButton btnChinhSuaGV = convertView.findViewById(R.id.imbtnChinhSuaGV);
        ImageButton btnXoaGV = convertView.findViewById(R.id.imgBtnXoaGV);




        tvMaGV.setText(giangVien.getMagv());
        tvHoGV.setText(giangVien.getHo());
        tvTenGV.setText(giangVien.getTen());
        tvChuyenMon.setText(giangVien.getChuyenmon());
        tvSTD.setText(giangVien.getSdt());



        btnChinhSuaGV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Hiển thị Dialog hoặc BottomSheetDialog để chỉnh sửa thông tin môn học

                showEditDialog(giangVien);

            }
        });

        // Bắt sự kiện khi nhấn vào nút Xóa
        btnXoaGV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("Xác nhận xóa giảng viên");
                builder.setMessage("Bạn có chắc muốn xóa giảng viên này?");
                builder.setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        iQuanTriThongTin.xoaGiangVien(giangVien.getMagv()).enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                if (response.isSuccessful()) {
                                    DSGV.remove(position);
                                    notifyDataSetChanged();
                                    Toast.makeText(mContext, "Xóa thành công!", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(mContext, "Xóa không thành công!", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                // Xử lý khi có lỗi xảy ra
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

    private void showEditDialog(GiangVien giangVien) {
        // Tạo và hiển thị Dialog hoặc BottomSheetDialog để chỉnh sửa thông tin môn học
        // Sử dụng LayoutInflater để inflate giao diện từ file layout XML
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View dialogView = inflater.inflate(R.layout.edit_giang_vien, null);
        EditText makhoa = dialogView.findViewById(R.id.loadMaKhoa);
        // Tìm kiếm và ánh xạ các thành phần giao diện trong dialogView
        EditText edtMaGV = dialogView.findViewById(R.id.edtMaGVUpdate);
        EditText edtHoGV = dialogView.findViewById(R.id.edtHoGVUpdate);
        EditText edtTenGV = dialogView.findViewById(R.id.edtTenGVUpdate);
        RadioGroup radioGroupHocHam = dialogView.findViewById(R.id.radioGroupHocHamUpdate);
        RadioGroup radioGroupHocVi = dialogView.findViewById(R.id.radioGroupHocViUpdate);
        EditText edtSDT = dialogView.findViewById(R.id.edtSDTGVUpdate);
        EditText edtChuyenMon = dialogView.findViewById(R.id.edtChuyenMonUpdate);
        EditText edtEmail = dialogView.findViewById(R.id.edtEmailUpdate);
//        ImageButton btnChonAnh = dialogView.findViewById(R.id.btnChonAnhUpdate);
        ivAnhGV = dialogView.findViewById(R.id.ivAnhGVUpdate);
        // Tương tự với các thành phần khác
        Button btnLuu = dialogView.findViewById(R.id.btnLuuGVUpdate);
        Button btnHuy = dialogView.findViewById(R.id.btnHuyGVUpdate);
        // Hiển thị thông tin môn học trong các EditText
        makhoa.setText(giangVien.getMakhoa());
        edtMaGV.setText(giangVien.getMagv());
        edtHoGV.setText(giangVien.getHo());
        edtTenGV.setText(giangVien.getTen());
        edtChuyenMon.setText(giangVien.getChuyenmon());
        edtSDT.setText(giangVien.getSdt());
        edtEmail.setText(giangVien.getEmail());

        if (giangVien.getHocham().equals("Giáo sư")) {
            radioGroupHocHam.check(R.id.radioButtonGiaoSu);
        }
        else if (giangVien.getHocham().equals("Không")){
            radioGroupHocHam.check(R.id.radioButtonKhong);
        }
        else radioGroupHocHam.check(R.id.radioButtonPhoGiaoSu);

        if (giangVien.getHocvi().equals("Thạc sĩ")) {
            radioGroupHocVi.check(R.id.radioButtonThacSi);
        }
        else if (giangVien.getHocvi().equals("Tiến sĩ")){
            radioGroupHocVi.check(R.id.radioButtonTienSi);
        }
        else radioGroupHocVi.check(R.id.radioButtonTienSiKH);
        if (giangVien.getHinhanh()!= null){
            Glide.with(mContext)
                    .load(ApiClient.getBaseUrl()+ "auth/get-img?name="+giangVien.getHinhanh())
                    .into(ivAnhGV);
        }



        // Tạo và hiển thị Dialog hoặc BottomSheetDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();
        dialog.show();

        btnLuu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String maKhoa1 = makhoa.getText().toString();
                String magv = edtMaGV.getText().toString();
                String ho = edtHoGV.getText().toString();
                String ten = edtTenGV.getText().toString();
                String hocham;
                String hocvi;
                String chuyenmon = edtChuyenMon.getText().toString();
                String sdt = edtSDT.getText().toString();
                String email = edtEmail.getText().toString();
                int selectedHocHamId = radioGroupHocHam.getCheckedRadioButtonId();
                if (selectedHocHamId == R.id.radioButtonPhoGiaoSu) {
                    hocham = "Phó giáo sư";
                }
                else if (selectedHocHamId == R.id.radioButtonGiaoSu) {
                    hocham = "Giáo sư";
                }
                else hocham = "Không";
                int selectedHocViId = radioGroupHocVi.getCheckedRadioButtonId();
                if (selectedHocViId == R.id.radioButtonThacSi) {
                    hocvi = "Thạc sĩ";
                }
                else if (selectedHocViId == R.id.radioButtonTienSi) {
                    hocvi = "Tiến sĩ";
                }
                else hocvi = "Tiến sĩ Khoa Học";
                GiangVien new1 = new GiangVien(magv, ho, ten, hocham,hocvi,chuyenmon, sdt, giangVien.getHinhanh(),email,maKhoa1);


                iQuanTriThongTin.updateGiangVien(new1).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            giangVien.setHo(ho);
                            giangVien.setTen(ten);
                            giangVien.setHocham(hocham);
                            giangVien.setHocvi(hocvi);
                            giangVien.setChuyenmon(chuyenmon);
                            giangVien.setSdt(sdt);
                            giangVien.setEmail(email);
                            notifyDataSetChanged();
                            Toast.makeText(mContext, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                            // Đóng dialog
                            dialog.dismiss();
                        } else {
                            Toast.makeText(mContext, "Cập nhật không thành công!", Toast.LENGTH_SHORT).show();
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
        });
        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Đóng dialog
                dialog.dismiss();
            }
        });


    }
    public static void setBase64ImageToImageView(String base64ImageString, ImageView imageView) {
        // Giải mã chuỗi base64 thành mảng byte
        byte[] decodedString = Base64.decode(base64ImageString, Base64.DEFAULT);

        // Chuyển đổi mảng byte thành đối tượng Bitmap
        Bitmap bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

        // Đặt Bitmap vào ImageView để hiển thị ảnh
        imageView.setImageBitmap(bitmap);
    }
//    public void filterByMaGV(String maGV) {
//        filteredGiangVienList.clear(); // Xóa danh sách giáo viên đã lọc
//
//        if (maGV.isEmpty()) { // Nếu mã giáo viên rỗng, hiển thị tất cả giáo viên
//            filteredGiangVienList.addAll(DSGV);
//        } else {
//            String lowerCaseQuery = maGV.toLowerCase(); // Chuyển đổi mã giáo viên sang chữ thường
//
//            // Lọc danh sách giáo viên gốc để chỉ chứa các giáo viên có mã giáo viên chứa maGV
//            for (GiangVien gv : DSGV) {
//                if (gv.getMagv().toLowerCase().contains(lowerCaseQuery)) {
//                    filteredGiangVienList.add(gv);
//                }
//            }
//        }
//        notifyDataSetChanged(); // Thông báo cho ListView cập nhật dữ liệu
//    }
}
