package com.localtovocal.Image;

import java.io.File;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class Utils {

    public static MultipartBody generateMultiPartBody(ArrayList<Payload> payloads) {
        MultipartBody.Builder requestBodyBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        for (Payload payload : payloads) {
            switch (payload.getMedia()) {
                case Constants.MEDIA_IMAGE:
                    RequestBody fbody = RequestBody.create(MediaType.parse("image/*"), new File(payload.getValue()));
                    requestBodyBuilder.addFormDataPart(payload.getParameter(), "image.jpeg", fbody);
                    break;

                case Constants.MEDIA_TEXT:
                    RequestBody name = RequestBody.create(MediaType.parse("text/plain"), payload.getValue());
                    requestBodyBuilder.addFormDataPart(payload.getParameter(), null, name);
                    break;
            }
        }
        return requestBodyBuilder.build();
    }
}