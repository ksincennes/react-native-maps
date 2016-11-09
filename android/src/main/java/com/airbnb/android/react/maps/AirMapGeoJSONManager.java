package com.airbnb.android.react.maps;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.WindowManager;

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

    @ReactProp(name = "coordinates")
    public void setCoordinates(AirMapGeoJSON view, String coordinates) {
        view.setCoordinates(coordinates);
    }

    @ReactProp(name = "style")
    public void setStyle(AirMapGeoJSON view,  ReadableMap style) {
        view.setStyle(style);
    }
}
