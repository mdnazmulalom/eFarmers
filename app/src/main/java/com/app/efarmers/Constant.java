package com.app.efarmers;

public class Constant {

    public static final String MAIN_URL="https://android.nkrit.com/efarmers/android";
    public static final String HOST_URL="https://android.nkrit.com/efarmers/";


    public static final String LOGIN_URL = MAIN_URL+"/login.php";

    public static final String SIGNUP_URL = MAIN_URL+"/signup.php";
    public static final String ORDER_SUBMIT_URL = MAIN_URL+"/order_submit.php";

    public static final String PROFILE_URL = MAIN_URL+"/profile.php?cell=";

    public static final String RETAILERS_PROFILE_URL = MAIN_URL+"/retailers_profile.php?cell=";

    public static final String PROFILE_UPDATE_URL = MAIN_URL+"/update_profile.php";

    public static final String RETAILERS_PROFILE_UPDATE_URL = MAIN_URL+"/retailer_profile_update.php";

    public static final String UPDATE_ORDER_URL = MAIN_URL+"/update_order.php";

    public static final String PUBLISH_NOTICE_URL = MAIN_URL+"/publish_notice.php";

    public static final String ORDER_LIST_URL = MAIN_URL+"/order_list.php?cell=";


    public static final String NOTICE_LIST_URL = MAIN_URL+"/notice_list.php";


    public static final String NOTICE_DELETE_URL = MAIN_URL+"/delete_notice.php";


    public static final String FARMER_ORDER_LIST_URL = MAIN_URL+"/farmer_order_list.php?cell=";


    public static final String USER_PROFILE_URL = MAIN_URL+"/android/user_profile.php?cell=";

    public static final String PROFILE_IMAGE_URL = MAIN_URL+"/android/";

    public static final String UPDATE_USER_PROFILE_URL = MAIN_URL+"/android/update_user_profile.php";

    public static final String AMBULANCE_LIST_URL = MAIN_URL+"/android/ambulance_list.php";

    public static final String HOSPITAL_LIST_URL = MAIN_URL+"/android/hospital_list.php";


    public static final String UPDATE_LOCATION_URL = MAIN_URL+"/android/update_location.php";

    public static final String DOCTOR_LIST_URL = MAIN_URL+"/android/doctor_list.php";


    public static final String REPORT_URL = MAIN_URL+"/admin/report/get_report.php";
    public static final String TEST_LIST_URL = MAIN_URL+"/android/get_test_list.php?text=";

    public static final String VIEW_URL = MAIN_URL+"/admin/report/";
    public static final String KEY_URL = "file_name";
    public static final String KEY_ID = "id";
    public static final String KEY_TITLE = "title";
    public static final String KEY_TIME = "time";

    public static final String KEY_STATUS = "status";
    public static final String KEY_RETAILER_CELL = "retailer_cell";
    public static final String KEY_TEST_NAME = "name";
    public static final String KEY_QUANTITY = "quantity";
    public static final String KEY_TEST_TYPE = "type";
    public static final String KEY_PRICE = "price";


    public static final String JSON_ARRAY = "result";

    public static final String KEY_CELL = "cell";

    public static final String KEY_USER_CELL = "user_cell";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_DATE = "date";

    //If server response is equal to this that means login is successful
    public static final String SIGNUP_SUCCESS = "success";
    public static final String USER_EXISTS = "exists";


    public static final String KEY_TYPE= "type";
    public static final String KEY_FILE= "file";



    public static final String KEY_NAME = "name";
    public static final String KEY_ORDER_ID = "order_id";
    public static final String KEY_PRODUCT_NAME = "product_name";

    public static final String KEY_CATEGORY = "category";
    public static final String KEY_STOCK = "stock";
    public static final String KEY_ADDRESS = "address";

    public static final String KEY_EMAIL = "email";
    public static final String KEY_CURRENCY = "Tk.";
    public static final String KEY_PAYMENT_METHOD= "payment_method";
    public static final String KEY_GENERAL_BED = "g_bed";
    public static final String KEY_GENDER = "gender";
    public static final String KEY_CABIN = "cabin";
    public static final String KEY_LOCATION = "location";
    public static final String KEY_PROFILE_IMAGE = "image";
    public static final String KEY_PROFILE_NOT_COMPLETED = "0";
    public static final String KEY_PROFILE_COMPLETED = "1";

    public static final String KEY_AGE = "age";

    public static final String KEY_VEHICLE1 = "vehicle1";
    public static final String KEY_VEHICLE2 = "vehicle2";
    public static final String KEY_DEAD = "dead";

    public static final String KEY_LATITUDE = "latitude";
    public static final String KEY_LONGITUDE = "longitude";

    public static final String KEY_WOUNDED = "wounded";

    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_FARMER_CELL = "farmer_cell";




    //We will use this to store the user cell number into shared preference
    public static final String SHARED_PREF_NAME = "com.app.accident.userlogin"; //pcakage name+ id

    //This would be used to store the cell of current logged in user
    public static final String CELL_SHARED_PREF = "cell";
    public static final String PASSWORD_SHARED_PREF = "password";
    public static final String AC_TYPE_SHARED_PREF = "ac_type";

    //We will use this to store the boolean in sharedpreference to track user is loggedin or not
    public static final String LOGGEDIN_SHARED_PREF = "loggedin";


}
