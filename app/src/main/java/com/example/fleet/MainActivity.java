package com.example.fleet;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private CallbackManager callbackManager;
    private LoginButton loginButton;
    private String id;
    private ImageView logo_btn;
    private String firstName, lastName;
    private User user = new User();
    private Boolean guiResponseFlag = false;
    private ArrayList<Group> groups = new ArrayList<>();
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        HttpHandler databaseReq = new HttpHandler();
        requestQueue = Volley.newRequestQueue(getApplicationContext());

        graphRequest();

        if (AccessToken.getCurrentAccessToken() != null && id != null) {
            getGroupInfo();
        }

        loginButton = findViewById(R.id.login_button);
        logo_btn = findViewById(R.id.logo_btn);

        callbackManager = CallbackManager.Factory.create();

        loginButton.setPermissions(Arrays.asList("user_friends"));
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                databaseReq.httpRequest("https://studev.groept.be/api/a20sd108/add_user/" + id + "/" + firstName + "/" + lastName, getApplicationContext());
                Log.d("demo ", "login successful");
            }

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
                if (AccessToken.getCurrentAccessToken() != null && id != null) {
                    getGroupInfo();
                }
            }
        });
    }

    public void graphRequest() {
        GraphRequest graphRequest = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                try {
                    id = object.getString("id");
                    user.setId(id);
                    firstName = object.getString("first_name");
                    user.setFirst_name(firstName);
                    lastName = object.getString("last_name");
                    user.setLast_name(lastName);
                    getGroupInfo();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        graphRequest();
    }

    AccessTokenTracker accessTokenTracker = new AccessTokenTracker() {
        @Override
        protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
            if (currentAccessToken == null) {
                LoginManager.getInstance().logOut();
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        accessTokenTracker.stopTracking();
    }

    public void getGroupInfo() {
        user.resetBundle();
        JsonArrayRequest groupInfoRequest = new JsonArrayRequest(Request.Method.GET, "https://studev.groept.be/api/a20sd108/get_group_info/" + user.getId(), null, response -> {
            for (int i = 0; i < response.length(); i++) {
                JSONObject groupInfo;
                try {
                    groupInfo = response.getJSONObject(i);
                    this.user.addGroupToBundle(new Group(groupInfo.getString("groupID"), groupInfo.getString("name"), groupInfo.getString("description")));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            goToGroupActivity();
        }, error -> {
        });
        requestQueue.add(groupInfoRequest);
    }

    public void goToGroupActivity() {
        Intent intent = new Intent(MainActivity.this, GroupActivity.class);
        intent.putExtra("user", user);
        startActivity(intent);
    }
}