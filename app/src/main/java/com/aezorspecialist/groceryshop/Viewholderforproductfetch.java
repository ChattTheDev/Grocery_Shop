package com.aezorspecialist.groceryshop;

import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class Viewholderforproductfetch extends RecyclerView.ViewHolder {

    public TextView prname1;
    public TextView prquantity1;
    public TextView prprice1;
    Spinner spinner;
    public Viewholderforproductfetch(@NonNull View itemView) {
        super(itemView);
        prname1 = itemView.findViewById(R.id.cartprodname1);
        prprice1 = itemView.findViewById(R.id.productpricecartpage1);
        prquantity1 = itemView.findViewById(R.id.prquantity1);
    }
}
