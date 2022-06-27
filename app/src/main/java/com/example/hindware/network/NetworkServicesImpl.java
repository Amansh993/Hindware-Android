package com.example.hindware.network;

import com.example.hindware.model.BaseRequestBean;
import com.example.hindware.model.ChangePhotoRequestBean;
import com.example.hindware.model.CheckAppVersionResponseBean;
import com.example.hindware.model.CheckQRResponseBean;
import com.example.hindware.model.GetIDTypeResponseBean;
import com.example.hindware.model.GetLanguageResponseBean;
import com.example.hindware.model.GetNotificationResponseBean;
import com.example.hindware.model.LoginResponseBean;
import com.example.hindware.model.MenuListRequestBean;
import com.example.hindware.model.MenuListResponseBean;
import com.example.hindware.model.ValidateOTPRequestBean;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by SandeepY on 08122020
 **/

public class NetworkServicesImpl {

    private NetworkServices networkServices;

    public NetworkServicesImpl() {
        networkServices = RestClient.getClient().create(NetworkServices.class);
    }

    public Observable<LoginResponseBean> sendOTP(BaseRequestBean requestBean) {
        return networkServices.sendOTP(requestBean)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<LoginResponseBean> verifyOTP(BaseRequestBean requestBean) {
        return networkServices.verifyOTP(requestBean)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<LoginResponseBean> setLang(BaseRequestBean requestBean) {
        return networkServices.setLang(requestBean)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<MenuListResponseBean> getMenuList(BaseRequestBean requestBean) {
        return networkServices.getMenuList(requestBean)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<CheckQRResponseBean> validateQR(BaseRequestBean requestBean) {
        return networkServices.validateQR(requestBean)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<CheckQRResponseBean> checkQR(BaseRequestBean requestBean) {
        return networkServices.checkQR(requestBean)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<LoginResponseBean> validateToken(BaseRequestBean requestBean) {
        return networkServices.validateToken(requestBean)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<LoginResponseBean> logout(BaseRequestBean requestBean) {
        return networkServices.logout(requestBean)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<LoginResponseBean> forgotPassword(BaseRequestBean requestBean) {
        return networkServices.forgotPassword(requestBean)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<CheckAppVersionResponseBean> getMobileVersion(BaseRequestBean requestBean) {
        return networkServices.getMobileVersion(requestBean)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<GetLanguageResponseBean> getLanguage(BaseRequestBean requestBean) {
        return networkServices.getLanguage(requestBean)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<LoginResponseBean> changePhoto(ChangePhotoRequestBean requestBean) {
        return networkServices.changePhoto(requestBean)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<GetIDTypeResponseBean> getIDs(BaseRequestBean requestBean) {
        return networkServices.getIDTypeOf(requestBean)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<GetNotificationResponseBean> getNotification(BaseRequestBean requestBean) {
        return networkServices.getNotificationData(requestBean)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
