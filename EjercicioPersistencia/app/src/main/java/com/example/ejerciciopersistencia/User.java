package com.example.ejerciciopersistencia;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;

public class User {

    private static final String FILE_NAME = "userData.txt";

    public static void saveUsername(Context context, String username){
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
            fileOutputStream.write(username.getBytes());

        }catch (Exception e){
            Log.e("ERROR WRITING", e.getMessage());
        }

        try{
            fileOutputStream.close();
        }catch (Exception e){
            Log.e("ERROR CLOSE WRITING", e.getMessage());
        }

    }

    public static boolean isRegistered(Activity context) {

        FileInputStream fileInputStream = null;

        try{
            fileInputStream = context.openFileInput(FILE_NAME);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            // si el archivo existe
            String line = bufferedReader.readLine();
            if(line != null && !line.equals("")){
                return true;
            }else {
                return false;
            }
        }catch (Exception e){
            try{
                fileInputStream.close();
            }catch (Exception er){}

            // Si NO existe el documento
            return false;
        }

    }



}
