package com.jennyni.fallproject.Bean;

/**
 * Created by Jenny on 2019/3/20.
 */

public class UploadBean {


    /**
     * status : 200
     * msg : success
     * result : {"card_id":"18255180000","lng":120.453657,"lat":31.329629,"rssi":"88","power":"77","fall":"0","loctype":"1","steps":"666","calor":"555","alert":0,"fence":1,"time":"2019-03-15 17:08:51","id":"54"}
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
         * card_id : 18255180000
         * lng : 120.453657
         * lat : 31.329629
         * rssi : 88
         * power : 77
         * fall : 0
         * loctype : 1
         * steps : 666
         * calor : 555
         * alert : 0
         * fence : 1
         * time : 2019-03-15 17:08:51
         * id : 54
         */

        private String card_id;
        private double lng;
        private double lat;
        private String rssi;
        private String power;
        private String fall;
        private String loctype;
        private String steps;
        private String calor;
        private int alert;
        private int fence;
        private String time;
        private String id;
        private String dname;
        private String reason;

        public String getCard_id() {
            return card_id;
        }

        public void setCard_id(String card_id) {
            this.card_id = card_id;
        }

        public double getLng() {
            return lng;
        }

        public void setLng(double lng) {
            this.lng = lng;
        }

        public double getLat() {
            return lat;
        }

        public void setLat(double lat) {
            this.lat = lat;
        }

        public String getRssi() {
            return rssi;
        }

        public void setRssi(String rssi) {
            this.rssi = rssi;
        }

        public String getPower() {
            return power;
        }

        public void setPower(String power) {
            this.power = power;
        }

        public String getFall() {
            return fall;
        }

        public void setFall(String fall) {
            this.fall = fall;
        }

        public String getLoctype() {
            return loctype;
        }

        public void setLoctype(String loctype) {
            this.loctype = loctype;
        }

        public String getSteps() {
            return steps;
        }

        public void setSteps(String steps) {
            this.steps = steps;
        }

        public String getCalor() {
            return calor;
        }

        public void setCalor(String calor) {
            this.calor = calor;
        }

        public int getAlert() {
            return alert;
        }

        public void setAlert(int alert) {
            this.alert = alert;
        }

        public int getFence() {
            return fence;
        }

        public void setFence(int fence) {
            this.fence = fence;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getDname() {
            return dname;
        }

        public void setDname(String dname) {
            this.dname = dname;
        }

        public String getReason() {
            return reason;
        }

        public void setReason(String reason) {
            this.reason = reason;
        }
    }
}
