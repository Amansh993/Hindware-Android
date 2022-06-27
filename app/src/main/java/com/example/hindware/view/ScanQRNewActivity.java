package com.example.hindware.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.zxing.Result;
import com.example.hindware.R;
import com.example.hindware.databinding.ActivityScanQRNewBinding;
import com.example.hindware.model.BaseRequestBean;
import com.example.hindware.model.CheckQRResponseBean;
import com.example.hindware.model.ValidateQRRequestBean;
import com.example.hindware.utility.CommonUtil;
import com.example.hindware.utility.QwikcilverSharedPref;
import com.example.hindware.utility.customprogress.CustomSpotsDialog;
import com.example.hindware.viewmodel.QwikcilverViewModel;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

import static com.example.hindware.utility.Constants.CONTROLLER;
import static com.example.hindware.utility.Constants.ENCRYPTED_REQUEST;
import static com.example.hindware.utility.Constants.PLAIN_REQUEST;
import static com.example.hindware.utility.Constants.TITLE;
import static com.example.hindware.utility.Constants.USER_TOKEN;

public class ScanQRNewActivity extends BaseActivity
        implements ZXingScannerView.ResultHandler, TransactionDialog.MyDialogCloseListener {

    private ZXingScannerView mScannerView;
    private ActivityScanQRNewBinding binding;
    private static final String TAG = "ScanQRNewActivity";
    private QwikcilverViewModel viewModel;
    private CompositeDisposable compositeDisposable;
    private SharedPreferences sharedPreferences;
    private AlertDialog spotsDialog;
    private String strTitle, strController;
    AlertDialog.Builder alertBuilder;
    AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_scan_q_r_new);
        initView();
        setListener();
    }

    private void initView() {
        /*
         * mScannerView = new ZXingScannerView(this){
         * 
         * @Override
         * protected IViewFinder createViewFinderView(Context context) {
         * return new QRCodeReaderViewFinderView(context);
         * }
         * };
         */
        if (null != getIntent().getExtras()) {
            Bundle bundle = getIntent().getExtras();
            strTitle = bundle.getString(TITLE);
            strController = bundle.getString(CONTROLLER);
        }

        Window window = this.getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            window.setStatusBarColor(Color.parseColor("#bdbdbd"));

        getSupportActionBar().setTitle(strTitle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.white)));

        QwikcilverSharedPref sharedPref = new QwikcilverSharedPref();
        sharedPreferences = sharedPref.getQwikcilverSharedPref();
        viewModel = new QwikcilverViewModel();
        compositeDisposable = new CompositeDisposable();

        spotsDialog = new CustomSpotsDialog.Builder().setContext(this).setCancelable(true).setTheme(R.style.Custom)
                .build();
        mScannerView = new ZXingScannerView(this);
        mScannerView.setBorderColor(Color.parseColor("#FFFFFF"));
        binding.contentFrame.addView(mScannerView);
    }

    private void setListener() {

        binding.edtQRCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (binding.edtQRCode.getText().toString().length() > 0) {
                    binding.btnQRSubmit.setVisibility(View.VISIBLE);
                } else {
                    binding.btnQRSubmit.setVisibility(View.GONE);
                }
            }
        });

        binding.btnQRSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(binding.edtQRCode.getText().toString())) {
                    CommonUtil.showShortToast(ScanQRNewActivity.this,
                            ScanQRNewActivity.this.getResources().getString(R.string.enter_qr_code_alert));
                } else {
                    checkQRCode(binding.edtQRCode.getText().toString());
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
        if (alertDialog != null)
            if (alertDialog.isShowing())
                alertDialog.dismiss();
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }

    @Override
    public void handleResult(Result rawResult) {
        Log.d(TAG, "handleResult: " + rawResult.getText());
        // mScannerView.resumeCameraPreview(this);
        checkQRCode(rawResult.getText());
    }

    private void checkQRCode(String qrResult) {
        if (CommonUtil.isInternetAvailable(ScanQRNewActivity.this)) {
            String strPlainRequest = "";
            ObjectMapper objectMapper = new ObjectMapper();
            ValidateQRRequestBean validateQRRequestBean = new ValidateQRRequestBean();
            validateQRRequestBean.set_Tk(QwikcilverSharedPref.getStringValue(sharedPreferences, USER_TOKEN));
            validateQRRequestBean.set_Ts(String.valueOf(CommonUtil.getTimeInMilliSec()));
            validateQRRequestBean.set_Qd(qrResult);
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
            compositeDisposable.add(viewModel.checkQR(requestBean, new Consumer<CheckQRResponseBean>() {
                @Override
                public void accept(CheckQRResponseBean responseBean) throws Exception {
                    spotsDialog.dismiss();
                    if (strController.equalsIgnoreCase(
                            ScanQRNewActivity.this.getResources().getString(R.string.claim_upi_controller))) {
                        Intent intentQRSummary = new Intent(ScanQRNewActivity.this, ScanQRSummaryActivity.class);
                        intentQRSummary.putExtra("scan_details", responseBean);
                        startActivityForResult(intentQRSummary, 201);
                    } else if (strController.equalsIgnoreCase(
                            ScanQRNewActivity.this.getResources().getString(R.string.genuinity_check_controller))) {
                        TransactionDialog transactionDialog = new TransactionDialog(responseBean.getResponse_code(),
                                responseBean.getMessage(), responseBean.getQrc_code(), responseBean.getQrc_prdname(),
                                responseBean.getQrc_description(), responseBean.getQrc_points(), strTitle);
                        transactionDialog.show(getSupportFragmentManager(), "TransactionDialog");
                    } else {
                        CommonUtil.showShortToast(ScanQRNewActivity.this, "Controller name mismatched");
                        mScannerView.resumeCameraPreview(ScanQRNewActivity.this);
                    }
                }
            }, new Consumer<Throwable>() {
                @Override
                public void accept(Throwable throwable) throws Exception {
                    spotsDialog.dismiss();
                    alertBuilder = new AlertDialog.Builder(ScanQRNewActivity.this);
                    alertBuilder.setTitle("Error!")
                            .setMessage(throwable.getMessage())
                            .setCancelable(false)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    mScannerView.resumeCameraPreview(ScanQRNewActivity.this);
                                }
                            });
                    alertDialog = alertBuilder.create();
                    alertDialog.show();
                }
            }));
        } else {
            CommonUtil.showShortToast(ScanQRNewActivity.this,
                    ScanQRNewActivity.this.getResources().getString(R.string.no_network_err));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 201) {
            binding.edtQRCode.setText("");
        }
    }

    private void validateQR(String strQRCode) {
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
        viewModel.validateQR(requestBean, new Consumer<CheckQRResponseBean>() {
            @Override
            public void accept(CheckQRResponseBean loginResponseBean) throws Exception {
                CommonUtil.showShortToast(ScanQRNewActivity.this, loginResponseBean.getMessage());
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                CommonUtil.showShortToast(ScanQRNewActivity.this, throwable.getMessage());
            }
        });
    }

    @Override
    public void handleDialogClose() {
        if (null != mScannerView)
            mScannerView.resumeCameraPreview(ScanQRNewActivity.this);
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