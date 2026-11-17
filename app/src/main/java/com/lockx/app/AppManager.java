package com.lockx.app;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class AppManager {

    private final Context context;
    private final PackageManager packageManager;

    public AppManager(Context context) {
        this.context = context;
        this.packageManager = context.getPackageManager();
    }

    public List<AppInfo> getInstalledApps() {

        List<AppInfo> apps = new ArrayList<>();

        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);

        List<ResolveInfo> results =
            packageManager.queryIntentActivities(intent, 0);

        for (ResolveInfo info : results) {

            String packageName =
                info.activityInfo.packageName;

            if (packageName.equals(context.getPackageName())) {
                continue;
            }

            String appName =
                info.loadLabel(packageManager).toString();

            AppInfo app = new AppInfo(
                appName,
                packageName
            );

            apps.add(app);
        }

        Collections.sort(apps,
            Comparator.comparing(
                app -> app.name.toLowerCase()
            )
        );

        return apps;
    }

    public static class AppInfo {

        public final String name;
        public final String packageName;

        public AppInfo(String name, String packageName) {
            this.name = name;
            this.packageName = packageName;
        }
    }
}
