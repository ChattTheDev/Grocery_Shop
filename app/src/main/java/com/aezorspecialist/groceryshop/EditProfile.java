package com.aezorspecialist.groceryshop;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class EditProfile extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    EditText e1, e2, e3, e4,e5,e6;
    Button b1;
    String user;
    DatabaseReference reference;
    FirebaseUser firebaseUser;
    FirebaseAuth mAuth;
    CircularImageView circularImageView1;
    Uri mImageUri;
    String myuri = "";
    ProgressDialog dialog;
    private StorageTask uploadtask;


    boolean isImageAdded = false;

    StorageReference mStorageRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_edit_profile);
        circularImageView1 = findViewById(R.id.chooseimage1);
        e1 = findViewById(R.id.nameedit1);
        e2 = findViewById(R.id.collegerolledit1);
        e3 = findViewById(R.id.branchedit1);
        e4 = findViewById(R.id.yearedit1);
        e5 = findViewById(R.id.cityedit1);
        e6 = findViewById(R.id.pinedit1);

        b1 = findViewById(R.id.updatedetails1);
        firebaseUser = mAuth.getInstance().getCurrentUser();
        user = firebaseUser.getUid();

        mStorageRef = FirebaseStorage.getInstance().getReference().child("UserImages");
        reference = FirebaseDatabase.getInstance().getReference().child("Users");


        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){

                    if(snapshot.child(user).child("image").exists()){

                        String image = snapshot.child(user).child("image").getValue(String.class);
                        String cusname = snapshot.child(user).child("customerName").getValue(String.class);
                        String cusmobile = snapshot.child(user).child("customerMobile").getValue(String.class);
                        String cusemail = snapshot.child(user).child("customeremail").getValue(String.class);
                        String cusaddress = snapshot.child(user).child("customerFullAddress").getValue(String.class);
                        String cuscity = snapshot.child(user).child("fullcity").getValue(String.class);
                        String cuspincode = snapshot.child(user).child("fullpincode").getValue(String.class);


                        Glide.with(getApplicationContext()).load(image).into(circularImageView1);
                        e1.setText(cusname);
                        e2.setText(cusmobile);
                        e3.setText(cusemail);
                        e4.setText(cusaddress);
                        e5.setText(cuscity);
                        e6.setText(cuspincode);


                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        circularImageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();


            }
        });

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new ProgressDialog(EditProfile.this);
                dialog.setTitle("Updating...");
                dialog.show();
                dialog.setCancelable(false);
                if(mImageUri !=null){
                    updatedata();
                }
                else {
                    uploadotherdata();
                }
            }
        });
    }

    private void uploadotherdata() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("customerFullAddress", e4.getText().toString());
        map.put("customerMobile", e2.getText().toString());
        map.put("customerName", e1.getText().toString());
        map.put("customeremail", e3.getText().toString());
        map.put("fullcity", e5.getText().toString());
        map.put("fullpincode", e6.getText().toString());
        reference
                .child(user)
                .updateChildren(map)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(getApplicationContext(), "Details Updated....", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });
    }

    private void updatedata() {
        final StorageReference referencestorage = mStorageRef.child(user + ".jpg");
        uploadtask = referencestorage.putFile(mImageUri);
        uploadtask.continueWithTask(new Continuation() {
            @Override
            public Object then(@NonNull Task task) throws Exception
            {
                if(!task.isSuccessful()){
                    throw task.getException();
                }
                return referencestorage.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if(task.isSuccessful()){
                    Uri downloaduri = task.getResult();
                    myuri = downloaduri.toString();

                    HashMap<String, Object> map = new HashMap<>();
                    map.put("customerFullAddress", e4.getText().toString());
                    map.put("customerMobile", e2.getText().toString());
                    map.put("customerName", e1.getText().toString());
                    map.put("customeremail", e3.getText().toString());
                    map.put("fullcity", e5.getText().toString());
                    map.put("fullpincode", e6.getText().toString());
                    map.put("image", myuri);

                    reference
                            .child(user)
                            .updateChildren(map)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(getApplicationContext(), "Details Updated....", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                }
                            });

                }
            }
        })
        ;
    }








    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==PICK_IMAGE_REQUEST && data!=null){
            mImageUri = data.getData();
            isImageAdded=true;
            circularImageView1.setImageURI(mImageUri);
        }
    }

}