package com.example.fleet;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.appevents.suggestedevents.ViewOnClickListener;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Array;
import java.nio.channels.ClosedByInterruptException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unchecked")
public class GroupActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ArrayList<Integer> GroupIdList = new ArrayList();

    private String[] s1 = {"test category"};
    private String[] s2 = {"group name"};
    private int[] images = {R.drawable.testperson1};
    private Map<Integer, ArrayList<User>> userMap = new HashMap<>();

    private ImageView userPicture;
    private String userId;
    private ImageView add_btn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_view);

        recyclerView = findViewById(R.id.recycler_view);
        userPicture = findViewById(R.id.user_picture);
        userId = getIntent().getStringExtra("id");
        Picasso.get().load("https://graph.facebook.com/"+ userId + "/picture?type=large")
                    .into(userPicture);

        add_btn = findViewById(R.id.add_btn);
        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Log.d("demo ", "add button pressed");
            }
        });

        RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter(this, s1, s2, images);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

}
