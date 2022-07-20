package com.example.ejerciciopersistencia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new android.os.Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                // código que se ejecuta dentro de 500 mililsegundos
                loadNextActivity();
            }
        }, 500);

    }

    private void loadNextActivity(){

        if(User.isRegistered(this)){
            // dentro de 0.5 segundos, nos envía a la pantalla de registro con un intent.
            Intent intentMenu = new Intent(getApplicationContext(), MainMenuActivity.class);
            startActivity(intentMenu);

        }else {
            // dentro de 0.5 segundos nos envia a la pantalla de menu prinicpal  con un intent.
            Intent intentRegister = new Intent(getApplicationContext(), RegisterActivity.class);
            startActivity(intentRegister);
        }

    }

}