package com.example.fleet;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.zxing.Result;

public class QRactivity extends AppCompatActivity {
    private ImageView cancelbtn;
    private CodeScanner mCodeScanner;
    private ImageView qrStart,manual_qr,qr_logo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qr_view);

        cancelbtn = findViewById(R.id.cancel);

        cancelbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QRactivity.this, GroupActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, R.anim.slide_down);
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
                        Toast.makeText(QRactivity.this, result.getText(), Toast.LENGTH_SHORT).show();
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
}
