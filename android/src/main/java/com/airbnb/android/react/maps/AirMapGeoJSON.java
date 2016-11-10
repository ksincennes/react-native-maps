package com.airbnb.android.react.maps;

import android.content.Context;

import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.geojson.GeoJsonFeature;
import com.google.maps.android.geojson.GeoJsonLayer;
import com.google.maps.android.geojson.GeoJsonGeometry;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;
import android.util.Log;




public class AirMapGeoJSON extends AirMapFeature {

    private JSONObject feature;
    private GeoJsonLayer layer;
    private JSONObject styles;
    private GoogleMap mapObj;
    private Map<String, GeoJsonPolygonStyle> polyStyles;
    private Map<String, GeoJsonLineStringStyle> lineStyles;
    private Map<String, GeoJsonPointStyle> pointStyles;
    private boolean byProp;


    public AirMapGeoJSON(Context context) {
        super(context);
    }

    public void setGeoJSON(String geoJson) {     
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

    public void setStyles(String style) {
        if(styles != ""){            
            styles = new JSONObject(style);
        }
        if(layer != null && style.has("name") && style.has("styles")){
            List<GeoJsonFeature> featureList = layer.getFeatures();
            String key = style.getString("name")
            JSONObject styleMap = 
            for(GeoJsonFeature styledFeature : featureList){
                if(styledFeature.has(key)){
                    String value = styledFeature.getString(key)
                    if(styles.has(value)){
                        JSONObject style = styles.getJSONObject(value)
                        if(styledFeature.getString("type").match("Polygon|MultiPolygon|GeometryCollection")){

                            if(style)
                        } //else if()
                    }
                }
            }
        }
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

    public void setFillColor(int color) {
        this.fillColor = color;
        if (polygon != null) {
            polygon.setFillColor(color);
        }
    }

    public void setStrokeColor(int color) {
        this.strokeColor = color;
        if (polygon != null) {
            polygon.setStrokeColor(color);
        }
    }

    public void setStrokeWidth(float width) {
        this.strokeWidth = width;
        if (polygon != null) {
            polygon.setStrokeWidth(width);
        }
    }

    public void setGeodesic(boolean geodesic) {
        this.geodesic = geodesic;
        if (polygon != null) {
            polygon.setGeodesic(geodesic);
        }
    }

    public void setZIndex(float zIndex) {
        this.zIndex = zIndex;
        if (polygon != null) {
            polygon.setZIndex(zIndex);
        }
    }

    private void generateLineStringStyle(){

    }
    private void generatePolyStyle(){
        
    }


}
