package com.app.efarmers.farmers.products;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
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
import com.app.efarmers.LoginActivity;
import com.app.efarmers.R;
import com.app.efarmers.SignUpActivity;
import com.app.efarmers.farmers.order.OrderDetailsActivity;
import com.app.efarmers.retailers.RetailersHomeActivity;
import com.app.efarmers.utils.BaseActivity;
import com.sslwireless.sslcommerzlibrary.model.initializer.SSLCommerzInitialization;
import com.sslwireless.sslcommerzlibrary.model.response.TransactionInfoModel;
import com.sslwireless.sslcommerzlibrary.model.util.CurrencyType;
import com.sslwireless.sslcommerzlibrary.model.util.SdkType;
import com.sslwireless.sslcommerzlibrary.view.singleton.IntegrateSSLCommerz;
import com.sslwireless.sslcommerzlibrary.viewmodel.listener.TransactionResponseListener;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import es.dmoral.toasty.Toasty;

public class OrderActivity extends BaseActivity implements TransactionResponseListener {

    TextView txtName,txtPrice,txtQuantity,txtOrderSubmit;
    Button btnPlus,btnMinus;
    EditText etxtFullAddress;

    RadioButton rbCashOnDelivery,rbOnlinePayment;

    ProgressDialog loading;
    SharedPreferences sharedPreferences;
    String name,id,price,getPrice,quantity,address,payment_method,userCell,farmer_cell;

    String TAG = "SSL",transactionId;

    int weight=10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        txtName=findViewById(R.id.txt_product_name);
        txtPrice=findViewById(R.id.txt_product_price);
        txtQuantity=findViewById(R.id.txt_weight);
        txtOrderSubmit=findViewById(R.id.txt_submit_order);

        btnMinus=findViewById(R.id.btn_minus);
        btnPlus=findViewById(R.id.btn_plus);

        transactionId = UUID.randomUUID().toString();

        //Fetching cell from shared preferences
        sharedPreferences =getSharedPreferences(Constant.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        userCell = sharedPreferences.getString(Constant.CELL_SHARED_PREF, "Not Available");


        etxtFullAddress=findViewById(R.id.etxt_full_address);

        rbCashOnDelivery=findViewById(R.id.rb_cash_on_delivery);
        rbOnlinePayment=findViewById(R.id.rb_online_payment);



        final String getName=getIntent().getExtras().getString("name");
        getPrice=getIntent().getExtras().getString("price");
        farmer_cell=getIntent().getExtras().getString("farmer_cell");
        id=getIntent().getExtras().getString("id");

        txtName.setText(getName);
        txtPrice.setText(""+Integer.valueOf(getPrice)*10);

        txtOrderSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                name=getName;
                price=txtPrice.getText().toString();
                quantity=txtQuantity.getText().toString();
                address=etxtFullAddress.getText().toString();

                if (rbCashOnDelivery.isChecked())
                {
                    payment_method="cod";
                }
                else {
                    payment_method="online";
                }

                if (address.isEmpty())
                {
                    etxtFullAddress.setError("Enter full address");
                    etxtFullAddress.requestFocus();
                }

                else
                {

                    if (payment_method.equals("online"))
                    {
                        sslPayment(Double.valueOf(price));
                    }
                    else
                    {
                        orderSubmit();
                    }

                }



            }
        });



        btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                weight++;
                txtQuantity.setText(weight+ " KG");

                int mPrice=weight*Integer.valueOf(getPrice);
                txtPrice.setText(""+mPrice);
            }
        });

        btnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (weight<=10)
                {
                    Toasty.warning(OrderActivity.this, "Minimum quantity is 10 Kg", Toast.LENGTH_SHORT).show();
                }
                else
                {
                   weight--;
                    txtQuantity.setText(weight+ " KG");

                    int mPrice=weight*Integer.valueOf(getPrice);
                    txtPrice.setText(""+mPrice);
                }
            }
        });


    }


    private void orderSubmit() {



        //showing progress dialog
//
        loading = new ProgressDialog(OrderActivity.this);
        loading.setMessage("Please wait....");
        loading.show();

        //Creating a string request
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.ORDER_SUBMIT_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        String myResponse=response.trim();


                        //for logcat
                        Log.d("RESPONSE", response);


                        //If we are getting success from server
                        if (myResponse.equals("success")) {


                            loading.dismiss();
                            //Starting profile activity
                            Intent intent = new Intent(OrderActivity.this, RetailersHomeActivity.class);
                            Toasty.success(OrderActivity.this, "Order successful", Toast.LENGTH_SHORT).show();
                            startActivity(intent);

                        }

                        //If we are getting success from server
                        if (myResponse.equals("failure")) {


                            loading.dismiss();

                            Toasty.success(OrderActivity.this, "Order failed!", Toast.LENGTH_SHORT).show();


                        }

                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //You can handle error here if you want

                        Toasty.error(OrderActivity.this, "Error in connection!", Toast.LENGTH_LONG).show();
                        // loading.dismiss();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //Adding parameters to request


                params.put(Constant.KEY_NAME, name);
                params.put(Constant.KEY_PRICE, price);
                params.put(Constant.KEY_QUANTITY, quantity);
                params.put(Constant.KEY_ADDRESS, address);
                params.put(Constant.KEY_PAYMENT_METHOD, payment_method);

                params.put(Constant.KEY_USER_CELL, userCell);
                params.put(Constant.KEY_FARMER_CELL, farmer_cell);

                params.put(Constant.KEY_ID, id);

                //returning parameter
                return params;
            }
        };


        //Adding the string request to the queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);



    }


    public void sslPayment(double amount) {


        final SSLCommerzInitialization sslCommerzInitialization = new SSLCommerzInitialization("onlin5d8f1b93e0a5d",
                "onlin5d8f1b93e0a5d@ssl", amount, CurrencyType.BDT,
                transactionId, "food", SdkType.TESTBOX);


        IntegrateSSLCommerz
                .getInstance(OrderActivity.this)
                .addSSLCommerzInitialization(sslCommerzInitialization)
                .buildApiCall(OrderActivity.this);


    }


    @Override
    public void transactionSuccess(TransactionInfoModel transactionInfoModel) {

        Log.d(TAG,"Success");
        // If payment is success and risk label is 0.
        if (transactionInfoModel.getRiskLevel().equals("0")) {
            Log.d(TAG, "Transaction Successfully completed");
            Log.d("Transaction ID", transactionInfoModel.getTranId());
            Log.d("Bank Trxn ID", transactionInfoModel.getBankTranId());
            Log.d("Pay Amount", transactionInfoModel.getAmount());
            Log.d("Store Amount", transactionInfoModel.getStoreAmount());
            Log.d("Trxn Date", transactionInfoModel.getTranDate());
            Log.d("Status", transactionInfoModel.getStatus());

            orderSubmit();

        }
    }

    @Override
    public void transactionFail(String s) {

    }

    @Override
    public void merchantValidationError(String s) {

    }
}
