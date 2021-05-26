package com.example.fleet;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceFragmentCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

public class MapActivity extends AppCompatActivity{
    private ImageView groups_button;
    MapView map = null;
    private MyLocationNewOverlay mLocationOverlay;
    //private Drawable marker = getResources().getDrawable( R.drawable.ic_marker);
    private RecyclerView rv_small,rv_extended;
    private ImageView rv_card_small, rv_card_extended;
    private int[] images = {R.drawable.testperson1,R.drawable.testperson1,R.drawable.testperson1,R.drawable.testperson1,R.drawable.testperson1,R.drawable.testperson1,R.drawable.testperson1,R.drawable.testperson1,R.drawable.testperson1,R.drawable.testperson1,R.drawable.testperson1};
    private ImageView cancel_btn;
    private ImageView changeLocation_card;
    private ImageView card;
    private ImageView cancel_group_btn, exit_group_btn;
    private ImageView qr_btn;
    private TextView changeLocation;
    private Switch changeStatus;
    private RequestQueue requestQueue;
    private String groupId;
    private ImageView leave_btn;
    private int groupStatus;
    private ArrayList<String> userIds;
    private ArrayList<String> userStatuses;
    private User user;
    private String userToSwitch;

    private ImageView status_switch_prompt, present_btn, otw_btn, coming_btn,not_btn, reset_status_btn, cancel_status_btn;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user = getIntent().getExtras().getParcelable("user");
        groupId = getIntent().getExtras().getString("groupId");
        groupStatus = getIntent().getExtras().getInt("groupStatus");
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        Boolean promptStatusSwitch = getIntent().getExtras().getBoolean("ShowStatusSwitch");

        fillUserStatuses();

        //handle permissions first, before map is created. not depicted here

