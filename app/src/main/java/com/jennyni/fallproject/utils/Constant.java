package com.jennyni.fallproject.utils;

/**
 * Constant类存放各个界面向服务器请求数据所需要的接口地址
 *
 * Created by Jenny on 2019/1/22.
 */

public class Constant {
    //内网接口
    public static final String BASE_WEBSITE = "http://www.phyth.cn/index/fall";

     //用户登录界面部分：
    //注册接口
    public static final String REQUEST_REGISTER_USER_URL = "/userRegister";
    //登录接口
    public static final String REQUEST_LOGIN_USER_URL = "/userlogin";
    //改密接口：
    public static final String REQUEST_PSW_USER_URL = "/userChangePass";

    //跌倒设备部分：
    public static final String REQUEST_UPDATE_USER_URL = "/userUpdate";
    //添加设备接口
    public static final String REQUEST_ADD_DEVICE_URL = "/addDevice";
    //解绑设备接口：
    public static final String REQUEST_DEL_DEVICE_URL = "/delDevice";
    //编辑设备位置信息接口：
    public static final String REQUEST_SETUP_DEVICE_URL = "/setup";
    //查询设备位置信息接口：
    public static final String REQUEST_ASKDEVINFO_DEVICE_URL = "/askdevinfo";
    //设备上线时间查询接口：
    public static final String REQUEST_ASKON_DEVICE_URL = "/askon";
    //查询设备最新数据
    public static final String REQUEST_ASKFALLINFO_DEVICE_URL = "/askfallinfo";

    //查询所有设备最新数据（findfragment）
    public static final String REQUEST_ASKALLFALLINFO_DEVICE_URL = "/askAllFallInfo";

    //查询今日运动轨迹：
    public static final String REQUEST_TODAY_TRACE_URL = "/askTodayTrack";

    //查询设备运动轨迹接口：
    public static final String REQUEST_ASKTRACK_DEVICE_URL = "/askTrack";
    //查询运动轨迹 d1，d2接口：
    public static final String REQUEST_ASKTRACKBETWEEN_DEVICE_URL ="/askTrackBetween";



}
