package com.example.hindware.viewmodel;

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
import com.example.hindware.network.NetworkServicesImpl;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by SandeepY on 08122020
 **/

public class QwikcilverViewModel {

    private Disposable disposable;

    private NetworkServicesImpl services;

    public QwikcilverViewModel() {
        services = new NetworkServicesImpl();
    }

    public Disposable sendOTP(BaseRequestBean requestBean, final Consumer<LoginResponseBean> response,
            final Consumer<Throwable> error) {
        disposable = services.sendOTP(requestBean)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<LoginResponseBean>() {
                    @Override
                    public void accept(LoginResponseBean loginResponseBean) throws Exception {
                        if (!loginResponseBean.getResponse_code().equalsIgnoreCase("00")) {
                            error.accept(new Throwable(loginResponseBean.getMessage()));
                            return;
                        }
                        response.accept(loginResponseBean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        error.accept(throwable);
                    }
                });
        return disposable;
    }

    public Disposable verifyOTP(BaseRequestBean requestBean, final Consumer<LoginResponseBean> response,
            final Consumer<Throwable> error) {
        disposable = services.verifyOTP(requestBean)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<LoginResponseBean>() {
                    @Override
                    public void accept(LoginResponseBean loginResponseBean) throws Exception {
                        if (!loginResponseBean.getResponse_code().equalsIgnoreCase("00")) {
                            error.accept(new Throwable(loginResponseBean.getMessage()));
                            return;
                        }
                        response.accept(loginResponseBean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        error.accept(throwable);
                    }
                });
        return disposable;
    }

    public Disposable setLang(BaseRequestBean requestBean, final Consumer<LoginResponseBean> response,
            final Consumer<Throwable> error) {
        disposable = services.setLang(requestBean)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<LoginResponseBean>() {
                    @Override
                    public void accept(LoginResponseBean loginResponseBean) throws Exception {
                        if (!loginResponseBean.getResponse_code().equalsIgnoreCase("00")) {
                            error.accept(new Throwable(loginResponseBean.getMessage()));
                            return;
                        }
                        response.accept(loginResponseBean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        error.accept(throwable);
                    }
                });
        return disposable;
    }

    public Disposable getMenuList(BaseRequestBean requestBean, final Consumer<MenuListResponseBean> response,
            final Consumer<Throwable> error) {
        disposable = services.getMenuList(requestBean)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<MenuListResponseBean>() {
                    @Override
                    public void accept(MenuListResponseBean loginResponseBean) throws Exception {
                        if (!loginResponseBean.getResponse_code().equalsIgnoreCase("00")) {
                            error.accept(new Throwable(loginResponseBean.getMessage()));
                            return;
                        }
                        response.accept(loginResponseBean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        error.accept(throwable);
                    }
                });
        return disposable;
    }

    public Disposable validateQR(BaseRequestBean requestBean, final Consumer<CheckQRResponseBean> response,
            final Consumer<Throwable> error) {
        disposable = services.validateQR(requestBean)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<CheckQRResponseBean>() {
                    @Override
                    public void accept(CheckQRResponseBean loginResponseBean) throws Exception {
                        if (!loginResponseBean.getResponse_code().equalsIgnoreCase("00")) {
                            error.accept(new Throwable(loginResponseBean.getMessage()));
                            return;
                        }
                        response.accept(loginResponseBean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        error.accept(throwable);
                    }
                });
        return disposable;
    }

    public Disposable checkQR(BaseRequestBean requestBean, final Consumer<CheckQRResponseBean> response,
            final Consumer<Throwable> error) {
        disposable = services.checkQR(requestBean)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<CheckQRResponseBean>() {
                    @Override
                    public void accept(CheckQRResponseBean loginResponseBean) throws Exception {
                        if (!loginResponseBean.getResponse_code().equalsIgnoreCase("00")) {
                            error.accept(new Throwable(loginResponseBean.getMessage()));
                            return;
                        }
                        response.accept(loginResponseBean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        error.accept(throwable);
                    }
                });
        return disposable;
    }

    public Disposable validateToken(BaseRequestBean requestBean, final Consumer<LoginResponseBean> response,
            final Consumer<Throwable> error) {
        disposable = services.validateToken(requestBean)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<LoginResponseBean>() {
                    @Override
                    public void accept(LoginResponseBean loginResponseBean) throws Exception {
                        if (!loginResponseBean.getResponse_code().equalsIgnoreCase("00")) {
                            error.accept(new Throwable(loginResponseBean.getMessage()));
                            return;
                        }
                        response.accept(loginResponseBean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        error.accept(throwable);
                    }
                });
        return disposable;
    }

