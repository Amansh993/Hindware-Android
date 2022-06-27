package com.example.hindware.view;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.material.navigation.NavigationView;
import com.google.zxing.integration.android.IntentIntegrator;
import com.example.hindware.R;
import com.example.hindware.adapter.NavigationMenuAdapter;
import com.example.hindware.databinding.ActivityDashboardBinding;
import com.example.hindware.model.BaseRequestBean;
import com.example.hindware.model.CheckQRResponseBean;
import com.example.hindware.model.LoginResponseBean;
import com.example.hindware.model.LogoutRequestBean;
import com.example.hindware.model.MenuListDataBean;
import com.example.hindware.model.MenuListRequestBean;
import com.example.hindware.model.MenuListResponseBean;
import com.example.hindware.model.SetLanguageRequestBean;
import com.example.hindware.model.ValidateQRRequestBean;
import com.example.hindware.utility.CommonUtil;
import com.example.hindware.utility.QwikcilverAPI;
import com.example.hindware.utility.QwikcilverSharedPref;
import com.example.hindware.utility.customprogress.CustomSpotsDialog;
import com.example.hindware.viewmodel.QwikcilverViewModel;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Locale;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;

import static com.example.hindware.utility.Constants.ENCRYPTED_REQUEST;
import static com.example.hindware.utility.Constants.PLAIN_REQUEST;
import static com.example.hindware.utility.Constants.USER_LANG_CODE;
import static com.example.hindware.utility.Constants.USER_TOKEN;

public class DashboardActivity extends BaseActivity implements View.OnClickListener {

