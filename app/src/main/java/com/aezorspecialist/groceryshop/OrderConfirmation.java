package com.aezorspecialist.groceryshop;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.RecyclerView;

import com.aezorspecialist.groceryshop.AllModels.Order;
import com.aezorspecialist.groceryshop.AllModels.Request;
import com.aezorspecialist.groceryshop.Database.Database;
import com.aezorspecialist.groceryshop.Viewholders.CartAdapter;
import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class OrderConfirmation extends AppCompatActivity {
    TextView price, finalprice, addititonalvalue, finalfullprices, orderid, valuefromdb, someofthemvalues;
    Button shopmore;
    String finalpricenew;
    String paying;
    double bc = 100.0;
    double firstdc = 60.0;
    double seconddc = 50.0;
    double thirddc = 40.0;
    RecyclerView crecy;

    FirebaseRecyclerAdapter<Modelforproductfetch, Viewholderforproductfetch> adapter1;
    FirebaseRecyclerOptions<Modelforproductfetch> options1;
    //    FirebaseRecyclerAdapter<ModelForCartitems, CartViewHolder> adapter;
//    FirebaseRecyclerOptions<ModelForCartitems> options;
    String savecutime, savecurdate;

    String totname2;
    String totquants2;
    String ProductKey;
    String cusname, cusmobile, cusaddress, cusemail;
    TextView t1, t2, t3, t4;
    String finalnew, ffp;
    String a;
    double abc;
    DatabaseReference requests;

    ConstraintLayout constraintLayout;
    NestedScrollView scrollView;
    String fetchvalue;
    String key;
    double fromfinalfullprices, fromfetchedvalues, sumofthem;
    String randid;
    String productuid;
    List<Order> cartorder = new ArrayList<>();
    CartAdapter adapterorder;
    Button vieworders;
    String username;
    String token, keynew;
    List<String> spinnerdatalistnew = new ArrayList<>();
    ValueEventListener listener2;
    Order orderitems;
    private Bitmap bitmap;
    private String URL = "https://fcm.googleapis.com/fcm/send";
    private RequestQueue mRequestque;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_order_confirmation);

//setting findviewbyid for each elements
        price = findViewById(R.id.fullfinal);
        finalprice = findViewById(R.id.finalfull);
        addititonalvalue = findViewById(R.id.addititonalvalue);
        finalfullprices = findViewById(R.id.finalfullprices);
        constraintLayout = findViewById(R.id.neworder);
        orderid = findViewById(R.id.order_id);
        t1 = findViewById(R.id.newtext);
        t2 = findViewById(R.id.newemail);
        t3 = findViewById(R.id.newaddress);
        t4 = findViewById(R.id.newmobile);
        scrollView = findViewById(R.id.nested);
        vieworders = findViewById(R.id.vieworders);
        mRequestque = Volley.newRequestQueue(OrderConfirmation.this);
        token = FirebaseInstanceId.getInstance().getToken();


        ////end////

        //setting recyclerviewlayoutmanager

        //end//
        // addingtocartforconfirm();
        randomeidgenerate();
        //cartitemload();
        gettinguservalue();

        calcualtefinalprice();
        sendnotification();
        //sendMail();

        vieworders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Order_Products_Fetch.class);
                intent.putExtra("productid", orderid.getText().toString());
                startActivity(intent);
            }
        });
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                addingtocart();
            }
        }, 900);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                sendMail();

            }
        }, 1000);


