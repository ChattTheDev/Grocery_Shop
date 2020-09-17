package com.aezorspecialist.groceryshop.Viewholders;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aezorspecialist.groceryshop.AllModels.Order;
import com.aezorspecialist.groceryshop.CartPage;
import com.aezorspecialist.groceryshop.Database.Database;
import com.aezorspecialist.groceryshop.Interface.ItemClickListener;
import com.aezorspecialist.groceryshop.Product_Details;
import com.aezorspecialist.groceryshop.R;
import com.aezorspecialist.groceryshop.User_Dashborad;
import com.amulyakhare.textdrawable.TextDrawable;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView txt_cart_name, txt_price, txt_productid;
    public ImageView img_cart_count, delete;
    public ElegantNumberButton elegantNumberButton;
    Context context;

    private ItemClickListener itemClickListener;

    public CartViewHolder(View itemView) {
        super(itemView);

        txt_cart_name = (TextView) itemView.findViewById(R.id.cartprodname);
        txt_price = (TextView) itemView.findViewById(R.id.productpricecartpage);
        txt_productid = (TextView) itemView.findViewById(R.id.prodid);
        delete = (ImageView) itemView.findViewById(R.id.delete);
        elegantNumberButton = (ElegantNumberButton)itemView.findViewById(R.id.elegentbutt);
//        itemView.setOnCreateContextMenuListener(this);

    }

    public void setTxt_cart_name(TextView txt_cart_name) {
        this.txt_cart_name = txt_cart_name;
    }

    @Override
    public void onClick(View v) {

    }
}


public class CartAdapter extends RecyclerView.Adapter<CartViewHolder> {

    private List<Order> listData = new ArrayList<>();
    private CartPage cartPage;
    String finalcounts;
    String stock;

    public CartAdapter(List<Order> listData, CartPage cartPage) {
        this.listData = listData;
        this.cartPage = cartPage;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(cartPage);
        View itemView = inflater.inflate(R.layout.singleitemforcartpage, parent, false);
        return new CartViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, final int position) {
//        TextDrawable drawable = TextDrawable.builder().buildRound("" + listData.get(position).getQuantity(), Color.RED);
//        holder.img_cart_count.setImageDrawable(drawable);

        holder.elegantNumberButton.setNumber(listData.get(position).getQuantity());
        //int price = (Integer.parseInt(listData.get(position).getPrice()))*(Integer.parseInt(listData.get(position).getQuantity()));
        holder.txt_price.setText(listData.get(position).getPrice());
        holder.txt_cart_name.setText(listData.get(position).getProductName());
        holder.txt_productid.setText(listData.get(position).getProductId());
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(cartPage);
//
//
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.singleitemdialog);
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(dialog.getWindow().getAttributes());
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                lp.gravity = Gravity.CENTER;

                dialog.getWindow().setAttributes(lp);
                final Button b1 = (Button) dialog.findViewById(R.id.yes);
                final Button b2 = (Button) dialog.findViewById(R.id.no);
                dialog.show();
                dialog.setCancelable(false);
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("UploadProductDetails");

                reference.child(listData.get(position).getProductId()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        stock = snapshot.child("finalstock").getValue().toString();


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                b1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String a = listData.get(position).getQuantity();
                        int f = Integer.valueOf(stock);
                        int g = Integer.valueOf(a);
                        int h = f + g;
                        finalcounts = String.valueOf(h);

                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("UploadProductDetails");

                        HashMap<String, Object> map = new HashMap<>();
                        map.put("finalstock", finalcounts);
                        reference.child(listData.get(position).getProductId()).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(cartPage, "Loading...", Toast.LENGTH_SHORT).show();
                                }

                            }
                        });



                        Order order = listData.get(position);
                        int bbb = order.getID();
                        new Database(cartPage).removeFromCart(bbb);


                        Intent intent = new Intent(cartPage, User_Dashborad.class);
                        cartPage.startActivity(intent);

                        Toast.makeText(cartPage, "Item Deleted...", Toast.LENGTH_SHORT).show();
                    }
                });
                b2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });


            }
        });

        holder.elegantNumberButton.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
            @Override
            public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
                Order order = listData.get(position);
                order.setQuantity(String.valueOf(newValue));
                new Database(cartPage).updatecart(order);

                int total = 0;
                List<Order> orders = new Database(cartPage).getCarts();
                for (Order item : orders)
                    total += (Integer.parseInt(item.getPrice())) * (Integer.parseInt(item.getQuantity()));

                cartPage.totalprice.setText(String.valueOf(total));

            }
        });

//        holder.elegantNumberButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(context, Product_Details.class);
//
//                String bc = order.getProductId();
//                String cd = order.getQuantity();
//                intent.putExtra("ProductKey", bc);
//                context.startActivity(intent);
////                new Database(context).updatecart(order);
//
//            }
//        });
//        new Product_Details().newdbcart();


//        holder.elegantNumberButton.setNumber(listData.get(position).getQuantity());
//        holder.elegantNumberButton.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
//            @Override
//            public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
//                Order order =listData.get(position);
//                order.setQuantity(String.valueOf(newValue));
//                new Database(context).updatecart(order);
//            }
//        });

    }


    @Override
    public int getItemCount() {
        return listData.size();
    }
}
