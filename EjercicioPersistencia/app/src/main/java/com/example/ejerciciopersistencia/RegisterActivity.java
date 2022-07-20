package com.example.ejerciciopersistencia;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.ejerciciopersistencia.databinding.ActivityRegisterBinding;

import java.io.FileOutputStream;

public class RegisterActivity extends AppCompatActivity {

    ActivityRegisterBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.registerButton.setOnClickListener(registerButtonClickEvent);

    }

    View.OnClickListener registerButtonClickEvent = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String username = binding.usernameField.getText().toString();


            if(!username.equals("")) {
                Toast t = Toast.makeText(getApplicationContext(), R.string.register_message_success, Toast.LENGTH_SHORT);
                t.show();
                User.saveUsername(getApplicationContext(), username);
                Intent intentMenu = new Intent(getApplicationContext(), MainMenuActivity.class);
                startActivity(intentMenu);
            }else {
                Toast t = Toast.makeText(getApplicationContext(), R.string.invalid_input_warning, Toast.LENGTH_SHORT);
                t.show();
            }



        }
    };





}