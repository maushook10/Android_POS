package com.mawshook.android_pos.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mawshook.android_pos.Constant;
import com.mawshook.android_pos.R;
import com.mawshook.android_pos.expense.EditExpenseActivity;
import com.mawshook.android_pos.model.Expense;
import com.mawshook.android_pos.networking.ApiClient;
import com.mawshook.android_pos.networking.ApiInterface;
import com.mawshook.android_pos.utils.Utils;
import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;

import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.gitonway.lee.niftymodaldialogeffects.lib.Effectstype.Slidetop;

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.MyViewHolder> {


    private List<Expense> expenseData;
    private Context context;
    Utils utils;

    SharedPreferences sp;
    String currency;


    public ExpenseAdapter(Context context, List<Expense> expenseData) {
        this.context = context;
        this.expenseData = expenseData;
        utils=new Utils();
        sp = context.getSharedPreferences(Constant.SHARED_PREF_NAME, Context.MODE_PRIVATE);

        currency = sp.getString(Constant.SP_CURRENCY_SYMBOL, "");


    }


    @NonNull
    @Override
    public ExpenseAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.expense_item, parent, false);
        return new MyViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final ExpenseAdapter.MyViewHolder holder, int position) {


        final String expenseId = expenseData.get(position).getExpenseId();
        String expenseName = expenseData.get(position).getExpenseName();
        String expenseNote = expenseData.get(position).getExpenseNote();
        String expenseAmount = expenseData.get(position).getExpenseAmount();
        String date = expenseData.get(position).getExpenseDate();
        String time = expenseData.get(position).getExpenseTime();


        holder.txtExpenseName.setText(expenseName);
        holder.txtExpenseAmount.setText(context.getString(R.string.expense)+" :"+currency + expenseAmount);
        holder.txtExpenseDateTime.setText(date + " " + time);
        holder.txtExpenseNote.setText(context.getString(R.string.note) + expenseNote);


        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                NiftyDialogBuilder dialogBuilder = NiftyDialogBuilder.getInstance(context);
                dialogBuilder
                        .withTitle(context.getString(R.string.delete))
                        .withMessage(context.getString(R.string.want_to_delete_expense))
                        .withEffect(Slidetop)
                        .withDialogColor("#43a047") //use color code for dialog
                        .withButton1Text(context.getString(R.string.yes))
                        .withButton2Text(context.getString(R.string.cancel))
                        .setButton1Click(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {


                                if (utils.isNetworkAvailable(context)) {
                                    deleteExpense(expenseId);
                                    expenseData.remove(holder.getAdapterPosition());
                                    dialogBuilder.dismiss();
                                }
                                else
                                {
                                    dialogBuilder.dismiss();
                                    Toasty.error(context, R.string.no_network_connection, Toast.LENGTH_SHORT).show();
                                }


                            }
                        })
                        .setButton2Click(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                dialogBuilder.dismiss();
                            }
                        })
                        .show();


            }
        });

    }

    @Override
    public int getItemCount() {
        return expenseData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView txtExpenseName, txtExpenseAmount, txtExpenseNote, txtExpenseDateTime;
        ImageView imgDelete, productImage;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            txtExpenseName = itemView.findViewById(R.id.txt_expense_name);
            txtExpenseAmount = itemView.findViewById(R.id.txt_expense_amount);
            txtExpenseNote = itemView.findViewById(R.id.txt_expense_note);
            txtExpenseDateTime = itemView.findViewById(R.id.txt_date_time);

            imgDelete = itemView.findViewById(R.id.img_delete);
            productImage = itemView.findViewById(R.id.product_image);

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            Intent i = new Intent(context, EditExpenseActivity.class);
            i.putExtra("expense_id", expenseData.get(getAdapterPosition()).getExpenseId());
            i.putExtra("expense_name", expenseData.get(getAdapterPosition()).getExpenseName());
            i.putExtra("expense_note", expenseData.get(getAdapterPosition()).getExpenseNote());
            i.putExtra("expense_amount", expenseData.get(getAdapterPosition()).getExpenseAmount());
            i.putExtra("expense_date", expenseData.get(getAdapterPosition()).getExpenseDate());
            i.putExtra("expense_time", expenseData.get(getAdapterPosition()).getExpenseTime());
            context.startActivity(i);
        }
    }


    //delete from server
    private void deleteExpense(String expenseId) {

        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        Call<Expense> call = apiInterface.deleteExpense(expenseId);
        call.enqueue(new Callback<Expense>() {
            @Override
            public void onResponse(@NonNull Call<Expense> call, @NonNull Response<Expense> response) {


                if (response.isSuccessful() && response.body() != null) {

                    String value = response.body().getValue();

                    if (value.equals(Constant.KEY_SUCCESS)) {
                        Toasty.error(context, R.string.expense_deleted, Toast.LENGTH_SHORT).show();
                        notifyDataSetChanged();

                    }

                    else if (value.equals(Constant.KEY_FAILURE)){
                        Toasty.error(context, R.string.error, Toast.LENGTH_SHORT).show();
                    }

                    else {
                        Toast.makeText(context, R.string.no_network_connection, Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<Expense> call, Throwable t) {
                Toast.makeText(context, "Error! " + t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }


}
