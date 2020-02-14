package com.app.efarmers.farmers.products.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.app.efarmers.Constant;
import com.app.efarmers.R;
import com.app.efarmers.farmers.products.ProductDescriptionActivity;
import com.app.efarmers.farmers.products.model.Product;
import com.bumptech.glide.Glide;

import java.util.List;

import es.dmoral.toasty.Toasty;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.MyViewHolder> {

    private List<Product> products;
    Context context;

    public ProductAdapter(Context context, List<Product> contacts) {
        this.context = context;
        this.products = contacts;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        String stock=products.get(position).getStock();
        if (stock==null || stock.isEmpty())
        {
            stock="N/A";
            holder.stock.setText("Stock:" +stock);
        }
        else
        {
            holder.stock.setText("Available Stock "+stock+" KG");
        }
        holder.name.setText(products.get(position).getName());

        holder.price.setText(Constant.KEY_CURRENCY+ products.get(position).getPrice());

        String url= Constant.MAIN_URL+"/product_image/"+ products.get(position).getImage();

        Glide.with(context)
                .load(url)
                .placeholder(R.drawable.loading)
                .error(R.drawable.not_found)
                .into(holder.img_product);


    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView name,price,stock;
        ImageView img_product;
        public MyViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.txt_name);
            price = itemView.findViewById(R.id.txt_price);
            stock=itemView.findViewById(R.id.txt_stock);
            img_product=itemView.findViewById(R.id.img_product);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Intent i = new Intent(context, ProductDescriptionActivity.class);
           // i.putExtra("id", products.get(getAdapterPosition()).getId());

            String stock=products.get(getAdapterPosition()).getProductId();
            int get_stock=Integer.parseInt(stock);
            if (get_stock<10){
                Toasty.error(context, "Low stock.You can't order!", Toast.LENGTH_SHORT).show();
            }

            else {

            i.putExtra("id", products.get(getAdapterPosition()).getProductId());
            i.putExtra("name", products.get(getAdapterPosition()).getName());
            i.putExtra("price", products.get(getAdapterPosition()).getPrice());
            i.putExtra("image", products.get(getAdapterPosition()).getImage());
            i.putExtra("description", products.get(getAdapterPosition()).getDescription());
            i.putExtra("farmer_cell", products.get(getAdapterPosition()).getFarmerCell());
            context.startActivity(i);
            Toast.makeText(context, products.get(getAdapterPosition()).getName(), Toast.LENGTH_SHORT).show();

            Log.d("ID"," id: "+products.get(getAdapterPosition()).getProductId());
        }}
    }
}