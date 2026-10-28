package com.smartcrop.doctor.auth;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.Nullable;

/**
 * Manages farmer login sessions, guest mode, and profile preferences offline.
 */
public class SessionManager {

    private static final String PREF_NAME = "smartcrop_auth_pref";
    private static final String KEY_IS_LOGGED_IN = "is_logged_in";
    private static final String KEY_IS_GUEST = "is_guest";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_USER_NAME = "user_name";
    private static final String KEY_USER_EMAIL_OR_PHONE = "user_email_or_phone";
    private static final String KEY_FARM_NAME = "farm_name";

    private static volatile SessionManager INSTANCE;
    private final SharedPreferences prefs;

    public SessionManager(Context context) {
        this.prefs = context.getApplicationContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static SessionManager getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (SessionManager.class) {
                if (INSTANCE == null) {
                    INSTANCE = new SessionManager(context);
                }
            }
        }
        return INSTANCE;
    }

    /**
     * Save active user login session.
     */
    public void login(long userId, String fullName, String emailOrPhone, @Nullable String farmName) {
        prefs.edit()
                .putBoolean(KEY_IS_LOGGED_IN, true)
                .putBoolean(KEY_IS_GUEST, false)
                .putLong(KEY_USER_ID, userId)
                .putString(KEY_USER_NAME, fullName)
                .putString(KEY_USER_EMAIL_OR_PHONE, emailOrPhone)
                .putString(KEY_FARM_NAME, farmName != null ? farmName : "")
                .apply();
    }

    /**
     * Activate guest mode for immediate offline diagnostics without an account.
     */
    public void setGuestMode(boolean isGuest) {
        prefs.edit()
                .putBoolean(KEY_IS_GUEST, isGuest)
                .putBoolean(KEY_IS_LOGGED_IN, false)
                .apply();
    }

    /**
     * Check if a registered user is logged in.
     */
    public boolean isLoggedIn() {
        return prefs.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    /**
     * Check if currently in guest mode.
     */
    public boolean isGuest() {
        return prefs.getBoolean(KEY_IS_GUEST, false);
    }

    /**
     * Check if user has an active session (either logged in or selected guest mode).
     */
    public boolean hasActiveSession() {
        return isLoggedIn() || isGuest();
    }

    public long getUserId() {
        return prefs.getLong(KEY_USER_ID, -1);
    }

    public String getUserName() {
        if (isGuest()) {
            return "Guest Farmer";
        }
        return prefs.getString(KEY_USER_NAME, "Farmer");
    }

    public String getUserEmailOrPhone() {
        return prefs.getString(KEY_USER_EMAIL_OR_PHONE, "");
    }

    public String getFarmName() {
        return prefs.getString(KEY_FARM_NAME, "");
    }

    /**
     * Clear all session data upon logout.
     */
    public void logout() {
        prefs.edit()
                .clear()
                .apply();
    }
}
