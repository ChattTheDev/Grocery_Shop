package com.aezorspecialist.groceryshop;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.aezorspecialist.groceryshop.AllModels.Model;
import com.aezorspecialist.groceryshop.Viewholders.StaggeredViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

public class SearchFragment extends Fragment {
    RecyclerView newrecyclerview;
    FirebaseRecyclerOptions<Model> options1;
    FirebaseRecyclerAdapter<Model, StaggeredViewHolder> adapter1;
    ProgressBar pp;
    View viewsearch;
    EditText searchnew;
    StaggeredGridLayoutManager staggeredGridLayoutManagerfinal;
    LinearLayoutManager manager;
    DatabaseReference referencenew;
    String values;
    FloatingActionButton floatingActionButton;


    public SearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewsearch = inflater.inflate(R.layout.fragment_search, container, false);

        newrecyclerview = viewsearch.findViewById(R.id.productrecycler);
        pp = viewsearch.findViewById(R.id.progresssearch);
        searchnew = viewsearch.findViewById(R.id.searchfeature);
        floatingActionButton = viewsearch.findViewById(R.id.itemsearch);
        referencenew = FirebaseDatabase.getInstance().getReference().child("UploadProductDetails");
        manager = new LinearLayoutManager(viewsearch.getContext());
        newrecyclerview.setLayoutManager(manager);
        //newrecyclerview.setHasFixedSize(true);
        pp.setVisibility(View.VISIBLE);
        LoadDatafinal();


        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String abc = searchnew.getText().toString();
                Query query = referencenew.orderByChild("productname").startAt(abc.toUpperCase()).endAt(abc.toLowerCase() + "\uf8ff");
                options1 = new FirebaseRecyclerOptions.Builder<Model>().setQuery(query, Model.class).build();
                adapter1 = new FirebaseRecyclerAdapter<Model, StaggeredViewHolder>(options1) {
                    @Override
                    protected void onBindViewHolder(@NonNull final StaggeredViewHolder holder, int position, @NonNull Model model) {

                        int a = Integer.parseInt(model.getOriginalprice());


                        holder.finalprodname.setText(model.getProductname());
                        holder.finalprodprice.setText("₹" + model.getFinalprice());
                        holder.finalprodoriginalprice.setText("( ₹" + Integer.toString(a) + ")");
                        holder.finalprodoriginalprice.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                        Picasso.get().load(model.getProductimage()).into(holder.imageView);
                        holder.finaldiscount.setText(model.getDiscountpercentage() + "% Off");
                        pp.setVisibility(View.GONE);


                    }

                    @NonNull
                    @Override
                    public StaggeredViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.gridforproducts, parent, false);
                        final StaggeredViewHolder viewHolder = new StaggeredViewHolder(v);
                        viewHolder.imageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                values = getRef(viewHolder.getAdapterPosition()).getKey();
                                Intent i = new Intent(v.getContext(), Product_Details.class);
                                i.putExtra("ProductKey", values);
                                startActivity(i);

                            }
                        });
                        return viewHolder;
                    }
                };
                newrecyclerview.setAdapter(adapter1);
                adapter1.startListening();


            }
        });


//        searchnew.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                if (s.toString() != null) {
//                    LoadDatafinal(s.toString());
//                } else {
//                    LoadDatafinal("");
//                }
//
//            }
//        });

        return viewsearch;
    }

    private void LoadDatafinal() {

        options1 = new FirebaseRecyclerOptions.Builder<Model>().setQuery(referencenew, Model.class).build();
        adapter1 = new FirebaseRecyclerAdapter<Model, StaggeredViewHolder>(options1) {
            @Override
            protected void onBindViewHolder(@NonNull final StaggeredViewHolder holder, int position, @NonNull Model model) {

                int a = Integer.parseInt(model.getOriginalprice());


                holder.finalprodname.setText(model.getProductname());
                holder.finalprodprice.setText("₹" + model.getFinalprice());
                holder.finalprodoriginalprice.setText("( ₹" + Integer.toString(a) + ")");
                holder.finalprodoriginalprice.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                Picasso.get().load(model.getProductimage()).into(holder.imageView);
                holder.finaldiscount.setText(model.getDiscountpercentage() + "% Off");
                pp.setVisibility(View.GONE);


            }

            @NonNull
            @Override
            public StaggeredViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.gridforproducts, parent, false);
                final StaggeredViewHolder viewHolder = new StaggeredViewHolder(v);
                viewHolder.imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        values = getRef(viewHolder.getAdapterPosition()).getKey();
                        Intent i = new Intent(v.getContext(), Product_Details.class);
                        i.putExtra("ProductKey", values);
                        startActivity(i);

                    }
                });
                return viewHolder;
            }
        };
        newrecyclerview.setAdapter(adapter1);
        adapter1.startListening();
    }
}