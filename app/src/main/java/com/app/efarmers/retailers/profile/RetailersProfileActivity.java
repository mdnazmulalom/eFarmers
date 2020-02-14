package com.app.efarmers.retailers.profile;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.efarmers.Constant;
import com.app.efarmers.R;
import com.app.efarmers.farmers.profile.EditProfileActivity;
import com.app.efarmers.farmers.profile.ProfileActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import es.dmoral.toasty.Toasty;

public class RetailersProfileActivity extends AppCompatActivity {


    TextView txtEdit,txtDonateDate,txtName,txtName2,txtGender,txtProfession,txtCell,txtEmail,txtLocation,txtPassword;

    String UserCell;

    private ProgressDialog loading;

    SharedPreferences sharedPreferences;

    ImageView profilePic;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retailers_profile);

        //Fetching cell from shared preferences
        sharedPreferences =getSharedPreferences(Constant.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String cell = sharedPreferences.getString(Constant.CELL_SHARED_PREF, "Not Available");
        UserCell = cell;
        getSupportActionBar().setHomeButtonEnabled(true); //for back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//for back button
        getSupportActionBar().setTitle("Profile");


        txtName=findViewById(R.id.txtName);
        txtName2=findViewById(R.id.txtName2);
        txtCell=findViewById(R.id.txtCell);
        txtGender=findViewById(R.id.txtGender);
        txtLocation=findViewById(R.id.txtLocation);


        profilePic=findViewById(R.id.profile_image);
        txtEdit=findViewById(R.id.txtEdit);

        txtEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(RetailersProfileActivity.this, EditProfileActivity.class);
                intent.putExtra("name",txtName.getText());
                intent.putExtra("cell",txtCell.getText());
                intent.putExtra("location",txtLocation.getText());
                intent.putExtra("gender",txtGender.getText());
                startActivity(intent);
            }
        });

        //call function
        getData();

    }



    private void getData() {

        //loading = ProgressDialog.show(this,"Please wait...","Fetching...",false,false);

        //showing progress dialog
        loading = new ProgressDialog(RetailersProfileActivity.this);
        loading.setMessage("Please wait....");
        loading.show();


        String url = Constant.RETAILERS_PROFILE_URL+ UserCell;  // url for connecting php file

        Log.d("URL",url);
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                loading.dismiss();
                showJSON(response);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Log.d("Volley Error","Error:"+error);
                        Toasty.error(RetailersProfileActivity.this, "No Internet Connection!", Toast.LENGTH_LONG).show();
                        loading.dismiss();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(RetailersProfileActivity.this);
        requestQueue.add(stringRequest);
    }



    private void showJSON(String response) {


        Log.d("RESPONSE",response);

        String name = "";
        String gender = "";
        String cell = "";
        String location="";
        String donate_date="";


        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray(Constant.JSON_ARRAY);
            JSONObject ProfileData = result.getJSONObject(0);

            name = ProfileData.getString(Constant.KEY_NAME);
            gender = ProfileData.getString(Constant.KEY_GENDER);
            cell = ProfileData.getString(Constant.KEY_CELL);
            location = ProfileData.getString(Constant.KEY_LOCATION);




        } catch (JSONException e) {
            e.printStackTrace();
        }

        //textViewResult.setText("Name:\t"+name+"\nAddress:\t" +address+ "\nVice Chancellor:\t"+ vc);

        txtName.setText(name);
        txtName2.setText(name);
        txtGender.setText(gender);

//        if (gender.equals("Female"))
//        {
//            profilePic.setImageResource(R.drawable.girl);
//        }





        txtCell.setText(cell);
        txtLocation.setText(location);

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

