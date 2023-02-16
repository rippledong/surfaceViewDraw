package com.ripple.viewdraw;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dongbowen on 2018/7/25.
 */

public class CustomSurfaceView extends SurfaceView implements SurfaceHolder.Callback, Runnable {
    private SurfaceHolder mHolder;
    private Canvas mCanvas;
    private boolean isDrawing;
    private List<SurfaceViewDraw> mSurfaceViewDraws;
    private List<SurfaceViewTouch> mTouchEvents;

    private long currTime = 0;

    public CustomSurfaceView(Context context) {
        this(context, null);
    }

    public CustomSurfaceView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mHolder = getHolder();
        mHolder.addCallback(this);
        mSurfaceViewDraws = new ArrayList<>();
        mTouchEvents = new ArrayList<>();
        setFocusable(true);
        setFocusableInTouchMode(true);
        setKeepScreenOn(true);
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        isDrawing = true;
        new Thread(this).start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        isDrawing = false;
    }

    @Override
    public void run() {
        while (isDrawing) {
            currTime = System.currentTimeMillis();
            drawing();
            while ((mSurfaceViewDraws.size() == 0 ? SurfaceViewDraw.INTERVAL :
                    mSurfaceViewDraws.get(0).getInterval()) > System.currentTimeMillis() - currTime) {
                Thread.yield();
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (mSurfaceViewDraws.size() != 0) {
            for (int i = 0, j = mSurfaceViewDraws.size(); i < j; i++) {
                mSurfaceViewDraws.get(i).setWidthHeight(getMeasuredWidth(), getMeasuredHeight());
            }
        }
    }

    private void drawing() {
        try {
            mCanvas = mHolder.lockCanvas();
            for (int i = 0, j = mSurfaceViewDraws.size(); i < j; i++) {
                mSurfaceViewDraws.get(i).draw(mCanvas);
            }
        } finally {
            mHolder.unlockCanvasAndPost(mCanvas);
        }
    }

    public void setDraw(SurfaceViewDraw draw) {
        addDraw(draw);
    }

    public void addDraw(SurfaceViewDraw draw) {
        if (draw instanceof BaseSurfaceViewDraw) {
            ((BaseSurfaceViewDraw) draw).setView(this);
        }
        mSurfaceViewDraws.add(draw);
    }

    public void setEvent(SurfaceViewTouch event) {
        addEvent(event);
    }

    public void addEvent(SurfaceViewTouch event) {
        mTouchEvents.add(event);
    }

    public void postEvent(Object object) {
        for (int i = 0, j = mSurfaceViewDraws.size(); i < j; i++) {
            if (mSurfaceViewDraws.get(i) instanceof BaseSurfaceViewDraw) {
                ((BaseSurfaceViewDraw) mSurfaceViewDraws.get(i)).event(object);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (0 != mTouchEvents.size()) {
            boolean result = false;
            for (int i = 0, j = mTouchEvents.size(); i < j; i++) {
                result |= mTouchEvents.get(i).onTouchEvent(event);
            }
            return result;
        }
        return super.onTouchEvent(event);
    }
}
