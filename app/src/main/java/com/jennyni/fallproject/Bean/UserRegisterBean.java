package com.jennyni.fallproject.Bean;

/**
 * 注册工具类
 * Created by Jenny on 2019/2/22.
 */

public class UserRegisterBean {

    /**
     * status : 200
     * msg : success
     * result : {"account":"18860000306","name":"zhu"}
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
         * account : 18860000306
         * name : zhu
         */

        private String account;
        private String name;
        private String reason;

        public String getAccount() {
            return account;
        }

        public void setAccount(String account) {
            this.account = account;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getReason() {
            return reason;
        }

        public void setReason(String reason) {
            this.reason = reason;
        }

    }
}
