package com.example.hindware.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import androidx.databinding.DataBindingUtil;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.hindware.R;
import com.example.hindware.model.BaseRequestBean;
import com.example.hindware.model.CheckAppVersionRequestBean;
import com.example.hindware.model.CheckAppVersionResponseBean;
import com.example.hindware.model.LoginResponseBean;
import com.example.hindware.model.VaidateTokenRequestBean;
import com.example.hindware.utility.CommonUtil;
import com.example.hindware.utility.QwikcilverAPI;
import com.example.hindware.utility.QwikcilverSharedPref;
import com.example.hindware.viewmodel.QwikcilverViewModel;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;

import static com.example.hindware.utility.Constants.ENCRYPTED_REQUEST;
import static com.example.hindware.utility.Constants.PLAIN_REQUEST;
import static com.example.hindware.utility.Constants.USER_TOKEN;

public class SplashActivity extends BaseActivity {

    private SharedPreferences sharedPreferences;
    private QwikcilverViewModel viewModel;
    private CompositeDisposable compositeDisposable;
    private Runnable splashRunnable = new Runnable() {
        @Override
        public void run() {
            // checkAppVersion();
            jump();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DataBindingUtil.setContentView(this, R.layout.activity_splash);
        initView();
    }

    private void initView() {
        QwikcilverSharedPref qwikcilverSharedPreference = new QwikcilverSharedPref();
        sharedPreferences = qwikcilverSharedPreference.getQwikcilverSharedPref();
        viewModel = new QwikcilverViewModel();
        compositeDisposable = new CompositeDisposable();
        Handler handler = new Handler();
        handler.postDelayed(splashRunnable, 2000);
    }

    private void jump() {
        String strPlainRequest = "";
        ObjectMapper objectMapper = new ObjectMapper();
        String strUserToken = QwikcilverSharedPref.getStringValue(sharedPreferences, USER_TOKEN);
        if (!TextUtils.isEmpty(strUserToken)) {
            Log.d("UserToken", strUserToken);
            VaidateTokenRequestBean vaidateTokenRequestBean = new VaidateTokenRequestBean();
            vaidateTokenRequestBean.set_tk(strUserToken);
            vaidateTokenRequestBean.set_ts(String.valueOf(CommonUtil.getTimeInMilliSec()));
            try {
                strPlainRequest = objectMapper.writeValueAsString(vaidateTokenRequestBean);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            String strEncrypted = CommonUtil.getEncryptedData(strPlainRequest);
            Log.d("EncryptedValidate", strEncrypted);
            BaseRequestBean validateRequestBean = new BaseRequestBean();
            validateRequestBean.setUsr_data(strEncrypted);

            compositeDisposable.add(viewModel.validateToken(validateRequestBean, new Consumer<LoginResponseBean>() {
                @Override
                public void accept(LoginResponseBean loginResponseBean) throws Exception {
                    goToDashboard(loginResponseBean);
                }
            }, new Consumer<Throwable>() {
                @Override
                public void accept(Throwable throwable) throws Exception {
                    startLogin();
                    if (throwable instanceof SocketTimeoutException)
                        CommonUtil.showShortToast(SplashActivity.this,
                                getResources().getString(R.string.connection_timeout_err));
                    else if (throwable instanceof UnknownHostException)
                        CommonUtil.showShortToast(SplashActivity.this,
                                getResources().getString(R.string.unknown_host_error));
                    else
                        CommonUtil.showShortToast(SplashActivity.this, throwable.getMessage());
                }
            }));
        } else {
            startLogin();
        }
    }

    private void checkAppVersion() {
        String strPlainRequest = "";
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            PackageInfo pInfo = SplashActivity.this.getPackageManager().getPackageInfo(getPackageName(), 0);

            CheckAppVersionRequestBean checkAppVersionRequestBean = new CheckAppVersionRequestBean();
            checkAppVersionRequestBean.set_Ts(String.valueOf(CommonUtil.getTimeInMilliSec()));
            checkAppVersionRequestBean.set_Ver(String.valueOf(pInfo.versionCode));
            checkAppVersionRequestBean.set_Mtype("android");
            checkAppVersionRequestBean.set_c(QwikcilverAPI.getC());

            strPlainRequest = objectMapper.writeValueAsString(checkAppVersionRequestBean);
        } catch (PackageManager.NameNotFoundException | JsonProcessingException e) {
            e.printStackTrace();
        }
        Log.d(PLAIN_REQUEST, strPlainRequest);
        String strEncrypted = CommonUtil.getEncryptedData(strPlainRequest);
        Log.d(ENCRYPTED_REQUEST, strEncrypted);
        BaseRequestBean baseRequestBean = new BaseRequestBean();
        baseRequestBean.setUsr_data(strEncrypted);

        if (CommonUtil.isInternetAvailable(SplashActivity.this)) {
            compositeDisposable
                    .add(viewModel.getMobileVersion(baseRequestBean, new Consumer<CheckAppVersionResponseBean>() {
                        @Override
                        public void accept(CheckAppVersionResponseBean baseResponseBean) throws Exception {
                            /*
                             * if (baseResponseBean.getMyData().equalsIgnoreCase("false")) {
                             * AppUpdateDialog appUpdateDialog = new AppUpdateDialog();
                             * appUpdateDialog.show(getSupportFragmentManager(), "AppUpdateDialog");
                             * } else {
                             * jump();
                             * }
                             */
                            jump();
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            CommonUtil.showLongToast(SplashActivity.this, throwable.getMessage());
                            finish();
                        }
                    }));
        } else {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    finish();
                }
            }, 2500);
            CommonUtil.showLongToast(SplashActivity.this,
                    SplashActivity.this.getResources().getString(R.string.no_network_err));
        }
    }

    private void startLogin() {
        Intent intent = new Intent(SplashActivity.this, LoginActivityNew.class);
        startActivity(intent);
        finish();
    }

    private void goToDashboard(LoginResponseBean bean) {
        QwikcilverSharedPref.setStringValue(sharedPreferences, USER_TOKEN, bean.getUsr_token());
        Intent intentDashboard = new Intent(SplashActivity.this, DashboardActivityNew.class);
        startActivity(intentDashboard);
        finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (null != compositeDisposable)
            compositeDisposable.clear();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != compositeDisposable)
            compositeDisposable.dispose();
    }
}