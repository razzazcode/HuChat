package com.h2code.huchat;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder>
{
    private List<Messages> userMessagesList;
    private FirebaseAuth mAuth;
    private DatabaseReference usersRef;


    public MessageAdapter (List<Messages> userMessagesList)
    {
        this.userMessagesList = userMessagesList;
    }



    public class MessageViewHolder extends RecyclerView.ViewHolder
    {
        public TextView senderMessageText, receiverMessageText;
        public CircleImageView receiverProfileImage;
        public ImageView messageSenderPicture, messageReceiverPicture;
public Button start , pause  ;

        public MessageViewHolder(@NonNull View itemView)
        {
            super(itemView);



            start = itemView.findViewById(R.id.start);

            pause = itemView.findViewById(R.id.pause);



            senderMessageText =  itemView.findViewById(R.id.sender_messsage_text);
            receiverMessageText =  itemView.findViewById(R.id.receiver_message_text);
            receiverProfileImage =  itemView.findViewById(R.id.message_profile_image);
            messageReceiverPicture = itemView.findViewById(R.id.message_receiver_image_view);
            messageSenderPicture = itemView.findViewById(R.id.message_sender_image_view);
        }
    }




    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
    {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.custom_messages_layout, viewGroup, false);

        mAuth = FirebaseAuth.getInstance();

        return new MessageViewHolder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull final MessageViewHolder messageViewHolder, final int i)
    {
        String messageSenderId = mAuth.getCurrentUser().getUid();
        Messages messages = userMessagesList.get(i);

        String fromUserID = messages.getFrom();
        String fromMessageType = messages.getType();

        usersRef = FirebaseDatabase.getInstance().getReference()
                .child("Users").child(fromUserID);

        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.hasChild("image"))
                {
                    String receiverImage = dataSnapshot.child("image").getValue().toString();

                    Picasso.get().load(receiverImage).placeholder(R.drawable.profile_image).into(messageViewHolder.receiverProfileImage);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        messageViewHolder.start.setVisibility(View.GONE);
        messageViewHolder.pause.setVisibility(View.GONE);


        messageViewHolder.receiverMessageText.setVisibility(View.GONE);
        messageViewHolder.receiverProfileImage.setVisibility(View.GONE);
        messageViewHolder.senderMessageText.setVisibility(View.GONE);
        messageViewHolder.messageSenderPicture.setVisibility(View.GONE);
        messageViewHolder.messageReceiverPicture.setVisibility(View.GONE);


        if (fromMessageType.equals("text"))
        {
            if (fromUserID.equals(messageSenderId))
            {
                messageViewHolder.senderMessageText.setVisibility(View.VISIBLE);

             //   messageViewHolder.senderMessageText.setBackgroundResource(R.drawable.sender_messages_layout);
                messageViewHolder.senderMessageText.setTextColor(Color.BLACK);
                messageViewHolder.senderMessageText
                        .setText(messages.getMessage() + "\n \n" + messages.getTime() + " - " + messages.getDate());
            }
            else
            {
                messageViewHolder.receiverProfileImage.setVisibility(View.VISIBLE);
                messageViewHolder.receiverMessageText.setVisibility(View.VISIBLE);

              //  messageViewHolder.receiverMessageText.setBackgroundResource(R.drawable.receiver_messages_layout);
                messageViewHolder.receiverMessageText.setTextColor(Color.BLACK);
                messageViewHolder.receiverMessageText.setText(messages.getMessage() + "\n \n" + messages.getTime() + " - " + messages.getDate());
            }
        }

        else         if (fromMessageType.equals("image"))
        {
  if (fromUserID.equals(messageSenderId)){

      messageViewHolder.messageSenderPicture.setVisibility(View.VISIBLE);

    Picasso.get()
  .load(messages.getMessage())
  .into(messageViewHolder.messageSenderPicture);

            }

            else {


                messageViewHolder.receiverProfileImage.setVisibility(View.VISIBLE);

                messageViewHolder.messageReceiverPicture.setVisibility(View.VISIBLE);


                Picasso.get()
                        .load(messages.getMessage())
                        .into(messageViewHolder.messageReceiverPicture);




            }
        }





        else         if (fromMessageType.equals("audio"))
        {
            if (fromUserID.equals(messageSenderId)){





                final   MediaPlayer mediaPlayer = new MediaPlayer();
                try {
                    mediaPlayer.setDataSource(userMessagesList.get(i).getMessage());

                    mediaPlayer.prepare();

                }
                catch (IOException e){

                    e.printStackTrace();
                }




                messageViewHolder.start.setVisibility(View.VISIBLE);

                messageViewHolder.start.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mediaPlayer.start();

                    }
                });




                messageViewHolder.pause.setVisibility(View.VISIBLE);

                messageViewHolder.pause.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mediaPlayer.pause();

                    }
                });





            }

            else {

                final   MediaPlayer mediaPlayer = new MediaPlayer();
                try {
                    mediaPlayer.setDataSource(userMessagesList.get(i).getMessage());

                    mediaPlayer.prepare();

                }
                catch (IOException e){

                    e.printStackTrace();
                }




                messageViewHolder.start.setVisibility(View.VISIBLE);

                messageViewHolder.start.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mediaPlayer.start();

                    }
                });




                messageViewHolder.pause.setVisibility(View.VISIBLE);

                messageViewHolder.pause.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mediaPlayer.pause();

                    }
                });




            }
        }








        else    if (fromMessageType.equals("pdf") ||   (fromMessageType.equals("docx")) )

        {

            if (fromUserID.equals(messageSenderId)){

                messageViewHolder.messageSenderPicture.setVisibility(View.VISIBLE);

Picasso.get()
        .load("https://firebasestorage.googleapis.com/v0/b/huchat-7011f.appspot.com/o/ImagesFiles%2Ffile.png?alt=media&token=d095da0d-029b-438e-9fc7-ed704e63d07d")
        .into(messageViewHolder.messageSenderPicture);



            }

            else {


                messageViewHolder.receiverProfileImage.setVisibility(View.VISIBLE);

                messageViewHolder.messageReceiverPicture.setVisibility(View.VISIBLE);


                Picasso.get()
                        .load("https://firebasestorage.googleapis.com/v0/b/huchat-7011f.appspot.com/o/ImagesFiles%2Ffile.png?alt=media&token=d095da0d-029b-438e-9fc7-ed704e63d07d")
                        .into(messageViewHolder.messageReceiverPicture);






            }

        }


        if(fromUserID.equals(messageSenderId)){

            messageViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


     if (userMessagesList.get(i).getType().equals("pdf")
             || userMessagesList.get(i).getType().equals("docx") ){

CharSequence options [] = new CharSequence[] {

        "Delete For Me Only" ,
        "Download and View Document" ,

        " Cancel " ,

        " Delete For Every One "

};


       AlertDialog.Builder builder = new AlertDialog.Builder(messageViewHolder.itemView.getContext());

       builder.setTitle("Delete Message");

       builder.setItems(options, new DialogInterface.OnClickListener() {
           @Override
           public void onClick(DialogInterface dialog, int which) {


               if (which == 0){


deleteSentMessages(i , messageViewHolder);



      Intent intent = new Intent(messageViewHolder.itemView.getContext(),  MainActivity.class);


      messageViewHolder.itemView.getContext().startActivity(intent);





               }

               else  if (which == 1) {


                   Intent intent = new Intent(Intent.ACTION_VIEW
                           , Uri.parse(userMessagesList.get(i).getMessage()));

                   messageViewHolder.itemView.getContext().startActivity(intent);




               }



               else  if (which == 3) {


                   deletetMessagesForAll(i , messageViewHolder);


                   Intent intent = new Intent(messageViewHolder.itemView.getContext(),  MainActivity.class);


                   messageViewHolder.itemView.getContext().startActivity(intent);

               }


           }
       });


builder.show();



                  }


           else          if (userMessagesList.get(i).getType().equals("text") ){

    CharSequence options [] = new CharSequence[] {

            "Delete For Me Only" ,

            " Cancel " ,

            " Delete For Every One "

    };


    AlertDialog.Builder builder = new AlertDialog.Builder(messageViewHolder.itemView.getContext());

    builder.setTitle("Delete Message");

    builder.setItems(options, new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {


     if (which == 0){

         deleteSentMessages(i , messageViewHolder);


         Intent intent = new Intent(messageViewHolder.itemView.getContext(),  MainActivity.class);


         messageViewHolder.itemView.getContext().startActivity(intent);


     }

  

     else  if (which == 2) {


         deletetMessagesForAll(i , messageViewHolder);


         Intent intent = new Intent(messageViewHolder.itemView.getContext(),  MainActivity.class);


         messageViewHolder.itemView.getContext().startActivity(intent);

     }




        }
    });


    builder.show();



                    }





           else          if (userMessagesList.get(i).getType().equals("image") ){

                        CharSequence options [] = new CharSequence[] {

                "Delete For Me Only" ,
                "Download and View Image" ,

                " Cancel " ,

                " Delete For Every One "

        };


        AlertDialog.Builder builder = new AlertDialog.Builder(messageViewHolder.itemView.getContext());

        builder.setTitle("Delete Message");

                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
  public void onClick(DialogInterface dialog, int which) {


      if (which == 0){

          deleteSentMessages(i , messageViewHolder);


          Intent intent = new Intent(messageViewHolder.itemView.getContext(),  MainActivity.class);


          messageViewHolder.itemView.getContext().startActivity(intent);


      }

      else  if (which == 1) {

          Intent intent = new Intent(messageViewHolder.itemView.getContext(),  imageViewerActivity.class);

          intent.putExtra("url" , userMessagesList.get(i).getMessage());

          messageViewHolder.itemView.getContext().startActivity(intent);


      }



      else  if (which == 3) {


          deletetMessagesForAll(i , messageViewHolder);



          Intent intent = new Intent(messageViewHolder.itemView.getContext(),  MainActivity.class);


          messageViewHolder.itemView.getContext().startActivity(intent);



      }


                            }
                        });


                        builder.show();



                    }





     else          if (userMessagesList.get(i).getType().equals("audio") ){

         CharSequence options [] = new CharSequence[] {

                 "Delete For Me Only" ,
                 "Download and View audio" ,

                 " Cancel " ,

                 " Delete For Every One "

         };


         AlertDialog.Builder builder = new AlertDialog.Builder(messageViewHolder.itemView.getContext());

         builder.setTitle("Delete Message");

         builder.setItems(options, new DialogInterface.OnClickListener() {
             @Override
             public void onClick(DialogInterface dialog, int which) {


    if (which == 0){

        deleteSentMessages(i , messageViewHolder);


    Intent intent = new Intent(messageViewHolder.itemView.getContext(),  MainActivity.class);


    messageViewHolder.itemView.getContext().startActivity(intent);


    }

                 else  if (which == 1) {

   //   Intent intent = new Intent(messageViewHolder.itemView.getContext(),  imageViewerActivity.class);

    //  intent.putExtra("url" , userMessagesList.get(i).getMessage());

     // messageViewHolder.itemView.getContext().startActivity(intent);
     //   playSong(userMessagesList.get(i).getMessage());



                 }



                 else  if (which == 3) {


                     deletetMessagesForAll(i , messageViewHolder);



                     Intent intent = new Intent(messageViewHolder.itemView.getContext(),  MainActivity.class);


                     messageViewHolder.itemView.getContext().startActivity(intent);



                 }


             }
         });


         builder.show();



     }


















                }
            });

        }






        else {


            messageViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


    if (userMessagesList.get(i).getType().equals("pdf")
            || userMessagesList.get(i).getType().equals("docx") ){

        CharSequence options [] = new CharSequence[] {

                "Delete For Me Only" ,
                "Download and View Document" ,

                " Cancel "


        };


        AlertDialog.Builder builder = new AlertDialog.Builder(messageViewHolder.itemView.getContext());

        builder.setTitle("Delete Message");

        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


                if (which == 0){

           deleteRecievedtMessages(i , messageViewHolder);


                    Intent intent = new Intent(messageViewHolder.itemView.getContext(),  MainActivity.class);


                    messageViewHolder.itemView.getContext().startActivity(intent);


       }

       else  if (which == 1) {


           Intent intent = new Intent(Intent.ACTION_VIEW
                   , Uri.parse(userMessagesList.get(i).getMessage()));

                    messageViewHolder.itemView.getContext().startActivity(intent);




        }






            }
        });


        builder.show();



    }


    else          if (userMessagesList.get(i).getType().equals("text") ){

        CharSequence options [] = new CharSequence[] {

                "Delete For Me Only" ,

                " Cancel "


     };


     AlertDialog.Builder builder = new AlertDialog.Builder(messageViewHolder.itemView.getContext());

     builder.setTitle("Delete Message");

     builder.setItems(options, new DialogInterface.OnClickListener() {
         @Override
         public void onClick(DialogInterface dialog, int which) {


             if (which == 0){

         deleteRecievedtMessages(i , messageViewHolder);


                 Intent intent = new Intent(messageViewHolder.itemView.getContext(),  MainActivity.class);


                 messageViewHolder.itemView.getContext().startActivity(intent);


     }








                 }
             });


             builder.show();



         }





         else          if (userMessagesList.get(i).getType().equals("image") ){

             CharSequence options [] = new CharSequence[] {

                     "Delete For Me Only" ,
                     "Download and View Image" ,

                     " Cancel "


                        };


                        AlertDialog.Builder builder = new AlertDialog.Builder(messageViewHolder.itemView.getContext());

    builder.setTitle("Delete Message");

    builder.setItems(options, new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {


            if (which == 0){


        deleteRecievedtMessages(i , messageViewHolder);



                Intent intent = new Intent(messageViewHolder.itemView.getContext(),  MainActivity.class);


                messageViewHolder.itemView.getContext().startActivity(intent);



    }

    else  if (which == 1) {



        Intent intent = new Intent(messageViewHolder.itemView.getContext(),  imageViewerActivity.class);

        intent.putExtra("url" , userMessagesList.get(i).getMessage());

messageViewHolder.itemView.getContext().startActivity(intent);




    }


        }
                        });


                        builder.show();



                    }




                }
            });





        }





    }




    @Override
    public int getItemCount()
    {
        return userMessagesList.size();
    }



    private void deleteSentMessages(final int i , final MessageViewHolder holder)
    {

        DatabaseReference rootref = FirebaseDatabase.getInstance().getReference();
        rootref.child("Messages").child(userMessagesList.get(i).getFrom())
                .child(userMessagesList.get(i).getTo())
                .child(userMessagesList.get(i).getMessageID())
                .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {


if (task.isSuccessful())
{
    Toast.makeText(holder.itemView.getContext()
            ,"Message Deleted" , Toast.LENGTH_SHORT);



}

else {

    Toast.makeText(holder.itemView.getContext()
            ,"Error Deleting Message" , Toast.LENGTH_SHORT);

}


            }
        });

    }





    private void deleteRecievedtMessages(final int i , final MessageViewHolder holder)
    {

        DatabaseReference rootref = FirebaseDatabase.getInstance().getReference();
        rootref.child("Messages").child(userMessagesList.get(i).getTo())
                .child(userMessagesList.get(i).getFrom())
                .child(userMessagesList.get(i).getMessageID())
                .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {


                if (task.isSuccessful())
                {
                    Toast.makeText(holder.itemView.getContext()
                            ,"Message Deleted" , Toast.LENGTH_SHORT);



                }

                else {

                    Toast.makeText(holder.itemView.getContext()
                            ,"Error Deleting Message" , Toast.LENGTH_SHORT);

                }


            }
        });

    }







    private void deletetMessagesForAll(final int i , final MessageViewHolder holder)
    {

        final DatabaseReference rootref = FirebaseDatabase.getInstance().getReference();
        rootref.child("Messages").child(userMessagesList.get(i).getTo())
                .child(userMessagesList.get(i).getFrom())
                .child(userMessagesList.get(i).getMessageID())
                .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {


                if (task.isSuccessful())
                {


                    rootref.child("Messages").child(userMessagesList.get(i).getFrom())
                            .child(userMessagesList.get(i).getTo())
                            .child(userMessagesList.get(i).getMessageID())
                            .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {


                            if (task.isSuccessful()){


                                Toast.makeText(holder.itemView.getContext()
                                        ,"Message Deleted" , Toast.LENGTH_SHORT);




                            }


                        }
                    });






                }

                else {

                    Toast.makeText(holder.itemView.getContext()
                            ,"Error Deleting Message" , Toast.LENGTH_SHORT);

                }


            }
        });

    }




private void playSong( String s){


  final   MediaPlayer mediaPlayer = new MediaPlayer();
    try {
        mediaPlayer.setDataSource(s);

        mediaPlayer.prepare();
    /*    mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(MediaPlayer mp) {
            mp.start();
        }
    });

        mediaPlayer.prepare(); */
    }
    catch (IOException e){

        e.printStackTrace();
    }


}





}
