package com.localtovocal.RetrofitModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BookingPlan {

    @SerializedName("result")
    @Expose
    private Boolean result;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private Data data;

    public Boolean getResult() {
        return result;
    }

    public void setResult(Boolean result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }


    public class Data {

        @SerializedName("user_planID")
        @Expose
        private String userPlanID;
        @SerializedName("userID")
        @Expose
        private String userID;
        @SerializedName("planID")
        @Expose
        private String planID;
        @SerializedName("txn_id")
        @Expose
        private String txnId;
        @SerializedName("amount")
        @Expose
        private String amount;
        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("strtotime")
        @Expose
        private String strtotime;
        @SerializedName("path")
        @Expose
        private String path;

        public String getUserPlanID() {
            return userPlanID;
        }

        public void setUserPlanID(String userPlanID) {
            this.userPlanID = userPlanID;
        }

        public String getUserID() {
            return userID;
        }

        public void setUserID(String userID) {
            this.userID = userID;
        }

        public String getPlanID() {
            return planID;
        }

        public void setPlanID(String planID) {
            this.planID = planID;
        }

        public String getTxnId() {
            return txnId;
        }

        public void setTxnId(String txnId) {
            this.txnId = txnId;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getStrtotime() {
            return strtotime;
        }

        public void setStrtotime(String strtotime) {
            this.strtotime = strtotime;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

    }






}