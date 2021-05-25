package com.example.fleet;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

@SuppressWarnings("unchecked")
public class GroupActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ArrayList<Integer> GroupIdList = new ArrayList();

    private Bundle groupBundle;

    private SearchView search_bar;
    private ImageView add_btn;
    private ImageView settings;
    private RequestQueue requestQueue;
    private User user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_view);

        ConstraintLayout constraintLayout = findViewById(R.id.GroupLayout);
        AnimationDrawable animationDrawable = (AnimationDrawable) constraintLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();

        recyclerView = findViewById(R.id.recycler_view);
        ImageView userPicture = findViewById(R.id.user_picture);
        user = getIntent().getExtras().getParcelable("user");
        requestQueue = Volley.newRequestQueue(getApplicationContext());

        userPicture.setImageBitmap(user.getPfPic());

        add_btn = findViewById(R.id.add_btn);
        settings = findViewById(R.id.settings);
        search_bar = findViewById(R.id.search_bar);

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

        add_btn.setOnClickListener(v -> {
                Arrays.stream(addCard)
                        .forEach(e -> e.setVisibility(View.VISIBLE));
                animSlideUp.reset();
                Arrays.stream(addCard).forEach(View::clearAnimation);
                Arrays.stream(addCard).forEach(e -> e.startAnimation(animSlideUp));
                add_btn.clearAnimation();
                add_btn.startAnimation(animSlideDown);
                add_btn.setVisibility(View.GONE);
        });
        settings.setOnClickListener(v -> {
            Intent intent = new Intent(GroupActivity.this, SettingsActivity.class);
            intent.putExtra("user", user);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_left, android.R.anim.fade_out);
        });

        cancel_button.setOnClickListener(v -> {
            Arrays.stream(addCard)
                    .forEach(e -> e.setVisibility(View.GONE));
            add_btn.setVisibility(View.VISIBLE);
            animSlideUp.reset();
            Arrays.stream(addCard).forEach(e -> e.clearAnimation());
            Arrays.stream(addCard).forEach(e -> e.startAnimation(animSlideDown));
            add_btn.clearAnimation();
            add_btn.startAnimation(animSlideUp);

        });
        qr_button.setOnClickListener(v -> {
            Intent intent = new Intent(GroupActivity.this, QRactivity.class);
            intent.putExtra("user", user);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
        });
        group_button.setOnClickListener(v -> {
            Intent intent = new Intent(GroupActivity.this, GroupCreateActivity.class);
            intent.putExtra("user", user);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
        });

        single_button.setOnClickListener(v -> {
            //TODO intent naar temp event creation
        });

        search_bar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //    adapter.getFilter().filter(newText);
                recycler(search_bar.getQuery().toString());
                return false;
            }
        });
        informRecycler();
    }

    private void informRecycler() {
        user.resetBundle();
        JsonArrayRequest groupInfoRequest = new JsonArrayRequest(Request.Method.GET, "https://studev.groept.be/api/a20sd108/get_all_group_info/" + user.getId(), null, response -> {
            for (int i = 0; i < response.length(); i++) {
                JSONObject groupInfo;
                try {
                    groupInfo = response.getJSONObject(i);
                    Group group = new Group(groupInfo.getString("groupID"), groupInfo.getString("name"), groupInfo.getString("description"), groupInfo.getInt("userCount"));
                    if(groupInfo.getInt("status") == 1){
                        group.setActive(1);
                    }
                    else{
                        group.setActive(0);
                    }
                    this.user.addGroupToBundle(group);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            groupBundle = user.getGroupsBundle();
            recycler("");
        }, error -> Toast.makeText(GroupActivity.this, "Unable to communicate with the server", Toast.LENGTH_LONG).show());
        requestQueue.add(groupInfoRequest);
    }

    private void recycler(String search) {
        RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter(this,search, user);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}