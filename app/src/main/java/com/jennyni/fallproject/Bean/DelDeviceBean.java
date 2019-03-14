package com.jennyni.fallproject.Bean;

/**
 * 解绑设备
 * Created by Jenny on 2019/3/4.
 */

public class DelDeviceBean {

    /**
     * status : 200
     * msg : success
     * result : {"reason":1}
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
         * reason : 1
         */

        private int reason;

        public int getReason() {
            return reason;
        }

        public void setReason(int reason) {
            this.reason = reason;
        }
    }
}
