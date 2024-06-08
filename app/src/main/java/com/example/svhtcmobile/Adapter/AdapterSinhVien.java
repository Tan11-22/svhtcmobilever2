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
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.example.svhtcmobile.Api.ApiClient;
import com.example.svhtcmobile.Api.apiService.IQuanTriThongTin;
import com.example.svhtcmobile.Controller.MainQuanTriSinhVien;
import com.example.svhtcmobile.Model.SinhVien;
import com.example.svhtcmobile.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdapterSinhVien extends ArrayAdapter<SinhVien> {

    private Context mContext;
    private int mResource;
    ArrayAdapter svAdapter;
    String base64 = "";
    String base64String;
    ImageView ivAnhSV;
    IQuanTriThongTin iQuanTriThongTin;
    private AppCompatActivity mActivity;
    private List<SinhVien> DSSV; // Không cần khai báo danh sách mới ở đây

    public AdapterSinhVien(Context context, int resource, List<SinhVien> objects, IQuanTriThongTin iQuanTriThongTin) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
        DSSV = objects; // Sử dụng danh sách được truyền vào
        this.iQuanTriThongTin = iQuanTriThongTin;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(mResource, parent, false);
        }

        SinhVien sinhVien = getItem(position);

        TextView tvMaSV = convertView.findViewById(R.id.tvMaSV);
        TextView tvHoSV = convertView.findViewById(R.id.tvHoSV);
        TextView tvTenSV = convertView.findViewById(R.id.tvTenSV);
        TextView tvSDT = convertView.findViewById(R.id.tvSDT);
        TextView tvDiaChi = convertView.findViewById(R.id.tvDiaChi);
        ImageButton btnChinhSuaSV = convertView.findViewById(R.id.imbtnChinhSuaSV);
        ImageButton btnXoaSV = convertView.findViewById(R.id.imgBtnXoaSV);
        ImageButton btnDaNghiHoc = convertView.findViewById(R.id.imbtnDaNghiHoc);
        assert sinhVien != null;
        if (sinhVien.getDanghihoc()) {
            btnDaNghiHoc.setImageResource(R.drawable.baseline_toggle_off_24);
        }
        else {
            btnDaNghiHoc.setImageResource(R.drawable.baseline_toggle_on_24);
        }


        tvMaSV.setText(sinhVien.getMasv());
        tvHoSV.setText(sinhVien.getHo());
        tvTenSV.setText(sinhVien.getTen());
        tvSDT.setText(sinhVien.getSdt());
        tvDiaChi.setText(sinhVien.getDiachi());

        btnDaNghiHoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                iQuanTriThongTin.updateDaNghiHoc(sinhVien.getMasv()).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            SinhVien sv = DSSV.get(position);
                            sv.setDanghihoc(!sv.getDanghihoc()); // Đảo ngược trạng thái
                            DSSV.set(position, sv);
                            notifyDataSetChanged();
                            Toast.makeText(mContext, "Thành công!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(mContext, "Không thành công!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        // Xử lý khi có lỗi xảy ra
                    }
                });
            }
        });

        btnChinhSuaSV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Hiển thị Dialog hoặc BottomSheetDialog để chỉnh sửa thông tin môn học
                showEditDialog(sinhVien);

            }
        });

        // Bắt sự kiện khi nhấn vào nút Xóa
        btnXoaSV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Hiển thị hộp thoại xác nhận xóa môn học
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("Xác nhận xóa sinh viên");
                builder.setMessage("Bạn có chắc muốn xóa sinh viên này?");
                builder.setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        iQuanTriThongTin.xoaSinhVien(sinhVien.getMasv()).enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                if (response.isSuccessful()) {
                                    DSSV.remove(position);
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
    private void showEditDialog(SinhVien sinhVien) {
        // Tạo và hiển thị Dialog hoặc BottomSheetDialog để chỉnh sửa thông tin môn học
        // Sử dụng LayoutInflater để inflate giao diện từ file layout XML
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View dialogView = inflater.inflate(R.layout.edit_sinh_vien, null);

        // Tìm kiếm và ánh xạ các thành phần giao diện trong dialogView
        EditText edtMaLop = dialogView.findViewById(R.id.loadMaLop);
        EditText edtMaSV = dialogView.findViewById(R.id.edtMaSVUpdate);
        EditText edtHoSV = dialogView.findViewById(R.id.edtHoSVUpdate);
        EditText edtTenSV = dialogView.findViewById(R.id.edtTenSVUpdate);
        RadioGroup radioGroupPhai = dialogView.findViewById(R.id.radioGroupPhaiUpdate);
        EditText edtDiaChi = dialogView.findViewById(R.id.edtDiaChiUpdate);
        EditText edtSDT = dialogView.findViewById(R.id.edtSDTUpdate);
        EditText edtNgaySinh = dialogView.findViewById(R.id.edtNgaySinhUpdate);
        EditText edtEmail = dialogView.findViewById(R.id.edtEmailUpdate);
//        ImageButton btnChonAnh = dialogView.findViewById(R.id.btnChonAnhUpdate);
        ivAnhSV = dialogView.findViewById(R.id.ivAnhSinhVienUpdate);
        // Tương tự với các thành phần khác
        Button btnLuu = dialogView.findViewById(R.id.btnLuuUpdate);
        Button btnHuy = dialogView.findViewById(R.id.btnHuyUpdate);
        // Hiển thị thông tin môn học trong các EditText
        edtMaLop.setText(sinhVien.getMalop());
        edtMaSV.setText(sinhVien.getMasv());
        edtHoSV.setText(sinhVien.getHo());
        edtTenSV.setText(sinhVien.getTen());
        if (sinhVien.getPhai()) {
            radioGroupPhai.check(R.id.radioButtonFemale); // Nếu là nữ
        } else {
            radioGroupPhai.check(R.id.radioButtonMale); // Nếu là nam
        }
        if(sinhVien.getHinhanh()!= null) {
            Glide.with(mContext)
                    .load(ApiClient.getBaseUrl()+ "auth/get-img?name="+sinhVien.getHinhanh())
                    .into(ivAnhSV);
        }

        edtDiaChi.setText(sinhVien.getDiachi());
        edtSDT.setText(sinhVien.getSdt());
        edtEmail.setText(sinhVien.getEmail());
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy");
        String ngaySinh = null;
        try {
            Date date = inputFormat.parse(sinhVien.getNgaysinh());
            ngaySinh= outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        edtNgaySinh.setText(ngaySinh);

        // Tạo và hiển thị Dialog hoặc BottomSheetDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();
        dialog.show();

        btnLuu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String maLop=edtMaLop.getText().toString();
                String maSV = edtMaSV.getText().toString();
                String hoSV = edtHoSV.getText().toString();
                String tenSV = edtTenSV.getText().toString();
                String diaChiSV = edtDiaChi.getText().toString();
                String sdtSV = edtSDT.getText().toString();
                String email = edtEmail.getText().toString();
                String inputDate = edtNgaySinh.getText().toString();

                String ngaySinh="";
                SimpleDateFormat inputFormat = new SimpleDateFormat("dd-MM-yyyy");

                // Định dạng của chuỗi ngày đầu ra
                SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");

                try {
                    // Chuyển đổi chuỗi ngày thành kiểu Date
                    Date date = inputFormat.parse(inputDate);

                    // Chuyển đổi thành chuỗi theo định dạng mới
                    ngaySinh= outputFormat.format(date);


                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Boolean phai;

                int selectedGenderId = radioGroupPhai.getCheckedRadioButtonId();
                if (selectedGenderId == R.id.radioButtonFemale) {
                    phai = true; // Nếu chọn giới tính nữ, gán giá trị là 1
                }
                else phai = false;
                SinhVien sinhVien1 = sinhVien;
                sinhVien1.setMasv(maSV);
                sinhVien1.setHo(hoSV);
                sinhVien1.setTen(tenSV);
                sinhVien1.setDiachi(diaChiSV);
                sinhVien1.setSdt(sdtSV);
                sinhVien1.setPhai(phai);
                sinhVien1.setNgaysinh(ngaySinh);
                sinhVien1.setEmail(email);

                String finalNgaySinh = ngaySinh;
                iQuanTriThongTin.updateSinhVien(sinhVien1).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            sinhVien.setMasv(maSV);
                            sinhVien.setHo(hoSV);
                            sinhVien.setTen(tenSV);
                            sinhVien.setDiachi(diaChiSV);
                            sinhVien.setSdt(sdtSV);
                            sinhVien.setPhai(phai);
                            sinhVien.setNgaysinh(finalNgaySinh);
                            sinhVien.setEmail(email);
                            Toast.makeText(mContext, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                            notifyDataSetChanged();

                            // Đóng dialog
                            dialog.dismiss();
                        } else {
                            Toast.makeText(mContext, "Cập nhật không thành công!", Toast.LENGTH_SHORT).show();
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

//        btnChonAnh.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Tạo Intent để chọn ảnh từ thư viện
//                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//                intent.setType("image/*");
//                // Gọi startActivityForResult để mở gallery và chọn ảnh
//                mActivity.startActivityForResult(Intent.createChooser(intent, "Chọn ảnh"), PICK_IMAGE_REQUEST);
//            }
//        });

    }




    public static void setBase64ImageToImageView(String base64ImageString, ImageView imageView) {
        // Giải mã chuỗi base64 thành mảng byte
        byte[] decodedString = Base64.decode(base64ImageString, Base64.DEFAULT);

        // Chuyển đổi mảng byte thành đối tượng Bitmap
        Bitmap bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

        // Đặt Bitmap vào ImageView để hiển thị ảnh
        imageView.setImageBitmap(bitmap);
    }

}

