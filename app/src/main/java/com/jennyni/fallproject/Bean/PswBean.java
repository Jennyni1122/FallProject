package com.jennyni.fallproject.Bean;

/**
 * 更改密码
 * Created by Jenny on 2019/2/22.
 */

public class PswBean {


    /**
     * status : 200
     * msg : success
     * result : {"phone_password":"c33367701511b4f6020ec61ded352059","update_time":"2019/03/04"}
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
         * phone_password : c33367701511b4f6020ec61ded352059
         * update_time : 2019/03/04
         */

        private String phone_password;
        private String update_time;
        private String reason;

        public String getPhone_password() {
            return phone_password;
        }

        public void setPhone_password(String phone_password) {
            this.phone_password = phone_password;
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
