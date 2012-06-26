package de.fhpotsdam.unfolding.data;

import java.util.ArrayList;
import java.util.List;

/**
 * A feature consisting of multiple features. Only contains one properties list.
 * 
 * Can be used for e.g. JSON's GeometryCollection, but also for MultiPolygons, etc.
 */
public class MultiFeature extends Feature {

	List<Feature> features = new ArrayList<Feature>();

	public MultiFeature() {
		super(FeatureType.MULTI);
	}

	public List<Feature> getFeatures() {
		return features;
	}

	public void setFeatures(List<Feature> features) {
		this.features = features;
	}
	
	public void addFeature(Feature feature) {
		this.features.add(feature);
	}

}
