package com.smartcrop.doctor.database.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

/**
 * Entity representing a farmer user account.
 * Persisted locally in Room for offline access.
 */
@Entity(tableName = "users", indices = {@Index(value = {"emailOrPhone"}, unique = true)})
public class User {

    @PrimaryKey(autoGenerate = true)
    private long id;

    @NonNull
    private String fullName;

    @NonNull
    private String emailOrPhone;

    private String farmName;

    @NonNull
    private String passwordHash;

    @NonNull
    private String salt;

    private long createdAt;

    private long lastLoginAt;

    public User(@NonNull String fullName, @NonNull String emailOrPhone, String farmName,
                @NonNull String passwordHash, @NonNull String salt, long createdAt, long lastLoginAt) {
        this.fullName = fullName;
        this.emailOrPhone = emailOrPhone;
        this.farmName = farmName;
        this.passwordHash = passwordHash;
        this.salt = salt;
        this.createdAt = createdAt;
        this.lastLoginAt = lastLoginAt;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @NonNull
    public String getFullName() {
        return fullName;
    }

    public void setFullName(@NonNull String fullName) {
        this.fullName = fullName;
    }

    @NonNull
    public String getEmailOrPhone() {
        return emailOrPhone;
    }

    public void setEmailOrPhone(@NonNull String emailOrPhone) {
        this.emailOrPhone = emailOrPhone;
    }

    public String getFarmName() {
        return farmName;
    }

    public void setFarmName(String farmName) {
        this.farmName = farmName;
    }

    @NonNull
    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(@NonNull String passwordHash) {
        this.passwordHash = passwordHash;
    }

    @NonNull
    public String getSalt() {
        return salt;
    }

    public void setSalt(@NonNull String salt) {
        this.salt = salt;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public long getLastLoginAt() {
        return lastLoginAt;
    }

    public void setLastLoginAt(long lastLoginAt) {
        this.lastLoginAt = lastLoginAt;
    }
}
