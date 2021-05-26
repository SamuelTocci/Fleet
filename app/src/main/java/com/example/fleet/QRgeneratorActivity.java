package com.example.fleet;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class QRgeneratorActivity extends AppCompatActivity {
    private User user;
    private String groupId;
    private int groupStatus;
    private TextView group_id;
    private ImageView cancel_activity;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qrgenerator_view);

        user = getIntent().getExtras().getParcelable("user");
        groupId = getIntent().getExtras().getString("groupId");
        groupStatus = getIntent().getExtras().getInt("groupStatus");

        group_id = findViewById(R.id.group_id_display);
        cancel_activity = findViewById(R.id.cancel_qr_gen);

        group_id.setText(groupId);

        cancel_activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QRgeneratorActivity.this, MapActivity.class);
                intent.putExtra("user", user);
                intent.putExtra("groupId", groupId);
                intent.putExtra("groupStatus",groupStatus);
                intent.putExtra("ShowStatusSwitch",false);
                startActivity(intent);
            }
        });
    }
}
