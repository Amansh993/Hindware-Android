package com.example.hindware.utility;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;

import androidx.core.content.ContextCompat;

import me.dm7.barcodescanner.core.DisplayUtils;
import me.dm7.barcodescanner.core.ViewFinderView;

/**
 * Created by SandeepY on 16122020
 **/

public class QRCodeReaderViewFinderView extends ViewFinderView {

    private static final float PORTRAIT_WIDTH_RATIO = 0.75F;
    private static final float PORTRAIT_WIDTH_HEIGHT_RATIO = 0.75F;
    private static final float LANDSCAPE_HEIGHT_RATIO = 0.625F;
    private static final float LANDSCAPE_WIDTH_HEIGHT_RATIO = 1.4F;
    private static final int MIN_DIMENSION_DIFF = 50;
    private static final float SQUARE_DIMENSION_RATIO = 0.625F;
    private static final int POINT_SIZE = 10;
    private static final long ANIMATION_DELAY = 30;
    private static final int LASER_STEP = 10;
    private final int mDefaultLaserColor;
    private final int mDefaultMaskColor;
    private final int mDefaultBorderColor;
    private final int mDefaultBorderStrokeWidth;
    private final int mDefaultBorderLineLength;
    protected Paint mLaserPaint;
    protected Paint mFinderMaskPaint;
    protected Paint mBorderPaint;
    protected int mBorderLineLength;
    protected boolean mSquareViewFinder;
    private Rect mFramingRect, mLaserRect;
    private int currentYLaser;
    private boolean isLaserGoingUp = false;
    private Context mContext;

    public QRCodeReaderViewFinderView(Context context) {
        super(context);
        mContext = context;
        this.mDefaultLaserColor = ContextCompat.getColor(context, me.dm7.barcodescanner.core.R.color.viewfinder_laser);
        this.mDefaultMaskColor = ContextCompat.getColor(context, me.dm7.barcodescanner.core.R.color.viewfinder_mask);
        this.mDefaultBorderColor = ContextCompat.getColor(context, android.R.color.white);
        this.mDefaultBorderStrokeWidth = this.getResources()
                .getInteger(me.dm7.barcodescanner.core.R.integer.viewfinder_border_width);
        this.mDefaultBorderLineLength = this.getResources()
                .getInteger(me.dm7.barcodescanner.core.R.integer.viewfinder_border_length);
        this.init();
    }

    public QRCodeReaderViewFinderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mDefaultLaserColor = ContextCompat.getColor(context, me.dm7.barcodescanner.core.R.color.viewfinder_laser);
        this.mDefaultMaskColor = ContextCompat.getColor(context, me.dm7.barcodescanner.core.R.color.viewfinder_mask);
        this.mDefaultBorderColor = ContextCompat.getColor(context, android.R.color.white);
        this.mDefaultBorderStrokeWidth = this.getResources()
                .getInteger(me.dm7.barcodescanner.core.R.integer.viewfinder_border_width);
        this.mDefaultBorderLineLength = this.getResources()
                .getInteger(me.dm7.barcodescanner.core.R.integer.viewfinder_border_length);
        this.init();
    }

    private void init() {
        this.mLaserPaint = new Paint();
        this.mLaserPaint.setColor(this.mDefaultLaserColor);
        this.mLaserPaint.setStyle(Paint.Style.FILL);
        this.mFinderMaskPaint = new Paint();
        this.mFinderMaskPaint.setColor(this.mDefaultMaskColor);
        this.mBorderPaint = new Paint();
        this.mBorderPaint.setColor(this.mDefaultBorderColor);
        this.mBorderPaint.setStyle(Paint.Style.STROKE);
        this.mBorderPaint.setStrokeWidth((float) this.mDefaultBorderStrokeWidth);
        this.mBorderLineLength = this.mDefaultBorderLineLength;
    }

    public void setLaserColor(int laserColor) {
        this.mLaserPaint.setColor(laserColor);
    }

    public void setMaskColor(int maskColor) {
        this.mFinderMaskPaint.setColor(maskColor);
    }

    public void setBorderColor(int borderColor) {
        this.mBorderPaint.setColor(borderColor);
    }

    public void setBorderStrokeWidth(int borderStrokeWidth) {
        this.mBorderPaint.setStrokeWidth((float) borderStrokeWidth);
    }

    public void setBorderLineLength(int borderLineLength) {
        this.mBorderLineLength = borderLineLength;
    }

    public void setSquareViewFinder(boolean set) {
        this.mSquareViewFinder = set;
    }

    public void setupViewFinder() {
        this.updateFramingRect();
        this.invalidate();
    }

    public Rect getFramingRect() {
        return this.mFramingRect;
    }

    public void onDraw(Canvas canvas) {
        if (this.getFramingRect() != null) {
            this.drawViewFinderMask(canvas);
            this.drawViewFinderBorder(canvas);
            this.drawLaser(canvas);
        }
    }

    public synchronized void updateLaserRect() {
        Point viewResolution = new Point(getWidth(), getHeight());
        int width;
        int height;
        int orientation = DisplayUtils.getScreenOrientation(mContext);

        if (mSquareViewFinder) {
            if (orientation != Configuration.ORIENTATION_PORTRAIT) {
                height = (int) (getHeight() * SQUARE_DIMENSION_RATIO);
                width = height;
            } else {
                width = (int) (getWidth() * SQUARE_DIMENSION_RATIO);
                height = width;
            }
        } else {
            if (orientation != Configuration.ORIENTATION_PORTRAIT) {
                height = (int) (getHeight() * LANDSCAPE_HEIGHT_RATIO);
                width = (int) (LANDSCAPE_WIDTH_HEIGHT_RATIO * height);
            } else {
                width = (int) (getWidth() * PORTRAIT_WIDTH_RATIO);
                height = (int) (PORTRAIT_WIDTH_HEIGHT_RATIO * width);
            }
        }

        if (width > getWidth()) {
            width = getWidth() - MIN_DIMENSION_DIFF;
        }

        if (height > getHeight()) {
            height = getHeight() - MIN_DIMENSION_DIFF;
        }

        int leftOffset = (viewResolution.x - width) / 2;
        int topOffset = (viewResolution.y - height) / 2;
        currentYLaser = getHeight() / 2;
        mLaserRect = new Rect(leftOffset, topOffset, (leftOffset + width), topOffset + height);
    }

    public void drawViewFinderMask(Canvas canvas) {
        int width = canvas.getWidth();
        int height = canvas.getHeight();
        Rect framingRect = this.getFramingRect();
        canvas.drawRect(0.0F, 0.0F, (float) width, (float) framingRect.top, this.mFinderMaskPaint);
        canvas.drawRect(0.0F, (float) framingRect.top, (float) framingRect.left, (float) (framingRect.bottom + 1),
                this.mFinderMaskPaint);
        canvas.drawRect((float) (framingRect.right + 1), (float) framingRect.top, (float) width,
                (float) (framingRect.bottom + 1), this.mFinderMaskPaint);
        canvas.drawRect(0.0F, (float) (framingRect.bottom + 1), (float) width, (float) height, this.mFinderMaskPaint);
    }

    public void drawViewFinderBorder(Canvas canvas) {
        Rect framingRect = this.getFramingRect();
        canvas.drawLine((float) (framingRect.left - 1), (float) (framingRect.top - 1), (float) (framingRect.left - 1),
                (float) (framingRect.top - 1 + this.mBorderLineLength), this.mBorderPaint);
        canvas.drawLine((float) (framingRect.left - 1), (float) (framingRect.top - 1),
                (float) (framingRect.left - 1 + this.mBorderLineLength), (float) (framingRect.top - 1),
                this.mBorderPaint);
        canvas.drawLine((float) (framingRect.left - 1), (float) (framingRect.bottom + 1),
                (float) (framingRect.left - 1), (float) (framingRect.bottom + 1 - this.mBorderLineLength),
                this.mBorderPaint);
        canvas.drawLine((float) (framingRect.left - 1), (float) (framingRect.bottom + 1),
                (float) (framingRect.left - 1 + this.mBorderLineLength), (float) (framingRect.bottom + 1),
                this.mBorderPaint);
        canvas.drawLine((float) (framingRect.right + 1), (float) (framingRect.top - 1), (float) (framingRect.right + 1),
                (float) (framingRect.top - 1 + this.mBorderLineLength), this.mBorderPaint);
        canvas.drawLine((float) (framingRect.right + 1), (float) (framingRect.top - 1),
                (float) (framingRect.right + 1 - this.mBorderLineLength), (float) (framingRect.top - 1),
                this.mBorderPaint);
        canvas.drawLine((float) (framingRect.right + 1), (float) (framingRect.bottom + 1),
                (float) (framingRect.right + 1), (float) (framingRect.bottom + 1 - this.mBorderLineLength),
                this.mBorderPaint);
        canvas.drawLine((float) (framingRect.right + 1), (float) (framingRect.bottom + 1),
                (float) (framingRect.right + 1 - this.mBorderLineLength), (float) (framingRect.bottom + 1),
                this.mBorderPaint);
    }

    public void drawLaser(Canvas canvas) {
        Rect framingRect = mLaserRect;
        // Draw a red "laser scanner" line through the middle to show decoding is active
        int middle = currentYLaser;
        canvas.drawRect(framingRect.left + 2, middle - 1, framingRect.right - 1, middle + 2, mLaserPaint);
        if (getFramingRect() != null) {
            if (isLaserGoingUp) {
                if (currentYLaser > getFramingRect().top + 11) {
                    currentYLaser = currentYLaser - LASER_STEP;
                } else {
                    isLaserGoingUp = false;
                }
            } else {
                if (currentYLaser < getFramingRect().bottom - 11) {
                    currentYLaser = currentYLaser + LASER_STEP;
                } else {
                    isLaserGoingUp = true;
                }
            }
        }

        postInvalidateDelayed(ANIMATION_DELAY,
                framingRect.left - POINT_SIZE,
                framingRect.top - POINT_SIZE,
                framingRect.right + POINT_SIZE,
                framingRect.bottom + POINT_SIZE);
    }

    protected void onSizeChanged(int xNew, int yNew, int xOld, int yOld) {
        this.updateFramingRect();
        updateLaserRect();
    }

    public synchronized void updateFramingRect() {
        Point viewResolution = new Point(this.getWidth(), this.getHeight());
        int orientation = DisplayUtils.getScreenOrientation(mContext);
        int width;
        int height;
        if (this.mSquareViewFinder) {
            if (orientation != 1) {
                height = (int) ((float) this.getHeight() * 0.625F);
                width = height;
            } else {
                width = (int) ((float) this.getWidth() * 0.625F);
                height = width;
            }
        } else if (orientation != 1) {
            height = (int) ((float) this.getHeight() * 0.625F);
            width = (int) (1.4F * (float) height);
        } else {
            width = (int) ((float) this.getWidth() * 0.75F);
            height = (int) (0.75F * (float) width) + 150;
        }

        if (width > this.getWidth()) {
            width = this.getWidth() - 50;
        }

        if (height > this.getHeight()) {
            height = this.getHeight() - 50;
        }

        int leftOffset = (viewResolution.x - width) / 2;
        int topOffset = ((viewResolution.y - height) / 2) + 50;// + 250
        this.mFramingRect = new Rect(leftOffset, topOffset, leftOffset + width, topOffset + height);
    }
}
