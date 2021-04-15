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

    private ImageView userPicture;
    private String userId;
    private ImageView add_btn;
    private ImageView add_btn_card;
    private ImageView cancel_button, qr_button,single_button,group_button;
    private ImageView plus1,plus2;

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

        add_btn_card = findViewById(R.id.addbtn_card);
        cancel_button = findViewById(R.id.cancel_button);
        qr_button = findViewById(R.id.qr_button);
        single_button = findViewById(R.id.single_button);
        group_button = findViewById(R.id.group_button);
        plus1 = findViewById(R.id.plus1);
        plus2 = findViewById(R.id.plus2);

        ImageView[] addCard = {cancel_button,qr_button,single_button,group_button,plus1,plus2,add_btn_card};

        Arrays.stream(addCard)
                .forEach(e -> e.setVisibility(View.GONE));

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
                    add_btn.setVisibility(View.GONE);
                    Arrays.stream(addCard)
                            .forEach(e -> e.setVisibility(View.VISIBLE));
            }
        });

        cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Arrays.stream(addCard)
                        .forEach(e -> e.setVisibility(View.GONE));
                add_btn.setVisibility(View.VISIBLE);
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
