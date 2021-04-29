package com.localtovocal.Fragments;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.localtovocal.Activities.AboutUsActivity;
import com.localtovocal.Activities.PrivacyPolicyActivity;
import com.localtovocal.Activities.RefundPolicyActivity;
import com.localtovocal.Activities.SplashActivity;
import com.localtovocal.Activities.SuggestionsActivity;
import com.localtovocal.Activities.TermAndConditionActivity;
import com.localtovocal.Others.AppConstats;
import com.localtovocal.Others.SharedHelper;
import com.localtovocal.R;

import java.util.Objects;


public class SettingsFragment extends Fragment {

    RelativeLayout logout,suggest,term,privacy,about,refund;
    GoogleSignInClient mGoogleSignInClient;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        logout = view.findViewById(R.id.logout);
        suggest = view.findViewById(R.id.suggest);
        term = view.findViewById(R.id.term);
        privacy = view.findViewById(R.id.privacy);
        about = view.findViewById(R.id.about);
        refund = view.findViewById(R.id.refund);

        googleSignIn();

        String userID = SharedHelper.getKey(getActivity(), AppConstats.USER_ID);

        if (userID.equals("")) {
            logout.setVisibility(View.GONE);
        } else {
            logout.setVisibility(View.VISIBLE);
        }


        term.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), TermAndConditionActivity.class));
            }
        });

        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), AboutUsActivity.class));
            }
        });
        privacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), PrivacyPolicyActivity.class));
            }
        });
        refund.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), RefundPolicyActivity.class));
            }
        });

        logout.setOnClickListener(view1 -> {

            String provider = SharedHelper.getKey(getActivity(), AppConstats.PROVIDER_NAME);

            Log.e("djahkjssa", provider);

            if (provider.equals("google")) {
                signOut();
            } else if (provider.equals("facebook")) {

                LoginManager.getInstance().logOut();
                SharedHelper.putKey(getActivity(), AppConstats.USER_ID, "");
                startActivity(new Intent(getActivity(), SplashActivity.class));
                getActivity().finish();

            } else {
                logout();
            }


        });



        suggest.setOnClickListener(v->{
            startActivity(new Intent(getActivity(), SuggestionsActivity.class));
        });

        return view;
    }


    public void googleSignIn() {

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(Objects.requireNonNull(getActivity()), gso);


    }


    private void signOut() {

        mGoogleSignInClient.signOut()
                .addOnCompleteListener(Objects.requireNonNull(getActivity()), task -> {

                    if (task.isSuccessful()) {

                        task.addOnSuccessListener(Objects.requireNonNull(getActivity()), new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                                SharedHelper.putKey(getActivity(), AppConstats.USER_ID, "");
                                startActivity(new Intent(getActivity(), SplashActivity.class));
                                getActivity().finish();

                            }
                        });
                    } else {
                        Toast.makeText(getActivity(), Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }


    public void logout() {
        final Dialog dialog = new Dialog(Objects.requireNonNull(getActivity()));
        dialog.setContentView(R.layout.logout);
        RelativeLayout relYes = dialog.findViewById(R.id.relYes);
        RelativeLayout relNo = dialog.findViewById(R.id.relNo);

        relYes.setOnClickListener(v -> {

            SharedHelper.putKey(getActivity(), AppConstats.USER_ID, "");
            startActivity(new Intent(getActivity(), SplashActivity.class));
            getActivity().finish();

        });


        relNo.setOnClickListener(v -> dialog.dismiss());


        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }
}