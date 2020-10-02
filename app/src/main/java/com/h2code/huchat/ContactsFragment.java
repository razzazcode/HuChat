package com.h2code.huchat;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class ContactsFragment extends Fragment
{
    private View ContactsView;
    private RecyclerView myContactsList;

    private DatabaseReference ContacsRef, UsersRef;
    private FirebaseAuth mAuth;
    private String currentUserID;


    public ContactsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        setHasOptionsMenu(true); // Add this!




        // Inflate the layout for this fragment
        ContactsView = inflater.inflate(R.layout.fragment_contacts, container, false);


        myContactsList = (RecyclerView) ContactsView.findViewById(R.id.contacts_list);
        myContactsList.setLayoutManager(new LinearLayoutManager(getContext()));


        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();


        ContacsRef = FirebaseDatabase.getInstance().getReference()
                .child("Contacts").child(currentUserID);
        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users");


        loadData("");

        return ContactsView;
    }


    private void loadData(String s) {

        Query databasesearchReference =  ContacsRef.orderByChild("Contact")
                .startAt(s)
                .endAt(s+"\uf8ff");

        FirebaseRecyclerOptions options =
                new FirebaseRecyclerOptions.Builder<Contacts>()
                .setQuery(databasesearchReference, Contacts.class)
                .build();


        final FirebaseRecyclerAdapter<Contacts, ContactsViewHolder> adapter
                = new FirebaseRecyclerAdapter<Contacts, ContactsViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final ContactsViewHolder holder, int position, @NonNull Contacts model)
            {
 final String userIDs = getRef(position).getKey();

 UsersRef.child(userIDs).addValueEventListener(new ValueEventListener() {
          @Override
     public void onDataChange(DataSnapshot dataSnapshot)
     {
         if (dataSnapshot.exists())
     {


         final String retName = dataSnapshot.child("name").getValue().toString();
         final String retStatus = dataSnapshot.child("status").getValue().toString();


         final String[] retImage = {"default_image"};

         if (dataSnapshot.hasChild("image"))
         {

             retImage[0] = dataSnapshot.child("image").getValue().toString();

           //  retImage[0] = dataSnapshot.child("image").getValue().toString();
             Picasso.get().load(retImage[0]).into(holder.profileImage);
         }



         if (dataSnapshot.child("userState").hasChild("state"))
         {
 String state = dataSnapshot.child("userState")
         .child("state").getValue().toString();

      String date = dataSnapshot.child("userState")
              .child("date").getValue().toString();

  String time = dataSnapshot.child("userState")
          .child("time").getValue().toString();


if (state.equals("online"))
{
    holder.onlineIcon.setVisibility(View.VISIBLE);

    holder.userStatus.setText(retStatus);

}
else if (state.equals("offline"))
{
    holder.onlineIcon.setVisibility(View.INVISIBLE);

    holder.userStatus.setText("Last Seen: " + date + " " + time);



}
       }
       else
       {
           holder.userStatus.setText("offline");



           holder.onlineIcon.setVisibility(View.INVISIBLE);
       }


   if (dataSnapshot.hasChild("image"))
   {
       String userImage = dataSnapshot.child("image").getValue().toString();
       String profileName = dataSnapshot.child("name").getValue().toString();
       String profileStatus = dataSnapshot.child("status").getValue().toString();

       holder.userName.setText(profileName);
       holder.userStatus.setText(profileStatus);
       Picasso.get().load(userImage).placeholder(R.drawable.profile_image).into(holder.profileImage);
   }
   else
   {
        String profileName = dataSnapshot.child("name").getValue().toString();
       String profileStatus = dataSnapshot.child("status").getValue().toString();

       holder.userName.setText(profileName);
       holder.userStatus.setText(profileStatus);
   }


         holder.itemView.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view)
             {
                 Intent profileIntent = new Intent(getContext(), ProfileActivity .class);
                 profileIntent.putExtra("visit_user_id", userIDs);
                 profileIntent.putExtra("visit_user_name", retName);
                 profileIntent.putExtra("visit_image", retImage[0]);
                 startActivity(profileIntent);
             }
         });






       }
   }

   @Override
   public void onCancelled(DatabaseError databaseError) {

   }
                });
            }

            @NonNull
            @Override
            public ContactsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
            {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.users_display_layout, viewGroup, false);
                ContactsViewHolder viewHolder = new ContactsViewHolder(view);
                return viewHolder;
            }
        };

        myContactsList.setAdapter(adapter);
        adapter.startListening();
    }





    @Override
    public void onCreateOptionsMenu(Menu menu , MenuInflater inflater) {



        super.onCreateOptionsMenu(menu, inflater);
      //  menu.clear();


        inflater.inflate(R.menu.searchfrag ,  menu);
        MenuItem menuItem = menu.findItem(R.id.SearchFragment);
        SearchView searchView = (SearchView) menuItem.getActionView();

      //  menuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW | MenuItem.SHOW_AS_ACTION_IF_ROOM);
        //  menuItem.setActionView(searchView);




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




    }









    public static class ContactsViewHolder extends RecyclerView.ViewHolder
    {
        TextView userName, userStatus;
        CircleImageView profileImage;
        ImageView onlineIcon;


        public ContactsViewHolder(@NonNull View itemView)
        {
            super(itemView);

            userName = itemView.findViewById(R.id.user_profile_name);
            userStatus = itemView.findViewById(R.id.user_status);
            profileImage = itemView.findViewById(R.id.users_profile_image);
            onlineIcon = (ImageView) itemView.findViewById(R.id.user_online_status);
        }
    }
}
