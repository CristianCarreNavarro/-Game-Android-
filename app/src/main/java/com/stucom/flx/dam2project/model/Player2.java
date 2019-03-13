package com.stucom.flx.dam2project.model;

import android.content.Context;
import android.content.SharedPreferences;

public class Player2 {
    private String name;
    private String email;
    private String avatar;
    private String token;
    private static Player2 player2;



    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getAvatar() { return avatar; }
    public String getToken() { return token; }

    public void setName(String name) { this.name = name; }
    public void setEmail(String email) { this.email = email; }
    public void setAvatar(String avatar) { this.avatar = avatar; }
    public void setToken(String token) { this.token = token; }


    //-----------------------SINGELTON------------------------------------

    //constructor Privado
    private Player2(Context context) {
        loadFromPrefs(context);
    }
    //constructor que te devuelve un player2 si no existiera y si no te devuelve el que ya existe
    public static Player2 returnPlayer(Context context){
        if(player2 == null){
            player2 = new Player2(context);
        }
        return player2;
    }

//Metodo que te carga los datos del Player2
    public void loadFromPrefs(Context context) {

        SharedPreferences prefs = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        this.name = prefs.getString("playerName", null);
        this.email = prefs.getString("playerEmail", null);
        this.avatar = prefs.getString("playerAvatar", null);
        this.token = prefs.getString("playerToken", null);
    }

//Metodo que te guarda los datos del Player2
    public void saveToPrefs(Context context) {

        SharedPreferences prefs = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = prefs.edit();
        prefsEditor.putString("playerName", name);
        prefsEditor.putString("playerEmail", email);
        prefsEditor.putString("playerAvatar", avatar);
        prefsEditor.putString("playerToken", token);
        prefsEditor.commit();
    }
}
