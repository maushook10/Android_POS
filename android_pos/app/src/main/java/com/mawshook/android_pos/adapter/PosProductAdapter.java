package com.mawshook.android_pos.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mawshook.android_pos.Constant;
import com.mawshook.android_pos.R;
import com.mawshook.android_pos.database.DatabaseAccess;
import com.mawshook.android_pos.model.Product;
import com.bumptech.glide.Glide;

import java.util.List;

import es.dmoral.toasty.Toasty;

public class PosProductAdapter extends RecyclerView.Adapter<PosProductAdapter.MyViewHolder> {


    private List<Product> productData;
    private Context context;
    MediaPlayer player;
    DatabaseAccess databaseAccess;
    SharedPreferences sp;
    String currency;



    public PosProductAdapter(Context context, List<Product> productData) {
        this.context = context;
        this.productData = productData;
        player = MediaPlayer.create(context, R.raw.delete_sound);
        databaseAccess = DatabaseAccess.getInstance(context);
        sp = context.getSharedPreferences(Constant.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        currency = sp.getString(Constant.SP_CURRENCY_SYMBOL, "");


    }


    @NonNull
    @Override
    public PosProductAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pos_product_item, parent, false);
        return new MyViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final PosProductAdapter.MyViewHolder holder, int position) {





        String productId = productData.get(position).getProductId();
        String productName = productData.get(position).getProductName();
        String productWeight = productData.get(position).getProductWeight();
        String productPrice = productData.get(position).getProductSellPrice();
        String weightUnit = productData.get(position).getProductWeightUnit();
        String productImage = productData.get(position).getProductImage();
        String productStock = productData.get(position).getProductStock();

        String tax = productData.get(position).getTax();

        String imageUrl= Constant.PRODUCT_IMAGE_URL+productImage;


        holder.txtProductName.setText(productName);
        holder.txtWeight.setText(productWeight + " " + weightUnit);
        holder.txtPrice.setText(currency + productPrice);


        double itemPrice=Double.parseDouble(productPrice);
        double getTax=Double.parseDouble(tax);



        double taxAmount=(itemPrice*getTax)/100;


        holder.txtTax.setText(context.getString(R.string.tax)+" "+currency+taxAmount+ " (" +tax+"%)");



        //Low stock marked as RED color
        int getStock=Integer.parseInt(productStock);
        if (getStock>5) {
            holder.txtStock.setText(context.getString(R.string.stock) + " : " + productStock);
            holder.txtStockStatus.setVisibility(View.VISIBLE);

            holder.txtStockStatus.setBackgroundColor(Color.parseColor("#43a047"));
            holder.txtStockStatus.setText(context.getString(R.string.in_stock));
        }
       else if (getStock==0) {
            holder.txtStock.setText(context.getString(R.string.stock) + " : " + productStock);
           
            holder.txtStock.setTextColor(Color.RED);

            holder.txtStockStatus.setVisibility(View.VISIBLE);
            holder.txtStockStatus.setText(context.getString(R.string.not_available));

        }
        else
        {
            holder.txtStock.setText(context.getString(R.string.stock) + " : " + productStock);
            holder.txtStock.setTextColor(Color.RED);
            holder.txtStockStatus.setVisibility(View.VISIBLE);
            holder.txtStockStatus.setText(context.getString(R.string.low_stock));


        }



        if (productImage != null) {
            if (productImage.length() < 3) {

                holder.productImage.setImageResource(R.drawable.image_placeholder);
            } else {


                Glide.with(context)
                        .load(imageUrl)
                        .placeholder(R.drawable.loading)
                        .error(R.drawable.image_placeholder)
                        .into(holder.productImage);

            }
        }



        holder.btnAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (getStock<=0)
                {

                    Toasty.warning(context, R.string.stock_not_available_please_update_stock, Toast.LENGTH_SHORT).show();
                }

                else {

                    databaseAccess.open();

                    int check = databaseAccess.addToCart(productId,productName, productWeight, weightUnit, productPrice, 1,productImage,productStock,taxAmount);

                    if (check == 1) {
                        Toasty.success(context, R.string.product_added_to_cart, Toast.LENGTH_SHORT).show();
                        player.start();
                    } else if (check == 2) {

                        Toasty.info(context, R.string.product_already_added_to_cart, Toast.LENGTH_SHORT).show();

                    } else {

                        Toasty.error(context, R.string.product_added_to_cart_failed_try_again, Toast.LENGTH_SHORT).show();

                    }
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return productData.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        Button btnAddToCart;
        TextView txtProductName, txtWeight, txtPrice,txtStock,txtStockStatus, txtTax;

        ImageView productImage;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            txtProductName = itemView.findViewById(R.id.txt_product_name);
            txtWeight = itemView.findViewById(R.id.txt_weight);
            txtStock = itemView.findViewById(R.id.txt_stock);
            txtPrice = itemView.findViewById(R.id.txt_price);
            productImage = itemView.findViewById(R.id.img_product);
            btnAddToCart = itemView.findViewById(R.id.btn_add_cart);
            txtStockStatus=itemView.findViewById(R.id.txt_stock_status);
            txtTax =itemView.findViewById(R.id.txt_tax);


        }
    }


}
