package com.jennyni.fallproject.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jennyni.fallproject.Bean.AddDeviceBean;
import com.jennyni.fallproject.Bean.AskDevInfoBean;
import com.jennyni.fallproject.Bean.AskFallInfoBean;
import com.jennyni.fallproject.Bean.AskTrackBetweenBean;
import com.jennyni.fallproject.Bean.AskonBean;
import com.jennyni.fallproject.Bean.DelDeviceBean;
import com.jennyni.fallproject.Bean.RegisterBean;
import com.jennyni.fallproject.Bean.SetUpBean;
import com.jennyni.fallproject.Bean.UserChangePassBean;
import com.jennyni.fallproject.Bean.UserLoginBean;
import com.jennyni.fallproject.Bean.UserUpdateBean;

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
    private JsonParse(){
    }
    public static JsonParse getInstance(){
        synchronized (JsonParse.class) {
            if (instance == null) {
                instance = new JsonParse();
            }
        }
        return instance;
    }


    /**
     * 解析注册界面获取的JSON数据
     * @param json
     * @return
     */
    public String getRegisterInfo(String json){

        try {
            JSONObject jsonObject = new JSONObject(json);
            //第一层解析，先解析status字段
            int status=  jsonObject.optInt("status");
            String msg = jsonObject.optString("msg");
            if (status==200){
                String result = jsonObject.optString("result");
                //使用gson库解析
                Gson gson = new Gson();
                StringBuilder sb = new StringBuilder();

                RegisterBean.ResultBean registerinfo = gson.fromJson(result, RegisterBean.ResultBean.class);
                sb.append(registerinfo.getAccount()+" "+registerinfo.getName()+"\n");
                return registerinfo.toString();
            }else{
                //失败不处理，返回null给上层
                return msg;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }



    /**
     * 2.手机端登录
     * @param response
     * @return
     */
    public  UserLoginBean.ResultBean getuserLoginInfo(String response){
        //使用gson库解析JSON数据
        Gson gson = new Gson();
        UserLoginBean bean = gson.fromJson(response,UserLoginBean.class);
        //如果结果码为200，返回查询的设备信息
        UserLoginBean.ResultBean body = bean.getResult();
        if (200==bean.getStatus()){
            StringBuilder sb = new StringBuilder();
            sb.append("用户登录:"+"\n"+body.getAccount());
            return body;
        }else{
            //状态码不为200时，返回错误信息:账户或者密码错误
            return null;
        }
    }

    /**
     * 3.手机用户修改登录密码
     * @param response
     * @return
     */
    public UserChangePassBean.ResultBean getuserChangePassInfo(String response){
        //使用gson库解析JSON数据
        Gson gson = new Gson();
        UserChangePassBean bean = gson.fromJson(response,UserChangePassBean.class);
        //如果结果码为200，返回查询的设备信息
        UserChangePassBean.ResultBean body = bean.getResult();
        if (200==bean.getStatus()){
            StringBuilder sb = new StringBuilder();
            sb.append("更改密码:"+"\n"+body.getPhone_password()+"\n"+body.getUpdate_time()+"\n"+body.getReason());
            return body;
        }else{
            //状态码不为200时，返回错误信息:账户或者密码错误
            return null;
        }
    }

    /**
     * 4.手机端刷新设备信息 获取的JSON数据
     * @param response
     * @return
     */
    public  List<UserUpdateBean.ResultBean> getuserUpdateInfo(String response){
        //使用gson库解析JSON数据
        Gson gson = new Gson();
        UserUpdateBean bean = gson.fromJson(response,UserUpdateBean.class);
        //如果结果码为200，返回查询的设备信息
        if ("200".equals(bean.getStatus())) {
            StringBuilder sb = new StringBuilder();
            Type listType = new TypeToken<List<UserUpdateBean.ResultBean>>() {
            }.getType();
            List<UserUpdateBean.ResultBean> list = gson.fromJson(response, listType);
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
        }else {
            return null;
        }

    }




    /**
     * 5.手机端添加设备 的JSON数据
     * @param response
     * @return
     */
    public String getAddDeviceInfo(String response){
        //使用gson库解析JSON数据
        Gson gson = new Gson();
        AddDeviceBean bean = gson.fromJson(response,AddDeviceBean.class);
        //如果结果码为200，返回查询的设备信息
        AddDeviceBean.ResultBean body = bean.getResult();
        if (200==bean.getStatus()){
            StringBuilder sb = new StringBuilder();
            sb.append("添加设备:"+"\n"+body.getPhone_account()+"\n"+body.getCard_id()+"\n");
            return sb.toString();
        }else{
            //状态码不为200时，返回错误信息:绑定设备不存在或已添加
            return "添加设备:"+"\n"+ body.getReason();
        }
    }

    /**
     * 6.手机用户解绑删除设备获取的JSON数据
     * @param response
     * @return
     */
    public String getDelDeviceInfo(String response){
        //使用gson库解析JSON数据
        Gson gson = new Gson();
        DelDeviceBean bean = gson.fromJson(response,DelDeviceBean.class);
        //如果结果码为200，返回查询的设备信息
        DelDeviceBean.ResultBean body = bean.getResult();
        if (200==bean.getStatus()){
            StringBuilder sb = new StringBuilder();
            sb.append("解绑删除设备:"+"\n"+body.getReason());
            return sb.toString();
        }else{
            //状态码不为200时，返回错误信息:绑定设备不存在
            return "解绑删除设备:"+"\n"+String.valueOf(body.getReason());
        }
    }



    /**
     * 7.编辑设置跌倒设备参数信息  获取的JSON数据
     * @param response
     * @return
     */
    public String getSetupInfo(String response){
        //使用gson库解析JSON数据
        Gson gson = new Gson();
        SetUpBean bean = gson.fromJson(response,SetUpBean.class);
        //如果结果码为200，返回查询的设备信息
        SetUpBean.ResultBean body = bean.getResult();
        if (200==bean.getStatus()){
            StringBuilder sb = new StringBuilder();
            sb.append("查询设备信息:\n"+body.getDev_name()+"\n"+body.getDev_age()+"\n"+body.getDev_sex()+"\n"+
                    body.getDev_idcard() +"\n"+body.getDev_phone()+"\n"+body.getAddress()+"\n"+body.getCasehistory()+"\n"+
                    body.getGuardian()+"\n"+body.getPilltime1()+"\n"+body.getIsgeo()+"\n"+body.getGeocenter() +"\n"+
                    body.getGeoradius()+"\n"+body.getUpdate_time()+"\n");
            return sb.toString();
        }else{
            //状态码不为200时，返回错误信息
            return "查询设备信息:"+"\n"+body.getReason();
        }
    }

    /**
     * 8.请求查询设备信息（单个人信息）获取的JSON数据
     * @param response
     * @return
     */
    public String getAskDevInfo(String response){

        //使用gson库解析JSON数据
        Gson gson = new Gson();
        AskDevInfoBean bean = gson.fromJson(response,AskDevInfoBean.class);
        //如果结果码为200，返回查询的设备信息
        AskDevInfoBean.ResultBean body = bean.getResult();
        if (200==bean.getStatus()){
            StringBuilder sb = new StringBuilder();
            sb.append("查询设备信息:\n"+body.getOrganization()+"\n"+body.getDname()+"\n"+body.getDage()+"\n"+body.getDsex()
                    +"\n"+body.getIdcard()+"\n"+body.getDphone()+"\n"+body.getAddress()+"\n"+body.getCasehistory()
                    +"\n"+body.getGuardian()+"\n"+body.getPilltime1()+"\n"+body.getIsgeo()+"\n"+body.getGeocenter()
                    +"\n"+body.getGeoradius()+"\n");
            return sb.toString();
        }else{
            //状态码不为200时，返回错误信息
            return "查询设备信息:"+"\n"+body.getReason();
        }
    }

    /**
     * 9.查询设备最新在线时间获取的JSON数据
     * @param response
     * @return
     */
    public String getAskonInfo(String response){

        //使用gson库解析JSON数据
        Gson gson = new Gson();
        AskonBean bean = gson.fromJson(response,AskonBean.class);
        //如果结果码为200，返回查询的设备信息
        AskonBean.ResultBean body = bean.getResult();
        if (200==bean.getStatus()){
            StringBuilder sb = new StringBuilder();
            sb.append("查询设备最新时间:"+"\n"+body.getTime());
            return sb.toString();
        }else{
            //状态码不为200时，返回错误信息:绑定设备不存在
            return "查询设备最新时间:"+"\n"+body.getReason();
        }
    }



    /**
     * 10.查询设备最新数据：(跌倒报警与地理围栏报警)获取的JSON数据
     * @param response
     * @return
     */
    public AskFallInfoBean.ResultBean getAskFallInfo(String response){

        //使用gson库解析JSON数据
        Gson gson = new Gson();
        AskFallInfoBean bean = gson.fromJson(response, AskFallInfoBean.class);
        //如果结果码为200，返回查询的设备信息
        AskFallInfoBean.ResultBean body = bean.getResult();
        if (200==bean.getStatus()) {
            StringBuilder sb = new StringBuilder();
            sb.append("设备最新数据：(跌倒报警与地理围栏报警)"+"\n"+body.getId() + "\n" + body.getCard_id() +
                    "\n" + body.getLng() + "\n" + body.getLat() + "\n" + body.getRssi() + "\n" +
                    body.getPower() + "\n" + body.getFall() + "\n" + body.getAlert() + "\n" +
                    body.getSteps() + "\n" + body.getCalor() + "\n" + body.getTime() + "\n" +
                    body.getDname() + "\n" + body.getReason()+"\n");
            return body;
        } else {
            //状态码不为200时，返回错误信息:绑定设备不存在
            return null;
        }
    }

    /**
     * 12.查询历史设备运动轨迹  (d1>d2)获取的JSON数据
     * （d1,d2 表示过去 d1 天到过去 d2 天的数据，d1=d2=0 表示今天的数据，d1=d2=1 表示昨天）
     * @param response
     * @return
     */
    public List<AskTrackBetweenBean.ResultBean> getAskTraceBetweenInfo(String response){
        //使用gson库解析JSON数据
        Gson gson = new Gson();
        AskTrackBetweenBean bean = gson.fromJson(response,AskTrackBetweenBean.class);
        //如果结果码为200，返回查询的设备信息
        if (200==bean.getStatus()){
            StringBuilder sb = new StringBuilder();
            List<AskTrackBetweenBean.ResultBean> list= bean.getResult();
            for (AskTrackBetweenBean.ResultBean askTraBetweenBean : list) {
                sb.append("查询历史设备运动轨迹:"+"\n"+askTraBetweenBean.getLng()+"\n"+askTraBetweenBean.getLat()+"\n"+askTraBetweenBean.getTime()+"\n");
            }
            return list;
        }else if (404 == bean.getStatus()){
            //状态码为400时，返回错误信息:绑定关系不存在
            return null;
        }else {
            return null;
        }
    }

}
