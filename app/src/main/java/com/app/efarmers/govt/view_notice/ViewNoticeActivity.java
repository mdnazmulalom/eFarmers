package com.app.efarmers.govt.view_notice;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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

import androidx.appcompat.app.AppCompatActivity;

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
import com.app.efarmers.govt.add_notice.AddNoticeActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class ViewNoticeActivity extends AppCompatActivity {

    ListView CustomList;
    Button btnSearch;
    EditText etxtSearch;
    private ProgressDialog loading;

    int Max_Size=999;
    public String noticeID[]=new String[Max_Size];

    SharedPreferences sharedPreferences;
    String get_ac_type;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_notice);

        CustomList = findViewById(R.id.listView);
        btnSearch = findViewById(R.id.btnSearch);
        etxtSearch = findViewById(R.id.etxt_search);


        getSupportActionBar().setHomeButtonEnabled(true); //for back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//for back button
        getSupportActionBar().setTitle("All Notice");



        //Fetching cell from shared preferences
        sharedPreferences = getSharedPreferences(Constant.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String cell = sharedPreferences.getString(Constant.CELL_SHARED_PREF, "Not Available");
        String get_password = sharedPreferences.getString(Constant.PASSWORD_SHARED_PREF, "0");
        get_ac_type = sharedPreferences.getString(Constant.AC_TYPE_SHARED_PREF, "Not Available");

        //call function
        getData("");


        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String searchText = etxtSearch.getText().toString().trim();

                if (searchText.isEmpty()) {
                    Toasty.error(ViewNoticeActivity.this, "Please input text!", Toast.LENGTH_SHORT).show();
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


        String url = Constant.NOTICE_LIST_URL +"?text=" + getSearchText;

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
                        Toasty.error(ViewNoticeActivity.this, "Network Error!", Toast.LENGTH_LONG).show();
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
                Toasty.error(ViewNoticeActivity.this, "No Notice Found!", Toast.LENGTH_SHORT).show();


            } else {
                for (int i = 0; i < result.length(); i++) {
                    JSONObject jo = result.getJSONObject(i);

                    String id = jo.getString(Constant.KEY_ID);
                    String title = jo.getString(Constant.KEY_TITLE);
                    String description = jo.getString(Constant.KEY_DESCRIPTION);
                    String time = jo.getString(Constant.KEY_TIME);
                    String date = jo.getString(Constant.KEY_DATE);


                    noticeID[i] = id;

                    HashMap<String, String> user_msg = new HashMap<>();

                    user_msg.put(Constant.KEY_TITLE, title);
                    user_msg.put(Constant.KEY_DESCRIPTION, description);
                    user_msg.put(Constant.KEY_TIME, "Time: " + time);
                    user_msg.put(Constant.KEY_DATE, "Date: " + date);


                    list.add(user_msg);
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        ListAdapter adapter = new SimpleAdapter(
                ViewNoticeActivity.this, list, R.layout.notice_list_items,
                new String[]{Constant.KEY_TITLE, Constant.KEY_DESCRIPTION, Constant.KEY_TIME, Constant.KEY_DATE},
                new int[]{R.id.txt_title, R.id.txt_description, R.id.txt_time, R.id.txt_date});
        CustomList.setAdapter(adapter);

        if (get_ac_type.equals("Govt")) {
            CustomList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ViewNoticeActivity.this);
                    builder.setIcon(R.mipmap.ic_launcher)
                            .setMessage("Want to delete notice ?")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {


                                    // Perform Your Task Here--When Yes Is Pressed.
                                    delete_news(noticeID[position]); //call publish_news function
                                    dialog.cancel();
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // Perform Your Task Here--When No is pressed
                                    dialog.cancel();
                                }
                            }).show();


                    return true;
                }
            });

        }
    }



    //news
  
        //Delete method for deleting contacts
        public void delete_news(final String getID ) {
        loading = new ProgressDialog(this);
        // loading.setIcon(R.drawable.wait_icon);
        loading.setTitle("Delete");
        loading.setMessage("Please wait....");
        loading.show();

        String URL = Constant.NOTICE_DELETE_URL;


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

                            Intent intent = new Intent(ViewNoticeActivity.this, GovtHomeActivity.class);
                            Toasty.success(ViewNoticeActivity.this, " Successfully Deleted!", Toast.LENGTH_SHORT).show();
                            startActivity(intent);

                        }


                        //If we are getting success from server
                        else if (response.equals("failure")) {

                            loading.dismiss();
                            //Starting profile activity


                            Toasty.error(ViewNoticeActivity.this, " Delete fail!", Toast.LENGTH_SHORT).show();


                        } else {

                            loading.dismiss();
                            Toast.makeText(ViewNoticeActivity.this, "Network Error", Toast.LENGTH_SHORT).show();

                        }

                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //You can handle error here if you want

                        Toast.makeText(ViewNoticeActivity.this, "No Internet Connection or \nThere is an error !!!", Toast.LENGTH_LONG).show();
                        loading.dismiss();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //Adding parameters to request

                params.put(Constant.KEY_ID, getID);

                Log.d("ID", getID);

                //returning parameter
                return params;
            }
        };

        //Adding the string request to the queue
        RequestQueue requestQueue = Volley.newRequestQueue(ViewNoticeActivity.this);
        requestQueue.add(stringRequest);

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