package Config;

/**
 * Created by LENOVO on 4/20/2016.
 * Please Note following BASE_URL is an url of your backend link where you hosted backend.. you can give your ip or domain path here..
 * This is end point of API url.. through this url all api calls.
 * <p>
 * Currency use in app..
 */
public class ConstValue {

    //public static String BASE_URL = "http://startptnow.nobosoft.com";
    //public static String BASE_URL = "http://startptnow.nobosoft.com";
    public static String BASE_URL = "http://startptnow.nobosoft.com";

    public static String CURRENCY = "USD.";

    /**
     * PAYPAL SETTINGS
     **/
    public static final Boolean enable_paypal = false;
    public static final String paypal_target_url_prefix = "";

    public static final String PREFS_LANGUAGE = "doct_lang";

    /**
     * ALLOW to show Arabic language choose button on drawer menu
     **/
    public static final boolean ENABLE_SPANISH_LANG_TO_CHOOSE = true;

}
