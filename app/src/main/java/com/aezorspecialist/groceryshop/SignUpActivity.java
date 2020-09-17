package com.aezorspecialist.groceryshop;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.aezorspecialist.groceryshop.AllModels.UploadComplaints;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mikhaellopez.circularimageview.CircularImageView;

public class SignUpActivity extends AppCompatActivity {

    Button reg;
    EditText customername1, customeremail1, customermobileno1, customerpassword1, customerfulladdress1, customercity1, customerpincode1;
    FirebaseAuth mAuth;
    FirebaseUser firebaseUser;
    FirebaseAuth.AuthStateListener authStateListener;
    StorageReference mStorageRef;
    CircularImageView circularImageViewsignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sign_up);
        reg = (Button) findViewById(R.id.newloginbutt);
        customeremail1 = (EditText) findViewById(R.id.customeremail);
        customerpassword1 = (EditText) findViewById(R.id.customerPass);

        customername1 = (EditText) findViewById(R.id.customername);

        customermobileno1 = (EditText) findViewById(R.id.customermobilenumber);
        customerfulladdress1 = (EditText)findViewById(R.id.customerfulladdress) ;
        customercity1 = (EditText)findViewById(R.id.customercity) ;
        customerpincode1 = (EditText)findViewById(R.id.customerpincode) ;

        mStorageRef = FirebaseStorage.getInstance().getReference().child("UserImage");



        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String email = customeremail1.getText().toString().trim();
                String password = customerpassword1.getText().toString().trim();
                final String name = customername1.getText().toString().trim();
                final String mobileno = customermobileno1.getText().toString().trim();
                final String fulladdress = customerfulladdress1.getText().toString().trim();
                final String fullcity = customercity1.getText().toString().trim();
                final String fullpincode = customerpincode1.getText().toString().trim();
                final String image = "https://firebasestorage.googleapis.com/v0/b/grocery-app-full.appspot.com/o/UserImages%2Fperson.png?alt=media&token=39155dd6-0bf3-4553-b63c-28fbf4ca775c";

                if (email.equals("") || password.equals("") || name.equals("") ||  mobileno.equals("") || fulladdress.equals("") || fullcity.equals("")|| fullpincode.equals("")) {
                    Toast.makeText(getApplicationContext(), "Field Vaccant", Toast.LENGTH_LONG).show();
                    return;
                }
                if (password.length() < 5) {

                    Toast.makeText(SignUpActivity.this, "Password Should Be Minimum 6 Digits", Toast.LENGTH_LONG).show();
                    return;

                }

                final ProgressDialog progressDialog = new ProgressDialog(SignUpActivity.this);
                progressDialog.setTitle("Registering!!!");
                progressDialog.setMessage("Processing.....");
                progressDialog.setCancelable(false);
                progressDialog.show();

                mAuth = FirebaseAuth.getInstance();


                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {


                        if (task.isSuccessful()) {

                            UploadComplaints user = new UploadComplaints(name, email, mobileno, fulladdress, fullcity, fullpincode, image);

                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if (task.isSuccessful()) {
                                        Toast.makeText(SignUpActivity.this, "Registration Successfull", Toast.LENGTH_LONG).show();
                                        Intent i = new Intent(SignUpActivity.this, User_Dashborad.class);
                                        startActivity(i);
                                        progressDialog.hide();

                                        finish();

                                    } else {
                                        //display a failure message
                                        Toast.makeText(SignUpActivity.this, "Something Went Wrong! Try Again Later", Toast.LENGTH_SHORT).show();
                                    }


                                }
                            });



                        }
                    }

                });

            }
        });
    }
}