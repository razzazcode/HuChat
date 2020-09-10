package com.h2code.huchat;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.transition.Hold;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class SearchData extends AppCompatActivity
{
    private Toolbar mToolbar;
    private RecyclerView FindFriendsRecyclerList;
    private DatabaseReference UsersRef ;
    private EditText databasesearchReferenceT ;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;




    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_data);



        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();







        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users");


        FindFriendsRecyclerList = (RecyclerView) findViewById(R.id.find_friends_recycler_lists);
        FindFriendsRecyclerList.setLayoutManager(new LinearLayoutManager(this));


        mToolbar = (Toolbar) findViewById(R.id.find_friends_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Find Friends");



loadData("");



        }


/*
    @Override
    protected void onStart()
    {
        super.onStart();








        FirebaseRecyclerOptions<Contacts> options =
                new FirebaseRecyclerOptions.Builder<Contacts>()
                        .setQuery(UsersRef, Contacts.clas .build();

        FirebaseRecyclerAdapter<Contacts, SearchDataViewHolder> adapter =
                new FirebaseRecyclerAdapter<Contacts, SearchDataViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull SearchDataViewHolder holder, final int position, @NonNull Contacts model)
                    {
                        holder.userName.setText(model.getName());
                        holder.userStatus.setText(model.getStatus());
                        Picasso.get().load(model.getImage()).placeholder(R.drawable.profile_image).into(holder.profileImage);


             holder.itemView.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View view)
                 {
                     String visit_user_id = getRef(position).getKey();

                     Intent profileIntent = new Intent(SearchData.this, ProfileActivity.class);
                     profileIntent.putExtra("visit_user_id", visit_user_id);
                     startActivity(profileIntent);
                 }
             });
         }

         @NonNull
         @Override
         public SearchDataViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
         {
             View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.users_display_layout, viewGroup, false);
             SearchDataViewHolder viewHolder = new SearchDataViewHolder(view);
             return viewHolder;
                    }
                };

        FindFriendsRecyclerList.setAdapter(adapter);

        adapter.startListening();
    }
*/

    public static  class SearchDataViewHolder extends RecyclerView.ViewHolder
    {
        TextView userName, userStatus;
        CircleImageView profileImage;


        public SearchDataViewHolder(@NonNull View itemView)
        {
            super(itemView);

            userName = itemView.findViewById(R.id.user_profile_name);
            userStatus = itemView.findViewById(R.id.user_status);
            profileImage = itemView.findViewById(R.id.users_profile_image);
        }
    }





    private void loadData(String s) {

        Query databasesearchReference =  UsersRef.orderByChild("name")
                .startAt(s)
               .endAt(s+"\uf8ff");


        // queryText = databasesearchReferenceT.getText().toString();


        FirebaseRecyclerOptions<Contacts> options =
                new FirebaseRecyclerOptions.Builder<Contacts>()
                        .setQuery(databasesearchReference, Contacts.class)
                        .build();

        FirebaseRecyclerAdapter<Contacts, SearchDataViewHolder> adapter =
                new FirebaseRecyclerAdapter<Contacts, SearchDataViewHolder>(options) {
    @Override
    protected void onBindViewHolder
 (@NonNull SearchDataViewHolder holder,
  final int position, @NonNull final Contacts model) {


        if (!currentUser.getUid().equals(getRef(position).getKey().toString())) {

            holder.userName.setText(model.getName());
            holder.userStatus.setText(model.getStatus());
            Picasso.get().load(model.getImage()).placeholder(R.drawable.profile_image).into(holder.profileImage);


            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    String visit_user_id = getRef(position).getKey();

                    Intent profileIntent = new Intent(SearchData.this, ProfileActivity.class);
                    profileIntent.putExtra("visit_user_id", visit_user_id);
                    profileIntent.putExtra("visit_user_name", model.getName());
                    startActivity(profileIntent);
                }
            });




       } else {

           holder.itemView.setVisibility(View.GONE);
            holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));

       }


    }

     @NonNull
     @Override
     public SearchDataViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
         View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.users_display_layout, viewGroup, false);
         SearchDataViewHolder viewHolder = new SearchDataViewHolder(view);
                        return viewHolder;
                    }
                };


        adapter.startListening();
        FindFriendsRecyclerList.setAdapter(adapter);


    }
















    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

getMenuInflater().inflate(R.menu.search_menu , menu);
        MenuItem menuItem = menu.findItem(R.id.Search);
        SearchView searchView = (SearchView) menuItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {


loadData(s);


                return false;
            }
        });



        return true;
    }






}







