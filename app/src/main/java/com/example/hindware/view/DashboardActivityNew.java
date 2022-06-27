package com.example.hindware.view;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.webkit.CookieManager;
import android.webkit.JavascriptInterface;
import android.webkit.URLUtil;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.signature.ObjectKey;
import com.example.hindware.model.GetNotificationRequestBean;
import com.example.hindware.model.GetNotificationResponseBean;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.material.navigation.NavigationView;
import com.example.hindware.R;
import com.example.hindware.adapter.NavigationMenuAdapter;
import com.example.hindware.databinding.ActivityDashboardNewBinding;
import com.example.hindware.model.BaseRequestBean;
import com.example.hindware.model.ChangePhotoRequestBean;
import com.example.hindware.model.LoginResponseBean;
import com.example.hindware.model.LogoutRequestBean;
import com.example.hindware.model.MenuListDataBean;
import com.example.hindware.model.MenuListRequestBean;
import com.example.hindware.model.MenuListResponseBean;
import com.example.hindware.model.SetLanguageRequestBean;
import com.example.hindware.utility.CommonUtil;
import com.example.hindware.utility.QwikcilverAPI;
import com.example.hindware.utility.QwikcilverSharedPref;
import com.example.hindware.utility.customprogress.CustomSpotsDialog;
import com.example.hindware.viewmodel.QwikcilverViewModel;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;

import static com.example.hindware.utility.Constants.CONTROLLER;
import static com.example.hindware.utility.Constants.ENCRYPTED_REQUEST;
import static com.example.hindware.utility.Constants.IS_RELOAD;
import static com.example.hindware.utility.Constants.PLAIN_REQUEST;
import static com.example.hindware.utility.Constants.TITLE;
import static com.example.hindware.utility.Constants.USER_LANG_CODE;
import static com.example.hindware.utility.Constants.USER_POINT;
import static com.example.hindware.utility.Constants.USER_TOKEN;

public class DashboardActivityNew extends LangBaseActivity implements View.OnClickListener {

