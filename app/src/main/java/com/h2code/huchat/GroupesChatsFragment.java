package com.h2code.huchat;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class GroupesChatsFragment extends Fragment





{

    private String CurrentgroupeName , currentUserID , CurrentUserName , CurrentGroupeCreatorId ;

    private Button GoToChat ;
    private View PrivateChatsView;

    private DatabaseReference UsersRef;
    private FirebaseAuth mAuth;



    public GroupesChatsFragment() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        PrivateChatsView = inflater.inflate(R.layout.fragment_groupeschats, container, false);


        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users");


        mAuth = FirebaseAuth.getInstance();

        currentUserID = mAuth.getCurrentUser().getUid();


        GoToChat = PrivateChatsView.findViewById(R.id.GoToChat);

                CurrentgroupeName  = getActivity().getIntent().getExtras().get("groupName").toString();

      //  CurrentGroupeCreatorId = getArguments().getString("GroupeCreatorId");
        GetUserInfo();
        GoToChat.setText(" Start Chating with Friends in \n "  + CurrentgroupeName + " \n Groupe");


        System.out.println("henna henna");
        GoToChat    .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {





                Intent GroupeSChatSActivity = new Intent(getContext(), GroupesAdvancedChatActivity.class);

                GroupeSChatSActivity.putExtra("CurrentgroupeName", CurrentgroupeName);

                GroupeSChatSActivity.putExtra("CurrentUserName", CurrentUserName);


                GroupeSChatSActivity.putExtra("CurrentGroupeCreatorId", CurrentGroupeCreatorId);



                startActivity(GroupeSChatSActivity);

            }});

        return PrivateChatsView;
    }




    private void GetUserInfo() {
        UsersRef.child(currentUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    CurrentUserName = dataSnapshot.child("name").getValue().toString();

                    CurrentGroupeCreatorId = dataSnapshot.child("OwnGrpName").child(CurrentgroupeName).child("GroupeCreator").getValue().toString();


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }








    }