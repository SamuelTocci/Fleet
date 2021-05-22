package com.example.fleet;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.zxing.Result;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class QRactivity extends AppCompatActivity {
    private ImageView cancelbtn;
    private CodeScanner mCodeScanner;
    private ImageView qrStart,manual_qr,qr_logo;
    private RequestQueue requestQueue;
    private User user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qr_view);

        requestQueue = Volley.newRequestQueue(getApplicationContext());
        user = getIntent().getExtras().getParcelable("user");

        cancelbtn = findViewById(R.id.cancel);

        cancelbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToGroupActivity();
            }
        });

        CodeScannerView scannerView = findViewById(R.id.scanner_view);
        scannerView.setVisibility(View.GONE);

        manual_qr = findViewById(R.id.manual_input);
        qr_logo = findViewById(R.id.qr_logo);

        qrStart = findViewById(R.id.qr_start);
        qrStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scannerView.setVisibility(View.VISIBLE);
                qrStart.setVisibility(View.GONE);
                qr_logo.setVisibility(View.GONE);
                manual_qr.setVisibility(View.GONE);
            }
        });

        mCodeScanner = new CodeScanner(this, scannerView);
        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        JsonArrayRequest groupInfoRequest = new JsonArrayRequest(Request.Method.GET,"https://studev.groept.be/api/a20sd108/link_group_to_user/" + result +"/" + user.getId(), null, new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                goToGroupActivity();
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(QRactivity.this, "Unable to communicate with the server", Toast.LENGTH_LONG).show();
                            }
                        });
                        requestQueue.add(groupInfoRequest);
                    }
                });
            }
        });
        scannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCodeScanner.startPreview();
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        mCodeScanner.startPreview();
    }

    @Override
    protected void onPause() {
        mCodeScanner.releaseResources();
        super.onPause();
    }

    public void goToGroupActivity() {
        Intent intent = new Intent(QRactivity.this, GroupActivity.class);
        intent.putExtra("user", this.user);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_down, R.anim.slide_up);
    }
}
