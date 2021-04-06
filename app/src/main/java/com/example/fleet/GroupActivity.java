package com.example.fleet;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.nio.channels.ClosedByInterruptException;
import java.util.ArrayList;

@SuppressWarnings("unchecked")
public class GroupActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ArrayList<Integer> GroupIdList = new ArrayList();

    private String s1[], s2[];
    private int images[];

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_view);

        recyclerView = findViewById(R.id.recycler_view);

        RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter(this, s1, s2, images);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
