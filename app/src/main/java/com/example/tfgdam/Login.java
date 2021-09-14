package com.example.tfgdam;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    //Creamos variables para posteriormente refenciar los id's o instanciar los objetos

    Button btRegistroLogin, btLogin;
    EditText etEmailLogin, etPassLogin;
    TextView tvOlvidarPass;

    FirebaseAuth firebaseAuth;

    ProgressDialog barraCarga;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Referenciamos id's

        etEmailLogin = findViewById(R.id.etEmailLogin);
        etPassLogin = findViewById(R.id.etPassLogin);
        btRegistroLogin = findViewById(R.id.btRegistroLogin);
        btLogin = findViewById(R.id.btLogin);
        tvOlvidarPass = findViewById(R.id.tvOlvidarPass);

        //Instanciamos objetos

        //Objeto para gestionar usuarios
        firebaseAuth = FirebaseAuth.getInstance();
        //Objeto barra de carga
        barraCarga = new ProgressDialog(Login.this);

        //Botón para acceder activity registro
        btRegistroLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Login.this, Registro.class);
                startActivity(i);
            }
        });

        //Boton acceder / comprueba si los datos son correctos(si el usuario esta registrado en la BD) y si es asi inicia sesion
        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Iniciamos los parametros de la barra de carga y la mostramos
                barraCarga.setTitle("Log In");
                barraCarga.setMessage("Buscando Usuario");
                barraCarga.setCanceledOnTouchOutside(false);
                barraCarga.show();

                //Obtenemos los datos de los campos correspondientes
                String mail = etEmailLogin.getText().toString();
                String pass = etPassLogin.getText().toString();

                iniciarSesion(mail, pass);
            }
        });
    }

    private void iniciarSesion(String mail, String pass) {
        // Usamos metodo para iniciar sesion de FirebaseAuth
        firebaseAuth.signInWithEmailAndPassword(mail, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                //Si se inicia sesión correctamente, cierra barra de carga y pasa a activity Inicio
                if(task.isSuccessful()){
                    barraCarga.dismiss();
                    Toast.makeText(Login.this, "Sesión Iniciada", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(Login.this, Inicio.class);
                    startActivity(i);
                }else{
                    barraCarga.dismiss();
                    System.out.println("Error");
                    Toast.makeText(Login.this, "Email o Contraseña Incorrectos", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}