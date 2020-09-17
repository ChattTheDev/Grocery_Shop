package com.aezorspecialist.groceryshop;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aezorspecialist.groceryshop.AllModels.Model;
import com.aezorspecialist.groceryshop.AllModels.Order;
import com.aezorspecialist.groceryshop.Database.Database;
import com.aezorspecialist.groceryshop.Viewholders.StaggeredViewHolder;
import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class Product_Details extends AppCompatActivity {
    ImageView productimagefinalboss, left, right, favbutton;
    TextView fullproductname, fullproductoriginalprice, fullproductdiscountedprice, productcounter, addtofav, categoryname, fullproddesc, stocks;
    Button buttplus, buttminus, addtocart;
    RecyclerView horizontalrecyclerview;
    FirebaseRecyclerOptions<Model> options;
    FirebaseRecyclerAdapter<Model, StaggeredViewHolder> adapter;
    DatabaseReference dataref;
    LinearLayoutManager linearLayoutManager;
    MenuItem item;

    DatabaseReference reference;
    int count = 0;
    Context context;
    public String ProductKey;
    FloatingActionButton flb;
    String key;
    String state = "normal";
    String shippingtate = "not shipped";
    public String finalstock;
    int h = 0, f, g;
    String finalcounts, counterfinal;
    List<Order> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_product__details);
        Toolbar toolbar12 = findViewById(R.id.itempagetoolbar);
        setSupportActionBar(toolbar12);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Product Page");

        ProductKey = getIntent().getStringExtra("ProductKey");


        productimagefinalboss = findViewById(R.id.imagefinalviewitem);
        fullproductname = findViewById(R.id.itempagefullproductname);
        fullproductoriginalprice = findViewById(R.id.itempageprodoriginalprce);
        fullproductdiscountedprice = findViewById(R.id.itempageproddiscountedprce);
        productcounter = findViewById(R.id.prodcounter);
        buttplus = findViewById(R.id.buttplusitempage);
        buttminus = findViewById(R.id.buttminusitempage);
        stocks = findViewById(R.id.stocks);
        horizontalrecyclerview = findViewById(R.id.horrecyclerview);
        dataref = FirebaseDatabase.getInstance().getReference().child("UploadProductDetails");
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        horizontalrecyclerview.setLayoutManager(linearLayoutManager);
        newloaddata();

        categoryname = findViewById(R.id.itempagecatdes);
        fullproddesc = findViewById(R.id.proddescitem);
        addtocart = findViewById(R.id.buttaddtocartoption);


        left = findViewById(R.id.leftbutt);
        right = findViewById(R.id.rightbutt);

        flb = findViewById(R.id.flb);

        flb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CartPage.class);
                startActivity(intent);
            }
        });


        addtocart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                newdbcart();
                String a = productcounter.getText().toString();
                f = Integer.valueOf(finalstock);
                g = Integer.valueOf(a);
                h = f - g;
                finalcounts = String.valueOf(h);

                if (a.equals("0")) {
                    Toast.makeText(getApplicationContext(), "Please Add Atleast One Item", Toast.LENGTH_LONG).show();
                } else if (g > f) {
                    Toast.makeText(getApplicationContext(), "You can not Add above than Maximum Stock", Toast.LENGTH_LONG).show();
                } else if (g < 0) {
                    Toast.makeText(getApplicationContext(), "Please Check Your Details", Toast.LENGTH_LONG).show();

                } else {

                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("UploadProductDetails");
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("finalstock", finalcounts);
                    reference.child(ProductKey).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getApplicationContext(), "Loading...", Toast.LENGTH_SHORT).show();
                                newdbcart();
                            }

                        }
                    });

                    //addingtocart();
                   // newdbcart();
                    Toast.makeText(getApplicationContext(), "Adding, Please Wait.....", Toast.LENGTH_SHORT).show();
                }


            }
        });


        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                horizontalrecyclerview.getLayoutManager().scrollToPosition(linearLayoutManager.findLastVisibleItemPosition() - 1);

            }
        });
        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                horizontalrecyclerview.getLayoutManager().scrollToPosition(linearLayoutManager.findLastVisibleItemPosition() + 1);

            }
        });


        buttplus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                productcounter = findViewById(R.id.prodcounter);

                count = count + 1;

                productcounter.setText("" + count);


            }
        });

        buttminus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                productcounter = findViewById(R.id.prodcounter);
                count = count - 1;
                if (count == 0) {
                    productcounter.setText("0");
                } else {
                    productcounter.setText("" + count);
                }


            }
        });

        reference = FirebaseDatabase.getInstance().getReference().child("UploadProductDetails");



        reference.child(ProductKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String a = snapshot.child("productname").getValue().toString();
                    String b = snapshot.child("productdescription").getValue().toString();
                    String c = snapshot.child("productcategory").getValue().toString();
                    String d = snapshot.child("originalprice").getValue().toString();
                    String e = snapshot.child("finalprice").getValue().toString();
                    String f = snapshot.child("productimage").getValue().toString();
                    String g = snapshot.child("finalstock").getValue().toString();

//
                    Glide.with(getApplicationContext()).load(f).into(productimagefinalboss);
                    fullproductname.setText(a);
                    fullproddesc.setText(b);
                    categoryname.setText(c);
                    fullproductoriginalprice.setText(d);
                    fullproductoriginalprice.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                    fullproductdiscountedprice.setText(e);
                    //stocks.setText(g);
                    finalstock = g;

                    int fg = Integer.valueOf(g);
                    if (fg == 0) {
                        stocks.setText("Out Of Stocks");

                    } else if (fg > 0 && fg < 2) {
                        stocks.setText("Hurry, Only a Few Left");
                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    public void newdbcart() {
        new Database(getBaseContext()).addToCart(new Order(
                ProductKey,
                fullproductname.getText().toString(),
                productcounter.getText().toString(),
                fullproductdiscountedprice.getText().toString(),finalstock));
        Toast.makeText(getApplicationContext(), "Added to Cart", Toast.LENGTH_LONG).show();

    }




    private void addingtocart() {
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String a = user.getUid();
        String savecutime, savecurdate;
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat currentdate = new SimpleDateFormat("MMM, dd, yyyy");
        savecurdate = currentdate.format(calendar.getTime());

        SimpleDateFormat currenttime = new SimpleDateFormat("HH:mm:ss a");
        savecutime = currenttime.format(calendar.getTime());
        final String finaltime = savecutime + savecurdate;
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Cart List");
        // String bc = databaseReference.child("User View").child(a).getKey();
        final HashMap<String, Object> cartmap = new HashMap<>();
        cartmap.put("pid", ProductKey);
        cartmap.put("pname", fullproductname.getText().toString());
        cartmap.put("price", fullproductdiscountedprice.getText().toString());
        cartmap.put("date", savecurdate);
        cartmap.put("time", savecutime);
        cartmap.put("quantity", productcounter.getText().toString());
        cartmap.put("cusid", a);


        databaseReference.child("User View").child(a).child("Products")
                .child(ProductKey).updateChildren(cartmap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {

                            FirebaseDatabase.getInstance().getReference().child("Orders").child(a).child("Products")
                                    .child(ProductKey)
                                    .updateChildren(cartmap)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            if (task.isSuccessful()) {
                                                Toast.makeText(getApplicationContext(), "Added to Cart", Toast.LENGTH_LONG).show();
                                            }

                                        }
                                    });


                        }

                    }
                });
    }

    private void newloaddata() {
        options = new FirebaseRecyclerOptions.Builder<Model>().setQuery(dataref, Model.class).build();
        adapter = new FirebaseRecyclerAdapter<Model, StaggeredViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull StaggeredViewHolder holder, int position, @NonNull Model model) {
                int a = Integer.parseInt(model.getOriginalprice());

                holder.finalprodname.setText(model.getProductname());
                holder.finalprodprice.setText(model.getFinalprice());
                holder.finalprodoriginalprice.setText(Integer.toString(a));
                holder.finalprodoriginalprice.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                Glide.with(getApplicationContext()).load(model.getProductimage()).into(holder.imageView);

            }

            @NonNull
            @Override
            public StaggeredViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.gridforproducts, parent, false);
                final StaggeredViewHolder viewHolder = new StaggeredViewHolder(v);
                viewHolder.imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        key = getRef(viewHolder.getAdapterPosition()).getKey();
                        Intent i = new Intent(v.getContext(), Product_Details.class);
                        i.putExtra("ProductKey", key);
                        startActivity(i);
                        finish();

                    }
                });
                return viewHolder;
            }
        };
        adapter.startListening();
        horizontalrecyclerview.setAdapter(adapter);
    }

    private void checkorderstate() {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String a = user.getUid();
        DatabaseReference orderref = FirebaseDatabase.getInstance().getReference().child("Orders").child(a);
        orderref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    shippingtate = snapshot.child("state").getValue().toString();

                    if (shippingtate.equals("shipped")) {

                        addtocart.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), "Ordering Disabled, Please Check My Orders Section to Know More", Toast.LENGTH_LONG).show();

                    } else if (shippingtate.equals("not shipped")) {

                        addtocart.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), "Ordering Disabled, Please Check My Orders Section to Know More", Toast.LENGTH_LONG).show();

                    }
                } else {
                    addtocart.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        //checkorderstate();
    }
}