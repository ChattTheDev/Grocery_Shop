package com.aezorspecialist.groceryshop;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Order_Products_Fetch extends AppCompatActivity {

    RecyclerView crecy1;
    FirebaseRecyclerAdapter<Modelforproductfetch, Viewholderforproductfetch> adapter1;
    FirebaseRecyclerOptions<Modelforproductfetch> options1;
    String customerid, cusmobileno, cusstatus, finalpricefukk, cusfinalprice;
    ArrayAdapter<String> arrayAdapter;
    ArrayList<String> spinnerdatalist;
    ValueEventListener listener;
    String state;
    String loadnew;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_order__products__fetch);


        crecy1 = findViewById(R.id.prfetch);


        loadnew = getIntent().getStringExtra("productid");


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(Order_Products_Fetch.this);
        crecy1.setLayoutManager(linearLayoutManager);
        crecy1.setHasFixedSize(true);

        final DatabaseReference cartlistref = FirebaseDatabase.getInstance().getReference("Orders");


        Query query = cartlistref.child(loadnew).child("foods");

        options1 = new FirebaseRecyclerOptions.Builder<Modelforproductfetch>().setQuery(query, Modelforproductfetch.class).build();
        adapter1 = new FirebaseRecyclerAdapter<Modelforproductfetch, Viewholderforproductfetch>(options1) {
            @Override
            protected void onBindViewHolder(@NonNull final Viewholderforproductfetch holder, int position, @NonNull Modelforproductfetch model) {
                holder.prname1.setText(model.getProductName());
                holder.prprice1.setText(model.getPrice());
                holder.prquantity1.setText(model.getQuantity());

            }


            @NonNull
            @Override
            public Viewholderforproductfetch onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.newmyorders, parent, false);

                Viewholderforproductfetch viewHolder = new Viewholderforproductfetch(view);
                return viewHolder;
            }
        };
        adapter1.startListening();
        crecy1.setAdapter(adapter1);
    }


}
