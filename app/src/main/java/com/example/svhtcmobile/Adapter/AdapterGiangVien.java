package com.example.svhtcmobile.Adapter;

import static android.app.Activity.RESULT_OK;
import static android.provider.MediaStore.ACTION_IMAGE_CAPTURE;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
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
import androidx.core.app.ActivityCompat;

import com.bumptech.glide.Glide;
import com.example.svhtcmobile.Api.ApiClient;
import com.example.svhtcmobile.Api.apiService.IQuanTriThongTin;
import com.example.svhtcmobile.Model.GiangVien;
import com.example.svhtcmobile.R;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AdapterGiangVien extends ArrayAdapter<GiangVien> {

    private Context mContext;
    private int mResource;
    private Activity activity;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Bitmap photo;
    ImageView ivAnhGV;
    IQuanTriThongTin iQuanTriThongTin;

    private List<GiangVien> DSGV; // Không cần khai báo danh sách mới ở đây

    private List<GiangVien> filteredGiangVienList;
    public AdapterGiangVien(Context context, int resource, List<GiangVien> objects , IQuanTriThongTin iQuanTriThongTin,Activity activity) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
        DSGV = objects; // Sử dụng danh sách được truyền vào
        this.iQuanTriThongTin = iQuanTriThongTin;
        this.activity = activity;
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
        ImageButton btnChonAnh = dialogView.findViewById(R.id.btnChonAnhUpdateGV);
        ImageButton btnChupAnh = dialogView.findViewById(R.id.btnCaptureUpdateGV);
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
        if (giangVien.getHocvi().equals("Cử nhân")) {
            radioGroupHocVi.check(R.id.radioButtonCuNhan);
        }
        else if (giangVien.getHocvi().equals("Thạc sĩ")) {
            radioGroupHocVi.check(R.id.radioButtonThacSi);
        }
        else if (giangVien.getHocvi().equals("Tiến sĩ")){
            radioGroupHocVi.check(R.id.radioButtonTienSi);
        }
        else radioGroupHocVi.check(R.id.radioButtonTienSiKH);
        if (giangVien.getHinhanh()!= null){
            Glide.with(mContext)
                    .load(ApiClient.getBaseUrl()+ "thong-tin/sinh-vien/get-img?name="+giangVien.getHinhanh())
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
                if (selectedHocViId == R.id.radioButtonCuNhan) {
                    hocvi = "Cử nhân";
                }
                else if (selectedHocViId == R.id.radioButtonThacSi) {
                    hocvi = "Thạc sĩ";
                }
                else if (selectedHocViId == R.id.radioButtonTienSi) {
                    hocvi = "Tiến sĩ";
                }
                else hocvi = "Tiến sĩ khoa học";
                GiangVien new1 = new GiangVien(magv, ho, ten, hocham,hocvi,chuyenmon, sdt, giangVien.getHinhanh(),email,maKhoa1);
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                photo.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                byte[] byteArray = byteArrayOutputStream.toByteArray();
                RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), byteArray);
                MultipartBody.Part in = MultipartBody.Part.createFormData("img", "", requestBody);
                Gson gson = new Gson();
                String dataGV = gson.toJson(new1);
                RequestBody gv1 = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), dataGV);

                iQuanTriThongTin.updateGiangVien(gv1,in).enqueue(new Callback<JsonObject>()  {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        JsonObject jsOb= response.body();
                        if (response.isSuccessful()) {
                            giangVien.setHo(ho);
                            giangVien.setTen(ten);
                            giangVien.setHocham(hocham);
                            giangVien.setHocvi(hocvi);
                            giangVien.setChuyenmon(chuyenmon);
                            giangVien.setSdt(sdt);
                            giangVien.setEmail(email);
                            giangVien.setHinhanh(jsOb.get("filename").getAsString());
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
                    public void onFailure(Call<JsonObject> call, Throwable t) {
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
        btnChonAnh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                activity.startActivityForResult(intent, PICK_IMAGE_REQUEST);
            }
        });

        btnChupAnh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(ACTION_IMAGE_CAPTURE);
                if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CAMERA}, 1);
                    return;
                }
                activity.startActivityForResult(cameraIntent, 99);
            }
        });


    }
    public void handleActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            try {
                photo = MediaStore.Images.Media.getBitmap(mContext.getContentResolver(), uri);
                if (ivAnhGV != null && photo != null) {
                    ivAnhGV.setImageBitmap(photo);
                }
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("TAG", "Error loading image: " + e.getMessage());
            }
        }

        if( requestCode == 99 && resultCode == Activity.RESULT_OK) {
            photo = (Bitmap) data.getExtras().get("data");
            ivAnhGV.setImageBitmap(photo);
        }
    }
}
