package com.jennyni.fallproject.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jennyni.fallproject.Bean.AskFallInfoBean;
import com.jennyni.fallproject.Bean.UserUpdateBean;
import com.jennyni.fallproject.db.SQLiteHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * 专门用于操作数据库
 * 当读取用户资料或者对用户进行更改时，需要对数据库进行操作。
 * Created by Jenny on 2019/1/25.
 */

public class DBUtils {
    public static final String PWD = "pass";
    private static DBUtils instance = null;
    private static SQLiteHelper helper;
    private static SQLiteDatabase db;
    private DBUtils(Context context){
        helper = new SQLiteHelper(context);
        db = helper.getWritableDatabase();
    }

    public static DBUtils getInstance(Context context) {
        if (instance == null){
         instance = new DBUtils(context);
        }
        return instance;
    }




    /**
     * 保存设备加载信息
     */
    public void saveUpdateDevInfo(List<UserUpdateBean.ResultBean> list) {
        Cursor cursor = db.rawQuery("SELECT * FROM " + SQLiteHelper.U_DeviceInfo, null);
        if (cursor.getCount() != 0)//添加数据时，如果星座表中有数据，则在添加新数据之前需删除旧数据
        {
            //删除表中的数据
            db.execSQL("DELETE FROM " + SQLiteHelper.U_DeviceInfo);
        }
        for (UserUpdateBean.ResultBean bean : list) {
            ContentValues cv = new ContentValues();
            cv.put("id", bean.getId());
            cv.put("cardid",bean.getCard_id());
            cv.put("cardpass",bean.getCard_pass());
            cv.put("dname", bean.getDev_name());
            cv.put("age",bean.getDev_age());
            cv.put("sex",bean.getDev_sex());
            cv.put("idcard",bean.getCard_id());
            cv.put("address", bean.getAddress());
            cv.put("casehistory", bean.getCasehistory());
            cv.put("guardian", bean.getGuardian());
            cv.put("pilltime1",bean.getPilltime1());
            cv.put("isgeo", bean.getIsgeo());
            cv.put("geocenter", bean.getGeocenter());
            cv.put("georadius", bean.getGeoradius());

            db.insert(SQLiteHelper.U_DeviceInfo, null, cv);
        }
    }

    /**
     * 根据cardid获取设备用户信息
     */
    public UserUpdateBean.ResultBean getLoadDeviceInfo(int cardid) {
        String sql = "SELECT * FROM " + SQLiteHelper.U_DeviceInfo + " WHERE cardid=?";
        Cursor cursor = db.rawQuery(sql, new String[]{cardid + ""});
        UserUpdateBean.ResultBean bean = null;
        while (cursor.moveToNext()) {
            bean = new UserUpdateBean.ResultBean();
            bean.setCard_id(cursor.getString(cursor.getColumnIndex("cardid")));
            bean.setCard_pass(cursor.getInt(cursor.getColumnIndex("cardpass")));
            bean.setDev_name(cursor.getString(cursor.getColumnIndex("dname")));
            bean.setDev_age(cursor.getInt(cursor.getColumnIndex("age")));
            bean.setDev_sex(cursor.getString(cursor.getColumnIndex("sex")));
            bean.setDev_idcard(cursor.getString(cursor.getColumnIndex("idcard")));
            bean.setDev_phone(cursor.getString(cursor.getColumnIndex("devphone")));
            bean.setAddress(cursor.getString(cursor.getColumnIndex("address")));
            bean.setCasehistory(cursor.getString(cursor.getColumnIndex("casehistory")));
            bean.setGuardian(cursor.getString(cursor.getColumnIndex("guardian")));
            bean.setIsgeo(cursor.getInt(cursor.getColumnIndex("isgeo")));
            bean.setGeocenter(cursor.getString(cursor.getColumnIndex("geocenter")));
            bean.setGeoradius(cursor.getInt(cursor.getColumnIndex("georadius")));
            bean.setGeopoints(cursor.getString(cursor.getColumnIndex("geopoints")));
        }
        cursor.close();
        return bean;
    }

//    /**
//     * 保存报警信息
//     */
//    public void saveAskFallInfo(AskDevInfoBean.ResultBean bean, String cardid) {
//        ContentValues cv = new ContentValues();
//        cv.put("id",bean.getId());
//        cv.put("cardid",bean.getCard_id);
//        cv.put("dname",bean.getDname());
//        cv.put("lng", bean.getLng());
//        cv.put("lat", bean.getLat());
//        cv.put("rssi", bean.getRssi());
//        cv.put("power", bean.getPower());
//        cv.put("fall", bean.getFall());
//        cv.put("alert", bean.getAlert());
//        cv.put("steps", bean.getSteps);
//        cv.put("calor", bean.getCalor());
//        cv.put("time", bean.getTime());
//        db.insert(SQLiteHelper.FALL_Info, null, cv);
//    }


    /**
     * 获取报警信息
     */
    public AskFallInfoBean.ResultBean getAskFallInfo(String account) {
        String sql = "SELECT * FROM " + SQLiteHelper.FALL_Info + " WHERE id=? ";
        Cursor cursor = db.rawQuery(sql, new String[]{account+""});
        AskFallInfoBean.ResultBean bean = null;
        while (cursor.moveToNext()) {
            bean = new AskFallInfoBean.ResultBean();
            bean.setId(cursor.getInt(cursor.getColumnIndex("id")));
            bean.setCard_id(cursor.getString(cursor.getColumnIndex("cardid")));
            bean.setDname(cursor.getString(cursor.getColumnIndex("dname")));
            bean.setLng(cursor.getString(cursor.getColumnIndex("lng")));
            bean.setLat(cursor.getString(cursor.getColumnIndex("lat")));
            bean.setRssi(cursor.getInt(cursor.getColumnIndex("rssi")));
            bean.setPower(cursor.getInt(cursor.getColumnIndex("power")));
            bean.setFall(cursor.getInt(cursor.getColumnIndex("fall")));
            bean.setAlert(cursor.getInt(cursor.getColumnIndex("alert")));
            bean.setTime(cursor.getString(cursor.getColumnIndex("time")));
        }
        cursor.close();
        return bean;
    }


}
