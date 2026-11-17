package com.lockx.app;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashSet;
import java.util.Set;

public class AppSelection {

    private static final String PREFS = "lockx_selected_apps";
    private static final String KEY = "selected_packages";

    private final SharedPreferences preferences;

    public AppSelection(Context context) {
        preferences = context.getSharedPreferences(
            PREFS,
            Context.MODE_PRIVATE
        );
    }

    public boolean selectApp(String packageName) {
        if (packageName == null || packageName.isEmpty()) {
            return false;
        }

        Set<String> apps = getSelectedApps();
        apps.add(packageName);

        return preferences.edit()
            .putStringSet(KEY, apps)
            .commit();
    }

    public boolean removeApp(String packageName) {
        Set<String> apps = getSelectedApps();
        apps.remove(packageName);

        return preferences.edit()
            .putStringSet(KEY, apps)
            .commit();
    }

    public boolean isSelected(String packageName) {
        return getSelectedApps().contains(packageName);
    }

    public Set<String> getSelectedApps() {
        Set<String> saved =
            preferences.getStringSet(KEY, new HashSet<>());

        return new HashSet<>(saved);
    }

    public void clearSelection() {
        preferences.edit().remove(KEY).apply();
    }
}