    public Disposable logout(BaseRequestBean requestBean, final Consumer<LoginResponseBean> response,
            final Consumer<Throwable> error) {
        disposable = services.logout(requestBean)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<LoginResponseBean>() {
                    @Override
                    public void accept(LoginResponseBean loginResponseBean) throws Exception {
                        if (!loginResponseBean.getResponse_code().equalsIgnoreCase("00")) {
                            if (loginResponseBean.getResponse_code().equalsIgnoreCase("33")) {
                                response.accept(loginResponseBean);
                            } else {
                                error.accept(new Throwable(loginResponseBean.getMessage()));
                            }
                        } else if (loginResponseBean.getResponse_code().equalsIgnoreCase("00")) {
                            response.accept(loginResponseBean);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        error.accept(throwable);
                    }
                });
        return disposable;
    }

    public Disposable forgotPassword(BaseRequestBean requestBean, final Consumer<LoginResponseBean> response,
            final Consumer<Throwable> error) {
        disposable = services.forgotPassword(requestBean)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<LoginResponseBean>() {
                    @Override
                    public void accept(LoginResponseBean loginResponseBean) throws Exception {
                        if (!loginResponseBean.getResponse_code().equalsIgnoreCase("00")) {
                            error.accept(new Throwable(loginResponseBean.getMessage()));
                            return;
                        }
                        response.accept(loginResponseBean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        error.accept(throwable);
                    }
                });
        return disposable;
    }

    public Disposable getMobileVersion(BaseRequestBean requestBean,
            final Consumer<CheckAppVersionResponseBean> response, final Consumer<Throwable> error) {
        disposable = services.getMobileVersion(requestBean)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<CheckAppVersionResponseBean>() {
                    @Override
                    public void accept(CheckAppVersionResponseBean defaultResponseBean) throws Exception {
                        if (!defaultResponseBean.getResponse_code().equalsIgnoreCase("00")) {
                            error.accept(new Throwable(defaultResponseBean.getMessage()));
                            return;
                        }
                        response.accept(defaultResponseBean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        error.accept(throwable);
                    }
                });
        return disposable;
    }

    public Disposable getLanguage(BaseRequestBean requestBean, final Consumer<GetLanguageResponseBean> response,
            final Consumer<Throwable> error) {
        disposable = services.getLanguage(requestBean)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<GetLanguageResponseBean>() {
                    @Override
                    public void accept(GetLanguageResponseBean loginResponseBean) throws Exception {
                        if (!loginResponseBean.getResponseCode().equalsIgnoreCase("00")) {
                            error.accept(new Throwable(loginResponseBean.getMessage()));
                            return;
                        }
                        response.accept(loginResponseBean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        error.accept(throwable);
                    }
                });
        return disposable;
    }

    public Disposable changePhoto(ChangePhotoRequestBean requestBean, final Consumer<LoginResponseBean> response,
            final Consumer<Throwable> error) {
        disposable = services.changePhoto(requestBean)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<LoginResponseBean>() {
                    @Override
                    public void accept(LoginResponseBean loginResponseBean) throws Exception {
                        if (!loginResponseBean.getResponse_code().equalsIgnoreCase("00")) {
                            error.accept(new Throwable(loginResponseBean.getMessage()));
                            return;
                        }
                        response.accept(loginResponseBean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        error.accept(throwable);
                    }
                });
        return disposable;
    }

    public Disposable getidT(BaseRequestBean requestBean, final Consumer<GetIDTypeResponseBean> response,
                                  final Consumer<Throwable> error) {
        disposable = services.getIDs(requestBean)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<GetIDTypeResponseBean>() {
                    @Override
                    public void accept(GetIDTypeResponseBean getIDTypeResponseBean) throws Exception {
                        if (!getIDTypeResponseBean.getResponseCode().equalsIgnoreCase("00")) {
                            error.accept(new Throwable(getIDTypeResponseBean.getMessage()));
                            return;
                        }
                        response.accept(getIDTypeResponseBean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        error.accept(throwable);
                    }
                });
        return disposable;
    }


    public Disposable getNotification(BaseRequestBean requestBean, final Consumer<GetNotificationResponseBean> response,
                             final Consumer<Throwable> error) {
        disposable = services.getNotification(requestBean)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<GetNotificationResponseBean>() {
                    @Override
                    public void accept(GetNotificationResponseBean getNotificationResponseBean) throws Exception {
                        if (!getNotificationResponseBean.getResponseCode().equalsIgnoreCase("00")) {
                            error.accept(new Throwable(getNotificationResponseBean.getMessage()));
                            return;
                        }
                        response.accept(getNotificationResponseBean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        error.accept(throwable);
                    }
                });
        return disposable;
    }

}
