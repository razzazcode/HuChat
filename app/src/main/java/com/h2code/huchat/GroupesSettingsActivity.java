package com.h2code.huchat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class GroupesSettingsActivity extends AppCompatActivity
{
    private Button UpdateAccountSettings;
    private EditText userName, userStatus;
    private CircleImageView userProfileImage;

    private String currentUserID , profilepicLink , gropeName ,CurrentgroupeName ,
            CurrentGroupeCreatorId ,CurrentUserName ;
    private FirebaseAuth mAuth;
    private DatabaseReference RootRef;

    private static final int GalleryPick = 2;
    private StorageReference GroupeProfileImagesRef;
    private ProgressDialog loadingBar;

    private Toolbar SettingsToolBar;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groupes_settings);



        CurrentgroupeName = getIntent().getExtras().get("CurrentgroupeName").toString();


        CurrentGroupeCreatorId = getIntent().getExtras().get("CurrentGroupeCreatorId").toString();


        CurrentUserName = getIntent().getExtras().get("CurrentUserName").toString();





        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        RootRef = FirebaseDatabase.getInstance().getReference() .child("GroupesMainActivity")
                .child("Groupes") . child(CurrentGroupeCreatorId)
                .child(CurrentgroupeName)
        ;


        GroupeProfileImagesRef = FirebaseStorage
                .getInstance().getReference() .child("GroupesMainActivity")
                .child("Groupes") . child(CurrentGroupeCreatorId)
                .child(CurrentgroupeName).child("CurrentGroupeImage")
                ;

        gropeName="";

        InitializeFields();


        userName.setVisibility(View.INVISIBLE);


        UpdateAccountSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                UpdateSettings();
            }
        });


        RetrieveUserInfo();


        userProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GalleryPick);
            }
        });
    }



    private void InitializeFields()
    {
        UpdateAccountSettings = (Button) findViewById(R.id.Groupesupdate_settings_button);
        userName = (EditText) findViewById(R.id.Groupesset_user_name);
        userStatus = (EditText) findViewById(R.id.Groupesset_profile_status);
        userProfileImage = (CircleImageView) findViewById(R.id.Groupesset_profile_image);
        loadingBar = new ProgressDialog(this);

        SettingsToolBar = (Toolbar) findViewById(R.id.Groupessettings_toolbar);
        setSupportActionBar(SettingsToolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setTitle("Groupes Settings");
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==GalleryPick  &&  resultCode==RESULT_OK  &&  data!=null)
        {
            Uri ImageUri = data.getData();

            CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1, 1)
                    .start(this);
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE)
        {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK)
            {
                loadingBar.setTitle("Set Profile Image");
                loadingBar.setMessage("Please wait, your profile image is updating...");
                loadingBar.setCanceledOnTouchOutside(false);
                loadingBar.show();

                Uri resultUri = result.getUri();



                final  StorageReference filePath = GroupeProfileImagesRef.child(currentUserID + ".jpg");

                filePath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task)
                    {
                        if (task.isSuccessful())
       {
           Toast.makeText(GroupesSettingsActivity.this, "Profile Image uploaded Successfully...", Toast.LENGTH_SHORT).show();

           // final String downloaedUrl = task.getResult().getDownloadUrl().toString();




           Task<Uri> urlTask = task.getResult().getStorage().getDownloadUrl();
           while (!urlTask.isSuccessful());
           Uri downloadUrl = urlTask.getResult();
           profilepicLink = downloadUrl.toString();


           //    final String downloadLinkofuploadedfile = filePath.getDownloadUrl().toString();
           //  task.getResult().getMetadata().getReference().getDownloadUrl().toString();

           RootRef.child("CurrentGroupesettings").child("image")
                                    .setValue(profilepicLink)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task)
                                        {
                                            if (task.isSuccessful())
                                            {
                                                Toast.makeText(GroupesSettingsActivity.this, "Image save in Database, Successfully...", Toast.LENGTH_SHORT).show();
                                                loadingBar.dismiss();
                                            }
                                            else
                                            {
                                                String message = task.getException().toString();
                                                Toast.makeText(GroupesSettingsActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                                                loadingBar.dismiss();
                                            }
                                        }
                                    });
                        }
                        else
                        {
                            String message = task.getException().toString();
                            Toast.makeText(GroupesSettingsActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();
                        }
                    }
                });
            }
        }
    }




    private void UpdateSettings()
    {
        String setUserName = userName.getText().toString();
        String setStatus = userStatus.getText().toString();

        if (TextUtils.isEmpty(setUserName))
        {
            Toast.makeText(this, "Please write your user name first....", Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(setStatus))
        {
            Toast.makeText(this, "Please write your status....", Toast.LENGTH_SHORT).show();
        }
        else
        {
            HashMap<String, Object> profileMap = new HashMap<>();
            profileMap.put("uid", currentUserID + gropeName);
            profileMap.put("name", setUserName);
            profileMap.put("status", setStatus);
            RootRef.child("CurrentGroupesettings").updateChildren(profileMap)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task)
                        {
                            if (task.isSuccessful())
                            {
                                Toast.makeText(GroupesSettingsActivity.this, "Profile Updated Successfully...", Toast.LENGTH_SHORT).show();


                  //               SendUserToMainActivity();
                            }
                            else
                            {
                                String message = task.getException().toString();
                                Toast.makeText(GroupesSettingsActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }



    private void RetrieveUserInfo()
    {
        RootRef.child("CurrentGroupesettings")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot)
                    {
        if ((dataSnapshot.exists()) && (dataSnapshot.hasChild("name") && (dataSnapshot.hasChild("image"))))
        {
            String retrieveUserName = dataSnapshot.child("name").getValue().toString();
            String retrievesStatus = dataSnapshot.child("status").getValue().toString();
            String retrieveProfileImage = dataSnapshot.child("image").getValue().toString();

            userName.setText(retrieveUserName);
            userStatus.setText(retrievesStatus);
            Picasso.get().load(retrieveProfileImage).into(userProfileImage);




            //  Picasso.with(Settingsactivity.this).load(retrieveProfileImage).into(userProfileImage);






                        }
                        else if ((dataSnapshot.exists()) && (dataSnapshot.hasChild("name")))
                        {
                            String retrieveUserName = dataSnapshot.child("name").getValue().toString();
                            String retrievesStatus = dataSnapshot.child("status").getValue().toString();

                            userName.setText(retrieveUserName);
                            userStatus.setText(retrievesStatus);
                        }
                        else
                        {
                            userName.setVisibility(View.VISIBLE);
                            Toast.makeText(GroupesSettingsActivity.this, "Please set & update your profile information...", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }



    private void SendUserToMainActivity()
    {
        Intent groupChatIntent = new Intent(GroupesSettingsActivity.this, GroupeChat2.class);

        groupChatIntent.putExtra("groupName" , CurrentgroupeName);


        groupChatIntent.putExtra("GroupeCreatorID" , CurrentGroupeCreatorId);


        groupChatIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(groupChatIntent);
        finish();
    }
}
