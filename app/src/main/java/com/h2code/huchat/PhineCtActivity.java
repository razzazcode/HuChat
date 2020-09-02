package com.h2code.huchat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.EditText;

public class PhineCtActivity extends AppCompatActivity {

    private EditText editText1;
    private EditText editText2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phine_ct);

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_CONTACTS, Manifest.permission.READ_CONTACTS}, PackageManager.PERMISSION_GRANTED);

        editText1 = findViewById(R.id.editText32);
        editText2 = findViewById(R.id.editText2);
    }

    public void getNameButton(View view){
        try {
            Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(editText2.getText().toString()));
            Cursor cursor = getContentResolver().query(uri, new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME}, null, null, null);

            String stringContactName = "INVALID";
            if (cursor != null){
                if (cursor.moveToFirst()){
                    stringContactName = cursor.getString(0);
                }
            }
            editText1.setText(stringContactName);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void getNumberButton(View view){
        try {
            Cursor cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.CommonDataKinds.Phone.TYPE},
                    "DISPLAY_NAME = '" + editText1.getText().toString() + "'", null, null);

            cursor.moveToFirst();
            editText2.setText(cursor.getString(0));
        }
        catch (Exception e){
            e.printStackTrace();
            editText2.setText("NA");
        }
    }
}