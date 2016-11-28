package com.airbnb.android.react.maps;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import android.util.Log;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.ViewGroupManager;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.facebook.react.common.MapBuilder;

import com.google.maps.android.geojson.GeoJsonFeature;
import com.google.maps.android.geojson.GeoJsonLayer;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;


public class AirMapGeoJSONManager extends ViewGroupManager<AirMapGeoJSON> {
    private final DisplayMetrics metrics;

    public AirMapGeoJSONManager(ReactApplicationContext reactContext) {
        super();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            metrics = new DisplayMetrics();
            ((WindowManager) reactContext.getSystemService(Context.WINDOW_SERVICE))
                    .getDefaultDisplay()
                    .getRealMetrics(metrics);
        } else {
            metrics = reactContext.getResources().getDisplayMetrics();
        }
    }

    @Override
    public String getName() {
        return "AIRMapGeoJSON";
    }

    @Override
    public AirMapGeoJSON createViewInstance(ThemedReactContext context) {
        return new AirMapGeoJSON(context);
    }

    @ReactProp(name = "geoJSON")
    public void setGeoJSON(AirMapGeoJSON view, String geoJSON) {
        view.setGeoJSON(geoJSON);
    }

    @ReactProp(name = "styles")
    public void setStyle(AirMapGeoJSON view,  String style) {
        view.setStyles(style);
    }

    @ReactProp(name = "byProp")
    public void setByProp(AirMapGeoJSON view, boolean byProp) {
        view.setByProp(byProp);
    }

    @ReactProp(name = "fillColor")
    public void setFillColor(AirMapGeoJSON view, String color) {
        Log.e("INTPARSE", "parsing int into: " + (int) Long.parseLong(color, 16));
        view.setFillColor((int) Long.parseLong(color, 16));
    }

    @ReactProp(name = "strokeColor")
    public void setStrokeColor(AirMapGeoJSON view, String color) {
        Log.e("INTPARSE", "parsing int into: " + (int) Long.parseLong(color, 16));
        view.setStrokeColor((int) Long.parseLong(color, 16));
    }

    @ReactProp(name = "strokeWidth", defaultFloat = 1.0f)
    public void setStrokeWidth(AirMapGeoJSON view, float widthInPoints) {
        float widthInScreenPx = metrics.density * widthInPoints; // done for parity with iOS
        view.setStrokeWidth(widthInScreenPx);
    }

    @ReactProp(name = "geodesic", defaultBoolean = false)
    public void setGeodesic(AirMapGeoJSON view, boolean geodesic) {
        view.setGeodesic(geodesic);
    }

    @ReactProp(name = "visible", defaultBoolean = false)
    public void setVisible(AirMapGeoJSON view, boolean visible) {
        view.setVisible(visible);
    }

    @ReactProp(name = "zIndex", defaultFloat = 1.0f)
    public void setZIndex(AirMapGeoJSON view, float zIndex) {
        view.setZIndex(zIndex);
    }

    @ReactProp(name = "color")
    public void setColor(AirMapGeoJSON view, String color) {
        Log.e("INTPARSE", "parsing int into: " + (int) Long.parseLong(color, 16));
        view.setColor((int) Long.parseLong(color, 16));
    }

    @ReactProp(name = "clickable", defaultBoolean = true)
    public void setClickable(AirMapGeoJSON view, boolean clickable) {
        view.setClickable(clickable);
    }

    @ReactProp(name = "width", defaultFloat = 1.0f)
    public void setWidth(AirMapGeoJSON view, float width) {
        float widthInScreenPx = metrics.density * width; // done for parity with iOS
        view.setWidth(widthInScreenPx);
    }

    @ReactProp(name = "title")
    public void setTitle(AirMapGeoJSON view, String title) {
        view.setTitle(title);
    }

    @ReactProp(name = "snippet")
    public void setSnippet(AirMapGeoJSON view, String snippet) {
        view.setSnippet(snippet);
    }

    @ReactProp(name = "rotation", defaultFloat = 1.0f)
    public void setRotation(AirMapGeoJSON view, float rotation) {
        view.setRotation(rotation);
    }

    @ReactProp(name = "infoWindowAnchor")
    public void setInfoWindowAnchor(AirMapGeoJSON view, ReadableMap map) {
        // should default to (0.5, 1) (bottom middle)
        double x = map != null && map.hasKey("U") ? map.getDouble("U") : 0.5;
        double y = map != null && map.hasKey("V") ? map.getDouble("V") : 1.0;
        view.setInfoWindowAnchor((float) x, (float) y);
    }

    @ReactProp(name = "flat", defaultBoolean = false)
    public void setFlat(AirMapGeoJSON view, boolean flat) {
        view.setFlat(flat);
    }

    @ReactProp(name = "draggable", defaultBoolean = false)
    public void setDraggable(AirMapGeoJSON view, boolean draggable) {
        view.setDraggable(draggable);
    }

    @ReactProp(name = "icon")
    public void setIcon(AirMapGeoJSON view, String icon) {
        view.setIcon(icon);
    }
}
