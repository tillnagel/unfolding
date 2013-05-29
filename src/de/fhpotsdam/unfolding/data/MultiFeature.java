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

	/**
	 * Creates a new MultiFeature.
	 */
	public MultiFeature() {
		super(FeatureType.MULTI);
	}

	/**
	 * Returns all features of this multi-feature. The features can be of any type, even further MultiFeatures.
	 * 
	 * @return All features.
	 */
	public List<Feature> getFeatures() {
		return features;
	}

	/**
	 * Sets all features of this multi-feature.
	 * 
	 * @param features
	 *            The features.
	 */
	public void setFeatures(List<Feature> features) {
		this.features = features;
	}

	/**
	 * Adss a feature to this multi-feature.
	 * 
	 * @param feature
	 *            A feature.
	 */
	public void addFeature(Feature feature) {
		this.features.add(feature);
	}

}
