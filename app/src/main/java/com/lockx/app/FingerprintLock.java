package com.lockx.app;

import android.content.Context;

import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.fragment.app.FragmentActivity;
import androidx.core.content.ContextCompat;

public class FingerprintLock {

    private final FragmentActivity activity;

    public FingerprintLock(FragmentActivity activity) {
        this.activity = activity;
    }

    public boolean isAvailable() {
        BiometricManager manager =
            BiometricManager.from(activity);

        return manager.canAuthenticate(
            BiometricManager.Authenticators.BIOMETRIC_STRONG
        ) == BiometricManager.BIOMETRIC_SUCCESS;
    }

    public void authenticate(
        BiometricPrompt.AuthenticationCallback callback
    ) {

        BiometricPrompt prompt = new BiometricPrompt(
            activity,
            ContextCompat.getMainExecutor(activity),
            callback
        );

        BiometricPrompt.PromptInfo info =
            new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Подтверждение личности")
                .setSubtitle("Войдите в LOCKX")
                .setDescription("Используйте отпечаток пальца")
                .setNegativeButtonText("Отмена")
                .setAllowedAuthenticators(
                    BiometricManager.Authenticators.BIOMETRIC_STRONG
                )
                .build();

        prompt.authenticate(info);
    }
}
