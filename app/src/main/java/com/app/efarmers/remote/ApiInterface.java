package com.app.efarmers.remote;



import com.app.efarmers.Constant;
import com.app.efarmers.farmers.products.model.Product;
import com.app.efarmers.farmers.products.model.ProductUpload;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface ApiInterface {

    @POST("retrofit/POST/readcontacts.php")
    Call<List<Product>> getContacts();

    @FormUrlEncoded
    @POST("retrofit/POST/addcontact.php")
    public Call<Product> insertUser(
            @Field("name") String name,
            @Field("email") String email);

    @FormUrlEncoded
    @POST("retrofit/POST/editcontact.php")
    public Call<Product> editUser(
            @Field("id") String id,
            @Field("name") String name,
            @Field("email") String email);


    @FormUrlEncoded
    @POST("retrofit/POST/deletecontact.php")
    Call<Product> deleteUser(
            @Field("id") int id
    );




       //for live data search
    @GET("android/product.php")
    Call<List<Product>> getProduct(
            @Query("item_type") String item_type,
            @Query("key") String keyword,
            @Query("cell") String cell
    );

    //for live data search
    @GET("android/all_products.php")
    Call<List<Product>> getAllProduct(
            @Query("item_type") String item_type,
            @Query("key") String keyword,
            @Query("cell") String cell
    );



    /* #notes
    -you can use only one annotation par request
       -You can not mix @part or @Field annotation
       -If we use Field in multi part it send whole string with "  "
       -So you use Request body for that to remove  " "

     */

    //for upload image and info
    @Multipart
    @POST("android/upload_product.php")
    Call<ProductUpload> uploadFile(@Part MultipartBody.Part file,
                                   @Part(Constant.KEY_FILE) RequestBody name,
                                   @Part(Constant.KEY_PRODUCT_NAME) RequestBody product_name,
                                   @Part(Constant.KEY_CATEGORY) RequestBody category,
                                   @Part(Constant.KEY_STOCK) RequestBody stock,
                                   @Part(Constant.KEY_PRICE) RequestBody price,
                                   @Part(Constant.KEY_DESCRIPTION) RequestBody description,
                                   @Part(Constant.KEY_FARMER_CELL) RequestBody farmer_cell);
}