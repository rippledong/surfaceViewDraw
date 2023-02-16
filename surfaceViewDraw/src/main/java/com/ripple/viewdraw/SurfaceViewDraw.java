package com.ripple.viewdraw;

import android.graphics.Canvas;

/**
 * Created by dongbowen on 2018/7/25.
 */

public interface SurfaceViewDraw {
    int INTERVAL = 50;

    int getInterval();

    void draw(Canvas canvas);

    void setWidthHeight(int width, int height);
}
