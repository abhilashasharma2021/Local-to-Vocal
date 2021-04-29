package com.localtovocal.RetrofitModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AboutUsModel {
    @SerializedName("result")
    @Expose
    private Boolean result;

    @SerializedName("message")
    @Expose
    private String message;

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

    public List<DataAbout> getData() {
        return data;
    }

    public void setData(List<DataAbout> data) {
        this.data = data;
    }

    @SerializedName("data")
    @Expose
    private List<AboutUsModel.DataAbout> data=null;

    public class DataAbout {

        @SerializedName("id")
        @Expose
        private String id;


        @SerializedName("AU")
        @Expose
        private String text;

        @SerializedName("path")
        @Expose
        private String path;

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
    }
}
