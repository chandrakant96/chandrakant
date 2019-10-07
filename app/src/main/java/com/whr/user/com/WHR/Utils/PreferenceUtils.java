package com.whr.user.com.WHR.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by lenovo on 11/29/2016.
 */

public class PreferenceUtils {

    SharedPreferences preferences;
    Context context;
    private static final String PREF_NAME = "androidhive-welcome";
    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";

    public void setFirstTimeLaunch(boolean isFirstTime) {
        SharedPreferences.Editor editor_loginStatus = preferences.edit();
        editor_loginStatus.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor_loginStatus.apply();
    }

    public boolean isFirstTimeLaunch() {
        return preferences.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }

    public PreferenceUtils(Context context) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        this.context = context;
    }

    public void setKeepMeLoin(boolean status) {
        SharedPreferences.Editor editor_loginStatus = preferences.edit();
        editor_loginStatus.putBoolean("keepMeLogin", status);
        editor_loginStatus.apply();
    }

    public boolean getKeepMeLogin() {
        return preferences.getBoolean("keepMeLogin", false);
    }

    public void setSpecialization(String setSpecialization) {
        SharedPreferences.Editor editor_Uid = preferences.edit();
        editor_Uid.putString("setSpecialization", setSpecialization);
        editor_Uid.apply();
    }

    public String getSpecialization() {
        return preferences.getString("setSpecialization", null);
    }


    public void setLogin(boolean status) {
        SharedPreferences.Editor editor_loginStatus = preferences.edit();
        editor_loginStatus.putBoolean("LOGIN", status);
        editor_loginStatus.apply();
    }

    public void setClear() {
        SharedPreferences.Editor editor_loginStatus = preferences.edit();
        editor_loginStatus.putBoolean("LOGIN", false);
        editor_loginStatus.clear();
        editor_loginStatus.apply();
    }

    public boolean isLogin() {
        return preferences.getBoolean("LOGIN", false);
    }

    public void setUID(String UID) {
        SharedPreferences.Editor editor_Uid = preferences.edit();
        editor_Uid.putString("UID", UID);
        editor_Uid.apply();
    }

    public String getUID() {
        //return "7588249500";
        return preferences.getString("UID", null);
    }


    public void setPassword(String password) {
        SharedPreferences.Editor editor_Uid = preferences.edit();
        editor_Uid.putString("Password", password);
        editor_Uid.apply();
    }

    public String getPassword() {
        return preferences.getString("Password", "");
    }


    public void setUserInfo(String userInfo) {
        SharedPreferences.Editor editor_Uid = preferences.edit();
        editor_Uid.putString("userInfo", userInfo);
        editor_Uid.apply();
    }


    public String getUserInfo() {
        return preferences.getString("userInfo", "");
    }


    public void setUserName(String uname) {
        SharedPreferences.Editor editor_Uid = preferences.edit();
        editor_Uid.putString("uname", uname);
        editor_Uid.apply();
    }

    public String getUserName() {
        return preferences.getString("uname", "");
    }


    public void setCno(String uname) {
        SharedPreferences.Editor editor_Uid = preferences.edit();
        editor_Uid.putString("cno", uname);
        editor_Uid.apply();
    }

    public String getCno() {
        return preferences.getString("cno", "");
    }

    public void setEmail(String uname) {
        SharedPreferences.Editor editor_Uid = preferences.edit();
        editor_Uid.putString("email", uname);
        editor_Uid.apply();
    }


    public String getEmail() {
        // return "s.jagtap048@gmail.com";
        return preferences.getString("email", "");
    }

    String flashmsg = "";

    public String getFlashmsg() {
        return preferences.getString("flashmsg", "");
    }

    public void setFlashmsg(String flashmsg) {
        SharedPreferences.Editor editor_Uid = preferences.edit();
        editor_Uid.putString("flashmsg", flashmsg);
        editor_Uid.apply();
    }

    public void setBloodGroup(String uname) {
        SharedPreferences.Editor editor_Uid = preferences.edit();
        editor_Uid.putString("bgroup", uname);
        editor_Uid.apply();
    }

    public String getBloodGroup() {
        return preferences.getString("bgroup", null);
    }


    public void setEmr_cno(String uname) {
        SharedPreferences.Editor editor_Uid = preferences.edit();
        editor_Uid.putString("emr_cno", uname);
        editor_Uid.apply();
    }

    public String getEmr_cno() {
        return preferences.getString("Emr_cno", null);
    }

    public void setPolicy(String uname) {
        SharedPreferences.Editor editor_Uid = preferences.edit();
        editor_Uid.putString("emr_cno", uname);
        editor_Uid.apply();
    }


    public String getPolicy() {
        return preferences.getString("Emr_cno", null);
    }


    public void isvalidate(boolean status) {
        SharedPreferences.Editor editor_loginStatus = preferences.edit();
        editor_loginStatus.putBoolean("isvalidate", status);
        editor_loginStatus.apply();
    }

    public boolean isvalidate() {
        return preferences.getBoolean("isvalidate", false);
    }


    public void setpwd(String uname) {
        SharedPreferences.Editor editor_Uid = preferences.edit();
        editor_Uid.putString("pwd", uname);
        editor_Uid.apply();
    }


    public String getpwd() {
        return preferences.getString("pwd", null);
    }

    public void setGender(String uname) {
        SharedPreferences.Editor editor_Uid = preferences.edit();
        editor_Uid.putString("gender", uname);
        editor_Uid.apply();
    }


    public String getGender() {
        return preferences.getString("gender", null);
    }


    public void setDob(String uname) {
        SharedPreferences.Editor editor_Uid = preferences.edit();
        editor_Uid.putString("dob", uname);
        editor_Uid.apply();
    }


    public String getDob() {
        return preferences.getString("dob", null);
    }

    public void setComp(int Comp) {

        SharedPreferences.Editor editor_Uid = preferences.edit();
        editor_Uid = preferences.edit();
        editor_Uid.putInt("Comp", Comp);
        editor_Uid.apply();
    }

    public int getComp() {
        return preferences.getInt("Comp", 0);
    }


    public void setadharno(long Comp) {

        SharedPreferences.Editor editor_Uid = preferences.edit();
        editor_Uid = preferences.edit();
        editor_Uid.putLong("Comp", Comp);
        editor_Uid.apply();
    }

    public long getadharno() {
        return preferences.getInt("Comp", 0);
    }

    public void setUserProfileImage(String profile_Image) {
        SharedPreferences.Editor editor_Uid = preferences.edit();
        editor_Uid.putString("profile_Image", profile_Image);
        editor_Uid.apply();
    }

    public String getUserProfileImage() {
        return preferences.getString("profile_Image", null);
    }

    public void setwallet_point(String wallet_point) {
        SharedPreferences.Editor editor_Uid = preferences.edit();
        editor_Uid.putString("wallet_point", wallet_point);
        editor_Uid.apply();
    }

    public String getwallet_point() {
        return preferences.getString("wallet_point", null);
    }


    public void setwallet_discount(String wallet_discount) {
        SharedPreferences.Editor editor_Uid = preferences.edit();
        editor_Uid.putString("wallet_discount", wallet_discount);
        editor_Uid.apply();
    }

    public String getwallet_discount() {
        return preferences.getString("wallet_discount", null);
    }

    public void setMedicineName(String nedicine) {

        SharedPreferences.Editor editor_Uid = preferences.edit();
        editor_Uid = preferences.edit();
        editor_Uid.putString("nedicine", nedicine);
        editor_Uid.apply();
    }

    public String getMedicine() {
        return preferences.getString("nedicine", null);
    }


    public void setDoctorList(String nedicine) {

        SharedPreferences.Editor editor_Uid = preferences.edit();
        editor_Uid = preferences.edit();
        editor_Uid.putString("nedicine", nedicine);
        editor_Uid.apply();
    }

    public String getDoctoList() {
        return preferences.getString("nedicine", null);
    }


    public void setNotificationToken(String notificationString) {
        SharedPreferences.Editor editor_Uid = preferences.edit();
        editor_Uid.putString("notificationString", notificationString);
        editor_Uid.apply();
    }

    public String getNotificationToken() {
        return preferences.getString("notificationString", null);
    }


    public void setLatitude(String Latitude) {
        SharedPreferences.Editor editor_Uid = preferences.edit();
        editor_Uid.putString("Latitude", Latitude);
        editor_Uid.apply();
    }

    public String getLatitude() {
        return preferences.getString("Latitude", null);
    }

    public void setlongitude(String longitude) {
        SharedPreferences.Editor editor_Uid = preferences.edit();
        editor_Uid.putString("longitude", longitude);
        editor_Uid.apply();
    }

    public String getlongitude() {
        return preferences.getString("longitude", null);
    }

    public void setconsultationfreestatus(String consultationfreestatus) {
        SharedPreferences.Editor editor_Uid = preferences.edit();
        editor_Uid.putString("consultationfreestatus", consultationfreestatus);
        editor_Uid.apply();
    }

    public String getconsultationfreestatus() {
        return preferences.getString("consultationfreestatus", "0");
    }

    public void setconsultationfreestatus1(String consultationfreestatus1) {
        SharedPreferences.Editor editor_Uid = preferences.edit();
        editor_Uid.putString("consultationfreestatus1", consultationfreestatus1);
        editor_Uid.apply();
    }

    public String getconsultationfreestatus1() {
        return preferences.getString("consultationfreestatus1", "0");
    }


    public String getLanguage() {
        return preferences.getString("LANGUAGE", "en");
    }

    public void setLanguage(String language) {
        SharedPreferences.Editor editor_loginStatus = preferences.edit();
        editor_loginStatus.putString("LANGUAGE", language);
        editor_loginStatus.apply();
    }
}