package com.stucom.flx.dam2project;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.stucom.flx.dam2project.model.API_Response;
import com.stucom.flx.dam2project.model.MyVolley;
import com.stucom.flx.dam2project.model.Player2;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    EditText textEmail;
    EditText textVerificationCode;
    Button buttonEnviar;
    String email;
    String codeVerify;
    boolean sendedcodeVerify;
    final static String URL = "https://api.flx.cat/dam2game";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        if(Player2.returnPlayer(RegisterActivity.this).getToken() != null ){
            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
            startActivity(intent);
            finish();//para que no se pueda volver a atras desde esta activity
        }


        textEmail = findViewById(R.id.EmailToSend);
        textVerificationCode = findViewById(R.id.verificationCode);
        buttonEnviar = findViewById(R.id.buttonEnviar);
        sendedcodeVerify = false;
        textVerificationCode.setVisibility(View.GONE);

        buttonEnviar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if(sendedcodeVerify==false){
                    email = textEmail.getText().toString();
                    Log.d("cris", "onClick:"+email);
                    RegistrarUsuario(email);
                    sendedcodeVerify=true;
                    textEmail.setVisibility(View.INVISIBLE);
                    textVerificationCode.setVisibility(View.VISIBLE);
                }else{
                    codeVerify = textVerificationCode.getText().toString();
                    RegistrarUsuarioConVerification(email,codeVerify);
            }}
        });
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    public void RegistrarUsuario(final String email) {
    StringRequest request = new StringRequest(Request.Method.POST, URL + "/register",
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    Log.d("chris", response);

                    //Sirve para serializar el codigo y convertirlo en texto
                    Gson gson = new Gson();

                    //Type token para saber que tipo te devuelve la request
                    Type typeToken = new TypeToken<API_Response<Integer>>() {}.getType();

                    //Esto es la respuesta<Int>
                    API_Response<Integer> apiResponse = gson.fromJson(response, typeToken);

                    //De la respuesta solo queremos el data
                    int id = apiResponse.getData();

                }
            }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            String message = error.toString();
            NetworkResponse response = error.networkResponse;

            if (response != null) {
                message = response.statusCode + " " + message;
            }
            Log.d("chris", message);
        }
    }
    ) {
        @Override
        public Map<String, String> getParams() {
            HashMap<String, String> params = new HashMap<>();
            params.put("email", email);
            return params;
        }
    };
    MyVolley.getInstance(this).add(request);
}

    public void RegistrarUsuarioConVerification(final String email, final String codeVerify) {
        StringRequest request = new StringRequest(Request.Method.POST, URL + "/register",
                new Response.Listener<String>() {
                    @Override public void onResponse(String response) {
                        Log.d("chris", response);

                        //Sirve para serializar el codigo y convertirlo en texto
                        Gson gson = new Gson();

                        //Type token para saber que tipo te devuelve la request
                        Type typeToken = new TypeToken<API_Response<String>>() {}.getType();

                        //Esto es la respuesta<String>
                        API_Response<String> apiResponse = gson.fromJson(response, typeToken);

                        //De la respuesta solo queremos el token
                        String token= apiResponse.getData();

                        //modificamos al Player2
                        Player2.returnPlayer(RegisterActivity.this).setEmail(email);
                        Player2.returnPlayer(RegisterActivity.this).setToken(token);

                        //guardamos los nuevos datos
                        Player2.returnPlayer(RegisterActivity.this).saveToPrefs(RegisterActivity.this);

                        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String message = error.toString();
                NetworkResponse response = error.networkResponse;

                if (response != null) {
                    message = response.statusCode + " " + message;
                }                Log.d("chris", message);
            }
        }
        ) {
            @Override
            public Map<String, String> getParams() {
                HashMap<String, String> params = new HashMap<>();
                params.put("email",email );
                params.put("verify",codeVerify );
                return params;
            }
        };
        MyVolley.getInstance(this).add(request);
    }



}
