package com.mawshook.android_pos.adapter;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.mawshook.android_pos.R;
import com.mawshook.android_pos.model.Category;
import com.mawshook.android_pos.model.Product;
import com.mawshook.android_pos.networking.ApiClient;
import com.mawshook.android_pos.networking.ApiInterface;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductCategoryAdapter extends RecyclerView.Adapter<ProductCategoryAdapter.MyViewHolder> {


    MediaPlayer player;
    private List<Category> categoryData;
    private Context context;
    RecyclerView recyclerView;
    ImageView imgNoProduct;
    TextView txtNoProducts;
    private ShimmerFrameLayout mShimmerViewContainer;


    public ProductCategoryAdapter(Context context, List<Category> categoryData, RecyclerView recyclerView, ImageView imgNoProduct, TextView txtNoProducts,  ShimmerFrameLayout mShimmerViewContainer) {
        this.context = context;
        this.categoryData = categoryData;
        this.recyclerView=recyclerView;
        player = MediaPlayer.create(context, R.raw.delete_sound);

        this.imgNoProduct=imgNoProduct;
        this.txtNoProducts=txtNoProducts;
        this.mShimmerViewContainer=mShimmerViewContainer;

    }


    @NonNull
    @Override
    public ProductCategoryAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_category_item, parent, false);
        return new MyViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final ProductCategoryAdapter.MyViewHolder holder, int position) {

        final String categoryId = categoryData.get(position).getProductCategoryId();
        String categoryName = categoryData.get(position).getProductCategoryName();




        holder.txtCategoryName.setText(categoryName);
        holder.cardCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                player.start();
               getProductsData(categoryId);

               mShimmerViewContainer.startShimmer();


            }
        });



    }

    @Override
    public int getItemCount() {
        return categoryData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txtCategoryName;
        CardView cardCategory;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            txtCategoryName = itemView.findViewById(R.id.txt_category_name);
            cardCategory=itemView.findViewById(R.id.card_category);

        }


    }





    public void getProductsData(String categoryId) {

        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<List<Product>> call;
        call = apiInterface.searchProductByCategory(categoryId);

        call.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(@NonNull Call<List<Product>> call, @NonNull Response<List<Product>> response) {


                if (response.isSuccessful() && response.body() != null) {
                    List<Product> productsList;
                    productsList = response.body();


                    if (productsList.isEmpty()) {

                        recyclerView.setVisibility(View.GONE);
                        imgNoProduct.setVisibility(View.VISIBLE);
                        imgNoProduct.setImageResource(R.drawable.not_found);
                        //Stopping Shimmer Effects
                        mShimmerViewContainer.stopShimmer();
                        mShimmerViewContainer.setVisibility(View.GONE);


                    } else {


                        //Stopping Shimmer Effects
                        mShimmerViewContainer.stopShimmer();
                        mShimmerViewContainer.setVisibility(View.GONE);

                        recyclerView.setVisibility(View.VISIBLE);
                        imgNoProduct.setVisibility(View.GONE);
                        PosProductAdapter productAdapter = new PosProductAdapter(context, productsList);

                        recyclerView.setAdapter(productAdapter);

                    }

                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Product>> call, @NonNull Throwable t) {

                Toast.makeText(context, R.string.something_went_wrong, Toast.LENGTH_SHORT).show();
                Log.d("Error : ", t.toString());
            }
        });


    }





}