    private static final int FILE_SELECT_CODE = 11;
    private Uri myURI;
    private ActivityDashboardNewBinding binding;
    private boolean doubleBackToExitPressedOnce = false;
    private QwikcilverViewModel viewModel;
    private SharedPreferences sharedPreferences;
    private AlertDialog spotsDialog;
    private ArrayList<MenuListDataBean> menuListDataBeans;
    private CompositeDisposable compositeDisposable;
    private NavigationMenuAdapter adapter;
    public final int CUSTOMIZED_REQUEST_CODE = 200;
    private static final int ZXING_CAMERA_PERMISSION = 1;
    private String stringTitle, stringContoller;
    private DrawerLayout drawer;
    private RecyclerView rvMenuNew;
    private WebView webView;
    private ImageView ivMenuBar, ivDashProfile, ivNavProfile, ivNoti;
    private TextView tvName, tvPoints, tvNavName, tvNavPoints;
    private boolean fromNavigation;
    private String fileBase64;
    private String strCommonUrl;
    private DownloadManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_dashboard_new);
        initView();
        setListener();
        setLanguage();
    }

    @Override
    protected void onStart() {
        super.onStart();
        String[] langArrayNew = QwikcilverSharedPref.getStringValue(sharedPreferences, USER_LANG_CODE).split("-");
        Locale locale = new Locale(langArrayNew[0]);
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.setLocale(locale);
        configuration.setLayoutDirection(locale);
        getBaseContext().getResources().updateConfiguration(configuration,
                getBaseContext().getResources().getDisplayMetrics());
    }

    private void initView() {
        QwikcilverSharedPref sharedPref = new QwikcilverSharedPref();
        sharedPreferences = sharedPref.getQwikcilverSharedPref();

        Window window = this.getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            window.setStatusBarColor(Color.parseColor("#1B1B1A"));

        spotsDialog = new CustomSpotsDialog.Builder().setContext(this).setCancelable(true).setTheme(R.style.Custom)
                .build();

        drawer = findViewById(R.id.drawer_layout_new);
        rvMenuNew = findViewById(R.id.rvMenuNew);
        ivMenuBar = findViewById(R.id.ivMenuBar);
        ivDashProfile = findViewById(R.id.ivDashProfile);
        ivNavProfile = findViewById(R.id.ivNavProfile);
        ivNoti = findViewById(R.id.ivNoti);
        tvName = findViewById(R.id.tvName);
        tvNavName = findViewById(R.id.tvNavName);
        tvPoints = findViewById(R.id.tvPoints);
        tvNavPoints = findViewById(R.id.tvNavPoints);

        viewModel = new QwikcilverViewModel();
        compositeDisposable = new CompositeDisposable();
        menuListDataBeans = new ArrayList<>();

        webView = findViewById(R.id.webViewNew);

        ScanQRClickJavascriptInterface scanQRClickJavascriptInterface = new ScanQRClickJavascriptInterface(
                DashboardActivityNew.this);
        webView.addJavascriptInterface(scanQRClickJavascriptInterface, "ScanQRCodeClickFunction");
        ProfileImageUploadJavascriptInterface profileImageUploadJavascriptInterface = new ProfileImageUploadJavascriptInterface(DashboardActivityNew.this);
        webView.addJavascriptInterface(profileImageUploadJavascriptInterface, "ProfileImageFunction");

        ExportFileJavascriptInterface exportFileJavascriptInterface = new ExportFileJavascriptInterface(
                DashboardActivityNew.this);
        webView.addJavascriptInterface(exportFileJavascriptInterface, "ExportFileDataFunction");

        OrderJavascriptInterface orderJavascriptInterface = new OrderJavascriptInterface(DashboardActivityNew.this);
        webView.addJavascriptInterface(orderJavascriptInterface, "OrderFunction");

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setAppCacheEnabled(false);
        webView.clearCache(true);
        webView.clearFocus();
        webView.setWebViewClient(new DashboardActivityNew.CustomWebViewClient());

        NavigationView navigationView = findViewById(R.id.nav_view_new);
        navigationView.bringToFront();
    }

    private void setListener() {
        ivMenuBar.setOnClickListener(this);
        ivNavProfile.setOnClickListener(this);
        ivDashProfile.setOnClickListener(this);
        ivNoti.setOnClickListener(this);
    }

    private void setLanguage() {
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
                getNotificationList();
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                if (null != spotsDialog && spotsDialog.isShowing())
                    spotsDialog.dismiss();
                if (throwable instanceof SocketTimeoutException)
                    CommonUtil.showShortToast(DashboardActivityNew.this,
                            getResources().getString(R.string.connection_timeout_err));
                else if (throwable instanceof UnknownHostException)
                    CommonUtil.showShortToast(DashboardActivityNew.this,
                            getResources().getString(R.string.unknown_host_error));
                else
                    CommonUtil.showShortToast(DashboardActivityNew.this, throwable.getMessage());
            }
        }));
    }


    private void getNotificationList() {
        String strPlainRequest = "";
        ObjectMapper objectMapper = new ObjectMapper();
        String strUserToken = QwikcilverSharedPref.getStringValue(sharedPreferences, USER_TOKEN);

        if (!TextUtils.isEmpty(strUserToken)) {
            GetNotificationRequestBean getNotificationRequestBean = new GetNotificationRequestBean();
            getNotificationRequestBean.set_tk(strUserToken);
            getNotificationRequestBean.set_ts(String.valueOf(CommonUtil.getTimeInMilliSec()));
            getNotificationRequestBean.set_act("0");
            try {
                strPlainRequest = objectMapper.writeValueAsString(getNotificationRequestBean);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            Log.d("PLAIN_REQUEST", strPlainRequest);
            String strEncrypted = CommonUtil.getEncryptedData(strPlainRequest);
            Log.d(ENCRYPTED_REQUEST, strEncrypted);
            BaseRequestBean requestBean = new BaseRequestBean();
            requestBean.setUsr_data(strEncrypted);

            compositeDisposable.add(viewModel.getNotification(requestBean, new Consumer<GetNotificationResponseBean>() {
                @Override
                public void accept(GetNotificationResponseBean getNotificationResponseBean) throws Exception {

                    Log.e("NotifcaitonURL", getNotificationResponseBean.getMessage());
                    String image = getNotificationResponseBean.getMessage();
                    Log.e("DATAOFIMAGE",image);
                    try {
                        URL url = new URL(image);
                        Picasso.get()
                                .load(String.valueOf(url))
                                .into(ivNoti);
                    }
                    catch(Exception e)
                    {
                    }


                }
            }, new Consumer<Throwable>() {
                @Override
                public void accept(Throwable throwable) throws Exception {
                    if (null != spotsDialog && spotsDialog.isShowing())
                        spotsDialog.dismiss();
                    if (throwable instanceof SocketTimeoutException)
                        CommonUtil.showShortToast(DashboardActivityNew.this,
                                getResources().getString(R.string.connection_timeout_err));
                    else if (throwable instanceof UnknownHostException)
                        CommonUtil.showShortToast(DashboardActivityNew.this,
                                getResources().getString(R.string.unknown_host_error));
                    else
                        CommonUtil.showShortToast(DashboardActivityNew.this, throwable.getMessage());
                }
            }));
        }
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
                        CommonUtil.showShortToast(DashboardActivityNew.this,
                                getResources().getString(R.string.connection_timeout_err));
                    else if (throwable instanceof UnknownHostException)
                        CommonUtil.showShortToast(DashboardActivityNew.this,
                                getResources().getString(R.string.unknown_host_error));
                    else
                        CommonUtil.showShortToast(DashboardActivityNew.this, throwable.getMessage());
                }
            }));
        }
    }

    private void setNavigationMenu(MenuListResponseBean responseBean) {
        if (null != responseBean.getFirstName()) {
            tvName.setText(DashboardActivityNew.this.getResources().getString(R.string.welcome_text) + " "
                    + responseBean.getFirstName());
        }

        if (null != responseBean.getPointBalance()) {
            tvPoints.setText(DashboardActivityNew.this.getResources().getString(R.string.points_text) + " "
                    + responseBean.getPointBalance());
            tvNavPoints.setText(DashboardActivityNew.this.getResources().getString(R.string.points_text) + " "
                    + responseBean.getPointBalance());
            QwikcilverSharedPref.setStringValue(sharedPreferences, USER_POINT, responseBean.getPointBalance());
        } else {
            tvPoints.setText(DashboardActivityNew.this.getResources().getString(R.string.points_text) + " 0");
            tvNavPoints.setText(DashboardActivityNew.this.getResources().getString(R.string.points_text) + " 0");
            QwikcilverSharedPref.setStringValue(sharedPreferences, USER_POINT, "0");
        }

        if (null != responseBean.getFirstName() && null == responseBean.getLastName()) {
            tvNavName.setText(DashboardActivityNew.this.getResources().getString(R.string.welcome_text) + " "
                    + responseBean.getFirstName());
        } else if (null == responseBean.getFirstName() && null != responseBean.getLastName()) {
            tvNavName.setText(DashboardActivityNew.this.getResources().getString(R.string.welcome_text) + " "
                    + responseBean.getLastName());
        } else if (null == responseBean.getFirstName() && null == responseBean.getLastName()) {
            tvNavName.setText(DashboardActivityNew.this.getResources().getString(R.string.welcome_text) + " " + "User");
        } else {
            String textNavName = DashboardActivityNew.this.getResources().getString(R.string.welcome_text) + " "
                    + responseBean.getFirstName() + " " + responseBean.getLastName();
            if (textNavName.length() > 15) {
                textNavName = textNavName.substring(0, 15) + "...";
                tvNavName.setText(textNavName);
            }
            tvNavName.setText(textNavName);
        }
        setProfileImageAfterUpdate(responseBean.getImageURL());

        menuListDataBeans = responseBean.getMenuResponseList();

        adapter = new NavigationMenuAdapter(menuListDataBeans, DashboardActivityNew.this);
        rvMenuNew.setLayoutManager(
                new LinearLayoutManager(DashboardActivityNew.this, LinearLayoutManager.VERTICAL, false));
        rvMenuNew.setAdapter(adapter);
        String strTokenDef = QwikcilverSharedPref.getStringValue(sharedPreferences, USER_TOKEN);
        String strURLLoadDefault = QwikcilverAPI.getRedirectURL() + "/LogMeIn?token=" + strTokenDef
                + "&action=Index&ctrl=Home&module=MobileApp&area=";

        Log.d("strURLLoadDefault", strURLLoadDefault);
        webView.loadUrl(strURLLoadDefault);
        setMenuListener();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivMenuBar:
                drawer.openDrawer(GravityCompat.START);
                break;
            case R.id.ivNavProfile:
            case R.id.ivDashProfile:
                navigateToProfile();
                break;
            case R.id.ivNoti:
                String strTokenDef = QwikcilverSharedPref.getStringValue(sharedPreferences, USER_TOKEN);
                String strURLLoadDefault = QwikcilverAPI.getRedirectURL() + "/LogMeIn?token=" + strTokenDef
                        + "&action=Index&ctrl=Message&module=MobileApp&area=";

                Log.d("strURLLoadDefault", strURLLoadDefault);
                webView.loadUrl(strURLLoadDefault);
//                NotificationDialog notificationDialog = new NotificationDialog();
//                notificationDialog.show(getSupportFragmentManager(), "NotificationDialog");
                break;
        }
    }

    private void navigateToProfile() {
        if (drawer.isDrawerOpen(GravityCompat.START))
            drawer.closeDrawers();
        if (null != spotsDialog)
            spotsDialog.show();
        String strBase = QwikcilverAPI.getRedirectURL();
        String strToken = QwikcilverSharedPref.getStringValue(sharedPreferences, USER_TOKEN);
        String strAction = "Index";
        String strController = "UserProfile";
        String strModule = "mobileapp";
        String strLoad = strBase + "/logmein?token=" + strToken + "&action=" + strAction + "&ctrl=" + strController
                + "&module=" + strModule;

        Log.d("strLoadforProfile", strLoad);
        webView.loadUrl(strLoad);
    }

    public class ScanQRClickJavascriptInterface {
        Context mContext;

        ScanQRClickJavascriptInterface(Context c) {
            mContext = c;
        }

        @JavascriptInterface
        public void onScanQRCodeClick(String strText) {
            fromNavigation = false;
            checkCameraPermisssion("", "", fromNavigation);
        }
    }

    public class ProfileImageUploadJavascriptInterface {
        Context mContext;


        ProfileImageUploadJavascriptInterface(Context c) {
            mContext = c;

        }

        @JavascriptInterface
        public void onProfileImageUploadClick() {

            onBrowse();
        }
    }


    public class ExportFileJavascriptInterface {
        Context mContext;

        ExportFileJavascriptInterface(Context c) {
            mContext = c;
        }

        @JavascriptInterface
        public void onExportFileClick(String fileURL) {
            Log.e("onExportFileClick",fileURL);

            fileDownload(fileURL);
        }
    }



    private void fileDownload(String fileURL){
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(fileURL));
        String title = URLUtil.guessFileName(fileURL,null,null);
        Log.e("title",title);
        request.setTitle(title);
        request.setDescription("Downloading file Please Wait....");
        String cookie= CookieManager.getInstance().getCookie(fileURL);
        request.addRequestHeader("cookie",cookie);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,title);
        manager = (DownloadManager)getSystemService(DOWNLOAD_SERVICE);
        manager.enqueue(request);

        Toast.makeText(DashboardActivityNew.this,"File Download Started",Toast.LENGTH_LONG).show();
    }

    public class OrderJavascriptInterface {
        Context mContext;

        OrderJavascriptInterface(Context c) {
            mContext = c;
        }

        @JavascriptInterface
        public void onCalled() {

        }
    }

    private void onBrowse() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        try {
            startActivityForResult(
                    Intent.createChooser(intent, "Select image file"),
                    FILE_SELECT_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            // Potentially direct the user to the Market with a Dialog
            Toast.makeText(this, "Please install a File Manager.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case FILE_SELECT_CODE:
                if (resultCode == RESULT_OK) {
                    Uri uri = data.getData();
                    myURI = data.getData();
                    callUpdateProfileImageAPi();
                }
                break;
        }
    }

    private void callUpdateProfileImageAPi() {
        if (null != spotsDialog) {
            if (!spotsDialog.isShowing()) {
                spotsDialog.show();
            }
        }
        byte[] bytes;
        try {
            InputStream in = getContentResolver().openInputStream(myURI);
            bytes = getBytes(in);
            Log.d("data", "onActivityResult: bytes size=" + bytes.length);
            Log.d("data", "onActivityResult: Base64string=" + Base64.encodeToString(bytes, Base64.DEFAULT));
            String ansValue = Base64.encodeToString(bytes, Base64.DEFAULT);
            ansValue = ansValue.replace(" ", "");
            ansValue = ansValue.replace("\n", "");
            Log.d("myBase64", ansValue);
            fileBase64 = ansValue;

            String strEncryptedToken = CommonUtil
                    .getEncryptedData(QwikcilverSharedPref.getStringValue(sharedPreferences, USER_TOKEN));
            ChangePhotoRequestBean changePhotoRequestBean = new ChangePhotoRequestBean();
            changePhotoRequestBean.setAthdata(strEncryptedToken);
            changePhotoRequestBean.setPhotodata(fileBase64);

            viewModel.changePhoto(changePhotoRequestBean, new Consumer<LoginResponseBean>() {
                @Override
                public void accept(LoginResponseBean loginResponseBean) throws Exception {
                    CommonUtil.showShortToast(DashboardActivityNew.this,
                            DashboardActivityNew.this.getResources().getString(R.string.profile_image_change));
                    if (null != spotsDialog) {
                        if (spotsDialog.isShowing()) {
                            spotsDialog.dismiss();
                        }
                    }
                    setProfileImageAfterUpdate(loginResponseBean.getMessage());
                    webView.reload();
                }
            }, new Consumer<Throwable>() {
                @Override
                public void accept(Throwable throwable) throws Exception {
                    CommonUtil.showShortToast(DashboardActivityNew.this, throwable.getMessage());
                    if (null != spotsDialog) {
                        if (spotsDialog.isShowing()) {
                            spotsDialog.dismiss();
                        }
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("error", "onActivityResult: " + e.toString());
        }
    }

    private void setProfileImageAfterUpdate(String imageURL) {
        Glide.with(DashboardActivityNew.this)
                .load(imageURL)
                .apply(RequestOptions.skipMemoryCacheOf(true))
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                .placeholder(R.drawable.ic_account)
                .into(ivDashProfile);

        Glide.with(DashboardActivityNew.this)
                .load(imageURL)
                .apply(RequestOptions.skipMemoryCacheOf(true))
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                .placeholder(R.drawable.ic_account)
                .into(ivNavProfile);
    }

    public byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

    private class CustomWebViewClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            Log.d("URLTOLOAD Hindware", url);
            if (null != spotsDialog) {
                if (!spotsDialog.isShowing())
                    spotsDialog.show();
            }
            if (url.contains("login") || url.contains("Login")) {
                Intent intent = new Intent(DashboardActivityNew.this, SplashActivity.class);
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

    private void setMenuListener() {
        compositeDisposable.add(adapter.getSelectedMenu().subscribe(new Consumer<MenuListDataBean>() {
            @Override
            public void accept(MenuListDataBean menuListDataBean) throws Exception {
                handleMenuClick(menuListDataBean);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                CommonUtil.showShortToast(DashboardActivityNew.this, throwable.getMessage());
            }
        }));
    }

    @Override
    protected void onResume() {
        super.onResume();
        tvPoints.setText(DashboardActivityNew.this.getResources().getString(R.string.points_text) + " "
                + QwikcilverSharedPref.getStringValue(sharedPreferences, USER_POINT));
        tvNavPoints.setText(DashboardActivityNew.this.getResources().getString(R.string.points_text) + " "
                + QwikcilverSharedPref.getStringValue(sharedPreferences, USER_POINT));
        if (QwikcilverSharedPref.getBooleanValue(sharedPreferences, IS_RELOAD)) {
            webView.reload();
            QwikcilverSharedPref.setBooleanValue(sharedPreferences, IS_RELOAD, false);
        }
    }

    private void handleMenuClick(MenuListDataBean menuItemBean) {
        drawer.closeDrawers();

        if (!TextUtils.isEmpty(menuItemBean.getmDL_CONTROLLER()) && menuItemBean.getmDL_CONTROLLER()
                .equalsIgnoreCase(DashboardActivityNew.this.getResources().getString(R.string.claim_upi_controller))) {
            fromNavigation = true;
            checkCameraPermisssion(menuItemBean.getmDL_TITLE(), menuItemBean.getmDL_CONTROLLER(), fromNavigation);
        } else if (!TextUtils.isEmpty(menuItemBean.getmDL_CONTROLLER())
                && menuItemBean.getmDL_CONTROLLER().equalsIgnoreCase(
                        DashboardActivityNew.this.getResources().getString(R.string.genuinity_check_controller))) {
            fromNavigation = true;
            checkCameraPermisssion(menuItemBean.getmDL_TITLE(), menuItemBean.getmDL_CONTROLLER(), fromNavigation);
        } else if (!TextUtils.isEmpty(menuItemBean.getmDL_CONTROLLER()) && menuItemBean.getmDL_CONTROLLER()
                .equalsIgnoreCase(DashboardActivityNew.this.getResources().getString(R.string.logout_controller))) {
            showLogoutDialog();
        } else {
            if (null != spotsDialog) {
                if (!spotsDialog.isShowing()) {
                    spotsDialog.show();
                }
            }
            String strBase = QwikcilverAPI.getRedirectURL();
            String strToken = QwikcilverSharedPref.getStringValue(sharedPreferences, USER_TOKEN);
            String strAction = menuItemBean.getmDL_ACTION();
            String strController = menuItemBean.getmDL_CONTROLLER();
            String strModule = "mobileapp";

            String strLoad = strBase + "/logmein?token=" + strToken + "&action=" + strAction + "&ctrl=" + strController
                    + "&module=" + strModule;// + "&area=" + strArea + "&acyid=" + strAcyId + "&roleid=" + strRoleId;
            Log.d("MenuAction", strLoad);
            webView.loadUrl(strLoad);
        }
    }

    private void checkCameraPermisssion(String strTitle, String strController, boolean fromNavi) {
        if (fromNavi) {
            stringTitle = strTitle;
            stringContoller = strController;
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[] { Manifest.permission.CAMERA }, ZXING_CAMERA_PERMISSION);
            } else {
                Intent intent = new Intent(this, ScanQRNewActivity.class);
                intent.putExtra(TITLE, strTitle);
                intent.putExtra(CONTROLLER, strController);
                startActivity(intent);
            }
        } else {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[] { Manifest.permission.CAMERA }, ZXING_CAMERA_PERMISSION);
            } else {
                ScannerDialog appUpdateDialog = new ScannerDialog();
                appUpdateDialog.show(getSupportFragmentManager(), "ScannerDialog");
            }
        }
    }

    private void showLogoutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getResources().getString(R.string.str_confirm));
        builder.setMessage(getResources().getString(R.string.str_logout_prompt));

        builder.setPositiveButton(getResources().getString(R.string.str_yes), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if (CommonUtil.isInternetAvailable(DashboardActivityNew.this))
                    callLogoutAPI();
                else
                    CommonUtil.showShortToast(DashboardActivityNew.this,
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
        if (CommonUtil.isInternetAvailable(DashboardActivityNew.this)) {
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

            compositeDisposable.add(viewModel.logout(requestBean, new Consumer<LoginResponseBean>() {
                @Override
                public void accept(LoginResponseBean loginResponseBean) throws Exception {
                    if (loginResponseBean.getResponse_code().equalsIgnoreCase("33")) {
                        if (null != spotsDialog)
                            spotsDialog.dismiss();
                        CommonUtil.showShortToast(DashboardActivityNew.this, loginResponseBean.getMessage());
                        CommonUtil.logoutUser();
                        Intent intent = new Intent(DashboardActivityNew.this, LoginActivityNew.class);
                        startActivity(intent);
                        finish();
                    } else if (loginResponseBean.getResponse_code().equalsIgnoreCase("00")) {
                        if (null != spotsDialog)
                            spotsDialog.dismiss();
                        CommonUtil.logoutUser();
                        Intent intent = new Intent(DashboardActivityNew.this, LoginActivityNew.class);
                        startActivity(intent);
                        finish();
                    }
                }
            }, new Consumer<Throwable>() {
                @Override
                public void accept(Throwable throwable) throws Exception {
                    if (null != spotsDialog)
                        spotsDialog.dismiss();
                    CommonUtil.showShortToast(DashboardActivityNew.this, throwable.getMessage());
                }
            }));
        } else {
            CommonUtil.showShortToast(DashboardActivityNew.this,
                    DashboardActivityNew.this.getResources().getString(R.string.no_network_err));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == ZXING_CAMERA_PERMISSION) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[0])) {
                // denied
                CommonUtil.showLongToast(DashboardActivityNew.this,
                        DashboardActivityNew.this.getResources().getString(R.string.allow_cam_permission));
                // finish();
            } else {
                if (fromNavigation) {
                    if (ActivityCompat.checkSelfPermission(this, permissions[0]) == PackageManager.PERMISSION_GRANTED) {
                        // allowed
                        // CommonUtil.showShortToast(ScanActivity.this,"Allow camera permission");
                        Intent intent = new Intent(this, ScanQRNewActivity.class);
                        intent.putExtra(TITLE, stringTitle);
                        intent.putExtra(CONTROLLER, stringContoller);
                        startActivity(intent);

                    } else {
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivity(intent);
                        CommonUtil.showLongToast(DashboardActivityNew.this, DashboardActivityNew.this.getResources()
                                .getString(R.string.allow_cam_permission_setting));
                    }
                } else {
                    if (ActivityCompat.checkSelfPermission(this, permissions[0]) == PackageManager.PERMISSION_GRANTED) {
                        // allowed
                        // CommonUtil.showShortToast(ScanActivity.this,"Allow camera permission");
                        ScannerDialog appUpdateDialog = new ScannerDialog();
                        appUpdateDialog.show(getSupportFragmentManager(), "ScannerDialog");

                    } else {
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivity(intent);
                        CommonUtil.showLongToast(DashboardActivityNew.this, "Allow camera permission");
                    }
                }

            }
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
                CommonUtil.showShortToast(this, getResources().getString(R.string.str_back_press));

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        doubleBackToExitPressedOnce = false;
                    }
                }, 2000);
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        // if(null != compositeDisposable)
        // compositeDisposable.clear();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != compositeDisposable)
            compositeDisposable.dispose();
    }
}