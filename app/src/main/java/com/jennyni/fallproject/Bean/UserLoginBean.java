package com.jennyni.fallproject.Bean;

/**2.手机端登录
 * Created by Jenny on 2019/3/8.
 */

public class UserLoginBean {

    /**
     * status : 200
     * msg : success
     * result : {"account":"18860000306"}
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
         * reason:
         */

        private String account;
        private String reason;

        public String getAccount() {
            return account;
        }

        public void setAccount(String account) {
            this.account = account;
        }

        public String getReason() {
            return reason;
        }

        public void setReason(String reason) {
            this.reason = reason;
        }
    }
}
