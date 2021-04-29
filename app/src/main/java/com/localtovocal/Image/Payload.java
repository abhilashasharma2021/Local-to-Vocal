package com.localtovocal.Image;

public class Payload {

    private String parameter;
    private String value;
    private String media;

    public Payload(String parameter, String value, String media) {
        this.parameter = parameter;
        this.value = value;
        this.media = media;
    }

    public String getMedia() {
        return media;
    }

    public String getParameter() {
        return parameter;
    }

    public String getValue() {
        return value;
    }

}
