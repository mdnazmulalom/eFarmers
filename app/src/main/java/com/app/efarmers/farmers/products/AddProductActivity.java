package com.app.efarmers.farmers.products;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.efarmers.Constant;
import com.app.efarmers.R;
import com.app.efarmers.farmers.products.model.ProductUpload;
import com.app.efarmers.remote.ApiClient;
import com.app.efarmers.remote.ApiInterface;
import com.app.efarmers.utils.BaseActivity;

import java.io.File;

import es.dmoral.toasty.Toasty;
import in.mayanknagwanshi.imagepicker.ImageSelectActivity;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddProductActivity extends BaseActivity {

    EditText etxtProductName,etxtStock,etxtCategory,etxtPrice,etxtDescription;
    TextView txtChooseImage,txtSubmit;
    ImageView imgProduct;
    String mediaPath,product_name,product_stock,product_category,product_description,product_price,farmerCell;
    ProgressDialog progressDialog;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        etxtProductName=findViewById(R.id.etxt_product_name);
        etxtCategory=findViewById(R.id.etxt_category);
        etxtDescription=findViewById(R.id.etxt_description);
        etxtPrice=findViewById(R.id.etxt_price);
        imgProduct=findViewById(R.id.image_product);
        etxtStock=findViewById(R.id.etxt_stock);

        txtChooseImage=findViewById(R.id.txt_choose_image);
        txtSubmit=findViewById(R.id.txt_submit);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Uploading...");

        //Fetching cell from shared preferences
        sharedPreferences =getSharedPreferences(Constant.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        farmerCell= sharedPreferences.getString(Constant.CELL_SHARED_PREF, "Not Available");


        //For choosing account type and open alert dialog
        etxtCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String[] categoryList = {"Fruits", "Crops","Vegetables"};

                AlertDialog.Builder builder = new AlertDialog.Builder(AddProductActivity.this);
                builder.setTitle("SELECT CATEGORY");
                //builder.setIcon(R.drawable.ic_gender);


                builder.setCancelable(false);
                builder.setItems(categoryList, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int position) {
                        switch (position) {
                            case 0:
                                etxtCategory.setText(categoryList[position]);
                                break;

                            case 1:
                                etxtCategory.setText(categoryList[position]);
                                break;

                            case 2:
                                etxtCategory.setText(categoryList[position]);
                                break;
                            default:
                                break;


                        }
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int position) {
                        dialog.dismiss();
                    }
                });


                AlertDialog categoryDialog = builder.create();

                categoryDialog.show();
            }

        });

        txtChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(AddProductActivity.this, ImageSelectActivity.class);
                intent.putExtra(ImageSelectActivity.FLAG_COMPRESS, true);//default is true
                intent.putExtra(ImageSelectActivity.FLAG_CAMERA, true);//default is true
                intent.putExtra(ImageSelectActivity.FLAG_GALLERY, true);//default is true
                startActivityForResult(intent, 1213);
            }
        });


        txtSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                 product_name=etxtProductName.getText().toString().trim();
                 product_category=etxtCategory.getText().toString().trim();
                 product_description=etxtDescription.getText().toString().trim();
                 product_price=etxtPrice.getText().toString().trim();
                 product_stock=etxtStock.getText().toString();

                if (product_name.isEmpty())
                {
                    etxtProductName.setError("Product name can't empty!");
                    etxtProductName.requestFocus();
                }

               else if (product_category.isEmpty())
                {
                    etxtCategory.setError("Product category can't empty!");
                    etxtCategory.requestFocus();
                }

                else if (product_stock.isEmpty())
                {
                    etxtStock.setError("Input product in KG");
                    etxtStock.requestFocus();
                }

                else if (product_price.isEmpty())
                {
                    etxtPrice.setError("Product price can't empty!");
                    etxtPrice.requestFocus();
                }

                else if (product_description.isEmpty())
                {
                    etxtDescription.setError("Product description can't empty!");
                    etxtDescription.requestFocus();
                }


                else
                {

                    AlertDialog.Builder builder = new AlertDialog.Builder(AddProductActivity.this);
                    builder.setMessage("Want to Add Product ?")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {


                                    // Perform Your Task Here--When Yes Is Pressed.
                                    //call method
                                    uploadFile(product_name,product_category,product_price,product_description);
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



            }
        });




    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            // When an Image is picked
            if (requestCode == 1213 && resultCode == RESULT_OK && null != data) {
                mediaPath = data.getStringExtra(ImageSelectActivity.RESULT_FILE_PATH);
                Bitmap selectedImage = BitmapFactory.decodeFile(mediaPath);
                imgProduct.setImageBitmap(selectedImage);
            }


        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
        }

    }


    // Uploading Image/Video
    private void uploadFile(String name,String category,String price,String description) {
        progressDialog.show();

        // Map is used to multipart the file using okhttp3.RequestBody
        File file = new File(mediaPath);

        // Parsing any Media type file
        RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), file);
        MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
        RequestBody filename = RequestBody.create(MediaType.parse("text/plain"), file.getName());

        RequestBody p_name = RequestBody.create(MediaType.parse("text/plain"), product_name);
        RequestBody p_category = RequestBody.create(MediaType.parse("text/plain"), product_category);
        RequestBody p_stock = RequestBody.create(MediaType.parse("text/plain"), product_stock);
        RequestBody p_price = RequestBody.create(MediaType.parse("text/plain"), product_price);
        RequestBody p_description = RequestBody.create(MediaType.parse("text/plain"), product_description);
        RequestBody f_cell = RequestBody.create(MediaType.parse("text/plain"),farmerCell);


        ApiInterface getResponse = ApiClient.getApiClient().create(ApiInterface.class);
        Call<ProductUpload> call = getResponse.uploadFile(fileToUpload, filename,p_name,p_category,p_stock,p_price,p_description,f_cell);
        call.enqueue(new Callback<ProductUpload>() {
            @Override
            public void onResponse(Call<ProductUpload> call, Response<ProductUpload> response) {
                ProductUpload serverResponse = response.body();
                if (serverResponse != null) {
                    if (serverResponse.getSuccess()) {
                        Toasty.success(getApplicationContext(), serverResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(AddProductActivity.this,AllProductsActivity.class));

                    } else {
                        Toasty.error(getApplicationContext(), serverResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    assert serverResponse != null;
                    Log.v("Response", serverResponse.toString());
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<ProductUpload> call, Throwable t) {

            }
        });
    }
}
