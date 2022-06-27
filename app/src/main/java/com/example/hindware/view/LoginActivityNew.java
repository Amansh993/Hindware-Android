package com.example.hindware.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.example.hindware.model.DropDownResponseList;
import com.example.hindware.model.GetIDTypeResponseBean;
import com.example.hindware.model.LanguageResponseList;
import com.example.hindware.model.LogoutRequestBean;
import com.example.hindware.model.SpinnerList;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.hindware.R;
import com.example.hindware.databinding.ActivityLoginNewBinding;
import com.example.hindware.model.BaseRequestBean;
import com.example.hindware.model.LoginResponseBean;
import com.example.hindware.model.SendOTPRequestBean;
import com.example.hindware.model.ValidateOTPRequestBean;
import com.example.hindware.utility.CommonUtil;
import com.example.hindware.utility.QwikcilverAPI;
import com.example.hindware.utility.QwikcilverSharedPref;
import com.example.hindware.utility.customprogress.CustomSpotsDialog;
import com.example.hindware.viewmodel.QwikcilverViewModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;

import static com.example.hindware.utility.Constants.FCM_TOKEN;
import static com.example.hindware.utility.Constants.FROM_LANG_SCREEN;
import static com.example.hindware.utility.Constants.USER_LANG;
import static com.example.hindware.utility.Constants.USER_LANG_CODE;
import static com.example.hindware.utility.Constants.USER_TOKEN;

/**
 * Created by SandeepY on 05012021
 **/

public class LoginActivityNew extends BaseActivity implements View.OnClickListener {

