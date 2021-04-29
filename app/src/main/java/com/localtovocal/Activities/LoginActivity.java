package com.localtovocal.Activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.Circle;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.localtovocal.Others.API;
import com.localtovocal.Others.AppConstats;
import com.localtovocal.Others.JsonInterface;
import com.localtovocal.Others.SharedHelper;
import com.localtovocal.R;
import com.localtovocal.RetrofitModels.LoginData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {

    TextView signUp, forgotPass;
    Button btnLogin;
    EditText email, password;
    String strEmail = "", strPassword = "", facebook_auth_id = "";
    JsonInterface jsonInterface;
    Retrofit retrofit;
    ProgressBar progress;

    CallbackManager callbackManager;
    ImageView fb, google;
    LoginButton loginButton;
    public static GoogleSignInClient mGoogleSignInClient;


    public static final int RC_GOOGLE_SIGN_IN = 9999;
    SignInButton signInButton;
    RelativeLayout facebookAnimation;


    String fbTags = "", googleTags = "", fbTagID = "", googleTagID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        signUp = findViewById(R.id.signUp);
        forgotPass = findViewById(R.id.forgotPass);
        google = findViewById(R.id.google);
        btnLogin = findViewById(R.id.btnLogin);
        email = findViewById(R.id.email);
        fb = findViewById(R.id.fb);
        password = findViewById(R.id.password);
        facebookAnimation = findViewById(R.id.facebookAnimation);
        progress = findViewById(R.id.progress);
        Sprite doubleBounce = new Circle();
        progress.setIndeterminateDrawable(doubleBounce);


        forgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ForgotPasswordActivity.class));
            }
        });


        ///Google///


        googleSignIn();


        //////Facebook
        callbackManager = CallbackManager.Factory.create();
        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions("email");


        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                Log.e("dcadfad", loginResult + "Success");
                Profile profile = Profile.getCurrentProfile();
                if (profile != null) {
                    facebook_auth_id = profile.getId();
                    String name = profile.getFirstName();
                    Log.e("ccvdcfadcf", profile.getId());
                    Log.e("ccvdcfadcf", "facebook");
                    Log.e("ccvdcfadcf", profile.getFirstName());

                    soicalFacebookLogin(facebook_auth_id, "facebook", name);
                }

                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                Log.e("uireuit", response.toString());
                                try {

                                    long fb_id = object.getLong("id");//use this for logout
                                    String email = object.getString("email");

                                    Log.e("ywerweuy", fb_id + "");
                                    Log.e("ywerweuy", email);

                                } catch (JSONException e) {
                                    Log.e("ywerweuy", e.getMessage(), e);
                                }


                            }

                        });

                request.executeAsync();


            }

            @Override
            public void onCancel() {
                Log.e("dcadfad", "Cancel");
            }

            @Override
            public void onError(FacebookException exception) {
                Log.e("dcadfad", Objects.requireNonNull(exception.getMessage()));

            }
        });

        retrofit = new Retrofit.Builder().baseUrl(API.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        jsonInterface = retrofit.create(JsonInterface.class);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                strEmail = email.getText().toString().trim();
                strPassword = password.getText().toString().trim();
                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.[a-z]+";

                if (TextUtils.isEmpty(strEmail)) {
                    Toast.makeText(LoginActivity.this, "Enter your email", Toast.LENGTH_SHORT).show();
                } else if (!strEmail.matches(emailPattern)) {
                    Toast.makeText(LoginActivity.this, "Invalid email", Toast.LENGTH_SHORT).show();

                } else if (TextUtils.isEmpty(strPassword)) {
                    Toast.makeText(LoginActivity.this, "Enter your password", Toast.LENGTH_SHORT).show();
                } else {
                    login();
                }

            }
        });

        signUp.setOnClickListener(view -> {
            SharedHelper.putKey(getApplicationContext(), AppConstats.SELECTED_TAGS, "");
            SharedHelper.putKey(getApplicationContext(), AppConstats.SELECTED_TAGS_ID, "");
            SharedHelper.putKey(getApplicationContext(), AppConstats.S_USERNAME, "");
            SharedHelper.putKey(getApplicationContext(), AppConstats.S_EMAIL, "");
            SharedHelper.putKey(getApplicationContext(), AppConstats.S_PASSWORD, "");
            SharedHelper.putKey(getApplicationContext(), AppConstats.S_MOBILE, "");
            SharedHelper.putKey(getApplicationContext(), AppConstats.S_SHOPNAME, "");


            startActivity(new Intent(getApplicationContext(), SignupActivity.class));
            Animatoo.animateShrink(LoginActivity.this);
            finish();
        });


    }


    public void googleSignIn() {


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();


        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


        signInButton = findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);

        google.setOnClickListener(view -> signIn());
    }

    @Override
    protected void onStart() {
        super.onStart();
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);

        if (acct != null) {
            String personName = acct.getDisplayName();
            String personGivenName = acct.getGivenName();
            String personFamilyName = acct.getFamilyName();
            String personEmail = acct.getEmail();
            String personId = acct.getId();
            Uri personPhoto = acct.getPhotoUrl();
        }

    }


    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_GOOGLE_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        callbackManager.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_GOOGLE_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }


    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {

            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            if (account != null) {
                String name = account.getDisplayName();
                String email = account.getEmail();
                String authID = account.getId();
                Uri imageURI = account.getPhotoUrl();
                String image = String.valueOf(imageURI);

                Log.e("dfdfsdfsdfs", name + "," + email + "," + authID + "," + image);
                soicalGoogleLogin(authID, "google", name, email, image);
            }


        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.e("kjckjsc", "signInResult:failed code=" + e.getStatusCode());
        }
    }

    public void login() {

        progress.setVisibility(View.VISIBLE);

        Map<String, String> map = new HashMap<>();
        map.put("control", "login");
        map.put("email", strEmail);
        map.put("password", strPassword);

        Call<LoginData> call = jsonInterface.userLogin(map);
        call.enqueue(new Callback<LoginData>() {
            @Override
            public void onResponse(Call<LoginData> call, Response<LoginData> response) {

                if (!response.isSuccessful()) {
                    Toast.makeText(LoginActivity.this, response.code(), Toast.LENGTH_SHORT).show();
                    progress.setVisibility(View.GONE);
                }

                Log.e("cmnkjcn", response.body().toString());

                LoginData loginData = response.body();
                String msg = loginData.getMessage();

                if (loginData.getResult()) {

                    LoginData.Data data = loginData.getData();

                    String userID = data.getUserID();
                    String type = data.getType();
                    String name = data.getName();
                    String password = data.getPassword();
                    String email = data.getEmail();
                    String mobile = data.getMobile();
                    String shop_name = data.getShopName();
                    String description = data.getDescription();
                    String address = data.getAddress();
                    String image = data.getImage();
                    String path = data.getPath();
                    String lat = data.getLatitude();
                    String lon = data.getLongitude();
                    String city = data.getCity();
                    String state = data.getState();
                    String mobile2 = data.getMobile2();
                    String subscription_status = data.getSubscription_status();

                    Log.e("djsdj", subscription_status);

                    List<LoginData.Data.Tag> tagList = data.getTag();
                    StringBuilder stringBuilder = new StringBuilder();
                    StringBuilder stringBuilder2 = new StringBuilder();

                    for (LoginData.Data.Tag myTags : tagList) {
                        stringBuilder.append(myTags.getTagName());
                        stringBuilder.append(",");

                        stringBuilder2.append(myTags.getTagID());
                        stringBuilder2.append(",");
                        Log.e("rettre", myTags.getTagID() + "," + myTags.getTagName());
                    }

                    String finalTag = stringBuilder.toString();
                    finalTag = finalTag.substring(0, finalTag.length() - 1);

                    String finalTagID = stringBuilder2.toString();
                    finalTagID = finalTagID.substring(0, finalTagID.length() - 1);

                    Log.e("tertret", finalTag);
                    Log.e("tertret", finalTagID);

                    Log.e("xdsbdjhsgc", userID);
                    Log.e("xdsbdjhsgc", type);
                    Log.e("xdsbdjhsgc", name);
                    Log.e("xdsbdjhsgc", email);
                    Log.e("xdsbdjhsgc", mobile);
                    Log.e("xdsbdjhsgc", image);
                    Log.e("xdsbdjhsgc", path);
                    Log.e("xdsbdjhsgc", shop_name);
                    Log.e("xdsbdjhsgc", description);
                    Log.e("xdsbdjhsgc", city);
                    Log.e("xdsbdjhsgc", state);
                    Log.e("xdsbdjhsgc", mobile2);


                    Log.e("xdsbdjhsgc", lat);
                    Log.e("xdsbdjhsgc", lon);

                    progress.setVisibility(View.GONE);

                    SharedHelper.putKey(getApplicationContext(), AppConstats.USER_ID, userID);
                    SharedHelper.putKey(getApplicationContext(), AppConstats.USER_TYPE, type);
                    SharedHelper.putKey(getApplicationContext(), AppConstats.USER_NAME, name);
                    SharedHelper.putKey(getApplicationContext(), AppConstats.USER_EMAIL, email);
                    SharedHelper.putKey(getApplicationContext(), AppConstats.USER_MOBILE_NUMBER, mobile);
                    SharedHelper.putKey(getApplicationContext(), AppConstats.USER_SHOPNAME, shop_name);
                    SharedHelper.putKey(getApplicationContext(), AppConstats.TAG, finalTag);
                    SharedHelper.putKey(getApplicationContext(), AppConstats.TAG_ID, finalTagID);
                    SharedHelper.putKey(getApplicationContext(), AppConstats.DISCRETION, description);
                    SharedHelper.putKey(getApplicationContext(), AppConstats.ADDRESS, address);
                    SharedHelper.putKey(getApplicationContext(), AppConstats.USER_CITY, city);
                    SharedHelper.putKey(getApplicationContext(), AppConstats.USER_STATE, state);
                    SharedHelper.putKey(getApplicationContext(), AppConstats.USER_ALTERNATE_MOBILE, mobile2);
                    SharedHelper.putKey(getApplicationContext(), AppConstats.USER_SUBSCRIPTION_STATUS, subscription_status);

                    SharedHelper.putKey(getApplicationContext(), AppConstats.USER_IMAGE, image);
                    SharedHelper.putKey(getApplicationContext(), AppConstats.USER_PATH, path);
                    SharedHelper.putKey(getApplicationContext(), AppConstats.USER_LATITUDE, lat);
                    SharedHelper.putKey(getApplicationContext(), AppConstats.USER_LONGITUDE, lon);
                    SharedHelper.putKey(LoginActivity.this, AppConstats.PROVIDER_NAME, "");

                    startActivity(new Intent(getApplicationContext(), BottomNavigationActivity.class));
                    Animatoo.animateShrink(LoginActivity.this);
                    finish();

                } else {
                    progress.setVisibility(View.GONE);
                    Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<LoginData> call, Throwable t) {
                Log.e("xdsbdjhsgc", t.getMessage());
                progress.setVisibility(View.GONE);
            }
        });
    }


    public void onClick(View view) {
        if (view == fb) {
            loginButton.performClick();
        }

        if (view == google) {
            signInButton.performClick();
        }


    }


    public void soicalGoogleLogin(String authID, String provider, String name, String email, String image) {

        AndroidNetworking.post(API.BASE_URL)
                .addBodyParameter("control", "social_signup")
                .addBodyParameter("auth_id", authID)
                .addBodyParameter("auth_provider", provider)
                .addBodyParameter("name", name)
                .addBodyParameter("email", email)
                .addBodyParameter("image", image)
                .addBodyParameter("type", "1")
                .setTag("social")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.e("dcnskdskj", response.toString());

                        try {

                            if (response.has("result")) {

                                String strResult = response.getString("result");
                                String message = response.getString("message");

                                if (strResult.equals("true")) {

                                    String data = response.getString("data");

                                    JSONObject jsonObject = new JSONObject(data);

                                    String userID = jsonObject.getString("userID");
                                    String name = jsonObject.getString("name");
                                    String email = jsonObject.getString("email");
                                    String auth_id = jsonObject.getString("auth_id");
                                    String auth_provider = jsonObject.getString("auth_provider");
                                    String type = jsonObject.getString("type");
                                    String mobile = jsonObject.getString("mobile");
                                    String shop_name = jsonObject.getString("shop_name");
                                    String description = jsonObject.getString("description");
                                    String address = jsonObject.getString("address");
                                    String image = jsonObject.getString("image");
                                    String tag = jsonObject.getString("tag");
                                    String latitude = jsonObject.getString("latitude");
                                    String longitude = jsonObject.getString("longitude");
                                    String reg_id = jsonObject.getString("reg_id");
                                    String path = jsonObject.getString("path");
                                    String mobile2 = jsonObject.getString("mobile2");
                                    String subscription_status = jsonObject.getString("subscription_status");


                                    JSONArray jsonArray = new JSONArray(tag);

                                    if (jsonArray.length() > 0) {
                                        StringBuilder stringBuilder = new StringBuilder();
                                        StringBuilder stringBuilder2 = new StringBuilder();

                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject object = jsonArray.getJSONObject(i);

                                            String tagID = object.getString("tagID");
                                            String tag_name = object.getString("tag_name");
                                            Log.e("dkmslkdsj", tag_name);
                                            stringBuilder.append(tag_name);
                                            stringBuilder.append(",");

                                            stringBuilder2.append(tagID);
                                            stringBuilder2.append(",");

                                        }

                                        googleTags = stringBuilder.toString();
                                        googleTags = googleTags.substring(0, googleTags.length() - 1);

                                        googleTagID = stringBuilder2.toString();
                                        googleTagID = googleTagID.substring(0, googleTagID.length() - 1);


                                    } else {
                                        googleTags = "";
                                    }


                                    Log.e("dcnskdskj", userID);
                                    Log.e("dcnskdskj", auth_id);
                                    Log.e("dcnskdskj", auth_provider);


                                    SharedHelper.putKey(getApplicationContext(), AppConstats.USER_ID, userID);
                                    SharedHelper.putKey(getApplicationContext(), AppConstats.USER_TYPE, type);
                                    SharedHelper.putKey(getApplicationContext(), AppConstats.USER_NAME, name);
                                    SharedHelper.putKey(getApplicationContext(), AppConstats.USER_EMAIL, email);
                                    SharedHelper.putKey(getApplicationContext(), AppConstats.USER_MOBILE_NUMBER, mobile);
                                    SharedHelper.putKey(getApplicationContext(), AppConstats.USER_SHOPNAME, shop_name);
                                    SharedHelper.putKey(getApplicationContext(), AppConstats.TAG, googleTags);
                                    SharedHelper.putKey(getApplicationContext(), AppConstats.TAG_ID, googleTagID);
                                    SharedHelper.putKey(getApplicationContext(), AppConstats.DISCRETION, description);
                                    SharedHelper.putKey(getApplicationContext(), AppConstats.ADDRESS, address);
                                    SharedHelper.putKey(getApplicationContext(), AppConstats.USER_IMAGE, image);
                                    SharedHelper.putKey(getApplicationContext(), AppConstats.USER_PATH, path);
                                    SharedHelper.putKey(LoginActivity.this, AppConstats.AUTH_ID, auth_id);
                                    SharedHelper.putKey(LoginActivity.this, AppConstats.PROVIDER_NAME, auth_provider);
                                    SharedHelper.putKey(getApplicationContext(), AppConstats.USER_LATITUDE, latitude);
                                    SharedHelper.putKey(getApplicationContext(), AppConstats.USER_LONGITUDE, longitude);
                                    SharedHelper.putKey(getApplicationContext(), AppConstats.USER_ALTERNATE_MOBILE, mobile2);
                                    SharedHelper.putKey(getApplicationContext(), AppConstats.USER_SUBSCRIPTION_STATUS, subscription_status);

                                    startActivity(new Intent(getApplicationContext(), BottomNavigationActivity.class));
                                    Animatoo.animateShrink(LoginActivity.this);
                                    finishAffinity();


                                } else {
                                    Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                                }

                            } else {
                                Toast.makeText(LoginActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();

                            }

                        } catch (Exception e) {
                            Log.e("dxksjkjs", e.getMessage());
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("dxksjkjs", anError.getMessage());
                    }
                });

    }


    public void soicalFacebookLogin(String authID, String provider, String name) {


        facebookAnimation.setVisibility(View.VISIBLE);
        AndroidNetworking.post(API.BASE_URL)
                .addBodyParameter("control", "social_signup")
                .addBodyParameter("auth_id", authID)
                .addBodyParameter("auth_provider", provider)
                .addBodyParameter("name", name)
                .addBodyParameter("type", "1")
                .setTag("social")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.e("dcnskdskj", response.toString());
                        facebookAnimation.setVisibility(View.GONE);
                        try {


                            if (response.has("result")) {

                                String strResult = response.getString("result");
                                String message = response.getString("message");

                                if (strResult.equals("true")) {

                                    String data = response.getString("data");

                                    JSONObject jsonObject = new JSONObject(data);

                                    String userID = jsonObject.getString("userID");
                                    String name = jsonObject.getString("name");
                                    String email = jsonObject.getString("email");
                                    String auth_id = jsonObject.getString("auth_id");
                                    String auth_provider = jsonObject.getString("auth_provider");
                                    String type = jsonObject.getString("type");
                                    String mobile = jsonObject.getString("mobile");
                                    String shop_name = jsonObject.getString("shop_name");
                                    String description = jsonObject.getString("description");
                                    String address = jsonObject.getString("address");
                                    String image = jsonObject.getString("image");
                                    String tag = jsonObject.getString("tag");
                                    String latitude = jsonObject.getString("latitude");
                                    String longitude = jsonObject.getString("longitude");
                                    String reg_id = jsonObject.getString("reg_id");
                                    String path = jsonObject.getString("path");
                                    String city = jsonObject.getString("city");
                                    String state = jsonObject.getString("state");
                                    String mobile2 = jsonObject.getString("mobile2");
                                    String subscription_status = jsonObject.getString("subscription_status");
                                    String pinCODE = jsonObject.getString("pinCODE");


                                    JSONArray jsonArray = new JSONArray(tag);

                                    if (jsonArray.length() > 0) {
                                        StringBuilder stringBuilder = new StringBuilder();
                                        StringBuilder stringBuilder2 = new StringBuilder();

                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject object = jsonArray.getJSONObject(i);

                                            String tagID = object.getString("tagID");
                                            String tag_name = object.getString("tag_name");

                                            Log.e("dkmslkdsj", tag_name);

                                            stringBuilder.append(tag_name);
                                            stringBuilder.append(",");

                                            stringBuilder2.append(tagID);
                                            stringBuilder2.append(",");

                                        }

                                        fbTags = stringBuilder.toString();
                                        fbTags = fbTags.substring(0, fbTags.length() - 1);

                                        fbTagID = stringBuilder2.toString();
                                        fbTagID = fbTagID.substring(0, fbTagID.length() - 1);


                                    } else {
                                        fbTags = "";
                                    }


                                    SharedHelper.putKey(getApplicationContext(), AppConstats.USER_ID, userID);
                                    SharedHelper.putKey(getApplicationContext(), AppConstats.USER_TYPE, type);
                                    SharedHelper.putKey(getApplicationContext(), AppConstats.USER_NAME, name);
                                    SharedHelper.putKey(getApplicationContext(), AppConstats.USER_EMAIL, email);
                                    SharedHelper.putKey(getApplicationContext(), AppConstats.USER_MOBILE_NUMBER, mobile);
                                    SharedHelper.putKey(getApplicationContext(), AppConstats.USER_SHOPNAME, shop_name);
                                    SharedHelper.putKey(getApplicationContext(), AppConstats.TAG, fbTags);
                                    SharedHelper.putKey(getApplicationContext(), AppConstats.TAG_ID, fbTagID);
                                    SharedHelper.putKey(getApplicationContext(), AppConstats.DISCRETION, description);
                                    SharedHelper.putKey(getApplicationContext(), AppConstats.ADDRESS, address);
                                    SharedHelper.putKey(getApplicationContext(), AppConstats.USER_CITY, city);
                                    SharedHelper.putKey(getApplicationContext(), AppConstats.USER_STATE, state);
                                    SharedHelper.putKey(getApplicationContext(), AppConstats.USER_ALTERNATE_MOBILE, mobile2);
                                    SharedHelper.putKey(getApplicationContext(), AppConstats.USER_PINCODE, pinCODE);

                                    SharedHelper.putKey(getApplicationContext(), AppConstats.USER_IMAGE, image);
                                    SharedHelper.putKey(getApplicationContext(), AppConstats.USER_PATH, path);
                                    SharedHelper.putKey(LoginActivity.this, AppConstats.AUTH_ID, auth_id);
                                    SharedHelper.putKey(LoginActivity.this, AppConstats.PROVIDER_NAME, auth_provider);
                                    SharedHelper.putKey(getApplicationContext(), AppConstats.USER_LATITUDE, latitude);
                                    SharedHelper.putKey(getApplicationContext(), AppConstats.USER_LONGITUDE, longitude);
                                    SharedHelper.putKey(getApplicationContext(), AppConstats.USER_SUBSCRIPTION_STATUS, subscription_status);

                                    facebookAnimation.setVisibility(View.GONE);
                                    startActivity(new Intent(getApplicationContext(), BottomNavigationActivity.class));
                                    Animatoo.animateShrink(LoginActivity.this);
                                    finishAffinity();


                                } else {
                                    facebookAnimation.setVisibility(View.GONE);
                                    Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                                }

                            } else {
                                facebookAnimation.setVisibility(View.GONE);
                                Toast.makeText(LoginActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();

                            }

                        } catch (Exception e) {
                            facebookAnimation.setVisibility(View.GONE);
                            Log.e("dxksjkjs", e.getMessage(), e);
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        facebookAnimation.setVisibility(View.GONE);
                        Log.e("dxksjkjs", anError.getMessage(), anError);
                    }
                });

    }


}