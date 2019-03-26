package com.jennyni.fallproject.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jennyni.fallproject.Bean.AddDeviceBean;
import com.jennyni.fallproject.Bean.AskAllFallInfoBean;
import com.jennyni.fallproject.Bean.AskDevInfoBean;
import com.jennyni.fallproject.Bean.AskFallInfoBean;
import com.jennyni.fallproject.Bean.AskTodayTrackBean;
import com.jennyni.fallproject.Bean.AskTrackBetweenBean;
import com.jennyni.fallproject.Bean.AskonBean;
import com.jennyni.fallproject.Bean.DelDeviceBean;
import com.jennyni.fallproject.Bean.UserRegisterBean;
import com.jennyni.fallproject.Bean.SetUpBean;
import com.jennyni.fallproject.Bean.UploadBean;
import com.jennyni.fallproject.Bean.UserChangePassBean;
import com.jennyni.fallproject.Bean.UserLoginBean;
import com.jennyni.fallproject.Bean.UserUpdateBean;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;

/**
 * 服务器使用的是JSON数据，
 * 需要创建一个JsonParse类解析从服务器中获取的JSON数据。
 * Created by Jenny on 2019/2/19.
 */

public class JsonParse {

    private static JsonParse instance;

    private JsonParse() {
    }

    public static JsonParse getInstance() {
        synchronized (JsonParse.class) {
            if (instance == null) {
                instance = new JsonParse();
            }
        }
        return instance;
    }


    /**
     * 解析注册界面获取的JSON数据
     *
     * @param response
     * @return
     */
    public String getuserRegisterInfo(String response) {
        try{
            //使用gson库解析JSON数据
            Gson gson = new Gson();
            UserRegisterBean bean = gson.fromJson(response,UserRegisterBean.class);
            //如果结果码为200，返回查询的设备信息
            UserRegisterBean.ResultBean body = bean.getResult();
            if (200==bean.getStatus()){
                StringBuilder sb = new StringBuilder();
                sb.append("用户注册:"+"\n"+body.getAccount()+"\n"+body.getName());
                return sb.toString();
            }else{
                //状态码不为200时，返回错误信息:账户或者密码错误
                return "用户注册:"+"\n"+ body.getReason();
            }
        }catch (Exception e){
            return null;
        }



    }


    /**
     * 2.手机端登录
     *
     * @param response
     * @return
     */
    public UserLoginBean.ResultBean getuserLoginInfo(String response) {

        try{
            //使用gson库解析JSON数据
            Gson gson = new Gson();
            UserLoginBean bean = gson.fromJson(response, UserLoginBean.class);
            //如果结果码为200，返回查询的设备信息
            UserLoginBean.ResultBean body = bean.getResult();
            if (200 == bean.getStatus()) {
                StringBuilder sb = new StringBuilder();
                sb.append("用户登录:" + "\n" + body.getAccount());
                return body;
            } else {
                //状态码不为200时，返回错误信息:账户或者密码错误
                return null;
            }
        }catch (Exception e){
            return null;
        }


    }

    /**
     * 3.手机用户修改登录密码
     *
     * @param response
     * @return
     */
    public UserChangePassBean.ResultBean getuserChangePassInfo(String response) {
        try{
            //使用gson库解析JSON数据
            Gson gson = new Gson();
            UserChangePassBean bean = gson.fromJson(response, UserChangePassBean.class);
            //如果结果码为200，返回查询的设备信息
            UserChangePassBean.ResultBean body = bean.getResult();
            if (200 == bean.getStatus()) {
                StringBuilder sb = new StringBuilder();
                sb.append("更改密码:" + "\n" + body.getPhone_password() + "\n" + body.getUpdate_time() + "\n" + body.getReason());
                return body;
            } else {
                //状态码不为200时，返回错误信息:账户或者密码错误
                return null;
            }
        }catch (Exception e){
            return null;
        }


    }

