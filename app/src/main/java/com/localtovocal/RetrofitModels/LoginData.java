package com.localtovocal.RetrofitModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LoginData {

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

        @SerializedName("userID")
        @Expose
        private String userID;
        @SerializedName("type")
        @Expose
        private String type;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("email")
        @Expose
        private String email;
        @SerializedName("password")
        @Expose
        private String password;
        @SerializedName("mobile")
        @Expose
        private String mobile;
        @SerializedName("shop_name")
        @Expose
        private String shopName;
        @SerializedName("tag")
        @Expose
        private List<Tag> tag = null;
        @SerializedName("description")
        @Expose
        private String description;
        @SerializedName("address")
        @Expose
        private String address;
        @SerializedName("city")
        @Expose
        private String city;
        @SerializedName("state")
        @Expose
        private String state;
        @SerializedName("image")
        @Expose
        private String image;
        @SerializedName("auth_id")
        @Expose
        private String authId;
        @SerializedName("auth_provider")
        @Expose
        private String authProvider;
        @SerializedName("latitude")
        @Expose
        private String latitude;
        @SerializedName("longitude")
        @Expose
        private String longitude;
        @SerializedName("strtotime")
        @Expose
        private String strtotime;
        @SerializedName("reg_id")
        @Expose
        private String regId;
        @SerializedName("path")
        @Expose
        private String path;

        @SerializedName("mobile2")
        @Expose
        private String mobile2;

        @SerializedName("subscription_status")
        @Expose
        private String subscription_status;


        public String getUserID() {
            return userID;
        }

        public void setUserID(String userID) {
            this.userID = userID;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getShopName() {
            return shopName;
        }

        public void setShopName(String shopName) {
            this.shopName = shopName;
        }

        public List<Tag> getTag() {
            return tag;
        }

        public void setTag(List<Tag> tag) {
            this.tag = tag;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getAuthId() {
            return authId;
        }

        public void setAuthId(String authId) {
            this.authId = authId;
        }

        public String getAuthProvider() {
            return authProvider;
        }

        public void setAuthProvider(String authProvider) {
            this.authProvider = authProvider;
        }

        public String getLatitude() {
            return latitude;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        public String getLongitude() {
            return longitude;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }

        public String getStrtotime() {
            return strtotime;
        }

        public void setStrtotime(String strtotime) {
            this.strtotime = strtotime;
        }

        public String getRegId() {
            return regId;
        }

        public void setRegId(String regId) {
            this.regId = regId;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public String getMobile2() {
            return mobile2;
        }

        public void setMobile2(String mobile2) {
            this.mobile2 = mobile2;
        }

        public String getSubscription_status() {
            return subscription_status;
        }

        public void setSubscription_status(String subscription_status) {
            this.subscription_status = subscription_status;
        }

        public class Tag {

            @SerializedName("users_tagID")
            @Expose
            private String usersTagID;
            @SerializedName("userID")
            @Expose
            private String userID;
            @SerializedName("tagID")
            @Expose
            private String tagID;
            @SerializedName("path")
            @Expose
            private String path;
            @SerializedName("tag_name")
            @Expose
            private String tagName;

            public String getUsersTagID() {
                return usersTagID;
            }

            public void setUsersTagID(String usersTagID) {
                this.usersTagID = usersTagID;
            }

            public String getUserID() {
                return userID;
            }

            public void setUserID(String userID) {
                this.userID = userID;
            }

            public String getTagID() {
                return tagID;
            }

            public void setTagID(String tagID) {
                this.tagID = tagID;
            }

            public String getPath() {
                return path;
            }

            public void setPath(String path) {
                this.path = path;
            }

            public String getTagName() {
                return tagName;
            }

            public void setTagName(String tagName) {
                this.tagName = tagName;
            }

        }








    }



}