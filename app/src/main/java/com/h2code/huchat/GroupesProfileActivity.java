package com.h2code.huchat;





import android.os.Bundle;
        import android.view.View;
        import android.widget.Button;
        import android.widget.TextView;

        import androidx.annotation.NonNull;
        import androidx.appcompat.app.AppCompatActivity;

        import com.google.android.gms.tasks.OnCompleteListener;
        import com.google.android.gms.tasks.Task;
        import com.google.firebase.auth.FirebaseAuth;
        import com.google.firebase.database.DataSnapshot;
        import com.google.firebase.database.DatabaseError;
        import com.google.firebase.database.DatabaseReference;
        import com.google.firebase.database.FirebaseDatabase;
        import com.google.firebase.database.ValueEventListener;
        import com.squareup.picasso.Picasso;

        import java.util.HashMap;

        import de.hdodenhof.circleimageview.CircleImageView;

public class GroupesProfileActivity extends AppCompatActivity
{
    private String receiverUserID, senderUserID, Current_State
         , GroupeCreatorImagePath
            , RecieverImagePath , ReacieverUserName, GroupeName ,GroupeCreatorId , GroupeCreatorUserName;

    private CircleImageView userProfileImage;
    private TextView userProfileName, userProfileStatus;
    private Button SendGroupeRequestButton, DeclineMessageRequestButton;

    private DatabaseReference UserRef, NewUserGroupesRef, ChatRequestsRef,GroupesContactsOfAgroupetRef, ContactsRef, GroupesNotificationRef, GroupesRequestsFragmentRef;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groupes_profile);


        mAuth = FirebaseAuth.getInstance();
        UserRef = FirebaseDatabase.getInstance().getReference().child("Users");
        ChatRequestsRef = FirebaseDatabase.getInstance().getReference().child("Chat Requests");
        ContactsRef = FirebaseDatabase.getInstance().getReference().child("Contacts");
        GroupesNotificationRef = FirebaseDatabase.getInstance().getReference()
                .child("GroupesMainActivity").child("Groupes Notifications");

        GroupeCreatorId = getIntent().getExtras().get("GroupeCreatorId").toString();

        receiverUserID = getIntent().getExtras().get("visit_user_id").toString();
        senderUserID = mAuth.getCurrentUser().getUid();
        GroupeName = getIntent().getExtras().get("GroupeName").toString();


        GroupesRequestsFragmentRef = FirebaseDatabase.getInstance().getReference()
                .child("GroupesMainActivity")
               .child("GroupesRequests") ;

        GroupesContactsOfAgroupetRef = FirebaseDatabase.getInstance().getReference()
                .child("GroupesMainActivity").child("Groupes")
                .child(senderUserID)
                .child(GroupeName).child("ContactsOfTheGroupe");


        NewUserGroupesRef = FirebaseDatabase.getInstance().getReference()
                .child("Users").child(receiverUserID).child("OwnGrpName");





        userProfileImage = (CircleImageView) findViewById(R.id.visit_profile_imageG);
        userProfileName = (TextView) findViewById(R.id.visit_user_nameG);
        userProfileStatus = (TextView) findViewById(R.id.visit_profile_statusG);
        SendGroupeRequestButton = (Button) findViewById(R.id.send_message_request_buttonG);
        DeclineMessageRequestButton = (Button) findViewById(R.id.decline_message_request_buttonG);
        Current_State = "new";

