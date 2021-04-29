package com.localtovocal.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.UploadProgressListener;
import com.jaiselrahman.filepicker.activity.FilePickerActivity;
import com.jaiselrahman.filepicker.config.Configurations;
import com.jaiselrahman.filepicker.model.MediaFile;
import com.localtovocal.Others.API;
import com.localtovocal.Others.AppConstats;
import com.localtovocal.Others.SharedHelper;
import com.localtovocal.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class UploadFileActivity extends AppCompatActivity {


    Button btnADD;
    public static final int FILE_IMAGE_REQUEST_CODE = 1111;
    public static final int FILE_VIDEO_REQUEST_CODE = 8989;
    String filePath = "";
    ArrayList<File> fileList = new ArrayList<>();

    RadioGroup radioGroup;
    RadioButton fileRadioButton;
    RelativeLayout lottie;
    ProgressDialog dialog;
    EditText etDescription;
    String strDescription = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_file);


        btnADD = findViewById(R.id.btnADD);
        radioGroup = findViewById(R.id.radioGroup);
        lottie = findViewById(R.id.lottie);
        ImageView back = findViewById(R.id.back);
        etDescription = findViewById(R.id.etDescription);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnADD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                strDescription = etDescription.getText().toString().trim();
                int selectRadio = radioGroup.getCheckedRadioButtonId();
                fileRadioButton = findViewById(selectRadio);

              if (selectRadio == -1) {
                    Toast.makeText(UploadFileActivity.this, "Select one from the above", Toast.LENGTH_SHORT).show();
                } else {

                    if (fileRadioButton.getText().equals("Image")) {

                        Intent intent = new Intent(UploadFileActivity.this, FilePickerActivity.class);
                        intent.putExtra(FilePickerActivity.CONFIGS, new Configurations.Builder()
                                .setCheckPermission(true)
                                .setShowImages(true)
                                .setShowVideos(false)
                                .enableImageCapture(true)
                                .setMaxSelection(3)
                                .setSkipZeroSizeFiles(true)
                                .build());
                        startActivityForResult(intent, FILE_IMAGE_REQUEST_CODE);
                    } else {
                        Intent intent = new Intent(UploadFileActivity.this, FilePickerActivity.class);
                        intent.putExtra(FilePickerActivity.CONFIGS, new Configurations.Builder()
                                .setCheckPermission(true)
                                .setShowImages(false)
                                .setShowVideos(true)
                                .enableImageCapture(true)
                                .setMaxSelection(1)
                                .setSkipZeroSizeFiles(true)
                                .build());
                        startActivityForResult(intent, FILE_VIDEO_REQUEST_CODE);

                        // Toast.makeText(UploadFileActivity.this, "Coming soon", Toast.LENGTH_SHORT).show();
                    }


                }


            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == FILE_IMAGE_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            List<MediaFile> mediaFiles = data.getParcelableArrayListExtra(FilePickerActivity.MEDIA_FILES);
            if (mediaFiles != null) {

                for (int i = 0; i < mediaFiles.size(); i++) {
                    Log.e("cscj", String.valueOf(mediaFiles.get(i).getPath()));

                    filePath = mediaFiles.get(i).getPath();

                    Log.e("fncjdshfckj", filePath);

                    fileList.add(new File(filePath));

                    btnADD.setText("FILE SUCCESSFULLY ADDED");
                    lottie.setVisibility(View.VISIBLE);


                }

                for (int i = 0; i < fileList.size(); i++) {

                    Log.e("skasksasasa", fileList.get(i).toString());
                }

                uploadFiles(fileList, "0", strDescription);

            } else {
                btnADD.setText("ADD FILE");
                lottie.setVisibility(View.GONE);
            }
        }


        //////VIDEO/////////


        if (requestCode == FILE_VIDEO_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            List<MediaFile> mediaFiles = data.getParcelableArrayListExtra(FilePickerActivity.MEDIA_FILES);

            if (mediaFiles != null) {

                for (int i = 0; i < mediaFiles.size(); i++) {

                    filePath = mediaFiles.get(i).getPath();

                    Log.e("retgrgerfg", filePath);

                    fileList.add(new File(filePath));

                    btnADD.setText("FILE SUCCESSFULLY ADDED");
                    lottie.setVisibility(View.VISIBLE);


                }

                uploadFiles(fileList, "1", strDescription);

            } else {
                btnADD.setText("ADD FILE");
                lottie.setVisibility(View.GONE);
            }
        }


    }


    public void uploadFiles(ArrayList<File> fileList, String type, String description) {

        dialog = new ProgressDialog(this);
        dialog.setTitle("Uploading files");
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(true);

        String userID = SharedHelper.getKey(UploadFileActivity.this, AppConstats.USER_ID);

        AndroidNetworking.upload(API.BASE_URL)
                .addMultipartParameter("control", "add_image_video")
                .addMultipartParameter("userID", userID)
                .addMultipartParameter("type", type)
                .addMultipartParameter("description", description)
                .addMultipartFileList("files[]", fileList)
                .setTag("uploadFiles")
                .build()
                .setUploadProgressListener(new UploadProgressListener() {
                    @Override
                    public void onProgress(long bytesUploaded, long totalBytes) {
                        Log.e("snjdhsd", totalBytes + "");
                        double p = ((bytesUploaded / (float) totalBytes) * 100);
                        dialog.setProgress((int) p);
                        dialog.setMessage("please wait...." + new DecimalFormat("##.##").format(p) + " %");
                        dialog.show();

                    }
                })
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("dffdsdfs", response.toString());

                        try {

                            if (response.has("result")) {

                                String strResult = response.getString("result");
                                String message = response.getString("message");

                                if (strResult.equals("true")) {

                                    String data = response.getString("data");

                                    JSONArray jsonArray = new JSONArray(data);

                                    SharedHelper.putKey(getApplicationContext(), AppConstats.RESULT_UPLOAD, "success");
                                    startActivity(new Intent(getApplicationContext(), BottomNavigationActivity.class));
                                    finish();
                                    Toast.makeText(UploadFileActivity.this, "File Succesfully upload", Toast.LENGTH_SHORT).show();
                                    btnADD.setText("SUCCESSFULLY UPLOADED");
                                    dialog.hide();

                                } else {
                                    dialog.hide();
                                    Toast.makeText(UploadFileActivity.this, message, Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                dialog.hide();
                                Toast.makeText(UploadFileActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();

                            }

                        } catch (Exception e) {
                            dialog.hide();
                            Log.e("fmksc", Objects.requireNonNull(e.getMessage()));
                        }


                    }

                    @Override
                    public void onError(ANError anError) {
                        dialog.hide();
                        Log.e("fmksc", Objects.requireNonNull(anError.getMessage()));
                    }
                });
    }


}