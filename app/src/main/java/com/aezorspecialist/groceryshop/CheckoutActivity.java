package com.aezorspecialist.groceryshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CheckoutActivity extends AppCompatActivity {
    EditText e1, e2, e3, e4,e5,e6;
    Button b1;
    String user;
    DatabaseReference reference;
    FirebaseUser firebaseUser;
    FirebaseAuth mAuth;
    String totprice, totname, totquants;
    ValueEventListener listener1;
    DatabaseReference databaseReference;
    String key;
    Spinner spinner;
    ArrayAdapter<String> arrayAdapter;
    List<String> spinnerdatalist = new ArrayList<>();
    String[] keynew;
    boolean isstringexists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_checkout);
        e1 = findViewById(R.id.nameedit);
        e2 = findViewById(R.id.collegerolledit);
        e3 = findViewById(R.id.branchedit);
        e4 = findViewById(R.id.yearedit);
        e5 = findViewById(R.id.cityedit);
        e6 = findViewById(R.id.pinedit);



        b1 = findViewById(R.id.updatedetails);

        totprice = getIntent().getStringExtra("totalprice");
        totname = getIntent().getStringExtra("finalname");
        totquants = getIntent().getStringExtra("finalquant");

        firebaseUser = mAuth.getInstance().getCurrentUser();
        user = firebaseUser.getUid();

        reference = FirebaseDatabase.getInstance().getReference().child("Users");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String cusname = snapshot.child(user).child("customerName").getValue(String.class);
                String cusmobile = snapshot.child(user).child("customerMobile").getValue(String.class);
                String cusemail = snapshot.child(user).child("customeremail").getValue(String.class);
                String cusaddress = snapshot.child(user).child("customerFullAddress").getValue(String.class);
                String cuscity = snapshot.child(user).child("fullcity").getValue(String.class);
                String cuspincode = snapshot.child(user).child("fullpincode").getValue(String.class);


                e1.setText(cusname);
                e2.setText(cusmobile);
                e3.setText(cusemail);
                e4.setText(cusaddress);
                e5.setText(cuscity);
                e6.setText(cuspincode);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String pincodevalidator = e6.getText().toString();

                databaseReference = FirebaseDatabase.getInstance().getReference().child("Pincodes");

                listener1 = databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        for (DataSnapshot item : snapshot.getChildren()) {
                            key = item.child("pincode").getValue(String.class);
                            spinnerdatalist.add(key);
                            if(spinnerdatalist.contains(pincodevalidator)){

                                Map<String, Object> map = new HashMap<>();
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
                                                Toast.makeText(getApplicationContext(), "Details Confirmed....", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(getApplicationContext(), FinalCheckout.class);
                                                intent.putExtra("finaltotprice", totprice);
                                                intent.putExtra("fullfinalname", totname);
                                                intent.putExtra("fullfinalquant", totquants);
                                                startActivity(intent);
                                            }
                                        });
                                Toast.makeText(getApplicationContext(), "You're Good to go", Toast.LENGTH_LONG).show();



                            }
                            else {
                                //e6.setError("Delivery Not Avaiable");
                                Toast.makeText(getApplicationContext(), "Sorry, we don't Deliver here..", Toast.LENGTH_LONG).show();

                            }
                        }
                        //Toast.makeText(getApplicationContext(), "Third data:"+spinnerdatalist.get(2), Toast.LENGTH_LONG).show();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });






            }
        });


    }

}