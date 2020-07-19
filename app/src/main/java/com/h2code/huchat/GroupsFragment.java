package com.h2code.huchat;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


/**
 * A simple {@link Fragment} subclass.
 */
public class GroupsFragment extends Fragment
{
    private View groupFragmentView;
    private ListView list_view;
    private ArrayAdapter<String> arrayAdapter;
    private ArrayList<String> list_of_groups = new ArrayList<>();

    private Button  createNewGroupe;

    private DatabaseReference GroupRef , groupeRootREF2 , UserGroupesRef , groupeCreatorId;

    private String currentUserID;
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



        GroupRef = FirebaseDatabase.getInstance().getReference().child("Groups");

        groupeRootREF2= FirebaseDatabase.getInstance().getReference()
                .child("GroupesMainActivity").child("Groupes");


        UserGroupesRef = FirebaseDatabase.getInstance().getReference()
                .child("Users").child(currentUserID).child("OwnGrpName");



        IntializeFields();


        RetrieveAndDisplayGroups();






        createNewGroupe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                RequestNewGroup();


            }
        });





        list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id)
            {
                String currentGroupName = adapterView.getItemAtPosition(position).toString();

                Intent groupChatIntent = new Intent(getContext(), GroupeChat2.class);
                groupChatIntent.putExtra("groupName" , currentGroupName);
              //  groupChatIntent.putExtra("groupCreatorName" , groupeCreatorId);

                startActivity(groupChatIntent);
            }
        });


        return groupFragmentView;
    }



    private void IntializeFields()
    {
        list_view = (ListView) groupFragmentView.findViewById(R.id.list_view);
        arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, list_of_groups);
        list_view.setAdapter(arrayAdapter);


        createNewGroupe= groupFragmentView.findViewById(R.id.createnewgroupe);



    }




    private void RetrieveAndDisplayGroups()
    {
        groupeRootREF2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                Set<String> set = new HashSet<>();
                Iterator iterator = dataSnapshot.getChildren().iterator();

                while (iterator.hasNext())
                {
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


         groupeRootREF2.child(currentUserID).child(groupName).child("GroupeName").setValue(groupName);

        groupeRootREF2.child(currentUserID).child(groupName).child("GroupeCreator")
                .setValue(currentUserID)


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


        UserGroupesRef.child(groupName).child("GroupeName").setValue(groupName);
        UserGroupesRef.child(groupName).child("GroupeCreator").setValue(currentUserID);

    }



}
