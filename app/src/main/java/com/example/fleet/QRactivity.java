package com.example.fleet;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class QRactivity extends AppCompatActivity {
    private ImageView cancelbtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qr_view);

        cancelbtn = findViewById(R.id.cancel);

        cancelbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QRactivity.this, GroupActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, R.anim.slide_down);
            }
        });
    }
}
