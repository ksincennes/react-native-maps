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

    public void setGeoJSON(String geoJson) {     package com.airbnb.android.react.maps;

import android.content.Context;

import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.geojson.GeoJsonFeature;
import com.google.maps.android.geojson.GeoJsonLayer;
import com.google.maps.android.geojson.GeoJsonGeometry;
import com.google.maps.android.geojson.GeoJsonPolygonStyle;
import com.google.maps.android.geojson.GeoJsonPointStyle;
import com.google.maps.android.geojson.GeoJsonLineStringStyle;

import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;
import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;
import android.util.Log;




public class AirMapGeoJSON extends AirMapFeature {

    private JSONObject feature;
    private GeoJsonLayer layer;
    private JSONObject styles;
    private GoogleMap mapObj;
    //Style maps for styling by a features properties
    private HashMap<String, GeoJsonPolygonStyle> polyStyles;
    private HashMap<String, GeoJsonLineStringStyle> lineStyles;
    private HashMap<String, GeoJsonPointStyle> pointStyles;
    //Styles that will apply to all features unless byProp is true.
    private GeoJsonPolygonStyle polyStyle;
    private GeoJsonPointStyle pointStyle;
    private GeoJsonLineStringStyle lineStyle;
    // Style geoJSON features by a property, allows one feature collection to have multiple features with different styles.
    private boolean byProp;
    private int strokeColor;
    private float strokeWidth;


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
                this.layer = new GeoJsonLayer(mapObj, feature);
                this.layer.addLayerToMap();
            }
        }
    }

    public void setStyles(String style) {
        if(style != ""){         
            try{   
                styles = new JSONObject(style);
            } catch (JSONException err){
                Log.e("Failed to get parse style string",err.getMessage());
            }
            generatePolyStyle(styles);
        }
    }



    @Override
    public Object getFeature() {
        return feature;
    }

    public void setByProp(boolean byProp){
        this.byProp = byProp;
    }

    @Override
    public void addToMap(GoogleMap map) {
        if( feature != null && feature.length() > 0){
            this.layer = new GeoJsonLayer(map, feature);
            styleLayers();
            this.layer.addLayerToMap();
        }
    }

    @Override
    public void removeFromMap(GoogleMap map) {
        if(this.layer != null){
            this.layer.removeLayerFromMap();
        }
    }



    private void styleLayers() {
        if(this.byProp && this.layer != null && styles.has("name") && styles.has("styles")){
            String key;
            try{
                key = styles.getString("name");
            } catch (JSONException err){
                Log.e("property \"name\" not set, cannot style", err.getMessage());
                return;
            }
            for(GeoJsonFeature styledFeature : this.layer.getFeatures()){
                if(styledFeature.hasProperty(key)){
                        String value = styledFeature.getProperty(key);
                        if(value == "3"){
                            Log.e("STYLEBROKENFEATURE", "feature is of type: " + styledFeature.getGeometry());
                            //Log.e("COORDINATES","number of co-ordinate arrays is: "+ styledFeature.getGeometry().Polygon.coordinates.length());
                        }
                        if(polyStyles != null && polyStyles.containsKey(value)){
                            styledFeature.setPolygonStyle(polyStyles.get(value));
                        } else{
                            Log.e("NOSTYLE", "Could not get style for: " + value);
                        }
                } else{
                    Log.e("STYLEFAIL", "feature did not have property: "+ key);
                }
            }
        } else {
            for(GeoJsonFeature styledFeature : this.layer.getFeatures()){
                if(this.polyStyle != null ){
                    styledFeature.setPolygonStyle(this.polyStyle);
                }
                if(this.pointStyle != null ){
                    styledFeature.setPointStyle(this.pointStyle);
                }
                if(this.lineStyle != null ){
                    styledFeature.setLineStringStyle(this.lineStyle);
                }
            }
        }
    }


