package com.jennyni.fallproject.Bean;

/**
 * 8. 请求查询设备信息
 * Created by Jenny on 2019/3/4.
 */

public class AskDevInfoBean {

    /**
     * status : 200
     * msg : succeed
     * result : {"organization":"康复室","dname":"朱颖康","dage":66,"dsex":"男","idcard":"341221199411178888","dphone":"15357875321","address":"中国苏州科技城","casehistory":"锁骨断裂过","guardian":"13330000888","pilltime1":81080,"pilltime2":null,"pilltime3":null,"isgeo":1,"geocenter":"115.672126,38.817129","georadius":1000,"geopoints":null}
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
         * organization : 康复室
         * dname : 朱颖康
         * dage : 66
         * dsex : 男
         * idcard : 341221199411178888
         * dphone : 15357875321
         * address : 中国苏州科技城
         * casehistory : 锁骨断裂过
         * guardian : 13330000888
         * pilltime1 : 81080
         * pilltime2 : null
         * pilltime3 : null
         * isgeo : 1
         * geocenter : 115.672126,38.817129
         * georadius : 1000
         * geopoints : null
         * reason:device is not found
         */

        private String organization;
        private String dname;
        private int dage;
        private String dsex;
        private String idcard;
        private String dphone;
        private String address;
        private String casehistory;
        private String guardian;
        private int pilltime1;
        private Object pilltime2;
        private Object pilltime3;
        private int isgeo;
        private String geocenter;
        private int georadius;
        private Object geopoints;
        private String reason;

        public String getOrganization() {
            return organization;
        }

        public void setOrganization(String organization) {
            this.organization = organization;
        }

        public String getDname() {
            return dname;
        }

        public void setDname(String dname) {
            this.dname = dname;
        }

        public int getDage() {
            return dage;
        }

        public void setDage(int dage) {
            this.dage = dage;
        }

        public String getDsex() {
            return dsex;
        }

        public void setDsex(String dsex) {
            this.dsex = dsex;
        }

        public String getIdcard() {
            return idcard;
        }

        public void setIdcard(String idcard) {
            this.idcard = idcard;
        }

        public String getDphone() {
            return dphone;
        }

        public void setDphone(String dphone) {
            this.dphone = dphone;
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

        public Object getPilltime2() {
            return pilltime2;
        }

        public void setPilltime2(Object pilltime2) {
            this.pilltime2 = pilltime2;
        }

        public Object getPilltime3() {
            return pilltime3;
        }

        public void setPilltime3(Object pilltime3) {
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

        public String getReason() {
            return reason;
        }

        public void setReason(String reason) {
            this.reason = reason;
        }
    }

}
