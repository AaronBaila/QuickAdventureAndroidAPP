package com.example.tfgdam;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class acDetalle extends AppCompatActivity {
    Button btAceptar, btRegresar;
    TextView tvTipolugar1, tvLugar1, tvTipolugar2, tvLugar2, tvTipolugar3, tvLugar3;

    int nPlanesBD;

    DatabaseReference mDatabaseSet, mDatabaseGet;
    FirebaseAuth firebaseAuth;

    List<PlanObj> listaDetalle;

    RecyclerView recyclerView;
    PlanObjAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ac_detalle);

        mDatabaseSet = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();

        btAceptar = findViewById(R.id.btAceptar);
        btRegresar = findViewById(R.id.btRegresar);

        listaDetalle = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PlanObjAdapter(this, listaDetalle);
        recyclerView.setAdapter(adapter);

        adapter = new PlanObjAdapter(this, listaDetalle);
        recyclerView.setAdapter(adapter);

        String tipoLugar1 = getIntent().getExtras().getString("tipoLugar1");
        String lugar1 = getIntent().getExtras().getString("lugar1");
        String direccion1 = getIntent().getExtras().getString("direccion1");

        String tipoLugar2 = getIntent().getExtras().getString("tipoLugar2");
        String lugar2 = getIntent().getExtras().getString("lugar2");
        String direccion2 = getIntent().getExtras().getString("direccion2");

        String tipoLugar3 = getIntent().getExtras().getString("tipoLugar3");
        String lugar3 = getIntent().getExtras().getString("lugar3");
        String direccion3 = getIntent().getExtras().getString("direccion3");

        PlanObj plan = new  PlanObj(direccion1, direccion2, direccion3, lugar1, lugar2, lugar3, tipoLugar1, tipoLugar2, tipoLugar3, "1");
        listaDetalle.add(plan);
        adapter.notifyDataSetChanged();

        String idUser = firebaseAuth.getCurrentUser().getUid();

        mDatabaseGet = FirebaseDatabase.getInstance().getReference("Usuarios").child(idUser);
        mDatabaseGet.addListenerForSingleValueEvent(valueEventListener);

        btAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                System.out.println("nPlanes1: " + nPlanesBD);
                int nPlanesFinales = nPlanesBD + 1;
                System.out.println("n: " + nPlanesFinales);
                String nPlanesFinalesStr = String.valueOf(nPlanesFinales);
                System.out.println("nPlanesFinales: " + nPlanesFinalesStr);

                HashMap hashMap = new HashMap();

                hashMap.put("nPlanes", nPlanesFinalesStr);

                mDatabaseGet.updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        System.out.println("EXITOOO !!");
                    }
                });

                Map<String, Object> map = new HashMap<>();
                map.put("nPlan", nPlanesFinalesStr);
                map.put("tipoLugar1" , tipoLugar1);
                map.put("lugar1" , lugar1);
                map.put("direccion1" , direccion1);
                map.put("tipoLugar2" , tipoLugar2);
                map.put("lugar2" , lugar2);
                map.put("direccion2" , direccion2);
                map.put("tipoLugar3" , tipoLugar3);
                map.put("lugar3" , lugar3);
                map.put("direccion3" , direccion3);

                mDatabaseSet.child("Planes").child(idUser).child(nPlanesFinalesStr).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            System.out.println("Exito");
                            Intent i = new Intent(acDetalle.this, Mapa.class);
                            startActivity(i);
                        } else {
                            System.out.println("Error1");
                        }
                    }
                });
            }
        });

       btRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(acDetalle.this, Mapa.class);
                startActivity(intent);
            }
        });
    }

    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if(dataSnapshot.exists()){
                ArrayList valores = new ArrayList();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    String valor = snapshot.getValue(String.class);
                    valores.add(valor);

                }
                System.out.println(valores);
                nPlanesBD = Integer.parseInt((String) valores.get(3));
                System.out.println(nPlanesBD);
            }
        }
        @Override
        public void onCancelled(@NonNull DatabaseError error) {
            System.out.println("OnCancelled");
        }
    };
}