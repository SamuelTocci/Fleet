package com.example.fleet;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class GroupCreateActivity extends AppCompatActivity {
    private EditText groupNameInput,groupDescriptionInput;
    private ImageView confirmBtn,cancelBtn;
    private User user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_create_view);

        user = getIntent().getExtras().getParcelable("user");

        groupNameInput = findViewById(R.id.groupNameInput);
        //groupNameInput.getText().toString();
        groupDescriptionInput = findViewById(R.id.groupDescriptionInput);

        confirmBtn = findViewById(R.id.confirmButton);
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //intent en gegevens in databank zetten
            }
        });
        cancelBtn = findViewById(R.id.cancelBtn);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GroupCreateActivity.this, GroupActivity.class);
                intent.putExtra("user", user);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_down, R.anim.slide_up);
            }
        });
    }
}
