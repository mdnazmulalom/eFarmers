package com.app.efarmers.farmers.order;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.efarmers.Constant;
import com.app.efarmers.R;
import com.app.efarmers.retailers.order.OrderListActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import es.dmoral.toasty.Toasty;

public class FarmerOrderActivity extends AppCompatActivity {


    ListView CustomList;
    Button btnSearch;
    EditText etxtSearch;
    private ProgressDialog loading;

    String cell;
    SharedPreferences sharedPreferences;
    int MAX_SIZE=999;
    public String mProductName[]=new String[MAX_SIZE];
    public String mProductPrice[]=new String[MAX_SIZE];
    public String mProductQuantity[]=new String[MAX_SIZE];
    public String mProductStatus[]=new String[MAX_SIZE];
    public String mRetailerCell[]=new String[MAX_SIZE];
    public String mAddress[]=new String[MAX_SIZE];
    public String mTime[]=new String[MAX_SIZE];
    public String mDate[]=new String[MAX_SIZE];
    public String mPaymentMethod[]=new String[MAX_SIZE];
    public String mOrderID[]=new String[MAX_SIZE];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farmer_order);

        CustomList = findViewById(R.id.listView);
        btnSearch = findViewById(R.id.btnSearch);
        etxtSearch = findViewById(R.id.etxt_search);


        getSupportActionBar().setHomeButtonEnabled(true); //for back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//for back button
        getSupportActionBar().setTitle("Order List");


        //Fetching cell from shared preferences
        sharedPreferences = getSharedPreferences(Constant.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        cell = sharedPreferences.getString(Constant.CELL_SHARED_PREF, "Not Available");


        //call function
        getData("");


        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String searchText = etxtSearch.getText().toString().trim();

                if (searchText.isEmpty()) {
                    Toasty.error(FarmerOrderActivity.this, "Please input text!", Toast.LENGTH_SHORT).show();
                } else {
                    getData(searchText);
                }
            }
        });
    }


    private void getData(String s) {

        String getSearchText = s;
        //showing progress dialog
        loading = new ProgressDialog(this);
        loading.setMessage("Please wait....");
        loading.show();

        if (!s.isEmpty()) {
            getSearchText = s;
        }


        String url = Constant.FARMER_ORDER_LIST_URL + cell + "&text=" + getSearchText;

        Log.d("URL", url);
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d("Response", response);
                loading.dismiss();
                showJSON(response);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        loading.dismiss();
                        Toasty.error(FarmerOrderActivity.this, "Network Error!", Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void showJSON(String response) {


        Log.d("Response 2", response);

        JSONObject jsonObject = null;
        ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
        try {
            jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray(Constant.JSON_ARRAY);

            if (result.length() == 0) {
                Toasty.error(FarmerOrderActivity.this, "No Order Found!", Toast.LENGTH_SHORT).show();
                finish();


            } else {
                for (int i = 0; i < result.length(); i++) {
                    JSONObject jo = result.getJSONObject(i);

                    String order_id = jo.getString(Constant.KEY_ORDER_ID);
                    String name = jo.getString(Constant.KEY_NAME);
                    String price = jo.getString(Constant.KEY_PRICE);
                    String quantity = jo.getString(Constant.KEY_QUANTITY);
                    String status = jo.getString(Constant.KEY_STATUS);
                    String retailer_cell = jo.getString(Constant.KEY_RETAILER_CELL);

                    String time = jo.getString(Constant.KEY_TIME);
                    String date = jo.getString(Constant.KEY_DATE);
                    String payment_method = jo.getString(Constant.KEY_PAYMENT_METHOD);
                    String address = jo.getString(Constant.KEY_ADDRESS);

                    mProductName[i]=name;
                    mProductPrice[i]=price;
                    mProductQuantity[i]=quantity;
                    mProductStatus[i]=status;
                    mDate[i]=date;
                    mTime[i]=time;
                    mPaymentMethod[i]=payment_method;
                    mAddress[i]=address;
                    mOrderID[i]=order_id;
                    mRetailerCell[i]=retailer_cell;



                    if (status.equals("0"))
                    {
                        status="Pending";
                    }

                    else if (status.equals("1"))
                    {
                        status="Confirmed";
                    }

                    else if (status.equals("2"))
                    {
                        status="Cancel";
                    }

                    HashMap<String, String> user_msg = new HashMap<>();

                    user_msg.put(Constant.KEY_NAME, name);
                    user_msg.put(Constant.KEY_PRICE, "Price: " + price);
                    user_msg.put(Constant.KEY_QUANTITY, "Quantity: " + quantity);
                    user_msg.put(Constant.KEY_STATUS, "Status: " + status);


                    list.add(user_msg);
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        ListAdapter adapter = new SimpleAdapter(
                FarmerOrderActivity.this, list, R.layout.order_list_items,
                new String[]{Constant.KEY_NAME, Constant.KEY_PRICE, Constant.KEY_QUANTITY, Constant.KEY_STATUS},
                new int[]{R.id.txt_name, R.id.txt_price, R.id.txt_quantity, R.id.txt_status});
        CustomList.setAdapter(adapter);


        CustomList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {



                Intent intent = new Intent(FarmerOrderActivity.this, OrderDetailsActivity.class);
                intent.putExtra("name",mProductName[position]);
                intent.putExtra("price",mProductPrice[position]);
                intent.putExtra("quantity",mProductQuantity[position]);

                intent.putExtra("status",mProductStatus[position]);
                intent.putExtra("retailer_cell",mRetailerCell[position]);

                intent.putExtra("address",mAddress[position]);
                intent.putExtra("time",mTime[position]);
                intent.putExtra("date", mDate[position]);
                intent.putExtra("payment_method",mPaymentMethod[position]);
                intent.putExtra("order_id",mOrderID[position]);


                startActivity(intent);

            }
        });


    }


    //for back button
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
