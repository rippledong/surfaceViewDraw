package com.ripple.viewdraw.clearance;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.ripple.viewdraw.CustomSurfaceView;
import com.ripple.viewdraw.R;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


/**
 * @author : dongbowen
 * @time : 2019/05/07
 * @desc :
 * @version: 1.0
 */
public class ClearanceActivity extends AppCompatActivity {
    private RelativeLayout mTopLayout;
    private FrameLayout mContentLayout;
    private CustomSurfaceView mView;

    private ClearanceView mDraw;
    private ClearanceUtil mUtil;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common);
        int x, y;
        double percent;
        x = getIntent().getIntExtra("x", 16);
        y = getIntent().getIntExtra("y", 16);
        percent = getIntent().getDoubleExtra("percent", .3d);
        mTopLayout = findViewById(R.id.top_layout);
        mContentLayout = findViewById(R.id.content);
        mUtil = new ClearanceUtil(x, y, percent);
        mView = new CustomSurfaceView(this);
        mDraw = new ClearanceView(this, mUtil);
        mView.setDraw(mDraw);
        mView.setEvent(mDraw);
        mContentLayout.addView(mView, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void back(View view) {
        finish();
    }
}
