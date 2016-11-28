package com.airbnb.android.react.maps;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.view.View;
import android.widget.LinearLayout;
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
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.image.CloseableStaticBitmap;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

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
    private BitmapDescriptor iconBitmapDescriptor;
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
            generateMarkerStyle(styles);
            generateLineStringStyle(styles);
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
                        if(polyStyles != null && polyStyles.containsKey(value)){
                            styledFeature.setPolygonStyle(polyStyles.get(value));
                        } else{
                            Log.e("NOSTYLE", "Could not get style for: " + value);
                        }
                        if(lineStyles != null && lineStyles.containsKey(value)){
                            styledFeature.setLineStringStyle(lineStyles.get(value));
                        } else{
                            Log.e("NOSTYLE", "Could not get style for: " + value);
                        }
                        if(pointStyles != null && pointStyles.containsKey(value)){
                            styledFeature.setPointStyle(pointStyles.get(value));
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
 * Functions to set style atributes that apply to Polygons, overridden if byProp is true
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
        this.polyStyle.setStrokeColor(color);
    }

    public void setStrokeWidth(float width) {
        this.strokeWidth = width;
        if(this.polyStyle == null){
            this.polyStyle = new GeoJsonPolygonStyle();
        }
        this.polyStyle.setStrokeWidth(width);
    }


/************
 * Functions to set style atributes that apply to multiple geoJSON objects, overridden if byProp is true
 * 
 */
    public void setGeodesic(boolean geodesic) {
        if(this.polyStyle == null){
            this.polyStyle = new GeoJsonPolygonStyle();
        }
        if(this.lineStyle == null){
            this.lineStyle = new GeoJsonLineStringStyle();
        }
        this.polyStyle.setGeodesic(geodesic);
        this.lineStyle.setGeodesic(geodesic);
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
        this.lineStyle.setVisible(visible);
    }

    public void setZIndex(float zIndex) {
        if(this.polyStyle == null){
            this.polyStyle = new GeoJsonPolygonStyle();
        }
        if(this.lineStyle == null){
            this.lineStyle = new GeoJsonLineStringStyle();
        }
        this.polyStyle.setZIndex(zIndex);
        this.lineStyle.setZIndex(zIndex);
    }

/************
 * Functions to set style atributes that apply to Lines, overridden if byProp is true
 * 
 */
    public void setColor(int color){
        if(this.lineStyle == null){
            this.lineStyle = new GeoJsonLineStringStyle();
        }
        this.lineStyle.setColor(color);
    }

    public void setClickable(boolean clickable) {
        if(this.lineStyle == null){
            this.lineStyle = new GeoJsonLineStringStyle();
        }
        this.lineStyle.setVisible(clickable);
    }

    public void setWidth(float width) {
        if(this.lineStyle == null){
            this.lineStyle = new GeoJsonLineStringStyle();
        }
        this.lineStyle.setWidth(width);
    }

    /************************************
     * Marker specific settings
     */
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

    public void setInfoWindowAnchor(float infoWindowAnchorU, float infoWindowAnchorV){
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

    public void setIcon(String uri) {
        iconBitmapDescriptor = generageBitmap(uri);
        this.pointStyle.setIcon(iconBitmapDescriptor);
    }

    private BitmapDescriptor generageBitmap(String uri){
        if (uri == null) {
            return null;
        } else if (uri.startsWith("http://") || uri.startsWith("https://") ||
                uri.startsWith("file://")) {
            return null;
        } else {
            return getBitmapDescriptorByName(uri);
        }
    }

    /*************************************************************
     * Functions that generate style maps for everything
     */
    private void generateLineStringStyle(JSONObject styles){
        if(this.lineStyles == null){
            this.lineStyles = new HashMap<String, GeoJsonLineStringStyle>();
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
                    GeoJsonLineStringStyle tmp = new GeoJsonLineStringStyle();
                    if(styleObj.has("color"))
                        tmp.setColor(styleObj.getInt("color"));
                    if(styleObj.has("zIndex"))
                        tmp.setZIndex((float)styleObj.getDouble("zIndex"));
                    if(styleObj.has("width"))
                        tmp.setWidth((float)styleObj.getDouble("width"));
                    if(styleObj.has("geodesic"))
                        tmp.setGeodesic(styleObj.getBoolean("geodesic"));
                    if(styleObj.has("visible"))
                        tmp.setVisible(styleObj.getBoolean("visible"));
                    if(styleObj.has("clickable"))
                        tmp.setClickable(styleObj.getBoolean("clickable"));

                    if(tmp != null)
                        this.lineStyles.put(key, tmp);

                } catch(JSONException err){
                    Log.e("Failed to parse style", err.getMessage());
                }
            }
        }
    }

    private void generateMarkerStyle(JSONObject styles){
        if(this.pointStyles == null){
            this.pointStyles = new HashMap<String, GeoJsonPointStyle>();
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
                    GeoJsonPointStyle tmp = new GeoJsonPointStyle();
                    if(styleObj.has("snippet"))
                        tmp.setSnippet(styleObj.getString("snippet"));
                    if(styleObj.has("rotation"))
                        tmp.setRotation((float)styleObj.getDouble("rotation"));
                    if(styleObj.has("infoWindowAnchor")){
                        JSONObject windowAnchor = styleObj.getJSONObject("infoWindowAnchor");
                        if(windowAnchor.has("U") && windowAnchor.has("V"))
                            this.pointStyle.setInfoWindowAnchor((float)windowAnchor.getDouble("U"), (float)windowAnchor.getDouble("V"));
                    }
                    if(styleObj.has("flat"))
                        tmp.setFlat(styleObj.getBoolean("flat"));
                    if(styleObj.has("draggable"))
                        tmp.setDraggable(styleObj.getBoolean("draggable"));
                    if(styleObj.has("visible"))
                        tmp.setVisible(styleObj.getBoolean("visible"));
                    if(styleObj.has("title"))
                        tmp.setSnippet(styleObj.getString("title"));
                    if(tmp != null)
                        this.pointStyles.put(key, tmp);
                    if(styleObj.has("anchor")){
                        JSONObject windowAnchor = styleObj.getJSONObject("anchor");
                        if(windowAnchor.has("U") && windowAnchor.has("V"))
                            this.pointStyle.setInfoWindowAnchor((float)windowAnchor.getDouble("U"), (float)windowAnchor.getDouble("V"));
                    }
                    if(styleObj.has("alpha"))
                        tmp.setAlpha((float)styleObj.getDouble("alpha"));

                } catch(JSONException err){
                    Log.e("Failed to parse style", err.getMessage());
                }
            }
        }
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
                    if(styleObj.has("zIndex"))
                        tmp.setZIndex((float)styleObj.getDouble("zIndex"));
                    if(styleObj.has("visible"))
                        tmp.setGeodesic(styleObj.getBoolean("visible"));

                    if(tmp != null)
                        this.polyStyles.put(key, tmp);

                } catch(JSONException err){
                    Log.e("Failed to parse style", err.getMessage());
                }
            }
        }
        
    }

    private int getDrawableResourceByName(String name) {
        return getResources().getIdentifier(
                name,
                "drawable",
                getContext().getPackageName());
    }

    private BitmapDescriptor getBitmapDescriptorByName(String name) {
        return BitmapDescriptorFactory.fromResource(getDrawableResourceByName(name));
    }
}
