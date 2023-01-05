package com.example.tp2android;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {
    TextView textView;
    TextView url;
    TextView challenge1;
    TextView challenge2;
    private int CALL_Perm;
    int chall1;
    int chall2;

    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == 78) {
                        Intent data = result.getData();

                        if (data!=null){
                            ActivityCompat.requestPermissions(MainActivity.this, new String[]
                                    {Manifest.permission.CALL_PHONE}, CALL_Perm);
                            Intent intent = new Intent(Intent.ACTION_CALL);
                            intent.setData(Uri.parse("tel:"+textView.getText().toString()));
                            ActivityCompat.requestPermissions(MainActivity.this, new String[]
                                    {Manifest.permission.CALL_PHONE}, CALL_Perm);
                            startActivity(intent);
                        }
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.edtTxtName);
        url = (TextView) findViewById(R.id.editURL);
        challenge1 = (TextView) findViewById(R.id.editTextTextPersonName3);
        challenge2 = (TextView) findViewById(R.id.editTextTextPersonName4);
    }

    public void call(View view) {
        Intent myIntent = new Intent("login.ACTION");
        activityResultLauncher.launch(myIntent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode,
                permissions, grantResults);
        //check the permission type using the requestCode
        if (requestCode == CALL_Perm) {
        //the array is empty if not granted
            if (grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED)
                Toast.makeText(this, "GRANTED CALL", Toast.LENGTH_SHORT).show();
        }
    }

    public void navigate(View view) {
        if (!challenge1.getText().toString().equals("") && !challenge2.getText().toString().equals("")){
            Intent myIntent = new Intent(MainActivity.this, Check.class);
            chall1 = Integer.parseInt(challenge1.getText().toString());
            chall2 = Integer.parseInt(challenge2.getText().toString());
            myIntent.putExtra("challenge1", chall1);
            myIntent.putExtra("challenge2", chall2);
            startActivityForResult(myIntent,1);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            int somme = data.getIntExtra("somme",1);
            if(somme == chall1 + chall2){
                String site = "https://www.emi.ac.ma/";
                if(!url.getText().toString().equals(""))
                    site = url.getText().toString();
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(site));
                startActivity(i);
            } else {
                Toast.makeText(this, "fausse somme", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void persoActivity(View view) {
        Intent intent = new Intent(this, ActivitePerso.class);
        startActivity(intent);
    }
}