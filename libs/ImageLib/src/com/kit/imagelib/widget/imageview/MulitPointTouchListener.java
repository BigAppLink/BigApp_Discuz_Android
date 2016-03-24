package com.kit.imagelib.widget.imageview;

import android.graphics.Matrix;
import android.graphics.PointF;
import android.util.FloatMath;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

public class MulitPointTouchListener implements OnTouchListener {

    Matrix matrix = new Matrix();
    Matrix savedMatrix = new Matrix();

    public ImageView image;
    static final int NONE = 0;
    static final int DRAG = 1;
    static final int ZOOM = 2;
    int mode = NONE;

    PointF start = new PointF();
    PointF mid = new PointF();
    float oldDist = 1f;

    public MulitPointTouchListener(ImageView image) {
        super();
        this.image = image;
        image.setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        this.image.setScaleType(ScaleType.MATRIX);
        // ImageView view = this.image;
        // ImageView view = (ImageView) v;
        // dumpEvent(event);
        try {
            switch (event.getAction() & MotionEvent.ACTION_MASK) {

                case MotionEvent.ACTION_DOWN:
                    // Log.w("FLAG", "ACTION_DOWN");
                    matrix.set(this.image.getImageMatrix());
                    savedMatrix.set(matrix);
                    start.set(event.getX(), event.getY());
                    mode = DRAG;
                    break;
                case MotionEvent.ACTION_POINTER_DOWN:
                    // Log.w("FLAG", "ACTION_POINTER_DOWN");
                    oldDist = spacing(event);
                    if (oldDist > 10f) {
                        savedMatrix.set(matrix);
                        midPoint(mid, event);
                        mode = ZOOM;
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    // Log.w("FLAG", "ACTION_UP");
                case MotionEvent.ACTION_POINTER_UP:
                    // Log.w("FLAG", "ACTION_POINTER_UP");
                    mode = NONE;
                    break;
                case MotionEvent.ACTION_MOVE:
                    // Log.w("FLAG", "ACTION_MOVE");
                    if (mode == DRAG) {
                        matrix.set(savedMatrix);
                        matrix.postTranslate(event.getX() - start.x, event.getY()
                                - start.y);
                    } else if (mode == ZOOM) {
                        float newDist = spacing(event);
                        if (newDist > 10f) {
                            matrix.set(savedMatrix);
                            float scale = newDist / oldDist;
                            matrix.postScale(scale, scale, mid.x, mid.y);
                        }
                    }
                    break;
            }

        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        this.image.setImageMatrix(matrix);
        return true;
    }

    private float spacing(MotionEvent event) {
        float x = 0;
        float y = 0;
        try {
            x = event.getX(0) - event.getX(1);
            y = event.getY(0) - event.getY(1);

        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return FloatMath.sqrt(x * x + y * y);

    }

    private void midPoint(PointF point, MotionEvent event) {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        point.set(x / 2, y / 2);
    }

    public void resetMatrix() {
        this.matrix = new Matrix();
        this.image.setImageMatrix(this.matrix);
        this.image.setScaleType(ScaleType.FIT_CENTER);
    }
}