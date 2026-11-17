package com.lockx.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;

import java.security.MessageDigest;
import java.security.SecureRandom;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class PatternLock {

    private static final String PREFS = "lockx_security";
    private static final String SALT = "pattern_salt";
    private static final String HASH = "pattern_hash";

    private final SharedPreferences prefs;

    public PatternLock(Context context) {
        prefs = context.getSharedPreferences(
            PREFS, Context.MODE_PRIVATE
        );
    }

    public boolean setPattern(String pattern) {
        if (!isValid(pattern)) {
            return false;
        }

        try {
            byte[] salt = new byte[16];
            new SecureRandom().nextBytes(salt);

            byte[] hash = hashPattern(pattern, salt);

            prefs.edit()
                .putString(SALT,
                    Base64.encodeToString(salt, Base64.NO_WRAP))
                .putString(HASH,
                    Base64.encodeToString(hash, Base64.NO_WRAP))
                .apply();

            return true;

        } catch (Exception e) {
            return false;
        }
    }

    public boolean verifyPattern(String pattern) {
        if (!isValid(pattern)) {
            return false;
        }

        try {
            String savedSalt = prefs.getString(SALT, null);
            String savedHash = prefs.getString(HASH, null);

            if (savedSalt == null || savedHash == null) {
                return false;
            }

            byte[] salt = Base64.decode(
                savedSalt, Base64.NO_WRAP
            );

            byte[] expected = Base64.decode(
                savedHash, Base64.NO_WRAP
            );

            byte[] actual = hashPattern(pattern, salt);

            return MessageDigest.isEqual(
                expected, actual
            );

        } catch (Exception e) {
            return false;
        }
    }

    public boolean hasPattern() {
        return prefs.contains(HASH);
    }

    private boolean isValid(String pattern) {
        if (pattern == null ||
            !pattern.matches("[0-8]{4,9}")) {
            return false;
        }

        return pattern.chars().distinct().count()
            == pattern.length();
    }

    private byte[] hashPattern(
        String pattern, byte[] salt
    ) throws Exception {

        PBEKeySpec spec = new PBEKeySpec(
            pattern.toCharArray(),
            salt,
            120000,
            256
        );

        try {
            SecretKeyFactory factory =
                SecretKeyFactory.getInstance(
                    "PBKDF2WithHmacSHA1"
                );

            return factory.generateSecret(spec).getEncoded();

        } finally {
            spec.clearPassword();
        }
    }
}
