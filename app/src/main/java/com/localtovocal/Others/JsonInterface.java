package com.localtovocal.Others;

import com.localtovocal.RetrofitModels.AboutUsModel;
import com.localtovocal.RetrofitModels.AdvertisementModel;
import com.localtovocal.RetrofitModels.BookingPlan;
import com.localtovocal.RetrofitModels.DeletePost;
import com.localtovocal.RetrofitModels.EditDescriptionModel;
import com.localtovocal.RetrofitModels.ForgotPassModel;
import com.localtovocal.RetrofitModels.GenrateOtpModel;
import com.localtovocal.RetrofitModels.LoginData;
import com.localtovocal.RetrofitModels.NewsDetailData;
import com.localtovocal.RetrofitModels.PaytmChecksumData;
import com.localtovocal.RetrofitModels.PostAdvertisement;
import com.localtovocal.RetrofitModels.PrivacyPolicyModel;
import com.localtovocal.RetrofitModels.RefundPolicyModel;
import com.localtovocal.RetrofitModels.ReviewModel;
import com.localtovocal.RetrofitModels.SearchShopData;
import com.localtovocal.RetrofitModels.ShowLocals;
import com.localtovocal.RetrofitModels.ShowReviewModel;
import com.localtovocal.RetrofitModels.ShowTags;
import com.localtovocal.RetrofitModels.ShowUserPosts;
import com.localtovocal.RetrofitModels.SubscribePlanModel;
import com.localtovocal.RetrofitModels.SuggestionsData;
import com.localtovocal.RetrofitModels.TermsAndConditionsData;
import com.localtovocal.RetrofitModels.TextModel;
import com.localtovocal.RetrofitModels.UpdateTagData;
import com.localtovocal.RetrofitModels.UpdateUserData;
import com.localtovocal.RetrofitModels.UserSignUp;
import com.localtovocal.RetrofitModels.VerifyOtpModel;

import java.util.Map;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;


public interface JsonInterface {

    @FormUrlEncoded
    @POST("control")
    Call<LoginData> userLogin(@FieldMap Map<String, String> params);


    @POST("control")
    Call<UserSignUp> newUser(@Body MultipartBody image);


    @POST("control")
    Call<UpdateUserData> updateUserData(@Body MultipartBody image);

    @FormUrlEncoded
    @POST("control")
    Call<NewsDetailData> showNewsDetails(@FieldMap Map<String,String> param);

    @FormUrlEncoded
    @POST("control")
    Call<ShowLocals> showLocals(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("control")
    Call<TermsAndConditionsData> termsAnConditions(@Field("control") String control);


    @FormUrlEncoded
    @POST("control")
    Call<ShowUserPosts> showUserPosts(@FieldMap Map<String, String> params);


    @FormUrlEncoded
    @POST("control")
    Call<ShowTags> showUserTags(@FieldMap Map<String, String> params);


    @FormUrlEncoded
    @POST("control")
    Call<SearchShopData> searchShops(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("control")
    Call<UpdateTagData> updateTagsData(@FieldMap Map<String, String> params);


    @FormUrlEncoded
    @POST("control")
    Call<SuggestionsData> sendSuggestion(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("control")
    Call<DeletePost> deletePost(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("control")
    Call<SubscribePlanModel> subscribePlans(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("control")
    Call<TextModel> showText(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("control")
    Call<AdvertisementModel> showAd(@Field("control") String control);

    @FormUrlEncoded
    @POST("control")
    Call<BookingPlan> bookPlan(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("control")
    Call<ReviewModel> giveReview(@FieldMap Map<String, String> params);


    @FormUrlEncoded
    @POST("control")
    Call<PostAdvertisement> postAdv(@FieldMap Map<String, String> params);


    @FormUrlEncoded
    @POST("control")
    Call<ShowReviewModel> showReviews(@FieldMap Map<String, String> params);


    @FormUrlEncoded
    @POST("control")
    Call<EditDescriptionModel> editDescription(@FieldMap Map<String, String> params);


    @FormUrlEncoded
    @POST("control")
    Call<ForgotPassModel> forgotPassword(@FieldMap Map<String, String> params);



    @FormUrlEncoded
    @POST("control")
    Call<PrivacyPolicyModel> privacypolicy(@Field("control") String control);

    @FormUrlEncoded
    @POST("control")
    Call<AboutUsModel> aboutus(@Field("control") String control);/*Params nahi ja rhe hain thats why field control @Field("control") pass here*/


    @FormUrlEncoded
    @POST("control")
    Call<RefundPolicyModel>refundPolicy(@Field("control") String control);


    @FormUrlEncoded
    @POST(API.BASE_URL)
    Call<GenrateOtpModel>genrateOtp(@FieldMap Map<String, String> params);/*Params ja rhe hain thats why field control@Field("control") not pass here*/

    @FormUrlEncoded
    @POST(API.BASE_URL)
    Call<VerifyOtpModel>verifyOtp(@FieldMap Map<String, String> params);/*Params ja rhe hain thats why field control@Field("control") not pass here*/

}