        //load/initialize the osmdroid configuration, this can be done
        Context ctx = getApplicationContext();
        //TODO depricated shit veranderen hier
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));
        //setting this before the layout is inflated is a good idea
        //it 'should' ensure that the map has a writable location for the map cache, even without permissions
        //if no tiles are displayed, you can try overriding the cache path using Configuration.getInstance().setCachePath
        //see also StorageUtils
        //note, the load method also sets the HTTP User Agent to your application's package name, abusing osm's tile servers will get you banned based on this string

        setContentView(R.layout.event_view);

        map = findViewById(R.id.openmapview);
        map.setTileSource(TileSourceFactory.MAPNIK);

        mapRequest();

        Animation animSlideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up);
        Animation animSlideDown = AnimationUtils.loadAnimation(this, R.anim.slide_down);

        changeLocation = findViewById(R.id.tv_changeLocation);
        changeLocation_card = findViewById(R.id.iv_changeLocation);

        rv_small = findViewById(R.id.rv_people);
        rv_card_small = findViewById(R.id.people_card);
        rv_extended = findViewById(R.id.rv_people_extended);
        rv_card_extended = findViewById(R.id.people_card_extended);
        cancel_btn = findViewById(R.id.cancel_btn);
        leave_btn = findViewById(R.id.leave_group);
        card = findViewById(R.id.groupleavecard);
        cancel_group_btn = findViewById(R.id.cancel_leave);
        exit_group_btn = findViewById(R.id.confirm_leave);
        qr_btn = findViewById(R.id.qr_gen);
        changeStatus = findViewById(R.id.status_switch);
        rv_extended.setVisibility(View.GONE);
        rv_card_extended.setVisibility(View.GONE);
        cancel_btn.setVisibility(View.GONE);
        leave_btn.setVisibility(View.GONE);
        card.setVisibility(View.GONE);
        cancel_group_btn.setVisibility(View.GONE);
        exit_group_btn.setVisibility(View.GONE);
        qr_btn.setVisibility(View.GONE);

        status_switch_prompt = findViewById(R.id.status_switch_prompt);
        present_btn = findViewById(R.id.present_btn);
        otw_btn = findViewById(R.id.otw_btn);
        coming_btn = findViewById(R.id.coming_btn);
        not_btn = findViewById(R.id.not_btn);
        reset_status_btn = findViewById(R.id.reset_status_btn);
        cancel_status_btn = findViewById(R.id.cancel_status_btn);

        hideStatusSwitchPrompt();

        if(promptStatusSwitch){
            status_switch_prompt.setVisibility(View.VISIBLE);
            present_btn.setVisibility(View.VISIBLE);
            otw_btn.setVisibility(View.VISIBLE);
            coming_btn.setVisibility(View.VISIBLE);
            not_btn.setVisibility(View.VISIBLE);
            reset_status_btn.setVisibility(View.VISIBLE);
            cancel_status_btn.setVisibility(View.VISIBLE);
            userToSwitch = getIntent().getExtras().getString("switchStatusForThisId");
        }

        cancel_status_btn.setOnClickListener(v -> hideStatusSwitchPrompt());

        reset_status_btn.setOnClickListener(v -> {
            changeStatus("NA");
            hideStatusSwitchPrompt();
        });

        present_btn.setOnClickListener(v -> {
            changeStatus("present");
            hideStatusSwitchPrompt();
        });

        otw_btn.setOnClickListener(v -> {
            changeStatus("omw");
            hideStatusSwitchPrompt();
        });

        coming_btn.setOnClickListener(v -> {
            changeStatus("coming");
            hideStatusSwitchPrompt();
        });

        not_btn.setOnClickListener(v -> {
            changeStatus("not coming");
            hideStatusSwitchPrompt();

        });

        cancel_btn.setOnClickListener(v -> {
            rv_card_small.setVisibility(View.VISIBLE);
            rv_small.setVisibility(View.VISIBLE);

            animSlideUp.reset();
            rv_card_extended.clearAnimation();
            rv_extended.clearAnimation();
            rv_card_extended.startAnimation(animSlideDown);
            rv_extended.startAnimation(animSlideDown);

            rv_card_extended.setVisibility(View.GONE);
            rv_extended.setVisibility(View.GONE);
            cancel_btn.setVisibility(View.GONE);
            leave_btn.setVisibility(View.GONE);
            qr_btn.setVisibility(View.GONE);

            changeLocation.setVisibility(View.VISIBLE);
            changeLocation_card.setVisibility(View.VISIBLE);
            changeStatus.setVisibility(View.VISIBLE);
        });


        rv_card_small.setOnClickListener(v -> {
            rv_card_extended.setVisibility(View.VISIBLE);
            rv_extended.setVisibility(View.VISIBLE);
            cancel_btn.setVisibility(View.VISIBLE);
            leave_btn.setVisibility(View.VISIBLE);
            qr_btn.setVisibility(View.VISIBLE);

            animSlideUp.reset();
            rv_card_extended.clearAnimation();
            rv_extended.clearAnimation();
            rv_card_extended.startAnimation(animSlideUp);
            rv_extended.startAnimation(animSlideUp);

            rv_card_small.setVisibility(View.GONE);
            rv_small.setVisibility(View.GONE);

            changeLocation.setVisibility(View.GONE);
            changeLocation_card.setVisibility(View.GONE);
            changeStatus.setVisibility(View.GONE);
        });

        leave_btn.setOnClickListener(v -> {
            card.setVisibility(View.VISIBLE);
            cancel_group_btn.setVisibility(View.VISIBLE);
            exit_group_btn.setVisibility(View.VISIBLE);
        });

        cancel_group_btn.setOnClickListener(v -> {
            card.setVisibility(View.GONE);
            cancel_group_btn.setVisibility(View.GONE);
            exit_group_btn.setVisibility(View.GONE);
        });

        exit_group_btn.setOnClickListener(v -> {
//
            JsonArrayRequest leaveGroupRequest = new JsonArrayRequest(Request.Method.GET, "https://studev.groept.be/api/a20sd108/leave_group/" + groupId + "/" + user.getId() + "/" + groupId, null, response -> {
//                    TODO fixen dat een groep leaven, de groep weg doet na de laatste persoon (best in database zelf)
//                    JSONObject usercount;
//                    for (int i = 0; i < response.length(); i++) {
//                        try {
//                            usercount = response.getJSONObject(i);
//                            if (true) {
//                                destroyGroup();
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }

            }, error -> {
                Toast.makeText(MapActivity.this, "Unable to communicate with the server", Toast.LENGTH_LONG).show();
            });
            requestQueue.add(leaveGroupRequest);

            card.setVisibility(View.GONE);
            cancel_group_btn.setVisibility(View.GONE);
            exit_group_btn.setVisibility(View.GONE);

            goToGroupActivity(user);
        });

        groups_button = findViewById(R.id.groups);
        groups_button.setOnClickListener(v -> goToGroupActivity(user));

        changeLocation_card.setOnClickListener(v -> {
                Intent intent = new Intent(MapActivity.this, ChangeLocationActivity.class);
                intent.putExtra("user", user);
                intent.putExtra("groupId", groupId);
                intent.putExtra("groupStatus",groupStatus);
                intent.putExtra("ShowStatusSwitch",false);
                startActivity(intent);
        });

        qr_btn.setOnClickListener(v -> {
            Intent intent = new Intent(MapActivity.this, QRgeneratorActivity.class);
            intent.putExtra("user", user);
            intent.putExtra("groupId", groupId);
            intent.putExtra("groupStatus",groupStatus);
            intent.putExtra("ShowStatusSwitch",false);
            startActivity(intent);
        });


        Switch onOffSwitch = findViewById(R.id.status_switch);
        boolean state;
        state = groupStatus == 1;
        onOffSwitch.setChecked(state);
        onOffSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            int state1;
            if(isChecked){
                state1 = 1;
            }
            else{
                changeAllStatuses("NA");
                state1 = 0;
            }
                JsonArrayRequest statusChangeRequest = new JsonArrayRequest(Request.Method.GET, "https://studev.groept.be/api/a20sd108/change_group_status/" + String.valueOf(state1) + "/" + groupId, null, response -> {
                }, error -> {
                    Toast.makeText(MapActivity.this, "Unable to communicate with the server", Toast.LENGTH_LONG).show();
                });
                requestQueue.add(statusChangeRequest);
            });
    }

    private void hideStatusSwitchPrompt() {
        status_switch_prompt.setVisibility(View.GONE);
        present_btn.setVisibility(View.GONE);
        otw_btn.setVisibility(View.GONE);
        coming_btn.setVisibility(View.GONE);
        not_btn.setVisibility(View.GONE);
        reset_status_btn.setVisibility(View.GONE);
        cancel_status_btn.setVisibility(View.GONE);
    }

    private void changeAllStatuses(String status){
            JsonArrayRequest changeStatusRequest = new JsonArrayRequest(Request.Method.GET, "https://studev.groept.be/api/a20sd108/change_all_status/" + status + "/" + groupId, null, response -> fillUserStatuses(), error -> Toast.makeText(MapActivity.this, "Unable to communicate with the server", Toast.LENGTH_LONG).show());
            requestQueue.add(changeStatusRequest);
    }

    private void changeStatus(String status){
        JsonArrayRequest changeStatusRequest = new JsonArrayRequest(Request.Method.GET, "https://studev.groept.be/api/a20sd108/change_status_in_group/" + status+ "/" + groupId + "/" + userToSwitch, null, response -> fillUserStatuses(), error -> Toast.makeText(MapActivity.this, "Unable to communicate with the server", Toast.LENGTH_LONG).show());
        requestQueue.add(changeStatusRequest);
    }

    private void goToGroupActivity(User user) {
        Intent intent = new Intent(MapActivity.this, GroupActivity.class);
        intent.putExtra("user", user);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, R.anim.slide_out_right);
    }

    private void mapRequest() {
        JsonArrayRequest meetingLocationRequest = new JsonArrayRequest(Request.Method.GET, "https://studev.groept.be/api/a20sd108/get_group_meeting/" + groupId, null, response -> {
            JSONObject meetingInfo;
            try {
                meetingInfo = response.getJSONObject(0);
                IMapController mapController = map.getController();
                mapController.setZoom(18.0);
                GeoPoint startPoint  = new GeoPoint(meetingInfo.getDouble("x_meeting"),meetingInfo.getDouble("y_meeting"));
                mapController.setCenter(startPoint);

                Marker startMarker = new Marker(map);
                startMarker.setPosition(startPoint);
                startMarker.setIcon(getResources().getDrawable(R.drawable.ic_marker));
                startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
                map.getOverlays().add(startMarker);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> Toast.makeText(MapActivity.this, "Unable to communicate with the server", Toast.LENGTH_LONG).show());
        requestQueue.add(meetingLocationRequest);
    }

    public void onResume(){
        super.onResume();
        //this will refresh the osmdroid configuration on resuming.
        //if you make changes to the configuration, use
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this));
        map.onResume(); //needed for compass, my location overlays, v6.0.0 and up
    }

    public void onPause(){
        super.onPause();
        //this will refresh the osmdroid configuration on resuming.
        //if you make changes to the configuration, use
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //Configuration.getInstance().save(this, prefs);
        map.onPause();  //needed for compass, my location overlays, v6.0.0 and up
    }

    public void fillUserStatuses() {
        userIds = new ArrayList<>();
        userStatuses = new ArrayList<>();
        JsonArrayRequest userStatusInfoRequest = new JsonArrayRequest(Request.Method.GET, "https://studev.groept.be/api/a20sd108/get_all_users_from_group/" + groupId, null, response -> {
            for (int i = 0; i < response.length(); i++) {
                JSONObject userInfo;
                try {
                    userInfo = response.getJSONObject(i);
                    userIds.add(userInfo.getString("userID"));
                    userStatuses.add(userInfo.get("status").toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            peopleRecycler();
        }, error -> Toast.makeText(MapActivity.this, "Unable to communicate with the server", Toast.LENGTH_LONG).show());
        requestQueue.add(userStatusInfoRequest);
    }

    private void peopleRecycler() {
        PeopleRecyclerAdapter rva_small = new PeopleRecyclerAdapter(this, images, userIds, userStatuses,user,groupId,groupStatus);
        rv_small.setAdapter(rva_small);
        rv_small.setLayoutManager(new GridLayoutManager(this,6));
        PeopleRecyclerAdapter rva_extended = new PeopleRecyclerAdapter(this, images, userIds, userStatuses,user,groupId,groupStatus);
        rv_extended.setAdapter(rva_extended);
        rv_extended.setLayoutManager(new GridLayoutManager(this,6));
    }
}
