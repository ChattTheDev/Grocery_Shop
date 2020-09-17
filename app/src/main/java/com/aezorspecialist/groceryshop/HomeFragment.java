package com.aezorspecialist.groceryshop;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.aezorspecialist.groceryshop.AllModels.Model;
import com.aezorspecialist.groceryshop.AllModels.ModelCategories;
import com.aezorspecialist.groceryshop.AllModels.SpecialOffersModel;
import com.aezorspecialist.groceryshop.Viewholders.HorizontalViewholder;
import com.aezorspecialist.groceryshop.Viewholders.SliderAdapter;
import com.aezorspecialist.groceryshop.Viewholders.SpecialOffersViewholder;
import com.aezorspecialist.groceryshop.Viewholders.StaggeredViewHolder;
import com.bumptech.glide.Glide;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.interfaces.ItemClickListener;
import com.denzcoskun.imageslider.models.SlideModel;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HomeFragment extends Fragment {

    RecyclerView hrrecyclerview, prrecyclerview, specialrecyclerview;
    FirebaseRecyclerOptions<ModelCategories> options;
    FirebaseRecyclerOptions<Model> options1;

    FirebaseRecyclerOptions<SpecialOffersModel> optionssp;
    FirebaseRecyclerAdapter<ModelCategories, HorizontalViewholder> adapter;
    FirebaseRecyclerAdapter<Model, StaggeredViewHolder> adapter1;
    FirebaseRecyclerAdapter<SpecialOffersModel, SpecialOffersViewholder> adaptersp;
    DatabaseReference dataref, reference, speref;
    LinearLayoutManager linearLayoutManager, linearLayoutManager2;
    StaggeredGridLayoutManager staggeredGridLayoutManager;
    ProgressBar hrprogress, prprogress, sppreogress;
    String key;
    View view;
    SliderView sliderLayout;
    SliderAdapter sliderAdapter;
    ImageSlider imageSlider;

//    final int duration = 10;
//    final int pixelsToMove = 30;
//    private final Handler mHandler = new Handler(Looper.getMainLooper());
//    private final Runnable SCROLLING_RUNNABLE = new Runnable() {
//
//        @Override
//        public void run() {
//            specialrecyclerview.smoothScrollBy(pixelsToMove, 0);
//            mHandler.postDelayed(this, duration);
//        }
//    };


    public HomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_home, container, false);

        hrprogress = view.findViewById(R.id.hrprogress);
//        sppreogress = view.findViewById(R.id.spprogress);
        prrecyclerview = view.findViewById(R.id.allproducts);
        prprogress = view.findViewById(R.id.allproductspr);
        reference = FirebaseDatabase.getInstance().getReference().child("UploadProductDetails");
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        prrecyclerview.setLayoutManager(staggeredGridLayoutManager);
        prprogress.setVisibility(View.VISIBLE);
        LoadData();
        hrrecyclerview = view.findViewById(R.id.horizontalrecyclerview);
        dataref = FirebaseDatabase.getInstance().getReference().child("ProductCategories");
        linearLayoutManager = new LinearLayoutManager(view.getContext(), LinearLayoutManager.HORIZONTAL, false);
        hrrecyclerview.setLayoutManager(linearLayoutManager);
        hrprogress.setVisibility(View.VISIBLE);
        newloaddata();

