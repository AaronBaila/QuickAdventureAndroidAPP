package com.example.tfgdam;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class Inicio extends AppCompatActivity{
    //CardView btMapa, btMisPlanes, btCerrarSesion, btConfig;

    ImageView btImagenAjustes, btImagenMapa, btImagenPerfilInicio, btImagenSalirInicio;

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);

        btImagenAjustes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //AUN NO IMPLEMENTADO
            }
        });

        btImagenMapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Inicio.this, Mapa.class);
                startActivity(i);
            }
        });

        btImagenPerfilInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //AUN NO IMPLEMENTADO
            }
        });

        btImagenSalirInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.getInstance().signOut();
                Toast toast=Toast.makeText(getApplicationContext(),"Sesión cerrada",Toast.LENGTH_SHORT);
                Intent i = new Intent(Inicio.this, Login.class);
                startActivity(i);
            }
        });

        /* btMapa =(CardView) findViewById(R.id.btMapa);
        btMisPlanes =(CardView) findViewById(R.id.btMisPlanes);
        btCerrarSesion =(CardView) findViewById(R.id.btCerrarSesion);
        btConfig =(CardView) findViewById(R.id.btConfig);

        if (nPlanesBD == 0) {
            Toast toast = Toast.makeText(getApplicationContext(), "No tienes planes", Toast.LENGTH_SHORT);
        }
        for (int i = 1; i <= nPlanesBD; i++) {
            String iterador = String.valueOf(i);
            mDatabase2 = FirebaseDatabase.getInstance().getReference("Planes").child(idUser).child(iterador);
            mDatabase2.addListenerForSingleValueEvent(valueEventListener2);*/
    }
}