package com.aezorspecialist.groceryshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.aezorspecialist.groceryshop.AllModels.Model;
import com.aezorspecialist.groceryshop.AllModels.SpecialOffersModel;
import com.aezorspecialist.groceryshop.Viewholders.SpecialOffersViewholder;
import com.aezorspecialist.groceryshop.Viewholders.StaggeredViewHolder;
import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class Special_Offers extends AppCompatActivity {
    RecyclerView recyclerView;
    FirebaseRecyclerOptions<Model> optionsspof;
    FirebaseRecyclerAdapter<Model, StaggeredViewHolder> adapterspof;
    String newkeu;
    StaggeredGridLayoutManager gridLayoutManager;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_special__offers);
        Toolbar toolbar = findViewById(R.id.toolspof);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        newkeu = getIntent().getStringExtra("catimagesfromslider");
        getSupportActionBar().setTitle("Special Offers from:"+newkeu);

        recyclerView = findViewById(R.id.specialoffersrecycler);

        reference = FirebaseDatabase.getInstance().getReference().child("UploadProductDetails");
        gridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(gridLayoutManager);
        LoadData("");







    }
    private void LoadData(String data) {
        data = newkeu;
        Query query = reference.orderByChild("productcategory").startAt(data).endAt(data+"\uf8ff");
        optionsspof = new FirebaseRecyclerOptions.Builder<Model>().setQuery(query, Model.class).build();
        adapterspof = new FirebaseRecyclerAdapter<Model, StaggeredViewHolder>(optionsspof) {
            @Override
            protected void onBindViewHolder(@NonNull final StaggeredViewHolder holder, int position, @NonNull Model model) {

                int a = Integer.parseInt(model.getOriginalprice());


                holder.finalprodname.setText(model.getProductname());
                holder.finalprodprice.setText("₹" + model.getFinalprice());
                holder.finalprodoriginalprice.setText("( ₹" + Integer.toString(a) + ")");
                holder.finalprodoriginalprice.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                Glide.with(getApplicationContext()).load(model.getProductimage()).into(holder.imageView);
                //Picasso.get().load(model.getProductimage()).into(holder.imageView);
                holder.finaldiscount.setText(model.getDiscountpercentage()+"% Off");


            }

            @NonNull
            @Override
            public StaggeredViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.gridforproducts, parent, false);
                final StaggeredViewHolder viewHolder = new StaggeredViewHolder(v);
                viewHolder.imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String key;
                        key = getRef(viewHolder.getAdapterPosition()).getKey();
                        Intent i = new Intent(v.getContext(), Product_Details.class);
                        i.putExtra("ProductKey", key);
                        startActivity(i);

                    }
                });
                return viewHolder;
            }
        };
        adapterspof.startListening();
        recyclerView.setAdapter(adapterspof);

    }
}