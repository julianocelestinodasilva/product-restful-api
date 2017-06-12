package br.com.productrestfulapi.acceptancetests;

/**
 * Created by juliano on 12/06/17.
 */
public class URLApi {

    private static final String  URL_API = "http://localhost:8080/productAPI";

    static String product() {
        return URL_API + "/product";
    }

    static String image() {
        return URL_API + "/image";
    }
}
