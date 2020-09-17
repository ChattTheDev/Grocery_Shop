package com.aezorspecialist.groceryshop;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

public class FinalCheckout extends AppCompatActivity implements PaymentResultListener {

    private static final String TAG = FinalCheckout.class.getSimpleName();
    Button paynow;
    TextView t1;
    String fullprice1, totname1, totquants1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_final_checkout);
//

        paynow = findViewById(R.id.razorpaycheck);
        t1 = findViewById(R.id.fullprice);

        totname1 = getIntent().getStringExtra("finalname");
        totquants1 = getIntent().getStringExtra("finalquant");

        fullprice1 = getIntent().getStringExtra("totalprice");

        t1.setText(fullprice1);
//        final DatabaseReference cartlistref = FirebaseDatabase.getInstance().getReference().child("Cart List");
//
//        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//        final String a = user.getUid();


        paynow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                double fp = Double.valueOf(t1.getText().toString());
                if (fp < 100) {
                    Toast.makeText(getApplicationContext(), "Please Order Minimum Rs. 100", Toast.LENGTH_LONG).show();

                } else {
                    startPayment();
                }


            }
        });
    }

    public void startPayment() {
        Checkout checkout = new Checkout();
        checkout.setImage(R.drawable.order);
        final Activity activity = this;
        try {
            JSONObject options = new JSONObject();

            options.put("name", "Rice & Spice Shop");
            options.put("description", "Token Money of 100rs");
            options.put("currency", "INR");
            options.put("amount", 10000);
            checkout.open(activity, options);
        } catch (Exception e) {
            Log.e(TAG, "Error in starting Razorpay Checkout", e);
        }
    }

    @Override
    public void onPaymentSuccess(String s) {
        Toast.makeText(getApplicationContext(), "Payment Successfull", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(getApplicationContext(), OrderConfirmation.class);
        intent.putExtra("finalfullprice", fullprice1);
        intent.putExtra("finalfullname", totname1);
        intent.putExtra("finalfullquants", totquants1);
        startActivity(intent);

    }

    @Override
    public void onPaymentError(int i, String s) {
        try {
            Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(), User_Dashborad.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}