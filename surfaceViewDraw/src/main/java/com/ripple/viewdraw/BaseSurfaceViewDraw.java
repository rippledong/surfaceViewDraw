package com.ripple.viewdraw;

/**
 * Created by dongbowen on 2018/11/6.
 */

public abstract class BaseSurfaceViewDraw implements SurfaceViewDraw {
    protected CustomSurfaceView mView;

    public void setView(CustomSurfaceView view) {
        mView = view;
    }

    public void postEvent(Object object) {
        if (null != mView) {
            mView.postEvent(object);
        }
    }

    public abstract void event(Object object);
}
