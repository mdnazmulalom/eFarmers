package com.app.efarmers.retailers;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.app.efarmers.Constant;
import com.app.efarmers.R;
import com.app.efarmers.farmers.products.AllProductsActivity;
import com.app.efarmers.retailers.order.OrderListActivity;
import com.app.efarmers.retailers.profile.RetailersProfileActivity;

import es.dmoral.toasty.Toasty;

public class RetailersHomeActivity extends AppCompatActivity {


   CardView cardProfile,cardProducts,cardMyOrder,cardLogout;

    SharedPreferences sp;
    SharedPreferences.Editor editor;

    //for double back press to exit
    private static final int TIME_DELAY = 2000;
    private static long back_pressed;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retailers_home);
        getSupportActionBar().setTitle("Retailers Panel");


        cardProfile=findViewById(R.id.card_profile);
        cardLogout=findViewById(R.id.card_logout);
        cardProducts=findViewById(R.id.card_products);
        cardMyOrder=findViewById(R.id.card_my_order);



        sp = getSharedPreferences(Constant.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        editor = sp.edit();


        cardProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(RetailersHomeActivity.this, RetailersProfileActivity.class);
                startActivity(intent);
            }
        });

        cardMyOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(RetailersHomeActivity.this, OrderListActivity.class);
                startActivity(intent);
            }
        });


        cardProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(RetailersHomeActivity.this, AllProductsActivity.class);
                intent.putExtra("type","retailer");
                startActivity(intent);
            }
        });
        cardLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.clear();
                editor.apply();
                finishAffinity();
            }
        });

    }

    //double backpress to exit
    @Override
    public void onBackPressed() {
        if (back_pressed + TIME_DELAY > System.currentTimeMillis()) {

            Intent intent=new Intent();
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            finish();

        } else {
            Toasty.info(this, "Press once again to exit!",
                    Toast.LENGTH_SHORT).show();
        }
        back_pressed = System.currentTimeMillis();
    }

}