/************
 * Functions to set style atributes that apply to all shapes, overridden if byProp is true
 * 
 */
    public void setFillColor(int color) {
        if(polyStyle == null){
            this.polyStyle = new GeoJsonPolygonStyle();
        }
        if(this.lineStyle == null){
            this.lineStyle = new GeoJsonLineStringStyle();
        }
        this.polyStyle.setFillColor(color);
    }

    public void setStrokeColor(int color) {
        this.strokeColor = color;
        if(this.polyStyle == null){
            this.polyStyle = new GeoJsonPolygonStyle();
        }
        if(this.lineStyle == null){
            this.lineStyle = new GeoJsonLineStringStyle();
        }
        this.polyStyle.setStrokeColor(color);
    }

    public void setStrokeWidth(float width) {
        this.strokeWidth = width;
        if(this.polyStyle == null){
            this.polyStyle = new GeoJsonPolygonStyle();
        }
        if(this.lineStyle == null){
            this.lineStyle = new GeoJsonLineStringStyle();
        }
        this.polyStyle.setStrokeWidth(width);
    }

    public void setGeodesic(boolean geodesic) {
        if(this.polyStyle == null){
            this.polyStyle = new GeoJsonPolygonStyle();
        }
        if(this.lineStyle == null){
            this.lineStyle = new GeoJsonLineStringStyle();
        }
        this.polyStyle.setGeodesic(geodesic);
    }

    public void setVisible(boolean visible) {
        if(this.polyStyle == null){
            this.polyStyle = new GeoJsonPolygonStyle();
        }
        if(this.pointStyle == null){
            this.pointStyle = new GeoJsonPointStyle();
        }
        if(this.lineStyle == null){
            this.lineStyle = new GeoJsonLineStringStyle();
        }
        this.polyStyle.setVisible(visible);
        this.pointStyle.setVisible(visible);
    }

    public void setZIndex(float zIndex) {
        if(this.polyStyle == null){
            this.polyStyle = new GeoJsonPolygonStyle();
        }
        if(this.lineStyle == null){
            this.lineStyle = new GeoJsonLineStringStyle();
        }
        this.polyStyle.setZIndex(zIndex);
    }

    public void setTitle(String title){
        if(this.pointStyle == null){
            this.pointStyle = new GeoJsonPointStyle();
        }
        this.pointStyle.setTitle(title);
    }

    public void setSnippet(String snippet){
        if(this.pointStyle == null){
            this.pointStyle = new GeoJsonPointStyle();
        }
        this.pointStyle.setSnippet(snippet);
    }

    public void setRotation(float rotation){
        if(this.pointStyle == null){
            this.pointStyle = new GeoJsonPointStyle();
        }
        this.pointStyle.setRotation(rotation);
    }

    public void setInforWindowAnchor(float infoWindowAnchorU, float infoWindowAnchorV){
        if(this.pointStyle == null){
            this.pointStyle = new GeoJsonPointStyle();
        }
        this.pointStyle.setInfoWindowAnchor(infoWindowAnchorU, infoWindowAnchorV);
    }

    public void setFlat(boolean isFlat){
        if(this.pointStyle == null){
            this.pointStyle = new GeoJsonPointStyle();
        }
        this.pointStyle.setFlat(isFlat);
    }

    public void setDraggable(boolean draggable){
        if(this.pointStyle == null){
            this.pointStyle = new GeoJsonPointStyle();
        }
        this.pointStyle.setDraggable(draggable);
    }

    private void generateLineStringStyle(){

    }
    private void generatePolyStyle(JSONObject styles){
        if(this.polyStyles == null){
            this.polyStyles = new HashMap<String, GeoJsonPolygonStyle>();
        }

        JSONObject styleMap;
        Iterator<String> keys;
        if(styles.has("styles")){
            try{
                styleMap = styles.getJSONObject("styles");
            } catch( JSONException err){
                Log.e("Failed styles parse", err.getMessage());
                return;
            }
            keys = styleMap.keys();
            while(keys.hasNext()){
                try{
                    String key = keys.next();
                    JSONObject styleObj = styleMap.getJSONObject(key);
                    GeoJsonPolygonStyle tmp = new GeoJsonPolygonStyle();
                    if(styleObj.has("fillColor"))
                        tmp.setFillColor(styleObj.getInt("fillColor"));
                    if(styleObj.has("strokeWidth"))
                        tmp.setStrokeWidth((float)styleObj.getDouble("strokeWidth"));
                    if(styleObj.has("strokeColor"))
                        tmp.setStrokeColor(styleObj.getInt("strokeColor"));
                    if(styleObj.has("geodesic"))
                        tmp.setGeodesic(styleObj.getBoolean("geodesic"));
                    if(tmp != null)
                        this.polyStyles.put(key, tmp);

                } catch(JSONException err){
                    Log.e("Failed to parse style", err.getMessage());
                }
            }
        }
        
    }


}

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
