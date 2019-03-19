package com.jennyni.fallproject.Bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 13.查看所有设备报警数据
 * Created by Jenny on 2019/3/16.
 */

public class AskAllFallInfoBean {

    /**
     * status : 200
     * msg : succeed
     * result : [{"id":2,"card_id":"18255000000","lng":"120.418955","lat":"31.326614","rssi":3,"power ":2,"fall":1,"alert":1,"fence":0,"loctype":1,"steps":666,"calor":555,"time":1548246462,"nam e":"熊二"},{"id":55,"card_id":"18255180000","lng":"120.453657","lat":"31.329629","rssi":4,"powe r":1,"fall":0,"alert":0,"fence":1,"loctype":1,"steps":666,"calor":555,"time":1552721345,"na me":"龙飞"}]
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
         * card_id : 18255000000
         * lng : 120.418955
         * lat : 31.326614
         * rssi : 3
         * power  : 2
         * fall : 1
         * alert : 1
         * fence : 0
         * loctype : 1
         * steps : 666
         * calor : 555
         * time : 1548246462
         * name : 熊二
         * power : 1
         * name : 龙飞
         */

        private int id;
        private String card_id;
        private String name;
        private String lng;
        private String lat;
        private int rssi;
        private int power;
        private int fall;
        private int alert;
        private int fence;
        private int loctype;
        private int steps;
        private int calor;
        private long time;




        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getCard_id() {
            return card_id;
        }

        public void setCard_id(String card_id) {
            this.card_id = card_id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getLng() {
            return lng;
        }

        public void setLng(String lng) {
            this.lng = lng;
        }

        public String getLat() {
            return lat;
        }

        public void setLat(String lat) {
            this.lat = lat;
        }

        public int getRssi() {
            return rssi;
        }

        public void setRssi(int rssi) {
            this.rssi = rssi;
        }

        public int getPower() {
            return power;
        }

        public void setPower(int power) {
            this.power = power;
        }

        public int getFall() {
            return fall;
        }

        public void setFall(int fall) {
            this.fall = fall;
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

        public int getLoctype() {
            return loctype;
        }

        public void setLoctype(int loctype) {
            this.loctype = loctype;
        }

        public int getSteps() {
            return steps;
        }

        public void setSteps(int steps) {
            this.steps = steps;
        }

        public int getCalor() {
            return calor;
        }

        public void setCalor(int calor) {
            this.calor = calor;
        }

        public long getTime() {
            return time;
        }

        public void setTime(long time) {
            this.time = time;
        }

    }
}