    private ActivityLoginNewBinding binding;
    private QwikcilverViewModel viewModel;
    private AlertDialog spotsDialog;
    private SharedPreferences sharedPreferences;
    private String strFCMToken;
    private boolean fromLangScreen;
    private CompositeDisposable compositeDisposable;
    private String[] langArray = { "English", "हिंदी", "nಕನ್ನಡ", "తెలుగు", "தமிழ்", "ગુજરાતી", "ਪੰਜਾਬੀ" };
    List<DropDownResponseList> spinnerL;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login_new);
        initView();
        callToGetIDTYpe();
        setListener();



        binding.enrollCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("Eroll", "Customer");

                Intent intentRegister = new Intent(LoginActivityNew.this, RegisterActivity.class);
                intentRegister.putExtra("spinner",(Serializable) spinnerL);
                startActivity(intentRegister);


            }
        });


    }

    private void initView() {
        Window window = this.getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            window.setStatusBarColor(Color.parseColor("#1B1B1A"));

        viewModel = new QwikcilverViewModel();
        spotsDialog = new CustomSpotsDialog.Builder().setContext(this).setCancelable(false).setTheme(R.style.Custom)
                .build();
        QwikcilverSharedPref qwikcilverSharedPreference = new QwikcilverSharedPref();
        sharedPreferences = qwikcilverSharedPreference.getQwikcilverSharedPref();
        compositeDisposable = new CompositeDisposable();

        Bundle bundle = getIntent().getExtras();
        if (null != bundle) {
            fromLangScreen = bundle.getBoolean(FROM_LANG_SCREEN);
        }
        if (fromLangScreen) {
            if (!TextUtils.isEmpty(QwikcilverSharedPref.getStringValue(sharedPreferences, USER_LANG)))
                binding.tvLanguage.setText(QwikcilverSharedPref.getStringValue(sharedPreferences, USER_LANG));
            else {
                binding.tvLanguage.setText("English");
                QwikcilverSharedPref.setStringValue(sharedPreferences, USER_LANG, "English");
                QwikcilverSharedPref.setStringValue(sharedPreferences, USER_LANG_CODE, "en-US");
            }
        } else {
            Locale locale = new Locale("en-US");
            locale.setDefault(locale);
            Configuration configuration = new Configuration();
            configuration.locale = locale;
            getBaseContext().getResources().updateConfiguration(configuration,
                    getBaseContext().getResources().getDisplayMetrics());

            binding.tvLanguage.setText("English");
            QwikcilverSharedPref.setStringValue(sharedPreferences, USER_LANG, "English");
            QwikcilverSharedPref.setStringValue(sharedPreferences, USER_LANG_CODE, "en-US");
        }
        binding.editTextMobile.setHint(LoginActivityNew.this.getResources().getString(R.string.mobile_text));
        binding.editTextOTP.setHint(LoginActivityNew.this.getResources().getString(R.string.otp_text));
        binding.buttonGetOtpSubmit.setText(LoginActivityNew.this.getResources().getString(R.string.get_otp));
    }

    private void setListener() {
        binding.tvLanguage.setOnClickListener(this);
        binding.ivLanguage.setOnClickListener(this);
        binding.buttonGetOtpSubmit.setOnClickListener(this);

    }





    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tvLanguage) {
            Intent intentTvLang = new Intent(LoginActivityNew.this, LanguageActivity.class);
            startActivity(intentTvLang);
            finish();
        } else if (v.getId() == R.id.ivLanguage) {
            Intent intentTvLang = new Intent(LoginActivityNew.this, LanguageActivity.class);
            startActivity(intentTvLang);
            finish();
        } else if (v.getId() == R.id.buttonGetOtpSubmit) {
            if (binding.buttonGetOtpSubmit.getText().toString()
                    .equalsIgnoreCase(LoginActivityNew.this.getResources().getString(R.string.get_otp))) {
                sendOTP();
            } else {
                verifyOTP();
            }
        }
    }


    private void sendOTP() {
        String strUsername = binding.editTextMobile.getText().toString();
        if (TextUtils.isEmpty(strUsername)) {
            binding.editTextMobile.setError(LoginActivityNew.this.getResources().getString(R.string.alert_mobile));
            // CommonUtil.showShortToast(LoginActivityNew.this,
            // LoginActivityNew.this.getResources().getString(R.string.alert_mobile));
        } else {
            processSendOTP(strUsername);
        }
    }

    private void verifyOTP() {
        String strUsername = binding.editTextMobile.getText().toString();
        String strOTP = binding.editTextOTP.getText().toString();
        if (TextUtils.isEmpty(strUsername)) {
            binding.editTextMobile.setError(LoginActivityNew.this.getResources().getString(R.string.alert_mobile));
            // CommonUtil.showShortToast(getApplicationContext(),
            // LoginActivityNew.this.getResources().getString(R.string.alert_mobile));
        } else if (TextUtils.isEmpty(strOTP)) {
            binding.editTextOTP.setError(LoginActivityNew.this.getResources().getString(R.string.alert_otp));
            // CommonUtil.showShortToast(LoginActivityNew.this,
            // LoginActivityNew.this.getResources().getString(R.string.alert_otp));
        } else {
            processVerifyOTP(strUsername, strOTP);
        }
    }

    private void callToGetIDTYpe() {
        if (CommonUtil.isInternetAvailable(LoginActivityNew.this)) {
            String strPlainRequest = "";
            ObjectMapper objectMapper = new ObjectMapper();
            LogoutRequestBean logoutRequestBean = new LogoutRequestBean();
            logoutRequestBean.set_tk("123456789");
            logoutRequestBean.set_ts(String.valueOf(CommonUtil.getTimeInMilliSec()));
            try {
                strPlainRequest = objectMapper.writeValueAsString(logoutRequestBean);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            String strEncrypted = CommonUtil.getEncryptedData(strPlainRequest);
            Log.d("EncryptedGetIDType", strEncrypted);
            BaseRequestBean requestBean = new BaseRequestBean();
            requestBean.setUsr_data(strEncrypted);

            compositeDisposable.add(viewModel.getidT(requestBean, new Consumer<GetIDTypeResponseBean>() {
                @Override
                public void accept(GetIDTypeResponseBean loginResponseBean) throws Exception {
                    if (loginResponseBean.getResponseCode().equalsIgnoreCase("33")) {

                        if (null != spotsDialog)
                            spotsDialog.dismiss();
                        CommonUtil.showShortToast(LoginActivityNew.this, loginResponseBean.getMessage());


                    } else if (loginResponseBean.getResponseCode().equalsIgnoreCase("00")) {
//                        if (null != spotsDialog)
//                            spotsDialog.dismiss();

                        Log.e("ResponseList",loginResponseBean.getResponseCode());

                        CommonUtil.showShortToast(LoginActivityNew.this, loginResponseBean.getMessage());
                        Log.e("loginResponseBean", loginResponseBean.getMessage());
                    }
                }
            }, new Consumer<Throwable>() {
                @Override
                public void accept(Throwable throwable) throws Exception {
                    if (null != spotsDialog)
                        spotsDialog.dismiss();
                    CommonUtil.showShortToast(LoginActivityNew.this, throwable.getMessage());
                    Log.e("Error OF Drop", throwable.getMessage());
                }
            }));
        } else {
            CommonUtil.showShortToast(LoginActivityNew.this,
                    LoginActivityNew.this.getResources().getString(R.string.no_network_err));
        }
    }

    private void processSendOTP(String username) {
        if (CommonUtil.isInternetAvailable(LoginActivityNew.this)) {
            strFCMToken = QwikcilverSharedPref.getStringValue(sharedPreferences, FCM_TOKEN);
            Log.e("TokenAtLogin", strFCMToken);
            SendOTPRequestBean requestBean = new SendOTPRequestBean();
            String strPlainRequest = "";
            ObjectMapper objectMapper = new ObjectMapper();

            requestBean.set_M(username);
            requestBean.set_C(QwikcilverAPI.getC());
            try {
                strPlainRequest = objectMapper.writeValueAsString(requestBean);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            Log.d("PlainLogSendOTP", strPlainRequest);
            String strEncrypted = CommonUtil.getEncryptedData(strPlainRequest);
            Log.d("EncryptedLogSendOTP", strEncrypted);
            BaseRequestBean loginRequestBean = new BaseRequestBean();
            loginRequestBean.setUsr_data(strEncrypted);

            spotsDialog.show();
            compositeDisposable.add(viewModel.sendOTP(loginRequestBean, new Consumer<LoginResponseBean>() {
                @Override
                public void accept(LoginResponseBean loginResponseBean) throws Exception {
                    spotsDialog.hide();
                    binding.editTextOTP.setVisibility(View.VISIBLE);
                    binding.editTextOTP.requestFocus();
                    binding.buttonGetOtpSubmit
                            .setText(LoginActivityNew.this.getResources().getString(R.string.submit_text));
                }
            }, new Consumer<Throwable>() {
                @Override
                public void accept(Throwable throwable) throws Exception {
                    CommonUtil.showShortToast(LoginActivityNew.this, throwable.getMessage());
                    spotsDialog.hide();
                    binding.editTextOTP.setVisibility(View.GONE);
                }
            }));
        } else {
            CommonUtil.showShortToast(LoginActivityNew.this,
                    LoginActivityNew.this.getResources().getString(R.string.no_network_err));
        }
    }

    private void processVerifyOTP(String username, String otp) {
        if (CommonUtil.isInternetAvailable(LoginActivityNew.this)) {
            ValidateOTPRequestBean requestBean = new ValidateOTPRequestBean();
            String strPlainRequest = "";
            ObjectMapper objectMapper = new ObjectMapper();

            requestBean.set_M(username);
            requestBean.set_C(QwikcilverAPI.getC());
            requestBean.set_Otp(otp);
            requestBean.set_Mtk("123456789");
            requestBean.set_Mtype("android");
            try {
                strPlainRequest = objectMapper.writeValueAsString(requestBean);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            Log.d("PlainLogVerifyOTP", strPlainRequest);
            String strEncrypted = CommonUtil.getEncryptedData(strPlainRequest);
            Log.d("EncryptedLogVerifyOTP", strEncrypted);
            BaseRequestBean loginRequestBean = new BaseRequestBean();
            loginRequestBean.setUsr_data(strEncrypted);

            spotsDialog.show();
            compositeDisposable.add(viewModel.verifyOTP(loginRequestBean, new Consumer<LoginResponseBean>() {
                @Override
                public void accept(LoginResponseBean loginResponseBean) throws Exception {
                    spotsDialog.hide();
                    goToDashboard(loginResponseBean);
                }
            }, new Consumer<Throwable>() {
                @Override
                public void accept(Throwable throwable) throws Exception {
                    spotsDialog.hide();
                    CommonUtil.showShortToast(LoginActivityNew.this, throwable.getMessage());
                }
            }));

        } else {
            CommonUtil.showShortToast(LoginActivityNew.this,
                    LoginActivityNew.this.getResources().getString(R.string.no_network_err));
        }
    }

    private void goToDashboard(LoginResponseBean bean) {
        QwikcilverSharedPref qwikcilverSharedPreference = new QwikcilverSharedPref();
        SharedPreferences sharedPreferences = qwikcilverSharedPreference.getQwikcilverSharedPref();
        QwikcilverSharedPref.setStringValue(sharedPreferences, USER_TOKEN, bean.getUsr_token());

        Intent intentDashboard = new Intent(LoginActivityNew.this, DashboardActivityNew.class);
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
