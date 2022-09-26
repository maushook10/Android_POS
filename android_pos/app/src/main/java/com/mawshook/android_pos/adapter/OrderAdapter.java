package com.mawshook.android_pos.adapter;

import android.content.Context;
import android.content.Intent;
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
import com.mawshook.android_pos.model.OrderList;
import com.mawshook.android_pos.networking.ApiClient;
import com.mawshook.android_pos.networking.ApiInterface;
import com.mawshook.android_pos.orders.OrderDetailsActivity;
import com.mawshook.android_pos.utils.Utils;
import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.gitonway.lee.niftymodaldialogeffects.lib.Effectstype.Slidetop;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.MyViewHolder> {


    Context context;
    private List<OrderList> orderData;
    Utils utils;


    public OrderAdapter(Context context, List<OrderList> orderData) {
        this.context = context;
        this.orderData = orderData;
        utils = new Utils();


    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {



        String customerName = orderData.get(position).getCustomerName();
        String invoiceId = orderData.get(position).getInvoiceId();
        String orderDate = orderData.get(position).getOrderDate();
        String orderTime = orderData.get(position).getOrderTime();
        String orderPaymentMethod = orderData.get(position).getOrderPaymentMethod();
        String orderType = orderData.get(position).getOrderType();
        String orderNote = orderData.get(position).getOrderNote();


        SimpleDateFormat fromUser = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat myFormat = new SimpleDateFormat("MMMM dd, yyyy", Locale.US);

        String reformattedDate = "";
        try {

            reformattedDate = myFormat.format(fromUser.parse(orderDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }


        holder.txtCustomerName.setText(customerName);
        holder.txtInvoiceId.setText(invoiceId);

        holder.txtPaymentMethod.setText(orderPaymentMethod);
        holder.txtOrderType.setText(orderType);
        holder.txtDate.setText(orderTime + " " + reformattedDate);

        if (orderNote == null || orderNote.equals("N/A")) {
            holder.txtOrderNote.setVisibility(View.GONE);
        } else {
            holder.txtOrderNote.setText(context.getString(R.string.table_number) + " :" + orderNote);
            holder.imgOrderImage.setImageResource(R.drawable.table_booking);
        }
        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                NiftyDialogBuilder dialogBuilder = NiftyDialogBuilder.getInstance(context);
                dialogBuilder
                        .withTitle(context.getString(R.string.delete))
                        .withMessage(context.getString(R.string.want_to_delete_order))
                        .withEffect(Slidetop)
                        .withDialogColor("#43a047") //use color code for dialog
                        .withButton1Text(context.getString(R.string.yes))
                        .withButton2Text(context.getString(R.string.cancel))
                        .setButton1Click(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {


                                if (utils.isNetworkAvailable(context)) {
                                    deleteOrder(invoiceId);
                                    orderData.remove(holder.getAdapterPosition());
                                    dialogBuilder.dismiss();
                                } else {
                                    dialogBuilder.dismiss();
                                    Toasty.error(context, R.string.no_network_connection, Toast.LENGTH_SHORT).show();
                                }

                                dialogBuilder.dismiss();
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
        return orderData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView txtCustomerName, txtOrderNote, txtInvoiceId, txtOrderType, txtPaymentMethod, txtDate;
        ImageView imgDelete, imgOrderImage;

        public MyViewHolder(View itemView) {
            super(itemView);

            txtInvoiceId = itemView.findViewById(R.id.txt_invoice_number);
            txtCustomerName = itemView.findViewById(R.id.txt_customer_name);
            txtOrderType = itemView.findViewById(R.id.txt_order_type);
            txtPaymentMethod = itemView.findViewById(R.id.txt_payment_method);
            txtDate = itemView.findViewById(R.id.txt_date);
            txtOrderNote = itemView.findViewById(R.id.txt_order_note);
            imgDelete = itemView.findViewById(R.id.img_delete);
            itemView.setOnClickListener(this);


        }

        @Override
        public void onClick(View view) {
            Intent i = new Intent(context, OrderDetailsActivity.class);
            i.putExtra(Constant.INVOICE_ID, orderData.get(getAdapterPosition()).getInvoiceId());
            i.putExtra(Constant.CUSTOMER_NAME, orderData.get(getAdapterPosition()).getCustomerName());
            i.putExtra(Constant.TAX, orderData.get(getAdapterPosition()).getTax());
            i.putExtra(Constant.ORDER_PRICE, orderData.get(getAdapterPosition()).getOrderPrice());
            i.putExtra(Constant.DISCOUNT, orderData.get(getAdapterPosition()).getDiscount());
            i.putExtra(Constant.ORDER_DATE, orderData.get(getAdapterPosition()).getOrderDate());
            i.putExtra(Constant.ORDER_TIME, orderData.get(getAdapterPosition()).getOrderTime());
            context.startActivity(i);
        }
    }


    //delete from server
    private void deleteOrder(String invoiceId) {

        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        Call<OrderList> call = apiInterface.deleteOrder(invoiceId);
        call.enqueue(new Callback<OrderList>() {
            @Override
            public void onResponse(@NonNull Call<OrderList> call, @NonNull Response<OrderList> response) {


                if (response.isSuccessful() && response.body() != null) {

                    String value = response.body().getValue();

                    if (value.equals(Constant.KEY_SUCCESS)) {
                        Toasty.error(context, R.string.order_deleted, Toast.LENGTH_SHORT).show();
                        notifyDataSetChanged();

                    } else if (value.equals(Constant.KEY_FAILURE)) {
                        Toasty.error(context, R.string.error, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, R.string.no_network_connection, Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<OrderList> call, @NonNull Throwable t) {
                Toast.makeText(context, "Error! " + t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }


}