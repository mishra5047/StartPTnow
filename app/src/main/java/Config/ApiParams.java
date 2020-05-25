package Config;

/**
 * Created by subhashsanghani on 10/18/16.
 * All end points for api is given here....
 * <p>
 * Please find api code in  application/controllers/api.php  file here.. all function releated end points is given..
 * /index.php/api/login   mean  public function login() is for login.
 */
public class ApiParams {

    public static String PARM_RESPONCE = "responce";
    public static String PARM_DATA = "data";
    public static String PARM_ERROR = "error";
    private static String API_V = "api";
    public static String CATEGORY_LIST = ConstValue.BASE_URL + "/index.php/" + API_V + "/get_categories";
    public static String BUSINESS_LIST = ConstValue.BASE_URL + "/index.php/" + API_V + "/get_business";
    public static String BUSINESS_SERVICES = ConstValue.BASE_URL + "/index.php/" + API_V + "/get_services";
    public static String GET_DOCTORS = ConstValue.BASE_URL + "/index.php/" + API_V + "/get_doctors";
    public static String BUSINESS_PHOTOS = ConstValue.BASE_URL + "/index.php/" + API_V + "/get_photos";
    public static String BUSINESS_REVIEWS = ConstValue.BASE_URL + "/index.php/" + API_V + "/get_reviews";
    public static String ADD_BUSINESS_REVIEWS = ConstValue.BASE_URL + "/index.php/" + API_V + "/add_business_review";
    public static String TIME_SLOT_URL = ConstValue.BASE_URL + "/index.php/" + API_V + "/get_time_slot";
    public static String GET_LOCALITY = ConstValue.BASE_URL + "/index.php/" + API_V + "/get_locality";

    public static String PAYMENT_URL = ConstValue.BASE_URL + "/index.php/payorder/paypal";

    public static String LOGIN_URL = ConstValue.BASE_URL + "/index.php/" + API_V + "/login";
    public static String REGISTER_URL = ConstValue.BASE_URL + "/index.php/" + API_V + "/signup";
    public static String BOOKAPPOINTMENT_URL = ConstValue.BASE_URL + "/index.php/" + API_V + "/add_appointment";
    public static String BOOKAPPOINTMENT_TEMP_URL = ConstValue.BASE_URL + "/index.php/" + API_V + "/add_appointment_temp";
    public static String CHANGE_PASSWORD_URL = ConstValue.BASE_URL + "/index.php/" + API_V + "/change_password";
    public static String FORGOT_PASSWORD_URL = ConstValue.BASE_URL + "/index.php/" + API_V + "/forgot_password";
    public static String USERDATA_URL = ConstValue.BASE_URL + "/index.php/" + API_V + "/get_userdata";
    public static String UPDATEPROFILE_URL = ConstValue.BASE_URL + "/index.php/" + API_V + "/update_profile";
    public static String MYAPPOINTMENTS_URL = ConstValue.BASE_URL + "/index.php/" + API_V + "/my_appointments";
    public static String CANCELAPPOINTMENTS_URL = ConstValue.BASE_URL + "/index.php/" + API_V + "/cancel_appointment";
    public static String REGISTER_FCM_URL = ConstValue.BASE_URL + "/index.php/" + API_V + "/register_fcm";
    public static String GET_INSURANCE_TYPE_URL = ConstValue.BASE_URL + "/index.php/" + API_V + "/get_insurance_type";
    public static String GET_INSURANCE_PROVIDERS_URL = ConstValue.BASE_URL + "/index.php/" + API_V + "/get_insurance_providers";
    public static String SEND_VERIFY_CODE_URL = ConstValue.BASE_URL + "/index.php/" + API_V + "/send_verify ";

    public static String PREF_NAME = "doctappo.pref";
    public static String PREF_CATEGORY = "pref_category";
    public static String COMMON_KEY = "user_id";
    //public static String PRICE_CART = "price_cart";
    public static String PREF_ERROR = "error_stack";
    public static String USER_DATA = "user_data";

    public static String USER_FULLNAME = "user_fullname";
    public static String USER_EMAIL = "user_email";
    public static String USER_PHONE = "user_phone";
    public static String USER_INSURANCE_TYPE = "user_insurance_type_id";
    public static String USER_INSURANCE_PROVIDER = "user_insurance_provider_id";

}
