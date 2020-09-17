package com.aezorspecialist.groceryshop.Viewholders;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aezorspecialist.groceryshop.R;

public class Newviewholder extends RecyclerView.ViewHolder {
    TextView prname2, prquantity2, prprice2;
    public Newviewholder(@NonNull View itemView) {
        super(itemView);
        prname2 = itemView.findViewById(R.id.cartprodnamenew);
        prprice2 = itemView.findViewById(R.id.productpricecartpagenew);
        prquantity2 = itemView.findViewById(R.id.prquantitynew);
    }
}
