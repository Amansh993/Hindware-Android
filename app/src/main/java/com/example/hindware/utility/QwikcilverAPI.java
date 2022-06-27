package com.example.hindware.utility;

import com.example.hindware.BuildConfig;

/**
 * Created by SandeepY on 07122020
 **/

public class QwikcilverAPI {

    private static String HINDWARE_BASE_URL = "";
    private static String RSA_MOD = "";
    private static String RSA_EXPO = "";
    private static String REDIRECTION_URL = "";
    private static String C = "";

    public static void setAPIEnvironment() {
        switch (APIEnvironment.valueOf(BuildConfig.ENV)) {
            case DEBUG:
                setupDebugEnvironment();
                break;

            case RELEASE:
                setupProductionEnvironment();
                break;

            default:
                setupDebugEnvironment();
                break;
        }
    }

    public static String getBaseUrl() {
        return HINDWARE_BASE_URL;
    }

    public static String getRedirectURL() {
        return REDIRECTION_URL;
    }

    public static String getRSAMod() {
        return RSA_MOD;
    }

    public static String getRSAExpo() {
        return RSA_EXPO;
    }

    public static String getC() {
        return C;
    }

    private static void setupDebugEnvironment() {
        HINDWARE_BASE_URL = "https://test.saluto.in/productapi/";
        RSA_MOD = "0tlLKB6gtbVScn2iW1pu8txgzqoLec6TZfpju0KAIcSPyitA5Guk5CRtbFQ3dMzTupSKA2XvOy/49pRwx6OAOTYRI3np1LKS4vMaX391FkKoqMqMOdioTC+l7xwYENTJweRU+c3mBm/vKauID158crRI5kYjJUvy2277NRyUFa/SnZNtpuFQD0HgNlfw1kz8eBb1qsFIjG2/GE3nEg0cCfNVkRxr8UgY6vC8PKlaBCQLcDi0bS33h0+BasbSUCD7dry9HTFYSejHmzgdjamATyfuFn4A6Rqs7tZfuAGVsCncOQMDDOeKcesabI22CxFWtuUyyh3Jr8DjCnfoUIgd5Q==";
        RSA_EXPO = "AQAB";
        REDIRECTION_URL = "https://test.saluto.in/hindware";
        C = "46";
    }

    private static void setupProductionEnvironment() {
        HINDWARE_BASE_URL = "https://test.saluto.in/productapi/";
        RSA_MOD = "0tlLKB6gtbVScn2iW1pu8txgzqoLec6TZfpju0KAIcSPyitA5Guk5CRtbFQ3dMzTupSKA2XvOy/49pRwx6OAOTYRI3np1LKS4vMaX391FkKoqMqMOdioTC+l7xwYENTJweRU+c3mBm/vKauID158crRI5kYjJUvy2277NRyUFa/SnZNtpuFQD0HgNlfw1kz8eBb1qsFIjG2/GE3nEg0cCfNVkRxr8UgY6vC8PKlaBCQLcDi0bS33h0+BasbSUCD7dry9HTFYSejHmzgdjamATyfuFn4A6Rqs7tZfuAGVsCncOQMDDOeKcesabI22CxFWtuUyyh3Jr8DjCnfoUIgd5Q==";
        RSA_EXPO = "AQAB";
        REDIRECTION_URL = "https://test.saluto.in/hindware";
        C = "46";
    }

    public enum APIEnvironment {
        DEBUG, RELEASE
    }
}
