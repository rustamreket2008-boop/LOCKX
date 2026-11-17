package com.lockx.app;

import android.os.Bundle;
import android.graphics.Color;
import android.content.res.ColorStateList;
import android.view.Gravity;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private final int BACKGROUND = Color.rgb(8, 15, 29);
    private final int BLUE = Color.rgb(22, 139, 255);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setStatusBarColor(BACKGROUND);
        getWindow().setNavigationBarColor(BACKGROUND);

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setGravity(Gravity.CENTER_HORIZONTAL);
        root.setPadding(dp(24), dp(40), dp(24), dp(24));
        root.setBackgroundColor(BACKGROUND);

        TextView logo = new TextView(this);
        logo.setText("LOCKX");
        logo.setTextSize(38);
        logo.setTextColor(BLUE);
        logo.setGravity(Gravity.CENTER);
        logo.setTypeface(null, 1);

        root.addView(logo);

        TextView subtitle = new TextView(this);
        subtitle.setText("Ваша безопасность — наш приоритет");
        subtitle.setTextSize(14);
        subtitle.setTextColor(Color.LTGRAY);
        subtitle.setGravity(Gravity.CENTER);

        LinearLayout.LayoutParams subParams =
            new LinearLayout.LayoutParams(
                -1, -2
            );
        subParams.topMargin = dp(10);
        root.addView(subtitle, subParams);

        addSpace(root, 45);

        addButton(root, "Мои приложения");
        addButton(root, "Настройки защиты");
        addButton(root, "Настройки");
        addButton(root, "О приложении");

        setContentView(root);
    }

    private void addButton(LinearLayout root, String text) {
        Button button = new Button(this);
        button.setText(text);
        button.setTextSize(16);
        button.setTextColor(Color.WHITE);
        button.setAllCaps(false);
        button.setBackgroundTintList(
            ColorStateList.valueOf(BLUE)
        );

        LinearLayout.LayoutParams params =
            new LinearLayout.LayoutParams(
                -1, dp(55)
            );

        params.topMargin = dp(14);

        root.addView(button, params);

        button.setOnClickListener(v ->
            Toast.makeText(
                this,
                "Этот раздел скоро будет доступен",
                Toast.LENGTH_SHORT
            ).show()
        );
    }

    private void addSpace(LinearLayout root, int height) {
        TextView space = new TextView(this);
        root.addView(
            space,
            new LinearLayout.LayoutParams(-1, dp(height))
        );
    }

    private int dp(int value) {
        return (int) (
            value * getResources()
                .getDisplayMetrics().density
        );
    }
}
