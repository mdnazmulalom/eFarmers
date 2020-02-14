package com.app.efarmers.govt;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.app.efarmers.Constant;
import com.app.efarmers.R;
import com.app.efarmers.farmers.products.AllProductsActivity;
import com.app.efarmers.govt.add_notice.AddNoticeActivity;
import com.app.efarmers.govt.view_notice.ViewNoticeActivity;
import com.app.efarmers.retailers.RetailersHomeActivity;

import es.dmoral.toasty.Toasty;

public class GovtHomeActivity extends AppCompatActivity {

    CardView cardAddNotice,cardViewNotice,cardLogout,cardProduct;
    SharedPreferences sp;
    SharedPreferences.Editor editor;

    //for double back press to exit
    private static final int TIME_DELAY = 2000;
    private static long back_pressed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_govt_home);

        cardAddNotice=findViewById(R.id.card_notice);
        cardViewNotice=findViewById(R.id.card_view_notice);
        cardLogout=findViewById(R.id.card_logout);
        cardProduct=findViewById(R.id.card_product);

        sp = getSharedPreferences(Constant.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        editor = sp.edit();



        cardAddNotice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(GovtHomeActivity.this, AddNoticeActivity.class);
                startActivity(intent);
            }
        });

        cardViewNotice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(GovtHomeActivity.this, ViewNoticeActivity.class);
                startActivity(intent);
            }
        });

        cardProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(GovtHomeActivity.this, AllProductsActivity.class);
                intent.putExtra("type","govt");
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
