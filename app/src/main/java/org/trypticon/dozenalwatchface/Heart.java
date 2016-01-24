package org.trypticon.dozenalwatchface;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;

/**
 * Rendering of the heart shape.
 */
public class Heart {
    private final Path untransformedPath;
    private final Path path;
    private final Paint paint;

    Heart(Context context) {
        paint = new Paint();
        paint.setColor(Workarounds.getColor(context, R.color.heart_fill));
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);

        untransformedPath = new Path();
        untransformedPath.moveTo(20.5f, 9.5f);
        untransformedPath.rCubicTo(-1.955f, 0.0f, -3.83f, 1.268f, -4.5f, 3.0f);
        untransformedPath.rCubicTo(-0.67f, -1.732f, -2.547f, -3.0f, -4.5f, -3.0f);
        untransformedPath.cubicTo(8.957f, 9.5f, 7.0f, 11.432f, 7.0f, 14.0f);
        untransformedPath.rCubicTo(0.0f, 3.53f, 3.793f, 6.257f, 9.0f, 11.5f);
        untransformedPath.rCubicTo(5.207f, -5.242f, 9.0f, -7.97f, 9.0f, -11.5f);
        untransformedPath.cubicTo(25.0f, 11.432f, 23.043f, 9.5f, 20.5f, 9.5f);
        untransformedPath.close();

        path = new Path();
    }

    void updateBounds(float left, float top, float right, float bottom) {
        Matrix matrix = new Matrix();
        matrix.setScale((right - left) / 32.0f, (bottom - top) / 32.0f);
        matrix.postTranslate(left, top);
        untransformedPath.transform(matrix, path);
    }

    Path getPath() {
        return path;
    }

    void draw(Canvas canvas) {
        canvas.drawPath(path, paint);
    }
}
