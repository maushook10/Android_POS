package com.mawshook.android_pos.orders;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.Toast;

import com.mawshook.android_pos.R;
import com.mawshook.android_pos.model.OrderDetails;
import com.mawshook.android_pos.pdf_report.BarCodeEncoder;
import com.mawshook.android_pos.utils.IPrintToPrinter;
import com.mawshook.android_pos.utils.WoosimPrnMng;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.woosim.printer.WoosimCmd;

import java.text.DecimalFormat;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class TestPrinter implements IPrintToPrinter {


    String name, price, qty, weight, totalPrice;
    double cost_total, subTotal;
    DecimalFormat f;

    private Context context;
    List<OrderDetails> orderDetailsList;
    String currency, servedBy, shopName, shopAddress, shopEmail, shopContact, invoiceId, orderDate, orderTime, customerName, footer, tax, discount;
    Bitmap bm;

    public TestPrinter(Context context, String shopName, String shopAddress, String shopEmail, String shopContact, String invoiceId, String orderDate, String orderTime, String customerName, String footer, double subTotal, String totalPrice, String tax, String discount, String currency, String served_by, List<OrderDetails> orderDetailsList) {
        this.context = context;
        this.shopName = shopName;
        this.shopAddress = shopAddress;
        this.shopEmail = shopEmail;
        this.shopContact = shopContact;
        this.invoiceId = invoiceId;
        this.orderDate = orderDate;
        this.orderTime = orderTime;
        this.customerName = customerName;
        this.footer = footer;
        this.subTotal = subTotal;
        this.totalPrice = totalPrice;
        this.tax = tax;
        this.discount = discount;
        this.currency = currency;
        this.servedBy = served_by;
        this.orderDetailsList = orderDetailsList;

        f = new DecimalFormat("#0.00");
    }

    @Override
    public void printContent(WoosimPrnMng prnMng) {


        //Generate barcode
        BarCodeEncoder qrCodeEncoder = new BarCodeEncoder();
        bm = null;

        try {
            bm = qrCodeEncoder.encodeAsBitmap(invoiceId, BarcodeFormat.CODE_128, 400, 48);
        } catch (WriterException e) {
            e.printStackTrace();
        }

        prnMng.printStr(shopName, 2, WoosimCmd.ALIGN_CENTER);
        prnMng.printStr(shopAddress, 1, WoosimCmd.ALIGN_CENTER);
        prnMng.printStr("Customer Receipt ", 1, WoosimCmd.ALIGN_CENTER);
        prnMng.printStr("Contact: " + shopContact, 1, WoosimCmd.ALIGN_CENTER);
        prnMng.printStr("Invoice ID: " + invoiceId, 1, WoosimCmd.ALIGN_CENTER);
        prnMng.printStr("Order Time: " + orderTime + " " + orderDate, 1, WoosimCmd.ALIGN_CENTER);
        prnMng.printStr(customerName, 1, WoosimCmd.ALIGN_CENTER);

        prnMng.printStr("Email: " + shopEmail, 1, WoosimCmd.ALIGN_CENTER);
        prnMng.printStr("Served By: " + servedBy, 1, WoosimCmd.ALIGN_CENTER);

        prnMng.printStr("--------------------------------");

        prnMng.printStr("  Items        Price  Qty  Total", 1, WoosimCmd.ALIGN_CENTER);
        prnMng.printStr("--------------------------------");

        double getItemPrice;

        for (int i = 0; i < orderDetailsList.size(); i++) {
            name = orderDetailsList.get(i).getProductName();
            price = orderDetailsList.get(i).getProductPrice();
            getItemPrice = Double.parseDouble(price);

            qty = orderDetailsList.get(i).getProductQuantity();
            weight = orderDetailsList.get(i).getProductWeight();

            cost_total = Integer.parseInt(qty) * Double.parseDouble(price);


            prnMng.leftRightAlign(name + " " + f.format(getItemPrice) + "x" + qty, "=" + f.format(cost_total));


        }

        prnMng.printStr("--------------------------------");
        prnMng.printStr("Sub Total: " + currency + f.format(subTotal), 1, WoosimCmd.ALIGN_RIGHT);
        prnMng.printStr("Total Tax (+): " + currency + f.format(Double.parseDouble(tax)), 1, WoosimCmd.ALIGN_RIGHT);
        prnMng.printStr("Discount (-): " + currency + f.format(Double.parseDouble(discount)), 1, WoosimCmd.ALIGN_RIGHT);
        prnMng.printStr("--------------------------------");
        prnMng.printStr("Total Price: " + currency + f.format(Double.parseDouble(totalPrice)), 1, WoosimCmd.ALIGN_RIGHT);

        prnMng.printNewLine();
        prnMng.printStr(footer, 1, WoosimCmd.ALIGN_CENTER);

        prnMng.printNewLine();

        //print barcode
        prnMng.printPhoto(bm);
        prnMng.printNewLine();

        printEnded(prnMng);
        prnMng.printNewLine();
    }

    @Override
    public void printEnded(WoosimPrnMng prnMng) {
        //Do any finalization you like after print ended.
        if (prnMng.printSucc()) {
            Toasty.success(context, R.string.print_succ, Toast.LENGTH_LONG).show();
        } else {
            Toasty.error(context, R.string.print_error, Toast.LENGTH_LONG).show();
        }
    }
}
