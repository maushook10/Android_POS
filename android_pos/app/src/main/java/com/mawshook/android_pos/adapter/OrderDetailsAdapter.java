package com.mawshook.android_pos.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mawshook.android_pos.Constant;
import com.mawshook.android_pos.R;
import com.mawshook.android_pos.model.OrderDetails;
import com.bumptech.glide.Glide;

import java.util.List;

public class OrderDetailsAdapter extends RecyclerView.Adapter<OrderDetailsAdapter.MyViewHolder> {


    Context context;
    private List<OrderDetails> orderData;
    public static double subTotalPrice=0;
    SharedPreferences sp;
    String currency;



    public OrderDetailsAdapter(Context context, List<OrderDetails> orderData) {
        this.context = context;
        this.orderData = orderData;
        sp = context.getSharedPreferences(Constant.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        currency = sp.getString(Constant.SP_CURRENCY_SYMBOL, "");


    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_details_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        holder.txtProductName.setText(orderData.get(position).getProductName());

        holder.txtProductQty.setText(context.getString(R.string.quantity) + orderData.get(position).getProductQuantity());

        String productWeight = orderData.get(position).getProductWeight();
        holder.txtProductWeight.setText(productWeight);

        String productImage = orderData.get(position).getProductImage();
        String imageUrl= Constant.PRODUCT_IMAGE_URL+productImage;



        String unitPrice = orderData.get(position).getProductPrice();
        String qty = orderData.get(position).getProductQuantity();
        double price = Double.parseDouble(unitPrice);
        int quantity = Integer.parseInt(qty);
        double cost = quantity * price;

        subTotalPrice=subTotalPrice+cost;
        Log.d("sub_total",""+subTotalPrice);



        holder.txtTotalCost.setText(currency + unitPrice + " x " + qty + " = " + currency + cost);

        if (productImage != null) {
            if (productImage.isEmpty()) {
                holder.imgProduct.setImageResource(R.drawable.image_placeholder);
                holder.imgProduct.setScaleType(ImageView.ScaleType.FIT_CENTER);
            } else {


                Glide.with(context)
                        .load(imageUrl)
                        .placeholder(R.drawable.loading)
                        .error(R.drawable.image_placeholder)
                        .into(holder.imgProduct);
            }
        }

    }

    @Override
    public int getItemCount() {
        return orderData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txtProductName, txtProductPrice, txtProductQty, txtProductWeight, txtTotalCost;
        ImageView imgProduct;


        public MyViewHolder(View itemView) {
            super(itemView);

            txtProductName = itemView.findViewById(R.id.txt_product_name);
            txtProductPrice = itemView.findViewById(R.id.txt_price);
            txtProductQty = itemView.findViewById(R.id.txt_qty);
            txtProductWeight = itemView.findViewById(R.id.txt_weight);
            imgProduct = itemView.findViewById(R.id.img_product);
            txtTotalCost = itemView.findViewById(R.id.txt_total_cost);


        }


    }


}