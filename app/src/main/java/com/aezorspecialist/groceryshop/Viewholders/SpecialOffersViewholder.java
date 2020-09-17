package com.aezorspecialist.groceryshop.Viewholders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.aezorspecialist.groceryshop.R;

public class SpecialOffersViewholder extends RecyclerView.ViewHolder {
     public ImageView specialimageview;
     public TextView specialtext;
     public CardView cardViewnew;
    public SpecialOffersViewholder(@NonNull View itemView) {
        super(itemView);

        specialimageview = itemView.findViewById(R.id.singleimageview);
        specialtext = itemView.findViewById(R.id.textcat);
        cardViewnew = itemView.findViewById(R.id.cardnew);
    }
}
