package com.aezorspecialist.groceryshop.Viewholders;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aezorspecialist.groceryshop.Interface.ItemClickListener;
import com.aezorspecialist.groceryshop.R;

public class OrderViewHolder extends RecyclerView.ViewHolder {
    public TextView txtorderid, txtorderstatus, txtorderphone, txtorderaddress, txttotalprice;
    public Button checkitems;
    public OrderViewHolder(@NonNull View itemView) {
        super(itemView);

        txtorderid = itemView.findViewById(R.id.orderidformyorders);
        txtorderstatus = itemView.findViewById(R.id.statusformyorders);
        txtorderphone = itemView.findViewById(R.id.phonenoformyorders);
        txtorderaddress = itemView.findViewById(R.id.addressmyorders);
        checkitems = itemView.findViewById(R.id.checkitems);
        txttotalprice = itemView.findViewById(R.id.totalprice);
    }
}
