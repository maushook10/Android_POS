package com.mawshook.android_pos.suppliers;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.mawshook.android_pos.model.Suppliers;
import com.mawshook.android_pos.networking.ApiClient;
import com.mawshook.android_pos.networking.ApiInterface;
import com.mawshook.android_pos.utils.BaseActivity;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddSuppliersActivity extends BaseActivity {


    ProgressDialog loading;
    EditText etxtSuppliersName, etxtSuppliersContactPerson, etxtSuppliersAddress, etxtSuppliersCell, etxtSuppliersEmail;
    TextView txtAddSuppliers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_suppliers);

        getSupportActionBar().setHomeButtonEnabled(true); //for back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//for back button
        getSupportActionBar().setTitle(R.string.add_suppliers);

        etxtSuppliersName = findViewById(R.id.etxt_supplier_name);
        etxtSuppliersContactPerson = findViewById(R.id.etxt_supplier_contact_name);
        etxtSuppliersCell = findViewById(R.id.etxt_supplier_cell);
        etxtSuppliersEmail = findViewById(R.id.etxt_supplier_email);
        etxtSuppliersAddress = findViewById(R.id.etxt_supplier_address);
        txtAddSuppliers = findViewById(R.id.txt_add_supplier);

        SharedPreferences sp = getSharedPreferences(Constant.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String shopID = sp.getString(Constant.SP_SHOP_ID, "");
        String ownerId = sp.getString(Constant.SP_OWNER_ID, "");




        txtAddSuppliers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String suppliersName = etxtSuppliersName.getText().toString().trim();
                String suppliersContactPerson = etxtSuppliersContactPerson.getText().toString().trim();
                String suppliersCell = etxtSuppliersCell.getText().toString().trim();
                String suppliersEmail = etxtSuppliersEmail.getText().toString().trim();
                String suppliersAddress = etxtSuppliersAddress.getText().toString().trim();


                if (suppliersName.isEmpty()) {
                    etxtSuppliersName.setError(getString(R.string.enter_suppliers_name));
                    etxtSuppliersName.requestFocus();
                } else if (suppliersContactPerson.isEmpty()) {
                    etxtSuppliersContactPerson.setError(getString(R.string.enter_suppliers_contact_person_name));
                    etxtSuppliersContactPerson.requestFocus();
                } else if (suppliersCell.isEmpty()) {
                    etxtSuppliersCell.setError(getString(R.string.enter_suppliers_cell));
                    etxtSuppliersCell.requestFocus();
                } else if (suppliersEmail.isEmpty() || !suppliersEmail.contains("@") || !suppliersEmail.contains(".")) {
                    etxtSuppliersEmail.setError(getString(R.string.enter_valid_email));
                    etxtSuppliersEmail.requestFocus();
                } else if (suppliersAddress.isEmpty()) {
                    etxtSuppliersAddress.setError(getString(R.string.enter_suppliers_address));
                    etxtSuppliersAddress.requestFocus();
                } else {


                    addSupplier(suppliersName, suppliersContactPerson, suppliersCell, suppliersEmail, suppliersAddress,shopID,ownerId);

                }


            }
        });

    }




    //for back button
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {// app icon in action bar clicked; goto parent activity.
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



    private void addSupplier(String name,String contactPerson,String cell,String email, String address,String shopId,String ownerId) {


        loading=new ProgressDialog(this);
        loading.setCancelable(false);
        loading.setMessage(getString(R.string.please_wait));
        loading.show();
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        Call<Suppliers> call = apiInterface.addSuppliers(name,contactPerson,cell,email,address,shopId,ownerId);
        call.enqueue(new Callback<Suppliers>() {
            @Override
            public void onResponse(@NonNull Call<Suppliers> call, @NonNull Response<Suppliers> response) {


                if (response.isSuccessful() && response.body() != null) {
                    String value = response.body().getValue();

                    if (value.equals(Constant.KEY_SUCCESS)) {

                        loading.dismiss();

                        Toasty.success(AddSuppliersActivity.this, R.string.suppliers_successfully_added, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(AddSuppliersActivity.this, SuppliersActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);

                    }
                    else if (value.equals(Constant.KEY_FAILURE)) {

                        loading.dismiss();

                        Toasty.error(AddSuppliersActivity.this, R.string.failed, Toast.LENGTH_SHORT).show();
                        finish();

                    }

                    else {
                        loading.dismiss();
                        Toasty.error(AddSuppliersActivity.this, R.string.failed, Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<Suppliers> call, @NonNull Throwable t) {
                loading.dismiss();
                Log.d("Error! ", t.toString());
                Toasty.error(AddSuppliersActivity.this, R.string.no_network_connection, Toast.LENGTH_SHORT).show();
            }
        });
    }

}
