package com.example.hindware.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;

import androidx.databinding.DataBindingUtil;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.hindware.R;
import com.example.hindware.databinding.ActivityScanQRSummaryBinding;
import com.example.hindware.model.BaseRequestBean;
import com.example.hindware.model.CheckQRResponseBean;
import com.example.hindware.model.ValidateQRRequestBean;
import com.example.hindware.utility.CommonUtil;
import com.example.hindware.utility.QwikcilverSharedPref;
import com.example.hindware.utility.customprogress.CustomSpotsDialog;
import com.example.hindware.viewmodel.QwikcilverViewModel;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;

import static com.example.hindware.utility.Constants.ENCRYPTED_REQUEST;
import static com.example.hindware.utility.Constants.IS_RELOAD;
import static com.example.hindware.utility.Constants.PLAIN_REQUEST;
import static com.example.hindware.utility.Constants.USER_LANG;
import static com.example.hindware.utility.Constants.USER_POINT;
import static com.example.hindware.utility.Constants.USER_TOKEN;

public class ScanQRSummaryActivity extends BaseActivity {

    private ActivityScanQRSummaryBinding binding;
    private String strQRCode, strDecider;
    private QwikcilverViewModel viewModel;
    private CompositeDisposable compositeDisposable;
    private SharedPreferences sharedPreferences;
    private AlertDialog spotsDialog;
    AlertDialog.Builder alertBuilder;
    AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_scan_q_r_summary);
        initView();
        setListener();
    }

    private void initView() {
        Window window = this.getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            window.setStatusBarColor(Color.parseColor("#bdbdbd"));

        getSupportActionBar().setTitle(ScanQRSummaryActivity.this.getResources().getString(R.string.scan_details_text));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.white)));

        QwikcilverSharedPref sharedPref = new QwikcilverSharedPref();
        sharedPreferences = sharedPref.getQwikcilverSharedPref();
        viewModel = new QwikcilverViewModel();
        compositeDisposable = new CompositeDisposable();

        spotsDialog = new CustomSpotsDialog.Builder().setContext(this).setCancelable(true).setTheme(R.style.Custom)
                .build();

        if (null != getIntent()) {
            CheckQRResponseBean bundle = (CheckQRResponseBean) getIntent().getSerializableExtra("scan_details");
            // binding.tvQRCode.setText("QR Code: " + bundle.getQrc_code());
            binding.tvQRProdNameSummary.setText(bundle.getQrc_sku());
            binding.tvQRDescSummary.setText(bundle.getQrc_code());
            binding.tvQRPointsSummary.setText(bundle.getQrc_points());
            strQRCode = bundle.getQrc_code();
        }
    }

    private void setListener() {
        binding.btnQRSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                validateQR();
            }
        });
    }

    private void validateQR() {
        if (CommonUtil.isInternetAvailable(ScanQRSummaryActivity.this)) {
            String strPlainRequest = "";
            ObjectMapper objectMapper = new ObjectMapper();
            ValidateQRRequestBean validateQRRequestBean = new ValidateQRRequestBean();
            validateQRRequestBean.set_Tk(QwikcilverSharedPref.getStringValue(sharedPreferences, USER_TOKEN));
            validateQRRequestBean.set_Ts(String.valueOf(CommonUtil.getTimeInMilliSec()));
            validateQRRequestBean.set_Qd(strQRCode);
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
            compositeDisposable.add(viewModel.validateQR(requestBean, new Consumer<CheckQRResponseBean>() {
                @Override
                public void accept(CheckQRResponseBean loginResponseBean) throws Exception {
                    QwikcilverSharedPref.setStringValue(sharedPreferences, USER_POINT,
                            loginResponseBean.getQrc_balance());
                    QwikcilverSharedPref.setBooleanValue(sharedPreferences, IS_RELOAD, true);
                    spotsDialog.dismiss();
                    // CommonUtil.showShortToast(ScanQRSummaryActivity.this,
                    // ScanQRSummaryActivity.this.getResources().getString(R.string.claim_sucess_text));
                    // CommonUtil.showShortToast(ScanQRSummaryActivity.this,
                    // loginResponseBean.getMessage());
                    // finish();
                    alertBuilder = new AlertDialog.Builder(ScanQRSummaryActivity.this);
                    alertBuilder
                            .setTitle(ScanQRSummaryActivity.this.getResources().getString(R.string.claim_sucess_text))
                            .setMessage(loginResponseBean.getMessage())
                            .setCancelable(false)
                            .setIcon(android.R.drawable.ic_dialog_info)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            });
                    alertDialog = alertBuilder.create();
                    alertDialog.show();
                }
            }, new Consumer<Throwable>() {
                @Override
                public void accept(Throwable throwable) throws Exception {
                    spotsDialog.dismiss();
                    CommonUtil.showShortToast(ScanQRSummaryActivity.this, throwable.getMessage());
                }
            }));
        } else {
            CommonUtil.showShortToast(ScanQRSummaryActivity.this,
                    ScanQRSummaryActivity.this.getResources().getString(R.string.no_network_err));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
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