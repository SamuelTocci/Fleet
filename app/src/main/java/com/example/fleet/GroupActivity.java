package com.example.fleet;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

    private ImageView userPicture = findViewById(R.id.user_picture);
    private final String userId = getIntent().getStringExtra("id");

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_view);

        recyclerView = findViewById(R.id.recycler_view);
        Picasso.get().load("https://graph.facebook.com/"+ userId + "/picture?type=large")
                    .into(userPicture);

        RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter(this, s1, s2, images);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
