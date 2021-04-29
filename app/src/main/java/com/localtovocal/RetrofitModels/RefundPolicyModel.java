package com.localtovocal.RetrofitModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RefundPolicyModel {
    @SerializedName("result")
    @Expose
    private Boolean result;

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("data")
    @Expose
    private List<RefundPolicyModel.DataRefund> data=null;

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

    public List<DataRefund> getData() {
        return data;
    }

    public void setData(List<DataRefund> data) {
        this.data = data;
    }

    public class DataRefund {

        @SerializedName("id")
        @Expose
        private String id;


        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        @SerializedName("RP")
        @Expose
        private String text;

        @SerializedName("path")
        @Expose
        private String path;

    }
}
