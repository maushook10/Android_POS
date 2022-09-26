package com.mawshook.android_pos.settings.shop;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.mawshook.android_pos.Constant;
import com.mawshook.android_pos.R;
import com.mawshook.android_pos.model.ShopInformation;
import com.mawshook.android_pos.networking.ApiClient;
import com.mawshook.android_pos.networking.ApiInterface;
import com.mawshook.android_pos.utils.BaseActivity;

import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShopInformationActivity extends BaseActivity {


    EditText etxtShopName, etxtShopContact, etxtShopEmail, etxtShopAddress;
    ProgressDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_information);

        getSupportActionBar().setHomeButtonEnabled(true); //for back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//for back button
        getSupportActionBar().setTitle(R.string.shop_information);


        etxtShopName = findViewById(R.id.etxt_shop_name);
        etxtShopContact = findViewById(R.id.etxt_shop_contact);
        etxtShopEmail = findViewById(R.id.etxt_shop_email);
        etxtShopAddress = findViewById(R.id.etxt_shop_address);


        SharedPreferences sp = getSharedPreferences(Constant.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String shopID = sp.getString(Constant.SP_SHOP_ID, "");



        getShopInfo(shopID);

        etxtShopName.setEnabled(false);
        etxtShopContact.setEnabled(false);
        etxtShopEmail.setEnabled(false);
        etxtShopAddress.setEnabled(false);




    }



    public void getShopInfo(String shopId) {

        loading=new ProgressDialog(this);
        loading.setMessage(getString(R.string.please_wait));
        loading.setCancelable(false);
        loading.show();

        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<List<ShopInformation>> call;
        call = apiInterface.shopInformation(shopId);

        call.enqueue(new Callback<List<ShopInformation>>() {
            @Override
            public void onResponse(@NonNull Call<List<ShopInformation>> call, @NonNull Response<List<ShopInformation>> response) {


                if (response.isSuccessful() && response.body() != null) {
                    List<ShopInformation> shopInformation;
                    shopInformation = response.body();


                    loading.dismiss();

                    if (shopInformation.isEmpty()) {


                        Toasty.warning(ShopInformationActivity.this, R.string.no_product_found, Toast.LENGTH_SHORT).show();


                    } else {


                        String shopName=shopInformation.get(0).getShopName();
                        String shopContact=shopInformation.get(0).getShopContact();
                        String shopEmail=shopInformation.get(0).getShopEmail();
                        String shopAddress=shopInformation.get(0).getShopAddress();



                        etxtShopName.setText(shopName);
                        etxtShopContact.setText(shopContact);
                        etxtShopEmail.setText(shopEmail);
                        etxtShopAddress.setText(shopAddress);





                    }

                }
            }

            @Override
            public void onFailure(@NonNull Call<List<ShopInformation>> call, @NonNull Throwable t) {

                Toast.makeText(ShopInformationActivity.this, R.string.something_went_wrong, Toast.LENGTH_SHORT).show();
                Log.d("Error : ", t.toString());
                loading.dismiss();
            }
        });


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
