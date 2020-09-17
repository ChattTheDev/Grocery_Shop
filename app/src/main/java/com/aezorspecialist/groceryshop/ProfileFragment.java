package com.aezorspecialist.groceryshop;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

public class ProfileFragment extends Fragment {

    CircularImageView circularImageView;
    CardView c1, c2, c3, c4;
    View view;
    FirebaseAuth mAuth;
    DatabaseReference reference;
    StorageReference storageReference;
    FirebaseUser firebaseUser;
    String user;
    TextView username, useremail, userphone;


    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_profile, container, false);


        c1 = view.findViewById(R.id.editprofile);
        c2 = view.findViewById(R.id.viewcart);
        c3 = view.findViewById(R.id.vieworders);
        c4 = view.findViewById(R.id.logout);

        username = view.findViewById(R.id.username);
        useremail = view.findViewById(R.id.useremail);
        circularImageView = view.findViewById(R.id.addimage);
        userphone = view.findViewById(R.id.userphone);

        firebaseUser = mAuth.getInstance().getCurrentUser();
        user = firebaseUser.getUid();

        reference = FirebaseDatabase.getInstance().getReference().child("Users");

        c1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(view.getContext(), EditProfile.class);
                startActivity(intent2);
            }
        });

        c2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(), CartPage.class);
                startActivity(intent);
            }
        });

        c3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(view.getContext(), MyOrders.class);
                intent1.putExtra("cusmobile", userphone.getText().toString());
                startActivity(intent1);
            }
        });


        c4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(view.getContext());
            builder1.setMessage("Are You Sure?");
            builder1.setCancelable(true);

            builder1.setPositiveButton(
                    "Yes",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            mAuth = FirebaseAuth.getInstance();
                            mAuth.signOut();
                            Intent i3 = new Intent(view.getContext(), CustomerLoginActivity.class);
                            i3.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(i3);


                        }
                    });

            builder1.setNegativeButton(
                    "No",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });

            AlertDialog alert11 = builder1.create();
            alert11.show();
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){

                    if(snapshot.child(user).child("image").exists()){

                        String image = snapshot.child(user).child("image").getValue(String.class);
                        String cusname = snapshot.child(user).child("customerName").getValue(String.class);
                        String cusemail = snapshot.child(user).child("customeremail").getValue(String.class);
                        String cusmobile = snapshot.child(user).child("customerMobile").getValue(String.class);


                        Glide.with(view.getContext()).load(image).into(circularImageView);
                        username.setText(cusname);
                        useremail.setText(cusemail);
                        userphone.setText(cusmobile);


                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}