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
import com.google.maps.android.geojson.GeoJsonFeature;
import com.google.maps.android.geojson.GeoJsonLayer;


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


    @ReactProp(name = "strokeWidth", defaultFloat = 1.0f)
    public void setStrokeWidth(AirMapGeoJSON view, float widthInPoints) {
        float widthInScreenPx = metrics.density * widthInPoints; // done for parity with iOS
        view.setStrokeWidth(widthInPoints);
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

    @ReactProp(name = "geodesic", defaultBoolean = false)
    public void setGeodesic(AirMapGeoJSON view, boolean geodesic) {
        view.setGeodesic(geodesic);
    }

    @ReactProp(name = "zIndex", defaultFloat = 1.0f)
    public void setZIndex(AirMapGeoJSON view, float zIndex) {
        view.setZIndex(zIndex);
    }
}
