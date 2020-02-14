package com.app.efarmers.farmers.products;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.efarmers.Constant;
import com.app.efarmers.R;
import com.app.efarmers.utils.BaseActivity;
import com.bumptech.glide.Glide;

public class ProductDescriptionActivity extends BaseActivity {

    String name,price,image,description,farmer_cell,id;
    ImageView imgProduct;
    TextView txtName,txtPrice,txtDescription,txtOrder;

    SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_description);

        imgProduct=findViewById(R.id.img_product);
        txtName=findViewById(R.id.txt_product_name);
        txtPrice=findViewById(R.id.txt_price);
        txtDescription=findViewById(R.id.txt_description);
        txtOrder=findViewById(R.id.txt_order);


        //Fetching cell from shared preferences
        sharedPreferences =getSharedPreferences(Constant.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String account_type = sharedPreferences.getString(Constant.AC_TYPE_SHARED_PREF, "Not Available");

        if (account_type.equals("Farmers"))
        {
            txtOrder.setVisibility(View.GONE);
        }

        name=getIntent().getExtras().getString("name");
        price=getIntent().getExtras().getString("price");
        description=getIntent().getExtras().getString("description");
        image=getIntent().getExtras().getString("image");
        farmer_cell=getIntent().getExtras().getString("farmer_cell");
        id=getIntent().getExtras().getString("id");

        String url=Constant.MAIN_URL+"/product_image/"+image;

        txtName.setText(name);
        txtPrice.setText(Constant.KEY_CURRENCY+price+"/KG");
        txtDescription.setText(description);

        Glide.with(ProductDescriptionActivity.this)
                .load(url)
                .placeholder(R.drawable.loading)
                .error(R.drawable.not_found)
                .into(imgProduct);



        txtOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ProductDescriptionActivity.this,OrderActivity.class);
                intent.putExtra("name",name);
                intent.putExtra("price",price);
                intent.putExtra("farmer_cell",farmer_cell);
                intent.putExtra("id",id);

                startActivity(intent);

            }
        });

    }
}
