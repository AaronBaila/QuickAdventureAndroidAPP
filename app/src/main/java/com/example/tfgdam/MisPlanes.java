package com.example.tfgdam;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MisPlanes extends AppCompatActivity {
    Button btMostrar, btVolverPlanes;
    DatabaseReference mDatabase, mDatabase2;
    FirebaseAuth firebaseAuth;

    RecyclerView recyclerView;
    PlanObjAdapter adapter;


    List<PlanObj> listaPlanes;

    int nPlanesBD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_planes);

        btMostrar  = findViewById(R.id.btMostrar);
        btVolverPlanes  = findViewById(R.id.btVolverPlanes);

        listaPlanes = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PlanObjAdapter(this, listaPlanes);
        recyclerView.setAdapter(adapter);

        //mDatabase = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();

        String idUser = firebaseAuth.getCurrentUser().getUid();

        mDatabase = FirebaseDatabase.getInstance().getReference("Usuarios").child(idUser);
        mDatabase.addListenerForSingleValueEvent(valueEventListener);

        btMostrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(nPlanesBD == 0){
                    Toast toast=Toast.makeText(getApplicationContext(),"No tienes planes",Toast.LENGTH_SHORT);
                }
                for(int i = 1; i <= nPlanesBD; i ++){
                    String iterador = String.valueOf(i);
                    mDatabase2 = FirebaseDatabase.getInstance().getReference("Planes").child(idUser).child(iterador);
                    mDatabase2.addListenerForSingleValueEvent(valueEventListener2);
                }
            }
        });

        btVolverPlanes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MisPlanes.this, Inicio.class);
                startActivity(i);
            }
        });

        adapter = new PlanObjAdapter(this, listaPlanes);
        recyclerView.setAdapter(adapter);
    }

    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if (dataSnapshot.exists()) {
                ArrayList valores = new ArrayList();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String valor = snapshot.getValue(String.class);
                    valores.add(valor);
                }
                nPlanesBD = Integer.parseInt((String) valores.get(3));
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
            System.out.println("OnCancelled");
        }
    };


    ValueEventListener valueEventListener2 = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            List<String> valores = new ArrayList<>();

            if(dataSnapshot.exists()){
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    String valoresAux = snapshot.getValue(String.class);
                    System.out.println("Valor: " + valoresAux);
                    valores.add(valoresAux);
                }
                listaPlanes.add(new PlanObj(valores.get(0), valores.get(1), valores.get(2), valores.get(3), valores.get(4), valores.get(5), valores.get(6), valores.get(7), valores.get(8), valores.get(9)));
                valores.clear();

                /*for(int i = 0; i<10; i++){
                    System.out.println(valores.get(i));
                }*/
                /*listaPlanes.add(new PlanObj(valores.get(0), valores.get(1), valores.get(2), valores.get(3), valores.get(4), valores.get(5), valores.get(6), valores.get(7), valores.get(8), valores.get(9)));
                System.out.println(listaPlanes.get(0).getDireccion1());*/
            }
            adapter.notifyDataSetChanged();
        }
        @Override
        public void onCancelled(@NonNull DatabaseError error) {
            System.out.println("OnCancelled");
        }
    };

    /*ValueEventListener valueEventListener2 = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if(dataSnapshot.exists()){
                ArrayList valores = new ArrayList();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    String valor = snapshot.getValue(String.class);
                    valores.add(valor);
                }
                System.out.println("*** Dentro de valuelistener2 ***");
                System.out.println(valores);
                nPlanesBD = Integer.parseInt((String) valores.get(3));
                System.out.println(nPlanesBD);
                nPlanesFinales = nPlanesBD + 1;
                nPlanesFinalesStr = String.valueOf(nPlanesFinales);
            }
        }
        @Override
        public void onCancelled(@NonNull DatabaseError error) {
            System.out.println("OnCancelled");
        }
    };*/
}