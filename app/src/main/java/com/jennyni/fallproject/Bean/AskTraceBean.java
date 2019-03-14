package com.jennyni.fallproject.Bean;

import java.util.List;

/**
 * 查询历史设备运动轨迹  （d 表示过去 d 天到今天的数据，d 默认为 1，降序）
 * Created by Jenny on 2019/3/4.
 */

public class AskTraceBean {


    /**
     * status : 200
     * msg : succeed
     * result : [{"lng":"120.411277","lat":"31.322345"},
     * {"lng":"120.419367","lat":"31.328375"},
     * {"lng":"120.419357","lat":"31.328385"},
     * {"lng":"120.419347","lat":"31.328385"}]
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
         * lng : 120.411277
         * lat : 31.322345
         */

        private String lng;
        private String lat;

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
    }
}