    /**
     * 4.手机端刷新设备信息 获取的JSON数据
     *
     * @param response
     * @return
     */
    public List<UserUpdateBean.ResultBean> getuserUpdateInfo(String response) {
        try {
            //使用gson库解析JSON数据
            Gson gson = new Gson();
            //这里已经解析好了，
            UserUpdateBean bean = gson.fromJson(response, UserUpdateBean.class);
            //如果结果码为200，返回查询的设备信息
            if (bean.getStatus() == 200) {
                StringBuilder sb = new StringBuilder();
                List<UserUpdateBean.ResultBean> list = null;
                //下面两部分，注释一部分，不注释一部分，你对比参考下

                //这么写
//            list = bean.getResult();
//            for (UserUpdateBean.ResultBean updateBean : list) {
//                sb.append("刷新加载设备：" + "\n" + updateBean.getId() + "\n" + updateBean.getPhone_account() + "\n" +
//                        updateBean.getDev_name() + "\n" + updateBean.getOrganization() + "\n" + updateBean.getCard_id() + "\n" +
//                        updateBean.getCard_pass() + "\n" + updateBean.getStatus() + "\n" + updateBean.getHeadimage() + "\n" +
//                        updateBean.getDev_age() + "\n" + updateBean.getDev_sex() + "\n" + updateBean.getDev_idcard() + "\n" +
//                        updateBean.getDev_phone() + "\n" + updateBean.getAddress() + "\n" + updateBean.getCasehistory() + "\n" +
//                        updateBean.getGuardian() + "\n" + updateBean.getPilltime1() + "\n" + updateBean.getPilltime2() + "\n" +
//                        updateBean.getPilltime3() + "\n" + updateBean.getIsgeo() + "\n" + updateBean.getGeocenter() + "\n" +
//                        updateBean.getGeoradius() + "\n" + updateBean.getCreate_time() + "\n" + updateBean.getUpdate_time() + "\n" +
//                        updateBean.getDelete_time() + "\n" + updateBean.getIs_delete() + "\n");
//            }

                //或者这么写
                //这里就不用解析了，写了也没错，但是解析写错了，我改了，可以通过git找以前的版本对比下
                Type listType = new TypeToken<List<UserUpdateBean.ResultBean>>() {
                }.getType();
                try {
                    list = gson.fromJson(new JSONObject(response).optString("result"), listType);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                for (UserUpdateBean.ResultBean updateBean : list) {
                    sb.append("刷新加载设备：" + "\n" + updateBean.getId() + "\n" + updateBean.getPhone_account() + "\n" +
                            updateBean.getDev_name() + "\n" + updateBean.getOrganization() + "\n" + updateBean.getCard_id() + "\n" +
                            updateBean.getCard_pass() + "\n" + updateBean.getStatus() + "\n" + updateBean.getHeadimage() + "\n" +
                            updateBean.getDev_age() + "\n" + updateBean.getDev_sex() + "\n" + updateBean.getDev_idcard() + "\n" +
                            updateBean.getDev_phone() + "\n" + updateBean.getAddress() + "\n" + updateBean.getCasehistory() + "\n" +
                            updateBean.getGuardian() + "\n" + updateBean.getPilltime1() + "\n" + updateBean.getPilltime2() + "\n" +
                            updateBean.getPilltime3() + "\n" + updateBean.getIsgeo() + "\n" + updateBean.getGeocenter() + "\n" +
                            updateBean.getGeoradius() + "\n" + updateBean.getCreate_time() + "\n" + updateBean.getUpdate_time() + "\n" +
                            updateBean.getDelete_time() + "\n" + updateBean.getIs_delete() + "\n");
                }
                return list;
            } else {
                return null;
            }
        }catch (Exception e){
            return null;
        }

    }


    /**
     * 5.手机端添加设备 的JSON数据
     *
     * @param response
     * @return
     */
    public String getAddDeviceInfo(String response) {
        try{
            //使用gson库解析JSON数据
            Gson gson = new Gson();
            AddDeviceBean bean = gson.fromJson(response, AddDeviceBean.class);
            //如果结果码为200，返回查询的设备信息
            AddDeviceBean.ResultBean body = bean.getResult();
            if (200 == bean.getStatus()) {
                StringBuilder sb = new StringBuilder();
                sb.append("添加设备:" + "\n" + body.getPhone_account() + "\n" + body.getCard_id() + "\n");
                return sb.toString();
            } else {
                //状态码不为200时，返回错误信息:绑定设备不存在或已添加
                return "添加设备:" + "\n" + body.getReason();
            }
        }catch (Exception e){
            return null;
        }


    }

    /**
     * 6.手机用户解绑删除设备获取的JSON数据
     *
     * @param response
     * @return
     */
    public String getDelDeviceInfo(String response) {
        try{
            //使用gson库解析JSON数据
            Gson gson = new Gson();
            DelDeviceBean bean = gson.fromJson(response, DelDeviceBean.class);
            //如果结果码为200，返回查询的设备信息
            DelDeviceBean.ResultBean body = bean.getResult();
            if (200 == bean.getStatus()) {
                StringBuilder sb = new StringBuilder();
                sb.append("解绑删除设备:" + "\n" + body.getReason());
                return sb.toString();
            } else {
                //状态码不为200时，返回错误信息:绑定设备不存在
                return "解绑删除设备:" + "\n" + String.valueOf(body.getReason());
            }
        }catch (Exception e){
            return null;
        }
    }


    /**
     * 7.编辑设置跌倒设备参数信息  获取的JSON数据
     *
     * @param response
     * @return
     */
    public String getSetupInfo(String response) {

        try{
            //使用gson库解析JSON数据
            Gson gson = new Gson();
            SetUpBean bean = gson.fromJson(response, SetUpBean.class);
            //如果结果码为200，返回查询的设备信息
            SetUpBean.ResultBean body = bean.getResult();
            if (200 == bean.getStatus()) {
                StringBuilder sb = new StringBuilder();
                sb.append("查询设备信息:\n" + body.getDev_name() + "\n" + body.getDev_age() + "\n" + body.getDev_sex() + "\n" +
                        body.getDev_idcard() + "\n" + body.getDev_phone() + "\n" + body.getAddress() + "\n" + body.getCasehistory() + "\n" +
                        body.getGuardian() + "\n" + body.getPilltime1() + "\n" + body.getIsgeo() + "\n" + body.getGeocenter() + "\n" +
                        body.getGeoradius() + "\n" + body.getUpdate_time() + "\n");
                return sb.toString();
            } else {
                //状态码不为200时，返回错误信息
                return "查询设备信息:" + "\n" + body.getReason();
            }
        }catch (Exception e){
            return null;
        }

    }

    /**
     * 8.请求查询设备信息（单个人信息）获取的JSON数据
     *
     * @param response
     * @return
     */
    public String getAskDevInfo(String response) {

        try{
            //使用gson库解析JSON数据
            Gson gson = new Gson();
            AskDevInfoBean bean = gson.fromJson(response, AskDevInfoBean.class);
            //如果结果码为200，返回查询的设备信息
            AskDevInfoBean.ResultBean body = bean.getResult();
            if (200 == bean.getStatus()) {
                StringBuilder sb = new StringBuilder();
                sb.append("查询设备信息:\n" + body.getOrganization() + "\n" + body.getDname() + "\n" + body.getDage() + "\n" + body.getDsex()
                        + "\n" + body.getIdcard() + "\n" + body.getDphone() + "\n" + body.getAddress() + "\n" + body.getCasehistory()
                        + "\n" + body.getGuardian() + "\n" + body.getPilltime1() + "\n" + body.getIsgeo() + "\n" + body.getGeocenter()
                        + "\n" + body.getGeoradius() + "\n");
                return sb.toString();
            } else {
                //状态码不为200时，返回错误信息
                return "查询设备信息:" + "\n" + body.getReason();
            }
        }catch (Exception e){
            return null;
        }

    }

    /**
     * 9.查询设备最新在线时间获取的JSON数据
     *
     * @param response
     * @return
     */
    public String getAskonInfo(String response) {

        try{
            //使用gson库解析JSON数据
            Gson gson = new Gson();
            AskonBean bean = gson.fromJson(response, AskonBean.class);
            //如果结果码为200，返回查询的设备信息
            AskonBean.ResultBean body = bean.getResult();
            if (200 == bean.getStatus()) {
                StringBuilder sb = new StringBuilder();
                sb.append("查询设备最新时间:" + "\n" + body.getTime());
                return sb.toString();
            } else {
                //状态码不为200时，返回错误信息:绑定设备不存在
                return "查询设备最新时间:" + "\n" + body.getReason();
            }
        }catch (Exception e){
            return null;
        }

    }


    /**
     * 10.查询设备最新数据：(跌倒报警与地理围栏报警)获取的JSON数据
     *
     * @param response
     * @return
     */
    public AskFallInfoBean.ResultBean getAskFallInfo(String response) {

        try{
            //使用gson库解析JSON数据
            Gson gson = new Gson();
            AskFallInfoBean bean = gson.fromJson(response, AskFallInfoBean.class);
            //如果结果码为200，返回查询的设备信息
            AskFallInfoBean.ResultBean body = bean.getResult();
            if (200 == bean.getStatus()) {
                StringBuilder sb = new StringBuilder();
                sb.append("设备最新数据：(跌倒报警与地理围栏报警)" + "\n" + body.getId() + "\n" + body.getCard_id() +
                        "\n" + body.getLng() + "\n" + body.getLat() + "\n" + body.getRssi() + "\n" +
                        body.getPower() + "\n" + body.getFall() + "\n" + body.getAlert() + "\n" +
                        body.getSteps() + "\n" + body.getCalor() + "\n" + body.getTime() + "\n" +
                        body.getDname() + "\n" + body.getReason() + "\n");
                return body;
            } else {
                //状态码不为200时，返回错误信息:绑定设备不存在
                return null;
            }
        }catch (Exception e){
            return null;
        }

    }



    /**
     * 12.查询历史设备运动轨迹  (d1>d2)获取的JSON数据
     * （d1,d2 表示过去 d1 天到过去 d2 天的数据，d1=d2=0 表示今天的数据，d1=d2=1 表示昨天）
     *
     * @param response
     * @return
     */
    public List<AskTodayTrackBean.ResultBean> getAskTodayTraceInfo(String response) {

        try{
//使用gson库解析JSON数据
            Gson gson = new Gson();
            AskTodayTrackBean bean = null;
            try {
                bean = gson.fromJson(response, AskTodayTrackBean.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (bean == null) return null;
            //如果结果码为200，返回查询的设备信息
            if (200 == bean.getStatus()) {
                StringBuilder sb = new StringBuilder();
                List<AskTodayTrackBean.ResultBean> list = bean.getResult();
                for (AskTodayTrackBean.ResultBean askTodayTraceBean : list) {
                    sb.append("查询历史设备运动轨迹:" + "\n" + askTodayTraceBean.getLng() + "\n" +
                            askTodayTraceBean.getLat() + "\n" + askTodayTraceBean.getTime() + "\n");
                }
                return list;
            } else if (404 == bean.getStatus()) {
                //状态码为400时，返回错误信息:绑定关系不存在
                return null;
            } else {
                return null;
            }
        }catch (Exception e){
            return null;
        }

    }

    /**
     * 12.查询历史设备运动轨迹  (d1>d2)获取的JSON数据
     * （d1,d2 表示过去 d1 天到过去 d2 天的数据，d1=d2=0 表示今天的数据，d1=d2=1 表示昨天）
     *
     * @param response
     * @return
     */
    public List<AskTrackBetweenBean.ResultBean> getAskTraceBetweenInfo(String response) {

        try{
//使用gson库解析JSON数据
            Gson gson = new Gson();
            AskTrackBetweenBean bean = null;
            try {
                bean = gson.fromJson(response, AskTrackBetweenBean.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (bean == null) return null;
            //如果结果码为200，返回查询的设备信息
            if (200 == bean.getStatus()) {
                StringBuilder sb = new StringBuilder();
                List<AskTrackBetweenBean.ResultBean> list = bean.getResult();
                for (AskTrackBetweenBean.ResultBean askTraBetweenBean : list) {
                    sb.append("查询历史设备运动轨迹:" + "\n" + askTraBetweenBean.getLng() + "\n" + askTraBetweenBean.getLat() + "\n" + askTraBetweenBean.getTime() + "\n");
                }
                return list;
            } else if (404 == bean.getStatus()) {
                //状态码为400时，返回错误信息:绑定关系不存在
                return null;
            } else {
                return null;
            }
        }catch (Exception e){
            return null;
        }

    }

    /**
     * 13.手机端刷新设备信息 获取的JSON数据
     *
     * @param response
     * @return
     */
    public List<AskAllFallInfoBean.ResultBean> getAskAllFallInfo(String response) {
        try{
            try {
                //使用gson库解析JSON数据
                Gson gson = new Gson();
                AskAllFallInfoBean bean = gson.fromJson(response, AskAllFallInfoBean.class);
                //如果结果码为200，返回查询的设备信息
                if (bean.getStatus() == 200) {
                    StringBuilder sb = new StringBuilder();
                    List<AskAllFallInfoBean.ResultBean> list = bean.getResult();
                    for (AskAllFallInfoBean.ResultBean alldevBean : list) {
                        sb.append("请求所有报警设备：" + "\n" + alldevBean.getId() + "\n" + alldevBean.getCard_id() + "\n" +
                                alldevBean.getName() + "\n" + alldevBean.getLng() + "\n" + alldevBean.getLat() + "\n" +
                                alldevBean.getRssi() + "\n" + alldevBean.getPower() + "\n" + alldevBean.getFall() + "\n"
                                + alldevBean.getAlert() + "\n" + alldevBean.getFence() + "\n" + alldevBean.getLoctype() + "\n"
                                + alldevBean.getSteps() + "\n" + alldevBean.getCalor() + "\n" + alldevBean.getTime() + "\n");
                    }
                    return list;
                } else {
                    return null;
                }

            }catch (Exception e){
                return null;
            }
        }catch (Exception e){
            return null;
        }



    }


    /**
     * 14.查询设备最新数据：(跌倒报警与地理围栏报警)获取的JSON数据
     *
     * @param response
     * @return
     */
    public UploadBean.ResultBean getUploadInfo(String response) {
        try{
            //使用gson库解析JSON数据
            Gson gson = new Gson();
            UploadBean bean = gson.fromJson(response, UploadBean.class);
            //如果结果码为200，返回查询的设备信息
            UploadBean.ResultBean body = bean.getResult();
            if (200 == bean.getStatus()) {
                StringBuilder sb = new StringBuilder();
                sb.append("数据校验：(跌倒报警与地理围栏报警)" + "\n" + body.getId() + "\n" + body.getCard_id() +
                        "\n" + body.getLng() + "\n" + body.getLat() + "\n" + body.getRssi() + "\n" +
                        body.getPower() + "\n" + body.getFall() + "\n" + body.getAlert() + "\n" +
                        body.getSteps() + "\n" + body.getCalor() + "\n" + body.getTime() + "\n" +
                        body.getDname() + "\n" + body.getReason() + "\n");
                return body;
            } else {
                //状态码不为200时，返回错误信息:绑定设备不存在
                return null;
            }
        }catch (Exception e){
            return null;
        }

    }

}
