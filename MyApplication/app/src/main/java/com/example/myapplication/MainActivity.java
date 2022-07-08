package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.LinkedList;

public class MainActivity extends AppCompatActivity {

    Button addbtn;
    Button removeLastbtn;
    Button clearbtn;
    TextInputEditText inputtxt;
    TextView outputtxt;

    LinkedList<String> content = new LinkedList<>();

    private void onContentChanges(){

        String out = "";

        for (String txt: content) {
            out += txt;
        }

        if(out.isEmpty()){
            out = getString(R.string.empty_list_warning);
        }

        outputtxt.setText(out);
    }

    private void addElement(String element){
        content.add("   - "+element + "\n");
    }

    private View.OnClickListener onAddClicked = new View.OnClickListener() {
        public void onClick(View v) {
            String txt = inputtxt.getText().toString();

            if(!txt.isEmpty()){
                addElement(txt);
                onContentChanges();
                inputtxt.setText("");
            }

        }
    };

    private View.OnClickListener onRemoveLastClicked = new View.OnClickListener() {
        public void onClick(View v) {
            content.removeLast();
            onContentChanges();
        }
    };

    private View.OnClickListener onClearClicked = new View.OnClickListener() {
        public void onClick(View v) {
            content.clear();
            onContentChanges();
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addbtn = findViewById(R.id.addbtn);
        removeLastbtn = findViewById(R.id.removelastbtn);
        clearbtn = findViewById(R.id.clearbtn);
        inputtxt = findViewById(R.id.textinput);
        outputtxt = findViewById(R.id.outputtxt);

        addbtn.setOnClickListener(onAddClicked);
        clearbtn.setOnClickListener(onClearClicked);
        removeLastbtn.setOnClickListener(onRemoveLastClicked);

        onContentChanges();

    }
}