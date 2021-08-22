package com.example.tfgdam;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import com.google.firebase.auth.FirebaseAuth;

public class RecuperarPass extends AppCompatActivity {

    EditText etEmail;
    TextView tvInfo;
    Button btRecuperar, btLogin;

    AwesomeValidation awesomeValidation;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperar_pass);

        etEmail = findViewById(R.id.etEmail);
        tvInfo = findViewById(R.id.tvInfo);
        btRecuperar = findViewById(R.id.btRecuperar);
        btLogin = findViewById(R.id.btLogin);

        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        //awesomeValidation.addValidation(this, R.id.etNombreRegistro, Patterns.EMAIL_ADDRESS, R.string.invalid_mail);

        btRecuperar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mail = etEmail.getText().toString();
                sendEmail(mail);
            }
        });

        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RecuperarPass.this, Login.class);
                startActivity(i);
            }
        });
    }

    public void sendEmail(String mail){
        String email = mail;
        firebaseAuth.sendPasswordResetEmail(mail).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(RecuperarPass.this, "Email Enviado !", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(RecuperarPass.this, Login.class);
                    startActivity(i);
                    finish();
                }else{
                    Toast.makeText(RecuperarPass.this, "Correo Invalido !", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}