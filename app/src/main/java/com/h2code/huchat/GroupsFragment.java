package com.h2code.huchat;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class GroupsFragment extends Fragment
{



    private RecyclerView myRequestsList , list_view22 , list_viewr;


    private View groupFragmentView;
    private ListView list_view;
    private ArrayAdapter<String> arrayAdapter;
    private ArrayList<String> list_of_groups = new ArrayList<>();

    private Button  createNewGroupe;

    private DatabaseReference generalGroupesRefSN, currentUserGroupesRef, UsersRef
            , GroupesContactsOfAgroupetRef ,GroupesRequestsFragmentRef ;

    private String CurrentUserName , currentUserID  , currentGroupName , CurrentGroupeCreatorId  ;
    private FirebaseUser currentUser;
    private FirebaseAuth mAuth;

    public GroupsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        groupFragmentView = inflater.inflate(R.layout.fragment_groups, container, false);


        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();




        generalGroupesRefSN = FirebaseDatabase.getInstance().getReference()
                .child("GroupesMainActivity").child("Groupes");


        currentUserGroupesRef = FirebaseDatabase.getInstance().getReference()
                .child("Users").child(currentUserID).child("OwnGrpName");

        UsersRef = FirebaseDatabase.getInstance().getReference()
                .child("Users");





        GetCurrentUserUserName();


IntializeFields();


  RetrieveAndDisplayGroups();






  createNewGroupe.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {


          RequestNewGroup();


            }
        });

