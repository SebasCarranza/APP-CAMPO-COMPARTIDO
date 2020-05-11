package com.focux.focuxRiego;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private EditText etn,etp;
    private static final String UsuarioValido="1";    //lomasvmt
    private static final String PasswordValido="2";   //paraiso2020

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        etn=(EditText)findViewById(R.id.editText);
        etp=(EditText)findViewById(R.id.editText2);
    }
    //Método para el botón
    public void Registrar(View view){
        String nombre=etn.getText().toString();
        String password=etp.getText().toString();
        if(nombre.length()==0){
            if(password.length()==0){
                Toast.makeText(this, "INGRESAR USUARIO Y CONSTRASEÑA",Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(this,"INGRESAR USUARIO",Toast.LENGTH_LONG).show();
            }

        }else {
            if(password.length()==0) {
                Toast.makeText(this, "INGRESAR CONSTRASEÑA", Toast.LENGTH_LONG).show();
            }
            else {
                if (nombre.equals(UsuarioValido) && password.equals(PasswordValido))  {
                    Intent menuRiego =new Intent(this, MenuPrincipal.class);
                    startActivity(menuRiego);
                }
                else{
                    Toast.makeText(this, "USUARIO O CONTRASEÑA INCORRECTO",Toast.LENGTH_SHORT).show();
                }
            }
        }

    }

}
