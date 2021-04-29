package com.localtovocal.Fragments;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interceptors.HttpLoggingInterceptor;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.bumptech.glide.Glide;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FetchPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.localtovocal.Adapters.AutoCompleteAdapter;
import com.localtovocal.Others.API;
import com.localtovocal.Others.AppConstats;
import com.localtovocal.Others.JsonInterface;
import com.localtovocal.Others.SharedHelper;
import com.localtovocal.R;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.app.Activity.RESULT_CANCELED;


public class EditProfileFragment extends Fragment {


    ImageView back;
    ImageView prf, camera;
    private static final String IMAGE_DIRECTORY = "/Local to Vocal";
    private final int GALLERY = 1;
    private final int CAMERA = 2;
    File f;

    EditText name, shopname, number, discretion, etCity, etState, tags, altNnumber, pincode;
    AutoCompleteTextView address;

    Button btnUpdate;

    String strName = "", strShopName = "", strNumber = "", strDiscretion = "", strAddress = "", strAlternateNumber = "";
    String strCity = "", strState = "";

    AutoCompleteAdapter adapter;
    PlacesClient placesClient;
    String strLatitude = "", strLongitude = "", strImage = "", strPinCode = "";

    ProgressDialog progressDialog;

    Retrofit retrofit;
    JsonInterface jsonInterface;

    public static String MY_TAGS = "";

    double ll, lo;
    private AdapterView.OnItemClickListener autocompleteClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            try {
                final AutocompletePrediction item = adapter.getItem(i);
                String placeID = null;
                if (item != null) {
                    placeID = item.getPlaceId();
                }

                List<Place.Field> placeFields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS
                        , Place.Field.LAT_LNG);

                FetchPlaceRequest request = null;

                if (placeID != null) {
                    request = FetchPlaceRequest.builder(placeID, placeFields)
                            .build();
                }

