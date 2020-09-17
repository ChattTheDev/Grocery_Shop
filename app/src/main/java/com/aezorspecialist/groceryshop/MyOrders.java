package com.aezorspecialist.groceryshop;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aezorspecialist.groceryshop.AllModels.Request;
import com.aezorspecialist.groceryshop.Viewholders.OrderViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class MyOrders extends AppCompatActivity {
    RecyclerView crecy1;
    FirebaseRecyclerAdapter<Request, OrderViewHolder> adapterformyorders;
    FirebaseRecyclerOptions<Request> optionsformyorders;

    DatabaseReference referenceformyorders;
    TextView t1, msg1, msgtot;
    String a;
    String cutomermobileno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_my_orders);

        crecy1 = findViewById(R.id.cartpagerecyclermyorders);
        a = getIntent().getStringExtra("orderid");

        referenceformyorders = FirebaseDatabase.getInstance().getReference().child("Orders");

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MyOrders.this);
        crecy1.setLayoutManager(linearLayoutManager);
        crecy1.setHasFixedSize(true);

        cutomermobileno = getIntent().getStringExtra("cusmobile");

        loadorders(cutomermobileno);

    }

    private void loadorders(String cutomermobileno) {

        Query query = referenceformyorders.orderByChild("phone").equalTo(cutomermobileno);

        optionsformyorders = new FirebaseRecyclerOptions.Builder<Request>().setQuery(query, Request.class).build();
        adapterformyorders = new FirebaseRecyclerAdapter<Request, OrderViewHolder>(optionsformyorders) {
            @NonNull
            @Override
            public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View vnew = LayoutInflater.from(parent.getContext()).inflate(R.layout.singleitemformyorders, parent, false);
                final OrderViewHolder viewHolder = new OrderViewHolder(vnew);
                return viewHolder;
            }

            @Override
            protected void onBindViewHolder(@NonNull final OrderViewHolder holder, int position, @NonNull Request model) {
                holder.txtorderid.setText(adapterformyorders.getRef(position).getKey());
                holder.txtorderstatus.setText(convertCodeToStatus(model.getStatus()));
                holder.txtorderaddress.setText(model.getAddress());
                holder.txtorderphone.setText(model.getPhone());
                holder.txttotalprice.setText(model.getTotal());
                holder.checkitems.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getApplicationContext(), Order_Products_Fetch.class);
                        intent.putExtra("productid",holder.txtorderid.getText().toString());
                        startActivity(intent);
                    }
                });


            }
        };
        adapterformyorders.startListening();
        crecy1.setAdapter(adapterformyorders);
    }

    private void oldmethod() {
        final DatabaseReference cartlistref = FirebaseDatabase.getInstance().getReference().child("Cart List").child("Admin View");
//
//        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//        final String a = user.getUid();
//        t1.setText(a);
//
//
//        Query query = cartlistref.child(a).child("Products").orderByChild("cusid").equalTo(t1.getText().toString());
//
//        options1 = new FirebaseRecyclerOptions.Builder<ModelForMyOrders>().setQuery(query, ModelForMyOrders.class).build();
//        adapter1 = new FirebaseRecyclerAdapter<ModelForMyOrders, Viewholderformyorders>(options1) {
//            @Override
//            protected void onBindViewHolder(@NonNull Viewholderformyorders holder, int position, @NonNull final ModelForMyOrders model) {
//
//
//                holder.prname1.setText(model.getPname());
//                holder.prprice1.setText(model.getPrice());
//                holder.prquantity1.setText(model.getQuantity());
//
//
//            }
//
//
//            @NonNull
//            @Override
//            public Viewholderformyorders onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//
//                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.singleitemformyorders, parent, false);
//
//                Viewholderformyorders viewHolder = new Viewholderformyorders(view);
//                return viewHolder;
//            }
//        };
//        adapter1.startListening();
//        crecy1.setAdapter(adapter1);

    }

    private void checkorderstate() {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String a = user.getUid();
        DatabaseReference orderref = FirebaseDatabase.getInstance().getReference().child("Orders");
        Query query = orderref.orderByChild("orderid").equalTo(a);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    String shippingtate = snapshot.child("state").getValue().toString();
                    String username = snapshot.child("cusname").getValue().toString();
                    String totprice = snapshot.child("price").getValue().toString();

                    if (shippingtate.equals("shipped")) {

                        msgtot.setText("Your Final Total Price is: ₹" + totprice);
                        msg1.setText("Dear " + username + "\n Hurray...Your Order has been Shipped");
                        Toast.makeText(getApplicationContext(), "You can Only Purchase Other items, once you will receive your order", Toast.LENGTH_LONG).show();

                    } else if (shippingtate.equals("not shipped")) {

                        msgtot.setText("Your Final Total Price is: ₹" + totprice);
                        msg1.setText("Dear " + username + "\n Your Order has not been Shipped Yet");
                        Toast.makeText(getApplicationContext(), "You can Only Purchase Other items, once you will receive your order", Toast.LENGTH_LONG).show();

                    } else {
                        FirebaseDatabase.getInstance().getReference("Cart List").child("Admin View").child(a).removeValue()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        if (task.isSuccessful()) {
                                        }

                                    }
                                });
                    }
                }
//                else {
//                    msg1.setText("Congratulations, Your Final Order has been Placed Successfully, Soon you will receive your Order at your Doorstep");
//                    msg1.setVisibility(View.VISIBLE);
//                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private String convertCodeToStatus(String status) {
        if (status.equals("0"))
            return "Placed";
        else if (status.equals("1"))
            return "Shipped";
        else
            return "Order Completed";
    }
}