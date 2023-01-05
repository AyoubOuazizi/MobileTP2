package com.example.tp2android;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MyContacts extends AppCompatActivity {
    int Perm_CTC;
    TextView result;
    Button details;
    Button call;
    Uri uriContact;
    String phone;
    int CALL_Perm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_contacts);
        result = (TextView) findViewById(R.id.textView10);
        details = (Button) findViewById(R.id.button7);
        call = (Button) findViewById(R.id.button8);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        //check the permission type using the requestCode
        if (requestCode == Perm_CTC) {
            //the array is empty if not granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "GRANTED CALL", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void contact(View view) {
        Intent myIntent = new Intent(Intent.ACTION_PICK, Uri.parse("content://contacts/people"));
        ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.READ_CONTACTS}, Perm_CTC);
        startActivityForResult(myIntent,2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2) {
            if(resultCode == RESULT_CANCELED) {
                result.setText("opération annulée");
            } else if (resultCode == RESULT_OK) {
                uriContact = data.getData();
                result.setText(uriContact.toString());
                details.setEnabled(true);
            }
        }
    }

    public void showDetails(View view) {
        if (uriContact != null) {
            ContentResolver cr = getContentResolver();
            Cursor cur = cr.query(uriContact, null, null, null, null);
            if (cur.getCount() > 0) {// thats mean some resutl has been found
                if(cur.moveToNext()) {
                    String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                    String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                    if (Integer.parseInt(cur.getString(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0)
                    {
                        // Query phone here. Covered next
                        Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = "+ id,null, null);
                        while (phones.moveToNext()) {
                            String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                            phone = phoneNumber;
                            result.setText("name: "+name+" / Number: "+phoneNumber);
                            call.setEnabled(true);
                        }
                        phones.close();
                    }
                }
            }
            cur.close();
        }
    }

    public void callNow(View view) {
        ActivityCompat.requestPermissions(MyContacts.this, new String[]
                {Manifest.permission.CALL_PHONE}, CALL_Perm);
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:"+phone));
        ActivityCompat.requestPermissions(MyContacts.this, new String[]
                {Manifest.permission.CALL_PHONE}, CALL_Perm);
        startActivity(intent);
    }
}