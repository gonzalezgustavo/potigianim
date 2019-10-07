package com.example.potigianhh.utils;

public class Constants {
    static final String PREFS_NAME = "PotigianHH";

    /*
    private static final String API_URL = "http://192.168.10.98:9092/api";

    public static final String LOGIN_URL = Constants.API_URL + "/preparadores/{preparerId}";
    public static final String REQUESTS_HEADERS_ASSIGNED_URL = Constants.API_URL + "/pedidos/cabe/asignados/{preparerId}?cigarrillos={onlyCigarettes}";
    public static final String REQUEST_DETAILS_URL = Constants.API_URL + "/pedidos/detalle/{prefixDoc}/{document}/{suffixDoc}";
    public static final String REQUEST_CLOSE_URL = Constants.API_URL + "/pedidos/preparaciones/{prefixDoc}/{document}/{suffixDoc}";
    */
    public static final String DEFAULT_BASE = "http://192.168.10.98:9092/api";
    public static String LOGIN_URL;
    public static String REQUESTS_HEADERS_ASSIGNED_URL;
    public static String REQUEST_DETAILS_URL;
    public static String REQUEST_CLOSE_URL;

    //public static final String LOGIN_URL = "http://5d83ba2bc9e341001407194f.mockapi.io/api/preparadores/";
    //public static final String REQUESTS_HEADERS_ASSIGNED_URL = "http://5d83ba2bc9e341001407194f.mockapi.io/api/cabe";
    //public static final String REQUEST_DETAILS_URL = "http://5d83ba2bc9e341001407194f.mockapi.io/api/detalle";
    //public static final String REQUEST_CLOSE_URL = "http://5d83ba2bc9e341001407194f.mockapi.io/api/preparaciones";

    public static final String PREPARER_KEY = "_preparer";
    public static final String FRAGMENT_KEY = "_currentFragment";
    public static final String ASSIGNED_KEY = "_assignedRequests:{preparerId}";
    public static final String CURRENT_REQUEST_KEY = "_currentRequest";
    public static final String COUNT_REQUEST_KEY = "_countRequest:{prefixDoc}-{document}-{suffixDoc}";
    public static final String API_KEY = "_api";

    public static final String DATE_SUFFIX = "_date";

    public static void setApiUrl(String baseUrl) {
        LOGIN_URL = baseUrl + "/preparadores/{preparerId}";
        REQUESTS_HEADERS_ASSIGNED_URL = baseUrl + "/pedidos/cabe/asignados/{preparerId}?cigarrillos={onlyCigarettes}";
        REQUEST_DETAILS_URL = baseUrl + "/pedidos/detalle/{prefixDoc}/{document}/{suffixDoc}";
        REQUEST_CLOSE_URL = baseUrl + "/pedidos/preparaciones/{prefixDoc}/{document}/{suffixDoc}";
    }
}
