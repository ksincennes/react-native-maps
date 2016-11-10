import React, { PropTypes } from 'react';
import {
  View,
} from 'react-native';
import decorateMapComponent, {
  USES_DEFAULT_IMPLEMENTATION,
  NOT_SUPPORTED,
} from './decorateMapComponent';

const propTypes = {
  ...View.propTypes,

  /**
   * string representation of the goeJSON
   */
  geoJSON: PropTypes.string,

  /**
   * Callback that is called when the user presses on the polygon
   */
  onPress: PropTypes.func,

  /**
   * The stroke width to use for the path.
   */
  strokeWidth: PropTypes.number,

  /**
   * The stroke color to use for the path.
   */
  strokeColor: PropTypes.string,

  /**
   * The fill color to use for the path.
   */
  fillColor: PropTypes.number,

  /**
   * The order in which this tile overlay is drawn with respect to other overlays. An overlay
   * with a larger z-index is drawn over overlays with smaller z-indices. The order of overlays
   * with the same z-index is arbitrary. The default zIndex is 0.
   *
   * @platform android
   */
  zIndex: PropTypes.number,

  /**
   * Boolean to indicate whether to draw each segment of the line as a geodesic as opposed to
   * straight lines on the Mercator projection. A geodesic is the shortest path between two
   * points on the Earth's surface. The geodesic curve is constructed assuming the Earth is
   * a sphere.
   *
   * @platform android
   */
  geodesic: PropTypes.bool,

  /**
   * json String representation of the styles to be applied to features in a feature collection.
   * @name name of key in properties object to style by
   * @styles map of name values to the style to use
   */
  styles: PropTypes.string,

  /**
   * byProp: if true, pass a styles string to style features, if false all features share a style
   */
  byProp: PropTypes.bool
};

const defaultProps = {
  strokeColor: parseInt('FFFF0000'),
  strokeWidth: 1,
  byProp: false
};

class MapGeoJson extends React.Component {
  render() {
    const AIRMapGeoJSON = this.getAirComponent();
    return (
      <AIRMapGeoJSON {...this.props} />
    );
  }
}

MapGeoJson.propTypes = propTypes;
MapGeoJson.defaultProps = defaultProps;

module.exports = decorateMapComponent(MapGeoJson, {
  componentType: 'GeoJSON',
  providers: {
    google: {
      ios: NOT_SUPPORTED,
      android: USES_DEFAULT_IMPLEMENTATION,
    },
  },
});