/*
        list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
     @Override
     public void onItemClick(AdapterView<?> adapterView, View view, int position, long id)
     {
  currentGroupName = adapterView.getItemAtPosition(position).toString();

  GetCurrentGroupeCreatorId();





     }
 }); */




 GroupesContactsOfAgroupetRef = FirebaseDatabase.getInstance().getReference()
         .child("GroupesMainActivity")
         .child("Groupes");


 GroupesRequestsFragmentRef = FirebaseDatabase.getInstance().getReference()
                .child("GroupesMainActivity").child("GroupesRequests");


        // myRequestsList = (RecyclerView) RequestsFragmentView.findViewById(R.id.list_view2);



        myRequestsList = (RecyclerView) groupFragmentView.findViewById(R.id.list_view2);
        myRequestsList.setLayoutManager(new LinearLayoutManager(getContext()));

        list_viewr = (RecyclerView) groupFragmentView.findViewById(R.id.list_viewr);
        list_viewr.setLayoutManager(new LinearLayoutManager(getContext()));





        list_view22 = (RecyclerView) groupFragmentView.findViewById(R.id.list_view2);
        list_view22.setLayoutManager(new LinearLayoutManager(getContext()));


        loadData("");


        return groupFragmentView;


    }




    private void IntializeFields()
    {
       // list_view =  groupFragmentView.findViewById(R.id.list_view);
        arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, list_of_groups);











    //    list_view.setAdapter(arrayAdapter);


        createNewGroupe= groupFragmentView.findViewById(R.id.createnewgroupe);



    }




    private void RetrieveAndDisplayGroups()
    {
        currentUserGroupesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                Set<String> set = new HashSet<>();
                Iterator iterator = dataSnapshot.getChildren().iterator();

                while (iterator.hasNext())
                {

  //  String ng = ((DataSnapshot)iterator.next()).getKey();

               /*   if (ng .contains("neh"))  {
                      set.add(ng);
                  }
*/
                    set.add(((DataSnapshot)iterator.next()).getKey());

                }


                list_of_groups.clear();
                list_of_groups.addAll(set);

                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }



    private void RequestNewGroup()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.AlertDialog);
        builder.setTitle("Enter Group 2 Name :");

        final EditText groupNameField = new EditText(getContext());
        groupNameField.setHint("e.g  Hu Chat");
        builder.setView(groupNameField);

        builder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                String groupName = groupNameField.getText().toString();

                if (TextUtils.isEmpty(groupName))
                {
                    Toast.makeText(getContext(), "Please write Group Name...", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    CreateNewGroup(groupName);
                }
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                dialogInterface.cancel();
            }
        });

        builder.show();
    }







    private void CreateNewGroup(final String groupName)
    {



      //  GettingCreatorUserName();

         generalGroupesRefSN.child(currentUserID).child(groupName).child("GroupeName").setValue(groupName);

        generalGroupesRefSN.child(currentUserID).child(groupName).child("GroupeCreator")
                .setValue(currentUserID);

        generalGroupesRefSN.child(currentUserID).child(groupName).child("GroupeCreatorUserName")
                .setValue(CurrentUserName)



                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        if (task.isSuccessful())
                        {
                            Toast.makeText(getContext(), groupName + " group is Created Successfully...", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


        currentUserGroupesRef.child(groupName).child("GroupeName").setValue(groupName);
        currentUserGroupesRef.child(groupName).child("GroupeCreator").setValue(currentUserID);

    }









    private void GetCurrentUserUserName() {
        UsersRef.child(currentUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
     if (dataSnapshot.exists()) {

         CurrentUserName = dataSnapshot.child("name").getValue().toString();




     }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }




    private void GetCurrentGroupeCreatorId() {
        UsersRef.child(currentUserID).child("OwnGrpName").child(currentGroupName)
                .addValueEventListener(new ValueEventListener() {
                    @Override
       public void onDataChange(DataSnapshot dataSnapshot) {
           if (dataSnapshot.exists()) {

               CurrentGroupeCreatorId =
         dataSnapshot.child("GroupeCreator").getValue().toString();



               Intent groupChatIntent = new Intent(getContext(), GroupeChat2.class);
               groupChatIntent.putExtra("groupName" , currentGroupName);




               groupChatIntent.putExtra("GroupeCreatorID" , CurrentGroupeCreatorId);

               groupChatIntent.putExtra("CurrentUserName", CurrentUserName);

               startActivity(groupChatIntent);










           }
       }

       @Override
       public void onCancelled(DatabaseError databaseError) {

                    }
                });


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




        FirebaseRecyclerAdapter<Contacts, RequestsGropesViewHolder> adapter =
   new FirebaseRecyclerAdapter<Contacts, RequestsGropesViewHolder>(options) {
       @Override
       protected void onBindViewHolder(@NonNull final RequestsGropesViewHolder holder, final int position, @NonNull Contacts model)
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

            .child("ContactsOfTheGroupe").child(currentUserID).child("contact")
                     .setValue(CurrentUserName).addOnCompleteListener(new OnCompleteListener<Void>() {
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

            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext());
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
          .setValue(CurrentUserName).addOnCompleteListener(new OnCompleteListener<Void>() {
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

              android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext());
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
  public RequestsGropesViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
  {
      View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.users_display_layout, viewGroup, false);
      RequestsGropesViewHolder holder = new RequestsGropesViewHolder(view);
      return holder;
  }
                };


        list_view22.setAdapter(adapter);


        myRequestsList.setAdapter(adapter);
        adapter.startListening();
    }










    public static class RequestsGropesViewHolder extends RecyclerView.ViewHolder
    {
        TextView userName, userStatus;
        CircleImageView profileImage;
        Button AcceptButton, CancelButton;


        public RequestsGropesViewHolder(@NonNull View itemView)
        {
            super(itemView);


            userName = itemView.findViewById(R.id.user_profile_name);
            userStatus = itemView.findViewById(R.id.user_status);
            profileImage = itemView.findViewById(R.id.users_profile_image);
            AcceptButton = itemView.findViewById(R.id.request_accept_btn);
            CancelButton = itemView.findViewById(R.id.request_cancel_btn);







        }
    }



    private void loadData(String s) {

//    Query databasesearchReference =  UserGroupesRef.orderByChild("Contact")
  //          .startAt(s)
  //          .endAt(s+"\uf8ff");


 FirebaseRecyclerOptions<Contacts> optionsr =
         new FirebaseRecyclerOptions.Builder<Contacts>()
                 .setQuery(currentUserGroupesRef, Contacts.class)
                 .build();


     FirebaseRecyclerAdapter<Contacts, GroupesViewHolder> adapterr =
 new FirebaseRecyclerAdapter<Contacts, GroupesViewHolder>(optionsr) {
 @Override
 protected void onBindViewHolder(@NonNull final GroupesViewHolder holder,
                                 int position, @NonNull Contacts model)
 {
     final String selectedgrpName = getRef(position).getKey();
     final String[] retImage = {"default_image"};

     final String[] selectedgrpCreatorId = {"grpcreator"};

     currentUserGroupesRef.child(selectedgrpName).addValueEventListener(new ValueEventListener() {
         @Override
         public void onDataChange(@NonNull DataSnapshot snapshot) {


   selectedgrpCreatorId[0] = snapshot.child("GroupeCreator").getValue().toString();

             generalGroupesRefSN.child(selectedgrpCreatorId[0]).child(selectedgrpName)
                     .child("CurrentGroupesettings").addValueEventListener(new ValueEventListener() {
                 @Override
                 public void onDataChange(DataSnapshot dataSnapshot)
                 {
                     if (dataSnapshot.exists())
                     {
                         if (dataSnapshot.hasChild("image"))
                         {
                             retImage[0] = dataSnapshot.child("image").getValue().toString();
                             Picasso.get().load(retImage[0]).into(holder.profileImage);
                         }

                         //final String retName = dataSnapshot.child("name").getValue().toString();
                         final String retStatus = dataSnapshot.child("status").getValue().toString();

                         holder.userName.setText(selectedgrpName);
                         holder.userStatus.setText(retStatus);




                         holder.itemView.setOnClickListener(new View.OnClickListener() {
                             @Override
                             public void onClick(View view)
                             {
                                 Intent groupChatIntent = new Intent(getContext(), GroupeChat2.class);
                                 groupChatIntent.putExtra("groupName" , selectedgrpName);




                                 groupChatIntent.putExtra("GroupeCreatorID" , selectedgrpCreatorId[0]);

                                 groupChatIntent.putExtra("CurrentUserName", CurrentUserName);

                                 startActivity(groupChatIntent);
                             }
                         });
                     }
                 }

                 @Override
                 public void onCancelled(DatabaseError databaseError) {

                 }
             });

         }

         @Override
         public void onCancelled(@NonNull DatabaseError error) {

         }
     });




                    }




    @NonNull
 @Override
 public GroupesViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
 {
     View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.users_display_layout, viewGroup, false);
     return new GroupesViewHolder(view);
 }
                };

        list_viewr.setAdapter(adapterr);
        adapterr.startListening();





    }



    public static class  GroupesViewHolder extends RecyclerView.ViewHolder
    {
        CircleImageView profileImage;
        TextView userStatus, userName;
        ImageView onlineIcon;


        public GroupesViewHolder(@NonNull View itemView)
        {
            super(itemView);

            profileImage = itemView.findViewById(R.id.users_profile_image);
            userStatus = itemView.findViewById(R.id.user_status);
            userName = itemView.findViewById(R.id.user_profile_name);

            onlineIcon = (ImageView) itemView.findViewById(R.id.user_online_status);




        }
    }




}
