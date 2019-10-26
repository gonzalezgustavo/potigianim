package com.example.potigianim.utils;

public class Constants {
    static final String PREFS_NAME = "PotigianIM";

    public static final String DEFAULT_BASE = "http://192.168.10.98:9092/api";
    public static String LOGIN_URL;
    public static String SEARCH_OC_URL;
    public static String OC_DETAILS_URL;

    public static final String USER_KEY = "_user";
    public static final String FRAGMENT_KEY = "_currentFragment";
    public static final String CURRENT_ORDER_KEY = "_currentOrder";
    public static final String CURRENT_ORDER_ADDITIONAL_KEY = "_currentOrderAdditional";
    public static final String COUNT_ORDER_KEY = "_countRequest:{prefixOc}-{oc}-{suffixOc}";
    public static final String TERMINAL_KEY = "_terminal";
    public static final String API_KEY = "_api";

    public static final String DATE_SUFFIX = "_date";

    public static void setApiUrl(String baseUrl) {
        LOGIN_URL = baseUrl + "/seguridad/{user}/{accessCode}";
        SEARCH_OC_URL = baseUrl + "/ordenes/{suffixCode}/proveedor/{providerCode}";
        OC_DETAILS_URL = baseUrl + "/ordenes/{prefixOc}/{oc}/{suffixOc}";
    }
}
