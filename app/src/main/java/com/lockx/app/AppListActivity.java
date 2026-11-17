package com.lockx.app;

import android.graphics.Color;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class AppListActivity extends AppCompatActivity {

    private final int BACKGROUND = Color.rgb(8, 15, 29);
    private final int BLUE = Color.rgb(22, 139, 255);

    private AppSelection selection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        selection = new AppSelection(this);

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(dp(20), dp(25), dp(20), dp(15));
        root.setBackgroundColor(BACKGROUND);

        TextView title = new TextView(this);
        title.setText("МОИ ПРИЛОЖЕНИЯ");
        title.setTextColor(BLUE);
        title.setTextSize(25);
        title.setGravity(Gravity.CENTER);
        title.setTypeface(null, 1);

        root.addView(title);

        TextView subtitle = new TextView(this);
        subtitle.setText("Выбери приложения для защиты");
        subtitle.setTextColor(Color.LTGRAY);
        subtitle.setTextSize(14);
        subtitle.setGravity(Gravity.CENTER);

        LinearLayout.LayoutParams subParams =
            new LinearLayout.LayoutParams(-1, -2);
        subParams.topMargin = dp(10);
        subParams.bottomMargin = dp(20);

        root.addView(subtitle, subParams);

        ScrollView scrollView = new ScrollView(this);

        LinearLayout list = new LinearLayout(this);
        list.setOrientation(LinearLayout.VERTICAL);

        AppManager manager = new AppManager(this);
        List<AppManager.AppInfo> apps = manager.getInstalledApps();

        if (apps.isEmpty()) {
            TextView empty = new TextView(this);
            empty.setText("Приложения не найдены");
            empty.setTextColor(Color.WHITE);
            empty.setTextSize(16);
            list.addView(empty);
        }

        for (AppManager.AppInfo app : apps) {

            CheckBox checkBox = new CheckBox(this);

            checkBox.setText(
                app.name + "\n" + app.packageName
            );

            checkBox.setTextColor(Color.WHITE);
            checkBox.setTextSize(14);
            checkBox.setButtonTintList(
                ColorStateList.valueOf(BLUE)
            );

            checkBox.setPadding(
                dp(8), dp(10), dp(8), dp(10)
            );

            checkBox.setChecked(
                selection.isSelected(app.packageName)
            );

            checkBox.setOnCheckedChangeListener(
                (button, checked) -> {

                    if (checked) {
                        selection.selectApp(app.packageName);
                        Toast.makeText(
                            this,
                            "Добавлено в защиту",
                            Toast.LENGTH_SHORT
                        ).show();
                    } else {
                        selection.removeApp(app.packageName);
                        Toast.makeText(
                            this,
                            "Удалено из защиты",
                            Toast.LENGTH_SHORT
                        ).show();
                    }
                }
            );

            list.addView(checkBox);
        }

        scrollView.addView(list);
        root.addView(
            scrollView,
            new LinearLayout.LayoutParams(-1, 0, 1)
        );

        setContentView(root);
    }

    private int dp(int value) {
        return (int) (
            value * getResources()
                .getDisplayMetrics().density
        );
    }
}
