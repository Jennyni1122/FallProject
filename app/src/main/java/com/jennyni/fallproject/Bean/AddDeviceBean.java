package com.jennyni.fallproject.Bean;

/**
 * 添加设备
 * Created by Jenny on 2019/3/4.
 */

public class AddDeviceBean {


    /**
     * status : 200
     * msg : success
     * result : {"phone_account":"18860000386","card_id":"18255180000"}
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
         * phone_account : 18860000386
         * card_id : 18255180000
         * reason:
         */

        private String phone_account;
        private String card_id;
        private String reason;

        public String getPhone_account() {
            return phone_account;
        }

        public void setPhone_account(String phone_account) {
            this.phone_account = phone_account;
        }

        public String getCard_id() {
            return card_id;
        }

        public void setCard_id(String card_id) {
            this.card_id = card_id;
        }

        public String getReason() {
            return reason;
        }

        public void setReason(String reason) {
            this.reason = reason;
        }
    }
}
