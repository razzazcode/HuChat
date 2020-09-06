package com.h2code.huchat;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;



import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;


public class FindFriendsSearchActivity extends AppCompatActivity {

    private EditText editTextProduct;
    private EditText editTextPrice;



    private DatabaseReference myRef;
    private String ValueDatabase;
    private String refinedData;
    private ListView listView;

    private SearchView searchView;
    private TextView textViewSearch;

private EditText searchable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_friends_search);

        myRef = FirebaseDatabase.getInstance().getReference().child("Users");



       //  editTextPrice = findViewById(R.id.editTextPrice);
      //  editTextProduct = findViewById(R.id.editTextProduct);

        searchable= findViewById(R.id.editTextTextPersonName);

        listView =findViewById(R.id.listView);
        searchView = findViewById(R.id.serachview);
        textViewSearch = findViewById(R.id.textViewSearch);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ValueDatabase = dataSnapshot.getValue().toString();
                refinedData = ValueDatabase.substring(1,ValueDatabase.length()-1);
                String List[] = refinedData.split(",");
               // listView.setAdapter(new ArrayAdapter<String>(FindFriendsSearchActivity.this, android.R.layout.simple_list_item_1, List));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                int SearchIndex = refinedData.indexOf(query);
                String SearchResult = refinedData.substring(SearchIndex);
            //    String SearchSplit[] = SearchResult.split(",");
                ArrayList<String> list=new ArrayList<String>();
                String searchtxt = searchable.getText().toString();
                String i = new String();
                if ( SearchResult.contains(searchtxt) ) {

                    list.add(SearchResult);
                }
                else {
                    list.add("llll");

                }
                //textViewSearch.setText(SearchSplit[0]);
                 listView.setAdapter(new ArrayAdapter<String>(FindFriendsSearchActivity.this,
                         android.R.layout.simple_list_item_1, list ));

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    public void InsertButton(View view){
        try {


            myRef.child("hghgh").setValue("gfhjghj");


       //     myRef.child(editTextProduct.getText().toString()).setValue(editTextPrice.getText().toString());
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}