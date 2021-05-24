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
    private String groupId;
    private TextView group_id;
    private ImageView cancel_activity;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qrgenerator_view);

        groupId = getIntent().getExtras().getString("groupId");

        group_id = findViewById(R.id.group_id_display);
        cancel_activity = findViewById(R.id.cancel_qr_gen);

        group_id.setText(groupId);

        cancel_activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QRgeneratorActivity.this, GroupActivity.class);
                startActivity(intent);
            }
        });
    }
}
