package com.mawshook.android_pos.report;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.mawshook.android_pos.Constant;
import com.mawshook.android_pos.R;
import com.mawshook.android_pos.model.MonthData;
import com.mawshook.android_pos.networking.ApiClient;
import com.mawshook.android_pos.networking.ApiInterface;
import com.mawshook.android_pos.utils.BaseActivity;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.whiteelephant.monthpicker.MonthPickerDialog;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GraphReportActivity extends BaseActivity {


    int mYear;
    BarChart barChart;
    TextView txtTotalSales, txtSelectYear, txtTotalTax, txtTotalDiscount, txtNetSales;
    ArrayList<BarEntry> barEntries;
    private ShimmerFrameLayout mShimmerViewContainer;

    SharedPreferences sp;
    String currency,shopID,ownerId;
    DecimalFormat f;
    LinearLayout layoutYear;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph_report);

        getSupportActionBar().setHomeButtonEnabled(true); //for back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//for back button
        getSupportActionBar().setTitle(R.string.monthly_sales_graph);

        sp = getSharedPreferences(Constant.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        currency = sp.getString(Constant.SP_CURRENCY_SYMBOL, "N/A");
        SharedPreferences sp = getSharedPreferences(Constant.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        shopID = sp.getString(Constant.SP_SHOP_ID, "");
        ownerId = sp.getString(Constant.SP_OWNER_ID, "");
        f = new DecimalFormat("#0.00");

        barChart = findViewById(R.id.barchart);
        txtTotalSales = findViewById(R.id.txt_total_sales);
        txtSelectYear = findViewById(R.id.txt_select_year);
        txtTotalTax = findViewById(R.id.txt_total_tax);
        txtTotalDiscount = findViewById(R.id.txt_discount);
        txtNetSales = findViewById(R.id.txt_net_sales);
        mShimmerViewContainer = findViewById(R.id.shimmer_view_container);
        layoutYear=findViewById(R.id.layout_year);


        barChart.setDrawBarShadow(false);

        barChart.setDrawValueAboveBar(true);

        barChart.setMaxVisibleValueCount(50);
        barChart.setPinchZoom(false);
        barChart.setDrawGridBackground(true);

        barChart.setVisibility(View.INVISIBLE);

        String currentYear = new SimpleDateFormat("yyyy", Locale.ENGLISH).format(new Date());
        txtSelectYear.setText(getString(R.string.year) + currentYear);

        mYear=Integer.parseInt(currentYear);


        getMonthlySales(shopID,ownerId,currentYear);

        layoutYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                chooseYearOnly();

            }
        });
    }


    public void getGraphData() {

        String[] monthList = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(monthList));
        xAxis.setCenterAxisLabels(true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1);
        xAxis.setGranularityEnabled(true);
        xAxis.setLabelCount(12);

        BarDataSet barDataSet = new BarDataSet(barEntries, getString(R.string.monthly_sales_report));
        barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);

        BarData barData = new BarData(barDataSet);
        barData.setBarWidth(0.9f);

        barChart.setData(barData);

        barChart.setScaleEnabled(false);  //for fixed bar chart,no zoom
        //for refresh chart
        barChart.notifyDataSetChanged();
        barChart.invalidate();

    }


    public void getMonthlySales(String shopId,String ownerId,String year) {

        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<List<MonthData>> call;
        call = apiInterface.getMonthlySales(shopId,ownerId,year);

        call.enqueue(new Callback<List<MonthData>>() {
            @Override
            public void onResponse(@NonNull Call<List<MonthData>> call, @NonNull Response<List<MonthData>> response) {


                if (response.isSuccessful() && response.body() != null) {
                    List<MonthData> monthDataList;
                    monthDataList = response.body();


                    if (monthDataList.isEmpty()) {


                        Log.d("Data", "Empty");


                    } else {

                        //Stopping Shimmer Effects
                        mShimmerViewContainer.stopShimmer();
                        mShimmerViewContainer.setVisibility(View.GONE);

                        barEntries = new ArrayList<>();
                        float jan = Float.parseFloat(monthDataList.get(0).getJan());
                        float feb = Float.parseFloat(monthDataList.get(0).getFeb());
                        float mar = Float.parseFloat(monthDataList.get(0).getMar());
                        float apr = Float.parseFloat(monthDataList.get(0).getApr());
                        float may = Float.parseFloat(monthDataList.get(0).getMay());
                        float jun = Float.parseFloat(monthDataList.get(0).getJun());
                        float jul = Float.parseFloat(monthDataList.get(0).getJul());
                        float aug = Float.parseFloat(monthDataList.get(0).getAug());
                        float sep = Float.parseFloat(monthDataList.get(0).getSep());
                        float oct = Float.parseFloat(monthDataList.get(0).getOct());
                        float nov = Float.parseFloat(monthDataList.get(0).getNov());
                        float dec = Float.parseFloat(monthDataList.get(0).getDec());


                        float totalTax = monthDataList.get(0).getTotalTax();
                        float totalDiscount = monthDataList.get(0).getTotalDiscount();
                        float totalOrderPrice = monthDataList.get(0).getTotalOrderPrice();


                        barEntries.add(new BarEntry(1, jan));
                        barEntries.add(new BarEntry(2, feb));
                        barEntries.add(new BarEntry(3, mar));
                        barEntries.add(new BarEntry(4, apr));
                        barEntries.add(new BarEntry(5, may));
                        barEntries.add(new BarEntry(6, jun));
                        barEntries.add(new BarEntry(7, jul));
                        barEntries.add(new BarEntry(8, aug));
                        barEntries.add(new BarEntry(9, sep));
                        barEntries.add(new BarEntry(10, oct));
                        barEntries.add(new BarEntry(11, nov));
                        barEntries.add(new BarEntry(12, dec));

                        getGraphData();

                        barChart.setVisibility(View.VISIBLE);
                        txtTotalSales.setVisibility(View.VISIBLE);


                        txtTotalSales.setText(getString(R.string.total_sales) + currency + f.format(totalOrderPrice));

                        txtTotalTax.setText(getString(R.string.total_tax) + ":" + currency + f.format(totalTax));
                        txtTotalDiscount.setText(getString(R.string.total_discount) + ":" + currency + f.format(totalDiscount));
                        float netSales = totalOrderPrice + totalTax - totalDiscount;

                        txtNetSales.setText(getString(R.string.net_sales) + currency + f.format(netSales));

                    }

                }
            }

            @Override
            public void onFailure(@NonNull Call<List<MonthData>> call, @NonNull Throwable t) {

                Toast.makeText(GraphReportActivity.this, R.string.something_went_wrong, Toast.LENGTH_SHORT).show();
                Log.d("Error : ", t.toString());
            }
        });


    }



    private void chooseYearOnly() {


        MonthPickerDialog.Builder builder = new MonthPickerDialog.Builder(GraphReportActivity.this, new MonthPickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(int selectedMonth, int selectedYear) {
                txtSelectYear.setText(getString(R.string.year)+" "+selectedYear);
                mYear = selectedYear;
                getMonthlySales(shopID,ownerId,""+mYear);

            }
        }, mYear, 0);

        builder.showYearOnly()
                .setYearRange(1990, 2099)
                .build()
                .show();




    }



    //for back button
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
