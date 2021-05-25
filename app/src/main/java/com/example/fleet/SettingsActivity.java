package com.example.fleet;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {
    private ImageView cancelBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_view);

        cancelBtn = findViewById(R.id.cancel_settings);
        cancelBtn.setOnClickListener(v -> goToGroupActivity());
    }

    public void goToGroupActivity() {
        Intent intent = new Intent(SettingsActivity.this, GroupActivity.class);
        startActivity(intent);
    }
}
