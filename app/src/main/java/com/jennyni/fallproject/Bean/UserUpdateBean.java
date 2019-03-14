package com.jennyni.fallproject.Bean;

import java.util.List;

/**
 * Created by Jenny on 2019/3/8.
 */

public class UserUpdateBean {


    /**
     * status : 200
     * msg : success
     * result : [{"id":2,"phone_account":"18860900316","dev_name":"peter","organization":"康复室","card_id":"18255180000","card_pass":123456,"status":"已启用","headimage":null,"dev_age":29,"dev_sex":"男","dev_idcard":"341221199001028541","dev_phone":"18860900316","address":"康复室","casehistory":"康复室","guardian":"13330000888","pilltime1":81080,"pilltime2":81080,"pilltime3":81080,"isgeo":0,"geocenter":"115.672126,38.817129","georadius":1000,"geopoints":null,"create_time":"2018/12/03","update_time":"2019/03/01","delete_time":"2019/03/01","is_delete":0}]
     */

    private int status;
    private String msg;
    private List<ResultBean> result;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<ResultBean> getResult() {
        return result;
    }

    public void setResult(List<ResultBean> result) {
        this.result = result;
    }

    public static class ResultBean {
        /**
         * id : 2
         * phone_account : 18860900316
         * dev_name : peter
         * organization : 康复室
         * card_id : 18255180000
         * card_pass : 123456
         * status : 已启用
         * headimage : null
         * dev_age : 29
         * dev_sex : 男
         * dev_idcard : 341221199001028541
         * dev_phone : 18860900316
         * address : 康复室
         * casehistory : 康复室
         * guardian : 13330000888
         * pilltime1 : 81080
         * pilltime2 : 81080
         * pilltime3 : 81080
         * isgeo : 0
         * geocenter : 115.672126,38.817129
         * georadius : 1000
         * geopoints : null
         * create_time : 2018/12/03
         * update_time : 2019/03/01
         * delete_time : 2019/03/01
         * is_delete : 0
         *
         */

        private int id;
        private String phone_account;
        private String dev_name;
        private String organization;
        private String card_id;
        private int card_pass;
        private String status;
        private Object headimage;
        private int dev_age;
        private String dev_sex;
        private String dev_idcard;
        private String dev_phone;
        private String address;
        private String casehistory;
        private String guardian;
        private int pilltime1;
        private int pilltime2;
        private int pilltime3;
        private int isgeo;
        private String geocenter;
        private int georadius;
        private Object geopoints;
        private String create_time;
        private String update_time;
        private String delete_time;
        private int is_delete;


        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getPhone_account() {
            return phone_account;
        }

        public void setPhone_account(String phone_account) {
            this.phone_account = phone_account;
        }

        public String getDev_name() {
            return dev_name;
        }

        public void setDev_name(String dev_name) {
            this.dev_name = dev_name;
        }

        public String getOrganization() {
            return organization;
        }

        public void setOrganization(String organization) {
            this.organization = organization;
        }

        public String getCard_id() {
            return card_id;
        }

        public void setCard_id(String card_id) {
            this.card_id = card_id;
        }

        public int getCard_pass() {
            return card_pass;
        }

        public void setCard_pass(int card_pass) {
            this.card_pass = card_pass;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public Object getHeadimage() {
            return headimage;
        }

        public void setHeadimage(Object headimage) {
            this.headimage = headimage;
        }

        public int getDev_age() {
            return dev_age;
        }

        public void setDev_age(int dev_age) {
            this.dev_age = dev_age;
        }

        public String getDev_sex() {
            return dev_sex;
        }

        public void setDev_sex(String dev_sex) {
            this.dev_sex = dev_sex;
        }

        public String getDev_idcard() {
            return dev_idcard;
        }

        public void setDev_idcard(String dev_idcard) {
            this.dev_idcard = dev_idcard;
        }

        public String getDev_phone() {
            return dev_phone;
        }

        public void setDev_phone(String dev_phone) {
            this.dev_phone = dev_phone;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getCasehistory() {
            return casehistory;
        }

        public void setCasehistory(String casehistory) {
            this.casehistory = casehistory;
        }

        public String getGuardian() {
            return guardian;
        }

        public void setGuardian(String guardian) {
            this.guardian = guardian;
        }

        public int getPilltime1() {
            return pilltime1;
        }

        public void setPilltime1(int pilltime1) {
            this.pilltime1 = pilltime1;
        }

        public int getPilltime2() {
            return pilltime2;
        }

        public void setPilltime2(int pilltime2) {
            this.pilltime2 = pilltime2;
        }

        public int getPilltime3() {
            return pilltime3;
        }

        public void setPilltime3(int pilltime3) {
            this.pilltime3 = pilltime3;
        }

        public int getIsgeo() {
            return isgeo;
        }

        public void setIsgeo(int isgeo) {
            this.isgeo = isgeo;
        }

        public String getGeocenter() {
            return geocenter;
        }

        public void setGeocenter(String geocenter) {
            this.geocenter = geocenter;
        }

        public int getGeoradius() {
            return georadius;
        }

        public void setGeoradius(int georadius) {
            this.georadius = georadius;
        }

        public Object getGeopoints() {
            return geopoints;
        }

        public void setGeopoints(Object geopoints) {
            this.geopoints = geopoints;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }

        public String getUpdate_time() {
            return update_time;
        }

        public void setUpdate_time(String update_time) {
            this.update_time = update_time;
        }

        public String getDelete_time() {
            return delete_time;
        }

        public void setDelete_time(String delete_time) {
            this.delete_time = delete_time;
        }

        public int getIs_delete() {
            return is_delete;
        }

        public void setIs_delete(int is_delete) {
            this.is_delete = is_delete;
        }


    }
}
