package com.aezorspecialist.groceryshop.Viewholders;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aezorspecialist.groceryshop.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class StaggeredViewHolder extends RecyclerView.ViewHolder{

    public ImageView imageView;
    public TextView finalprodname;
    public TextView finalprodprice;
    public TextView finalprodoriginalprice;
    public TextView finaldiscount;


    public StaggeredViewHolder(@NonNull View itemView) {
        super(itemView);

        imageView = itemView.findViewById(R.id.finalprodimage);
        finalprodname = itemView.findViewById(R.id.finalprodname);
        finalprodprice = itemView.findViewById(R.id.finaldiscountedprice);
        finalprodoriginalprice = itemView.findViewById(R.id.originalprice);
        finaldiscount = itemView.findViewById(R.id.discountcounter);
    }
}
