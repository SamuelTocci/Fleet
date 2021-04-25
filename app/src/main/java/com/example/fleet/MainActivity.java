package com.example.fleet;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private CallbackManager callbackManager;
    private LoginButton loginButton;
    private String id;
    private ImageView logo_btn;
    private String firstName, lastName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(AccessToken.getCurrentAccessToken() != null && id != null){
            createUserSQL();
            Intent intent = new Intent(MainActivity.this, GroupActivity.class);
            intent.putExtra("userId", id);
            startActivity(intent);
        }

        loginButton = findViewById(R.id.login_button);
        logo_btn = findViewById(R.id.logo_btn);

        callbackManager = CallbackManager.Factory.create();

        loginButton.setPermissions(Arrays.asList("user_friends"));
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) { Log.d("demo ", "login successful"); }

            @Override
            public void onCancel() {
                Log.d("demo ", "login canceled");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d("demo ", "login error");
            }
        });

        logo_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                graphRequest();
                if(AccessToken.getCurrentAccessToken() != null && id != null){
                    createUserSQL();
                    Intent intent = new Intent(MainActivity.this, GroupActivity.class);
                    intent.putExtra("userId", id);
                    startActivity(intent);
                }
            }
        });
    }

    public ArrayList<String> httpRequest(String url) {
        ArrayList<String> output = new ArrayList<>();
        new Thread(new Runnable(){
            @Override
            public void run() {
                BufferedReader in = null;
                try {
                    URL myURL = new URL(url);
                    URLConnection myURLConnection = myURL.openConnection();
                    myURLConnection.connect();

                    in = new BufferedReader(new InputStreamReader(
                            myURLConnection.getInputStream()));
                    String inputLine;
                    while ((inputLine = in.readLine()) != null) {
                        output.add(inputLine);
                    }
                    in.close();
                } catch (MalformedURLException e) {
                    Log.d("demo", e.toString());
                } catch (IOException e) {
                    Log.d("demo", e.toString());
                }
            }
        }).start();
        return output;
    }

    //TODO id is altijd null momenteel
    public void graphRequest(){
        GraphRequest graphRequest = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                Log.d("demo", object.toString());
                try {
                    id = object.getString("id");
                    firstName = object.getString("first_name");
                    lastName = object.getString("last_name");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        Bundle bundle = new Bundle();
        bundle.putString("fields", "name, id, first_name, last_name");
        graphRequest.setParameters(bundle);
        graphRequest.executeAsync();
    }

    public void createUserSQL(){
        if(httpRequest("https://studev.groept.be/api/a20sd108/user_info_req/"+ id) == null){
            httpRequest("https://studev.groept.be/api/a20sd108/add_user/"+ id + "/" + firstName + "/" + lastName );
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        graphRequest();
    }

    AccessTokenTracker accessTokenTracker = new AccessTokenTracker() {
        @Override
        protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
            if(currentAccessToken == null){
                LoginManager.getInstance().logOut();
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        accessTokenTracker.stopTracking();
    }


}