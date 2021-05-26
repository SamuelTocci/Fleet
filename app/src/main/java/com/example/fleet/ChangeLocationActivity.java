package com.example.fleet;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class ChangeLocationActivity extends AppCompatActivity {

    private ImageView cancel, confirm;
    private TextView x_coord, y_coord;
    private User user;
    private String groupId;
    private int groupStatus;
    private RequestQueue requestQueue;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_location_view);

        user = getIntent().getExtras().getParcelable("user");
        groupId = getIntent().getExtras().getString("groupId");
        groupStatus = getIntent().getExtras().getInt("groupStatus");

        cancel = findViewById(R.id.cancel_location);
        confirm = findViewById(R.id.add_location);
        x_coord = findViewById(R.id.x_coordinates);
        y_coord = findViewById(R.id.y_coordinates);

        requestQueue = Volley.newRequestQueue(getApplicationContext());

        cancel.setOnClickListener(v -> {
            Intent intent = new Intent(ChangeLocationActivity.this, MapActivity.class);
            startActivity(intent);
        });

        confirm.setOnClickListener(v -> {
            JsonArrayRequest changeLocationRequest = new JsonArrayRequest(Request.Method.GET, "https://studev.groept.be/api/a20sd108/make_group_meeting/" + x_coord.getText().toString() + "/" + y_coord.getText().toString() + "/" + groupId, null, response -> {
            goToMapActivity();
                }, error -> Toast.makeText(ChangeLocationActivity.this, "Unable to communicate with the server", Toast.LENGTH_LONG).show());
            requestQueue.add(changeLocationRequest);
        });
    }

    private void goToMapActivity(){
        Intent intent = new Intent(ChangeLocationActivity.this, MapActivity.class);
        intent.putExtra("user", user);
        intent.putExtra("groupId", groupId);
        intent.putExtra("groupStatus",groupStatus);
        intent.putExtra("ShowStatusSwitch",false);
        startActivity(intent);
    }
}