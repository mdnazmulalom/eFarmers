package com.app.efarmers.farmers.order;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.efarmers.Constant;
import com.app.efarmers.R;
import com.app.efarmers.farmers.FarmersHomeActivity;
import com.app.efarmers.farmers.products.OrderActivity;
import com.app.efarmers.utils.BaseActivity;

import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class OrderDetailsActivity extends BaseActivity {

    String  name,getStatus,order_id,date,time,price,quantity,status,retailer_cell,payment_method,address;

    TextView txtRetailerCell,txtOrderStatus,txtTimeDate,txtOrderId,txtProductName,txtProductPrice,txtProductQuantity,txtFullAddress, txtPaymentMethod,txtConfirmOrder,txtCancelOrder;

    ProgressDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);

        txtProductName=findViewById(R.id.txt_product_name);
        txtProductPrice=findViewById(R.id.txt_product_price);
        txtProductQuantity=findViewById(R.id.txt_quantity);
        txtRetailerCell=findViewById(R.id.txt_call_retailer);
        txtOrderId=findViewById(R.id.txt_order_id);
        txtFullAddress=findViewById(R.id.txt_full_address);
        txtPaymentMethod =findViewById(R.id.txt_payment_method);
        txtTimeDate=findViewById(R.id.txt_time_date);
        txtOrderStatus=findViewById(R.id.txt_order_status);
        txtConfirmOrder=findViewById(R.id.txt_confirm_order);
        txtCancelOrder=findViewById(R.id.txt_cancel_order);

        order_id=getIntent().getExtras().getString("order_id");
        name=getIntent().getExtras().getString("name");
        quantity=getIntent().getExtras().getString("quantity");
        price=getIntent().getExtras().getString("price");
        status=getIntent().getExtras().getString("status");
        retailer_cell=getIntent().getExtras().getString("retailer_cell");
        payment_method=getIntent().getExtras().getString("payment_method");
        address=getIntent().getExtras().getString("address");
        date=getIntent().getExtras().getString("date");
        time=getIntent().getExtras().getString("time");




        txtOrderId.setText(getString(R.string.order_id)+" : "+order_id);
        txtProductName.setText(name);
        txtProductPrice.setText("Tk. "+price);
        txtProductQuantity.setText(quantity);
        txtFullAddress.setText(address);
        txtPaymentMethod.setText(payment_method);
        txtTimeDate.setText(getString(R.string.time_date)+" : "+time+ " "+date);



        if (status.equals("0"))
        {
            txtOrderStatus.setText("Order Pending");

        }

        else if (status.equals("1"))
        {



            txtOrderStatus.setText("Order Confirmed");
            txtCancelOrder.setVisibility(View.GONE);
            txtConfirmOrder.setVisibility(View.GONE);
        }

        else if (status.equals("2"))
        {
            txtOrderStatus.setText("Cancel");
            txtCancelOrder.setVisibility(View.GONE);
            txtConfirmOrder.setVisibility(View.GONE);
            txtRetailerCell.setVisibility(View.GONE);
        }



        txtRetailerCell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + retailer_cell));
                startActivity(intent);

            }
        });





        txtConfirmOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                AlertDialog.Builder builder = new AlertDialog.Builder(OrderDetailsActivity.this);
                builder.setMessage("Want to confirmed order ?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {


                                // Perform Your Task Here--When Yes Is Pressed.
                                UpdateOrder("1");
                                dialog.cancel();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Perform Your Task Here--When No is pressed
                                dialog.cancel();
                            }
                        }).show();





            }
        });

        txtCancelOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                AlertDialog.Builder builder = new AlertDialog.Builder(OrderDetailsActivity.this);
                builder.setMessage("Want to cancel order ?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {


                                // Perform Your Task Here--When Yes Is Pressed.
                                UpdateOrder("2");
                                dialog.cancel();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Perform Your Task Here--When No is pressed
                                dialog.cancel();
                            }
                        }).show();



            }
        });
    }





    //update contact method
    public void UpdateOrder(String s) {

        getStatus=s;


        loading = new ProgressDialog(this);
        // loading.setIcon(R.drawable.wait_icon);
        loading.setTitle("Update");
        loading.setMessage("Please wait....");
        loading.show();

        String URL = Constant.UPDATE_ORDER_URL;


        //Creating a string request
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        //for track response in logcat
                        Log.d("RESPONSE", response);
                        // Log.d("RESPONSE", userCell);

                        String getResponse=response.trim();

                        //If we are getting success from server
                        if (getResponse.equals("success")) {

                            loading.dismiss();
                            //Starting profile activity

                            Intent intent = new Intent(OrderDetailsActivity.this, FarmersHomeActivity.class);

                            if (getStatus.equals("1"))
                                Toasty.success(OrderDetailsActivity.this, " Order Successfully Confirmed!", Toast.LENGTH_SHORT).show();
                            else if (getStatus.equals("2"))
                                Toasty.error(OrderDetailsActivity.this, " Order Cancel!", Toast.LENGTH_SHORT).show();


                            startActivity(intent);

                        }


                        //If we are getting success from server
                        else if (getResponse.equals("failure")) {

                            loading.dismiss();
                            //Starting profile activity

                            Intent intent = new Intent(OrderDetailsActivity.this, FarmerOrderActivity.class);
                            Toast.makeText(OrderDetailsActivity.this, " Update fail!", Toast.LENGTH_SHORT).show();
                            //startActivity(intent);

                        } else {

                            loading.dismiss();
                            Toast.makeText(OrderDetailsActivity.this, "Network Error", Toast.LENGTH_SHORT).show();

                        }

                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //You can handle error here if you want

                        Toast.makeText(OrderDetailsActivity.this, "No Internet Connection or \nThere is an error !!!", Toast.LENGTH_LONG).show();
                        loading.dismiss();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //Adding parameters to request

                params.put(Constant.KEY_ID, order_id);
                params.put(Constant.KEY_STATUS, getStatus);


                //returning parameter
                return params;
            }
        };


        //Adding the string request to the queue
        RequestQueue requestQueue = Volley.newRequestQueue(OrderDetailsActivity.this);
        requestQueue.add(stringRequest);


    }

}
