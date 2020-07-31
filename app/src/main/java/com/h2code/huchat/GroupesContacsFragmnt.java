package com.h2code.huchat;




import android.content.Intent;
import android.os.Bundle;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ImageView;
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
        import com.google.firebase.database.ValueEventListener;
        import com.squareup.picasso.Picasso;

        import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class GroupesContacsFragmnt extends Fragment
{
    private View ContactsView;
    private RecyclerView myContactsList;

    private DatabaseReference ContacsRef, UsersRef , GroupesUsersRef , CurrentGroupeContacsRef;
    private FirebaseAuth mAuth;
    private String currentUserID , CurrentgroupeName , GroupeCreatorID;


    public GroupesContacsFragmnt() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ContactsView = inflater.inflate(R.layout.fragment_contacts, container, false);


        myContactsList = (RecyclerView) ContactsView.findViewById(R.id.contacts_list);
        myContactsList.setLayoutManager(new LinearLayoutManager(getContext()));


        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();


        CurrentgroupeName = getActivity().getIntent().getExtras().get("groupName").toString();
        GroupeCreatorID =  getActivity().getIntent().getExtras().get("GroupeCreatorID").toString();



        GroupesUsersRef = FirebaseDatabase.getInstance().getReference()
                .child("Users").child(currentUserID).child("OwnGrpName")
                .child(CurrentgroupeName);

        CurrentGroupeContacsRef = FirebaseDatabase.getInstance().getReference()
                .child("GroupesMainActivity").child("Groupes")
                .child(GroupeCreatorID).child(CurrentgroupeName)
        .child("ContactsOfTheGroupe");
        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users");







        return ContactsView;
    }


    @Override
    public void onStart()
    {
        super.onStart();

        FirebaseRecyclerOptions options =
                new FirebaseRecyclerOptions.Builder<Contacts>()
                        .setQuery(CurrentGroupeContacsRef, Contacts.class)
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
         if (dataSnapshot.child("userState").hasChild("state"))
         {
             String state = dataSnapshot.child("userState").child("state").getValue().toString();
             String date = dataSnapshot.child("userState").child("date").getValue().toString();
             String time = dataSnapshot.child("userState").child("time").getValue().toString();

             if (state.equals("online"))
             {
                 holder.onlineIcon.setVisibility(View.VISIBLE);
             }
             else if (state.equals("offline"))
             {
                 holder.onlineIcon.setVisibility(View.INVISIBLE);
                                }
     }
     else
     {
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
             Intent GroupchatReq = new Intent(getContext(), GroupesProfileActivity.class);

             GroupchatReq.putExtra("GroupeCreatorId", currentUserID);
             GroupchatReq.putExtra("GroupeName", CurrentgroupeName );
             GroupchatReq.putExtra("visit_user_id", userIDs);


             startActivity(GroupchatReq);
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
