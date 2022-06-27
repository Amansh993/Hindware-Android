package com.example.hindware.utility;

import android.app.Service;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hindware.R;

import org.apache.commons.codec.binary.Base64;

import java.math.BigInteger;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import io.reactivex.subjects.PublishSubject;

import static com.example.hindware.utility.Constants.PLAIN_REQUEST;

/**
 * Created by SandeepY on 07122020
 **/

public class CommonUtil {

    public static boolean canConnect(Context mContext) {
        boolean ret = false;
        ConnectivityManager connectMgr = (ConnectivityManager) mContext.getSystemService(Service.CONNECTIVITY_SERVICE);
        NetworkInfo networkinfo = connectMgr.getActiveNetworkInfo();

        if (networkinfo != null && networkinfo.isConnected() && networkinfo.getState() == NetworkInfo.State.CONNECTED)
            ret = true;
        else {
            CommonUtil.showShortToast(mContext, "Network not available");
            // LocalBroadcastManager.getInstance(mContext).sendBroadcast(new
            // Intent(NO_NETWORK_ACTION));
        }
        Log.v("component coreutils", "can connect : " + ret);

        return ret;
    }

    public static void showShortToast(Context context, String message) {
        if (context != null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = inflater.inflate(R.layout.toast_layout, null);

            TextView text = (TextView) layout.findViewById(R.id.text);
            text.setText(message);

            // Toast toast = new Toast(context);
            // toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
            Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
            // toast.setDuration(Toast.LENGTH_LONG);
            // toast.setGravity(Gravity.TOP,0,0);
            toast.setView(layout);
            toast.show();
            // https://stackoverflow.com/questions/31175601/how-can-i-change-default-toast-message-color-and-background-color-in-android
        }
    }

    public static void showLongToast(Context context, String message) {
        if (context != null)
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    public static boolean isInternetAvailable(Context mContext) {
        boolean ret = false;
        ConnectivityManager connectMgr = (ConnectivityManager) mContext.getSystemService(Service.CONNECTIVITY_SERVICE);
        NetworkInfo networkinfo = connectMgr.getActiveNetworkInfo();

        if (networkinfo != null && networkinfo.isConnected() && networkinfo.getState() == NetworkInfo.State.CONNECTED)
            ret = true;

        return ret;
    }

    public static String getEncryptedData(String plainRequest) {

        // Log plain request
        Log.d(PLAIN_REQUEST, plainRequest);
        // ASCII charset conversion
        Charset characterSet = Charset.forName("US-ASCII");
        byte[] toEncryptData = plainRequest.getBytes(characterSet);
        Log.d("toEncryptData", toEncryptData.toString());

        // RSA encryption using modulus and exponent
        byte[] modulusBytes = Base64.decodeBase64(QwikcilverAPI.getRSAMod().getBytes());
        byte[] exponentBytes = Base64.decodeBase64(QwikcilverAPI.getRSAExpo().getBytes());
        Log.d("modulusBytes", modulusBytes.toString());
        Log.d("exponentBytes", exponentBytes.toString());

        BigInteger exp = new BigInteger(1, exponentBytes);
        BigInteger mod = new BigInteger(1, modulusBytes);
        RSAPublicKeySpec keySpec = new RSAPublicKeySpec(mod, exp);

        byte[] cipherData = new byte[0];
        try {
            KeyFactory fact = KeyFactory.getInstance("RSA");
            PublicKey pubKeyn = fact.generatePublic(keySpec);
            Log.d("pubKeyn", pubKeyn.toString());
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1PADDING");
            cipher.init(Cipher.ENCRYPT_MODE, pubKeyn);
            cipherData = cipher.doFinal(toEncryptData);
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // convert to base64
        String encryptedStringBase64 = android.util.Base64.encodeToString(cipherData, android.util.Base64.DEFAULT);

        // Url-encoding
        String urlEncoded = URLEncoder.encode(encryptedStringBase64);

        Log.d("EncryptedAuth : ", urlEncoded);
        return urlEncoded;
    }

    public static long getTimeInMilliSec() {
        try {
            Calendar calendar = Calendar.getInstance();
            // calendar.clear();
            calendar.set(1970, Calendar.JANUARY, 1, 0, 0, 0);
            long millisSinceEpoch = System.currentTimeMillis();
            millisSinceEpoch = millisSinceEpoch / 1000;
            return millisSinceEpoch - 1262284200;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static String formatDecimal(String strToFormat) {
        DecimalFormat format = new DecimalFormat();
        format.setDecimalSeparatorAlwaysShown(false);
        return format.format(Double.parseDouble(strToFormat));
    }

    public static void logoutUser() {
        QwikcilverSharedPref qwikcilverSharedPref = new QwikcilverSharedPref();
        SharedPreferences sharedPreferences = qwikcilverSharedPref.getQwikcilverSharedPref();
        sharedPreferences.edit().clear().apply();
    }

    public static String getCurrentDateAndTime() {
        SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy hh:mm aa");
        return dateformat.format(Calendar.getInstance().getTime());
    }

    public static void showCountDownTimer(final PublishSubject<Boolean> isFinish, final long millisInFuture,
            long countDownInterval) {
        final CountDownTimer timer = new CountDownTimer(millisInFuture, countDownInterval) {
            @Override
            public void onTick(long l) {
                long counter = l / 1000;
                int progVal = (int) (100 * (1 - ((float) l / millisInFuture)));
            }

            @Override
            public void onFinish() {
                isFinish.onNext(true);
            }
        };
        timer.start();
    }
}
