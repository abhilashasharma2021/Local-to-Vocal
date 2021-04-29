package com.localtovocal;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.yalantis.ucrop.UCrop;

import java.io.File;

public class CropImageActivity extends AppCompatActivity {


    ImageView image;
    public static final int CODE_IMG_GALLERY = 1;
    public static final String CROP_IMAGE = "Cropped_Image";
    public static final String IMAGE_DIRECTORY = "IMAGE_DIRECTORY";

    private int GALLERY = 1, CAMERA = 2;
    File f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop_image);

        image = findViewById(R.id.image);

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivityForResult(new Intent()
                        .setAction(Intent.ACTION_GET_CONTENT)
                        .setType("image/*"), CODE_IMG_GALLERY);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CODE_IMG_GALLERY && resultCode == RESULT_OK) {

            Uri imageUri = data.getData();


            image.setImageURI(imageUri);

            if (imageUri != null) {
                startCrop(imageUri);
            }

        }
        else if (requestCode == UCrop.REQUEST_CROP && resultCode == RESULT_OK) {

            Uri imageUriResultCrop = UCrop.getOutput(data);
            File directory = new File(imageUriResultCrop.getEncodedPath());
            String strFileName = directory.toString();
            Log.e("imageUri", strFileName);
            image.setImageURI(imageUriResultCrop);
            Log.e("SDfsdfsdf","OK");
        }
    }




    private void startCrop(@NonNull Uri uri) {

        String destinationFileName = CROP_IMAGE;
        destinationFileName += ".jpg";

        String directoryPath = Environment.getExternalStorageDirectory() + "/" + IMAGE_DIRECTORY + "/";
        String filePath = directoryPath + ".jpg";

        UCrop uCrop = UCrop.of(uri, Uri.fromFile(new File(getExternalCacheDir().getAbsolutePath(), destinationFileName)));

        uCrop.withAspectRatio(1, 1)
                .withMaxResultSize(600, 600)
                .withOptions(getOptions())
                .start(this);

    }


    private UCrop.Options getOptions() {

        UCrop.Options options = new UCrop.Options();
        options.setCompressionQuality(40);
        options.setHideBottomControls(false);
        options.setFreeStyleCropEnabled(true);
        options.setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        options.setToolbarColor(getResources().getColor(R.color.colorPrimary));
        options.setToolbarTitle("Crop Image");
        return options;

    }
}