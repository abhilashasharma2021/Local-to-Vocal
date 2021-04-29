package com.localtovocal.RetrofitModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SubscribePlanModel {

    @SerializedName("result")
    @Expose
    private Boolean result;
    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("data")
    @Expose
    private List<Datum> data = null;
    @SerializedName("status")
    @Expose
    private String status;


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

    public List<Datum> getData() {
        return data;
    }

    public void setData(List<Datum> data) {
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public class Datum {

        @SerializedName("planID")
        @Expose
        private String planID;
        @SerializedName("month")
        @Expose
        private String month;
        @SerializedName("price")
        @Expose
        private String price;
        @SerializedName("path")
        @Expose
        private String path;

        @SerializedName("remain_day")
        @Expose
        private String remain_day;

        public String getPlanID() {
            return planID;
        }

        public void setPlanID(String planID) {
            this.planID = planID;
        }

        public String getMonth() {
            return month;
        }

        public void setMonth(String month) {
            this.month = month;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public String getRemain_day() {
            return remain_day;
        }

        public void setRemain_day(String remain_day) {
            this.remain_day = remain_day;
        }
    }



}
