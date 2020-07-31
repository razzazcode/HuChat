package com.h2code.huchat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class GroupesAdvancedChatActivity extends AppCompatActivity {



    private Toolbar GroupeChatToolBar;
    private ImageButton SendMessageButton  , SendFilesButton ;
    private EditText MessageInputText;

    private TextView userName, userLastSeen;
    private CircleImageView userImage;

    private FirebaseAuth mAuth;
    private DatabaseReference UsersRef,  GroupMessageKeyRef
            , groupeRootREF2;

    private String currentGroupName, currentUserID,
            SendertUserName, currentDate,
            currentTime , GroupeCreatorId , GroupeMainImage;


    private final List<Messages> messagesList = new ArrayList<>();
    private LinearLayoutManager linearLayoutManager;
    private GroupesMessageAdapter groupeMessageAdapter;
    private RecyclerView userMessagesList;


    private String saveCurrentTime, saveCurrentDate;

    private String checker="" , myUri="";

    private StorageTask uploadTask;

    private Uri fileUri;
    private ProgressDialog loadingBar;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groupes_advanced_chat);

        System.out.println("henna henna HAHAHAHAHA HAHAAHAHHA HAHAAHAHA");

        currentGroupName = getIntent().getExtras().get("CurrentgroupeName").toString();

        GroupeCreatorId = getIntent().getExtras().get("CurrentGroupeCreatorId").toString();


        SendertUserName = getIntent().getExtras().get("CurrentUserName").toString();

        Toast.makeText(GroupesAdvancedChatActivity.this, currentGroupName, Toast.LENGTH_SHORT).show();

        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();

        groupeRootREF2= FirebaseDatabase.getInstance().getReference()
                .child("GroupesMainActivity")
                .child("Groupes").child(GroupeCreatorId)
                .child(currentGroupName);

        IntializeControllers();

        userName.setText(currentGroupName);
      // Picasso.get().load().placeholder(R.drawable.profile_image).into(userImage);

