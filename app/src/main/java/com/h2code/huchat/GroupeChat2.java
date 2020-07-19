package com.h2code.huchat;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
//import com.onesignal.OneSignal;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class GroupeChat2 extends AppCompatActivity
{
    private Toolbar mToolbar;
    private ViewPager myViewPager;
    private TabLayout myTabLayout;
    private GroupesTabAccessorAdapter myTabsAccessorAdapter;

    private FirebaseAuth mAuth;
    private DatabaseReference GroupeRootRef , RootRef , UserGroupesRef;
    private String currentUserID , CurrentgroupeName , CurrentGroupeCreatorId;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groupe_chat2);

/*
FirebaseApp.initializeApp(MainActivity.this);


        OneSignal.startInit(this).init();
        OneSignal.setSubscription(true);
        OneSignal.idsAvailable(new OneSignal.IdsAvailableHandler() {
            @Override
            public void idsAvailable(String userId, String registrationId) {
                FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getUid()).child("notificationKey").setValue(userId);
            }
        });
        OneSignal.setInFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification);

*/



        CurrentgroupeName = getIntent().getExtras().get("groupName").toString();





        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        RootRef = FirebaseDatabase.getInstance().getReference();

        GroupeRootRef = FirebaseDatabase.getInstance().getReference()
                .child("GroupesMainActivity").child("Groupes") ;


        UserGroupesRef = FirebaseDatabase.getInstance().getReference()
                .child("Users").child(currentUserID).child("OwnGrpName");



        GettingGroupeCreatorId();



        mToolbar = (Toolbar) findViewById(R.id.main_page_toolbarG);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Hu Chat Groupes");


        myViewPager = (ViewPager) findViewById(R.id.main_tabs_pagerG);
        myTabsAccessorAdapter = new GroupesTabAccessorAdapter(getSupportFragmentManager());
        myViewPager.setAdapter(myTabsAccessorAdapter);


        myTabLayout = (TabLayout) findViewById(R.id.main_tabsG);
        myTabLayout.setupWithViewPager(myViewPager);
    }

    private void GettingGroupeCreatorId() {


        UserGroupesRef.child(CurrentgroupeName).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.hasChild("GroupeCreatorId")) {

                    CurrentGroupeCreatorId = dataSnapshot.child("GroupeCreatorId")
                            .getValue().toString();

                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


    @Override
    protected void onStart()
    {
        super.onStart();

        updateUserStatus("online");

    }


    @Override
    protected void onStop()
    {
        super.onStop();


    }



    @Override
    protected void onDestroy()
    {
        super.onDestroy();


    }






    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        super.onCreateOptionsMenu(menu);

      //  if (currentUserID == CurrentGroupeCreatorId ) {

            getMenuInflater().inflate(R.menu.groupes_menu, menu);

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        super.onOptionsItemSelected(item);

        if (item.getItemId() == R.id.Groupes_logout_option)
        {
            // OneSignal.setSubscription(false);


            updateUserStatus("offline");
            mAuth.signOut();
            SendUserToLoginActivity();
        }
        if (item.getItemId() == R.id.Groupes_settings_option)
        {
            SendUserToSettingsActivity();
        }

        if (item.getItemId() == R.id.Groupes_find_Groupes_option)
        {
            SendUserToGroupeFindFriendsActivity();
        }

        return true;
    }









    private void SendUserToLoginActivity()
    {
        Intent loginIntent = new Intent(GroupeChat2.this, LoginActivity.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);

    }

    private void SendUserToSettingsActivity()
    {
        Intent settingsIntent = new Intent(GroupeChat2.this, GroupesSettingsActivity.class);
        startActivity(settingsIntent);
    }


    private void SendUserToGroupeFindFriendsActivity()
    {
        Intent findFriendsIntent = new Intent(GroupeChat2.this, GroupeFindFriendsActivity.class);
        startActivity(findFriendsIntent);
    }



    private void updateUserStatus(String state)
    {
        String saveCurrentTime, saveCurrentDate;

        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("hh:mm a");
        saveCurrentTime = currentTime.format(calendar.getTime());

        HashMap<String, Object> onlineStateMap = new HashMap<>();
        onlineStateMap.put("time", saveCurrentTime);
        onlineStateMap.put("date", saveCurrentDate);
        onlineStateMap.put("state", state);

        RootRef.child("Users").child(currentUserID).child("userState")
                .updateChildren(onlineStateMap);

    }
}