                if (request != null) {
                    placesClient.fetchPlace(request).addOnSuccessListener(new OnSuccessListener<FetchPlaceResponse>() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onSuccess(FetchPlaceResponse task) {

                            final LatLng lotlonaddress = task.getPlace().getLatLng();
                            ll = lotlonaddress.latitude;
                            lo = lotlonaddress.longitude;

                            updateLocation();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            e.printStackTrace();
                            Log.e("snjsjhc", e.getMessage());
                            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);


        back = view.findViewById(R.id.back);
        btnUpdate = view.findViewById(R.id.btnUpdate);
        name = view.findViewById(R.id.name);
        shopname = view.findViewById(R.id.shopname);
        tags = view.findViewById(R.id.tags);
        number = view.findViewById(R.id.number);
        discretion = view.findViewById(R.id.discretion);
        address = view.findViewById(R.id.address);
        prf = view.findViewById(R.id.prf);
        camera = view.findViewById(R.id.camera);
        etCity = view.findViewById(R.id.etCity);
        etState = view.findViewById(R.id.etState);
        altNnumber = view.findViewById(R.id.altNnumber);
        pincode = view.findViewById(R.id.pincode);


        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .build();

        retrofit = new Retrofit.Builder().baseUrl(API.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        jsonInterface = retrofit.create(JsonInterface.class);


        tags.setOnClickListener(view13 -> Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new TagsFragment()).commit());


        getSetData();

        back.setOnClickListener(view1 -> Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MyProfileFragment()).commit());


        camera.setOnClickListener(view12 -> showPictureDialog());

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                strName = name.getText().toString().trim();
                strShopName = shopname.getText().toString().trim();
                strNumber = number.getText().toString().trim();
                strDiscretion = discretion.getText().toString().trim();
                strAddress = address.getText().toString().trim();
                strCity = etCity.getText().toString().trim();
                strState = etState.getText().toString().trim();
                strAlternateNumber = altNnumber.getText().toString().trim();
                strPinCode = pincode.getText().toString().trim();


                updateProf();


            }
        });


        String apiKey = getString(R.string.api_key);

        if (apiKey.isEmpty()) {
            Log.e("cdsdjml", String.valueOf(R.string.error));
            return null;
        }

        // Setup Places Client
        if (!Places.isInitialized()) {
            Places.initialize(getActivity(), apiKey);
        }

        placesClient = Places.createClient(getActivity());

        address.setThreshold(1);
        address.setOnItemClickListener(autocompleteClickListener);
        adapter = new AutoCompleteAdapter(getActivity(), placesClient);
        address.setAdapter(adapter);

        return view;
    }


    public void getSetData() {

        String uName = SharedHelper.getKey(getActivity(), AppConstats.USER_NAME);
        String uNumber = SharedHelper.getKey(getActivity(), AppConstats.USER_MOBILE_NUMBER);
        String uEmail = SharedHelper.getKey(getActivity(), AppConstats.USER_EMAIL);
        String uShopName = SharedHelper.getKey(getActivity(), AppConstats.USER_SHOPNAME);
        String uImage = SharedHelper.getKey(getActivity(), AppConstats.USER_IMAGE);
        String uPath = SharedHelper.getKey(getActivity(), AppConstats.USER_PATH);
        String uDesc = SharedHelper.getKey(getActivity(), AppConstats.DISCRETION);
        String uAddress = SharedHelper.getKey(getActivity(), AppConstats.ADDRESS);
        String city = SharedHelper.getKey(getActivity(), AppConstats.USER_CITY);
        String state = SharedHelper.getKey(getActivity(), AppConstats.USER_STATE);
        String uTags = SharedHelper.getKey(getActivity(), AppConstats.TAG);
        String alternateNumber = SharedHelper.getKey(getActivity(), AppConstats.USER_ALTERNATE_MOBILE);
        String uPincode = SharedHelper.getKey(getActivity(), AppConstats.USER_PINCODE);

        Log.e("dmkslksdlks", uTags);

        name.setText(uName);
        number.setText(uNumber);
        shopname.setText(uShopName);
        discretion.setText(uDesc);
        address.setText(uAddress);
        etCity.setText(city);
        etState.setText(state);
        pincode.setText(uPincode);

        if (alternateNumber.equals("")) {
            altNnumber.setHint("Alternate Mobile Number");
        } else {

            altNnumber.setText(alternateNumber);

        }

        if (MY_TAGS.equals("")) {

            tags.setText(uTags);

        } else {

            tags.setText(MY_TAGS);

        }

        if (uPath.equals("")) {
            prf.setImageResource(R.drawable.prf_male);
        } else {
            Picasso.get().load(uPath + uImage).error(R.drawable.prf_male).into(prf);

        }


    }


    @Override
    public void onResume() {
        super.onResume();


    }


    private void updateLocation() {
        try {
            Geocoder geocoder;
            List<Address> addresses;


            strLatitude = String.valueOf(ll);
            strLongitude = String.valueOf(lo);

            geocoder = new Geocoder(getActivity(), Locale.getDefault());
            addresses = geocoder.getFromLocation(ll, lo, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5


            if (addresses != null) {
                String address = addresses.get(0).getAddressLine(0);

                // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
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
                pincode.setText(postalCode);
                strState = state;

            }


        } catch (IOException e) {
            Log.e("gfvdfrgvd", e.getMessage(), e);
        }

    }

    private void showPictureDialog() {
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(getActivity());
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {
                "Select photo from gallery",
                "Capture photo from camera"};
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                choosePhotoFromGallary();
                                break;
                            case 1:
                                takePhotoFromCamera();
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }

    public void choosePhotoFromGallary() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(galleryIntent, GALLERY);
    }

    private void takePhotoFromCamera() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_CANCELED) {
            return;
        }
        if (requestCode == GALLERY) {
            if (data != null) {
                Uri contentURI = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), contentURI);
                    String path = saveImage(bitmap);
                    prf.setImageBitmap(bitmap);

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "Failed!", Toast.LENGTH_SHORT).show();
                }
            }

        } else if (requestCode == CAMERA) {
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            prf.setImageBitmap(thumbnail);
            assert thumbnail != null;
            saveImage(thumbnail);

        }


    }

    public String saveImage(Bitmap myBitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File wallpaperDirectory = new File(
                Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY);

        if (!wallpaperDirectory.exists()) {
            wallpaperDirectory.mkdirs();
        }

        try {
            f = new File(wallpaperDirectory, Calendar.getInstance()
                    .getTimeInMillis() + ".jpg");
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(getActivity(),
                    new String[]{f.getPath()},
                    new String[]{"image/jpeg"}, null);
            fo.close();


            strImage = f.getAbsolutePath();

            Log.e("sjhdsd", f.getAbsolutePath());
            return f.getAbsolutePath();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return "";
    }


    public void updateProf() {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Profile Update");
        progressDialog.setMessage("please wait.....");
        progressDialog.setCancelable(false);
        progressDialog.show();


        final String userID = SharedHelper.getKey(getActivity(), AppConstats.USER_ID);

        AndroidNetworking.upload(API.BASE_URL)
                .addMultipartParameter("control", "update_profile")
                .addMultipartParameter("userID", userID)
                .addMultipartParameter("mobile", strNumber)
                .addMultipartFile("image", f)
                .addMultipartParameter("name", strName)
                .addMultipartParameter("shop_name", strShopName)
                .addMultipartParameter("description", strDiscretion)
                .addMultipartParameter("address", strAddress)
                .addMultipartParameter("latitude", strLatitude)
                .addMultipartParameter("longitude", strLongitude)
                .addMultipartParameter("city", strCity)
                .addMultipartParameter("state", strState)
                .addMultipartParameter("pinCODE", strPinCode)
                .addMultipartParameter("mobile2", strAlternateNumber)
                .setTag("update")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("smlkmdsklfcm", response.toString());
                        try {

                            if (response.has("result")) {

                                String strResult = response.getString("result");
                                String message = response.getString("message");

                                if (strResult.equals("true")) {

                                    String data = response.getString("data");

                                    JSONObject jsonObject = new JSONObject(data);

                                    String rName = jsonObject.getString("name");
                                    String rShopName = jsonObject.getString("shop_name");
                                    String rImage = jsonObject.getString("image");
                                    String rPath = jsonObject.getString("path");
                                    String rLattitude = jsonObject.getString("latitude");
                                    String rLongitude = jsonObject.getString("longitude");
                                    String rAddress = jsonObject.getString("address");
                                    String rMobile = jsonObject.getString("mobile");
                                    String rDiscretion = jsonObject.getString("description");
                                    String rCity = jsonObject.getString("city");
                                    String rState = jsonObject.getString("state");
                                    String mobile2 = jsonObject.getString("mobile2");
                                    String rPinCODE = jsonObject.getString("pinCODE");

                                    Log.e("aftgdsctfv", rName);
                                    Log.e("aftgdsctfv", rShopName);
                                    Log.e("aftgdsctfv", rPath + rImage);
                                    Log.e("aftgdsctfv", rLattitude);
                                    Log.e("aftgdsctfv", rLongitude);
                                    Log.e("aftgdsctfv", rName);
                                    Log.e("aftgdsctfv", rAddress);
                                    Log.e("aftgdsctfv", rMobile);
                                    Log.e("aftgdsctfv", rCity);
                                    Log.e("aftgdsctfv", rState);
                                    Log.e("fsdfsf", rPinCODE);

                                    name.setText(rName);
                                    shopname.setText(rShopName);
                                    number.setText(rMobile);
                                    discretion.setText(rDiscretion);
                                    address.setText(rAddress);
                                    altNnumber.setText(mobile2);
                                    pincode.setText(rPinCODE);
                                    SharedHelper.putKey(getActivity(), AppConstats.USER_ID, userID);
                                    SharedHelper.putKey(getActivity(), AppConstats.USER_NAME, rName);
                                    SharedHelper.putKey(getActivity(), AppConstats.USER_MOBILE_NUMBER, rMobile);
                                    SharedHelper.putKey(getActivity(), AppConstats.USER_SHOPNAME, rShopName);
                                    SharedHelper.putKey(getActivity(), AppConstats.DISCRETION, rDiscretion);
                                    SharedHelper.putKey(getActivity(), AppConstats.ADDRESS, rAddress);
                                    SharedHelper.putKey(getActivity(), AppConstats.USER_CITY, rCity);
                                    SharedHelper.putKey(getActivity(), AppConstats.USER_STATE, rState);
                                    SharedHelper.putKey(getActivity(), AppConstats.USER_IMAGE, rImage);
                                    SharedHelper.putKey(getActivity(), AppConstats.USER_PATH, rPath);
                                    SharedHelper.putKey(getActivity(), AppConstats.USER_LATITUDE, rLattitude);
                                    SharedHelper.putKey(getActivity(), AppConstats.USER_LONGITUDE, rLongitude);
                                    SharedHelper.putKey(getActivity(), AppConstats.USER_ALTERNATE_MOBILE, mobile2);
                                    SharedHelper.putKey(getActivity(), AppConstats.USER_PINCODE, rPinCODE);


                                    Glide.with(getActivity()).load(rPath + rImage).into(prf);
                                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MyProfileFragment()).commit();

                                    progressDialog.hide();
                                } else {
                                    Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                                    progressDialog.hide();
                                }
                            } else {
                                progressDialog.hide();
                                Toast.makeText(getActivity(), "Something went wrong !!", Toast.LENGTH_SHORT).show();

                            }

                        } catch (Exception e) {
                            progressDialog.hide();
                            Log.e("sjdhs", e.getMessage(), e);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        progressDialog.hide();
                        Log.e("sjdhs", anError.getMessage(), anError);
                    }
                });
    }
}