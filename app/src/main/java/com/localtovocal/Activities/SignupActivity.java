package com.localtovocal.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.Circle;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.Task;
import com.localtovocal.Image.Constants;
import com.localtovocal.Image.Payload;
import com.localtovocal.Image.Utils;
import com.localtovocal.Others.API;
import com.localtovocal.Others.AppConstats;
import com.localtovocal.Others.JsonInterface;
import com.localtovocal.Others.SharedHelper;
import com.localtovocal.R;
import com.localtovocal.RetrofitModels.EditDescriptionModel;
import com.localtovocal.RetrofitModels.GenrateOtpModel;
import com.localtovocal.RetrofitModels.LoginData;
import com.localtovocal.RetrofitModels.UserSignUp;
import com.yalantis.ucrop.UCrop;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SignupActivity extends AppCompatActivity {

    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 8787;
    LinearLayout lin_shopName, lin_address, lin_discretion, lin_cityState;
    RelativeLayout lin_tag;
    View v1, v2, v3, v4;
    EditText name, email, password, number, shopname, discretion, etCity, etState, pincode;
    EditText tag;
    AutoCompleteTextView address;
    String strName = "", strEmail = "", strPassword = "", strNumber = "", strUserOrShop = "";
    String strTag = "", strDescretion = "", strShopName = "", strAddress = "", strCity = "", strState = "";
    String strLatitude = "", strLongitude = "";

    TextView signIn, tc_policy;
    ImageView imgPrf;

    public static final int CODE_IMG_GALLERY = 1111;
    public static final String CROP_IMAGE = "Cropped_Image";


    String strImage = "";
    JsonInterface jsonInterface;
    Retrofit retrofit;
    ProgressBar progress;
    Button btnSignUp;

    private FusedLocationProviderClient fusedLocationProviderClient;
    String tusername = "", temail = "", tpassword = "", tnumber = "", tshopName = "", tImageResult = "";

    boolean isLocationEnable;
    Location location;

    String strPinCode = "";
    String strGetBimap = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);


        lin_shopName = findViewById(R.id.lin_shopName);
        lin_address = findViewById(R.id.lin_address);
        lin_discretion = findViewById(R.id.lin_discretion);
        lin_tag = findViewById(R.id.lin_tag);
        lin_cityState = findViewById(R.id.lin_cityState);
        v1 = findViewById(R.id.v1);
        v2 = findViewById(R.id.v2);
        v3 = findViewById(R.id.v3);
        v4 = findViewById(R.id.v4);
        signIn = findViewById(R.id.signIn);
        imgPrf = findViewById(R.id.imgPrf);
        btnSignUp = findViewById(R.id.btnSignUp);
        pincode = findViewById(R.id.pincode);


        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        number = findViewById(R.id.number);
        shopname = findViewById(R.id.shopname);
        tag = findViewById(R.id.tag);
        discretion = findViewById(R.id.discretion);
        address = findViewById(R.id.address);
        etCity = findViewById(R.id.etCity);
        etState = findViewById(R.id.etState);

        progress = findViewById(R.id.progress);
        Sprite doubleBounce = new Circle();
        progress.setIndeterminateDrawable(doubleBounce);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);


        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .build();

        retrofit = new Retrofit.Builder().baseUrl(API.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();

        jsonInterface = retrofit.create(JsonInterface.class);


        tc_policy = findViewById(R.id.tc_policy);


        String getTags = SharedHelper.getKey(getApplicationContext(), AppConstats.SELECTED_TAGS);


        strImage = SharedHelper.getKey(getApplicationContext(), AppConstats.ImageFile);
        strGetBimap = SharedHelper.getKey(getApplicationContext(), AppConstats.BitmapImage);
        tusername = SharedHelper.getKey(getApplicationContext(), AppConstats.S_USERNAME);
        temail = SharedHelper.getKey(getApplicationContext(), AppConstats.S_EMAIL);
        tpassword = SharedHelper.getKey(getApplicationContext(), AppConstats.S_PASSWORD);
        tnumber = SharedHelper.getKey(getApplicationContext(), AppConstats.S_MOBILE);
        tshopName = SharedHelper.getKey(getApplicationContext(), AppConstats.S_SHOPNAME);

        Log.e("dskdsklsdlkzns", tImageResult);


        tag.setText(getTags);
        name.setText(tusername);
        email.setText(temail);
        password.setText(tpassword);
        number.setText(tnumber);
        shopname.setText(tshopName);


        SpannableString sp = new SpannableString("By signing up I agree to the Terms and Conditions");

        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {

                startActivity(new Intent(getApplicationContext(), TermAndConditionActivity.class));
                Animatoo.animateShrink(SignupActivity.this);
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(true);
                ds.setColor(Color.parseColor("#0d47a1"));
            }
        };


        sp.setSpan(clickableSpan, 29, 49, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tc_policy.setText(sp);
        tc_policy.setMovementMethod(LinkMovementMethod.getInstance());
        tc_policy.setHighlightColor(Color.TRANSPARENT);


        signIn.setOnClickListener(view -> {
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            Animatoo.animateShrink(SignupActivity.this);
            finish();
        });

        imgPrf.setOnClickListener(view -> startActivityForResult(new Intent()
                .setAction(Intent.ACTION_GET_CONTENT)
                .setType("image/*"), CODE_IMG_GALLERY));


        btnSignUp.setOnClickListener(view -> {

            strName = name.getText().toString().trim();
            strEmail = email.getText().toString().trim();
            strPassword = password.getText().toString().trim();
            strNumber = number.getText().toString().trim();
            strShopName = shopname.getText().toString().trim();

            strDescretion = discretion.getText().toString().trim();
            strAddress = address.getText().toString().trim();
            strCity = etCity.getText().toString().trim();
            strState = etState.getText().toString().trim();
            strPinCode = pincode.getText().toString().trim();

            String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.[a-z]+";


            if (TextUtils.isEmpty(strName)) {
                Toast.makeText(SignupActivity.this, "Please enter your name", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(strEmail)) {
                Toast.makeText(SignupActivity.this, "Please enter your email", Toast.LENGTH_SHORT).show();

            } else if (TextUtils.isEmpty(strPassword)) {
                Toast.makeText(SignupActivity.this, "Please enter password", Toast.LENGTH_SHORT).show();

            } else if (strPassword.length() < 4) {
                Toast.makeText(SignupActivity.this, "Enter atleast 4 digit password", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(strNumber)) {
                Toast.makeText(SignupActivity.this, "Please enter your mobile number", Toast.LENGTH_SHORT).show();

            } else if (strNumber.length() != 10) {
                Toast.makeText(this, "Please enter 10 digit mobile number", Toast.LENGTH_SHORT).show();
            } else if (strImage.equals("")) {

                Toast.makeText(SignupActivity.this, "Please select an image", Toast.LENGTH_SHORT).show();

            } else if (TextUtils.isEmpty(strShopName)) {
                Toast.makeText(SignupActivity.this, "Shop name is empty", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(strDescretion)) {
                Toast.makeText(SignupActivity.this, "Discretion is empty", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(strAddress)) {
                Toast.makeText(SignupActivity.this, "Address is empty", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(strCity)) {
                Toast.makeText(SignupActivity.this, "City is empty", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(strState)) {
                Toast.makeText(SignupActivity.this, "State is empty", Toast.LENGTH_SHORT).show();
            } else if (strLatitude.equals("") || strLongitude.equals("")) {
                Toast.makeText(SignupActivity.this, "Please select address from the list", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(strPinCode)) {

                Toast.makeText(SignupActivity.this, "Pincode is empty", Toast.LENGTH_SHORT).show();

            } else {

               /* SharedHelper.putKey(getApplicationContext(), AppConstats.S_USERNAME, strName);
                SharedHelper.putKey(getApplicationContext(), AppConstats.S_EMAIL, strEmail);
                SharedHelper.putKey(getApplicationContext(), AppConstats.S_PASSWORD, strPassword);
                SharedHelper.putKey(getApplicationContext(), AppConstats.S_MOBILE, strNumber);
                SharedHelper.putKey(getApplicationContext(), AppConstats.S_SHOPNAME, strShopName);
                SharedHelper.putKey(getApplicationContext(), AppConstats.S_Discreption, strDescretion);
                SharedHelper.putKey(getApplicationContext(), AppConstats.S_City, strCity);
                SharedHelper.putKey(getApplicationContext(), AppConstats.S_Address, strAddress);
                SharedHelper.putKey(getApplicationContext(), AppConstats.USER_PINCODE, strPinCode);
                SharedHelper.putKey(getApplicationContext(), AppConstats.USER_STATE, strState);
                SharedHelper.putKey(getApplicationContext(), AppConstats.USER_LATITUDE, strLatitude);
                SharedHelper.putKey(getApplicationContext(), AppConstats.USER_LONGITUDE, strLongitude);*/
             //   signUp();
                genrate_Otp();
            }




        });


        tag.setOnClickListener(view -> {

                    strName = name.getText().toString().trim();
                    strEmail = email.getText().toString().trim();
                    strPassword = password.getText().toString().trim();
                    strNumber = number.getText().toString().trim();
                    strShopName = shopname.getText().toString().trim();

                    SharedHelper.putKey(getApplicationContext(), AppConstats.S_USERNAME, strName);
                    SharedHelper.putKey(getApplicationContext(), AppConstats.S_EMAIL, strEmail);
                    SharedHelper.putKey(getApplicationContext(), AppConstats.S_PASSWORD, strPassword);
                    SharedHelper.putKey(getApplicationContext(), AppConstats.S_MOBILE, strNumber);
                    SharedHelper.putKey(getApplicationContext(), AppConstats.S_SHOPNAME, strShopName);

                    startActivity(new Intent(getApplicationContext(), TagsActivity.class));

                }

        );


        getPermissions();
        getDeviceLocation();


       /* if (!strGetBimap.equals("")) {
            byte[] imageAsBytes = Base64.decode(strGetBimap.getBytes(), Base64.DEFAULT);
            imgPrf.setImageBitmap(BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length));
        }*/

    }


    public void getDeviceLocation() {

        try {

            if (isLocationEnable) {

                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                    return;
                }
                Task<Location> locationResult = fusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(task -> {

                    if (task.isSuccessful()) {
                        location = locationResult.getResult();

                        if (location != null) {

                            strLatitude = String.valueOf(location.getLatitude());
                            strLongitude = String.valueOf(location.getLongitude());
                            Log.e("locationC", strLatitude + "," + strLongitude);
                            updateLocation(location.getLatitude(), location.getLongitude());

                        } else {

                            Toast.makeText(this, "Unable to fetch your location", Toast.LENGTH_SHORT).show();
                        }

                    }

                });
            }

        } catch (Exception e) {
            //  Log.e("sndclks", e.getMessage(), e);
        }


    }


    public void getPermissions() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            isLocationEnable = true;
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        isLocationEnable = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    isLocationEnable = true;
                }
            }
        }

    }


    private void updateLocation(Double lat, Double lon) {
        try {

            Geocoder geocoder;
            List<Address> addresses;


            geocoder = new Geocoder(this, Locale.getDefault());

            addresses = geocoder.getFromLocation(lat, lon, 1);

            if (addresses != null) {

                String addre = addresses.get(0).getAddressLine(0);
                String city = addresses.get(0).getLocality();
                String state = addresses.get(0).getAdminArea();
                String country = addresses.get(0).getCountryName();
                String postalCode = addresses.get(0).getPostalCode();
                String knownName = addresses.get(0).getFeatureName();
                String area = addresses.get(0).getSubAdminArea();
                String street = addresses.get(0).getSubLocality();


                etCity.setText(city);
                strCity = city;
                etState.setText(state);
                strState = state;
                pincode.setText(postalCode);
                address.setText(knownName + " " + street);
                Log.e("ieufrer", "message " + area);
                Log.e("ieufrer", "message " + knownName);
                Log.e("ieufrer", "message " + street);


            }


        } catch (IOException e) {
            // Log.e("gfvdfrgvd", e.getMessage());
        }

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CODE_IMG_GALLERY && resultCode == RESULT_OK) {
            Uri imageUri = data.getData();
            imgPrf.setImageURI(imageUri);
            if (imageUri != null) {
                startCrop(imageUri);
            } else {
                startCrop(imageUri);
            }
        } else if (requestCode == UCrop.REQUEST_CROP && resultCode == RESULT_OK) {

            Uri imageUriResultCrop = UCrop.getOutput(data);
            File directory = new File(imageUriResultCrop.getEncodedPath());
            String strFileName = directory.toString();
            Log.e("imageUri", strFileName);

            try {

                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUriResultCrop);
                Log.e("hehhshshsh", bitmap.toString());

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos); //bm is the bitmap object
                byte[] b = baos.toByteArray();
                String strImageBitmap = Base64.encodeToString(b, Base64.DEFAULT);

                SharedHelper.putKey(SignupActivity.this, AppConstats.BitmapImage, strImageBitmap);
                SharedHelper.putKey(SignupActivity.this, AppConstats.ImageFile, strFileName);

                // imgPrf.setImageURI(imageUriResultCrop);
                imgPrf.setImageBitmap(bitmap);
            } catch (IOException ignored) {

            }

            strImage = strFileName;
        }


    }


    private void startCrop(@NonNull Uri uri) {

        String destinationFileName = CROP_IMAGE;
        destinationFileName += ".jpg";

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


    public void signUp() {

        String getTagsID = SharedHelper.getKey(getApplicationContext(), AppConstats.SELECTED_TAGS_ID);
        Log.e("dskdsklsdlkzns", getTagsID);
        progress.setVisibility(View.VISIBLE);
        ArrayList<Payload> payLoads = new ArrayList<>();
        payLoads.add(new Payload("control", "signup", Constants.MEDIA_TEXT));
        payLoads.add(new Payload("email", strEmail, Constants.MEDIA_TEXT));
        payLoads.add(new Payload("password", strPassword, Constants.MEDIA_TEXT));
        payLoads.add(new Payload("mobile", strNumber, Constants.MEDIA_TEXT));
        payLoads.add(new Payload("name", strName, Constants.MEDIA_TEXT));
        payLoads.add(new Payload("type", "1", Constants.MEDIA_TEXT));
        payLoads.add(new Payload("image", strImage, Constants.MEDIA_IMAGE));
        payLoads.add(new Payload("tag", getTagsID, Constants.MEDIA_TEXT));
        payLoads.add(new Payload("shop_name", strShopName, Constants.MEDIA_TEXT));
        payLoads.add(new Payload("description", strDescretion, Constants.MEDIA_TEXT));
        payLoads.add(new Payload("address", strAddress, Constants.MEDIA_TEXT));
        payLoads.add(new Payload("latitude", strLatitude, Constants.MEDIA_TEXT));
        payLoads.add(new Payload("longitude", strLongitude, Constants.MEDIA_TEXT));
        payLoads.add(new Payload("city", strCity, Constants.MEDIA_TEXT));
        payLoads.add(new Payload("state", strState, Constants.MEDIA_TEXT));
        payLoads.add(new Payload("pinCODE", strPinCode, Constants.MEDIA_TEXT));


        Call<UserSignUp> call = jsonInterface.newUser(Utils.generateMultiPartBody(payLoads));

        call.enqueue(new Callback<UserSignUp>() {
            @Override
            public void onResponse(@NonNull Call<UserSignUp> call, @NonNull Response<UserSignUp> response) {


                if (!response.isSuccessful()) {
                    Toast.makeText(SignupActivity.this, response.code(), Toast.LENGTH_SHORT).show();
                    progress.setVisibility(View.GONE);
                }

                UserSignUp userSignUp = response.body();

                if (userSignUp != null) {

                    UserSignUp.Data data = userSignUp.getData();

                    if (userSignUp.getResult()) {

                        String id = data.getUserID();
                        String name = data.getName();
                        String image = data.getImage();
                        String path = data.getPath();
                        String shopName = data.getShopName();
                        String tag = data.getTag();
                        String dis = data.getDescription();
                        String address = data.getAddress();
                        String mobile = data.getMobile();
                        String lat = data.getLatitude();
                        String lon = data.getLongitude();

                        Log.e("xjhxdjs", id);
                        Log.e("xjhxdjs", name);
                        Log.e("xjhxdjs", image);
                        Log.e("xjhxdjs", path);
                        Log.e("xjhxdjs", mobile);

                        Log.e("xjhxdjs", shopName);
                        Log.e("xjhxdjs", tag);
                        Log.e("xjhxdjs", dis);
                        Log.e("xjhxdjs", address);

                        Log.e("xjhxdjs", lat);
                        Log.e("xjhxdjs", lon);

                        progress.setVisibility(View.GONE);
                        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                        finish();

                    } else {

                        Toast.makeText(SignupActivity.this, userSignUp.getMessage(), Toast.LENGTH_SHORT).show();
                        progress.setVisibility(View.GONE);
                    }
                }


            }

            @Override
            public void onFailure(@NonNull Call<UserSignUp> call, @NonNull Throwable t) {

                Log.e("csjnjc", t.getMessage(), t);
            }
        });
    }
    public void genrate_Otp() {

        progress.setVisibility(View.VISIBLE);
        Map<String, String> map = new HashMap<>();
        map.put("control","genrate_otp");
        map.put("mobile",strNumber);
        Call<GenrateOtpModel> call = jsonInterface.genrateOtp(map);

        call.enqueue(new Callback<GenrateOtpModel>() {
            @Override
            public void onResponse(Call<GenrateOtpModel> call, Response<GenrateOtpModel> response) {

                Log.e("dlgklfd",response.toString());

                if (!response.isSuccessful()) {
                    Toast.makeText(SignupActivity.this, response.code(), Toast.LENGTH_SHORT).show();
                    progress.setVisibility(View.GONE);
                }


                GenrateOtpModel genrateOtpModel = response.body();


                if (genrateOtpModel != null) {
                    if (genrateOtpModel.getResult()) {

                        String mobile = genrateOtpModel.getMobile();
                        String otp = genrateOtpModel.getOtp();

                        Log.e("dgkmldf", mobile);
                        Log.e("dgkmldf", otp);

                        SharedHelper.putKey(getApplicationContext(), AppConstats.S_MOBILE, mobile);
                        Toast.makeText(SignupActivity.this, genrateOtpModel.getMessage(), Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(SignupActivity.this, OtpVerifyActivity.class));
                        progress.setVisibility(View.GONE);


                    } else {
                        progress.setVisibility(View.GONE);
                        Toast.makeText(SignupActivity.this, genrateOtpModel.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }


            }

            @Override
            public void onFailure(Call<GenrateOtpModel> call, Throwable t) {
                Log.e("xdsbdjhsgc", t.getMessage());
                progress.setVisibility(View.GONE);
            }
        });

    }


}