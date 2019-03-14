package com.jennyni.fallproject.Bean;

/**
 * 7.编辑设置跌倒设备参数信息
 * Created by Jenny on 2019/3/8.
 */

public class SetUpBean {


    /**
     * status : 200
     * msg : success
     * result : {"dev_name":"zhu","dev_age":"66","dev_sex":"男","dev_idcard":"3412211994 11178888","dev_phone":"15357875321","address":"中国苏州科技城","casehistory":"锁骨断裂过","guardian":"13330000888","pilltime1 ":"081080","isgeo":"1","geocenter":"115.672126,38.817129","georadius":"1000","update_time":"2019/03/04"}
     */

    private int status;
    private String msg;
    private ResultBean result;

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

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public static class ResultBean {
        /**
         * dev_name : zhu
         * dev_age : 66
         * dev_sex : 男
         * dev_idcard : 3412211994 11178888
         * dev_phone : 15357875321
         * address : 中国苏州科技城
         * casehistory : 锁骨断裂过
         * guardian : 13330000888
         * pilltime1  : 081080
         * isgeo : 1
         * geocenter : 115.672126,38.817129
         * georadius : 1000
         * update_time : 2019/03/04
         * reason:
         */

        private String dev_name;
        private String dev_age;
        private String dev_sex;
        private String dev_idcard;
        private String dev_phone;
        private String address;
        private String casehistory;
        private String guardian;
        private String pilltime1;
        private String isgeo;
        private String geocenter;
        private String georadius;
        private String update_time;
        private String reason;

        public String getDev_name() {
            return dev_name;
        }

        public void setDev_name(String dev_name) {
            this.dev_name = dev_name;
        }

        public String getDev_age() {
            return dev_age;
        }

        public void setDev_age(String dev_age) {
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

        public String getPilltime1() {
            return pilltime1;
        }

        public void setPilltime1(String pilltime1) {
            this.pilltime1 = pilltime1;
        }

        public String getIsgeo() {
            return isgeo;
        }

        public void setIsgeo(String isgeo) {
            this.isgeo = isgeo;
        }

        public String getGeocenter() {
            return geocenter;
        }

        public void setGeocenter(String geocenter) {
            this.geocenter = geocenter;
        }

        public String getGeoradius() {
            return georadius;
        }

        public void setGeoradius(String georadius) {
            this.georadius = georadius;
        }

        public String getUpdate_time() {
            return update_time;
        }

        public void setUpdate_time(String update_time) {
            this.update_time = update_time;
        }

        public String getReason() {
            return reason;
        }

        public void setReason(String reason) {
            this.reason = reason;
        }
    }
}