    private ActivityDashboardBinding binding;
    private boolean doubleBackToExitPressedOnce = false;
    private QwikcilverViewModel viewModel;
    private SharedPreferences sharedPreferences;
    private AlertDialog spotsDialog;
    private ArrayList<MenuListDataBean> menuListDataBeans;
    private WebView webView;
    private CompositeDisposable compositeDisposable;
    private NavigationMenuAdapter adapter;
    private DrawerLayout drawer;
    private RecyclerView rvMenu;
    // private SwipeRefreshLayout swlMain;
    private String strCommonUrl;
    private TextView tvUserName;
    private ImageView ivUserImage, ivLogo;
    private LinearLayout llLang;
    public final int CUSTOMIZED_REQUEST_CODE = 200;
    private static final int ZXING_CAMERA_PERMISSION = 1;
    private String stringDecider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_dashboard);
        initView();
        setValues();
        setListener();
    }

    private void initView() {
        Window window = this.getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            window.setStatusBarColor(Color.parseColor("#1B1B1A"));

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (null != getSupportActionBar()) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setElevation(0);
        }
        spotsDialog = new CustomSpotsDialog.Builder().setContext(this).setCancelable(true).setTheme(R.style.Custom)
                .build();

        drawer = findViewById(R.id.drawer_layout);
        rvMenu = findViewById(R.id.rvMenu);
        LinearLayout llNavView = findViewById(R.id.llNavView);

        QwikcilverSharedPref sharedPref = new QwikcilverSharedPref();
        sharedPreferences = sharedPref.getQwikcilverSharedPref();

        tvUserName = llNavView.findViewById(R.id.tvUserName);
        ivUserImage = llNavView.findViewById(R.id.ivUserImage);
        llLang = findViewById(R.id.llLang);

        webView = findViewById(R.id.webView);
        // webView.setBackgroundColor(Color.parseColor("#000000"));

        // ClickJavascriptInterface clickJavascriptInterface = new
        // ClickJavascriptInterface(DashboardActivity.this);
        // webView.addJavascriptInterface(clickJavascriptInterface, "ClickFunction");

        // SocialMediaClickJavascriptInterface socialMediaClickJavascriptInterface = new
        // SocialMediaClickJavascriptInterface(DashboardActivity.this);
        // webView.addJavascriptInterface(socialMediaClickJavascriptInterface,
        // "SocialMediaClickFunction");

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setAppCacheEnabled(false);
        webView.clearCache(true);
        webView.clearFocus();
        webView.setWebViewClient(new CustomWebViewClient());

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        // toggle.setDrawerIndicatorEnabled(true);
        // toggle.setHomeAsUpIndicator(R.drawable.appmenu);
        toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.openDrawer(GravityCompat.START);
            }
        });
        toggle.syncState();

        /*
         * viewModel = new LoginViewModel();
         * SobhaSharedPreference sobhaSharedPreference = new SobhaSharedPreference();
         * spotsDialog = new SpotsDialog.Builder().setContext(this).setCancelable(true).
         * setMessage("Please wait...").build();
         * sharedPreferences = sobhaSharedPreference.getSobhaSharedPreference();
         * if (null != getIntent()) {
         * bundle = getIntent().getExtras();
         * }
         * 
         * binding.webView.clearCache(true);
         * binding.webView.clearHistory();
         */
    }

    private void setListener() {
        llLang.setOnClickListener(this);
    }

    private void setValues() {
        viewModel = new QwikcilverViewModel();
        compositeDisposable = new CompositeDisposable();
        menuListDataBeans = new ArrayList<>();
        if (CommonUtil.isInternetAvailable(DashboardActivity.this)) {
            setlanguage();
        } else
            CommonUtil.showShortToast(DashboardActivity.this, getResources().getString(R.string.no_network_err));
    }

    private void setlanguage() {
        String strPlainRequest = "";
        ObjectMapper objectMapper = new ObjectMapper();
        SetLanguageRequestBean menuListRequestBean = new SetLanguageRequestBean();
        menuListRequestBean.set_Tk(QwikcilverSharedPref.getStringValue(sharedPreferences, USER_TOKEN));
        menuListRequestBean.set_Ts(String.valueOf(CommonUtil.getTimeInMilliSec()));
        menuListRequestBean.set_Ln(QwikcilverSharedPref.getStringValue(sharedPreferences, USER_LANG_CODE));
        try {
            strPlainRequest = objectMapper.writeValueAsString(menuListRequestBean);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        Log.d(PLAIN_REQUEST, strPlainRequest);
        String strEncrypted = CommonUtil.getEncryptedData(strPlainRequest);
        Log.d(ENCRYPTED_REQUEST, strEncrypted);
        BaseRequestBean requestBean = new BaseRequestBean();
        requestBean.setUsr_data(strEncrypted);
        spotsDialog.show();
        compositeDisposable.add(viewModel.setLang(requestBean, new Consumer<LoginResponseBean>() {
            @Override
            public void accept(LoginResponseBean menuListResponseBean) throws Exception {
                getMenuListFromAPI();
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                if (null != spotsDialog && spotsDialog.isShowing())
                    spotsDialog.dismiss();
                if (throwable instanceof SocketTimeoutException)
                    CommonUtil.showShortToast(DashboardActivity.this,
                            getResources().getString(R.string.connection_timeout_err));
                else if (throwable instanceof UnknownHostException)
                    CommonUtil.showShortToast(DashboardActivity.this,
                            getResources().getString(R.string.unknown_host_error));
                else
                    CommonUtil.showShortToast(DashboardActivity.this, throwable.getMessage());
            }
        }));
    }

    private void getMenuListFromAPI() {
        String strPlainRequest = "";
        ObjectMapper objectMapper = new ObjectMapper();
        String strUserToken = QwikcilverSharedPref.getStringValue(sharedPreferences, USER_TOKEN);

        if (!TextUtils.isEmpty(strUserToken)) {
            MenuListRequestBean menuListRequestBean = new MenuListRequestBean();
            menuListRequestBean.set_tk(strUserToken);
            menuListRequestBean.set_ts(String.valueOf(CommonUtil.getTimeInMilliSec()));
            try {
                strPlainRequest = objectMapper.writeValueAsString(menuListRequestBean);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            Log.d(PLAIN_REQUEST, strPlainRequest);
            String strEncrypted = CommonUtil.getEncryptedData(strPlainRequest);
            Log.d(ENCRYPTED_REQUEST, strEncrypted);
            BaseRequestBean requestBean = new BaseRequestBean();
            requestBean.setUsr_data(strEncrypted);

            compositeDisposable.add(viewModel.getMenuList(requestBean, new Consumer<MenuListResponseBean>() {
                @Override
                public void accept(MenuListResponseBean menuListResponseBean) throws Exception {
                    setNavigationMenu(menuListResponseBean);
                }
            }, new Consumer<Throwable>() {
                @Override
                public void accept(Throwable throwable) throws Exception {
                    if (null != spotsDialog && spotsDialog.isShowing())
                        spotsDialog.dismiss();
                    if (throwable instanceof SocketTimeoutException)
                        CommonUtil.showShortToast(DashboardActivity.this,
                                getResources().getString(R.string.connection_timeout_err));
                    else if (throwable instanceof UnknownHostException)
                        CommonUtil.showShortToast(DashboardActivity.this,
                                getResources().getString(R.string.unknown_host_error));
                    else
                        CommonUtil.showShortToast(DashboardActivity.this, throwable.getMessage());
                }
            }));
        }
    }

    private void setNavigationMenu(MenuListResponseBean responseBean) {
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.bringToFront();

        if (null != responseBean.getFirstName() && null != responseBean.getLastName())
            tvUserName.setText(responseBean.getFirstName() + " " + responseBean.getLastName());

        Glide.with(DashboardActivity.this)
                .load(responseBean.getImageName())
                .placeholder(R.drawable.ic_account)
                .into(ivUserImage);

        menuListDataBeans = responseBean.getMenuResponseList();

        adapter = new NavigationMenuAdapter(menuListDataBeans, DashboardActivity.this);
        rvMenu.setLayoutManager(new LinearLayoutManager(DashboardActivity.this, LinearLayoutManager.VERTICAL, false));
        rvMenu.setAdapter(adapter);

        String strTokenDef = QwikcilverSharedPref.getStringValue(sharedPreferences, USER_TOKEN);
        String strURLLoadDefault = QwikcilverAPI.getRedirectURL() + "/LogMeIn?token=" + strTokenDef
                + "&action=Index&ctrl=Home&module=MobileApp&area=";
        webView.loadUrl(strURLLoadDefault);
        setMenuListener();
    }

    private void setMenuListener() {
        compositeDisposable.add(adapter.getSelectedMenu().subscribe(new Consumer<MenuListDataBean>() {
            @Override
            public void accept(MenuListDataBean menuListDataBean) throws Exception {
                handleMenuClick(menuListDataBean);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                CommonUtil.showShortToast(DashboardActivity.this, throwable.getMessage());
            }
        }));
    }

    private void handleMenuClick(MenuListDataBean menuItemBean) {
        drawer.closeDrawers();

        if (TextUtils.isEmpty(menuItemBean.getmDL_SUBTITLE1())) {
            String strBase = QwikcilverAPI.getRedirectURL();
            String strToken = QwikcilverSharedPref.getStringValue(sharedPreferences, USER_TOKEN);
            String strAction = menuItemBean.getmDL_ACTION();
            String strController = menuItemBean.getmDL_CONTROLLER();
            String strModule = "mobileapp";
            // String strArea = menuItemBean.getmDL_AREA();
            // String strAcyId = SchoolHandySharedPref.getStringValue(sharedPreferences,
            // ACY_ID);
            // String strRoleId = SchoolHandySharedPref.getStringValue(sharedPreferences,
            // ROLE_ID);

            String strLoad = strBase + "/logmein?token=" + strToken + "&action=" + strAction + "&ctrl=" + strController
                    + "&module=" + strModule;// + "&area=" + strArea + "&acyid=" + strAcyId + "&roleid=" + strRoleId;
            Log.d("MenuAction", strLoad);
            webView.loadUrl(strLoad);
        } else if (!TextUtils.isEmpty(menuItemBean.getmDL_SUBTITLE1())
                && menuItemBean.getmDL_SUBTITLE1().equalsIgnoreCase("ClaimUPI")) {
            checkCameraPermisssion(menuItemBean.getmDL_SUBTITLE1());
        } else if (!TextUtils.isEmpty(menuItemBean.getmDL_SUBTITLE1())
                && menuItemBean.getmDL_SUBTITLE1().equalsIgnoreCase("GenuinityCheck")) {
            checkCameraPermisssion(menuItemBean.getmDL_SUBTITLE1());
        }
    }

    private void checkCameraPermisssion(String strDecider) {
        stringDecider = strDecider;
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[] { Manifest.permission.CAMERA }, ZXING_CAMERA_PERMISSION);
        } else {
            Intent intent = new Intent(this, ScanQRNewActivity.class);
            intent.putExtra("decider", strDecider);
            startActivity(intent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == ZXING_CAMERA_PERMISSION) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[0])) {
                // denied
                CommonUtil.showLongToast(DashboardActivity.this,
                        "Please grant camera permission to use the QR Scanner");
                // finish();
            } else {
                if (ActivityCompat.checkSelfPermission(this, permissions[0]) == PackageManager.PERMISSION_GRANTED) {
                    // allowed
                    // CommonUtil.showShortToast(ScanActivity.this,"Allow camera permission");
                    Intent intent = new Intent(this, ScanQRNewActivity.class);
                    intent.putExtra("decider", stringDecider);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", getPackageName(), null);
                    intent.setData(uri);
                    startActivity(intent);
                    CommonUtil.showLongToast(DashboardActivity.this, "Allow camera permission");
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode != CUSTOMIZED_REQUEST_CODE && requestCode != IntentIntegrator.REQUEST_CODE) {
            // This is important, otherwise the result will not be passed to the fragment
            super.onActivityResult(requestCode, resultCode, data);
            return;
        }

        if (null != data) {
            if (null != data.getExtras()) {
                String strToUserId = data.getExtras().getString("SCAN_RESULT");
                // validateQR();
            } else {
                CommonUtil.showShortToast(this, "No ID found");
            }
        } else {
            // CommonUtil.showShortToast(this, "Scan cancelled!!");
        }
    }

    private void validateQR() {
        String strPlainRequest = "";
        ObjectMapper objectMapper = new ObjectMapper();
        ValidateQRRequestBean validateQRRequestBean = new ValidateQRRequestBean();
        validateQRRequestBean.set_Tk(QwikcilverSharedPref.getStringValue(sharedPreferences, USER_TOKEN));
        validateQRRequestBean.set_Ts(String.valueOf(CommonUtil.getTimeInMilliSec()));
        validateQRRequestBean.set_Qd("123456789");
        try {
            strPlainRequest = objectMapper.writeValueAsString(validateQRRequestBean);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        Log.d(PLAIN_REQUEST, strPlainRequest);
        String strEncrypted = CommonUtil.getEncryptedData(strPlainRequest);
        Log.d(ENCRYPTED_REQUEST, strEncrypted);
        BaseRequestBean requestBean = new BaseRequestBean();
        requestBean.setUsr_data(strEncrypted);

        spotsDialog.show();
        viewModel.checkQR(requestBean, new Consumer<CheckQRResponseBean>() {
            @Override
            public void accept(CheckQRResponseBean loginResponseBean) throws Exception {
                CommonUtil.showShortToast(DashboardActivity.this, loginResponseBean.getMessage());
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                CommonUtil.showShortToast(DashboardActivity.this, throwable.getMessage());
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                showLogoutDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.llLang:
                Intent intent = new Intent(DashboardActivity.this, LanguageActivity.class);
                intent.putExtra("fromDash", true);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (webView.canGoBack()) {
                webView.goBack();
            } else {
                if (doubleBackToExitPressedOnce) {
                    super.onBackPressed();
                    return;
                }
                this.doubleBackToExitPressedOnce = true;
                Toast.makeText(this, getResources().getString(R.string.str_back_press), Toast.LENGTH_SHORT).show();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        doubleBackToExitPressedOnce = false;
                    }
                }, 2000);
            }
        }
    }

    private class CustomWebViewClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            Log.d("URLTOLOAD", url);
            if (null != spotsDialog) {
                if (!spotsDialog.isShowing())
                    spotsDialog.show();
            }
            if (url.contains("login") || url.contains("Login")) {
                Intent intent = new Intent(DashboardActivity.this, SplashActivity.class);
                startActivity(intent);
                finish();
            }
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            strCommonUrl = url;
            try {
                if (null != spotsDialog && spotsDialog.isShowing())
                    spotsDialog.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void showLogoutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getResources().getString(R.string.str_confirm));
        builder.setMessage(getResources().getString(R.string.str_logout_prompt));

        builder.setPositiveButton(getResources().getString(R.string.str_yes), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if (CommonUtil.isInternetAvailable(DashboardActivity.this))
                    callLogoutAPI();
                else
                    CommonUtil.showShortToast(DashboardActivity.this,
                            getResources().getString(R.string.no_network_err));
            }
        });

        builder.setNegativeButton(getResources().getString(R.string.str_no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void callLogoutAPI() {
        if (null != spotsDialog)
            spotsDialog.show();
        String strPlainRequest = "";
        ObjectMapper objectMapper = new ObjectMapper();
        LogoutRequestBean logoutRequestBean = new LogoutRequestBean();
        logoutRequestBean.set_tk(QwikcilverSharedPref.getStringValue(sharedPreferences, USER_TOKEN));
        logoutRequestBean.set_ts(String.valueOf(CommonUtil.getTimeInMilliSec()));
        try {
            strPlainRequest = objectMapper.writeValueAsString(logoutRequestBean);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        String strEncrypted = CommonUtil.getEncryptedData(strPlainRequest);
        Log.d("EncryptedLogout", strEncrypted);
        BaseRequestBean requestBean = new BaseRequestBean();
        requestBean.setUsr_data(strEncrypted);

        viewModel.logout(requestBean, new Consumer<LoginResponseBean>() {
            @Override
            public void accept(LoginResponseBean loginResponseBean) throws Exception {
                if (null != spotsDialog)
                    spotsDialog.dismiss();
                CommonUtil.logoutUser();

                Locale locale = new Locale("en");
                locale.setDefault(locale);
                Configuration configuration = new Configuration();
                configuration.locale = locale;
                getBaseContext().getResources().updateConfiguration(configuration,
                        getBaseContext().getResources().getDisplayMetrics());

                Intent intent = new Intent(DashboardActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                if (null != spotsDialog)
                    spotsDialog.dismiss();
                CommonUtil.showShortToast(DashboardActivity.this, throwable.getMessage());
            }
        });
    }
}