package com.h2code.huchat;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

    public class GroupeFindFriendsActivity extends AppCompatActivity
    {
        private Toolbar mToolbar;
        private RecyclerView FindFriendsRecyclerList;
        private DatabaseReference UsersRef;

        private FirebaseAuth mAuth;
        private FirebaseUser currentUser;
        private String  CurrentgroupeName , CurrentGroupeCreatorId , CurrentUserName ;

        @Override
        protected void onCreate(Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_groupe_find_friends);
            mAuth = FirebaseAuth.getInstance();

                      currentUser = mAuth.getCurrentUser();

            UsersRef = FirebaseDatabase.getInstance().getReference().child("Users");


            CurrentgroupeName = getIntent().getExtras().get("CurrentgroupeName").toString();

            CurrentUserName = getIntent().getExtras().get("CurrentUserName").toString();

            CurrentGroupeCreatorId   =   getIntent().getExtras().get("CurrentGroupeCreatorId").toString();


            FindFriendsRecyclerList = (RecyclerView) findViewById(R.id.recycler_viewg);
            FindFriendsRecyclerList.setLayoutManager(new LinearLayoutManager(this));


            mToolbar = (Toolbar) findViewById(R.id.find_friends_toolbarg);
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
                            .setQuery(UsersRef, Contacts.class)
                            .build();

            FirebaseRecyclerAdapter<Contacts, FindFriendViewHolder> adapter =
                    new FirebaseRecyclerAdapter<Contacts, FindFriendViewHolder>(options) {
   @Override
   protected void onBindViewHolder(@NonNull FindFriendViewHolder holder, final int position, @NonNull Contacts model)
   {
       holder.userName.setText(model.getName());
       holder.userStatus.setText(model.getStatus());
       Picasso.get().load(model.getImage()).placeholder(R.drawable.profile_image).into(holder.profileImage);


       holder.itemView.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view)
           {
     String visit_user_id = getRef(position).getKey();

     Intent profileIntent = new Intent(GroupeFindFriendsActivity.this, GroupesProfileActivity.class);

     profileIntent.putExtra("CurrentgroupeName", CurrentgroupeName);

     profileIntent.putExtra("CurrentUserName", CurrentUserName);


     profileIntent.putExtra("CurrentGroupeCreatorId", CurrentGroupeCreatorId);


                profileIntent.putExtra("visit_user_id", visit_user_id);
                startActivity(profileIntent);
            }
        });
                        }

  @NonNull
  @Override
  public FindFriendViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
  {
      View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.users_display_layout, viewGroup, false);
      FindFriendViewHolder viewHolder = new FindFriendViewHolder(view);
                            return viewHolder;
                        }
                    };

            FindFriendsRecyclerList.setAdapter(adapter);

            adapter.startListening();
        } */



        public static class FindFriendViewHolder extends RecyclerView.ViewHolder
        {
            TextView userName, userStatus;
            CircleImageView profileImage;


            public FindFriendViewHolder(@NonNull View itemView)
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

  FirebaseRecyclerAdapter<Contacts, FindFriendViewHolder> adapter =
          new FirebaseRecyclerAdapter<Contacts, FindFriendViewHolder>(options) {
              @Override
              protected void onBindViewHolder
 (@NonNull FindFriendViewHolder holder,
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

          Intent profileIntent = new Intent(GroupeFindFriendsActivity.this, GroupesProfileActivity.class);

          profileIntent.putExtra("CurrentgroupeName", CurrentgroupeName);

          profileIntent.putExtra("CurrentUserName", CurrentUserName);


          profileIntent.putExtra("CurrentGroupeCreatorId", CurrentGroupeCreatorId);


          profileIntent.putExtra("visit_user_id", visit_user_id);
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
                        public FindFriendViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.users_display_layout, viewGroup, false);
                            FindFriendViewHolder viewHolder = new FindFriendViewHolder(view);
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
