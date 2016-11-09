package com.airbnb.android.react.maps;

import android.content.Context;

import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.geojson.GeoJsonFeature;
import com.google.maps.android.geojson.GeoJsonLayer;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;
import org.json.JSONException;
import android.util.Log;

public class AirMapGeoJSON extends AirMapFeature {

    private JSONObject feature;
    private GeoJsonLayer layer;
    private ReadableMap style;
    private GoogleMap mapObj;

    public AirMapGeoJSON(Context context) {
        super(context);
    }

    public void setCoordinates(String geoJson) {     
    Log.w("GEOJSONPARSE", geoJson);
        if (geoJson != "") {
            try{
                feature = new JSONObject(geoJson);
            } catch(JSONException err){
                Log.e("GEOJSONPARSE",err.getMessage());
            }
            if(mapObj != null){
                layer = new GeoJsonLayer(mapObj, feature);
                layer.addLayerToMap();
            }
        }
    }

    public void setStyle(ReadableMap style) {
        style = style;
       /* if (feature != null) {
            GeoJsonPolygonStyle polyStyle = new GeoJsonPolygonStyle()
            feature.getDefault
            polyStyle
            feature.setStrokeWidth(width);
        }*/
    }



    @Override
    public Object getFeature() {
        return feature;
    }

    @Override
    public void addToMap(GoogleMap map) {
        if( feature != null && feature.length() > 0){
            layer = new GeoJsonLayer(map, feature);
            layer.addLayerToMap();
        } else {
            mapObj = map;
        }
    }

    @Override
    public void removeFromMap(GoogleMap map) {
        if(layer != null){
            layer.removeLayerFromMap();
        }
    }
}
