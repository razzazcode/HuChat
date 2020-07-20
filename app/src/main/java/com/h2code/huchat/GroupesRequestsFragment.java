package com.h2code.huchat;



import android.app.AlertDialog;
        import android.content.DialogInterface;
        import android.content.Intent;
        import android.os.Bundle;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.Button;
        import android.widget.TextView;
        import android.widget.Toast;

        import androidx.annotation.NonNull;
        import androidx.fragment.app.Fragment;
        import androidx.recyclerview.widget.LinearLayoutManager;
        import androidx.recyclerview.widget.RecyclerView;

        import com.firebase.ui.database.FirebaseRecyclerAdapter;
        import com.firebase.ui.database.FirebaseRecyclerOptions;
        import com.google.android.gms.tasks.OnCompleteListener;
        import com.google.android.gms.tasks.Task;
        import com.google.firebase.auth.FirebaseAuth;
        import com.google.firebase.database.DataSnapshot;
        import com.google.firebase.database.DatabaseError;
        import com.google.firebase.database.DatabaseReference;
        import com.google.firebase.database.FirebaseDatabase;
        import com.google.firebase.database.ValueEventListener;
        import com.squareup.picasso.Picasso;

import java.security.PublicKey;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class GroupesRequestsFragment extends Fragment
{
    private View RequestsFragmentView;
    private RecyclerView myRequestsList;

    private DatabaseReference ChatRequestsRef, GroupesContactsOfAgroupetRef,  UsersRef, ContactsRef , GroupesRequestsFragmentRef;
    private FirebaseAuth mAuth;
    private String currentUserID;



    public GroupesRequestsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        RequestsFragmentView = inflater.inflate(R.layout.fragment_requests, container, false);


        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users");
        ChatRequestsRef = FirebaseDatabase.getInstance().getReference().child("Chat Requests");
        ContactsRef = FirebaseDatabase.getInstance().getReference().child("Contacts");



        GroupesContactsOfAgroupetRef = FirebaseDatabase.getInstance().getReference().child("GroupesMainActivity")
                .child("Groupes");


        GroupesRequestsFragmentRef = FirebaseDatabase.getInstance().getReference()
                .child("GroupesMainActivity").child("GroupesRequests");


        myRequestsList = (RecyclerView) RequestsFragmentView.findViewById(R.id.chat_requests_list);
        myRequestsList.setLayoutManager(new LinearLayoutManager(getContext()));


        return RequestsFragmentView;
    }


    @Override
    public void onStart()
    {
        super.onStart();

        FirebaseRecyclerOptions<Contacts> options =
                new FirebaseRecyclerOptions.Builder<Contacts>()
            .setQuery(GroupesRequestsFragmentRef
                   .child(currentUserID), Contacts.class)
                        .build();


        FirebaseRecyclerAdapter<Contacts, RequestsViewHolder> adapter =
                new FirebaseRecyclerAdapter<Contacts, RequestsViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull final RequestsViewHolder holder, final int position, @NonNull Contacts model)
     {
         holder.itemView.findViewById(R.id.request_accept_btn).setVisibility(View.VISIBLE);
         holder.itemView.findViewById(R.id.request_cancel_btn).setVisibility(View.VISIBLE);


         final String list_GroupeNmae = getRef(position).getKey();







         DatabaseReference getTypeRef = getRef(position)
                 .child("request_type").getRef();




    /*     holder.profileImage.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {

                 GroupesRequestsFragmentRef.child(currentUserID).child(list_GroupeNmae)
            .addValueEventListener(new ValueEventListener() {
                @Override
        public void onDataChange(DataSnapshot dataSnapshot)
    {


        final String requestGroupeName = dataSnapshot.child("GroupeName").getValue().toString();


        final String requestGroupeCreator = dataSnapshot.child("GroupeCreator").getValue().toString();
        Intent profileIntent = new Intent(getContext(), GroupesProfileActivity.class);
        profileIntent.putExtra("visit_user_id", requestGroupeCreator);
        profileIntent.putExtra("GroupeCreatorId" , requestGroupeCreator);

        profileIntent.putExtra("GroupeName" , requestGroupeName);
        startActivity(profileIntent);
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
                     });





             }
         }); */


         getTypeRef.addValueEventListener(new ValueEventListener() {
             @Override
             public void onDataChange(DataSnapshot dataSnapshot)
             {
                                if (dataSnapshot.exists())
      {
          String type = dataSnapshot.getValue().toString();

          if (type.equals("received"))
          {






     GroupesRequestsFragmentRef.child(currentUserID).child(list_GroupeNmae)
             .addValueEventListener(new ValueEventListener() {
         @Override
         public void onDataChange(DataSnapshot dataSnapshot)
         {
                      if (dataSnapshot.hasChild("GroupeCreatorImagePath"))
     {
         final String requestProfileImage = dataSnapshot.child("GroupeCreatorImagePath").getValue().toString();

         Picasso.get().load(requestProfileImage).into(holder.profileImage);
     }


             if (dataSnapshot.hasChild("GroupeName" )
                     && dataSnapshot.hasChild("GroupeCreator")
             && dataSnapshot.hasChild("GroupeCreatorUserName" )) {


                 final String requestGroupeName = dataSnapshot.child("GroupeName").getValue().toString();
                 final String requestGroupeCreator = dataSnapshot.child("GroupeCreator").getValue().toString();
final String  GroupeCreatorUserName = dataSnapshot.child("GroupeCreatorUserName").getValue().toString();


                 holder.userName.setText(GroupeCreatorUserName);
                 holder.userStatus.setText("wants to invite you to the groupe ." + requestGroupeName);









     holder.AcceptButton.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {







    GroupesContactsOfAgroupetRef.child(requestGroupeCreator).child(list_GroupeNmae)

            .child("ContactsOfTheGroupe").child(currentUserID)
           .setValue("Saved").addOnCompleteListener(new OnCompleteListener<Void>() {
       @Override
       public void onComplete(@NonNull Task<Void> task)
                {

         if (task.isSuccessful())
         {
             GroupesRequestsFragmentRef.child(currentUserID).child(list_GroupeNmae)
                     .removeValue()
      .addOnCompleteListener(new OnCompleteListener<Void>() {
          @Override
                         public void onComplete(@NonNull Task<Void> task)
                         {
                             if (task.isSuccessful())
                                     {

                  UsersRef  .child(currentUserID).child("OwnGrpName")
                          .child(list_GroupeNmae)
                          .child("GroupeNameValue").setValue(list_GroupeNmae);

                    UsersRef  .child(currentUserID).child("OwnGrpName")
                                  .child(list_GroupeNmae)
                            .child("GroupeCreator").setValue(requestGroupeCreator);

             GroupesRequestsFragmentRef.child(list_GroupeNmae)
                     .child(requestGroupeCreator).child(currentUserID)
                    .removeValue()
       .addOnCompleteListener(new OnCompleteListener<Void>() {
           @Override
           public void onComplete(@NonNull Task<Void> task)
           {
               if (task.isSuccessful())
               {
                   Toast.makeText
                (getContext(), "New Contact Saved in " + list_GroupeNmae + " Groupe", Toast.LENGTH_SHORT).show();
               }
                             }
                         });
             }
              }
          });
                          }

          }
      });

                                                    }







                                                });

            holder.CancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {




        GroupesRequestsFragmentRef.child(currentUserID).child(list_GroupeNmae)
                .removeValue()
       .addOnCompleteListener(new OnCompleteListener<Void>() {
           @Override
           public void onComplete(@NonNull Task<Void> task)
           {
                        if (task.isSuccessful())
     {
         GroupesRequestsFragmentRef.child(list_GroupeNmae).child(requestGroupeCreator)
                 .child(currentUserID)
             .removeValue()
             .addOnCompleteListener(new OnCompleteListener<Void>() {
        @Override
        public void onComplete(@NonNull Task<Void> task)
        {
            if (task.isSuccessful())
            {
                Toast.makeText(getContext(), "Contact Deleted", Toast.LENGTH_SHORT).show();
            }
        }
                    });
                 }
             }
         });


         }
     });



     holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
         public void onClick(View view)
         {
             CharSequence options[] = new CharSequence[]
                     {
                             "Accept",
                             "Cancel"
                            };

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(requestGroupeName  + "  Chat Request");

        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                if (i == 0)
                {
                    GroupesContactsOfAgroupetRef.child(currentUserID).child(list_GroupeNmae).child("Contact")
                                        .setValue("Saved").addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
         public void onComplete(@NonNull Task<Void> task)
         {
             if (task.isSuccessful())
             {
                 GroupesContactsOfAgroupetRef.child(list_GroupeNmae)
                         .child(currentUserID).child("Contact")
            .setValue("Saved").addOnCompleteListener(new OnCompleteListener<Void>() {
        @Override
        public void onComplete(@NonNull Task<Void> task)
        {
            if (task.isSuccessful())
            {
                GroupesRequestsFragmentRef.child(currentUserID).child(list_GroupeNmae)
                        .removeValue()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                                         public void onComplete(@NonNull Task<Void> task)
            {
                if (task.isSuccessful())
                      {
                          GroupesRequestsFragmentRef.child(list_GroupeNmae)
                                  .child(requestGroupeCreator)
                                  .child(currentUserID)
  .removeValue()
  .addOnCompleteListener(new OnCompleteListener<Void>() {
      @Override
      public void onComplete(@NonNull Task<Void> task)
      {
          if (task.isSuccessful())
          {
              Toast.makeText(getContext(), "New Contact Saved", Toast.LENGTH_SHORT).show();
             }
         }
                  });
      }
  }
                   });
       }
          }
               });
           }
       }
                                });
                            }
                            if (i == 1)
                            {
                                GroupesRequestsFragmentRef.child(currentUserID).child(list_GroupeNmae)
   .removeValue()
   .addOnCompleteListener(new OnCompleteListener<Void>() {
       @Override
       public void onComplete(@NonNull Task<Void> task)
       {
           if (task.isSuccessful())
    {
        GroupesRequestsFragmentRef.child(list_GroupeNmae)

                .child(requestGroupeCreator)

                .child(currentUserID)
      .removeValue()
      .addOnCompleteListener(new OnCompleteListener<Void>() {
   @Override
   public void onComplete(@NonNull Task<Void> task)
   {
       if (task.isSuccessful())
       {
           Toast.makeText(getContext(), "Contact Deleted", Toast.LENGTH_SHORT).show();
                     }
                           }
                       });
           }
                                            }
                                        });
                            }
                        }
                    });
                    builder.show();
                }
            });

        }}

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
                                        });
                                    }
             else if (type.equals("sent"))
             {
                 Button request_sent_btn = holder.itemView.findViewById(R.id.request_accept_btn);
                 request_sent_btn.setText("Cancel sent request");




                 request_sent_btn.setOnClickListener(new View.OnClickListener() {
                                            @Override
       public void onClick(View v) {



           GroupesRequestsFragmentRef.child(currentUserID).child(list_GroupeNmae)
                   .removeValue()
                   .addOnCompleteListener(new OnCompleteListener<Void>() {
                       @Override
                       public void onComplete(@NonNull Task<Void> task)
                       {
                           if (task.isSuccessful())
                           {
                               GroupesRequestsFragmentRef.child(list_GroupeNmae)


                                       .child(currentUserID)
       .removeValue()
       .addOnCompleteListener(new OnCompleteListener<Void>() {
           @Override
           public void onComplete(@NonNull Task<Void> task)
           {
               if (task.isSuccessful())
                            {
                                Toast.makeText(getContext(), "you have cancelled the chat request.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
                            }
                        });
            }
        });






     holder.itemView.findViewById(R.id.request_cancel_btn).setVisibility(View.INVISIBLE);

     UsersRef.child(list_GroupeNmae).addValueEventListener(new ValueEventListener() {
         @Override
         public void onDataChange(DataSnapshot dataSnapshot)
         {
             if (dataSnapshot.hasChild("image"))
             {
                 final String requestProfileImage = dataSnapshot.child("image").getValue().toString();

                 Picasso.get().load(requestProfileImage).into(holder.profileImage);
             }

             final String requestUserName = dataSnapshot.child("name").getValue().toString();
             final String requestUserStatus = dataSnapshot.child("status").getValue().toString();

             holder.userName.setText(requestUserName);
             holder.userStatus.setText("you have sent a request to " + requestUserName);





  holder.itemView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view)
      {
          CharSequence options[] = new CharSequence[]
                  {
             "Cancel Chat Request"
     };

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
    builder.setTitle("Already Sent Request");

    builder.setItems(options, new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i)
        {
     if (i == 0)
               {
                   GroupesRequestsFragmentRef.child(currentUserID).child(list_GroupeNmae)
                           .removeValue()
     .addOnCompleteListener(new OnCompleteListener<Void>() {
         @Override
                public void onComplete(@NonNull Task<Void> task)
    {
        if (task.isSuccessful())
        {
            GroupesRequestsFragmentRef.child(list_GroupeNmae).child(currentUserID)
                                .removeValue()
           .addOnCompleteListener(new OnCompleteListener<Void>() {
               @Override
               public void onComplete(@NonNull Task<Void> task)
               {
        if (task.isSuccessful())
        {
            Toast.makeText(getContext(), "you have cancelled the chat request.", Toast.LENGTH_SHORT).show();
        }
              }
          });
                    }
                }
            });
                             }
                         }
                     });
                     builder.show();
                 }
             });

         }

         @Override
         public void onCancelled(DatabaseError databaseError) {

         }
     });
                                    }
                                }
         }

         @Override
         public void onCancelled(DatabaseError databaseError) {

         }
                        });
                    }

                    @NonNull
         @Override
         public RequestsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
         {
             View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.users_display_layout, viewGroup, false);
             RequestsViewHolder holder = new RequestsViewHolder(view);
             return holder;
                    }
                };

        myRequestsList.setAdapter(adapter);
        adapter.startListening();
    }



    public static class RequestsViewHolder extends RecyclerView.ViewHolder
    {
        TextView userName, userStatus;
        CircleImageView profileImage;
        Button AcceptButton, CancelButton;


        public RequestsViewHolder(@NonNull View itemView)
        {
            super(itemView);


            userName = itemView.findViewById(R.id.user_profile_name);
            userStatus = itemView.findViewById(R.id.user_status);
            profileImage = itemView.findViewById(R.id.users_profile_image);
            AcceptButton = itemView.findViewById(R.id.request_accept_btn);
            CancelButton = itemView.findViewById(R.id.request_cancel_btn);







        }
    }










}
