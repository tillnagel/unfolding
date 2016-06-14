import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.utils.MapUtils;

Location berlinLocation = new Location(52.5f, 13.4f);
Location veniceLocation = new Location(45.44f, 12.34f);
Location lisbonLocation = new Location(38.71f, -9.14f);

UnfoldingMap map;

void setup() {
  size(800, 600, P2D);

  map = new UnfoldingMap(this, 100, 100, 600, 400);
  map.zoomAndPanTo(4, new Location(50.26f, 12.1f));
  MapUtils.createDefaultEventDispatcher(this, map);

  ImageMarker imgMarker1 = new ImageMarker(lisbonLocation, loadImage("ui/marker.png"));
  ImageMarker imgMarker2 = new ImageMarker(veniceLocation, loadImage("ui/marker_red.png"));
  ImageMarker imgMarker3 = new ImageMarker(berlinLocation, loadImage("ui/marker_gray.png"));
  map.addMarkers(imgMarker1, imgMarker2, imgMarker3);
}

void draw() {
  map.draw();
}