package com.example.fleet;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {
    private ImageView cancelBtn;
    private User user;
    private String groupId;
    private int groupStatus;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_view);

        user = getIntent().getExtras().getParcelable("user");
        groupId = getIntent().getExtras().getString("groupId");
        groupStatus = getIntent().getExtras().getInt("groupStatus");

        cancelBtn = findViewById(R.id.cancel_settings);
        cancelBtn.setOnClickListener(v -> goToGroupActivity());
    }

    public void goToGroupActivity() {
        Intent intent = new Intent(SettingsActivity.this, GroupActivity.class);
        intent.putExtra("user", user);
        intent.putExtra("groupId", groupId);
        intent.putExtra("groupStatus",groupStatus);
        intent.putExtra("ShowStatusSwitch",false);
        startActivity(intent);
    }
}