InfoOfCreator();
        RetrieveUserInfo();
    }

    private void InfoOfCreator() {



        UserRef.child(senderUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                if ((dataSnapshot.exists())  &&  (dataSnapshot.hasChild("image")))
                {
                    GroupeCreatorImagePath = dataSnapshot.child("image").getValue().toString();
                    GroupeCreatorUserName = dataSnapshot.child("name").getValue().toString();
                    String userstatus = dataSnapshot.child("status").getValue().toString();

                }
                else
                {
                    GroupeCreatorUserName = dataSnapshot.child("name").getValue().toString();
                    String userstatus = dataSnapshot.child("status").getValue().toString();





                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




    }

    private void RetrieveUserInfo()
    {
        UserRef.child(receiverUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                if ((dataSnapshot.exists())  &&  (dataSnapshot.hasChild("image")))
                {
                    RecieverImagePath = dataSnapshot.child("image").getValue().toString();
                     ReacieverUserName = dataSnapshot.child("name").getValue().toString();
                    String userstatus = dataSnapshot.child("status").getValue().toString();



                    Picasso.get().load(RecieverImagePath).placeholder(R.drawable.profile_image).into(userProfileImage);
                    userProfileName.setText(GroupeCreatorUserName);
                    userProfileStatus.setText(userstatus);


                    ManageChatRequests();
                }
                else
                {
                    ReacieverUserName = dataSnapshot.child("name").getValue().toString();
                    String userstatus = dataSnapshot.child("status").getValue().toString();

                    userProfileName.setText(ReacieverUserName);
                    userProfileStatus.setText(userstatus);


                    ManageChatRequests();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }




    private void ManageChatRequests()
    {
        GroupesRequestsFragmentRef.child(GroupeName).child(senderUserID)
                .addValueEventListener(new ValueEventListener() {
                    @Override
   public void onDataChange(DataSnapshot dataSnapshot)
   {
       if (dataSnapshot.hasChild(receiverUserID))
       {
           String request_type = dataSnapshot.child(receiverUserID).child("request_type").getValue().toString();

    if (request_type.equals("sent"))
    {
        Current_State = "request_sent";
        SendGroupeRequestButton.setText("Cancel Chat Request");
    }
    else if (request_type.equals("received"))
    {
        Current_State = "request_received";
                         SendGroupeRequestButton.setText("Accept Chat Request");

      DeclineMessageRequestButton.setVisibility(View.VISIBLE);
      DeclineMessageRequestButton.setEnabled(true);

      DeclineMessageRequestButton.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view)
          {
              CancelGrouopeRequest();
          }
      });
                            }
                        }
                        else
                        {
       GroupesContactsOfAgroupetRef
               .addListenerForSingleValueEvent(new ValueEventListener() {
              @Override
              public void onDataChange(DataSnapshot dataSnapshot)
              {
      if (dataSnapshot.hasChild(receiverUserID))
      {
          Current_State = "friends";
          SendGroupeRequestButton.setText("Remove this Contact");
      }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
                                    });
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });



        if (!senderUserID.equals(receiverUserID))
        {

            SendGroupeRequestButton.setText("Send Groupe Invitation");

            SendGroupeRequestButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    SendGroupeRequestButton.setEnabled(false);

                    if (Current_State.equals("new"))
                    {
                        SendGroupeChatRequest();


                    }
                    if (Current_State.equals("request_sent"))
                    {
                        CancelGrouopeRequest();
                    }
                    if (Current_State.equals("request_received"))
                    {
                        AcceptChatRequest();
                    }
                    if (Current_State.equals("friends"))
                    {
                        RemoveSpecificContact();
                    }
                }
            });
        }
        else
        {
            SendGroupeRequestButton.setVisibility(View.INVISIBLE);
        }
    }



    private void RemoveSpecificContact()
    {

        NewUserGroupesRef.child(GroupeName).removeValue();

        GroupesContactsOfAgroupetRef.child(receiverUserID)
                .removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {

       if (task.isSuccessful())
       {
           SendGroupeRequestButton.setEnabled(true);
           Current_State = "new";
           SendGroupeRequestButton.setText("Send Groupe Invitation");

           DeclineMessageRequestButton.setVisibility(View.INVISIBLE);
           DeclineMessageRequestButton.setEnabled(false);
                      }

                    }
                });
    }



    private void AcceptChatRequest()
    {
        GroupesContactsOfAgroupetRef.child(receiverUserID)
                .setValue("Saved")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {

                   if (task.isSuccessful())
                   {
                  GroupesRequestsFragmentRef.child(GroupeName).
                    child(senderUserID).child(receiverUserID)
   .removeValue()
   .addOnCompleteListener(new OnCompleteListener<Void>() {
       @Override
       public void onComplete(@NonNull Task<Void> task)
       {
           if (task.isSuccessful())
                                                  {
                     GroupesRequestsFragmentRef.child(receiverUserID)
                             .child(GroupeName)
        .removeValue()
        .addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {
       SendGroupeRequestButton.setEnabled(true);
       Current_State = "friends";
       SendGroupeRequestButton.setText("Remove this Contact From Grouope" + GroupeName);

       DeclineMessageRequestButton.setVisibility(View.INVISIBLE);
       DeclineMessageRequestButton.setEnabled(false);

       NewUserGroupesRef.child(GroupeName).child("GroupeNameValue").setValue(GroupeName);

       NewUserGroupesRef.child(GroupeName).child("GroupeCreator").setValue(senderUserID);



                                    }
                                });
                    }
                                                   }
                                               });
                                   }

                    }




                });
    }




    private void CancelGrouopeRequest()
    {
        GroupesRequestsFragmentRef.child(GroupeName).child(senderUserID).child(receiverUserID)
                .removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        if (task.isSuccessful())
    {
        GroupesRequestsFragmentRef.child(receiverUserID).child(GroupeName)
                .removeValue()
   .addOnCompleteListener(new OnCompleteListener<Void>() {
       @Override
       public void onComplete(@NonNull Task<Void> task)
       {
           if (task.isSuccessful())
           {
               SendGroupeRequestButton.setEnabled(true);
               Current_State = "new";
       SendGroupeRequestButton.setText("Send Groupe invitation");

       DeclineMessageRequestButton.setVisibility(View.INVISIBLE);
       DeclineMessageRequestButton.setEnabled(false);
                               }
                           }
                       });
           }
                    }
                });
    }




    private void SendGroupeChatRequest()
    {
        GroupesRequestsFragmentRef.child(GroupeName).child(senderUserID)
                .child(receiverUserID)
                .child("request_type").setValue("sent")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        if (task.isSuccessful())
     {



         GroupesRequestsFragmentRef.child(receiverUserID)
                 .child(GroupeName).child("GroupeCreatorImagePath").setValue(GroupeCreatorImagePath);

         GroupesRequestsFragmentRef.child(receiverUserID)
                 .child(GroupeName).child("GroupeCreator").setValue(senderUserID);

         GroupesRequestsFragmentRef.child(receiverUserID)
                 .child(GroupeName).child("GroupeName").setValue(GroupeName);

         GroupesRequestsFragmentRef.child(receiverUserID)
                 .child(GroupeName).child("GroupeCreatorUserName").setValue(GroupeCreatorUserName);

         GroupesRequestsFragmentRef.child(receiverUserID)
                 .child(GroupeName)
                 .child("request_type").setValue("received")
    .addOnCompleteListener(new OnCompleteListener<Void>() {
        @Override
        public void onComplete(@NonNull Task<Void> task)
        {
            if (task.isSuccessful())
            {
         HashMap<String, String> GroupechatNotificationMap = new HashMap<>();
                GroupechatNotificationMap.put("from", senderUserID);
                GroupechatNotificationMap.put("type", "request");

                GroupechatNotificationMap.put("groupeName" , GroupeName );



                GroupesNotificationRef.child(receiverUserID).child(GroupeName).push()
                     .setValue(GroupechatNotificationMap)
                     .addOnCompleteListener(new OnCompleteListener<Void>() {
          @Override
          public void onComplete(@NonNull Task<Void> task)
          {
              if (task.isSuccessful())
                                                 {
            SendGroupeRequestButton.setEnabled(true);
            Current_State = "request_sent";
            SendGroupeRequestButton.setText("Cancel Grouope Invitition ");
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