//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//
//                deletingcartitems();
//
//            }
//        }, 2000);

    }

    private void randomeidgenerate() {
        Random r = new Random();
        int i1 = r.nextInt(999999 - 111111) + 111111;
        randid = String.valueOf(i1);
        orderid.setText(randid);

    }

    //uploading the orders table
    private void addingtocart() {

        requests = FirebaseDatabase.getInstance().getReference().child("Orders");
        cartorder = new Database(OrderConfirmation.this).getCarts();


        Request request = new Request(
                t4.getText().toString(),
                t1.getText().toString(),
                t3.getText().toString(),
                finalfullprices.getText().toString(),
                cartorder,
                token
        );
        requests.child(orderid.getText().toString())
                .setValue(request);
//        new Database(getBaseContext()).cleanCart();
        Toast.makeText(OrderConfirmation.this, "Your  Order has been Confirmed!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), User_Dashborad.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();

    }

    private void deletingcartitems() {
        new Database(getBaseContext()).cleanCart();
    }

    private void calcualtefinalprice() {
        finalpricenew = getIntent().getStringExtra("finalfullprice");
        price.setText(finalpricenew);
        double a = Double.valueOf(finalpricenew);
        abc = a - bc;
        finalprice.setText(String.valueOf(abc));
        String prices = finalprice.getText().toString();

        if (abc > 0 && abc < 999) {
            addititonalvalue.setText("60");

            double fdc = abc + firstdc;
            finalfullprices.setText(String.valueOf(fdc));

        } else if (abc > 1000 && abc < 1999) {
            addititonalvalue.setText("50");
            double sdc = abc + seconddc;
            finalfullprices.setText(String.valueOf(sdc));
        } else if (abc > 2000 && abc < 2999) {
            addititonalvalue.setText("40");
            double tdc = abc + thirddc;
            finalfullprices.setText(String.valueOf(tdc));
        } else {
            addititonalvalue.setText("0");
            finalfullprices.setText(prices);
        }
    }

    private void gettinguservalue() {
        FirebaseUser newuser = FirebaseAuth.getInstance().getCurrentUser();
        username = newuser.getUid();
        final DatabaseReference dataref = FirebaseDatabase.getInstance().getReference().child("Users");
        dataref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                cusname = snapshot.child(username).child("customerName").getValue(String.class);
                cusmobile = snapshot.child(username).child("customerMobile").getValue(String.class);
                cusemail = snapshot.child(username).child("customeremail").getValue(String.class);
                cusaddress = snapshot.child(username).child("customerFullAddress").getValue(String.class);

                t1.setText(cusname);
                t2.setText(cusemail);
                t3.setText(cusaddress);
                t4.setText(cusmobile);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void sendnotification() {
        JSONObject json = new JSONObject();
        try {
            json.put("to", "/topics/" + "orderconfirm");
            JSONObject notificationObj = new JSONObject();
            notificationObj.put("title", "Rice and Spice Shop");
            notificationObj.put("body", "New Order..");

            JSONObject extraData = new JSONObject();
            extraData.put("brandId", "puma");
            extraData.put("category", "Shoes");


            json.put("notification", notificationObj);
            json.put("data", extraData);


            JsonObjectRequest request = new JsonObjectRequest(com.android.volley.Request.Method.POST, URL,
                    json,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            Log.d("MUR", "onResponse: ");
                            Toast.makeText(getApplicationContext(), "Notification Has Been Sent To User", Toast.LENGTH_SHORT).show();
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("MUR", "onError: " + error.networkResponse);
                }
            }
            ) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> header = new HashMap<>();
                    header.put("content-type", "application/json");
                    header.put("authorization", "key=AAAAQAt1EWo:APA91bGAKWQQOmtCa8xNLXOi2BEiqs0EIVEGsEt4ysBs3emyObcev0h8Y254iMlBngxQDsBG3cMvaJoveQF67e5sQtsQqaM-kJX-glrshdrXTfJruEauKfsLBhdnRKhyC4g6elafYjBI");
                    return header;
                }
            };
            mRequestque.add(request);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getorderdetailsforemail() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Orders");

        listener2 = reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot item : snapshot.getChildren()) {
                    keynew = item.child("foods").getValue(String.class);
                    spinnerdatalistnew.add(key);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void sendMail() {

        Order[] mStringArray = new Order[cartorder.size()];
        mStringArray = cartorder.toArray(mStringArray);

        for (int i = 0; i < mStringArray.length; i++) {
            orderitems = mStringArray[i];
        }

        String mail = "foodscentral4u@gmail.com";
        String message = "*******Order Details******\n" + orderitems;
        String subject = "Order Confirmed By Rice and Spice Shop";

        //Send Mail
        JavaMailAPI javaMailAPI = new JavaMailAPI(this, mail, subject, message);

        javaMailAPI.execute();
       // new Database(getBaseContext()).cleanCart();


    }
}