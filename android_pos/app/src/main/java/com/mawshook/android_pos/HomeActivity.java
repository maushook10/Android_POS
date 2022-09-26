package com.mawshook.android_pos;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.mawshook.android_pos.customers.CustomersActivity;
import com.mawshook.android_pos.expense.ExpenseActivity;
import com.mawshook.android_pos.login.LoginActivity;
import com.mawshook.android_pos.orders.OrdersActivity;
import com.mawshook.android_pos.pos.PosActivity;
import com.mawshook.android_pos.product.ProductActivity;
import com.mawshook.android_pos.report.ReportActivity;
import com.mawshook.android_pos.settings.SettingsActivity;
import com.mawshook.android_pos.suppliers.SuppliersActivity;
import com.mawshook.android_pos.utils.BaseActivity;
import com.mawshook.android_pos.utils.LocaleManager;
import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;

import es.dmoral.toasty.Toasty;

import static com.gitonway.lee.niftymodaldialogeffects.lib.Effectstype.Slidetop;

public class HomeActivity extends BaseActivity {


    CardView cardCustomers, cardProducts, cardSupplier, cardPos, cardOrderList, cardReport, cardSettings, cardExpense, cardLogout;
    //for double back press to exit
    private static final int TIME_DELAY = 2000;
    private static long backPressed;

    SharedPreferences sp;
    SharedPreferences.Editor editor;
    String userType;
    TextView txtShopName, txtSubText;

    private AdView adView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        getSupportActionBar().setTitle(R.string.app_name);
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.actionbar_gradient));
        getSupportActionBar().setElevation(0);


        cardCustomers = findViewById(R.id.card_customers);
        cardSupplier = findViewById(R.id.card_suppliers);
        cardProducts = findViewById(R.id.card_products);
        cardPos = findViewById(R.id.card_pos);
        cardOrderList = findViewById(R.id.card_all_orders);
        cardReport = findViewById(R.id.card_reports);
        cardSettings = findViewById(R.id.card_settings);
        cardExpense = findViewById(R.id.card_expense);
        cardLogout = findViewById(R.id.card_logout);
        txtShopName = findViewById(R.id.txt_shop_name);
        txtSubText = findViewById(R.id.txt_sub_text);


        sp = getSharedPreferences(Constant.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        editor = sp.edit();

        userType = sp.getString(Constant.SP_USER_TYPE, "");
        String shopName = sp.getString(Constant.SP_SHOP_NAME, "");
        String staffName = sp.getString(Constant.SP_STAFF_NAME, "");
        txtShopName.setText(shopName);
        txtSubText.setText("Hi " + staffName);


        if (Build.VERSION.SDK_INT >= 23) //Android MarshMellow Version or above
        {
            requestPermission();

        }


//       Admob initialization
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        adView = findViewById(R.id.adview);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);


        cardCustomers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, CustomersActivity.class);
                startActivity(intent);


            }
        });

        cardSupplier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, SuppliersActivity.class);
                startActivity(intent);


            }
        });


        cardProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, ProductActivity.class);
                startActivity(intent);


            }
        });


        cardPos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, PosActivity.class);
                startActivity(intent);


            }
        });

        cardOrderList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, OrdersActivity.class);
                startActivity(intent);


            }
        });


        cardReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (userType.equals(Constant.ADMIN) || userType.equals(Constant.MANAGER)) {
                    Intent intent = new Intent(HomeActivity.this, ReportActivity.class);
                    startActivity(intent);
                } else {
                    Toasty.error(HomeActivity.this, R.string.you_dont_have_permission_to_access_this_page, Toast.LENGTH_SHORT).show();
                }

            }
        });


        cardExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (userType.equals(Constant.ADMIN) || userType.equals(Constant.MANAGER)) {
                    Intent intent = new Intent(HomeActivity.this, ExpenseActivity.class);
                    startActivity(intent);
                } else {
                    Toasty.error(HomeActivity.this, R.string.you_dont_have_permission_to_access_this_page, Toast.LENGTH_SHORT).show();
                }
            }
        });


        cardSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (userType.equals(Constant.ADMIN)) {
                    Intent intent = new Intent(HomeActivity.this, SettingsActivity.class);
                    startActivity(intent);
                } else {
                    Toasty.error(HomeActivity.this, R.string.you_dont_have_permission_to_access_this_page, Toast.LENGTH_SHORT).show();
                }
            }
        });


        cardLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                NiftyDialogBuilder dialogBuilder = NiftyDialogBuilder.getInstance(HomeActivity.this);
                dialogBuilder
                        .withTitle(getString(R.string.logout))
                        .withMessage(R.string.want_to_logout_from_app)
                        .withEffect(Slidetop)
                        .withDialogColor("#43a047") //use color code for dialog
                        .withButton1Text(getString(R.string.yes))
                        .withButton2Text(getString(R.string.cancel))
                        .setButton1Click(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                editor.putString(Constant.SP_EMAIL, "");
                                editor.putString(Constant.SP_PASSWORD, "");
                                editor.putString(Constant.SP_USER_NAME, "");
                                editor.putString(Constant.SP_USER_TYPE, "");
                                editor.apply();

                                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finish();
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.language_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {


            case R.id.local_french:
                setNewLocale(this, LocaleManager.FRENCH);
                return true;


            case R.id.local_english:
                setNewLocale(this, LocaleManager.ENGLISH);
                return true;


            case R.id.local_bangla:
                setNewLocale(this, LocaleManager.BANGLA);
                return true;

            case R.id.local_spanish:
                setNewLocale(this, LocaleManager.SPANISH);
                return true;

            case R.id.local_hindi:
                setNewLocale(this, LocaleManager.HINDI);
                return true;
            default:
                Log.d("Default", "default");

        }

        return super.onOptionsItemSelected(item);
    }


    private void setNewLocale(AppCompatActivity mContext, @LocaleManager.LocaleDef String language) {
        LocaleManager.setNewLocale(this, language);
        Intent intent = mContext.getIntent();
        startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    //double back press to exit
    @Override
    public void onBackPressed() {
        if (backPressed + TIME_DELAY > System.currentTimeMillis()) {

            finishAffinity();

        } else {
            Toasty.info(this, R.string.press_once_again_to_exit,
                    Toast.LENGTH_SHORT).show();
        }
        backPressed = System.currentTimeMillis();
    }


    private void requestPermission() {
        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {

                            //write your action if needed
                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // show alert dialog navigating to Settings

                        }
                    }


                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).
                withErrorListener(error -> Toast.makeText(getApplicationContext(), R.string.error, Toast.LENGTH_SHORT).show())
                .onSameThread()
                .check();
    }
}
