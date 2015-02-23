package de.fhpotsdam.unfolding.data;

import java.util.List;

import processing.core.PApplet;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.utils.MapUtils;
import de.fhpotsdam.unfolding.utils.ScreenPosition;

public class PolygonWithHolesTestApp extends PApplet {

	String jsonString = "{\"type\":\"FeatureCollection\",\"features\":[{\"geometry\":{\"type\":\"Polygon\",\"coordinates\":[[[13.3960736,52.4986344],[13.3969122,52.4997383],[13.3971551,52.4996784],[13.3972029,52.4996666],[13.3970242,52.4994108],[13.3969239,52.4994371],[13.3968893,52.4993937],[13.3969923,52.4993676],[13.3968868,52.4992268],[13.3967771,52.4992567],[13.3966365,52.4990766],[13.3969638,52.4989774],[13.3970880,52.4989771],[13.3970944,52.4990449],[13.3974270,52.4990467],[13.3974311,52.4990731],[13.3982644,52.4990747],[13.3982642,52.4992622],[13.3974251,52.4992630],[13.3974166,52.4996138],[13.3975651,52.4995772],[13.3975765,52.4993472],[13.3979273,52.4993451],[13.3980096,52.4994675],[13.3984903,52.4993489],[13.3984270,52.4992604],[13.3984228,52.4987632],[13.3992533,52.4987664],[13.3994965,52.4991006],[13.3996454,52.4990639],[13.3996016,52.4990043],[13.3996825,52.4989815],[13.3997288,52.4990433],[13.3998845,52.4990049],[13.3995303,52.4985123],[13.3978147,52.4985011],[13.3962126,52.4984928],[13.3961271,52.4985798],[13.3960736,52.4986344]],[[13.3964318,52.4988082],[13.3966686,52.4987388],[13.3966699,52.4985923],[13.3968489,52.4985928],[13.3968487,52.4989015],[13.3965682,52.4989818],[13.3964318,52.4988082]],[[13.3963138,52.4986691],[13.3963977,52.4985922],[13.3965126,52.4985924],[13.3965138,52.4986680],[13.3964851,52.4986951],[13.3963592,52.4987263],[13.3963138,52.4986691]],[[13.3992472,52.4986696],[13.3992475,52.4986215],[13.3994126,52.4986240],[13.3994760,52.4987150],[13.3994069,52.4987338],[13.3992472,52.4986696]],[[13.3994572,52.4988134],[13.3995420,52.4987905],[13.3996138,52.4988897],[13.3995290,52.4989126],[13.3994572,52.4988134]],[[13.3989054,52.4986732],[13.3989064,52.4986171],[13.3990952,52.4986184],[13.3990943,52.4986745],[13.3989054,52.4986732]],[[13.3982833,52.4986723],[13.3982836,52.4986138],[13.3987367,52.4986146],[13.3987365,52.4986730],[13.3982833,52.4986723]],[[13.3970181,52.4986617],[13.3970185,52.4986026],[13.3974831,52.4986037],[13.3974827,52.4986628],[13.3970181,52.4986617]],[[13.3970126,52.4988774],[13.3970151,52.4987461],[13.3974792,52.4987495],[13.3974766,52.4988808],[13.3970126,52.4988774]]]},\"type\":\"Feature\",\"id\":\"fe98934308\",\"properties\":{\"kind\":\"residential\",\"name\":\"Deutsches Patent- und Markenamt  Technisches Informationszentrum Berlin\",\"area\":36309}}]}";
	String jsonStringNonClosed = "{\"type\":\"FeatureCollection\",\"features\":[{\"geometry\":{\"type\":\"Polygon\",\"coordinates\":[[[13.3960736,52.4986344],[13.3969122,52.4997383],[13.3971551,52.4996784],[13.3972029,52.4996666],[13.3970242,52.4994108],[13.3969239,52.4994371],[13.3968893,52.4993937],[13.3969923,52.4993676],[13.3968868,52.4992268],[13.3967771,52.4992567],[13.3966365,52.4990766],[13.3969638,52.4989774],[13.3970880,52.4989771],[13.3970944,52.4990449],[13.3974270,52.4990467],[13.3974311,52.4990731],[13.3982644,52.4990747],[13.3982642,52.4992622],[13.3974251,52.4992630],[13.3974166,52.4996138],[13.3975651,52.4995772],[13.3975765,52.4993472],[13.3979273,52.4993451],[13.3980096,52.4994675],[13.3984903,52.4993489],[13.3984270,52.4992604],[13.3984228,52.4987632],[13.3992533,52.4987664],[13.3994965,52.4991006],[13.3996454,52.4990639],[13.3996016,52.4990043],[13.3996825,52.4989815],[13.3997288,52.4990433],[13.3998845,52.4990049],[13.3995303,52.4985123],[13.3978147,52.4985011],[13.3962126,52.4984928],[13.3961271,52.4985798]],[[13.3964318,52.4988082],[13.3966686,52.4987388],[13.3966699,52.4985923],[13.3968489,52.4985928],[13.3968487,52.4989015],[13.3965682,52.4989818],[13.3964318,52.4988082]],[[13.3963138,52.4986691],[13.3963977,52.4985922],[13.3965126,52.4985924],[13.3965138,52.4986680],[13.3964851,52.4986951],[13.3963592,52.4987263],[13.3963138,52.4986691]],[[13.3992472,52.4986696],[13.3992475,52.4986215],[13.3994126,52.4986240],[13.3994760,52.4987150],[13.3994069,52.4987338],[13.3992472,52.4986696]],[[13.3994572,52.4988134],[13.3995420,52.4987905],[13.3996138,52.4988897],[13.3995290,52.4989126],[13.3994572,52.4988134]],[[13.3989054,52.4986732],[13.3989064,52.4986171],[13.3990952,52.4986184],[13.3990943,52.4986745],[13.3989054,52.4986732]],[[13.3982833,52.4986723],[13.3982836,52.4986138],[13.3987367,52.4986146],[13.3987365,52.4986730],[13.3982833,52.4986723]],[[13.3970181,52.4986617],[13.3970185,52.4986026],[13.3974831,52.4986037],[13.3974827,52.4986628],[13.3970181,52.4986617]],[[13.3970126,52.4988774],[13.3970151,52.4987461],[13.3974792,52.4987495],[13.3974766,52.4988808],[13.3970126,52.4988774]]]},\"type\":\"Feature\",\"id\":\"fe98934308\",\"properties\":{\"kind\":\"residential\",\"name\":\"Deutsches Patent- und Markenamt  Technisches Informationszentrum Berlin\",\"area\":36309}}]}";

