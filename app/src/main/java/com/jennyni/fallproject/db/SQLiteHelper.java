package com.jennyni.fallproject.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.jennyni.fallproject.sqlite.SQLiteHelper.U_USERINFO;

/**
 *
 * Created by Jenny on 2019/1/25.
 */

public class SQLiteHelper extends SQLiteOpenHelper {

    private static final int DB_VERSION = 1;
    public static String DB_NAME = "fall.db";
    public static final String U_UserInfo = "userinfo";     //用户信息
    public static final String U_DeviceInfo = "deviceinfo";     //设备信息
    public static final String FALL_Info = "fallinfo";     //设备信息
    public SQLiteHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        /**
         * 创建用户信息表
         */
        db.execSQL("CREATE TABLE  IF NOT EXISTS " + U_USERINFO + "( "
                + "_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "account text, "       //App 注册人手机号
                + "pass text, "          //App 登录密码
                + "name text "           //App 登录昵称
                + ")");
        /**
         * 创建设备信息表
         */
        db.execSQL("CREATE TABLE  IF NOT EXISTS " + U_DeviceInfo + "( "
                + "_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "cardid text, "       //设备唯一标识码
                + "cardpass text, "     //设备密码
                + "dname text, "        //设备用户昵称
                + "age text, "          //设备用户年龄
                + "sex text, "          //设备用户性别
                + "idcard text, "       //设备用户身份证
                + "devphone text, "     //设备用户电话
                + "address text,"       //设备用户住址
                + "casehistory text, "  //设备用户病史
                + "guardian text ,"     //监护人手机号"
                + "pilltime1 text,"     //吃药时间
                + "isgeo text,"         //是否启用地理围栏
                + "geocenter text, "    //地理圆形围栏中心点
                + "georadius text ,"    //圆形围栏半径
                + "geopoints text,"     //多边形围栏坐标点
                + "d text,"             //查询前 d 天到现在的数据
                + "creat_time,"         //创建时间
                + "update_time,"        //更新时间
                + "delete_time"         //删除时间
                + ")");

        /**
         * 创建跌倒信息表
         */
        db.execSQL("CREATE TABLE  IF NOT EXISTS " + FALL_Info + "( "
                + "_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "id text, "         //
                + "cardid text, "     //设备号
                + "dname text,"        //设备用户
                + "lng text, "        //经度
                + "lat text, "        //纬度
                + "rssi text, "       //信号
                + "power text, "      //电量
                + "fall text, "       //跌倒
                + "alert text,"       //地理围栏
                + "steps text, "      //步数
                + "calor text ,"      //卡路里
                + "time text"        //时间

                + ")");

    }


    /**
     * 当数据库版本号增加时才会调用此方法
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + U_USERINFO);
        db.execSQL("DROP TABLE IF EXISTS " + U_DeviceInfo);
        db.execSQL("DROP TABLE IF EXISTS " + FALL_Info);
        onCreate(db);
    }
}
