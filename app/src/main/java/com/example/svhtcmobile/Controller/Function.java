package com.example.svhtcmobile.Controller;

import java.time.LocalDate;
import java.time.Month;
import java.util.HashMap;
import java.util.Map;

public class Function {
    public static Map<String, Object> layHocKyHienTai(){
        Map<String, Object > thongTin=new HashMap<>();
        int semester;
        String academicYear;
        LocalDate time;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            time = LocalDate.now();
        }
        else{
            System.out.println("Phien ban khong phu hop");
            return null;
        }
        int year=time.getYear();
        Month month=time.getMonth();
        if((month.getValue()>month.JUNE.getValue())&&(month.getValue()<=month.DECEMBER.getValue())){
            semester=1;
            if(month.getValue()<=month.DECEMBER.getValue()){
                academicYear=String.valueOf(year)+"-"+String.valueOf(year+1);
            }
            else{
                academicYear=String.valueOf(year-1)+"-"+String.valueOf(year);
            }
        }else {
            academicYear=String.valueOf(year-1)+"-"+String.valueOf(year);
            if(month.getValue()<=month.JULY.getValue()){
                semester=2;
            }
            else{
                semester=3;
            }
        }
        thongTin.put("nienKhoa",academicYear);
        thongTin.put("hocKy",semester);
        return thongTin;
    }
}
