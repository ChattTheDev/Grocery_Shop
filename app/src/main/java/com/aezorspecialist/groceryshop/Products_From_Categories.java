package com.aezorspecialist.groceryshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.aezorspecialist.groceryshop.AllModels.Model;
import com.aezorspecialist.groceryshop.Viewholders.StaggeredViewHolder;
import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class Products_From_Categories extends AppCompatActivity {

    RecyclerView pfcrecyclerview;
    ProgressBar pfcprogress;
    StaggeredGridLayoutManager staggeredGridLayoutManager;
    DatabaseReference dataref;
    FirebaseRecyclerOptions<Model> options;
    FirebaseRecyclerAdapter<Model, StaggeredViewHolder> adapter;
     String ProductKey;
    String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_products__from__categories);
        ProductKey = getIntent().getStringExtra("catname");
        Toolbar pfctoolbar = findViewById(R.id.pfctoolbar);
        setSupportActionBar(pfctoolbar);
        getSupportActionBar().setTitle("Products From "+ ProductKey);

        pfcrecyclerview = findViewById(R.id.pfcrecyclerview);
        pfcprogress = findViewById(R.id.pfcprogressbar);

        dataref = FirebaseDatabase.getInstance().getReference().child("UploadProductDetails");
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        pfcrecyclerview.setLayoutManager(staggeredGridLayoutManager);
        pfcprogress.setVisibility(View.VISIBLE);

        LoadData("");


    }

    private void LoadData(String data) {
        data = ProductKey;
        final Query query = dataref.orderByChild("productcategory").startAt(data).endAt(data + "\uf8ff");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    options = new FirebaseRecyclerOptions.Builder<Model>().setQuery(query, Model.class).build();
                    adapter = new FirebaseRecyclerAdapter<Model, StaggeredViewHolder>(options) {
                        @Override
                        protected void onBindViewHolder(@NonNull final StaggeredViewHolder holder, int position, @NonNull Model model) {

                            int a = Integer.parseInt(model.getOriginalprice());
                            holder.finalprodname.setText(model.getProductname());
                            holder.finalprodprice.setText("₹" + model.getFinalprice());
                            holder.finalprodoriginalprice.setText("( ₹" + Integer.toString(a) + ")");
                            holder.finalprodoriginalprice.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                            Glide.with(getApplicationContext()).load(model.getProductimage()).into(holder.imageView);
                            pfcprogress.setVisibility(View.GONE);


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
                    pfcrecyclerview.setAdapter(adapter);

                }
                else{
                    Toast.makeText(getApplicationContext(), "No Products Found..", Toast.LENGTH_SHORT).show();
                    pfcprogress.setVisibility(View.GONE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}