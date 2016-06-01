package com.inst.mobileinstitutions;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.inst.mobileinstitutions.API.APICredentials;
import com.inst.mobileinstitutions.API.Models.User;
import com.inst.mobileinstitutions.Forms.List.FormListActivity;
import com.inst.mobileinstitutions.API.APICall;

import rx.Subscriber;
import rx.functions.Action1;

public class LoginActivityFragment extends Fragment {

    private TextView textView;
    private CallbackManager mCallbackManager;
    private Profile profile;
    private AccessTokenTracker tokenTracker;
    private ProfileTracker profileTracker;

    public LoginActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.onActivityCreated(savedInstanceState);
//        try {
//            PackageInfo info = getPackageManager().getPackageInfo(
//                    "com.inst.mobileinstitutions",
//                    PackageManager.GET_SIGNATURES);
//            for (Signature signature : info.signatures) {
//                MessageDigest md = MessageDigest.getInstance("SHA");
//                md.update(signature.toByteArray());
//                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
//            }
//        } catch (PackageManager.NameNotFoundException e) {
//
//        } catch (NoSuchAlgorithmException e) {
//
//        }
        mCallbackManager = CallbackManager.Factory.create();
        tokenTracker=new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken accessToken, AccessToken accessToken1) {

            }
        };
        profileTracker=new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile profile, Profile profile1) {
                textView.setText(displayMessage(profile1));
            }
        };
        tokenTracker.startTracking();
        profileTracker.startTracking();
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        textView=(TextView)view.findViewById(R.id.text);
        textView.setText("You are not logged in");
        LoginButton loginButton = (LoginButton) view.findViewById(R.id.login_button);
        loginButton.setCompoundDrawables(null, null, null, null);
        loginButton.setReadPermissions("user_friends");
        loginButton.setFragment(this);
        loginButton.registerCallback(mCallbackManager, mFacebookCallback);
    }

    private FacebookCallback<LoginResult> mFacebookCallback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            AccessToken accessToken = loginResult.getAccessToken();
            profile = Profile.getCurrentProfile();
            textView.setText(displayMessage(profile));
            APICall.signUp(profile.getFirstName(),profile.getId());
            final String firstName=profile.getFirstName();
            final String password=profile.getId();
            APICall.getUserByEmail(firstName).subscribe(new Subscriber<User>() {
                @Override
                public void onCompleted() {
                    APICredentials.setUsername(firstName);
                    APICredentials.setPassword(password);
                    APICredentials.cancelAccessToken();
                }

                @Override
                public void onError(Throwable e) {
                    Log.d("error",e.toString());
                }

                @Override
                public void onNext(User user) {
                    APICredentials.setLoggedUser(user);
                }
            });
        }

        @Override
        public void onCancel() {

        }
        @Override
        public void onError(FacebookException e) {
            Toast.makeText(getActivity(), "Липса на интернет връзка..."+ e , Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && isNetworkAvailable()) {
            Intent secondActivityIntent = new Intent(getActivity(), FormListActivity.class);
            startActivity(secondActivityIntent);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Profile profile = Profile.getCurrentProfile();
        textView.setText(displayMessage(profile));
    }

    @Override
    public void onStop() {
        super.onStop();
        profileTracker.stopTracking();
        tokenTracker.stopTracking();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private String displayMessage(Profile profile) {
        StringBuilder stringBuilder = new StringBuilder();
        if (profile != null) {
            stringBuilder.append("Влязъл" + profile.getFirstName());
            Toast.makeText(getActivity(), "Влизане като: "+profile.getFirstName(), Toast.LENGTH_SHORT).show();
        }else{
            stringBuilder.append("Не успяхте да влезете!");
        }
        return stringBuilder.toString();
    }
}