userImage.setImageResource(R.drawable.profile_image);


        SendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                SendMessage();
            }
        });


        DisplayLastSeen();




        SendFilesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CharSequence options[] = new CharSequence[] {

                        "Image",
                        "PDF Files",
                        "Ms Word Files"
                };


                AlertDialog.Builder builder = new AlertDialog.Builder(GroupesAdvancedChatActivity.this)  ;

                builder.setTitle("Select the File" );

                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {

                        if (i==0){

                            checker = "image";


                            Intent intent = new Intent();


                            intent.setAction(Intent.ACTION_GET_CONTENT);
                            intent.setType("image/*");
                            startActivityForResult(intent.createChooser(intent, "Select image"), 436);


                        }

                        if (i==1){

                            checker="pdf";

                            Intent intent = new Intent();


                            intent.setAction(Intent.ACTION_GET_CONTENT);
                            intent.setType("application/pdf");
                            startActivityForResult(intent.createChooser(intent, "Select pdf file"), 436);






                        }

                        if (i==2){

                            checker = "docx";

                            Intent intent = new Intent();


                            intent.setAction(Intent.ACTION_GET_CONTENT);
                            intent.setType("application/msword");
                            startActivityForResult(intent.createChooser(intent, "Select Ms Word File"), 436);







                        }




                    }
                });

                builder.show();

            }
        });








    }












    private void DisplayLastSeen() {

    }


    private void IntializeControllers() {


        GroupeChatToolBar = (Toolbar) findViewById(R.id.chat_toolbarGCA);
        setSupportActionBar(GroupeChatToolBar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);

        LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View actionBarView = layoutInflater.inflate(R.layout.custom_chat_bar, null);
        actionBar.setCustomView(actionBarView);

        userName = (TextView) findViewById(R.id.custom_profile_name);
        userLastSeen = (TextView) findViewById(R.id.custom_user_last_seen);
        userImage = (CircleImageView) findViewById(R.id.custom_profile_image);

        SendMessageButton = (ImageButton) findViewById(R.id.send_message_btnGCA);
        SendFilesButton = (ImageButton) findViewById(R.id.send_files_btnGCA);
        MessageInputText = (EditText) findViewById(R.id.input_messageGCA);

        groupeMessageAdapter = new GroupesMessageAdapter(messagesList);
        userMessagesList = (RecyclerView) findViewById(R.id.private_messages_list_of_usersGCA);
        linearLayoutManager = new LinearLayoutManager(this);
        userMessagesList.setLayoutManager(linearLayoutManager);
        userMessagesList.setAdapter(groupeMessageAdapter);


        loadingBar = new ProgressDialog(this);




        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("hh:mm a");
        saveCurrentTime = currentTime.format(calendar.getTime());



    }








    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==436  &&  resultCode==RESULT_OK  &&  data!=null)
        {


            loadingBar.setTitle("Sending file ");
            loadingBar.setMessage("Please wait, your File is uploading...");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();




            fileUri = data.getData();



            if (!checker.equals("image")) {


                StorageReference storageReference = FirebaseStorage
                        .getInstance().getReference() .child("GroupesMainActivity")
                        .child("Groupes").child(GroupeCreatorId)
                        .child(currentGroupName).child("DocumentFiles");

                final String messageSenderRef = "Messages/" + currentUserID ;
              //  final String messageReceiverRef = "Messages/" + messageReceiverID + "/" + messageSenderID;

                DatabaseReference userMessageKeyRef = groupeRootREF2.child("Messages")
                       .push();

                final    String messagePushID = userMessageKeyRef.getKey();

                final StorageReference filePath = storageReference
                        .child(messagePushID + "." + checker);



                filePath.putFile(fileUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String downloadUrl = uri.toString();

            Map messageDocsBody = new HashMap();
            messageDocsBody.put("message",downloadUrl);
            messageDocsBody.put("name",fileUri.getLastPathSegment());
            messageDocsBody.put("type",checker);
            messageDocsBody.put("groupeCreatorId", GroupeCreatorId);

            messageDocsBody.put("from",currentUserID);
            messageDocsBody.put("to", currentGroupName);
            messageDocsBody.put("messageID", messagePushID);
            messageDocsBody.put("time", saveCurrentTime);
            messageDocsBody.put("date", saveCurrentDate);


            Map messageBodyDDetail = new HashMap();
            messageBodyDDetail.put("Messages" + "/" + messagePushID, messageDocsBody);
         //   messageBodyDDetail.put(messageReceiverRef + "/" + messagePushID, messageDocsBody);

            groupeRootREF2.updateChildren(messageBodyDDetail);
            loadingBar.dismiss();

        }
    }).addOnFailureListener(new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception e) {
            loadingBar.dismiss();
            Toast.makeText(GroupesAdvancedChatActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    });
                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        double p = (100.0* taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                        loadingBar.setMessage((int) p + " % Uploading...");
                    }
                });




            }

            else if (checker.equals("image")) {



                StorageReference storageReference = FirebaseStorage
                        .getInstance().getReference() .child("GroupesMainActivity")
                        .child("Groupes").child(GroupeCreatorId)
                        .child(currentGroupName).child("ImagesFiles");

                final String messageSenderRef = "Messages/" + currentUserID ;
              //  final String messageReceiverRef = "Messages/" + messageReceiverID + "/" + messageSenderID;

                DatabaseReference userMessageKeyRef = groupeRootREF2.child("Messages")
                        .push();

                final    String messagePushID = userMessageKeyRef.getKey();

                final StorageReference filePath = storageReference
                        .child(messagePushID + "." + "jpg");



                uploadTask = filePath.putFile(fileUri);

                uploadTask.continueWithTask(new Continuation() {
                    @Override
                    public Object then(@NonNull Task task) throws Exception {

                        if (!task.isSuccessful()) {

                            throw task.getException();
                        }

                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {


                        if (task.isSuccessful()) {




   Uri downloadUri = task.getResult();

   myUri = downloadUri.toString();


   Map messageimageBody = new HashMap();
   messageimageBody.put("message", myUri);

   messageimageBody.put("name", fileUri.getLastPathSegment());

         messageimageBody.put("groupeCreatorId", GroupeCreatorId);

   messageimageBody.put("type", checker);
   messageimageBody.put("from", currentUserID);
   messageimageBody.put("to", currentGroupName);
   messageimageBody.put("messageID", messagePushID);
   messageimageBody.put("time", saveCurrentTime);
   messageimageBody.put("date", saveCurrentDate);

   Map messageBodyDetails = new HashMap();
   messageBodyDetails.put("Messages" + "/" + messagePushID, messageimageBody);
  // messageBodyDetails.put( messageReceiverRef + "/" + messagePushID, messageimageBody);

   groupeRootREF2.updateChildren(messageBodyDetails).addOnCompleteListener(new OnCompleteListener() {
              @Override
              public void onComplete(@NonNull Task task)
              {
                  if (task.isSuccessful())
                  {
                      loadingBar.dismiss();

              Toast.makeText(GroupesAdvancedChatActivity.this, "Message Sent Successfully...", Toast.LENGTH_SHORT).show();
          }
          else
          {

              loadingBar.dismiss();



              Toast.makeText(GroupesAdvancedChatActivity.this, "Error", Toast.LENGTH_SHORT).show();
          }
          MessageInputText.setText("");
                                }
                            });



                        }


                    }
                });

            }

            else
            {

                loadingBar.dismiss();

                Toast.makeText(this, "please select option", Toast.LENGTH_SHORT).show();
            }

        }





    }





    @Override
    protected void onStart()
    {
        super.onStart();

        groupeRootREF2.child("Messages")
                .addChildEventListener(new ChildEventListener() {
   @Override
   public void onChildAdded(DataSnapshot dataSnapshot, String s)
   {











           if (!dataSnapshot.hasChild(currentUserID))
           {

      Messages messages = dataSnapshot.getValue(Messages.class);



     messagesList.add(messages);

     groupeMessageAdapter.notifyDataSetChanged();

     userMessagesList.smoothScrollToPosition(userMessagesList
             .getAdapter().getItemCount());

                 }


/*
     Messages messages = dataSnapshot.getValue(Messages.class);



     messagesList.add(messages);

     groupeMessageAdapter.notifyDataSetChanged();

     userMessagesList.smoothScrollToPosition(userMessagesList
             .getAdapter().getItemCount());

     */
 }

 @Override
 public void onChildChanged(DataSnapshot dataSnapshot, String s) {

 }

 @Override
 public void onChildRemoved(DataSnapshot dataSnapshot) {

 }

 @Override
 public void onChildMoved(DataSnapshot dataSnapshot, String s) {

 }

 @Override
 public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }






    private void SendMessage()
    {
        String messageText = MessageInputText.getText().toString();

        if (TextUtils.isEmpty(messageText))
        {
            Toast.makeText(this, "first write your message...", Toast.LENGTH_SHORT).show();
        }
        else
        {
            String messageSenderRef = "Messages/" + currentUserID ;
          //  String messageReceiverRef = "Messages/" + GroupeCreatorId + "/" + messageSenderID;

            DatabaseReference GroupeMessageKeyRef = groupeRootREF2.child("Messages")
                    .push();

            String messagePushID = GroupeMessageKeyRef.getKey();

            Map messageTextBody = new HashMap();
            messageTextBody.put("message", messageText);
            messageTextBody.put("groupeCreatorId", GroupeCreatorId);

      messageTextBody.put("type", "text");

            messageTextBody.put("from", currentUserID);
            messageTextBody.put("to", currentGroupName);
            messageTextBody.put("messageID", messagePushID);
            messageTextBody.put("time", saveCurrentTime);
            messageTextBody.put("date", saveCurrentDate);

            Map messageBodyDetails = new HashMap();
            messageBodyDetails.put("Messages/" + messagePushID, messageTextBody);
          //  messageBodyDetails.put( messageReceiverRef + "/" + messagePushID, messageTextBody);

            groupeRootREF2.updateChildren(messageBodyDetails).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task)
                {
                    if (task.isSuccessful())
                    {
                        Toast.makeText(GroupesAdvancedChatActivity.this, "Message Sent Successfully...", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(GroupesAdvancedChatActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                    MessageInputText.setText("");
                }
            });
        }
    }







}