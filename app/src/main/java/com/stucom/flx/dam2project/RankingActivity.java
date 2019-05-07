package com.stucom.flx.dam2project;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.service.notification.NotificationListenerService;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;
import com.stucom.flx.dam2project.model.API_Response;
import com.stucom.flx.dam2project.model.InfoPlayer;
import com.stucom.flx.dam2project.model.MyVolley;
import com.stucom.flx.dam2project.model.Player;


import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class RankingActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    String URL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences ranking = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String getToken = ranking.getString("token","");
        URL = "https://api.flx.cat/dam2game/ranking?token="+getToken;
        downloadUsers();
    }

    void downloadUsers() {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                String json = response.toString();
                Type typeToken = new TypeToken<API_Response<List<Player>>>() {}.getType();
                Gson gson = new Gson();
                API_Response<List<Player>> apiResponse = gson.fromJson(json, typeToken);
                List<Player> players = apiResponse.getData();
                PlayersAdapter adapter = new PlayersAdapter(players);
                recyclerView.setAdapter(adapter);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
              //  Toast.makeText(NotificationListenerService.Ranking.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        MyVolley.getInstance(this).add(request);
    }

    class PlayerViewHolder extends  RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView imageView;
        TextView textView;
        TextView points;
        int id;


        public PlayerViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageViewPhoto);
            textView = itemView.findViewById(R.id.textViewName);
            points = itemView.findViewById(R.id.textViewPoints);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(RankingActivity.this, InfoPlayer.class);
            intent.putExtra("id", id);
            intent.putExtra("image", imageView.toString());
            intent.putExtra("name", textView.toString());
            //startActivity(intent);
            //startActivity(intent);
            startActivity(intent);
            Toast.makeText(RankingActivity.this, "click", Toast.LENGTH_LONG).show();

        }

    }

    public class PlayersAdapter extends RecyclerView.Adapter<PlayerViewHolder> {
        List<Player> players = new ArrayList<>();

        public PlayersAdapter(List<Player> players) {
            super();
            this.players = players;
        }

        @NonNull
        @Override
        public PlayerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item,parent,false);
            return new PlayerViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull PlayerViewHolder playerViewHolder, int position) {
            Collections.sort(players);
            Player player = players.get(position);
            playerViewHolder.textView.setText(player.getName());
            playerViewHolder.points.setText("Puntos: " + player.getTotalScore());
            Picasso.get().load(player.getImage()).into(playerViewHolder.imageView);
            playerViewHolder.id = player.getId();
        }


        @Override
        public int getItemCount() {
            return players.size();
        }
    }



}


