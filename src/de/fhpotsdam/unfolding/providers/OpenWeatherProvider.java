package de.fhpotsdam.unfolding.providers;

import de.fhpotsdam.unfolding.core.Coordinate;
import de.fhpotsdam.unfolding.geo.MercatorProjection;
import de.fhpotsdam.unfolding.geo.Transformation;

/**
 * Provider based on Leaflet-providers:
 * http://leaflet-extras.github.io/leaflet-providers/preview/index.html Map data
 * (c)OpenWeatherMap http://openweathermap.org
 */
public class OpenWeatherProvider {

    public static abstract class GenericOpenWeatherMapProvider extends AbstractMapTileUrlProvider {

        public GenericOpenWeatherMapProvider() {
            super(new MercatorProjection(26, new Transformation(1.068070779e7, 0.0, 3.355443185e7, 0.0,
                    -1.068070890e7, 3.355443057e7)));
        }

        public String getZoomString(Coordinate coordinate) {
            return (int) coordinate.zoom + "/" + (int) coordinate.column + "/" + (int) coordinate.row;
        }

        @Override
        public int tileWidth() {
            return 256;
        }

        @Override
        public int tileHeight() {
            return 256;
        }

        @Override
        public abstract String[] getTileUrls(Coordinate coordinate);
    }

    public static class Snow extends GenericOpenWeatherMapProvider {

        @Override
        public String[] getTileUrls(Coordinate coordinate) {
            String url = "http://tile.openweathermap.org/map/snow/" + getZoomString(coordinate) + ".png";
            return new String[]{url};
        }
    }

    public static class Temperature extends GenericOpenWeatherMapProvider {

        @Override
        public String[] getTileUrls(Coordinate coordinate) {
            String url = "http://tile.openweathermap.org/map/temp/" + getZoomString(coordinate) + ".png";
            return new String[]{url};
        }
    }

    public static class Wind extends GenericOpenWeatherMapProvider {

        @Override
        public String[] getTileUrls(Coordinate coordinate) {
            String url = "http://tile.openweathermap.org/map/wind/" + getZoomString(coordinate) + ".png";
            return new String[]{url};
        }
    }

    public static class PressureContour extends GenericOpenWeatherMapProvider {

        @Override
        public String[] getTileUrls(Coordinate coordinate) {
            String url = "http://tile.openweathermap.org/map/pressure_cntr/" + getZoomString(coordinate) + ".png";
            return new String[]{url};
        }
    }

    public static class Pressure extends GenericOpenWeatherMapProvider {

        @Override
        public String[] getTileUrls(Coordinate coordinate) {
            String url = "http://tile.openweathermap.org/map/pressure/" + getZoomString(coordinate) + ".png";
            return new String[]{url};
        }
    }

    public static class RainClassic extends GenericOpenWeatherMapProvider {

        @Override
        public String[] getTileUrls(Coordinate coordinate) {
            String url = "http://tile.openweathermap.org/map/rain_cls/" + getZoomString(coordinate) + ".png";
            return new String[]{url};
        }
    }

    public static class Rain extends GenericOpenWeatherMapProvider {

        @Override
        public String[] getTileUrls(Coordinate coordinate) {
            String url = "http://tile.openweathermap.org/map/rain/" + getZoomString(coordinate) + ".png";
            return new String[]{url};
        }
    }

    public static class PrecipitationClassic extends GenericOpenWeatherMapProvider {

        @Override
        public String[] getTileUrls(Coordinate coordinate) {
            String url = "http://tile.openweathermap.org/map/precipitation_cls/" + getZoomString(coordinate) + ".png";
            return new String[]{url};
        }
    }

    public static class Precipitation extends GenericOpenWeatherMapProvider {

        @Override
        public String[] getTileUrls(Coordinate coordinate) {
            String url = "http://tile.openweathermap.org/map/precipitation/" + getZoomString(coordinate) + ".png";
            return new String[]{url};
        }
    }

    public static class CloudsClassic extends GenericOpenWeatherMapProvider {

        @Override
        public String[] getTileUrls(Coordinate coordinate) {
            String url = "http://tile.openweathermap.org/map/clouds_cls/" + getZoomString(coordinate) + ".png";
            return new String[]{url};
        }
    }

    public static class Clouds extends GenericOpenWeatherMapProvider {

        @Override
        public String[] getTileUrls(Coordinate coordinate) {
            String url = "http://tile.openweathermap.org/map/clouds/" + getZoomString(coordinate) + ".png";
            return new String[]{url};
        }
    }
}
