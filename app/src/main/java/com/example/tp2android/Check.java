package com.example.tp2android;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Check extends AppCompatActivity {
    TextView chall1;
    TextView chall2;
    EditText result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check);

        // intent
        final Intent intent = getIntent();
        int challenge1 = intent.getIntExtra("challenge1",0);
        int challenge2 = intent.getIntExtra("challenge2",1);
        chall1 = (TextView)findViewById( R.id.textView8 );
        chall2 = (TextView)findViewById( R.id.textView9 );
        chall1.setText(Integer.toString(challenge1));
        chall2.setText(Integer.toString(challenge2));

        result = (EditText)findViewById( R.id.editTextTextPersonName );
    }

    public void cancel(View view) {
        finish();
        Toast.makeText(this, "l’opération a été annulée", Toast.LENGTH_SHORT).show();
    }

    public void ok(View view) {
        int somme = Integer.parseInt(result.getText().toString());
        Intent resultIntent = new Intent();
        resultIntent.putExtra("somme", somme);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }
}