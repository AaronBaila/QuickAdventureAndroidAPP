package com.example.tfgdam;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class Registro extends AppCompatActivity {

    //Creamos variables para posteriormente refenciar los id's o instanciar los objetos

    EditText etEmail, etNickName, etPass, etPass2;
    TextView tvErrorPass, tvAlreadyCuenta;
    Button btRegistro;
    String nPlanes = "0";

    ProgressDialog barraCarga;

    AwesomeValidation awesomeValidation;
    DatabaseReference mDatabase;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ig_registro);

        //Referenciamos id's

        etEmail = findViewById(R.id.etEmail);
        etNickName = findViewById(R.id.etNickName);
        etPass = findViewById(R.id.etPass);
        etPass = findViewById(R.id.etPass2);
        tvErrorPass = findViewById(R.id.tvErrorPass);
        tvAlreadyCuenta = findViewById(R.id.tvAlreadyCuenta);
        btRegistro = findViewById(R.id.btRegistro);

        //Instanciamos objetos

        //Objeto para gestionar base de datos
        mDatabase = FirebaseDatabase.getInstance().getReference();
        //Objeto para gestionar usuarios
        firebaseAuth = FirebaseAuth.getInstance();
        //Objeto barra de carga
        barraCarga = new ProgressDialog(Registro.this);

        //Validamos campos de correo y contraseña
        validarCampos();

        //Botón para volver a login
        tvAlreadyCuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Registro.this, Login.class);
                startActivity(i);
            }
        });

        //Boton para realizar el proceso de registro con los datos recogidos.
        btRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Iniciamos los parametros de la barra de carga y la mostramos
                barraCarga.setTitle("Registro");
                barraCarga.setMessage("Creando Usuario");
                barraCarga.setCanceledOnTouchOutside(false);
                barraCarga.show();

                //Obtenemos los datos de los campos correspondientes
                String mail = etEmail.getText().toString();
                String nombre = etNickName.getText().toString();
                String pass = etPass.getText().toString();
                String pass2 = etPass2.getText().toString();

                //Comprobamos que la contraseña se ha escrito correctamente
                if(pass.equalsIgnoreCase(pass2)){
                    //Comprobamos que todos los campos estan validados y si es asi procedemos a crear el usuario.
                    if(awesomeValidation.validate()) {
                        //Si las validaciones son correctas se crea el usuario y se almacena en BD
                        crearUsuario(mail , pass, nombre);
                    }else{
                        //Error en las validaciones
                        System.out.println("Error3");
                        Toast.makeText(Registro.this, "Completa todos los datos.", Toast.LENGTH_SHORT).show();
                        barraCarga.dismiss();
                    }
                }else{
                    //Si las contraseñas no coinciden (No esta bien escrita)
                    barraCarga.dismiss();
                    tvErrorPass.setText("Error. Contraseñas deben coincidir !");
                }
            }
        });
    }

    //Metodo para validar los campos de correo y contraseña
    public void validarCampos(){
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        awesomeValidation.addValidation(this, R.id.etEmail, Patterns.EMAIL_ADDRESS, R.string.invalid_mail);
        awesomeValidation.addValidation(this, R.id.etPass, ".{6,}", R.string.invalid_pass);
    }

    //Metodo para crear usuario y almacenarlo en BD
    public void crearUsuario(String mail, String pass, String nombre){
        //Creamos usuario mediante el metodo de FirebaseAuth
        firebaseAuth.createUserWithEmailAndPassword(mail, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                //Si la creacion de usuario es realizada con exito se almacena en BD
                if (task.isSuccessful()) {

                    //Obtenemos el id del usuario recien creado
                    String idUser = firebaseAuth.getCurrentUser().getUid();

                    //Creamos hashMap(estructura de datos) para almacenar los datos posteriormente en la BD
                    Map<String, Object> map = new HashMap<>();
                    map.put("nPlanes" , nPlanes);
                    map.put("idUser" , idUser);
                    map.put("Nombre" , nombre);
                    map.put("Mail" , mail);

                    //Almacenamos usuario en BD con el metodo de DatabaseReference
                    mDatabase.child("Usuarios").child(idUser).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            ////Si el almacenamiento es realizado con exito se cierra la barra de carga y se vuelve al activity Login
                            if (task.isSuccessful()) {
                                System.out.println("Exito");
                                barraCarga.dismiss();
                                Intent i = new Intent(Registro.this, Login.class);
                                startActivity(i);
                            } else {
                                System.out.println("Error1");
                                barraCarga.dismiss();
                            }
                        }
                    });
                } else {
                    //Si la creacion de usuario NO es realizada con exito
                    System.out.println("Error2");
                    String errorCode = ((FirebaseAuthException) task.getException()).getErrorCode();
                    System.out.println(errorCode);
                    Toast.makeText(Registro.this, errorCode, Toast.LENGTH_SHORT).show();
                    barraCarga.dismiss();
                }
            }
        });
    }
}