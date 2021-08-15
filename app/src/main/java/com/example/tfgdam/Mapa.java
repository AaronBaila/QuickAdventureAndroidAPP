package com.example.tfgdam;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Mapa extends AppCompatActivity implements LocationListener, OnMapReadyCallback {
    //Inicializamos variables
    Button btBuscar, btDetalle, btVolverMapa;
    SupportMapFragment supportMapFragment;
    private GoogleMap map;
    boolean isPermissionGaranted = false;
    private FusedLocationProviderClient fusedLocationProviderClient;
    double currentLat = 0, currentLong = 0;
    List<String> lugaresFinales = new ArrayList<String>();
    List<String> direccionesFinales = new ArrayList<String>();
    //Variable Numero al Azar para seleccionar de manera aleatoria un tipo de lugar
    int numAlAzar = -1;
    List<Integer> numerosAlAzar = new ArrayList<Integer>();
    //Incializamos Array de tipo place type (Array de lugares)
    String tipoLugarLista[] = {"restaurant", "bar", "cafe", "movie_theater"};
    //Inicializamos Array de nombre de los lugares
    String nombreTipoLista[] = {"Restaurante", "Bar", "Cafe", "Cine"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa);

        //Asignamos variables
        btBuscar = findViewById(R.id.btBuscar);
        btDetalle = findViewById(R.id.btDetalle);
        btVolverMapa = findViewById(R.id.btVolverMapa);

        //Garantizamos permisos
        comprobarPermisos();

        //Inicicalizamos fused provider cliente y obtenemos la localizacion actual
        fusedLocationProviderClient = new FusedLocationProviderClient(this);
        localizacionActual();

        btBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < 3; i++) {
                    System.out.println("**** Dentro del Boton ****");
                    //Generamos el numero al Azar para posteriormente seleccionar un tipo de lugar aleatorio en la url.
                    int numAlAzar1 = (int) Math.floor(Math.random() * 4);
                    //Comprobamos que numAlAzar no se repita para no repetir tipo de lugar.
                    if (numAlAzar1 == numAlAzar) {
                        while (numAlAzar1 == numAlAzar) {
                            numAlAzar1 = (int) Math.floor(Math.random() * 4);
                        }
                    }
                    //Genearmos la URL
                    numAlAzar = numAlAzar1;
                    numerosAlAzar.add(numAlAzar);
                    String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json" + //url
                            "?location=" + currentLat + "," + currentLong + //Localizacion
                            "&radius=5000" + //Radio de busqueda
                            "&types=" + tipoLugarLista[numAlAzar] + //Tipo de lugar
                            "&sensor=true" + // sensor
                            "&key=" + getResources().getString(R.string.google_map_key); //Clave de Api
                    //Ejecutamos el metodo task para descargar los datos en Json
                    System.out.println("******* Antes de PlaceTask *********");
                    new PlaceTask().execute(url);
                }

                btBuscar.setText("Volver a Buscar");
                btDetalle.setVisibility(View.VISIBLE);

                /*lugaresFinales.clear();
                direccionesFinales.clear();*/
            }
        });

        btDetalle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tipoLugar1 = nombreTipoLista[0];
                System.out.println("tipoLugar1");
                String lugar1 = lugaresFinales.get(0);
                System.out.println(lugar1);
                String direccion1 = direccionesFinales.get(0);
                System.out.println(direccion1);

                String tipoLugar2 = nombreTipoLista[1];
                System.out.println(tipoLugar2);
                String lugar2 = lugaresFinales.get(1);
                System.out.println(lugar2);
                String direccion2 = direccionesFinales.get(1);
                System.out.println(direccion2);

                String tipoLugar3 = nombreTipoLista[2];
                System.out.println(tipoLugar3);
                String lugar3 = lugaresFinales.get(2);
                System.out.println(lugar3);
                String direccion3 = direccionesFinales.get(2);
                System.out.println(direccion3);

                Intent intent = new Intent(Mapa.this, acDetalle.class);

                        intent.putExtra("tipoLugar1", tipoLugar1);
                        intent.putExtra("lugar1", lugar1);
                        intent.putExtra("direccion1", direccion1);

                        intent.putExtra("tipoLugar2", tipoLugar2);
                        intent.putExtra("lugar2", lugar2);
                        intent.putExtra("direccion2", direccion2);

                        intent.putExtra("tipoLugar3", tipoLugar3);
                        intent.putExtra("lugar3", lugar3);
                        intent.putExtra("direccion3", direccion3);

                startActivity(intent);
            }
        });

        btVolverMapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Mapa.this, Inicio.class);
                startActivity(i);
            }
        });

    }

    //Metodo para obtener la localizacion actual mediante fusedLocationProviderClient
    @SuppressLint("MissingPermission")
    private void localizacionActual() {
        System.out.println("**** Obtenemos Localizacion Actual ****");
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                Location location = task.getResult();
                currentLat = location.getLatitude();
                currentLong = location.getLongitude();
                iniciarMapa();
            }
        });
    }

    //Metodod para iniciar el mapa dentro del fragmento
    private void iniciarMapa() {
        System.out.println("**** Iniciamos Mapa ****");
        if(isPermissionGaranted){
            supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.google_map);
            supportMapFragment.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {
                    //Cuando el mapa esta listo
                    map = googleMap;
                    LatLng miLocalizacionActual = new LatLng(currentLat, currentLong);
                    CameraUpdate actualizarCamara = CameraUpdateFactory.newLatLngZoom(miLocalizacionActual, 18);
                    map.moveCamera(actualizarCamara);
                    map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                    googleMap.addMarker(new MarkerOptions()
                            .position(miLocalizacionActual)
                            .title("Localización Actual"));
                }
            });
        }
    }

    //Metodo para comprobar permisoso y si no garantizarlos manualmente (Utilizamos libreria Dexter)
    private void comprobarPermisos() {
        Dexter.withContext(this).withPermission(Manifest.permission.ACCESS_FINE_LOCATION).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                Toast toast=Toast.makeText(getApplicationContext(),"Permisos Garantizados",Toast.LENGTH_SHORT);
                isPermissionGaranted = true;
                iniciarMapa();
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), "");
            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                permissionToken.continuePermissionRequest();
            }
        }).check();
    }


    @Override
    public void onLocationChanged(@NonNull Location location) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
    }

    private class PlaceTask  extends AsyncTask<String, Integer, String>{
        @Override
        protected String doInBackground(String... strings) {
            System.out.println(" **** 1. Dentro de PlaceTask ****");
            String data = null;
            try {
                //Descargamos datos de consulta
                data = downloadUrl(strings[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return data;
        }

        @Override
        protected void onPostExecute(String s) {
            //Execute parser task
            new ParserTask().execute(s);
        }
    }

    private String downloadUrl(String string) throws  IOException{
        System.out.println(" **** 2. Dentro de downloadUrl ****");
        //Inicializamos url
        URL url = new URL(string);
        //Inicializamos conexion
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        //Conectamos
        connection.connect();
        //Inicializamos Input Stream
        InputStream stream = connection.getInputStream();
        //Inicializar buffered reader
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        //Inicicalizamos String builder
        StringBuilder builder = new StringBuilder();
        //Inicializamos string variable
        String line = "";
        //Use while loop
        while((line = reader.readLine()) != null){
            //Append Line
            builder.append(line);
        }
        //Get append data
        String data = builder.toString();
        System.out.println("*** Data de download: " + data);
        //close reader
        reader.close();
        //return data
        return data;
    }

    private class ParserTask extends AsyncTask<String, Integer, List<HashMap<String,String>>>{
        @Override
        protected List<HashMap<String, String>> doInBackground(String... strings) {
            System.out.println("**** 3. Dentro de ParserTask ****");
            System.out.println(strings[0]);
            //Creamos la clase Json parser
            JsonParser jsonParser = new JsonParser();
            // Inicializamos la lista de HashMap
            List<HashMap<String,String>> mapList = null;
            JSONObject object = null;
            try {
                //Inicializamos objeto Json
                object = new JSONObject(strings[0]);
                //Parse json object
                System.out.println("parseResult: " + jsonParser.parseResult(object));
                mapList = jsonParser.parseResult(object);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //Return MapList
            return mapList;
        }

        @Override
        protected void onPostExecute(List<HashMap<String, String>> hashMaps) {
            System.out.println("**** 4. Pintamos mapa ****");
            //Limpiamos Mapa
            map.clear();
            //Usamos bucle For
            List<String> listaNombres = new ArrayList<String>();
            List<String> listaDirecciones = new ArrayList<String>();
            for(int i = 0; i<hashMaps.size(); i++){
                HashMap<String,String> hashMapList = hashMaps.get(i);
                listaNombres.add(hashMapList.get("name"));
                listaDirecciones.add(hashMapList.get("direccion"));
                String name = hashMapList.get("name");
                //Get latitude
                double lat = Double.parseDouble(hashMapList.get("lat"));
                //Get longitude
                double lng = Double.parseDouble(hashMapList.get("lng"));
                //Concatenamos latitud y longirtud
                LatLng latLng = new LatLng(lat, lng);
                //Pintamos las marcas
                MarkerOptions options = new MarkerOptions();
                //Set position
                options.position(latLng);
                //Set title
                options.title(name);
                //Añadimos marcador a el mapa
                map.addMarker(options);
            }
            int numAlAzar2 = (int) Math.floor(Math.random()*listaNombres.size());
            lugaresFinales.add(listaNombres.get(numAlAzar2));
            direccionesFinales.add(listaDirecciones.get(numAlAzar2));

            /*System.out.println("************ NOMBRES *********");
            for(int j = 0; j<lugaresFinales.size(); j++){
                int i = numerosAlAzar.get(j);
                System.out.println(nombreLugarLista[i] + ": " +  lugaresFinales.get(j) + " Direccion: " + direccionesFinales.get(j));
            }*/
        }
    }
}