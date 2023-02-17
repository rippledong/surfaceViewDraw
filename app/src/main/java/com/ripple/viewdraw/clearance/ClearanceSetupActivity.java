package com.ripple.viewdraw.clearance;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.ripple.viewdraw.R;

import androidx.annotation.Nullable;


/**
 * @author : dongbowen
 * @time : 2019/05/07
 * @desc :
 * @version: 1.0
 */
public class ClearanceSetupActivity extends Activity {
    private EditText x, y, percent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clearance_setup);
        x = findViewById(R.id.x);
        y = findViewById(R.id.y);
        percent = findViewById(R.id.percent);
        x.setText("16");
        y.setText("16");
        percent.setText("30");
    }

    public void start(View view) {
        int x = 16, y = 16;
        double percent = 30d / 100;
        try {
            x = Integer.valueOf(this.x.getText().toString());
            y = Integer.valueOf(this.y.getText().toString());
            percent = Double.valueOf(this.percent.getText().toString()) / 100;
        } catch (Exception e) {
            e.printStackTrace();
        }
        Intent intent = new Intent(this, ClearanceActivity.class);
        intent.putExtra("x", x);
        intent.putExtra("y", y);
        intent.putExtra("percent", percent);
        startActivity(intent);
    }
}
