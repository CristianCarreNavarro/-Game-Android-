package com.stucom.flx.dam2project;


import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.stucom.flx.dam2project.model.MyVolley;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import static android.content.Intent.ACTION_OPEN_DOCUMENT;


public class SettingsActivity extends AppCompatActivity {

    //Declaramos la variables globales
    TextView nombreText;
    TextView mail;
    Uri imagenUrl;
    ImageView foto;
    ImageView imageViewFoto;
    ImageView btnCam;
    private int PICK_IMAGE = 1;
    private static final int REQUEST_IMAGE_CAPTURE = 2;
    File fotoParaGuardar;
    Button btnSave;
    String encodedImage;


    //en el onCreate buscamos el valor de los inputs y lo asignamos a las variables
    //buscamos a traves del id cata elemento
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mail = findViewById(R.id.inputMail);
        final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        //stringEmail = settings.getString("email", "");
        mail.setText(settings.getString("email",""));
        foto = findViewById(R.id.btnFoto);

        //
        foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirGaleria();
            }

        });
        //
        btnCam = findViewById(R.id.btnCam);
        btnCam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirCamera();
            }

        });

        nombreText = findViewById(R.id.inputUsername);
        imageViewFoto = findViewById(R.id.imageViewFoto);
        btnSave = findViewById(R.id.btnSave);
        //creamos el directorio donde vamos a guardar la imagen de la camara
        File dir = new File(Environment.getExternalStorageDirectory(), "deltaForce");


        //si no existe la volvemos a crear
        if (!dir.exists()) {
            dir.mkdir();
        }
        //Es la ruta con el nombre de la foto que vamos a guardar
        fotoParaGuardar = new File(Environment.getExternalStorageDirectory() + "/" + "deltaForce/" + "foto.jpg");

        //FUNCION PARA GUARDAR EL NICKNAME Y LA IMAGEN EL SERVIDOR  ****************************************
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nombreText.getText().toString();
                String URL = "https://api.flx.cat/dam2game/user";
                StringRequest request = new StringRequest(Request.Method.PUT, URL,
                        new Response.Listener<String>() {
                            @Override public void onResponse(String response) {
                                Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
                                startActivity(intent);
                                Toast.makeText(SettingsActivity.this, nombreText.getText().toString(), Toast.LENGTH_LONG).show();

                            }
                        }, new Response.ErrorListener() {
                    @Override public void onErrorResponse(VolleyError error) {

                    }
                }) {
                    //Esta es la primera peticion cuando solo le pasamos el correo
                    //LE PASAMOS EL EMAIL PARA OBTENER EL CODIGO DE VERIFICACION
                    @Override protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<>();
                        params.put("token", settings.getString("token",""));
                        params.put("name", nombreText.getText().toString());
                        params.put("image", encodedImage);
                        return params;
                    }
                };
                MyVolley.getInstance(SettingsActivity.this).add(request);

            }

        });
        //************************************************
    }







    //Abrimos la galeria creando un nuevo Intent para la galeria
    //a traves de la ruta de la galeria
    public void abrirGaleria() {
        Intent gallery = new Intent(ACTION_OPEN_DOCUMENT);
        gallery.setType("image/*");
        startActivityForResult(gallery, PICK_IMAGE);

    }

    //Abrimos la camara creando un Intent para la camara
    public void abrirCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        imagenUrl = Uri.EMPTY;
        fotoParaGuardar = new File(Environment.getExternalStorageDirectory() + "/" + "unknown/" + ".png");;
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {

            //Establecemos la URl a traves de la variables 'fotoParaGuardar'
            imagenUrl = FileProvider.getUriForFile(this, getPackageName() + ".fileprovider", fotoParaGuardar);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imagenUrl);
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);

        }
    }


    //Utilizamos la siguente funcion para definir como se tiene que recuperar la informacion del Activity
    // ..y que es lo que tiene que hacer en cada caso
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (resultCode == RESULT_OK && requestCode == PICK_IMAGE) {
                imagenUrl = data.getData();
                imageViewFoto.setImageURI(imagenUrl);
                //*************************************
                //Convertimos la uri de la imagen a un bitmap
                final InputStream imageStream;
                imageStream = getContentResolver().openInputStream(imagenUrl);
                final Bitmap bitmapImage = BitmapFactory.decodeStream(imageStream);
                encodedImage = encodeImage(bitmapImage);
                guardar();
            }
            if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
                guardar();
                encodedImage = encodeImage((Bitmap)data.getExtras().get("photo.jpg"));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public String encodeImage(Bitmap image) {
        Bitmap immagex = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immagex.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);
        return imageEncoded;
    }



    //la siguente funcion es para guardar la foto en el shared preferences
    public void guardar() {
        if (imagenUrl != null) {
            SharedPreferences prefs = getSharedPreferences(getPackageName(), MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("imageViewFoto", imagenUrl.toString());
            editor.apply();
        }
    }

    //en el onPause guardamos el valor para que despues volver a escribirlo en el onResume
    // en el putString ponemos una clave y seguido por el valor
    @Override
    public void onPause() {
        //Aqui guardamos la foto URI
        SharedPreferences prefs = getSharedPreferences(getPackageName(), MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("inputUsername", nombreText.getText().toString());
        editor.putString("inputMail", mail.getText().toString());
        editor.apply();
        super.onPause();
    }

    //EN EL ON RESUME o al girar la pantalla, se guardan los datos y se vuelven a escribir
    @Override
    public void onResume() {
        //SACAMOS LA FOTO DEL URI
        super.onResume();
        SharedPreferences prefs = getSharedPreferences(getPackageName(), MODE_PRIVATE);
        nombreText.setText(prefs.getString("inputUsername", "USERNAME"));
        //mail.setText(prefs.getString("inputMail", ""));
        String rutaImagen = prefs.getString("imageViewFoto", "");
        imageViewFoto.setImageURI(Uri.parse(rutaImagen));


    }




}
