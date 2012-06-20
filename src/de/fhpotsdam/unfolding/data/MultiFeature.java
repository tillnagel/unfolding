package de.fhpotsdam.unfolding.data;

import java.util.ArrayList;
import java.util.List;

public class MultiFeature extends Feature {

	List<Feature> features = new ArrayList<Feature>();

	public MultiFeature(FeatureType type) {
		super(type);
	}

	public List<Feature> getFeatures() {
		return features;
	}

	public void setFeatures(List<Feature> features) {
		this.features = features;
	}
	
}
