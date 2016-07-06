package com.jiayusoft.mobile.utils;

import android.os.Environment;

import java.io.File;

/**
 * Created by ASUS on 2014/7/1.
 */
public interface GlobalData {

    public static final String versionCode = "versioncode";
    public static final String softName = "softname";
    public static final String defaultSoftName = "doctor";
    public static final String defaultDBName = "DB";
    public static final String defaultNetErrorMsg = "网络连接错误，请检查网络或服务地址设置。";


    public static final String defaultServerUrl = "221.214.98.55:8880/mobile";
    public static final String innerServerUrl = "11.0.0.55:8880/mobile";
    public static final String serverUrl = "serverUrl";

    public static final String officeInited = "officeInited";
    public static final String officeVersion = "officeVersion";
    public static final int officeCurrentVersion = 2;
    //2 增加西院科室信息

    public static final String LOGIN_USER_NAME = "LOGIN_USER_NAME";
    public static final String LOGIN_PASSWORD = "LOGIN_PASSWORD";
    public static final String LOGIN_SAVE_PASSWORD = "LOGIN_SAVE_PASSWORD";
    public static final String loginAutoLogin = "loginAutoLogin";
    public static final String loginSuoshuJigouName = "loginSuoshuJigouName";
    public static final String loginSuoshuJigouID = "loginSuoshuJigouID";

    public static final int search_time_result = 100;
    public static final String searchBeginTime = "searchBeginTime";
    public static final String searchEndTime = "searchEndTime";

    public static final String itemTitle = "itemTitle";
    public static final String itemType = "itemType";
    public static final int searchTypeCheck = 1;
    public static final int searchTypeBorrow = 2;

    public static final int cardImageStar = 1;
    public static final int cardImageBorrow = 2;
    public static final int cardImageNull = 3;
    public static final int cardImageNotYet = 4;
    public static final int cardImageAccepted = 5;
    public static final int cardImageRefused = 6;
    public static final int cardImageQuality = 7;

    public static final int cardClickEvent = 1;
    public static final int cardImageEvent = 2;

    public static final int httpGet = 1;
    public static final int httpPost = 2;

    public static final int searchBorrowLoadMore = 3;

    public static final int tagLogin = 1;
//    String loginUrl = "/user/login/doctor";
    String loginUrl = "/user/login/doctorwithimages";
    public static final String loginUserID = "userid";
    public static final String loginPassword = "password";
    public static final String loginOrgcode = "orgcode";

    public static final int tagCheckUpdate = 2;
    public static final int tagCheckDBUpdate = 21;
    String checkUpdateUrl = "/user/checkUpdate";

    public static final int tagDownloadNewFile = 3;
    public static final int tagDownloadDBNewFile = 31;

    public static final int tagGetOrgInfo = 4;
    String getOrgInfoUrl = "/user/orginfo";

    String logoImgUrl = "Logo/%s";//fileName



    int tagSearchLoadMore = 11;
    String searchLoadMoreUrl = "/bingan/doctor/list";
    String searchBorrowLoadMoreUrl = "/bingan/borrowlist";
    int tagSearchQualityLoadMore = 12;
    String searchQualityLoadMoreUrl = "/bingan/qualityneedrepair";
//    String searchQualityLoadMoreUrl = "/bingan/qualitystates";

    int tagBinganDetail = 13;
    String binganDetailUrl = "/bingan/doctor/detail/%s/%s";
    String binganDetailImageUrl = "Doc/%s";//fileName

    int tagBorrowBingan = 14;
    String borrowBinganUrl = "/bingan/borrow";

    int tagSearchStatesLoadMore = 15;
    String searchBorrowStatesLoadMoreUrl = "/bingan/borrowstates";




    String baseFolder = Environment.getExternalStorageDirectory().getPath()+ File.separator+"JiayuSoft"+ File.separator;
    String updateFolder = baseFolder + "update" + File.separator;

}
