package com.inwi.clubinwi.Utils;

import com.inwi.clubinwi.BuildConfig;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.io.File;


public class Constants {

    public static final String URL_BASE = BuildConfig.BASE_URL;

    public static final String URL_PREINSCRIPTION = URL_BASE + "/api/client/preinscription";
    public static final String URL_ACTIVATION = URL_BASE + "/api/client/activation";
    public static final String URL_SIGNUP = URL_BASE + "/api/client/inscription";
    public static final String URL_SIGNIN = URL_BASE + "/api/client/login";
    public static final String URL_FORGOT_PASSWORD = URL_BASE + "/api/client/request-password";
    public static final String URL_CONTROL_VERSION = URL_BASE + "/api/add/device";
    public static final String URL_TOMBOLAS = URL_BASE + "/api/get/tickets";

    public static final String URL_TOMBOLAS_BENEFIT = URL_BASE + "/api/benefit/ticket";
    public static final String URL_TOMBOLAS_ANNIF= URL_BASE + "/api/cadeaux/birthday/reserver";

    public static final String SHARED_PREFERENCES_NAME = "clubinwi_preferences";
    public static final String SHARED_PREFERENCES_GCM_TOKEN = SHARED_PREFERENCES_NAME + "gcm_token";
    public static final String SHARED_PREFERENCES_GCM_TOKEN_VERSION = SHARED_PREFERENCES_NAME + "gcm_token_version";
    public static final String USER_TOKEN = SHARED_PREFERENCES_NAME + "user_token";
    public static final String USER_PHONE = SHARED_PREFERENCES_NAME + "user_phone";
    public static final String USER_FULLNAME = SHARED_PREFERENCES_NAME + "user_fullname";
    public static final String USER_POINT = SHARED_PREFERENCES_NAME + "user_point";
    public static final String USER_FIRST_NAME = SHARED_PREFERENCES_NAME + "user_first_name";
    public static final String USER_LAST_NAME = SHARED_PREFERENCES_NAME + "user_last_name";
    public static final String USER_CNI = SHARED_PREFERENCES_NAME + "user_cni";
    public static final String USER_AVATAR = SHARED_PREFERENCES_NAME + "user_avatar";
    public static final String USER_EMAIL = SHARED_PREFERENCES_NAME + "user_email";
    public static final String USER_FILLEULS = SHARED_PREFERENCES_NAME + "user_filleuls";
    public static final String USER_FILLEULS_COUNT = SHARED_PREFERENCES_NAME + "user_filleuls_count";
    public static final String USER_FORFAITS = SHARED_PREFERENCES_NAME + "user_forfaits";
    public static final String USER_FORFAITS_ACTIF = SHARED_PREFERENCES_NAME + "user_forfaits_actif";
    public static final String USER_FORFAITS_COUNT = SHARED_PREFERENCES_NAME + "user_forfaits_count";
    public static final String USER_LEVEL = SHARED_PREFERENCES_NAME + "user_level";
    public static final String USER_LEVEL_TYPE = SHARED_PREFERENCES_NAME + "user_level_type";
    public static final String USER_CADEAUX = SHARED_PREFERENCES_NAME + "user_cadeaux";
    public static final String USER_DATE = SHARED_PREFERENCES_NAME + "user_date";
    public static final String USER_BIRTHDAY = SHARED_PREFERENCES_NAME + "user_birthday";
    public static final String USER_PUSH_STATUS = SHARED_PREFERENCES_NAME + "user_push_status";
    public static final String USER_ZIPCODE = SHARED_PREFERENCES_NAME + "user_zipcode";
    public static final String USER_ADDRESS = SHARED_PREFERENCES_NAME + "user_push_address";
    public static final String USER_CITY = SHARED_PREFERENCES_NAME + "user_city";
    public static final String USER_LANGUE = SHARED_PREFERENCES_NAME + "user_langue";
    public static final String URL_DEJA_MEMBER = SHARED_PREFERENCES_NAME + "url_deja_membre";
    public static final String URL_MODE_CONNECT = SHARED_PREFERENCES_NAME + "url_mode_connecte";
    public static final String PARRAIN_AVATAR = SHARED_PREFERENCES_NAME + "parrain_avatar";
    public static final String PARRAIN_FULLNAME = SHARED_PREFERENCES_NAME + "parrain_fullname";
    public static final String PARRAIN_LEVEL = SHARED_PREFERENCES_NAME + "parrain_level";
    public static final String PARRAIN_FORFAITS_COUNT = SHARED_PREFERENCES_NAME + "parrain_forfaits_count";
    public static final String PARRAIN_FILLEULS_COUNT = SHARED_PREFERENCES_NAME + "parrain_filleuls_count";
    public static final String PARRAIN_CADEAUX = SHARED_PREFERENCES_NAME + "parrain_cadeaux";
    public static final String PARRAIN_DATE = SHARED_PREFERENCES_NAME + "parrain_date";
    public static final int CAMERA_PERMISSIONS_REQUEST =777 ;
    public static final String URL_MODE_CONNECT_AR = SHARED_PREFERENCES_NAME + "url_mode_connecte_ar";;
    public static File cacheDir;
    public static ImageLoader imageLoader;
    public static ImageLoaderConfiguration config;
    public static DisplayImageOptions options;

    public static final String FONT_NAME_LIGHT = "fonts/omnes_Light.otf";
    public static final String FONT_NAME_REGULAR = "fonts/omnes_medium.ttf";
    public static final String FONT_NAME_BOLD = "fonts/omnes_semibold.ttf";

    public static final int FONT_STYLE_LIGHT = 1;
    public static final int FONT_STYLE_REGULAR = 2;
    public static final int FONT_STYLE_BOLD = 3;

}