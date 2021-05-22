package com.example.fleet;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

@SuppressWarnings("unchecked")
public class GroupActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ArrayList<Integer> GroupIdList = new ArrayList();

    //TODO data van sql in datastructuur zetten
//    private String[] s1 = {"test category"};
//    private String[] s2 = {"group name"};
//    private int[] images = {R.drawable.testperson1};
    private Bundle groupBundle;

    private ImageView add_btn;
    private ImageView settings;
    private RequestQueue requestQueue;
    private User user;
    private ArrayList<String> groupNameList = new ArrayList<>();
    private ArrayList<String> groupDescriptionList = new ArrayList<>();
    private ArrayList<String> groupIdList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_view);

        recyclerView = findViewById(R.id.recycler_view);
        ImageView userPicture = findViewById(R.id.user_picture);
        user = getIntent().getExtras().getParcelable("user");
        groupBundle = user.getGroupsBundle();

        try {
            Bitmap mBitmap = user.getFacebookProfilePicture();
            userPicture.setImageBitmap(mBitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }

        add_btn = findViewById(R.id.add_btn);
        settings = findViewById(R.id.settings);

        // dit kan mss makkelijker geschreven worden
        ImageView add_btn_card = findViewById(R.id.addbtn_card);
        ImageView cancel_button = findViewById(R.id.cancel_button);
        ImageView qr_button = findViewById(R.id.qr_button);
        ImageView single_button = findViewById(R.id.single_button);
        ImageView group_button = findViewById(R.id.group_button);
        ImageView plus1 = findViewById(R.id.plus1);
        ImageView plus2 = findViewById(R.id.plus2);

        ImageView[] addCard = {cancel_button, qr_button, single_button, group_button, plus1, plus2, add_btn_card};

        Arrays.stream(addCard)
                .forEach(e -> e.setVisibility(View.GONE));

        Animation animSlideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up);
        Animation animSlideDown = AnimationUtils.loadAnimation(this, R.anim.slide_down);

        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Arrays.stream(addCard)
                            .forEach(e -> e.setVisibility(View.VISIBLE));
                    animSlideUp.reset();
                    Arrays.stream(addCard).forEach(e -> e.clearAnimation());
                    Arrays.stream(addCard).forEach(e -> e.startAnimation(animSlideUp));
                    add_btn.clearAnimation();
                    add_btn.startAnimation(animSlideDown);
                    add_btn.setVisibility(View.GONE);
            }
        });
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GroupActivity.this, SettingsActivity.class);
                intent.putExtra("user", user);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, android.R.anim.fade_out);
            }
        });

        cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Arrays.stream(addCard)
                        .forEach(e -> e.setVisibility(View.GONE));
                add_btn.setVisibility(View.VISIBLE);
                animSlideUp.reset();
                Arrays.stream(addCard).forEach(e -> e.clearAnimation());
                Arrays.stream(addCard).forEach(e -> e.startAnimation(animSlideDown));
                add_btn.clearAnimation();
                add_btn.startAnimation(animSlideUp);

            }
        });
        qr_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GroupActivity.this, QRactivity.class);
                intent.putExtra("user", user);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
            }
        });
        group_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GroupActivity.this, GroupCreateActivity.class);
                intent.putExtra("user", user);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
            }
        });

        single_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO intent naar temp event creation
            }
        });

//
        for (String key : groupBundle.keySet()){
            Group group = groupBundle.getParcelable(key);
            groupNameList.add(group.getName().toString());
            groupDescriptionList.add(group.getDescription().toString());
            groupIdList.add(group.getId().toString());
        }

//        for (Iterator<String> it = groupBundle.keySet().iterator(); it.hasNext(); ){
//            String key = it.next();
//            Group group = groupBundle.getParcelable(key);
//            groupNameList.add(group.getName());
//            groupDescriptionList.add(group.getDescription());
//        }

        RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter(this, groupNameList, groupDescriptionList, groupIdList, user);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

}
