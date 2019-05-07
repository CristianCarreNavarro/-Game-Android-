package com.stucom.flx.dam2project.model;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
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
import com.stucom.flx.dam2project.R;

import org.json.JSONObject;

import java.lang.reflect.Type;

public class InfoPlayer extends AppCompatActivity {

    ImageView imagePlayer;
    TextView usernamePlayer;
    EditText messagePlayer;
    Button btnSendMessage;
    String URL;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_player);

        int idUser = getIntent().getExtras().getInt("id");
        SharedPreferences ranking = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String token = ranking.getString("token","");
        URL = "https://api.flx.cat/dam2game/user/"+idUser+"?token="+token;

        loadInfoUser();
        btnSendMessage = findViewById(R.id.btnSendMessage);

    }
    void loadInfoUser() {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                String json = response.toString();
                Type typeToken = new TypeToken<API_Response<Player>>() {}.getType();
                Gson gson = new Gson();
                API_Response<Player> apiResponse = gson.fromJson(json, typeToken);
                imagePlayer = findViewById(R.id.imagePlayer);
                usernamePlayer = findViewById(R.id.usernamePlayer);
                messagePlayer = findViewById(R.id.messagePlayer);
                usernamePlayer.setText(apiResponse.getData().getName());
                Picasso.get().load(apiResponse.getData().getImage()).into(imagePlayer);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(InfoPlayer.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        MyVolley.getInstance(this).add(request);
    }


}
