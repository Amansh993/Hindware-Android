package com.example.hindware.network;

import com.example.hindware.model.BaseRequestBean;
import com.example.hindware.model.ChangePhotoRequestBean;
import com.example.hindware.model.CheckAppVersionResponseBean;
import com.example.hindware.model.CheckQRResponseBean;
import com.example.hindware.model.GetIDTypeRequestBean;
import com.example.hindware.model.GetIDTypeResponseBean;
import com.example.hindware.model.GetLanguageResponseBean;
import com.example.hindware.model.GetNotificationResponseBean;
import com.example.hindware.model.LoginResponseBean;
import com.example.hindware.model.MenuListRequestBean;
import com.example.hindware.model.MenuListResponseBean;
import com.example.hindware.model.ValidateOTPRequestBean;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by SandeepY on 08122020
 **/

public interface NetworkServices {

    /*
     * @POST("reward.svc/authuserdatat")ValidateQRCodeData
     * Observable<LoginResponseBean> userLogin(@Body BaseRequestBean requestBean);
     */
    @POST("influencer.svc/sendotp")
    Observable<LoginResponseBean> sendOTP(@Body BaseRequestBean requestBean);

    @POST("influencer.svc/ValidateOTP")
    Observable<LoginResponseBean> verifyOTP(@Body BaseRequestBean requestBean);

    @POST("influencer.svc/SetLanguage")
    Observable<LoginResponseBean> setLang(@Body BaseRequestBean requestBean);

    @POST("influencer.svc/GetMenuDetailMulti")
    Observable<MenuListResponseBean> getMenuList(@Body BaseRequestBean requestBean);

    @POST("influencer.svc/ValidateQRCodeData")
    Observable<CheckQRResponseBean> validateQR(@Body BaseRequestBean requestBean);

    @POST("influencer.svc/CheckQRCodeData")
    Observable<CheckQRResponseBean> checkQR(@Body BaseRequestBean requestBean);

    @POST("influencer.svc/validatetokene")
    Observable<LoginResponseBean> validateToken(@Body BaseRequestBean requestBean);

    @POST("influencer.svc/logouttoken")
    Observable<LoginResponseBean> logout(@Body BaseRequestBean requestBean);

    @POST("influencer.svc/forgotpassword")
    Observable<LoginResponseBean> forgotPassword(@Body BaseRequestBean requestBean);

    @POST("influencer.svc/GetMobileVersion")
    Observable<CheckAppVersionResponseBean> getMobileVersion(@Body BaseRequestBean requestBean);

    @POST("influencer.svc/GetLanguage")
    Observable<GetLanguageResponseBean> getLanguage(@Body BaseRequestBean requestBean);

    @POST("influencer.svc/changephoto")
    Observable<LoginResponseBean> changePhoto(@Body ChangePhotoRequestBean requestBean);

    @POST("influencer.svc/GetIdType")
    Observable<GetIDTypeResponseBean> getIDTypeOf(@Body BaseRequestBean requestBean);

    @POST("influencer.svc/GetMessageData")
    Observable<GetNotificationResponseBean> getNotificationData(@Body BaseRequestBean requestBean);
}
