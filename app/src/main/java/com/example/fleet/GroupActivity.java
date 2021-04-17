package com.example.fleet;

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

import com.facebook.appevents.suggestedevents.ViewOnClickListener;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Array;
import java.nio.channels.ClosedByInterruptException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

@SuppressWarnings("unchecked")
public class GroupActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ArrayList<Integer> GroupIdList = new ArrayList();

    private String[] s1 = {"test category"};
    private String[] s2 = {"group name"};
    private int[] images = {R.drawable.testperson1};
    private Map<Integer, ArrayList<User>> userMap = new HashMap<>();

    private ImageView add_btn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_view);

        recyclerView = findViewById(R.id.recycler_view);
        ImageView userPicture = findViewById(R.id.user_picture);
        String userId = getIntent().getStringExtra("userId");
        Picasso.get().load("https://graph.facebook.com/"+ userId + "/picture?type=large")
                    .into(userPicture);

        add_btn = findViewById(R.id.add_btn);

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

//        add_btn_card.setVisibility(View.GONE);
//        cancel_button.setVisibility(View.GONE);
//        qr_button.setVisibility(View.GONE);
//        single_button.setVisibility(View.GONE);
//        group_button.setVisibility(View.GONE);
//        plus1.setVisibility(View.GONE);
//        plus2.setVisibility(View.GONE);

        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Log.d("demo ", "add button pressed");
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
                //intent naar qr scanner
            }
        });
        single_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //intent naar temp event creation
            }
        });
        group_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //intent naar group creation view
            }
        });

        RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter(this, s1, s2, images);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

}
