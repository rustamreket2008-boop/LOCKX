package com.lockx.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;

import java.security.MessageDigest;
import java.security.SecureRandom;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class PinLock {

    private static final String PREFS = "lockx_security";
    private static final String SALT = "pin_salt";
    private static final String HASH = "pin_hash";

    private final SharedPreferences prefs;

    public PinLock(Context context) {
        prefs = context.getSharedPreferences(
            PREFS, Context.MODE_PRIVATE
        );
    }

    public boolean setPin(String pin) {
        if (pin == null ||
            !pin.matches("[0-9]{4}|[0-9]{6}")) {
            return false;
        }

        try {
            byte[] salt = new byte[16];
            new SecureRandom().nextBytes(salt);

            byte[] hash = hashPin(pin, salt);

            prefs.edit()
                .putString(SALT, Base64.encodeToString(
                    salt, Base64.NO_WRAP))
                .putString(HASH, Base64.encodeToString(
                    hash, Base64.NO_WRAP))
                .apply();

            return true;

        } catch (Exception e) {
            return false;
        }
    }

    public boolean verifyPin(String pin) {
        if (pin == null ||
            !pin.matches("[0-9]{4}|[0-9]{6}")) {
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

            byte[] actual = hashPin(pin, salt);

            return MessageDigest.isEqual(
                expected, actual
            );

        } catch (Exception e) {
            return false;
        }
    }

    public boolean hasPin() {
        return prefs.contains(HASH);
    }

    private byte[] hashPin(String pin, byte[] salt)
            throws Exception {

        PBEKeySpec spec = new PBEKeySpec(
            pin.toCharArray(),
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
