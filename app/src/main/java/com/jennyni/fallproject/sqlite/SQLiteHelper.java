package com.jennyni.fallproject.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 *
 * Created by Jenny on 2018/12/10.
 */

public class SQLiteHelper extends SQLiteOpenHelper {

    private static final int DB_VRESION = 1;
    public static String DB_NAME = "fallguard.db";
    public static final String U_USERINFO = "userinfo";     //手机用户信息
    public static final String DEVICE_INFO= "deviceinfo";   //设备用户信息
    public static final String FALLMSG_INFO= "fallmsginfo";
    public SQLiteHelper(Context context) {
        super(context, DB_NAME, null, DB_VRESION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE  IF NOT EXISTS " + U_USERINFO + "( "
                        + "_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + "account text,"     //App注册人
                        + "pass text,"        //App登录密码
                        + "name text,"        //App 登录昵称
                        + "head text"
                        + ")");

        db.execSQL("CREATE TABLE  IF NOT EXISTS " + DEVICE_INFO + "( "
                + "_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "account text,"       //手机注册人
                + "cardid text,"        //设备唯一标识码（绑定用户）
                + "cardpass text,"      //设备密码（添加设备用） 123456 数字，6-8 位
                + "dname text,"         //设备用户昵称
                + "headimage text,"
                + "age text,"           //设备用户年龄
                + "sex text,"           //设备用户性别
                + "idcard text,"        //设备用户身份证
                + "devphone text,"      //设备用户电话
                + "address text,"       //设备用户住址
                + "casehistory text,"   //设备用户病史
                + "guardian text"       //监护人手机号码
                + ")");

//        /**
//         * 创建跌倒信息表
//         */
        db.execSQL("CREATE TABLE  IF NOT EXISTS " + FALLMSG_INFO + "( "
                + "_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "id INTEGER, "            // id
                + "cardid VARCHAR, "        // cardid
                + "usrame VARCHAR, "        //用户名
                + "fall INTEGER,"           //跌倒报警
                + "alert INTEGER,"          //围栏报警
                + "time VARCHAR"            //时间
                + ")");

    }

    /**
     * 当数据库版本号增加时才会调用此方法
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + U_USERINFO);
        db.execSQL("DROP TABLE IF EXISTS " + DEVICE_INFO);
        db.execSQL("DROP TABLE IF EXISTS " + FALLMSG_INFO);
        onCreate(db);
    }
}
