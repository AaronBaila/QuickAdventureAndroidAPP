package com.example.tfgdam;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Inicio extends AppCompatActivity{
    ImageView btImagenAjustes, btImagenMapa, btImagenPerfilInicio, btImagenSalirInicio;

    TextView tvNoHay;

    FirebaseAuth firebaseAuth;

    DatabaseReference mDatabase, mDatabase2;

    RecyclerView rvPlanesInicio;
    PlanObjAdapter adapter;
    List<PlanObj> listaPlanes;

    ProgressDialog tempDialog;
    CountDownTimer contador;
    int i = 0;

    int nPlanesBD;

    String idUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);

        System.out.println("onCreate");

        btImagenAjustes = findViewById(R.id.btImagenAjustes);
        btImagenMapa = findViewById(R.id.btImagenMapa);
        btImagenPerfilInicio = findViewById(R.id.btImagenPerfilInicio);
        btImagenSalirInicio = findViewById(R.id.btImagenSalirInicio);

        tvNoHay = findViewById(R.id.tvNoHay);

        tempDialog = new ProgressDialog(Inicio.this);
        tempDialog.setMessage("Cargando perfil...");
        tempDialog.setCancelable(false);
        tempDialog.setProgress(i);
        tempDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        tempDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.GRAY));


        listaPlanes = new ArrayList<>();
        rvPlanesInicio = (RecyclerView) findViewById(R.id.rvPlanesInicio);
        rvPlanesInicio.setHasFixedSize(true);
        rvPlanesInicio.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PlanObjAdapter(this, listaPlanes);
        rvPlanesInicio.setAdapter(adapter);

        //mDatabase = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();

        idUser = firebaseAuth.getCurrentUser().getUid();

        mDatabase = FirebaseDatabase.getInstance().getReference("Usuarios").child(idUser);
        mDatabase.addListenerForSingleValueEvent(valueEventListener);

        System.out.println("Antes de ValueEventListener Y tempDialog");

        tempDialog.show();
        contador = new CountDownTimer(5000, 1000){
            public void onTick(long millisUntilFinished){
                tempDialog.setMessage("Cargando perfil...");
            }

            public void onFinish(){
                System.out.println("Numero Planes: " + nPlanesBD);
                mostrarPlanes(nPlanesBD);
                tempDialog.dismiss();
            }
        }.start();


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
                Toast toast = Toast.makeText(getApplicationContext(), "Sesión cerrada", Toast.LENGTH_SHORT);
                Intent i = new Intent(Inicio.this, Login.class);
                startActivity(i);
            }
        });
    }

    private void mostrarPlanes(int nPlanesBD) {
        System.out.println("*** Dentro de Mostrar Planes");
        if (nPlanesBD == 0) {
            //Toast toast = Toast.makeText(getApplicationContext(), "No tienes planes", Toast.LENGTH_SHORT);
            tvNoHay.setText("No has realizado ningún plan por el momento.");
        }
        for (int i = 1; i <= nPlanesBD; i++) {
            String iterador = String.valueOf(i);
            mDatabase2 = FirebaseDatabase.getInstance().getReference("Planes").child(idUser).child(iterador);
            mDatabase2.addListenerForSingleValueEvent(valueEventListener2);
        }
    }

    ValueEventListener valueEventListener2 = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            List<String> valores = new ArrayList<>();
            if (dataSnapshot.exists()) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String valoresAux = snapshot.getValue(String.class);
                    System.out.println("Valor: " + valoresAux);
                    valores.add(valoresAux);
                }
                listaPlanes.add(new PlanObj(valores.get(0), valores.get(1), valores.get(2), valores.get(3), valores.get(4), valores.get(5), valores.get(6), valores.get(7), valores.get(8), valores.get(9)));
                valores.clear();

                /* for(int i = 0; i<10; i++){
                    System.out.println(valores.get(i));
                }
                listaPlanes.add(new PlanObj(valores.get(0), valores.get(1), valores.get(2), valores.get(3), valores.get(4), valores.get(5), valores.get(6), valores.get(7), valores.get(8), valores.get(9)));
                System.out.println(listaPlanes.get(0).getDireccion1()); */
            }
            System.out.println("**** TERMINA VALUEEL2 ***");
            //adapter.notifyDataSetChanged();
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
            System.out.println("OnCancelled");
        }
    };

    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if (dataSnapshot.exists()) {
                ArrayList valores = new ArrayList();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String valor = snapshot.getValue(String.class);
                    System.out.println("Valor: " + valor);
                    valores.add(valor);
                }
                nPlanesBD = Integer.parseInt((String) valores.get(3));
                System.out.println("**** TERMINA VALUEE1 *** nPlanesBD1: " + nPlanesBD);
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
            System.out.println("OnCancelled");
        }
    };

}