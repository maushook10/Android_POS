package com.mawshook.android_pos.expense;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.mawshook.android_pos.Constant;
import com.mawshook.android_pos.R;
import com.mawshook.android_pos.model.Expense;
import com.mawshook.android_pos.networking.ApiClient;
import com.mawshook.android_pos.networking.ApiInterface;
import com.mawshook.android_pos.utils.BaseActivity;

import java.util.Calendar;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditExpenseActivity extends BaseActivity {


    String dateTime = "";
    int mYear, mMonth, mDay, mHour, mMinute;
    ProgressDialog loading;


    EditText etxtExpenseName, etxtExpenseNote, etxtExpenseAmount, etxtDate, etxtTime;
    TextView txtEditExpense, txtUpdateExpense;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_expense);


        getSupportActionBar().setHomeButtonEnabled(true); //for back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//for back button
        getSupportActionBar().setTitle(R.string.edit_expense);

        etxtExpenseName = findViewById(R.id.etxt_expense_name);
        etxtExpenseNote = findViewById(R.id.etxt_expense_note);
        etxtExpenseAmount = findViewById(R.id.etxt_expense_amount);
        etxtDate = findViewById(R.id.etxt_date);
        etxtTime = findViewById(R.id.etxt_time);

        txtEditExpense = findViewById(R.id.txt_edit_expense);
        txtUpdateExpense = findViewById(R.id.txt_update_expense);


        String getExpenseId = getIntent().getExtras().getString("expense_id");
        String getExpenseName = getIntent().getExtras().getString("expense_name");
        String getExpenseNote = getIntent().getExtras().getString("expense_note");
        String getExpenseAmount = getIntent().getExtras().getString("expense_amount");
        String getExpenseDate = getIntent().getExtras().getString("expense_date");
        String getExpenseTime = getIntent().getExtras().getString("expense_time");


        etxtExpenseName.setText(getExpenseName);
        etxtExpenseNote.setText(getExpenseNote);
        etxtExpenseAmount.setText(getExpenseAmount);
        etxtDate.setText(getExpenseDate);
        etxtTime.setText(getExpenseTime);


        etxtExpenseName.setEnabled(false);
        etxtExpenseNote.setEnabled(false);
        etxtExpenseAmount.setEnabled(false);
        etxtDate.setEnabled(false);
        etxtTime.setEnabled(false);
        txtUpdateExpense.setVisibility(View.INVISIBLE);

        etxtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                datePicker();
            }
        });


        etxtTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                timePicker();
            }
        });


        txtEditExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                etxtExpenseName.setEnabled(true);
                etxtExpenseNote.setEnabled(true);
                etxtExpenseAmount.setEnabled(true);
                etxtDate.setEnabled(true);
                etxtTime.setEnabled(true);

                etxtExpenseName.setTextColor(Color.RED);
                etxtExpenseNote.setTextColor(Color.RED);
                etxtExpenseAmount.setTextColor(Color.RED);
                etxtDate.setTextColor(Color.RED);
                etxtTime.setTextColor(Color.RED);

                txtUpdateExpense.setVisibility(View.VISIBLE);
                txtEditExpense.setVisibility(View.GONE);


            }
        });


        txtUpdateExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String expenseName = etxtExpenseName.getText().toString();
                String expenseNote = etxtExpenseNote.getText().toString();
                String expenseAmount = etxtExpenseAmount.getText().toString();
                String expenseDate = etxtDate.getText().toString();
                String expenseTime = etxtTime.getText().toString();


                if (expenseName.isEmpty()) {
                    etxtExpenseName.setError(getString(R.string.expense_name_cannot_be_empty));
                    etxtExpenseName.requestFocus();
                } else if (expenseAmount.isEmpty()) {
                    etxtExpenseAmount.setError(getString(R.string.expense_amount_cannot_be_empty));
                    etxtExpenseAmount.requestFocus();
                } else {



                  updateExpense(getExpenseId, expenseName, expenseAmount, expenseNote, expenseDate, expenseTime);



                }


            }


        });

    }


    private void datePicker() {

        // Get Current Date
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(EditExpenseActivity.this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                        int month = monthOfYear + 1;
                        String fm = "" + month;
                        String fd = "" + dayOfMonth;

                        if (monthOfYear < 10) {
                            fm = "0" + month;
                        }
                        if (dayOfMonth < 10) {
                            fd = "0" + dayOfMonth;
                        }
                        dateTime = year + "-" + (fm) + "-" + fd;


                        etxtDate.setText(dateTime);
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }


    private void timePicker() {
        // Get Current Time


        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(EditExpenseActivity.this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String amPm;
                        mHour = hourOfDay;
                        mMinute = minute;

                        if (mHour < 12) {
                            amPm = "AM";

                        } else {
                            amPm = "PM";
                            mHour = hourOfDay - 12;
                        }

                        etxtTime.setText(mHour + ":" + minute + " " + amPm);
                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();
    }



    private void updateExpense(String id,String name,String amount,String note, String date,String time) {


        Log.d("Expense Data", name+ " "+amount+ " "+note);

        loading=new ProgressDialog(this);
        loading.setCancelable(false);
        loading.setMessage(getString(R.string.please_wait));
        loading.show();
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        Call<Expense> call = apiInterface.updateExpense(id,name,amount,note,date,time);
        call.enqueue(new Callback<Expense>() {
            @Override
            public void onResponse(@NonNull Call<Expense> call, @NonNull Response<Expense> response) {


                if (response.isSuccessful() && response.body() != null) {
                    String value = response.body().getValue();

                    if (value.equals(Constant.KEY_SUCCESS)) {

                        loading.dismiss();

                        Toasty.success(EditExpenseActivity.this, R.string.update_successfully, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(EditExpenseActivity.this, ExpenseActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);

                    }
                    else if (value.equals(Constant.KEY_FAILURE)) {

                        loading.dismiss();

                        Toasty.error(EditExpenseActivity.this, R.string.failed, Toast.LENGTH_SHORT).show();
                        finish();

                    }

                    else {
                        loading.dismiss();
                        Toasty.error(EditExpenseActivity.this, R.string.failed, Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<Expense> call, @NonNull Throwable t) {
                loading.dismiss();
                Log.d("Error! ", t.toString());
                Toasty.error(EditExpenseActivity.this, R.string.no_network_connection, Toast.LENGTH_SHORT).show();
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
