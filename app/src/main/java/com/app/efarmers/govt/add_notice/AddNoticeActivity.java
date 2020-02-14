package com.app.efarmers.govt.add_notice;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
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
import com.app.efarmers.govt.GovtHomeActivity;

import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class AddNoticeActivity extends AppCompatActivity {


    EditText etxtTitle, etxtDescription;
    TextView txtSubmit;
    ProgressDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_notice);

        etxtTitle = findViewById(R.id.etxt_title);
        etxtDescription = findViewById(R.id.etxt_description);
        txtSubmit = findViewById(R.id.txt_submit);


        getSupportActionBar().setHomeButtonEnabled(true); //for back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//for back button
        getSupportActionBar().setTitle("Add Notice");

        txtSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AddNoticeActivity.this);
                builder.setIcon(R.mipmap.ic_launcher)
                        .setMessage("Want to publish ?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {


                                // Perform Your Task Here--When Yes Is Pressed.
                                publish_news(); //call publish_news function
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


    //Save contact method
    public void publish_news() {

        final String title = etxtTitle.getText().toString();
        final String description = etxtDescription.getText().toString();


        if (title.isEmpty()) {

            etxtTitle.setError("Add title!");
            etxtTitle.requestFocus();
        } else if (description.isEmpty()) {
           etxtDescription.setError("Add description");
           etxtDescription.requestFocus();

        } else {
            loading = new ProgressDialog(this);
            loading.setMessage("Please wait....");
            loading.show();

            String URL = Constant.PUBLISH_NOTICE_URL;


            //Creating a string request
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {


                            //for track response in logcat
                            Log.d("RESPONSE", response);
                            // Log.d("RESPONSE", userCell);


                            //If we are getting success from server
                            if (response.equals("success")) {

                                loading.dismiss();
                                //Starting profile activity

                                Intent intent = new Intent(AddNoticeActivity.this, GovtHomeActivity.class);
                                Toasty.success(AddNoticeActivity.this, " Successfully Saved!", Toast.LENGTH_SHORT).show();
                                startActivity(intent);

                            }


                            //If we are getting success from server
                            else if (response.equals("failure")) {

                                loading.dismiss();
                                //Starting profile activity

                                //Intent intent = new Intent(AddNoticeActivity.this, HomeActivity.class);
                                Toasty.success(AddNoticeActivity.this, " Save fail!", Toast.LENGTH_SHORT).show();
                                //startActivity(intent);

                            } else {

                                loading.dismiss();
                                Toast.makeText(AddNoticeActivity.this, "Network Error", Toast.LENGTH_SHORT).show();

                            }

                        }
                    },

                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            //You can handle error here if you want

                            Toast.makeText(AddNoticeActivity.this, "No Internet Connection or \nThere is an error !!!", Toast.LENGTH_LONG).show();
                            loading.dismiss();
                        }
                    }) {

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    //Adding parameters to request


                    params.put(Constant.KEY_TITLE, title);
                    params.put(Constant.KEY_DESCRIPTION, description);

                    //returning parameter
                    return params;
                }
            };


            //Adding the string request to the queue
            RequestQueue requestQueue = Volley.newRequestQueue(AddNoticeActivity.this);
            requestQueue.add(stringRequest);
        }

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
