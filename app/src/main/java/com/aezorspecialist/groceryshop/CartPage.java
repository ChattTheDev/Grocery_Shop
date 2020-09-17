package com.aezorspecialist.groceryshop;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aezorspecialist.groceryshop.AllModels.Order;
import com.aezorspecialist.groceryshop.Database.Database;
import com.aezorspecialist.groceryshop.Viewholders.CartAdapter;
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

public class CartPage extends AppCompatActivity {
    public TextView totalprice;
    RecyclerView crecy;
    Button gotoproduct, checkout;
    String user;
    DatabaseReference reference, databaseReference, requests;
    FirebaseUser firebaseUser;
    FirebaseAuth mAuth;
    List<String> spinnerdatalist = new ArrayList<>();
    String key;
    ValueEventListener listener1;
    List<Order> cart = new ArrayList<>();
    CartAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_cart_page);
        Toolbar toolbar = findViewById(R.id.cartpagetoolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Your Cart");


        crecy = findViewById(R.id.cartpagerecycler);
        gotoproduct = findViewById(R.id.gotobutt);
        checkout = findViewById(R.id.checkoutbutton);
        totalprice = findViewById(R.id.totprice);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(CartPage.this);
        crecy.setLayoutManager(linearLayoutManager);
        crecy.setHasFixedSize(true);
        newload();
        requests = FirebaseDatabase.getInstance().getReference("Requests");


        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(CartPage.this);


                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.activity_checkout);
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(dialog.getWindow().getAttributes());
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                lp.gravity = Gravity.CENTER;

                dialog.getWindow().setAttributes(lp);
                dialog.setTitle("Confirm Your Details");

                final EditText e1 = dialog.findViewById(R.id.nameedit);
                final EditText e2 = dialog.findViewById(R.id.collegerolledit);
                final EditText e3 = dialog.findViewById(R.id.branchedit);
                final EditText e4 = dialog.findViewById(R.id.yearedit);
                final EditText e5 = dialog.findViewById(R.id.cityedit);
                final EditText e6 = dialog.findViewById(R.id.pinedit);
                final Button b1 = dialog.findViewById(R.id.updatedetails);
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
                                    if (spinnerdatalist.contains(pincodevalidator)) {

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
                                                        dialog.dismiss();
                                                        Intent intent = new Intent(getApplicationContext(), FinalCheckout.class);
                                                        intent.putExtra("totalprice", totalprice.getText().toString());
                                                        startActivity(intent);


//
                                                    }
                                                });
                                        Toast.makeText(getApplicationContext(), "You're Good to go", Toast.LENGTH_LONG).show();


                                    } else {
                                        Toast.makeText(getApplicationContext(), "Sorry, we don't Deliver here..", Toast.LENGTH_LONG).show();

                                    }
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });


                    }
                });
                dialog.show();
//
            }
        });
        gotoproduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), User_Dashborad.class);
                startActivity(intent);
            }
        });


    }

    private void newload() {
        cart = new Database(CartPage.this).getCarts();
        adapter = new CartAdapter(cart, CartPage.this);
        crecy.setAdapter(adapter);

        int total = 0;
        for (Order order : cart)
            total += (Integer.parseInt(order.getPrice())) * (Integer.parseInt(order.getQuantity()));

        totalprice.setText(String.valueOf(total));

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(), User_Dashborad.class);
        startActivity(intent);
        finish();
    }
}