package com.aezorspecialist.groceryshop;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.aezorspecialist.groceryshop.Database.Database;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.razorpay.Checkout;

public class User_Dashborad extends AppCompatActivity {


    TextView t1;
    View hview;
    FirebaseAuth mAuth;
    BottomNavigationView bottomNav;
    DatabaseReference reference;
    int countcart = 0;
    int countorder = 0;
    long back_pressed;
    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;
                    switch (item.getItemId()) {
                        case R.id.nav_home:
                            selectedFragment = new HomeFragment();
                            break;
                        case R.id.cart:
                            selectedFragment = new MyCartFragment();
                            BadgeDrawable badgeDrawable = bottomNav.getOrCreateBadge(R.id.cart);
                            badgeDrawable.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark));
                            badgeDrawable.setVisible(true);
                            Intent intent = new Intent(getApplicationContext(), CartPage.class);
                            startActivity(intent);
                            int bc = new Database(User_Dashborad.this).getcouncart();
                            badgeDrawable.setNumber(bc);
                            break;
                        case R.id.orders:
                            selectedFragment = new SearchFragment();
                            BadgeDrawable badgeDrawable1 = bottomNav.getOrCreateBadge(R.id.orders);
                            badgeDrawable1.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark));
                            badgeDrawable1.setVisible(true);
                            break;
                        case R.id.profile:
                            selectedFragment = new ProfileFragment();
                            break;
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            selectedFragment).commit();
                    return true;
                }
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_user__dashborad);
        //Checkout.preload(getApplicationContext());
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Rice & Spice Shop");
//        DrawerLayout drawer = findViewById(R.id.drawer_layout);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawer.setDrawerListener(toggle);
//        toggle.syncState();
//        NavigationView navigationView = findViewById(R.id.nav_view);
//        hview = navigationView.getHeaderView(0);
//        navigationView.setNavigationItemSelectedListener(this);
//        navigationView.getMenu().getItem(0).setChecked(true);
//
//        t1 = hview.findViewById(R.id.emailname);
        //loginact();
        //FirebaseMessaging.getInstance().subscribeToTopic("orderstatus");

        bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String a = user.getUid();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new HomeFragment()).commit();
        }


        reference = FirebaseDatabase.getInstance().getReference().child("Cart List");

        //for getting orders count

        reference.child("Admin View").child(a).child("Products").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                countorder = (int) snapshot.getChildrenCount();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //for getting cartitem count

        reference.child("User View").child(a).child("Products").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                countcart = (int) snapshot.getChildrenCount();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

//        viewPager2 = findViewById(R.id.viewpager);
//        viewPager2.setAdapter(new OrdersPageAdapter(User_Dashborad.this));
//        tabLayout = findViewById(R.id.tablayout);
//        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(tabLayout, viewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
//            @Override
//            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
//                switch (position) {
//                    case 0:
//                        tab.setText("Home");
//                        tab.setIcon(R.drawable.ic_baseline_home_24);
//                        break;
//                    case 1:
//                        tab.setText("My Cart");
//                        tab.setIcon(R.drawable.ic_baseline_shopping_cart_24);
//                        BadgeDrawable badgeDrawable = tab.getOrCreateBadge();
//                        badgeDrawable.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark));
//                        badgeDrawable.setVisible(true);
//                        break;
//                    case 2:
//                        tab.setText("My Orders");
//                        tab.setIcon(R.drawable.ic_baseline_label_important_24);
//                        break;
//                    case 3:
//                        tab.setText("Profile");
//                        tab.setIcon(R.drawable.ic_baseline_person_24);
//                        break;
//                }
//
//            }
//        });
//        tabLayoutMediator.attach();
//
//        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
//            @Override
//            public void onPageSelected(int position) {
//                BadgeDrawable badgeDrawable = tabLayout.getTabAt(position).getOrCreateBadge();
//                badgeDrawable.setVisible(false);
//            }
//        });


    }

//    private void loginact() {
//        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//        if (user != null) {
//
//
//            String email = user.getEmail();
//            t1.setText("" + email);
//        }
//    }

//    @SuppressWarnings("StatementWithEmptyBody")
//    @Override
//    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//
//        int id = item.getItemId();
//
//        if (id == R.id.nav_mycart) {
//            Intent intent = new Intent(getApplicationContext(), CartPage.class);
//            startActivity(intent);
//
//        } else if (id == R.id.nav_myorders) {
//
//            Intent intent = new Intent(getApplicationContext(), MyOrders.class);
//            startActivity(intent);
//
//
//        } else if (id == R.id.nav_signout) {
//            AlertDialog.Builder builder1 = new AlertDialog.Builder(User_Dashborad.this);
//            builder1.setMessage("Are You Sure?");
//            builder1.setCancelable(true);
//
//            builder1.setPositiveButton(
//                    "Yes",
//                    new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int id) {
//                            mAuth = FirebaseAuth.getInstance();
//                            mAuth.signOut();
//                            Intent i3 = new Intent(getApplicationContext(), CustomerLoginActivity.class);
//                            startActivity(i3);
//                            finishAffinity();
//
//
//                        }
//                    });
//
//            builder1.setNegativeButton(
//                    "No",
//                    new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int id) {
//                            dialog.cancel();
//                        }
//                    });
//
//            AlertDialog alert11 = builder1.create();
//            alert11.show();
//
//        }
//
//        DrawerLayout drawer = findViewById(R.id.drawer_layout);
//        drawer.closeDrawer(GravityCompat.START);
//        return true;
//    }

    @Override
    public void onBackPressed() {

        if (back_pressed + 1000 > System.currentTimeMillis()){
            super.onBackPressed();
            Intent startMain = new Intent(Intent.ACTION_MAIN);
            startMain.addCategory(Intent.CATEGORY_HOME);
            startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(startMain);
        }
        else{
            Toast.makeText(getBaseContext(),
                    "Press once again to exit!", Toast.LENGTH_SHORT)
                    .show();
        }
        back_pressed = System.currentTimeMillis();





    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//        return super.onOptionsItemSelected(item);
//    }

}