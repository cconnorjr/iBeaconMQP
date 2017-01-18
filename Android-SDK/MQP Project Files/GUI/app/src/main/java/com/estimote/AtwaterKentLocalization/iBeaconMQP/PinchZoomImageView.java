package com.estimote.AtwaterKentLocalization.iBeaconMQP;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.widget.ImageView;

import java.io.IOException;

/**
 * Created by d4veg on 12/16/2016.
 */

public class PinchZoomImageView extends ImageView {

    private Bitmap bitmap;
    private int mImageHeight;
    private int mImageWidth;
    private static final float minZoom = 1.f;
    private static final float maxZoom = 3.f;
    private float scaleFactor = 1.f;
    private ScaleGestureDetector SGD;

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            scaleFactor = scaleFactor * detector.getScaleFactor();
            scaleFactor = Math.max(minZoom, Math.min(maxZoom, scaleFactor));
            invalidate();
            requestLayout();
            return super.onScale(detector);
        }
    }


    public PinchZoomImageView(Context context, AttributeSet attrs) {
        super(context, attrs);

        SGD = new ScaleGestureDetector(getContext(),new ScaleListener());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        SGD.onTouchEvent(event);
        return true;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
        super.onMeasure(widthMeasureSpec,heightMeasureSpec);

        int imageWidth = MeasureSpec.getSize(widthMeasureSpec);
        int imageHeight = MeasureSpec.getSize(heightMeasureSpec);
        int scaledWidth = (int) (Math.round(mImageWidth) * scaleFactor);
        int scaledHeight = (int) (Math.round(mImageHeight) * scaleFactor);

        setMeasuredDimension(
                Math.min(imageWidth, mImageWidth),
                Math.min(imageHeight, mImageHeight)
        );
    }

    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);

        canvas.save();
        canvas.scale(scaleFactor, scaleFactor,SGD.getFocusX(), SGD.getFocusY());
        canvas.drawBitmap(bitmap, 0, 0, null);
        canvas.restore();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh){
        super.onSizeChanged(w, h, oldw, oldh);
    }

    public void setImageUri(Uri uri){
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(),uri);
            float aspectRatio = (float) bitmap.getHeight() / (float) bitmap.getWidth();
            DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
            mImageWidth = displayMetrics.widthPixels;
            mImageHeight = Math.round(mImageWidth * aspectRatio);
            bitmap = Bitmap.createScaledBitmap(bitmap, mImageWidth, mImageHeight, false);
            invalidate();
            requestLayout();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
