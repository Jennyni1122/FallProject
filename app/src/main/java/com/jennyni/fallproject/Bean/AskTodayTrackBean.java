package com.jennyni.fallproject.Bean;

import java.util.List;

/**
 * Created by Jenny on 2019/3/18.
 */

public class AskTodayTrackBean {
    /**
     * status : 200
     * msg : succeed
     * result : [{"lng":"120.418955","lat":"31.326614","time":1551454486},{"lng":"120.449057","lat":"31.331486","time":1548385908},{"lng":"120.449057","lat":"31.331486","time":1548385877},{"lng":"120.449057","lat":"31.331486","time":1548246669},{"lng":"120.418955","lat":"31.326614","time":1548246462},{"lng":"120.4173","lat":"31.325084","time":1548244988},{"lng":"120.419347","lat":"31.328385","time":1543823452}]
     */

    private int status;
    private String msg;
    private List<AskTodayTrackBean.ResultBean> result;

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

    public List<AskTodayTrackBean.ResultBean> getResult() {
        return result;
    }

    public void setResult(List<AskTodayTrackBean.ResultBean> result) {
        this.result = result;
    }

    public static class ResultBean {
        /**
         * lng : 120.418955
         * lat : 31.326614
         * time : 1551454486
         */

        private String lng;
        private String lat;
        private int time;

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

        public int getTime() {
            return time;
        }

        public void setTime(int time) {
            this.time = time;
        }
    }
}
