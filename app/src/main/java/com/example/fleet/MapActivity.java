package com.example.fleet;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
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

import java.util.Arrays;

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
    private TextView changeLocation;
    private RequestQueue requestQueue;
    private String groupId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        User user = getIntent().getExtras().getParcelable("user");
        groupId = getIntent().getExtras().getString("groupId");
        requestQueue = Volley.newRequestQueue(getApplicationContext());


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

        map = (MapView) findViewById(R.id.openmapview);
        map.setTileSource(TileSourceFactory.MAPNIK);

        mapStuff();

        Animation animSlideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up);
        Animation animSlideDown = AnimationUtils.loadAnimation(this, R.anim.slide_down);

        changeLocation = findViewById(R.id.tv_changeLocation);
        changeLocation_card = findViewById(R.id.iv_changeLocation);

        rv_small = findViewById(R.id.rv_people);
        rv_card_small = findViewById(R.id.people_card);
        rv_extended = findViewById(R.id.rv_people_extended);
        rv_card_extended = findViewById(R.id.people_card_extended);
        cancel_btn = findViewById(R.id.cancel_btn);
        rv_extended.setVisibility(View.GONE);
        rv_card_extended.setVisibility(View.GONE);
        cancel_btn.setVisibility(View.GONE);

        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

                changeLocation.setVisibility(View.VISIBLE);
                changeLocation_card.setVisibility(View.VISIBLE);
            }
        });


        rv_card_small.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rv_card_extended.setVisibility(View.VISIBLE);
                rv_extended.setVisibility(View.VISIBLE);
                cancel_btn.setVisibility(View.VISIBLE);

//                rv_card_small.clearAnimation();
//                rv_small.clearAnimation();
//                rv_card_small.startAnimation(animSlideDown);
//                rv_small.startAnimation(animSlideDown);

                animSlideUp.reset();
                rv_card_extended.clearAnimation();
                rv_extended.clearAnimation();
                rv_card_extended.startAnimation(animSlideUp);
                rv_extended.startAnimation(animSlideUp);

                rv_card_small.setVisibility(View.GONE);
                rv_small.setVisibility(View.GONE);

                changeLocation.setVisibility(View.GONE);
                changeLocation_card.setVisibility(View.GONE);
            }
        });

        PeopleRecyclerAdapter rva_small = new PeopleRecyclerAdapter(this, images);
        rv_small.setAdapter(rva_small);
        rv_small.setLayoutManager(new GridLayoutManager(this,6));
        PeopleRecyclerAdapter rva_extended = new PeopleRecyclerAdapter(this, images);
        rv_extended.setAdapter(rva_extended);
        rv_extended.setLayoutManager(new GridLayoutManager(this,6));

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

        changeLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(MapActivity.this, LocationActivity.class);
//                intent.putExtra("user", user);
//                startActivity(intent);
            }
        });
    }

    private void mapStuff() {
        IMapController mapController = map.getController();
        mapController.setZoom(20.0);
        GeoPoint startPoint = new GeoPoint(50.880192,4.699782);
        mapController.setCenter(startPoint);

        // dit werkt ni ma mss permissions vragen fzo
        //        this.mLocationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(this),map);
        //        this.mLocationOverlay.enableMyLocation();
        //        map.getOverlays().add(this.mLocationOverlay);

        Marker startMarker = new Marker(map);
        startMarker.setPosition(startPoint);
        startMarker.setIcon(getResources().getDrawable(R.drawable.ic_marker));
        startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        map.getOverlays().add(startMarker);
//        JsonArrayRequest meetingLocation = new JsonArrayRequest(Request.Method.GET,"https://studev.groept.be/api/a20sd108/get_group_meeting/" + groupId, null, new Response.Listener<JSONArray>() {
//            @Override
//            public void onResponse(JSONArray response) {
//                try {
//                    JSONObject groupInfo = response.getJSONObject(1);
//                    IMapController mapController = map.getController();
//                    mapController.setZoom(20.0);
//                    GeoPoint startPoint = new GeoPoint(groupInfo.getLong("x_meeting"), groupInfo.getLong("y_meeting"));
//                    mapController.setCenter(startPoint);
//
//                    // dit werkt ni ma mss permissions vragen fzo
//                    //        this.mLocationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(this),map);
//                    //        this.mLocationOverlay.enableMyLocation();
//                    //        map.getOverlays().add(this.mLocationOverlay);
//
//                    Marker startMarker = new Marker(map);
//                    startMarker.setPosition(startPoint);
//                    startMarker.setIcon(getResources().getDrawable(R.drawable.ic_marker));
//                    startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
//                    map.getOverlays().add(startMarker);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                IMapController mapController = map.getController();
//                mapController.setZoom(20.0);
//                GeoPoint startPoint = new GeoPoint(50, 60);
//                mapController.setCenter(startPoint);
//
//                // dit werkt ni ma mss permissions vragen fzo
//                //        this.mLocationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(this),map);
//                //        this.mLocationOverlay.enableMyLocation();
//                //        map.getOverlays().add(this.mLocationOverlay);
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(MapActivity.this, "Unable to communicate with the server", Toast.LENGTH_LONG).show();
//            }
//        });
//        requestQueue.add(meetingLocation);
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
}
