package com.example.fleet;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MapActivity extends AppCompatActivity {
    private ImageView groups_button;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_view);
        User user = getIntent().getExtras().getParcelable("user");

        groups_button = findViewById(R.id.groups);
        groups_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MapActivity.this, GroupActivity.class);
                intent.putExtra("user", user);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, R.anim.slide_out_right);
            }
        });
    }


    private void getData(){
//        if(getIntent().hasExtra("images") && getIntent().hasExtra("data1") &&
//                getIntent().hasExtra("data2")){
//
//            data1 = getIntent().getStringExtra("data1");
//            data2 = getIntent().getStringExtra("data2");
//            image = getIntent().getIntExtra("images", 1);
//        }
    }

    private void setData(){
        //set alle gegevens
    }
}
