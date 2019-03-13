package com.stucom.flx.dam2project;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;
import com.stucom.flx.dam2project.model.API_Response;
import com.stucom.flx.dam2project.model.MyVolley;
import com.stucom.flx.dam2project.model.Player;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;




public class RankingActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                downloadPlayers();
            }
        });
    }
    @Override
    public void onResume() {
        super.onResume();
        downloadPlayers();
    }

    final static String URL = "https://api.flx.cat/dam2game";

    public void downloadPlayers() {
        JsonObjectRequest request = new JsonObjectRequest(
            Request.Method.GET, URL,null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                String json = response.toString();
                Gson gson = new Gson();
                Type typeToken = new TypeToken<API_Response<List<Player>>>() {}.getType();
                API_Response<List<Player>> apiResponse = gson.fromJson(json, typeToken);
                List<Player> players = apiResponse.getData();
                PlayersAdapter adapter = new PlayersAdapter(players);
                recyclerView.setAdapter(adapter);

                String message = "Downloaded " + players.size() + " players\n";
                for (Player player : players) {
                    message += player.getName() + ":" + player.getImage() + "\n";
                }

                swipeRefreshLayout.setRefreshing(false);
            }
        },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    String message = error.toString();
                    NetworkResponse response = error.networkResponse;
                    if (response != null) {
                        message = response.statusCode + " " + message;
                    }

                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        );
        MyVolley.getInstance(this).add(request);
    }


    class PlayerViewHolder extends RecyclerView.ViewHolder {

        TextView textNameCell;
        TextView textScoreCell;
        ImageView imageViewCell;

        PlayerViewHolder(@NonNull View itemView) {
            super(itemView);
            textNameCell = itemView.findViewById(R.id.textNameCell);
            imageViewCell = itemView.findViewById(R.id.imageViewCell);
            textScoreCell = itemView.findViewById(R.id.textScoreCell);
        }
    }


    class PlayersAdapter extends RecyclerView.Adapter<PlayerViewHolder> {

        private List<Player> players;

        PlayersAdapter(List<Player> players) {
            super();
            this.players = players;
        }

        @NonNull @Override
        public PlayerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
            View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.activity_cell, parent, false);
            return new PlayerViewHolder(view);
        }
        @Override
        public void onBindViewHolder(@NonNull PlayerViewHolder viewHolder, int position) {
            Player player = players.get(position);
            viewHolder.textNameCell.setText(player.getName());
            String points = player.getTotalScore();
            viewHolder.textScoreCell.setText(points);
            Picasso.get().load(player.getImage()).into(viewHolder.imageViewCell);
        }


        @Override
        public int getItemCount() {
            return players.size();
        }
    }






}
