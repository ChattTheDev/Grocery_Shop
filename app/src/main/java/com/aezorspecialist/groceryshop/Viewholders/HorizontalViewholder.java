package com.aezorspecialist.groceryshop.Viewholders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.aezorspecialist.groceryshop.R;

public class HorizontalViewholder extends RecyclerView.ViewHolder {

    public ImageView catimage;
    public TextView catname;
    public CardView cardView;
    public HorizontalViewholder(@NonNull View itemView) {
        super(itemView);

        catimage = itemView.findViewById(R.id.cateimage);
        catname = itemView.findViewById(R.id.catname);
        cardView = itemView.findViewById(R.id.cardcategories);


    }
}
