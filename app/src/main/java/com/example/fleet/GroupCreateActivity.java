package com.example.fleet;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class GroupCreateActivity extends AppCompatActivity {
    private EditText groupNameInput, groupDescriptionInput;
    private String newGroupID = null;
    private ImageView confirmBtn, cancelBtn;
    private User user;
    private RequestQueue requestQueue;
    private ArrayList<String> groupIDs = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_create_view);
        requestQueue = Volley.newRequestQueue(getApplicationContext());

        user = getIntent().getExtras().getParcelable("user");

        groupNameInput = findViewById(R.id.groupNameInput);
        groupDescriptionInput = findViewById(R.id.groupDescriptionInput);

        confirmBtn = findViewById(R.id.confirmButton);
        confirmBtn.setOnClickListener(v -> {
            if (!groupNameInput.getText().toString().equals("")) {
                doEverything();
            } else {
                Toast.makeText(GroupCreateActivity.this, "Name is required", Toast.LENGTH_LONG).show();
            }
        });
        cancelBtn = findViewById(R.id.cancelBtn);
        cancelBtn.setOnClickListener(v -> goToGroupActivity());
    }

    public void doEverything() {
        getGroupIDs();
    }

    public void generateNewID() {
        int tempID = 0;
        while (exists() || tempID == 0) {
            tempID = 1 + (int) (Math.random() * 999998);
        }
        newGroupID = String.valueOf(tempID);
        makeGroup();

    }

    public boolean exists() {
        for (String id : groupIDs) {
            if (id.equals(newGroupID)) {
                return true;
            }
        }
        return false;
    }

    public void getGroupIDs() {
        JsonArrayRequest groupIDsRequest = new JsonArrayRequest(Request.Method.GET, "https://studev.groept.be/api/a20sd108/get_all_groups", null, response -> {
            for (int i = 0; i < response.length(); i++) {
                JSONObject responseIDs;
                try {
                    responseIDs = response.getJSONObject(i);
                    groupIDs.add(responseIDs.getString("groupID"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            generateNewID();
        }, error -> Toast.makeText(GroupCreateActivity.this, "Unable to communicate with the server", Toast.LENGTH_LONG).show());
        requestQueue.add(groupIDsRequest);
    }

    public void makeGroup() {
        JsonArrayRequest newGroupRequest = new JsonArrayRequest(Request.Method.GET, "https://studev.groept.be/api/a20sd108/make_group/" + newGroupID + "/" + groupNameInput.getText().toString() + "/" + groupDescriptionInput.getText().toString()+ "/" + newGroupID + "/" + user.getId(), null, response -> goToGroupActivity(), error -> Toast.makeText(GroupCreateActivity.this, "Unable to communicate with the server", Toast.LENGTH_LONG).show());
        requestQueue.add(newGroupRequest);
    }

    public void goToGroupActivity() {
        Intent intent = new Intent(GroupCreateActivity.this, GroupActivity.class);
        intent.putExtra("user", this.user);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_down, R.anim.slide_up);
    }
}