	List<Feature> features;

	UnfoldingMap map;

	public void setup() {
		size(800, 600);

		features = GeoJSONReader.loadDataFromJSON(this, jsonString);
		for (Feature feature : features) {
			ShapeFeature shapeFeature = (ShapeFeature) feature;
			debugRingFeature(shapeFeature);
		}

		map = new UnfoldingMap(this);
		map.zoomAndPanTo(16, new Location(52.501, 13.395));
		MapUtils.createDefaultEventDispatcher(this, map);
	}

	public static void debugRingFeature(ShapeFeature shapeFeature) {
		println("locations #: " + shapeFeature.getLocations().size());
		println("has rings: " + (shapeFeature.getInteriorRings() != null));
		if (shapeFeature.getInteriorRings() != null) {
			println("rings #: " + shapeFeature.getInteriorRings().size());

			for (List<Location> interiorRing : shapeFeature.getInteriorRings()) {
				println("ring.locations #: " + interiorRing.size());
			}
		}

	}

	public void draw() {
		map.draw();

		for (Feature feature : features) {
			ShapeFeature shapeFeature = (ShapeFeature) feature;
			fill(255, 0, 0, 150);
			beginShape();

			// main shape
			beginContour();
			for (Location location : shapeFeature.getLocations()) {
				ScreenPosition pos = map.getScreenPosition(location);
				vertex(pos.x, pos.y);
			}
			endContour();

			// interior rings
			for (List<Location> interiorRing : shapeFeature.getInteriorRings()) {
				beginContour();
				for (Location location : interiorRing) {
					ScreenPosition pos = map.getScreenPosition(location);
					vertex(pos.x, pos.y);
				}
				endContour();
			}

			// Main shape is not closed in Java2D renderer if having interior rings.
			// See https://github.com/processing/processing/issues/3122
			endShape(CLOSE);
		}

		drawTest();
	}

	public void drawTest() {
		fill(0);
		for (Feature feature : features) {
			ShapeFeature shapeFeature = (ShapeFeature) feature;

			// main shape
			int i = 0;
			for (Location location : shapeFeature.getLocations()) {
				ScreenPosition pos = map.getScreenPosition(location);
				ellipse(pos.x, pos.y, 5, 5);
				text(i + "", pos.x, pos.y);
				i++;
			}

			// interior rings
			for (List<Location> interiorRing : shapeFeature.getInteriorRings()) {
				int j = 0;
				for (Location location : interiorRing) {
					ScreenPosition pos = map.getScreenPosition(location);
					ellipse(pos.x, pos.y, 5, 5);
					text(j + "", pos.x, pos.y);
					j++;
				}
			}

			// Main shape is not closed in Java2D renderer if having interior rings.
			// See https://github.com/processing/processing/issues/3122
		}
	}

}
