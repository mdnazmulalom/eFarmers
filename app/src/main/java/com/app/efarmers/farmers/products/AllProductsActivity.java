package com.app.efarmers.farmers.products;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.efarmers.Constant;
import com.app.efarmers.R;
import com.app.efarmers.farmers.products.adapter.ProductAdapter;
import com.app.efarmers.farmers.products.model.Product;
import com.app.efarmers.remote.ApiClient;
import com.app.efarmers.remote.ApiInterface;
import com.app.efarmers.utils.BaseActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AllProductsActivity extends BaseActivity {


    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    ProductAdapter productAdapter;
    private List<Product> productList;
    private ApiInterface apiInterface;

    private ProgressBar progressBar;
    SharedPreferences sharedPreferences;
    String cell,getType;
    ImageView imgNoProduct;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_products);

        progressBar = findViewById(R.id.progressBar);
        recyclerView = findViewById(R.id.recycler);
        imgNoProduct=findViewById(R.id.image_no_product);

        getType=getIntent().getExtras().getString("type");

        //Fetching cell from shared preferences
        sharedPreferences =getSharedPreferences(Constant.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        cell = sharedPreferences.getString(Constant.CELL_SHARED_PREF, "Not Available");


        // set a GridLayoutManager with default vertical orientation and 3 number of columns
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(),2);
        recyclerView.setLayoutManager(gridLayoutManager); // set LayoutManager to RecyclerView


//        layoutManager = new LinearLayoutManager(this);
//        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        fetchData("product", "",cell);

    }

    public void fetchData(String type, String key,String cell) {
        Call<List<Product>> call;
        if (getType.equals("farmer")) {
            call = apiInterface.getProduct(type, key, cell);
        }
        else
        {
          call = apiInterface.getAllProduct(type, key, cell);
        }
        call.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                progressBar.setVisibility(View.INVISIBLE);
                productList = response.body();
                Log.d("response", productList.toString());


                if (productList.size()==0)
                {
                    imgNoProduct.setVisibility(View.VISIBLE);
                    imgNoProduct.setImageResource(R.drawable.no_product);
                    productAdapter = new ProductAdapter(AllProductsActivity.this, productList);

                    recyclerView.setAdapter(productAdapter);

                    productAdapter.notifyDataSetChanged();//for search
                }

                else
                {

                    imgNoProduct.setVisibility(View.GONE);

                    productAdapter = new ProductAdapter(AllProductsActivity.this, productList);

                    recyclerView.setAdapter(productAdapter);

                    productAdapter.notifyDataSetChanged();//for search
                }



            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(AllProductsActivity.this, "Error : " + t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        //for hide actionbar item
//        MenuItem itemAdd = (MenuItem) menu.findItem(R.id.add);
//        itemAdd.setVisible(false);
//


        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                fetchData("product", query,cell);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                fetchData("product", newText,cell);
                return false;
            }
        });
        return true;
    }


    //menu item selection
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    //when activity is resumed this method is called
    @Override
    protected void onResume() {
        super.onResume();
        fetchData("product", "",cell);
    }
}