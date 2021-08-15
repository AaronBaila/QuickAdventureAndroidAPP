package com.example.tfgdam;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Inicio extends AppCompatActivity{
    CardView btMapa, btMisPlanes, btCerrarSesion, btConfig;

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);

        btMapa =(CardView) findViewById(R.id.btMapa);
        btMisPlanes =(CardView) findViewById(R.id.btMisPlanes);
        btCerrarSesion =(CardView) findViewById(R.id.btCerrarSesion);
        btConfig =(CardView) findViewById(R.id.btConfig);

        btMapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Inicio.this, Mapa.class);
                startActivity(i);
            }
        });

        btMisPlanes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Inicio.this, MisPlanes.class);
                startActivity(i);
            }
        });

        btCerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.getInstance().signOut();
                Toast toast=Toast.makeText(getApplicationContext(),"Sesión cerrada",Toast.LENGTH_SHORT);
                Intent i = new Intent(Inicio.this, Login.class);
                startActivity(i);
            }
        });

        btConfig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //AUN NO IMPLEMENTADO
            }
        });
    }
}