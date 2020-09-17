package com.aezorspecialist.groceryshop;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aezorspecialist.groceryshop.AllModels.ModelForMyOrders;
import com.aezorspecialist.groceryshop.Viewholders.Viewholderformyorders;
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

public class MyOrdersFragment extends Fragment {

    RecyclerView crecy1;
    FirebaseRecyclerAdapter<ModelForMyOrders, Viewholderformyorders> adapter1;
    FirebaseRecyclerOptions<ModelForMyOrders> options1;
    TextView t1, msg1, msgtot;
    View view;

    public MyOrdersFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         view =  inflater.inflate(R.layout.fragment_my_orders, container, false);

        crecy1 = view.findViewById(R.id.cartpagerecyclermyorders);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());
        crecy1.setLayoutManager(linearLayoutManager);
        crecy1.setHasFixedSize(true);

        t1 = view.findViewById(R.id.userid);
        msg1 = view.findViewById(R.id.msg1);
        msgtot = view.findViewById(R.id.msgfinalprice);
        checkorderstate();

//        final DatabaseReference cartlistref = FirebaseDatabase.getInstance().getReference().child("Cart List").child("Admin View");
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
//
        return view;
    }

    private void checkorderstate(){

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String a = user.getUid();
        DatabaseReference orderref = FirebaseDatabase.getInstance().getReference().child("Orders").child(a);
        orderref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists()){
                    String shippingtate = snapshot.child("state").getValue().toString();
                    String username = snapshot.child("cusname").getValue().toString();
                    String totprice = snapshot.child("price").getValue().toString();

                    if(shippingtate.equals("shipped")){

                        msgtot.setText("Your Final Total Price is: ₹" + totprice);
                        msg1.setText("Dear "+ username +"\n Hurray...Your Order has been Shipped");
                        Toast.makeText(view.getContext(), "You can Only Purchase Other items, Once you will Receive your Order", Toast.LENGTH_LONG).show();

                    }
                    else if(shippingtate.equals("not shipped")){

                        msgtot.setText("Your Final Total Price is: ₹" + totprice);
                        msg1.setText("Dear "+ username +"\n Your Order has not been Shipped Yet");
                        msg1.setVisibility(View.VISIBLE);
                        Toast.makeText(view.getContext(), "You can Only Purchase Other items, Once you will Receive your Order", Toast.LENGTH_LONG).show();

                    }
                    else {
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

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}