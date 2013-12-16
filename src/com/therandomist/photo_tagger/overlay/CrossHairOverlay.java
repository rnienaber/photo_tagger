package com.therandomist.photo_tagger.overlay;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.Projection;

public class CrossHairOverlay extends Overlay {

    public boolean draw(Canvas canvas, MapView mapView, boolean shadow, long when) {
        Projection projection = mapView.getProjection();
        Point center = projection.toPixels(mapView.getMapCenter(), null);

        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setColor(Color.parseColor("#E04232"));
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(2.0f);
        int innerRadius = 5;

        canvas.drawCircle(center.x, center.y, innerRadius, p);
        return true;
    }
}
