package com.mawshook.android_pos.report;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mawshook.android_pos.Constant;
import com.mawshook.android_pos.R;
import com.mawshook.android_pos.adapter.OrderDetailsAdapter;
import com.mawshook.android_pos.model.OrderDetails;
import com.mawshook.android_pos.model.SalesReport;
import com.mawshook.android_pos.networking.ApiClient;
import com.mawshook.android_pos.networking.ApiInterface;
import com.mawshook.android_pos.utils.BaseActivity;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SalesReportActivity extends BaseActivity {



    private RecyclerView recyclerView;
    ImageView imgNoProduct;
    TextView txtNoProducts, txtTotalPrice,txtTotalTax,txtTotalDiscount,txtNetSales;
    private ShimmerFrameLayout mShimmerViewContainer;
    SharedPreferences sp;
    String currency,shopID,ownerId,currentYear;
    DecimalFormat f;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_report);

        recyclerView = findViewById(R.id.recycler);
        imgNoProduct = findViewById(R.id.image_no_product);
        f = new DecimalFormat("#0.00");
        txtNoProducts = findViewById(R.id.txt_no_products);
        txtTotalPrice = findViewById(R.id.txt_total_price);
        txtTotalTax=findViewById(R.id.txt_total_tax);
        txtTotalDiscount=findViewById(R.id.txt_total_discount);
        txtNetSales=findViewById(R.id.txt_net_sales);
        mShimmerViewContainer = findViewById(R.id.shimmer_view_container);

        sp = getSharedPreferences(Constant.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        currency = sp.getString(Constant.SP_CURRENCY_SYMBOL, "N/A");

        imgNoProduct.setVisibility(View.GONE);
        txtNoProducts.setVisibility(View.GONE);

        SharedPreferences sp = getSharedPreferences(Constant.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        shopID = sp.getString(Constant.SP_SHOP_ID, "");
        ownerId = sp.getString(Constant.SP_OWNER_ID, "");


        getSupportActionBar().setHomeButtonEnabled(true); //for back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//for back button
        getSupportActionBar().setTitle(R.string.all_sales);


        // set a GridLayoutManager with default vertical orientation and 3 number of columns
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(SalesReportActivity.this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager); // set LayoutManager to RecyclerView

        recyclerView.setHasFixedSize(true);

        currentYear = new SimpleDateFormat("yyyy", Locale.ENGLISH).format(new Date());
        //sum of all transaction
        getSalesReport("all",shopID,ownerId,currentYear);
        //to view all sales data
        getReport("all",shopID,ownerId,currentYear);




    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.all_sales_menu, menu);
        return true;
    }


    //for back button
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                this.finish();
                return true;
            case R.id.menu_all_sales:
                getReport("all",shopID,ownerId,currentYear);

                return true;

            case R.id.menu_daily:
                getReport("daily",shopID,ownerId,currentYear);
                getSupportActionBar().setTitle(R.string.daily);

                return true;

            case R.id.menu_weekly:
                getReport("weekly",shopID,ownerId,currentYear);
                getSupportActionBar().setTitle(R.string.weekly);

                return true;


            case R.id.menu_monthly:
                getReport("monthly",shopID,ownerId,currentYear);
                getSupportActionBar().setTitle(R.string.monthly);


                return true;

            case R.id.menu_yearly:
                getReport("yearly",shopID,ownerId,currentYear);
                getSupportActionBar().setTitle(R.string.yearly);


                return true;



            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public void getReport(String type,String shopId,String ownerId,String year) {

       getSalesReport(type,shopId,ownerId,year);
       getReportList(type,shopId,ownerId,year);
        //Stopping Shimmer Effects
        mShimmerViewContainer.startShimmer();
        mShimmerViewContainer.setVisibility(View.VISIBLE);



    }





    public void getSalesReport(String type,String shopId,String ownerId,String year) {

        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<List<SalesReport>> call;
        call = apiInterface.getSalesReport(type,shopId,ownerId,year);

        call.enqueue(new Callback<List<SalesReport>>() {
            @Override
            public void onResponse(@NonNull Call<List<SalesReport>> call, @NonNull Response<List<SalesReport>> response) {


                if (response.isSuccessful() && response.body() != null) {
                    List<SalesReport> salesReport;
                    salesReport = response.body();



                    if (salesReport.isEmpty()) {


                        Log.d("Data", "Empty");


                    } else {


                        String totalOrderPrice=salesReport.get(0).getTotalOrderPrice();
                        String totalTax=salesReport.get(0).getTotalTax();
                        String totalDiscount=salesReport.get(0).getTotalDiscount();

                        if (totalOrderPrice!=null) {

                            txtTotalPrice.setText(getString(R.string.total_price) + "=" + currency + f.format(Double.parseDouble(totalOrderPrice)));
                            txtTotalTax.setText(getString(R.string.total_tax) + "=" + currency + f.format(Double.parseDouble(totalTax)));
                            txtTotalDiscount.setText(getString(R.string.total_discount) + "=" + currency + f.format(Double.parseDouble(totalDiscount)));

                            Double orderPrice = Double.parseDouble(totalOrderPrice);
                            Double getTax = Double.parseDouble(totalTax);
                            Double getDiscount = Double.parseDouble(totalDiscount);
                            Double netSales = orderPrice + getTax - getDiscount;
                            txtNetSales.setText(getString(R.string.net_sales) + "=" + currency + f.format(netSales));
                        }
                        else
                        {
                            txtTotalPrice.setVisibility(View.INVISIBLE);
                            txtTotalTax.setVisibility(View.INVISIBLE);
                            txtTotalDiscount.setVisibility(View.INVISIBLE);
                            txtNetSales.setVisibility(View.INVISIBLE);
                        }

                    }

                }
            }

            @Override
            public void onFailure(@NonNull Call<List<SalesReport>> call, @NonNull Throwable t) {

                Toast.makeText(SalesReportActivity.this, R.string.something_went_wrong, Toast.LENGTH_SHORT).show();
                Log.d("Error : ", t.toString());
            }
        });


    }






    public void getReportList(String type,String shopId,String ownerId,String year) {


        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<List<OrderDetails>> call;
        call = apiInterface.getReportList(type,shopId,ownerId,year);

        call.enqueue(new Callback<List<OrderDetails>>() {
            @Override
            public void onResponse(@NonNull Call<List<OrderDetails>> call, @NonNull Response<List<OrderDetails>> response) {


                if (response.isSuccessful() && response.body() != null) {
                    List<OrderDetails> orderDetails;
                    orderDetails = response.body();



                    if (orderDetails.isEmpty()) {

                        //Stopping Shimmer Effects
                        mShimmerViewContainer.stopShimmer();
                        mShimmerViewContainer.setVisibility(View.GONE);


                        recyclerView.setVisibility(View.GONE);
                        imgNoProduct.setVisibility(View.VISIBLE);
                        imgNoProduct.setImageResource(R.drawable.not_found);
                        txtNoProducts.setVisibility(View.VISIBLE);
                        txtTotalPrice.setVisibility(View.GONE);
                        Toasty.warning(SalesReportActivity.this, R.string.no_product_found, Toast.LENGTH_SHORT).show();


                    } else {


                        //Stopping Shimmer Effects
                        mShimmerViewContainer.stopShimmer();
                        mShimmerViewContainer.setVisibility(View.GONE);

                        OrderDetailsAdapter orderDetailsAdapter = new OrderDetailsAdapter(SalesReportActivity.this, orderDetails);

                        recyclerView.setAdapter(orderDetailsAdapter);

                        recyclerView.setVisibility(View.VISIBLE);
                        imgNoProduct.setVisibility(View.GONE);
                        txtNoProducts.setVisibility(View.GONE);
                        txtTotalPrice.setVisibility(View.VISIBLE);



                    }

                }
            }

            @Override
            public void onFailure(@NonNull Call<List<OrderDetails>> call, @NonNull Throwable t) {


                Toast.makeText(SalesReportActivity.this, R.string.something_went_wrong, Toast.LENGTH_SHORT).show();
                Log.d("Error : ", t.toString());
            }
        });


    }



}

