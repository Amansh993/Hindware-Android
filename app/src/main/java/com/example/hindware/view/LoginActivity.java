package com.example.hindware.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.hindware.R;
import com.example.hindware.databinding.ActivityLoginBinding;
import com.example.hindware.model.BaseRequestBean;
import com.example.hindware.model.LoginResponseBean;
import com.example.hindware.model.SendOTPRequestBean;
import com.example.hindware.model.ValidateOTPRequestBean;
import com.example.hindware.utility.CommonUtil;
import com.example.hindware.utility.QwikcilverAPI;
import com.example.hindware.utility.QwikcilverSharedPref;
import com.example.hindware.utility.customprogress.CustomSpotsDialog;
import com.example.hindware.viewmodel.QwikcilverViewModel;

import java.util.Locale;

import io.reactivex.functions.Consumer;

import static com.example.hindware.utility.Constants.FCM_TOKEN;
import static com.example.hindware.utility.Constants.USER_LANG;
import static com.example.hindware.utility.Constants.USER_LANG_CODE;
import static com.example.hindware.utility.Constants.USER_TOKEN;

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private ActivityLoginBinding binding;
    private QwikcilverViewModel viewModel;
    private AlertDialog spotsDialog;
    private SharedPreferences sharedPreferences;
    private String strFCMToken;
    private String[] langArray = { "English", "हिंदी", "nಕನ್ನಡ", "తెలుగు", "தமிழ்", "ગુજરાતી", "ਪੰਜਾਬੀ" };
    private boolean isResendTrue;
    private boolean isTimerDone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        initView();
        setListener();
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

        if (!TextUtils.isEmpty(QwikcilverSharedPref.getStringValue(sharedPreferences, USER_LANG)))
            binding.tvLangText.setText(QwikcilverSharedPref.getStringValue(sharedPreferences, USER_LANG));
        else {
            binding.tvLangText.setText("en");
            QwikcilverSharedPref.setStringValue(sharedPreferences, USER_LANG, "en");
            QwikcilverSharedPref.setStringValue(sharedPreferences, USER_LANG_CODE, "en-US");
        }
    }

    private void setLangCode(String lang) {
        binding.tvLangText.setText(lang);
    }

    private void setListener() {
        binding.rlLang.setOnClickListener(this);
        binding.btnSendVerifyOTP.setOnClickListener(this);
        binding.edtOTP.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (isResendTrue) {
                    if (binding.edtOTP.getText().toString().length() > 0) {
                        binding.btnSendVerifyOTP.setEnabled(true);
                        binding.btnSendVerifyOTP
                                .setText(LoginActivity.this.getResources().getString(R.string.verify_otp));
                        binding.btnSendVerifyOTP.setBackgroundColor(Color.parseColor("#FF0000"));
                        binding.btnSendVerifyOTP.setTextColor(Color.parseColor("#FFFFFF"));
                    } else {
                        if (isTimerDone) {
                            binding.btnSendVerifyOTP.setEnabled(true);
                            binding.btnSendVerifyOTP
                                    .setText(LoginActivity.this.getResources().getString(R.string.resend_otp));
                            binding.btnSendVerifyOTP.setBackgroundColor(Color.parseColor("#FF0000"));
                            binding.btnSendVerifyOTP.setTextColor(Color.parseColor("#FFFFFF"));
                        }
                    }
                }
            }
        });
    }

    private void setLanguage(String lang) {
        Locale locale = new Locale(lang);
        locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.locale = locale;
        getBaseContext().getResources().updateConfiguration(configuration,
                getBaseContext().getResources().getDisplayMetrics());

        recreate();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rlLang:
                Intent intent = new Intent(LoginActivity.this, LanguageActivity.class);
                startActivity(intent);
                finish();
                break;

            case R.id.btnSendVerifyOTP:
                if (binding.btnSendVerifyOTP.getText().toString()
                        .equalsIgnoreCase(LoginActivity.this.getResources().getString(R.string.send_otp))
                        || binding.btnSendVerifyOTP.getText().toString()
                                .equalsIgnoreCase(LoginActivity.this.getResources().getString(R.string.resend_otp)))
                    sendOTP();
                else
                    verifyOTP();
                break;
        }
    }

    private void openLanguageChangeDialog() {
        androidx.appcompat.app.AlertDialog.Builder mBuilder = new androidx.appcompat.app.AlertDialog.Builder(
                LoginActivity.this);
        mBuilder.setTitle("Choose Language");
        mBuilder.setSingleChoiceItems(langArray, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (i == 0) {
                    setLanguage("en");
                    binding.tvLangText.setText("En");
                } else if (i == 1) {
                    setLanguage("hi");
                    binding.tvLangText.setText("Hi");
                } else if (i == 2) {
                    setLanguage("kn");
                    binding.tvLangText.setText("Kn");
                } else if (i == 3) {
                    setLanguage("te");
                    binding.tvLangText.setText("Te");
                } else if (i == 4) {
                    setLanguage("ta");
                    binding.tvLangText.setText("Ta");
                } else if (i == 5) {
                    setLanguage("gu");
                    binding.tvLangText.setText("Gu");
                }
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (!LoginActivity.this.isDestroyed()) {
                            dialogInterface.dismiss();
                        }
                    }
                }, 1000);
            }
        });
        androidx.appcompat.app.AlertDialog mDialog = mBuilder.create();
        mDialog.show();
    }

    private void sendOTP() {
        String strUsername = binding.edtMobileNumber.getText().toString();
        // String strPassword = binding.edtPassword.getText().toString();
        if (TextUtils.isEmpty(strUsername)) {
            Toast.makeText(this, LoginActivity.this.getResources().getString(R.string.alert_username),
                    Toast.LENGTH_SHORT).show();
        } /*
           * else if (TextUtils.isEmpty(strPassword)) {
           * Toast.makeText(this,
           * LoginActivity.this.getResources().getString(R.string.alert_password),
           * Toast.LENGTH_SHORT).show();
           * }
           */ else {
            processSendOTP(strUsername);
        }
    }

    private void verifyOTP() {
        String strUsername = binding.edtMobileNumber.getText().toString();
        String strOTP = binding.edtOTP.getText().toString();
        if (TextUtils.isEmpty(strUsername)) {
            Toast.makeText(this, LoginActivity.this.getResources().getString(R.string.alert_username),
                    Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(strOTP)) {
            Toast.makeText(this, LoginActivity.this.getResources().getString(R.string.otp_text), Toast.LENGTH_SHORT)
                    .show();
        } else {
            processVerifyOTP(strUsername, strOTP);
        }
    }

    private void processSendOTP(String username) {
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
        Log.d("PlainLogSendOTPs", strPlainRequest);
        String strEncrypted = CommonUtil.getEncryptedData(strPlainRequest);
        Log.d("EncryptedLogSendOTP", strEncrypted);
        BaseRequestBean loginRequestBean = new BaseRequestBean();
        loginRequestBean.setUsr_data(strEncrypted);

        spotsDialog.show();
        viewModel.sendOTP(loginRequestBean, new Consumer<LoginResponseBean>() {
            @Override
            public void accept(LoginResponseBean loginResponseBean) throws Exception {
                // Toast.makeText(LoginActivity.this, loginResponseBean.getMessage(),
                // Toast.LENGTH_SHORT).show();
                spotsDialog.hide();
                binding.edtOTP.setVisibility(View.VISIBLE);
                isTimerDone = false;

                new CountDownTimer(31000, 1000) {

                    public void onTick(long millisUntilFinished) {
                        isResendTrue = true;
                        if (binding.edtOTP.getText().toString().length() > 0) {
                            binding.btnSendVerifyOTP.setEnabled(true);
                            binding.btnSendVerifyOTP
                                    .setText(LoginActivity.this.getResources().getString(R.string.verify_otp));
                            binding.btnSendVerifyOTP.setBackgroundColor(Color.parseColor("#FF0000"));
                            binding.btnSendVerifyOTP.setTextColor(Color.parseColor("#FFFFFF"));
                        } else {
                            binding.btnSendVerifyOTP.setEnabled(false);
                            binding.btnSendVerifyOTP
                                    .setText("Resend OTP in " + millisUntilFinished / 1000 + " seconds");
                            binding.btnSendVerifyOTP.setBackgroundColor(Color.parseColor("#BEBDBC"));
                            binding.btnSendVerifyOTP.setTextColor(Color.parseColor("#000000"));
                            // here you can have your logic to set text to edittext
                        }
                    }

                    public void onFinish() {
                        isTimerDone = true;
                        binding.btnSendVerifyOTP.setEnabled(true);
                        if (binding.edtOTP.getText().toString().length() > 0)
                            binding.btnSendVerifyOTP
                                    .setText(LoginActivity.this.getResources().getString(R.string.verify_otp));
                        else
                            binding.btnSendVerifyOTP
                                    .setText(LoginActivity.this.getResources().getString(R.string.resend_otp));
                        binding.btnSendVerifyOTP.setBackgroundColor(Color.parseColor("#FF0000"));
                        binding.btnSendVerifyOTP.setTextColor(Color.parseColor("#FFFFFF"));
                    }
                }.start();
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                Toast.makeText(LoginActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
                spotsDialog.hide();
                binding.edtOTP.setVisibility(View.GONE);
            }
        });
    }

    private void processVerifyOTP(String username, String otp) {
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
        viewModel.verifyOTP(loginRequestBean, new Consumer<LoginResponseBean>() {
            @Override
            public void accept(LoginResponseBean loginResponseBean) throws Exception {
                spotsDialog.hide();
                // Toast.makeText(LoginActivity.this, loginResponseBean.getMessage(),
                // Toast.LENGTH_SHORT).show();
                goToDashboard(loginResponseBean);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                spotsDialog.hide();
                Toast.makeText(LoginActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void goToDashboard(LoginResponseBean bean) {
        QwikcilverSharedPref qwikcilverSharedPreference = new QwikcilverSharedPref();
        SharedPreferences sharedPreferences = qwikcilverSharedPreference.getQwikcilverSharedPref();
        QwikcilverSharedPref.setStringValue(sharedPreferences, USER_TOKEN, bean.getUsr_token());

        Intent intentDashboard = new Intent(LoginActivity.this, DashboardActivityNew.class);
        startActivity(intentDashboard);
        finish();
    }
}