package com.smartcrop.doctor.repository;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.Nullable;

import com.smartcrop.doctor.auth.PasswordUtils;
import com.smartcrop.doctor.auth.SessionManager;
import com.smartcrop.doctor.database.AppDatabase;
import com.smartcrop.doctor.database.dao.UserDao;
import com.smartcrop.doctor.database.entity.User;

public class UserRepository {

    private final UserDao userDao;
    private final SessionManager sessionManager;
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    public interface AuthCallback {
        void onSuccess(User user);
        void onError(String errorMessage);
    }

    public interface UserCallback {
        void onUserLoaded(@Nullable User user);
    }

    public UserRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        this.userDao = db.userDao();
        this.sessionManager = SessionManager.getInstance(application);
    }

    /**
     * Register a new farmer account locally in Room.
     */
    public void register(String fullName, String emailOrPhone, @Nullable String farmName,
                         String password, AuthCallback callback) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            try {
                String cleanContact = emailOrPhone != null ? emailOrPhone.trim() : "";
                String cleanName = fullName != null ? fullName.trim() : "";
                String cleanFarm = farmName != null ? farmName.trim() : "";

                if (cleanName.isEmpty()) {
                    postError(callback, "Please enter your full name.");
                    return;
                }
                if (cleanContact.isEmpty()) {
                    postError(callback, "Please enter an email address or phone number.");
                    return;
                }
                if (password == null || password.length() < 6) {
                    postError(callback, "Password must be at least 6 characters.");
                    return;
                }

                User existing = userDao.findByEmailOrPhone(cleanContact);
                if (existing != null) {
                    postError(callback, "An account with this email or phone already exists.");
                    return;
                }

                String salt = PasswordUtils.generateSalt();
                String passwordHash = PasswordUtils.hashPassword(password, salt);
                long now = System.currentTimeMillis();

                User newUser = new User(cleanName, cleanContact, cleanFarm, passwordHash, salt, now, now);
                long generatedId = userDao.insert(newUser);
                newUser.setId(generatedId);

                // Update session
                sessionManager.login(generatedId, cleanName, cleanContact, cleanFarm);

                postSuccess(callback, newUser);
            } catch (Exception e) {
                postError(callback, "Registration failed: " + e.getLocalizedMessage());
            }
        });
    }

    /**
     * Authenticate an existing farmer against the local Room database.
     */
    public void login(String emailOrPhone, String password, AuthCallback callback) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            try {
                String cleanContact = emailOrPhone != null ? emailOrPhone.trim() : "";
                if (cleanContact.isEmpty()) {
                    postError(callback, "Please enter your email or phone.");
                    return;
                }
                if (password == null || password.isEmpty()) {
                    postError(callback, "Please enter your password.");
                    return;
                }

                User user = userDao.findByEmailOrPhone(cleanContact);
                if (user == null) {
                    postError(callback, "No account found for '" + cleanContact + "'. Please check your credentials or create an account.");
                    return;
                }

                boolean matches = PasswordUtils.verifyPassword(password, user.getSalt(), user.getPasswordHash());
                if (!matches) {
                    postError(callback, "Incorrect password. Please try again.");
                    return;
                }

                // Update last login timestamp
                user.setLastLoginAt(System.currentTimeMillis());
                userDao.update(user);

                // Save session
                sessionManager.login(user.getId(), user.getFullName(), user.getEmailOrPhone(), user.getFarmName());

                postSuccess(callback, user);
            } catch (Exception e) {
                postError(callback, "Login error: " + e.getLocalizedMessage());
            }
        });
    }

    /**
     * Retrieve user by id asynchronously.
     */
    public void getUserById(long id, UserCallback callback) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            User user = userDao.findById(id);
            mainHandler.post(() -> callback.onUserLoaded(user));
        });
    }

    private void postSuccess(AuthCallback callback, User user) {
        if (callback != null) {
            mainHandler.post(() -> callback.onSuccess(user));
        }
    }

    private void postError(AuthCallback callback, String message) {
        if (callback != null) {
            mainHandler.post(() -> callback.onError(message));
        }
    }
}
