package com.example.fleet;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.RecyclerViewHolder>{

    String[] data1;
    String[] data2;
    int[] images;
    Context context;
    Activity activity;
    User user;
    private RequestQueue requestQueue;

    public RecyclerViewAdapter(Context ct, String[] s1, String[] s2, int[] img, User user){ //[[1,2,3],[5,1,8]]
        context = ct;
        data1 = s1;
        data2 = s2;
        images = img;
        this.user = user;
    }
    //groupname, description, [userid]
    public void httpRequest(String url) {
        StringRequest submitRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        requestQueue.add(submitRequest);
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_row, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        holder.description.setText(data1[position]);
        holder.groupName.setText(data2[position]);
        holder.picture.setImageResource(images[position]);

        holder.recycler_row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity activity = (Activity) context;
                Intent intent = new Intent(context, MapActivity.class);
                intent.putExtra("user", user);
                context.startActivity(intent);
                activity.overridePendingTransition(R.anim.slide_in_right, android.R.anim.fade_out);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(data1 == null){return 0;}
        return data1.length;
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder{

        TextView description,groupName;
        ImageView picture;
        ConstraintLayout recycler_row;

        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            description = itemView.findViewById(R.id.group_description);
            groupName = itemView.findViewById(R.id.group_name);
            picture = itemView.findViewById(R.id.profile_pic);
            recycler_row = itemView.findViewById(R.id.recycler_row);
        }
    }
}
