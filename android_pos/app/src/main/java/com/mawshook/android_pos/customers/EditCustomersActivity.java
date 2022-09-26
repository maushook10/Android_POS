package com.mawshook.android_pos.customers;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.mawshook.android_pos.Constant;
import com.mawshook.android_pos.R;
import com.mawshook.android_pos.model.Customer;
import com.mawshook.android_pos.networking.ApiClient;
import com.mawshook.android_pos.networking.ApiInterface;
import com.mawshook.android_pos.utils.BaseActivity;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditCustomersActivity extends BaseActivity {


    ProgressDialog loading;
    EditText etxtCustomerName, etxtAddress, etxtCustomerCell, etxtCustomerEmail;
    TextView txtEditCustomer, txtUpdateInformation;
    String getCustomerId, getCustomerName, getCustomerCell, getCustomerEmail, getCustomerAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_customers);

        getSupportActionBar().setHomeButtonEnabled(true); //for back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//for back button
        getSupportActionBar().setTitle(R.string.edit_customer);


        etxtCustomerName = findViewById(R.id.etxt_customer_name);
        etxtCustomerCell = findViewById(R.id.etxt_customer_cell);
        etxtCustomerEmail = findViewById(R.id.etxt_email);
        etxtAddress = findViewById(R.id.etxt_address);

        txtEditCustomer = findViewById(R.id.txt_edit_customer);
        txtUpdateInformation = findViewById(R.id.txt_update_customer);

        getCustomerId = getIntent().getExtras().getString("customer_id");
        getCustomerName = getIntent().getExtras().getString("customer_name");
        getCustomerCell = getIntent().getExtras().getString("customer_cell");
        getCustomerEmail = getIntent().getExtras().getString("customer_email");
        getCustomerAddress = getIntent().getExtras().getString("customer_address");


        etxtCustomerName.setText(getCustomerName);
        etxtCustomerCell.setText(getCustomerCell);
        etxtCustomerEmail.setText(getCustomerEmail);
        etxtAddress.setText(getCustomerAddress);


        etxtCustomerName.setEnabled(false);
        etxtCustomerCell.setEnabled(false);
        etxtCustomerEmail.setEnabled(false);
        etxtAddress.setEnabled(false);

        txtUpdateInformation.setVisibility(View.INVISIBLE);


        txtEditCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                etxtCustomerName.setEnabled(true);
                etxtCustomerCell.setEnabled(true);
                etxtCustomerEmail.setEnabled(true);
                etxtAddress.setEnabled(true);

                etxtCustomerName.setTextColor(Color.RED);
                etxtCustomerCell.setTextColor(Color.RED);
                etxtCustomerEmail.setTextColor(Color.RED);
                etxtAddress.setTextColor(Color.RED);
                txtUpdateInformation.setVisibility(View.VISIBLE);

                txtEditCustomer.setVisibility(View.GONE);

            }
        });


        txtUpdateInformation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String customerName = etxtCustomerName.getText().toString().trim();
                String customerCell = etxtCustomerCell.getText().toString().trim();
                String customerEmail = etxtCustomerEmail.getText().toString().trim();
                String customerAddress = etxtAddress.getText().toString().trim();

                if (customerName.isEmpty()) {
                    etxtCustomerName.setError(getString(R.string.enter_customer_name));
                    etxtCustomerName.requestFocus();
                } else if (customerCell.isEmpty()) {
                    etxtCustomerCell.setError(getString(R.string.enter_customer_cell));
                    etxtCustomerCell.requestFocus();
                } else if (customerEmail.isEmpty() || !customerEmail.contains("@") || !customerEmail.contains(".")) {
                    etxtCustomerEmail.setError(getString(R.string.enter_valid_email));
                    etxtCustomerEmail.requestFocus();
                } else if (customerAddress.isEmpty()) {
                    etxtAddress.setError(getString(R.string.enter_customer_address));
                    etxtAddress.requestFocus();
                } else {


                    updateCustomer(getCustomerId, customerName, customerCell, customerEmail, customerAddress);


                }
            }

        });


    }



    private void updateCustomer(String id,String name,String cell,String email, String address) {

        loading=new ProgressDialog(this);
        loading.setCancelable(false);
        loading.setMessage(getString(R.string.please_wait));
        loading.show();

        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        Call<Customer> call = apiInterface.updateCustomers(id,name,cell,email,address);
        call.enqueue(new Callback<Customer>() {
            @Override
            public void onResponse(@NonNull Call<Customer> call, @NonNull Response<Customer> response) {


                if (response.isSuccessful() && response.body() != null) {
                    String value = response.body().getValue();

                    if (value.equals(Constant.KEY_SUCCESS)) {

                        loading.dismiss();

                        Toasty.success(EditCustomersActivity.this, R.string.update_successfully, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(EditCustomersActivity.this, CustomersActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);

                    }
                    else if (value.equals(Constant.KEY_FAILURE)) {

                        loading.dismiss();

                        Toasty.error(EditCustomersActivity.this, R.string.failed, Toast.LENGTH_SHORT).show();
                        finish();

                    }

                    else {
                        loading.dismiss();
                        Toasty.error(EditCustomersActivity.this, R.string.failed, Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<Customer> call, @NonNull Throwable t) {
                loading.dismiss();
                Log.d("Error! ", t.toString());
                Toasty.error(EditCustomersActivity.this, R.string.no_network_connection, Toast.LENGTH_SHORT).show();
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