//        specialrecyclerview = view.findViewById(R.id.specialopoffers);

        imageSlider = view.findViewById(R.id.image_slider);
        final List<SlideModel> remoteimages = new ArrayList<>();

        speref = FirebaseDatabase.getInstance().getReference().child("ProductOffers");
        speref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot data:snapshot.getChildren()){
                    remoteimages.add(new SlideModel(data.child("ImageUrl").getValue().toString(), data.child("CategoryName").getValue().toString(), ScaleTypes.CENTER_CROP));

                }
                imageSlider.setImageList(remoteimages,ScaleTypes.CENTER_CROP);
                imageSlider.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onItemSelected(int i) {
                        Intent intent = new Intent(getContext(), Special_Offers.class);
                        intent.putExtra("catimagesfromslider", remoteimages.get(i).getTitle().toString());
                        startActivity(intent);
                        Toast.makeText(view.getContext(), remoteimages.get(i).getTitle().toString(), Toast.LENGTH_SHORT).show();
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


//        sliderLayout = view.findViewById(R.id.imageSlider);
//        sliderAdapter = new SliderAdapter(view.getContext());
//        sliderLayout.setSliderAdapter(sliderAdapter);
//        sliderLayout.setIndicatorAnimation(IndicatorAnimationType.WORM); //set indicator animation by using IndicatorAnimationType. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
//        sliderLayout.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
//        sliderLayout.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
//        sliderLayout.setIndicatorSelectedColor(Color.WHITE);
//        sliderLayout.setIndicatorUnselectedColor(Color.GRAY);
//        sliderLayout.setScrollTimeInSec(4); //set scroll delay in seconds :
//        sliderLayout.startAutoCycle();









//        linearLayoutManager2 = new LinearLayoutManager(view.getContext(), LinearLayoutManager.HORIZONTAL, false);
//        specialrecyclerview.setLayoutManager(linearLayoutManager2);
//        sppreogress.setVisibility(View.VISIBLE);
//        loadforoffers();

//        specialrecyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//                int lastItem = linearLayoutManager2.findLastCompletelyVisibleItemPosition();
//                if(lastItem == linearLayoutManager2.getItemCount()-1){
//                    mHandler.removeCallbacks(SCROLLING_RUNNABLE);
//                    Handler postHandler = new Handler();
//                    postHandler.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            specialrecyclerview.setAdapter(null);
//                            specialrecyclerview.setAdapter(adaptersp);
//                            mHandler.postDelayed(SCROLLING_RUNNABLE, 1000);
//                        }
//                    }, 1000);
//                }
//            }
//        });
//        mHandler.postDelayed(SCROLLING_RUNNABLE, 1000);

        return view;
    }

    private void loadforoffers() {

        optionssp = new FirebaseRecyclerOptions.Builder<SpecialOffersModel>().setQuery(speref, SpecialOffersModel.class).build();
        adaptersp = new FirebaseRecyclerAdapter<SpecialOffersModel, SpecialOffersViewholder>(optionssp) {
            @Override
            protected void onBindViewHolder(@NonNull final SpecialOffersViewholder holder, int position, @NonNull SpecialOffersModel model) {

                holder.specialtext.setText(model.getCategoryName());
                Glide.with(view.getContext()).load(model.getImageUrl()).into(holder.specialimageview);

                holder.cardViewnew.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent clickcard = new Intent(getContext(), Special_Offers.class);
                        clickcard.putExtra("spofftext", holder.specialtext.getText().toString());
                        startActivity(clickcard);

                    }
                });


                sppreogress.setVisibility(View.GONE);



            }

            @NonNull
            @Override
            public SpecialOffersViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View vnew = LayoutInflater.from(parent.getContext()).inflate(R.layout.specialofferssingleitem, parent, false);
                final SpecialOffersViewholder viewHolder = new SpecialOffersViewholder(vnew);
                return viewHolder;
            }
        };
        adaptersp.startListening();
        specialrecyclerview.setAdapter(adaptersp);
    }

    private void LoadData() {
        options1 = new FirebaseRecyclerOptions.Builder<Model>().setQuery(reference, Model.class).build();
        adapter1 = new FirebaseRecyclerAdapter<Model, StaggeredViewHolder>(options1) {
            @Override
            protected void onBindViewHolder(@NonNull final StaggeredViewHolder holder, int position, @NonNull Model model) {

                int a = Integer.parseInt(model.getOriginalprice());


                holder.finalprodname.setText(model.getProductname());
                holder.finalprodprice.setText("₹" + model.getFinalprice());
                holder.finalprodoriginalprice.setText("( ₹" + Integer.toString(a) + ")");
                holder.finalprodoriginalprice.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                Glide.with(view.getContext()).load(model.getProductimage()).into(holder.imageView);
                //Picasso.get().load(model.getProductimage()).into(holder.imageView);
                holder.finaldiscount.setText(model.getDiscountpercentage()+"% Off");
                prprogress.setVisibility(View.GONE);


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

                    }
                });
                return viewHolder;
            }
        };
        adapter1.startListening();
        prrecyclerview.setAdapter(adapter1);

    }

    private void newloaddata() {
        options = new FirebaseRecyclerOptions.Builder<ModelCategories>().setQuery(dataref, ModelCategories.class).build();
        adapter = new FirebaseRecyclerAdapter<ModelCategories, HorizontalViewholder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final HorizontalViewholder holder, int position, @NonNull ModelCategories model) {

                holder.catname.setText(model.getCategoryName());
                Glide.with(view.getContext()).load(model.getImageUrl()).into(holder.catimage);
                //Picasso.get().load(model.getImageUrl()).into(holder.catimage);
                holder.cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getContext(), Products_From_Categories.class);
                        intent.putExtra("catname", holder.catname.getText().toString());
                        startActivity(intent);
                    }
                });


                hrprogress.setVisibility(View.GONE);


            }

            @NonNull
            @Override
            public HorizontalViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.singleforfetchingcategories, parent, false);
                final HorizontalViewholder viewHolder = new HorizontalViewholder(v);
                return viewHolder;
            }
        };
        adapter.startListening();
        hrrecyclerview.setAdapter(adapter);
    }

    private  void loadfornewoffers(){


    }
}