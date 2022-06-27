package com.example.hindware.view;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.hindware.R;
import com.example.hindware.adapter.LanguageAdapter;
import com.example.hindware.databinding.ActivityLanguageBinding;
import com.example.hindware.model.BaseRequestBean;
import com.example.hindware.model.GetLanguageRequestBean;
import com.example.hindware.model.LanguageResponseList;
import com.example.hindware.utility.CommonUtil;
import com.example.hindware.utility.QwikcilverAPI;
import com.example.hindware.utility.QwikcilverSharedPref;
import com.example.hindware.utility.customprogress.CustomSpotsDialog;
import com.example.hindware.viewmodel.QwikcilverViewModel;

import java.util.ArrayList;
import java.util.Locale;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;

import static com.example.hindware.utility.Constants.FROM_LANG_SCREEN;
import static com.example.hindware.utility.Constants.USER_LANG;
import static com.example.hindware.utility.Constants.USER_LANG_CODE;

public class LanguageActivity extends BaseActivity {

    private ActivityLanguageBinding binding;
    private QwikcilverViewModel viewModel;
    private AlertDialog spotsDialog;
    private SharedPreferences sharedPreferences;
    private String[] langArray = { "English", "हिंदी", "nಕನ್ನಡ", "తెలుగు", "தமிழ்", "ગુજરાતી", "ਪੰਜਾਬੀ" };
    private String selectedLang = "en";
    private ArrayList<LanguageResponseList> languageResponseList;
    private LanguageAdapter languageAdapter;
    private CompositeDisposable compositeDisposable;
    private boolean fromDash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_language);
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
        compositeDisposable = new CompositeDisposable();
        setLanguageList();
        Bundle bundle = getIntent().getExtras();
        if (null != bundle) {
            fromDash = bundle.getBoolean("fromDash");
        }
        // getLanguage();
    }

    private void setLanguageList() {
        LanguageResponseList languageBeanEn = new LanguageResponseList();
        languageBeanEn.setlNG_CODE("en-US");
        languageBeanEn.setlNG_ENGLISHNAME("English");
        languageBeanEn.setlNG_NAME("English");

        LanguageResponseList languageBeanHi = new LanguageResponseList();
        languageBeanHi.setlNG_CODE("hi-IN");
        languageBeanHi.setlNG_ENGLISHNAME("Hindi");
        languageBeanHi.setlNG_NAME("हिंदी");

        LanguageResponseList languageBeanKn = new LanguageResponseList();
        languageBeanKn.setlNG_CODE("kn-IN");
        languageBeanKn.setlNG_ENGLISHNAME("Kannada");
        languageBeanKn.setlNG_NAME("nಕನ್ನಡ");

        LanguageResponseList languageBeanTe = new LanguageResponseList();
        languageBeanTe.setlNG_CODE("te-IN");
        languageBeanTe.setlNG_ENGLISHNAME("Telugu");
        languageBeanTe.setlNG_NAME("తెలుగు");

        LanguageResponseList languageBeanTa = new LanguageResponseList();
        languageBeanTa.setlNG_CODE("ta-IN");
        languageBeanTa.setlNG_ENGLISHNAME("Tamil");
        languageBeanTa.setlNG_NAME("தமிழ்");

        LanguageResponseList languageBeanGu = new LanguageResponseList();
        languageBeanGu.setlNG_CODE("gu-IN");
        languageBeanGu.setlNG_ENGLISHNAME("Gujarati");
        languageBeanGu.setlNG_NAME("ગુજરાતી");

        LanguageResponseList languageBeanPa = new LanguageResponseList();
        languageBeanPa.setlNG_CODE("pa-IN");
        languageBeanPa.setlNG_ENGLISHNAME("Punjabi");
        languageBeanPa.setlNG_NAME("ਪੰਜਾਬੀ");

        languageResponseList = new ArrayList<>();
        languageResponseList.add(languageBeanEn);
        languageResponseList.add(languageBeanHi);
        languageResponseList.add(languageBeanKn);
        languageResponseList.add(languageBeanTe);
        languageResponseList.add(languageBeanTa);
        languageResponseList.add(languageBeanGu);
        languageResponseList.add(languageBeanPa);

        languageAdapter = new LanguageAdapter(languageResponseList, LanguageActivity.this);
        binding.rvLanguage
                .setLayoutManager(new LinearLayoutManager(LanguageActivity.this, LinearLayoutManager.VERTICAL, false));
        binding.rvLanguage.setAdapter(languageAdapter);
    }

    private void setListener() {
        compositeDisposable.add(languageAdapter.getSelectLang().subscribe(new Consumer<LanguageResponseList>() {
            @Override
            public void accept(LanguageResponseList languageResponseList) throws Exception {
                changeLang(languageResponseList);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                CommonUtil.showShortToast(LanguageActivity.this, throwable.getMessage());
            }
        }));
    }

    private void changeLang(LanguageResponseList langBean) {

        String[] langArrayNew = langBean.getlNG_CODE().split("-");
        Locale locale = new Locale(langArrayNew[0]);
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.locale = locale;
        getBaseContext().getResources().updateConfiguration(configuration,
                getBaseContext().getResources().getDisplayMetrics());

        QwikcilverSharedPref.setStringValue(sharedPreferences, USER_LANG, langBean.getlNG_ENGLISHNAME());
        QwikcilverSharedPref.setStringValue(sharedPreferences, USER_LANG_CODE, langBean.getlNG_CODE());

        if (fromDash) {
            Intent intent = new Intent(LanguageActivity.this, DashboardActivityNew.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        } else {
            Intent intent = new Intent(LanguageActivity.this, LoginActivityNew.class);
            intent.putExtra(FROM_LANG_SCREEN, true);
            startActivity(intent);
            finish();
        }
    }

    private void getLanguage() {
        GetLanguageRequestBean getLanguageRequestBean = new GetLanguageRequestBean();
        String strPlainRequest = "";
        ObjectMapper objectMapper = new ObjectMapper();

        getLanguageRequestBean.set_c(QwikcilverAPI.getC());
        getLanguageRequestBean.set_Ts(String.valueOf(CommonUtil.getTimeInMilliSec()));

        try {
            strPlainRequest = objectMapper.writeValueAsString(getLanguageRequestBean);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        String strEncrypted = CommonUtil.getEncryptedData(strPlainRequest);
        Log.d("EncryptedLogin", strEncrypted);
        BaseRequestBean requestBean = new BaseRequestBean();
        requestBean.setUsr_data(strEncrypted);

        spotsDialog.show();

        /*
         * viewModel.getLanguage(requestBean, new Consumer<GetLanguageResponseBean>() {
         * 
         * @Override
         * public void accept(GetLanguageResponseBean getLanguageResponseBean) throws
         * Exception {
         * spotsDialog.hide();
         * if (getLanguageResponseBean.getLanguageResponseList().size() > 0) {
         * // set up spinner
         * } else {
         * binding.msLanguage.setItems(langArray);
         * }
         * }
         * }, new Consumer<Throwable>() {
         * 
         * @Override
         * public void accept(Throwable throwable) throws Exception {
         * Toast.makeText(LanguageActivity.this, throwable.getMessage(),
         * Toast.LENGTH_SHORT).show();
         * spotsDialog.hide();
         * }
         * });
         */
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(LanguageActivity.this, LoginActivityNew.class);
        startActivity(intent);
    }
}