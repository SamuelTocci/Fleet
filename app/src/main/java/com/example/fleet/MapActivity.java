package com.example.fleet;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MapActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_view);

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
