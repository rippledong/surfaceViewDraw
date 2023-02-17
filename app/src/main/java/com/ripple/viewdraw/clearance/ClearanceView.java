package com.ripple.viewdraw.clearance;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.GestureDetector;
import android.view.MotionEvent;

import com.ripple.viewdraw.R;
import com.ripple.viewdraw.SurfaceViewDraw;
import com.ripple.viewdraw.SurfaceViewTouch;

import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

/**
 * @author : dongbowen
 * @time : 2019/05/07
 * @desc :
 * @version: 1.0
 */
public class ClearanceView implements SurfaceViewDraw, SurfaceViewTouch,
        GestureDetector.OnGestureListener {
    private final int INTERVAL = 50;
    private float scale = 1.0f;
    private float cW = 0, lineW, minCellW, maxCellW;
    private int x, y;
    private float px = 0, py = 0;
    private float width, height;
    private Paint linePaint;
    private boolean fail = false;

    private Context mContext;
    private ClearanceUtil mUtil;
    private int[][] mList;
    private Bitmap[] mImages;
    private GestureDetector mGesture;

    public ClearanceView(Context context, ClearanceUtil util) {
        mContext = context;
        mGesture = new GestureDetector(context, this);
        mUtil = util;
        mList = util.getVoidList();
        x = util.x;
        y = util.y;
        mImages = new Bitmap[13];
        mImages[0] = getBitmapFromVectorDrawable(context, R.drawable.clearance_void);
        mImages[1] = getBitmapFromVectorDrawable(context, R.drawable.clearance_1);
        mImages[2] = getBitmapFromVectorDrawable(context, R.drawable.clearance_2);
        mImages[3] = getBitmapFromVectorDrawable(context, R.drawable.clearance_3);
        mImages[4] = getBitmapFromVectorDrawable(context, R.drawable.clearance_4);
        mImages[5] = getBitmapFromVectorDrawable(context, R.drawable.clearance_5);
        mImages[6] = getBitmapFromVectorDrawable(context, R.drawable.clearance_6);
        mImages[7] = getBitmapFromVectorDrawable(context, R.drawable.clearance_7);
        mImages[8] = getBitmapFromVectorDrawable(context, R.drawable.clearance_8);
        mImages[9] = getBitmapFromVectorDrawable(context, R.drawable.clearance_9);
        mImages[10] = getBitmapFromVectorDrawable(context, R.drawable.clearance_flag);
        mImages[11] = getBitmapFromVectorDrawable(context, R.drawable.clearance_10);
        mImages[12] = getBitmapFromVectorDrawable(context, R.drawable.clearance_block);
        linePaint = new Paint();
        linePaint.setAntiAlias(true);
        linePaint.setColor(Color.parseColor("#456456"));
    }

    @Override
    public int getInterval() {
        return 50;
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawColor(Color.GRAY);
        lineW = cW * 9 / 10;
        startDraw(canvas);
    }

    private void startDraw(Canvas canvas) {
        //得到 绘制 的x,y 的起始坐标和结束坐标
        int startX = getX(px);
        int startY = getY(py);
        int endX = getX(px + width);
        int endY = getY(py + height);
        Matrix matrix = new Matrix();
        matrix.setScale(lineW / getBitmap(0).getWidth(), lineW / getBitmap(0).getHeight());
        //开始从起始坐标绘制 不能超过x,y
        for (int i = startX, j; i <= endX && i < x; i++) {
            for (j = startY; j <= endY && j < y; j++) {
                canvas.save();
                canvas.translate(getXpX(i), getYpY(j));
                canvas.drawBitmap(getBitmap(mList[i][j]), matrix, null);
                canvas.restore();
            }
        }
    }

    private float getXpX(int index) {
        return index * cW - px;
    }

    private float getYpY(int index) {
        return index * cW - py;
    }

    private int getX(float x) {
        return (int) (x / cW);
    }

    private int getY(float y) {
        return (int) (y / cW);
    }

    private Bitmap getBitmap(int value) {
        return mImages[value];
    }

    @Override
    public void setWidthHeight(int width, int height) {
        this.width = width;
        this.height = height;
        cW = width / x;
        cW = cW > height / y ? height / y : cW;
        minCellW = cW;
        maxCellW = width / 3;
    }

    private float baseValue, last_x, last_y;
    private boolean downIsVoid = false;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub
        // return ArtFilterActivity.this.mGestureDetector.onTouchEvent(event);
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            baseValue = 0;
            float x = last_x = event.getRawX();
            float y = last_y = event.getRawY();
//            if (mList[getX(x)][getY(y)] == 12) {
//                downIsVoid = true;
//            }
        } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
            if (event.getPointerCount() == 2) {
                float x = event.getX(0) - event.getX(1);
                float y = event.getY(0) - event.getY(1);
                float value = (float) Math.sqrt(x * x + y * y);// 计算两点的距离
                if (baseValue == 0) {
                    baseValue = value;
                } else {
                    if (value - baseValue >= 1) {
                        float scale = value / baseValue;// 当前两点间的距离除以手指落下时两点间的距离就是需要缩放的比例。
                        cW += scale * 10;  //缩放
                        if (cW >= maxCellW) {
                            cW = maxCellW;
                        }
                        return true;
                    } else if (value - baseValue <= -1) {
                        float scale = value / baseValue;// 当前两点间的距离除以手指落下时两点间的距离就是需要缩放的比例。
                        cW -= scale * 10;  //缩放
                        if (cW <= minCellW) {
                            cW = minCellW;
                        }
                        return true;
                    }
                }
            } else if (event.getPointerCount() == 1) {
                float x = event.getRawX();
                float y = event.getRawY();
                x -= last_x;
                y -= last_y;

//                if (x >= 10 || y >= 10 || x <= -10 || y <= -10) {
                px -= x; //移动图片位置
                py -= y;
                if (px < 0) {
                    px = 0;
                }
                if (py < 0) {
                    py = 0;
                }
//                }
                last_x = event.getRawX();
                last_y = event.getRawY();
                return true;
            }
        } else if (event.getAction() == MotionEvent.ACTION_UP) {

        }
        return mGesture.onTouchEvent(event);
    }

    public Bitmap getBitmapFromVectorDrawable(Context context, int drawableId) {
        Drawable drawable = ContextCompat.getDrawable(context, drawableId);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            drawable = (DrawableCompat.wrap(drawable)).mutate();
        }

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return true;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        if (fail) {
            return false;
        }
        int x = getX(px + motionEvent.getX());
        int y = getY(py + motionEvent.getY());
        if (x < 0 || x == this.x || y < 0 || y == this.y) {
            return false;
        }
        click(x, y);
        return true;
    }

    private void click(int x, int y) {
        if (x < 0 || x == this.x || y < 0 || y == this.y) {
            return;
        }
        if (mList[x][y] != 12) {
            return;
        }
        mList[x][y] = mUtil.get(x, y);
        if (mList[x][y] == 0) {
            click(x - 1, y);
            click(x, y - 1);
            click(x + 1, y);
            click(x, y + 1);
        }
        if (mList[x][y] == 9) {
            fail = true;
            mList = mUtil.mList;
            mList[x][y] = 11;
        }
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }
}
