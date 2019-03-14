package com.jennyni.fallproject.Bean;

/**
 * 9. 请求查询设备最新在线时间
 * Created by Jenny on 2019/3/8.
 */

public class AskonBean {

    /**
     * status : 200
     * msg : succeed
     * result : {"time":1543823607}
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
         * time : 1543823607
         */

        private int time;
        private String reason;

        public int getTime() {
            return time;
        }

        public void setTime(int time) {
            this.time = time;
        }

        public String getReason() {
            return reason;
        }

        public void setReason(String reason) {
            this.reason = reason;
        }
    }
}
